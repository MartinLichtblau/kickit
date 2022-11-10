import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IContest } from 'app/shared/model/contest.model';
import { ContestMode } from 'app/shared/model/enumerations/contest-mode.model';
import { Location } from 'app/shared/model/enumerations/location.model';
import { Team } from 'app/shared/model/enumerations/team.model';
import { getEntity, updateEntity, createEntity, reset } from './contest.reducer';

export const ContestUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const contestEntity = useAppSelector(state => state.contest.entity);
  const loading = useAppSelector(state => state.contest.loading);
  const updating = useAppSelector(state => state.contest.updating);
  const updateSuccess = useAppSelector(state => state.contest.updateSuccess);
  const contestModeValues = Object.keys(ContestMode);
  const locationValues = Object.keys(Location);
  const teamValues = Object.keys(Team);

  const handleClose = () => {
    navigate('/contest');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...contestEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          contestMode: 'ONE',
          location: 'WUERZBURG',
          winnerTeam: 'T1',
          ...contestEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="kickitApp.contest.home.createOrEditLabel" data-cy="ContestCreateUpdateHeading">
            Create or edit a Contest
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="contest-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Date" id="contest-date" name="date" data-cy="date" type="date" />
              <ValidatedField label="Contest Mode" id="contest-contestMode" name="contestMode" data-cy="contestMode" type="select">
                {contestModeValues.map(contestMode => (
                  <option value={contestMode} key={contestMode}>
                    {contestMode}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Location" id="contest-location" name="location" data-cy="location" type="select">
                {locationValues.map(location => (
                  <option value={location} key={location}>
                    {location}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Winner Team" id="contest-winnerTeam" name="winnerTeam" data-cy="winnerTeam" type="select">
                {teamValues.map(team => (
                  <option value={team} key={team}>
                    {team}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/contest" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ContestUpdate;

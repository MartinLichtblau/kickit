import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IContest } from 'app/shared/model/contest.model';
import { getEntities as getContests } from 'app/entities/contest/contest.reducer';
import { IGame } from 'app/shared/model/game.model';
import { Location } from 'app/shared/model/enumerations/location.model';
import { Team } from 'app/shared/model/enumerations/team.model';
import { getEntity, updateEntity, createEntity, reset } from './game.reducer';

export const GameUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const contests = useAppSelector(state => state.contest.entities);
  const gameEntity = useAppSelector(state => state.game.entity);
  const loading = useAppSelector(state => state.game.loading);
  const updating = useAppSelector(state => state.game.updating);
  const updateSuccess = useAppSelector(state => state.game.updateSuccess);
  const locationValues = Object.keys(Location);
  const teamValues = Object.keys(Team);

  const handleClose = () => {
    navigate('/game');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getContests({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...gameEntity,
      ...values,
      contest: contests.find(it => it.id.toString() === values.contest.toString()),
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
          location: 'WUERZBURG',
          winnerTeam: 'T1',
          ...gameEntity,
          contest: gameEntity?.contest?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="kickitApp.game.home.createOrEditLabel" data-cy="GameCreateUpdateHeading">
            Create or edit a Game
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="game-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Location" id="game-location" name="location" data-cy="location" type="select">
                {locationValues.map(location => (
                  <option value={location} key={location}>
                    {location}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Winner Team" id="game-winnerTeam" name="winnerTeam" data-cy="winnerTeam" type="select">
                {teamValues.map(team => (
                  <option value={team} key={team}>
                    {team}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="game-contest" name="contest" data-cy="contest" label="Contest" type="select">
                <option value="" key="0" />
                {contests
                  ? contests.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/game" replace color="info">
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

export default GameUpdate;

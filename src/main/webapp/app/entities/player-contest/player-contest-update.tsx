import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPlayer } from 'app/shared/model/player.model';
import { getEntities as getPlayers } from 'app/entities/player/player.reducer';
import { IContest } from 'app/shared/model/contest.model';
import { getEntities as getContests } from 'app/entities/contest/contest.reducer';
import { IPlayerContest } from 'app/shared/model/player-contest.model';
import { Team } from 'app/shared/model/enumerations/team.model';
import { getEntity, updateEntity, createEntity, reset } from './player-contest.reducer';

export const PlayerContestUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const players = useAppSelector(state => state.player.entities);
  const contests = useAppSelector(state => state.contest.entities);
  const playerContestEntity = useAppSelector(state => state.playerContest.entity);
  const loading = useAppSelector(state => state.playerContest.loading);
  const updating = useAppSelector(state => state.playerContest.updating);
  const updateSuccess = useAppSelector(state => state.playerContest.updateSuccess);
  const teamValues = Object.keys(Team);

  const handleClose = () => {
    navigate('/player-contest');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPlayers({}));
    dispatch(getContests({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...playerContestEntity,
      ...values,
      player: players.find(it => it.id.toString() === values.player.toString()),
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
          team: 'T1',
          ...playerContestEntity,
          player: playerContestEntity?.player?.id,
          contest: playerContestEntity?.contest?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="kickitApp.playerContest.home.createOrEditLabel" data-cy="PlayerContestCreateUpdateHeading">
            Create or edit a Player Contest
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="player-contest-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Team" id="player-contest-team" name="team" data-cy="team" type="select">
                {teamValues.map(team => (
                  <option value={team} key={team}>
                    {team}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="player-contest-player" name="player" data-cy="player" label="Player" type="select">
                <option value="" key="0" />
                {players
                  ? players.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="player-contest-contest" name="contest" data-cy="contest" label="Contest" type="select">
                <option value="" key="0" />
                {contests
                  ? contests.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/player-contest" replace color="info">
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

export default PlayerContestUpdate;

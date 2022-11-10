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
import { IGame } from 'app/shared/model/game.model';
import { getEntities as getGames } from 'app/entities/game/game.reducer';
import { IPlayerGame } from 'app/shared/model/player-game.model';
import { PlayerPosition } from 'app/shared/model/enumerations/player-position.model';
import { getEntity, updateEntity, createEntity, reset } from './player-game.reducer';

export const PlayerGameUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const players = useAppSelector(state => state.player.entities);
  const games = useAppSelector(state => state.game.entities);
  const playerGameEntity = useAppSelector(state => state.playerGame.entity);
  const loading = useAppSelector(state => state.playerGame.loading);
  const updating = useAppSelector(state => state.playerGame.updating);
  const updateSuccess = useAppSelector(state => state.playerGame.updateSuccess);
  const playerPositionValues = Object.keys(PlayerPosition);

  const handleClose = () => {
    navigate('/player-game');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPlayers({}));
    dispatch(getGames({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...playerGameEntity,
      ...values,
      player: players.find(it => it.id.toString() === values.player.toString()),
      game: games.find(it => it.id.toString() === values.game.toString()),
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
          playerPosition: 'FRONT',
          ...playerGameEntity,
          player: playerGameEntity?.player?.id,
          game: playerGameEntity?.game?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="kickitApp.playerGame.home.createOrEditLabel" data-cy="PlayerGameCreateUpdateHeading">
            Create or edit a Player Game
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="player-game-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Player Position"
                id="player-game-playerPosition"
                name="playerPosition"
                data-cy="playerPosition"
                type="select"
              >
                {playerPositionValues.map(playerPosition => (
                  <option value={playerPosition} key={playerPosition}>
                    {playerPosition}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="player-game-player" name="player" data-cy="player" label="Player" type="select">
                <option value="" key="0" />
                {players
                  ? players.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="player-game-game" name="game" data-cy="game" label="Game" type="select">
                <option value="" key="0" />
                {games
                  ? games.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/player-game" replace color="info">
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

export default PlayerGameUpdate;

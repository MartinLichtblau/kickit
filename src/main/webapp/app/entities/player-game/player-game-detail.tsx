import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './player-game.reducer';

export const PlayerGameDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const playerGameEntity = useAppSelector(state => state.playerGame.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="playerGameDetailsHeading">Player Game</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{playerGameEntity.id}</dd>
          <dt>
            <span id="playerPosition">Player Position</span>
          </dt>
          <dd>{playerGameEntity.playerPosition}</dd>
          <dt>Player</dt>
          <dd>{playerGameEntity.player ? playerGameEntity.player.id : ''}</dd>
          <dt>Game</dt>
          <dd>{playerGameEntity.game ? playerGameEntity.game.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/player-game" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/player-game/${playerGameEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PlayerGameDetail;

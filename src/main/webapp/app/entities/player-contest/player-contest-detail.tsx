import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './player-contest.reducer';

export const PlayerContestDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const playerContestEntity = useAppSelector(state => state.playerContest.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="playerContestDetailsHeading">Player Contest</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{playerContestEntity.id}</dd>
          <dt>
            <span id="team">Team</span>
          </dt>
          <dd>{playerContestEntity.team}</dd>
          <dt>Player</dt>
          <dd>{playerContestEntity.player ? playerContestEntity.player.id : ''}</dd>
          <dt>Contest</dt>
          <dd>{playerContestEntity.contest ? playerContestEntity.contest.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/player-contest" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/player-contest/${playerContestEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PlayerContestDetail;

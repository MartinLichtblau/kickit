import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './contest.reducer';

export const ContestDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const contestEntity = useAppSelector(state => state.contest.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contestDetailsHeading">Contest</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{contestEntity.id}</dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>{contestEntity.date ? <TextFormat value={contestEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="contestMode">Contest Mode</span>
          </dt>
          <dd>{contestEntity.contestMode}</dd>
          <dt>
            <span id="location">Location</span>
          </dt>
          <dd>{contestEntity.location}</dd>
          <dt>
            <span id="winnerTeam">Winner Team</span>
          </dt>
          <dd>{contestEntity.winnerTeam}</dd>
        </dl>
        <Button tag={Link} to="/contest" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/contest/${contestEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContestDetail;

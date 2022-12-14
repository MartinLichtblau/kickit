import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IContest } from 'app/shared/model/contest.model';
import { getEntities } from './contest.reducer';

export const Contest = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const contestList = useAppSelector(state => state.contest.entities);
  const loading = useAppSelector(state => state.contest.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="contest-heading" data-cy="ContestHeading">
        Contests
        <div className="d-flex justify-content-end">
          <Link to="/contest/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Contest
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {contestList && contestList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Date</th>
                <th>Contest Mode</th>
                <th>Location</th>
                <th>Winner Team</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {contestList.map((contest, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/contest/${contest.id}`} color="link" size="sm">
                      {contest.id}
                    </Button>
                  </td>
                  <td>{contest.date ? <TextFormat type="date" value={contest.date} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{contest.contestMode}</td>
                  <td>{contest.location}</td>
                  <td>{contest.winnerTeam}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/contest/${contest.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/contest/${contest.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/contest/${contest.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Contests found</div>
        )}
      </div>
    </div>
  );
};

export default Contest;

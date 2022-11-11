import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPlayerContest } from 'app/shared/model/player-contest.model';
import { getEntities } from './player-contest.reducer';

export const PlayerContest = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const playerContestList = useAppSelector(state => state.playerContest.entities);
  const loading = useAppSelector(state => state.playerContest.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="player-contest-heading" data-cy="PlayerContestHeading">
        {location.pathname == '/contest/1056/edit' ? 'Contest Player' : 'Player Contests'}
        <div className="d-flex justify-content-end">
          <Link to="/player-contest/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Player Contest
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {playerContestList && playerContestList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Team</th>
                <th>Player</th>
                <th>Contest</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {playerContestList.map((playerContest, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/player-contest/${playerContest.id}`} color="link" size="sm">
                      {playerContest.id}
                    </Button>
                  </td>
                  <td>{playerContest.team}</td>
                  <td>{playerContest.player ? <Link to={`/player/${playerContest.player.id}`}>{playerContest.player.name}</Link> : ''}</td>
                  <td>
                    {playerContest.contest ? <Link to={`/contest/${playerContest.contest.id}`}>{playerContest.contest.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/player-contest/${playerContest.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/player-contest/${playerContest.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/player-contest/${playerContest.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Player Contests found</div>
        )}
      </div>
    </div>
  );
};

export default PlayerContest;

import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Row>
        <Col md="8">
          <h2>Mayflower Football Contests</h2>
          {account?.login ? (
            <div></div>
          ) : (
            <div>
              <Alert color="warning">
                If you want to
                <span>&nbsp;</span>
                <Link to="/login" className="alert-link">
                  sign in
                </Link>
                , you can try the default accounts:
                <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;) <br />- User (login=&quot;user&quot; and
                password=&quot;user&quot;).
              </Alert>

              <Alert color="warning">
                You don&apos;t have an account yet?&nbsp;
                <Link to="/account/register" className="alert-link">
                  Register a new account
                </Link>
              </Alert>
            </div>
          )}
        </Col>
        <Col md="4">
          <Link to="/player/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; New Player
          </Link>
          <Link to="/contest/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Start Contest
          </Link>
        </Col>
      </Row>
      <Row>
        <Col md="6" className="pad">
          <h3>Latest Results</h3>
        </Col>
        <Col md="6">
          <h3>Leaderboard</h3>
        </Col>
      </Row>
    </Row>
  );
};

export default Home;

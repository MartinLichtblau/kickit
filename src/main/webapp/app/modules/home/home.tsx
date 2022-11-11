import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import ContestList from 'app/entities/contest';
import PlayerList from 'app/entities/player';

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
      </Row>
      <Row className="pad"></Row>
      <Row>
        <Col md="6">
          <h3>Latest Results</h3>
          <Row> {<ContestList />} </Row>
        </Col>
        <Col md="6">
          <h3>Leaderboard</h3>
          <Row> {<PlayerList />} </Row>
        </Col>
      </Row>
    </Row>
  );
};

export default Home;

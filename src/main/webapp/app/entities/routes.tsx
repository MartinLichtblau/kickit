import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Player from './player';
import Contest from './contest';
import PlayerContest from './player-contest';
import Game from './game';
import PlayerGame from './player-game';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="player/*" element={<Player />} />
        <Route path="contest/*" element={<Contest />} />
        <Route path="player-contest/*" element={<PlayerContest />} />
        <Route path="game/*" element={<Game />} />
        <Route path="player-game/*" element={<PlayerGame />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};

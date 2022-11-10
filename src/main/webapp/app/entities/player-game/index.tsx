import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PlayerGame from './player-game';
import PlayerGameDetail from './player-game-detail';
import PlayerGameUpdate from './player-game-update';
import PlayerGameDeleteDialog from './player-game-delete-dialog';

const PlayerGameRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PlayerGame />} />
    <Route path="new" element={<PlayerGameUpdate />} />
    <Route path=":id">
      <Route index element={<PlayerGameDetail />} />
      <Route path="edit" element={<PlayerGameUpdate />} />
      <Route path="delete" element={<PlayerGameDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PlayerGameRoutes;

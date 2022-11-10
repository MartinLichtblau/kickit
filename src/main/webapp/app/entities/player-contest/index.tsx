import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PlayerContest from './player-contest';
import PlayerContestDetail from './player-contest-detail';
import PlayerContestUpdate from './player-contest-update';
import PlayerContestDeleteDialog from './player-contest-delete-dialog';

const PlayerContestRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PlayerContest />} />
    <Route path="new" element={<PlayerContestUpdate />} />
    <Route path=":id">
      <Route index element={<PlayerContestDetail />} />
      <Route path="edit" element={<PlayerContestUpdate />} />
      <Route path="delete" element={<PlayerContestDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PlayerContestRoutes;

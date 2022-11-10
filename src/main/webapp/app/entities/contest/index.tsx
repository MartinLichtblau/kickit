import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Contest from './contest';
import ContestDetail from './contest-detail';
import ContestUpdate from './contest-update';
import ContestDeleteDialog from './contest-delete-dialog';

const ContestRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Contest />} />
    <Route path="new" element={<ContestUpdate />} />
    <Route path=":id">
      <Route index element={<ContestDetail />} />
      <Route path="edit" element={<ContestUpdate />} />
      <Route path="delete" element={<ContestDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ContestRoutes;

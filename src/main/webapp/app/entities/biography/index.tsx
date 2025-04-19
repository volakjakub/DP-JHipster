import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Biography from './biography';
import BiographyDetail from './biography-detail';
import BiographyUpdate from './biography-update';
import BiographyDeleteDialog from './biography-delete-dialog';

const BiographyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Biography />} />
    <Route path="new" element={<BiographyUpdate />} />
    <Route path=":id">
      <Route index element={<BiographyDetail />} />
      <Route path="edit" element={<BiographyUpdate />} />
      <Route path="delete" element={<BiographyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BiographyRoutes;

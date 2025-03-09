import React from 'react';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { Route } from 'react-router';
import Biography from './biography';
import Education from './education';
import Language from './language';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="biography/*" element={<Biography />} />
        <Route path="education/*" element={<Education />} />
        <Route path="language/*" element={<Language />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};

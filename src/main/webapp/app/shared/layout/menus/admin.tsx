import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

const adminMenuItems = () => (
  <>
    <MenuItem icon="users" to="/admin/user-management">
      Správa uživatelů
    </MenuItem>
    <MenuItem icon="tachometer-alt" to="/admin/metrics">
      Metriky
    </MenuItem>
    <MenuItem icon="heart" to="/admin/health">
      Stav systému
    </MenuItem>
    <MenuItem icon="cogs" to="/admin/configuration">
      Konfigurace
    </MenuItem>
    <MenuItem icon="tasks" to="/admin/logs">
      Logy
    </MenuItem>
    {/* jhipster-needle-add-element-to-admin-menu - JHipster will add entities to the admin menu here */}
  </>
);

const openAPIItem = () => (
  <MenuItem icon="book" to="/admin/docs">
    API
  </MenuItem>
);

export const AdminMenu = ({ showOpenAPI }) => (
  <NavDropdown icon="users-cog" name="Administrace" id="admin-menu" data-cy="adminMenu">
    {adminMenuItems()}
    {showOpenAPI && openAPIItem()}
  </NavDropdown>
);

export default AdminMenu;

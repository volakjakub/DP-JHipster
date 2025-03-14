import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/biography">
        Životopisy
      </MenuItem>
      <MenuItem icon="asterisk" to="/education">
        Vzdělání
      </MenuItem>
      <MenuItem icon="asterisk" to="/language">
        Jazyky
      </MenuItem>
      <MenuItem icon="asterisk" to="/skill">
        Dovednosti
      </MenuItem>
      <MenuItem icon="asterisk" to="/project">
        Projekty
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;

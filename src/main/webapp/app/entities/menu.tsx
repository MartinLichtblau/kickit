import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/player">
        Player
      </MenuItem>
      <MenuItem icon="asterisk" to="/contest">
        Contest
      </MenuItem>
      <MenuItem icon="asterisk" to="/player-contest">
        Player Contest
      </MenuItem>
      <MenuItem icon="asterisk" to="/game">
        Game
      </MenuItem>
      <MenuItem icon="asterisk" to="/player-game">
        Player Game
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;

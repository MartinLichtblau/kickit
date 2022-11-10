import player from 'app/entities/player/player.reducer';
import contest from 'app/entities/contest/contest.reducer';
import playerContest from 'app/entities/player-contest/player-contest.reducer';
import game from 'app/entities/game/game.reducer';
import playerGame from 'app/entities/player-game/player-game.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  player,
  contest,
  playerContest,
  game,
  playerGame,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;

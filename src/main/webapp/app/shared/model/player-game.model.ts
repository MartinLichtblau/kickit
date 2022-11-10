import { IPlayer } from 'app/shared/model/player.model';
import { IGame } from 'app/shared/model/game.model';
import { PlayerPosition } from 'app/shared/model/enumerations/player-position.model';

export interface IPlayerGame {
  id?: number;
  playerPosition?: PlayerPosition;
  player?: IPlayer | null;
  game?: IGame | null;
}

export const defaultValue: Readonly<IPlayerGame> = {};

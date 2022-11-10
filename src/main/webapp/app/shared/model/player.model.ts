import { IPlayerGame } from 'app/shared/model/player-game.model';
import { IPlayerContest } from 'app/shared/model/player-contest.model';
import { Location } from 'app/shared/model/enumerations/location.model';

export interface IPlayer {
  id?: number;
  name?: string | null;
  location?: Location | null;
  playedGames?: IPlayerGame[] | null;
  playedContests?: IPlayerContest[] | null;
}

export const defaultValue: Readonly<IPlayer> = {};

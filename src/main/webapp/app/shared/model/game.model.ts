import { IPlayerGame } from 'app/shared/model/player-game.model';
import { IContest } from 'app/shared/model/contest.model';
import { Location } from 'app/shared/model/enumerations/location.model';
import { Team } from 'app/shared/model/enumerations/team.model';

export interface IGame {
  id?: number;
  location?: Location;
  winnerTeam?: Team | null;
  gamePlayers?: IPlayerGame[] | null;
  contest?: IContest | null;
}

export const defaultValue: Readonly<IGame> = {};

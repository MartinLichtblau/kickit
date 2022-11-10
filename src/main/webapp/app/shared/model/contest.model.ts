import dayjs from 'dayjs';
import { IPlayerContest } from 'app/shared/model/player-contest.model';
import { IGame } from 'app/shared/model/game.model';
import { ContestMode } from 'app/shared/model/enumerations/contest-mode.model';
import { Location } from 'app/shared/model/enumerations/location.model';
import { Team } from 'app/shared/model/enumerations/team.model';

export interface IContest {
  id?: number;
  date?: string | null;
  contestMode?: ContestMode | null;
  location?: Location | null;
  winnerTeam?: Team | null;
  contestPlayers?: IPlayerContest[] | null;
  games?: IGame[] | null;
}

export const defaultValue: Readonly<IContest> = {};

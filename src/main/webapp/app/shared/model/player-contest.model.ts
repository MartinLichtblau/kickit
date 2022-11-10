import { IPlayer } from 'app/shared/model/player.model';
import { IContest } from 'app/shared/model/contest.model';
import { Team } from 'app/shared/model/enumerations/team.model';

export interface IPlayerContest {
  id?: number;
  team?: Team;
  player?: IPlayer | null;
  contest?: IContest | null;
}

export const defaultValue: Readonly<IPlayerContest> = {};

import dayjs from 'dayjs';
import { IBiography } from 'app/shared/model/biography.model';
import { ISkill } from 'app/shared/model/skill.model';

export interface IProject {
  id?: number;
  name?: string;
  client?: string;
  start?: dayjs.Dayjs;
  end?: dayjs.Dayjs | null;
  description?: string | null;
  biography?: IBiography | null;
  skills?: ISkill[] | null;
}

export const defaultValue: Readonly<IProject> = {};

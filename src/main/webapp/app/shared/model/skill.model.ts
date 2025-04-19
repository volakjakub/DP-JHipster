import { IBiography } from 'app/shared/model/biography.model';
import { IProject } from 'app/shared/model/project.model';

export interface ISkill {
  id?: number;
  name?: string;
  expertise?: number;
  biography?: IBiography | null;
  projects?: IProject[] | null;
}

export const defaultValue: Readonly<ISkill> = {};

import { IBiography } from 'app/shared/model/biography.model';

export interface ISkill {
  id?: number;
  name?: string;
  expertise?: number;
  biography?: IBiography | null;
}

export const defaultValue: Readonly<ISkill> = {};

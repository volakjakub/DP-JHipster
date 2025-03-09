import dayjs from 'dayjs';
import { IBiography } from 'app/shared/model/biography.model';
import { EducationType } from 'app/shared/model/enumerations/education-type.model';

export interface IEducation {
  id?: number;
  school?: string;
  type?: keyof typeof EducationType;
  start?: dayjs.Dayjs;
  end?: dayjs.Dayjs | null;
  biography?: IBiography | null;
}

export const defaultValue: Readonly<IEducation> = {};

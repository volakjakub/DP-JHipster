import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IBiography {
  id?: number;
  firstName?: string;
  lastName?: string;
  title?: string | null;
  phone?: string;
  email?: string;
  street?: string;
  city?: string;
  country?: string;
  position?: string;
  employedFrom?: dayjs.Dayjs;
  image?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IBiography> = {};

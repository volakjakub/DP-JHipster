import { IBiography } from 'app/shared/model/biography.model';
import { LanguageName } from 'app/shared/model/enumerations/language-name.model';

export interface ILanguage {
  id?: number;
  name?: keyof typeof LanguageName;
  expertise?: number;
  biography?: IBiography | null;
}

export const defaultValue: Readonly<ILanguage> = {};

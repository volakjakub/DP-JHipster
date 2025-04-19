import { LanguageName, LanguageNameValues } from 'app/shared/model/enumerations/language-name.model';

const LanguageNameConverter = (props: { enumValue: LanguageName }) => {
  return LanguageNameValues[props.enumValue] || 'ERROR';
};

export default LanguageNameConverter;

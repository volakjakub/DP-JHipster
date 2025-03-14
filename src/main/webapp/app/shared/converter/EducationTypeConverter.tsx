import { EducationType, EducationTypeValues } from 'app/shared/model/enumerations/education-type.model';

const EducationTypeConverter = (props: { enumValue: EducationType }) => {
  return EducationTypeValues[props.enumValue] || 'ERROR';
};

export default EducationTypeConverter;

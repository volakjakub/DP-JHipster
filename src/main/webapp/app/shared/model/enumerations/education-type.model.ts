export enum EducationType {
  HIGH_SCHOOL = 'HIGH_SCHOOL',

  BACHELOR = 'BACHELOR',

  MASTER = 'MASTER',

  DOCTORATE = 'DOCTORATE',
}

export const EducationTypeValues = {
  [EducationType.HIGH_SCHOOL]: 'Maturitní vzdělání',
  [EducationType.BACHELOR]: 'Bakalářské vzdělání',
  [EducationType.MASTER]: 'Magisterské vzdělání',
  [EducationType.DOCTORATE]: 'Doktorské vzdělání',
};

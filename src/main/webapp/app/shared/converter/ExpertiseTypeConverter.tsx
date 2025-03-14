const ExpertiseTypeConverter = (props: { value: number }) => {
  switch (props.value) {
    case 1:
      return 'Základní znalost';
    case 2:
      return 'Lepší znalost';
    case 3:
      return 'Pokročilá znalost';
    case 4:
      return 'Rozšířená znalost';
    case 5:
      return 'Seniorní znalost';
    default:
      return 'ERROR';
  }
};

export default ExpertiseTypeConverter;

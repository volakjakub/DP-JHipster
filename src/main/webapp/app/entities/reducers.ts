import biography from 'app/entities/biography/biography.reducer';
import education from 'app/entities/education/education.reducer';
import language from 'app/entities/language/language.reducer';
import skill from 'app/entities/skill/skill.reducer';
import project from 'app/entities/project/project.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  biography,
  education,
  language,
  skill,
  project,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;

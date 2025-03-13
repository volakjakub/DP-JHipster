import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { TextFormat, byteSize, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './biography.reducer';
import { getLanguageEntitiesByBiographyId } from 'app/entities/language/language.reducer';
import { getEducationEntitiesByBiographyId } from 'app/entities/education/education.reducer';
import { getProjectEntitiesByBiographyId } from 'app/entities/project/project.reducer';

export const BiographyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
    dispatch(getLanguageEntitiesByBiographyId(id));
    dispatch(getEducationEntitiesByBiographyId(id));
    dispatch(getProjectEntitiesByBiographyId(id));
  }, []);

  const biographyEntity = useAppSelector(state => state.biography.entity);
  const languageList = useAppSelector(state => state.language.entities);
  const languageLoading = useAppSelector(state => state.language.loading);
  const educationList = useAppSelector(state => state.education.entities);
  const educationLoading = useAppSelector(state => state.education.loading);
  const projectList = useAppSelector(state => state.project.entities);
  const projectLoading = useAppSelector(state => state.project.loading);
  return (
    <div>
      <Row>
        <Col md="3">
          <h2 data-cy="biographyDetailsHeading">Životopis</h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="title">Titul</span>
            </dt>
            <dd>{biographyEntity.title}</dd>
            <dt>
              <span id="firstName">Křestní jméno</span>
            </dt>
            <dd>{biographyEntity.firstName}</dd>
            <dt>
              <span id="lastName">Přijmení</span>
            </dt>
            <dd>{biographyEntity.lastName}</dd>
            <dt>
              <span id="phone">Telefon</span>
            </dt>
            <dd>{biographyEntity.phone}</dd>
            <dt>
              <span id="email">E-mail</span>
            </dt>
            <dd>{biographyEntity.email}</dd>
            <dt>
              <span id="image">Fotografie</span>
            </dt>
            <dd>
              {biographyEntity.image ? (
                <div>
                  {biographyEntity.imageContentType ? (
                    <a onClick={openFile(biographyEntity.imageContentType, biographyEntity.image)}>
                      <img src={`data:${biographyEntity.imageContentType};base64,${biographyEntity.image}`} style={{ maxHeight: '30px' }} />
                    </a>
                  ) : null}
                </div>
              ) : null}
            </dd>
            <hr />
            <dt>
              <span id="street">Ulice</span>
            </dt>
            <dd>{biographyEntity.street}</dd>
            <dt>
              <span id="city">Město</span>
            </dt>
            <dd>{biographyEntity.city}</dd>
            <dt>
              <span id="country">Země</span>
            </dt>
            <dd>{biographyEntity.country}</dd>
            <hr />
            <dt>
              <span id="position">Pozice</span>
            </dt>
            <dd>{biographyEntity.position}</dd>
            <dt>
              <span id="employedFrom">Zaměstnán/a od</span>
            </dt>
            <dd>
              {biographyEntity.employedFrom ? (
                <TextFormat value={biographyEntity.employedFrom} type="date" format={APP_LOCAL_DATE_FORMAT} />
              ) : null}
            </dd>
            <dt>Uživatel</dt>
            <dd>{biographyEntity.user ? biographyEntity.user.login : ''}</dd>
          </dl>
          <Button tag={Link} to={`/biography/${biographyEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Upravit životopis</span>
          </Button>
        </Col>
        <Col md="9">
          <h2 data-cy="biographyDetailsHeading">Jazyky</h2>
          <Link to="/language/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Přidat Jazyk
          </Link>
          <br />
          <br />
          <div className="table-responsive">
            {languageLoading ? (
              <div className="alert alert-warning">Načítání</div>
            ) : languageList && languageList.length > 0 ? (
              <Table responsive>
                <thead>
                  <tr>
                    <th className="hand">Název</th>
                    <th className="hand">Zkušenost</th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {languageList.map((language, i) => (
                    <tr key={`language-${i}`} data-cy="entityTable">
                      <td>{language.name}</td>
                      <td>{language.expertise}</td>
                      <td className="text-end">
                        <div className="btn-group flex-btn-group-container">
                          <Button tag={Link} to={`/language/${language.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Upravit jazyk</span>
                          </Button>
                          <Button
                            onClick={() => (window.location.href = `/language/${language.id}/delete`)}
                            color="danger"
                            size="sm"
                            data-cy="entityDeleteButton"
                          >
                            <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Odstranit jazyk</span>
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            ) : (
              <div className="alert alert-warning">Žádné jazyk</div>
            )}
          </div>

          <h2 data-cy="biographyDetailsHeading">Vzdělání</h2>
          <Link to="/education/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Přidat Vzdělání
          </Link>
          <br />
          <br />
          <div className="table-responsive">
            {educationLoading ? (
              <div className="alert alert-warning">Načítání</div>
            ) : educationList && educationList.length > 0 ? (
              <Table responsive>
                <thead>
                  <tr>
                    <th className="hand">Škola</th>
                    <th className="hand">Typ</th>
                    <th className="hand">Počátek studia</th>
                    <th className="hand">Ukončení studia</th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {educationList.map((education, i) => (
                    <tr key={`education-${i}`} data-cy="entityTable">
                      <td>{education.school}</td>
                      <td>{education.type}</td>
                      <td>{education.start ? <TextFormat value={education.start} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                      <td>{education.end ? <TextFormat value={education.end} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                      <td className="text-end">
                        <div className="btn-group flex-btn-group-container">
                          <Button tag={Link} to={`/education/${education.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Upravit vzdělání</span>
                          </Button>
                          <Button
                            onClick={() => (window.location.href = `/education/${education.id}/delete`)}
                            color="danger"
                            size="sm"
                            data-cy="entityDeleteButton"
                          >
                            <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Odstranit vzdělání</span>
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            ) : (
              <div className="alert alert-warning">Žádné vzdělání</div>
            )}
          </div>
        </Col>
      </Row>
      <br />
      <hr />
      <Row>
        <Col md="12">
          <h2 data-cy="biographyDetailsHeading">Projekty</h2>
          <Link to="/project/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Přidat Projekt
          </Link>
          <br />
          <br />
          <div className="table-responsive">
            {projectLoading ? (
              <div className="alert alert-warning">Načítání</div>
            ) : projectList && projectList.length > 0 ? (
              <Table responsive>
                <thead>
                  <tr>
                    <th className="hand">Název</th>
                    <th className="hand">Klient</th>
                    <th className="hand">Začátek projektu</th>
                    <th className="hand">Ukončení projektu</th>
                    <th className="hand">Popis</th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {projectList.map((project, i) => (
                    <tr key={`project-${i}`} data-cy="entityTable">
                      <td>{project.name}</td>
                      <td>{project.client}</td>
                      <td>{project.start ? <TextFormat value={project.start} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                      <td>{project.end ? <TextFormat value={project.end} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                      <td className="text-end">
                        <div className="btn-group flex-btn-group-container">
                          <Button tag={Link} to={`/project/${project.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Upravit projekt</span>
                          </Button>
                          <Button
                            onClick={() => (window.location.href = `/project/${project.id}/delete`)}
                            color="danger"
                            size="sm"
                            data-cy="entityDeleteButton"
                          >
                            <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Odstranit projekt</span>
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            ) : (
              <div className="alert alert-warning">Žádné projekty</div>
            )}
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default BiographyDetail;

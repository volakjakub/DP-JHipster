import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './project.reducer';

export const ProjectDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const account = useAppSelector(state => state.authentication.account);
  const projectEntity = useAppSelector(state => state.project.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="projectDetailsHeading">Projekt</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{projectEntity.id}</dd>
          <dt>
            <span id="name">Název</span>
          </dt>
          <dd>{projectEntity.name}</dd>
          <dt>
            <span id="client">Klient</span>
          </dt>
          <dd>{projectEntity.client}</dd>
          <dt>
            <span id="start">Začátek projektu</span>
          </dt>
          <dd>{projectEntity.start ? <TextFormat value={projectEntity.start} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="end">Ukončení projektu</span>
          </dt>
          <dd>{projectEntity.end ? <TextFormat value={projectEntity.end} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="description">Popis</span>
          </dt>
          <dd>{projectEntity.description}</dd>
          <dt>Životopis</dt>
          <dd>{projectEntity.biography ? projectEntity.biography.id : ''}</dd>
          <dt>Dovednosti</dt>
          <dd>
            {projectEntity.skills
              ? projectEntity.skills.map((val, i) => (
                  <span key={val.id}>
                    {val.name}
                    {projectEntity.skills && i === projectEntity.skills.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        {account && account?.login === projectEntity.biography?.user?.login ? (
          <div>
            <Button tag={Link} to={`/biography/${projectEntity.biography?.id}`} replace color="info" data-cy="entityDetailsBackButton">
              <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Zpět</span>
            </Button>
            &nbsp;
            <Button tag={Link} to={`/project/${projectEntity.id}/edit`} replace color="primary">
              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Upravit</span>
            </Button>
          </div>
        ) : (
          <div>
            <Button tag={Link} to="/project" replace color="info" data-cy="entityDetailsBackButton">
              <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Zpět</span>
            </Button>
            &nbsp;
            <Button tag={Link} to={`/project/${projectEntity.id}/edit`} replace color="secondary" disabled={true}>
              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Upravit</span>
            </Button>
          </div>
        )}
      </Col>
    </Row>
  );
};

export default ProjectDetail;

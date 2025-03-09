import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './skill.reducer';

export const SkillDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const skillEntity = useAppSelector(state => state.skill.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="skillDetailsHeading">Skill</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{skillEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{skillEntity.name}</dd>
          <dt>
            <span id="expertise">Expertise</span>
          </dt>
          <dd>{skillEntity.expertise}</dd>
          <dt>Biography</dt>
          <dd>{skillEntity.biography ? skillEntity.biography.id : ''}</dd>
          <dt>Projects</dt>
          <dd>
            {skillEntity.projects
              ? skillEntity.projects.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {skillEntity.projects && i === skillEntity.projects.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/skill" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Zpět</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/skill/${skillEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Upravit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SkillDetail;

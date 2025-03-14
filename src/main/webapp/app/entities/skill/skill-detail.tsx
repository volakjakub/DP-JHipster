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

  const account = useAppSelector(state => state.authentication.account);
  const skillEntity = useAppSelector(state => state.skill.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="skillDetailsHeading">Dovednost</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{skillEntity.id}</dd>
          <dt>
            <span id="name">Název</span>
          </dt>
          <dd>{skillEntity.name}</dd>
          <dt>
            <span id="expertise">Zkušenost</span>
          </dt>
          <dd>{skillEntity.expertise}</dd>
          <dt>Životopis</dt>
          <dd>{skillEntity.biography ? skillEntity.biography.id : ''}</dd>
          <dt>Projekty</dt>
          <dd>
            {skillEntity.projects
              ? skillEntity.projects.map((val, i) => (
                  <span key={val.id}>
                    {val.id}
                    {skillEntity.projects && i === skillEntity.projects.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        {account && account?.login === skillEntity.biography?.user?.login ? (
          <div>
            <Button tag={Link} to={`/biography/${skillEntity.biography?.id}`} replace color="info" data-cy="entityDetailsBackButton">
              <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Zpět</span>
            </Button>
            &nbsp;
            <Button tag={Link} to={`/skill/${skillEntity.id}/edit`} replace color="primary">
              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Upravit</span>
            </Button>
          </div>
        ) : (
          <div>
            <Button tag={Link} to="/skill" replace color="info" data-cy="entityDetailsBackButton">
              <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Zpět</span>
            </Button>
            &nbsp;
            <Button tag={Link} to={`/skill/${skillEntity.id}/edit`} replace color="secondary" disabled={true}>
              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Upravit</span>
            </Button>
          </div>
        )}
      </Col>
    </Row>
  );
};

export default SkillDetail;

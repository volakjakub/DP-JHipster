import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './language.reducer';

export const LanguageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const languageEntity = useAppSelector(state => state.language.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="languageDetailsHeading">Language</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{languageEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{languageEntity.name}</dd>
          <dt>
            <span id="expertise">Expertise</span>
          </dt>
          <dd>{languageEntity.expertise}</dd>
          <dt>Biography</dt>
          <dd>{languageEntity.biography ? languageEntity.biography.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/language" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Zpět</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/language/${languageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Upravit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default LanguageDetail;

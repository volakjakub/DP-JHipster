import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './education.reducer';

export const EducationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const educationEntity = useAppSelector(state => state.education.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="educationDetailsHeading">Education</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{educationEntity.id}</dd>
          <dt>
            <span id="school">School</span>
          </dt>
          <dd>{educationEntity.school}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{educationEntity.type}</dd>
          <dt>
            <span id="start">Start</span>
          </dt>
          <dd>{educationEntity.start ? <TextFormat value={educationEntity.start} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="end">End</span>
          </dt>
          <dd>{educationEntity.end ? <TextFormat value={educationEntity.end} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>Biography</dt>
          <dd>{educationEntity.biography ? educationEntity.biography.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/education" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Zpět</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/education/${educationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Upravit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default EducationDetail;

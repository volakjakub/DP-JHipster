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

  const account = useAppSelector(state => state.authentication.account);
  const educationEntity = useAppSelector(state => state.education.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="educationDetailsHeading">Vzdělání</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{educationEntity.id}</dd>
          <dt>
            <span id="school">Škola</span>
          </dt>
          <dd>{educationEntity.school}</dd>
          <dt>
            <span id="type">Typ</span>
          </dt>
          <dd>{educationEntity.type}</dd>
          <dt>
            <span id="start">Počátek studia</span>
          </dt>
          <dd>{educationEntity.start ? <TextFormat value={educationEntity.start} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="end">Ukončení studia</span>
          </dt>
          <dd>{educationEntity.end ? <TextFormat value={educationEntity.end} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>Životopis</dt>
          <dd>{educationEntity.biography ? educationEntity.biography.id : ''}</dd>
        </dl>
        {account && account?.login === educationEntity.biography?.user?.login ? (
          <div>
            <Button tag={Link} to={`/biography/${educationEntity.biography?.id}`} replace color="info" data-cy="entityDetailsBackButton">
              <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Zpět</span>
            </Button>
            &nbsp;
            <Button tag={Link} to={`/education/${educationEntity.id}/edit`} replace color="primary">
              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Upravit</span>
            </Button>
          </div>
        ) : (
          <div>
            <Button tag={Link} to="/education" replace color="info" data-cy="entityDetailsBackButton">
              <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Zpět</span>
            </Button>
            &nbsp;
            <Button tag={Link} to={`/education/${educationEntity.id}/edit`} replace color="secondary" disabled={true}>
              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Upravit</span>
            </Button>
          </div>
        )}
      </Col>
    </Row>
  );
};

export default EducationDetail;

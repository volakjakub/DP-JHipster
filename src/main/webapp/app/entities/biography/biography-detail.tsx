import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './biography.reducer';

export const BiographyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const biographyEntity = useAppSelector(state => state.biography.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="biographyDetailsHeading">Biography</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{biographyEntity.id}</dd>
          <dt>
            <span id="firstName">First Name</span>
          </dt>
          <dd>{biographyEntity.firstName}</dd>
          <dt>
            <span id="lastName">Last Name</span>
          </dt>
          <dd>{biographyEntity.lastName}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{biographyEntity.title}</dd>
          <dt>
            <span id="phone">Phone</span>
          </dt>
          <dd>{biographyEntity.phone}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{biographyEntity.email}</dd>
          <dt>
            <span id="street">Street</span>
          </dt>
          <dd>{biographyEntity.street}</dd>
          <dt>
            <span id="city">City</span>
          </dt>
          <dd>{biographyEntity.city}</dd>
          <dt>
            <span id="country">Country</span>
          </dt>
          <dd>{biographyEntity.country}</dd>
          <dt>
            <span id="position">Position</span>
          </dt>
          <dd>{biographyEntity.position}</dd>
          <dt>
            <span id="employedFrom">Employed From</span>
          </dt>
          <dd>
            {biographyEntity.employedFrom ? (
              <TextFormat value={biographyEntity.employedFrom} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="image">Image</span>
          </dt>
          <dd>{biographyEntity.image}</dd>
          <dt>User</dt>
          <dd>{biographyEntity.user ? biographyEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/biography" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Zpět</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/biography/${biographyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Upravit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BiographyDetail;

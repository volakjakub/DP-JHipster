import './home.scss';

import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';

import { Alert, Button, Col, Row } from 'reactstrap';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getEntityByUsername } from 'app/entities/biography/biography.reducer';

export const Home = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);

  useEffect(() => {
    if (account?.login) {
      dispatch(getEntityByUsername(account.login));
    }
  }, []);

  const biographyEntity = useAppSelector(state => state.biography.entity);
  return (
    <Row>
      <Col md="12">
        <h1 className="display-4">Vítejte v Curriculum!</h1>
        <p className="lead">V této aplikaci si můžete spravovat svůj profesní životopis.</p>
        {account?.login ? (
          biographyEntity?.id ? (
            <div>
              <Button tag={Link} to={`/biography/${biographyEntity?.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Můj životopis</span>
              </Button>
            </div>
          ) : (
            <div>
              <Link to="/biography/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
                <FontAwesomeIcon icon="plus" />
                &nbsp; Vytvořit Životopis
              </Link>
            </div>
          )
        ) : (
          <div>
            <Alert color="warning">
              Pro úpravu životopisu se musíte nejdříve
              <span>&nbsp;</span>
              <Link to="/login" className="alert-link">
                přihlásit.
              </Link>
            </Alert>
          </div>
        )}
      </Col>
    </Row>
  );
};

export default Home;

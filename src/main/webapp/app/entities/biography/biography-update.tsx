import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedBlobField, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createEntity, getEntity, reset, updateEntity } from './biography.reducer';

export const BiographyUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const account = useAppSelector(state => state.authentication.account);
  const biographyEntity = useAppSelector(state => state.biography.entity);
  const loading = useAppSelector(state => state.biography.loading);
  const updating = useAppSelector(state => state.biography.updating);
  const updateSuccess = useAppSelector(state => state.biography.updateSuccess);

  const handleClose = () => {
    navigate(`/biography/${biographyEntity?.id}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...biographyEntity,
      ...values,
      user: account,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...biographyEntity,
          user: account,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="curriculumApp.biography.home.createOrEditLabel" data-cy="BiographyCreateUpdateHeading">
            Vytvořit nebo upravit Životopis
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Načítání...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="biography-id" label="ID" validate={{ required: true }} hidden={true} />
              ) : null}
              <ValidatedField
                label="Titul"
                id="biography-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  maxLength: { value: 50, message: 'Toto pole nemůže být delší než 50 znaků.' },
                }}
              />
              <ValidatedField
                label="Křestní jméno"
                id="biography-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                  maxLength: { value: 50, message: 'Toto pole nemůže být delší než 50 znaků.' },
                }}
              />
              <ValidatedField
                label="Přijmení"
                id="biography-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                  maxLength: { value: 50, message: 'Toto pole nemůže být delší než 50 znaků.' },
                }}
              />
              <ValidatedField
                label="Telefon"
                id="biography-phone"
                name="phone"
                data-cy="phone"
                type="text"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                  maxLength: { value: 50, message: 'Toto pole nemůže být delší než 50 znaků.' },
                }}
              />
              <ValidatedField
                label="E-mail"
                id="biography-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                  maxLength: { value: 50, message: 'Toto pole nemůže být delší než 50 znaků.' },
                }}
              />
              <ValidatedBlobField label="Fotografie" id="biography-image" name="image" data-cy="image" isImage accept="image/*" />
              <hr />
              <ValidatedField
                label="Ulice"
                id="biography-street"
                name="street"
                data-cy="street"
                type="text"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                  maxLength: { value: 50, message: 'Toto pole nemůže být delší než 50 znaků.' },
                }}
              />
              <ValidatedField
                label="Město"
                id="biography-city"
                name="city"
                data-cy="city"
                type="text"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                  maxLength: { value: 50, message: 'Toto pole nemůže být delší než 50 znaků.' },
                }}
              />
              <ValidatedField
                label="Země"
                id="biography-country"
                name="country"
                data-cy="country"
                type="text"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                  maxLength: { value: 50, message: 'Toto pole nemůže být delší než 50 znaků.' },
                }}
              />
              <hr />
              <ValidatedField
                label="Pozice"
                id="biography-position"
                name="position"
                data-cy="position"
                type="text"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                  maxLength: { value: 50, message: 'Toto pole nemůže být delší než 50 znaků.' },
                }}
              />
              <ValidatedField
                label="Zaměstnán/a od"
                id="biography-employedFrom"
                name="employedFrom"
                data-cy="employedFrom"
                type="date"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                }}
              />
              {isNew ? (
                <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to={`/`} replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Zpět</span>
                </Button>
              ) : (
                <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to={`/biography/${id}`} replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Zpět</span>
                </Button>
              )}
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Uložit
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BiographyUpdate;

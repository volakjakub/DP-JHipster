import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getBiographies } from 'app/entities/biography/biography.reducer';
import { EducationType } from 'app/shared/model/enumerations/education-type.model';
import { createEntity, getEntity, reset, updateEntity } from './education.reducer';

export const EducationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const biographies = useAppSelector(state => state.biography.entities);
  const educationEntity = useAppSelector(state => state.education.entity);
  const loading = useAppSelector(state => state.education.loading);
  const updating = useAppSelector(state => state.education.updating);
  const updateSuccess = useAppSelector(state => state.education.updateSuccess);
  const educationTypeValues = Object.keys(EducationType);

  const handleClose = () => {
    navigate(`/education${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBiographies({}));
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
      ...educationEntity,
      ...values,
      biography: biographies.find(it => it.id.toString() === values.biography?.toString()),
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
          type: 'HIGH_SCHOOL',
          ...educationEntity,
          biography: educationEntity?.biography?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="curriculumApp.education.home.createOrEditLabel" data-cy="EducationCreateUpdateHeading">
            Vytvořit nebo upravit Education
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="education-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="School"
                id="education-school"
                name="school"
                data-cy="school"
                type="text"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                  maxLength: { value: 50, message: 'Toto pole nemůže být delší než 50 znaků.' },
                }}
              />
              <ValidatedField label="Type" id="education-type" name="type" data-cy="type" type="select">
                {educationTypeValues.map(educationType => (
                  <option value={educationType} key={educationType}>
                    {educationType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Start"
                id="education-start"
                name="start"
                data-cy="start"
                type="date"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                }}
              />
              <ValidatedField label="End" id="education-end" name="end" data-cy="end" type="date" />
              <ValidatedField id="education-biography" name="biography" data-cy="biography" label="Biography" type="select">
                <option value="" key="0" />
                {biographies
                  ? biographies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/education" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Zpět</span>
              </Button>
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

export default EducationUpdate;

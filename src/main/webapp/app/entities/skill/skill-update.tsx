import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm, isNumber } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './skill.reducer';
import ExpertiseTypeConverter from 'app/shared/converter/ExpertiseTypeConverter';

export const SkillUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const biography = useAppSelector(state => state.biography.entity);
  const skillEntity = useAppSelector(state => state.skill.entity);
  const loading = useAppSelector(state => state.skill.loading);
  const updating = useAppSelector(state => state.skill.updating);
  const updateSuccess = useAppSelector(state => state.skill.updateSuccess);

  const handleClose = () => {
    navigate(`/biography/${biography?.id}`);
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
    if (values.expertise !== undefined && typeof values.expertise !== 'number') {
      values.expertise = Number(values.expertise);
    }

    const entity = {
      ...skillEntity,
      ...values,
      biography,
      projects: [],
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
          ...skillEntity,
          biography,
          projects: [],
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="curriculumApp.skill.home.createOrEditLabel" data-cy="SkillCreateUpdateHeading">
            Vytvořit nebo upravit Dovednost
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
                <ValidatedField name="id" required readOnly id="skill-id" label="ID" validate={{ required: true }} hidden={true} />
              ) : null}
              <ValidatedField
                label="Název"
                id="skill-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                  maxLength: { value: 50, message: 'Toto pole nemůže být delší než 50 znaků.' },
                }}
              />
              <ValidatedField
                label="Zkušenost"
                id="skill-expertise"
                name="expertise"
                data-cy="expertise"
                type="text"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                  validate: v => isNumber(v) || 'Toto pole by mělo obsahovat číslo.',
                }}
              >
                <option value={1} key={1}>
                  <ExpertiseTypeConverter value={1} />
                </option>
                <option value={2} key={2}>
                  <ExpertiseTypeConverter value={2} />
                </option>
                <option value={3} key={3}>
                  <ExpertiseTypeConverter value={3} />
                </option>
                <option value={4} key={4}>
                  <ExpertiseTypeConverter value={4} />
                </option>
                <option value={5} key={5}>
                  <ExpertiseTypeConverter value={5} />
                </option>
              </ValidatedField>
              <Button
                tag={Link}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to={`/biography/${biography?.id}`}
                replace
                color="info"
              >
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

export default SkillUpdate;

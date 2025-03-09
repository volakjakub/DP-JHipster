import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm, isNumber } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getBiographies } from 'app/entities/biography/biography.reducer';
import { createEntity, getEntity, reset, updateEntity } from './skill.reducer';

export const SkillUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const biographies = useAppSelector(state => state.biography.entities);
  const skillEntity = useAppSelector(state => state.skill.entity);
  const loading = useAppSelector(state => state.skill.loading);
  const updating = useAppSelector(state => state.skill.updating);
  const updateSuccess = useAppSelector(state => state.skill.updateSuccess);

  const handleClose = () => {
    navigate(`/skill${location.search}`);
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
    if (values.expertise !== undefined && typeof values.expertise !== 'number') {
      values.expertise = Number(values.expertise);
    }

    const entity = {
      ...skillEntity,
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
          ...skillEntity,
          biography: skillEntity?.biography?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="curriculumApp.skill.home.createOrEditLabel" data-cy="SkillCreateUpdateHeading">
            Vytvořit nebo upravit Skill
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="skill-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
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
                label="Expertise"
                id="skill-expertise"
                name="expertise"
                data-cy="expertise"
                type="text"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                  validate: v =>
                    (isNumber(v) || 'Toto pole by mělo obsahovat číslo.') && ((v >= 1 && v <= 5) || 'Hodnota musí být mezi 1 a 5.'),
                }}
              />
              <ValidatedField id="skill-biography" name="biography" data-cy="biography" label="Biography" type="select">
                <option value="" key="0" />
                {biographies
                  ? biographies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/skill" replace color="info">
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

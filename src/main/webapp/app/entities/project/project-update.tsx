import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getSkillEntitiesByBiographyId } from 'app/entities/skill/skill.reducer';
import { createEntity, getEntity, reset, updateEntity } from './project.reducer';

export const ProjectUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const biography = useAppSelector(state => state.biography.entity);
  const skills = useAppSelector(state => state.skill.entities);
  const projectEntity = useAppSelector(state => state.project.entity);
  const loading = useAppSelector(state => state.project.loading);
  const updating = useAppSelector(state => state.project.updating);
  const updateSuccess = useAppSelector(state => state.project.updateSuccess);

  const handleClose = () => {
    navigate(`/biography/${biography?.id}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSkillEntitiesByBiographyId(biography?.id));
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
      ...projectEntity,
      ...values,
      biography,
      skills: mapIdList(values.skills),
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
          ...projectEntity,
          biography,
          skills: projectEntity?.skills?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="curriculumApp.project.home.createOrEditLabel" data-cy="ProjectCreateUpdateHeading">
            Vytvořit nebo upravit Projekt
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
                <ValidatedField name="id" required readOnly id="project-id" label="ID" validate={{ required: true }} hidden={true} />
              ) : null}
              <ValidatedField
                label="Název"
                id="project-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                  maxLength: { value: 50, message: 'Toto pole nemůže být delší než 50 znaků.' },
                }}
              />
              <ValidatedField
                label="Klient"
                id="project-client"
                name="client"
                data-cy="client"
                type="text"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                  maxLength: { value: 50, message: 'Toto pole nemůže být delší než 50 znaků.' },
                }}
              />
              <ValidatedField
                label="Zahájení projektu"
                id="project-start"
                name="start"
                data-cy="start"
                type="date"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                }}
              />
              <ValidatedField label="Ukončení projektu" id="project-end" name="end" data-cy="end" type="date" />
              <ValidatedField label="Popis" id="project-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="Skills" id="project-skills" data-cy="skills" type="select" multiple name="skills">
                <option value="" key="0" />
                {skills
                  ? skills.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
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

export default ProjectUpdate;

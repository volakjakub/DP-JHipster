import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm, isNumber } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { LanguageName } from 'app/shared/model/enumerations/language-name.model';
import { createEntity, getEntity, reset, updateEntity } from './language.reducer';
import LanguageNameConverter from 'app/shared/converter/LanguageNameConverter';

export const LanguageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const biography = useAppSelector(state => state.biography.entity);
  const languageEntity = useAppSelector(state => state.language.entity);
  const loading = useAppSelector(state => state.language.loading);
  const updating = useAppSelector(state => state.language.updating);
  const updateSuccess = useAppSelector(state => state.language.updateSuccess);
  const languageNameValues = Object.keys(LanguageName);

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
      ...languageEntity,
      ...values,
      biography,
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
          name: 'CZECH',
          ...languageEntity,
          biography,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="curriculumApp.language.home.createOrEditLabel" data-cy="LanguageCreateUpdateHeading">
            Vytvořit nebo upravit Jazyk
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
                <ValidatedField name="id" required readOnly id="language-id" label="ID" validate={{ required: true }} hidden={true} />
              ) : null}
              <ValidatedField label="Název" id="language-name" name="name" data-cy="name" type="select">
                {languageNameValues.map(languageName => (
                  <option value={languageName} key={languageName}>
                    <LanguageNameConverter enumValue={languageName as LanguageName} />
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Zkušenost"
                id="language-expertise"
                name="expertise"
                data-cy="expertise"
                type="text"
                validate={{
                  required: { value: true, message: 'Toto pole je povinné.' },
                  validate: v => isNumber(v) || 'Toto pole by mělo obsahovat číslo.',
                }}
              />
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

export default LanguageUpdate;

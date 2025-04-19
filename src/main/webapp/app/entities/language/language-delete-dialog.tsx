import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { deleteEntity, getEntity } from './language.reducer';
import { getBiographyEntityByUsername } from 'app/entities/biography/biography.reducer';

export const LanguageDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const account = useAppSelector(state => state.authentication.account);
  const biography = useAppSelector(state => state.biography.entity);

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);

    if (account?.login) {
      dispatch(getBiographyEntityByUsername(account.login));
    }
  }, []);

  const languageEntity = useAppSelector(state => state.language.entity);
  const updateSuccess = useAppSelector(state => state.language.updateSuccess);

  const handleClose = () => {
    navigate(`/biography/${biography?.id}`);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(languageEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="languageDeleteDialogHeading">
        Potvrzení odstranění
      </ModalHeader>
      <ModalBody id="curriculumApp.language.delete.question">Jste si jisti, že chcete smazat Jazyk {languageEntity.name}?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Zrušit
        </Button>
        <Button id="jhi-confirm-delete-language" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Odstranit
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default LanguageDeleteDialog;

import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { deleteEntity, getEntity } from './education.reducer';

export const EducationDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const biography = useAppSelector(state => state.biography.entity);

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const educationEntity = useAppSelector(state => state.education.entity);
  const updateSuccess = useAppSelector(state => state.education.updateSuccess);

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
    dispatch(deleteEntity(educationEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="educationDeleteDialogHeading">
        Potvrzení odstranění
      </ModalHeader>
      <ModalBody id="curriculumApp.education.delete.question">
        Jste si jisti, že chcete smazat Vzdělání {educationEntity.school} ({educationEntity.type})?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Zrušit
        </Button>
        <Button id="jhi-confirm-delete-education" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Odstranit
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default EducationDeleteDialog;

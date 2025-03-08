import React, { useEffect, useState } from 'react';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { useSearchParams } from 'react-router-dom';
import { toast } from 'react-toastify';

import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { handlePasswordResetFinish, reset } from '../password-reset.reducer';

export const PasswordResetFinishPage = () => {
  const dispatch = useAppDispatch();

  const [searchParams] = useSearchParams();
  const key = searchParams.get('key');

  const [password, setPassword] = useState('');

  useEffect(
    () => () => {
      dispatch(reset());
    },
    [],
  );

  const handleValidSubmit = ({ newPassword }) => dispatch(handlePasswordResetFinish({ key, newPassword }));

  const updatePassword = event => setPassword(event.target.value);

  const getResetForm = () => {
    return (
      <ValidatedForm onSubmit={handleValidSubmit}>
        <ValidatedField
          name="newPassword"
          label="Nové heslo"
          placeholder="Nové heslo"
          type="password"
          validate={{
            required: { value: true, message: 'Heslo je povinné.' },
            minLength: { value: 4, message: 'Vaše heslo musí mít alespoň 4 znaky.' },
            maxLength: { value: 50, message: 'Vaše heslo nemůže být delší než 50 znaků.' },
          }}
          onChange={updatePassword}
          data-cy="resetPassword"
        />
        <PasswordStrengthBar password={password} />
        <ValidatedField
          name="confirmPassword"
          label="Potvrzení nového hesla"
          placeholder="Potvrzení nového hesla"
          type="password"
          validate={{
            required: { value: true, message: 'Potvrzení hesla je povinné.' },
            minLength: { value: 4, message: 'Vaše potvrzení hesla musí mít alespoň 4 znaky.' },
            maxLength: { value: 50, message: 'Vaše potvrzení hesla nemůže být delší než 50 znaků.' },
            validate: v => v === password || 'Nové heslo a potvrzení nového hesla se neshodují!',
          }}
          data-cy="confirmResetPassword"
        />
        <Button color="success" type="submit" data-cy="submit">
          Ověřit nové heslo
        </Button>
      </ValidatedForm>
    );
  };

  const successMessage = useAppSelector(state => state.passwordReset.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="4">
          <h1>Obnovit heslo</h1>
          <div>{key ? getResetForm() : null}</div>
        </Col>
      </Row>
    </div>
  );
};

export default PasswordResetFinishPage;

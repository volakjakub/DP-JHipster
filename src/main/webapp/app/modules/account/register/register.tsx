import React, { useEffect, useState } from 'react';
import { ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { Alert, Button, Col, Row } from 'reactstrap';
import { toast } from 'react-toastify';
import { Link } from 'react-router-dom';

import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { handleRegister, reset } from './register.reducer';

export const RegisterPage = () => {
  const [password, setPassword] = useState('');
  const dispatch = useAppDispatch();

  useEffect(
    () => () => {
      dispatch(reset());
    },
    [],
  );

  const handleValidSubmit = ({ username, email, firstPassword }) => {
    dispatch(handleRegister({ login: username, email, password: firstPassword, langKey: 'en' }));
  };

  const updatePassword = event => setPassword(event.target.value);

  const successMessage = useAppSelector(state => state.register.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1 id="register-title" data-cy="registerTitle">
            Registrace
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          <ValidatedForm id="register-form" onSubmit={handleValidSubmit}>
            <ValidatedField
              name="username"
              label="Uživatelské jméno"
              placeholder="Vaše uživatelské jméno"
              validate={{
                required: { value: true, message: 'Uživatelské jméno již existuje.' },
                pattern: {
                  value: /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/,
                  message: 'Your username is invalid.',
                },
                minLength: { value: 1, message: 'Uživatelské jméno musí mít alespoň 1 znak.' },
                maxLength: { value: 50, message: 'Uživatelské jméno nesmí být delší než 50 znaků.' },
              }}
              data-cy="username"
            />
            <ValidatedField
              name="email"
              label="Email"
              placeholder="Váš email"
              type="email"
              validate={{
                required: { value: true, message: 'Email je povinný.' },
                minLength: { value: 5, message: 'Váš email musí mít alespoň 5 znaků.' },
                maxLength: { value: 254, message: 'Váš email nemůže být delší než 50 znaků.' },
                validate: v => isEmail(v) || 'Zadaný email není validní.',
              }}
              data-cy="email"
            />
            <ValidatedField
              name="firstPassword"
              label="Nové heslo"
              placeholder="Nové heslo"
              type="password"
              onChange={updatePassword}
              validate={{
                required: { value: true, message: 'Heslo je povinné.' },
                minLength: { value: 4, message: 'Vaše heslo musí mít alespoň 4 znaky.' },
                maxLength: { value: 50, message: 'Vaše heslo nemůže být delší než 50 znaků.' },
              }}
              data-cy="firstPassword"
            />
            <PasswordStrengthBar password={password} />
            <ValidatedField
              name="secondPassword"
              label="Potvrzení nového hesla"
              placeholder="Potvrzení nového hesla"
              type="password"
              validate={{
                required: { value: true, message: 'Potvrzení hesla je povinné.' },
                minLength: { value: 4, message: 'Vaše potvrzení hesla musí mít alespoň 4 znaky.' },
                maxLength: { value: 50, message: 'Vaše potvrzení hesla nemůže být delší než 50 znaků.' },
                validate: v => v === password || 'Nové heslo a potvrzení nového hesla se neshodují!',
              }}
              data-cy="secondPassword"
            />
            <Button id="register-submit" color="primary" type="submit" data-cy="submit">
              Registrovat
            </Button>
          </ValidatedForm>
          <p>&nbsp;</p>
          <Alert color="warning">
            <span>Pokud se chcete </span>
            <Link to="/login" className="alert-link">
              přihlásit
            </Link>
            <span>
              , můžete vyzkoušet predvolené účty:
              <br />- Administrátor (jméno=&quot;admin&quot; a heslo=&quot;admin&quot;) <br />- Uživatel (jméno=&quot;user&quot; a
              heslo=&quot;user&quot;).
            </span>
          </Alert>
        </Col>
      </Row>
    </div>
  );
};

export default RegisterPage;

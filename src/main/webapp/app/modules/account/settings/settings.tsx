import React, { useEffect } from 'react';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { reset, saveAccountSettings } from './settings.reducer';

export const SettingsPage = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.settings.successMessage);

  useEffect(() => {
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  const handleValidSubmit = values => {
    dispatch(
      saveAccountSettings({
        ...account,
        ...values,
      }),
    );
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="settings-title">
            Nastavení uživatele [<strong>{account.login}</strong>]
          </h2>
          <ValidatedForm id="settings-form" onSubmit={handleValidSubmit} defaultValues={account}>
            <ValidatedField
              name="firstName"
              label="Křestní jméno"
              id="firstName"
              placeholder="Vaše křestní jméno"
              validate={{
                required: { value: true, message: 'Křestní jméno je povinné.' },
                minLength: { value: 1, message: 'Křestní jméno musí obsahovat alespoň 1 znak' },
                maxLength: { value: 50, message: 'Křestní jméno nemůže být delší než 50 znaků' },
              }}
              data-cy="firstname"
            />
            <ValidatedField
              name="lastName"
              label="Příjmení"
              id="lastName"
              placeholder="Vaše příjmení"
              validate={{
                required: { value: true, message: 'Příjmení je povinné.' },
                minLength: { value: 1, message: 'Příjmení musí obsahovat alespoň 1 znak' },
                maxLength: { value: 50, message: 'Příjmení nemůže být delší než 50 znaků' },
              }}
              data-cy="lastname"
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
            <Button color="primary" type="submit" data-cy="submit">
              Uložit
            </Button>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default SettingsPage;

import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { Alert, Col, Row } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <h1 className="display-4">Vítejte, Java Hipster!</h1>
        <p className="lead">Toto je vaše domovská stránka</p>
        {account?.login ? (
          <div>
            <Alert color="success">Jste přihlášen jako &quot;{account.login}&quot;.</Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              Pokud se chcete
              <span>&nbsp;</span>
              <Link to="/login" className="alert-link">
                přihlásit
              </Link>
              , můžete vyzkoušet predvolené účty:
              <br />- Administrátor (jméno=&quot;admin&quot; a heslo=&quot;admin&quot;) <br />- Uživatel (jméno=&quot;user&quot; a
              heslo=&quot;user&quot;).
            </Alert>

            <Alert color="warning">
              Ještě nemáte účet?&nbsp;
              <Link to="/account/register" className="alert-link">
                Zaregistrujte si nový účet.
              </Link>
            </Alert>
          </div>
        )}
        <p>Pokud máte nějaké dotazy týkající se JHipster:</p>

        <ul>
          <li>
            <a href="https://www.jhipster.tech/" target="_blank" rel="noopener noreferrer">
              Domovská stránka JHipster
            </a>
          </li>
          <li>
            <a href="https://stackoverflow.com/tags/jhipster/info" target="_blank" rel="noopener noreferrer">
              JHipster ve službě Stack Overflow
            </a>
          </li>
          <li>
            <a href="https://github.com/jhipster/generator-jhipster/issues?state=open" target="_blank" rel="noopener noreferrer">
              Nahlašování chyb JHipster
            </a>
          </li>
          <li>
            <a href="https://gitter.im/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
              Veřejná chatovací místnost JHipster
            </a>
          </li>
          <li>
            <a href="https://twitter.com/jhipster" target="_blank" rel="noopener noreferrer">
              Sledovat @jhipster ve službě Twitter
            </a>
          </li>
        </ul>

        <p>
          Pokud se vám JHipster líbí, nezapomeňte nám dát hvězdičku ve službě{' '}
          <a href="https://github.com/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
            GitHub
          </a>
          !
        </p>
      </Col>
    </Row>
  );
};

export default Home;

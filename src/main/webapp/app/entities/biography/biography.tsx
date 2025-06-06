import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, byteSize, getPaginationState, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './biography.reducer';

export const Biography = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const biographyList = useAppSelector(state => state.biography.entities);
  const loading = useAppSelector(state => state.biography.loading);
  const totalItems = useAppSelector(state => state.biography.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="biography-heading" data-cy="BiographyHeading">
        Životopisy
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Obnovit
          </Button>
        </div>
      </h2>
      <div className="table-responsive">
        {biographyList && biographyList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('firstName')}>
                  Křestní jméno <FontAwesomeIcon icon={getSortIconByFieldName('firstName')} />
                </th>
                <th className="hand" onClick={sort('lastName')}>
                  Přijmení <FontAwesomeIcon icon={getSortIconByFieldName('lastName')} />
                </th>
                <th className="hand" onClick={sort('title')}>
                  Titul <FontAwesomeIcon icon={getSortIconByFieldName('title')} />
                </th>
                <th className="hand" onClick={sort('phone')}>
                  Telefon <FontAwesomeIcon icon={getSortIconByFieldName('phone')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  E-mail <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('street')}>
                  Ulice <FontAwesomeIcon icon={getSortIconByFieldName('street')} />
                </th>
                <th className="hand" onClick={sort('city')}>
                  Město <FontAwesomeIcon icon={getSortIconByFieldName('city')} />
                </th>
                <th className="hand" onClick={sort('country')}>
                  Země <FontAwesomeIcon icon={getSortIconByFieldName('country')} />
                </th>
                <th className="hand" onClick={sort('position')}>
                  Pozice <FontAwesomeIcon icon={getSortIconByFieldName('position')} />
                </th>
                <th className="hand" onClick={sort('employedFrom')}>
                  Zaměsnán/a od <FontAwesomeIcon icon={getSortIconByFieldName('employedFrom')} />
                </th>
                <th className="hand" onClick={sort('image')}>
                  Fotografie <FontAwesomeIcon icon={getSortIconByFieldName('image')} />
                </th>
                <th>
                  Uživatel <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {biographyList.map((biography, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/biography/${biography.id}`} color="link" size="sm">
                      {biography.id}
                    </Button>
                  </td>
                  <td>{biography.firstName}</td>
                  <td>{biography.lastName}</td>
                  <td>{biography.title}</td>
                  <td>{biography.phone}</td>
                  <td>{biography.email}</td>
                  <td>{biography.street}</td>
                  <td>{biography.city}</td>
                  <td>{biography.country}</td>
                  <td>{biography.position}</td>
                  <td>
                    {biography.employedFrom ? (
                      <TextFormat type="date" value={biography.employedFrom} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {biography.image ? (
                      <div>
                        {biography.imageContentType ? (
                          <a onClick={openFile(biography.imageContentType, biography.image)}>
                            <img src={`data:${biography.imageContentType};base64,${biography.image}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {biography.imageContentType}, {byteSize(biography.image)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{biography.user ? biography.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/biography/${biography.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Detaily</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/biography/${biography.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="secondary"
                        size="sm"
                        data-cy="entityEditButton"
                        disabled={true}
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Upravit</span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/biography/${biography.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="secondary"
                        size="sm"
                        data-cy="entityDeleteButton"
                        disabled={true}
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Odstranit</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Biographies found</div>
        )}
      </div>
      {totalItems ? (
        <div className={biographyList && biographyList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Biography;

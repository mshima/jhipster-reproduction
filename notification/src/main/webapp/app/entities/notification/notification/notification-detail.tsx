import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router';
import { Button, Row, Col } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './notification.reducer';

export const NotificationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const notificationEntity = useAppSelector(state => state.notification.notification.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="notificationDetailsHeading">
          <Translate contentKey="notificationApp.notificationNotification.detail.title">Notification</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="notificationApp.notificationNotification.title">Title</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.title}</dd>
          <dt>
            <Translate contentKey="notificationApp.notificationNotification.user">User</Translate>
          </dt>
          <dd>{notificationEntity.user ? notificationEntity.user.login : ''}</dd>
        </dl>
        <Button as={Link as any} to="/notification/notification" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/notification/notification/${notificationEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default NotificationDetail;

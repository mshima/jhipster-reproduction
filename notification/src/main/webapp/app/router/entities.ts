import { Authority } from '@/shared/jhipster/constants';
const Entities = () => import('@/entities/entities.vue');

const Notification = () => import('@/entities/notification/notification/notification.vue');
const NotificationUpdate = () => import('@/entities/notification/notification/notification-update.vue');
const NotificationDetails = () => import('@/entities/notification/notification/notification-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/notification',
  component: Entities,
  children: [
    {
      path: 'notification',
      name: 'Notification',
      component: Notification,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'notification/new',
      name: 'NotificationCreate',
      component: NotificationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'notification/:notificationId/edit',
      name: 'NotificationEdit',
      component: NotificationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'notification/:notificationId/view',
      name: 'NotificationView',
      component: NotificationDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};

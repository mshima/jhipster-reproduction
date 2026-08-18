import { defineComponent, provide } from 'vue';

import UserService from '@/entities/user/user.service';

import NotificationService from './notification/notification/notification.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('notificationService', () => new NotificationService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});

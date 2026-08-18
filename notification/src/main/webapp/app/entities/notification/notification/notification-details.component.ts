import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type INotification } from '@/shared/model/notification/notification.model';

import NotificationService from './notification.service';

export default defineComponent({
  name: 'NotificationDetails',
  setup() {
    const notificationService = inject('notificationService', () => new NotificationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const notification: Ref<INotification> = ref({});

    const retrieveNotification = async notificationId => {
      try {
        const res = await notificationService().find(notificationId);
        notification.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.notificationId) {
      retrieveNotification(route.params.notificationId);
    }

    return {
      alertService,
      notification,

      previousState,
      t$: useI18n().t,
    };
  },
});

import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import { useAlertService } from '@/shared/alert/alert.service';
import { type INotification } from '@/shared/model/notification/notification.model';

import NotificationService from './notification.service';

export default defineComponent({
  name: 'Notification',
  setup() {
    const { t: t$ } = useI18n();
    const notificationService = inject('notificationService', () => new NotificationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const notifications: Ref<INotification[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveNotifications = async () => {
      isFetching.value = true;
      try {
        const res = await notificationService().retrieve();
        notifications.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveNotifications();
    };

    onMounted(async () => {
      await retrieveNotifications();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: INotification) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeNotification = async () => {
      try {
        await notificationService().delete(removeId.value);
        const message = t$('notificationApp.notificationNotification.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveNotifications();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      notifications,
      handleSyncList,
      isFetching,
      retrieveNotifications,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeNotification,
      t$,
    };
  },
});

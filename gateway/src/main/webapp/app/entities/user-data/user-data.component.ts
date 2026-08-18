import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IUserData } from '@/shared/model/user-data.model';

import UserDataService from './user-data.service';

export default defineComponent({
  name: 'UserData',
  setup() {
    const { t: t$ } = useI18n();
    const userDataService = inject('userDataService', () => new UserDataService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const userDatas: Ref<IUserData[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveUserDatas = async () => {
      isFetching.value = true;
      try {
        const res = await userDataService().retrieve();
        userDatas.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveUserDatas();
    };

    onMounted(async () => {
      await retrieveUserDatas();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IUserData) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeUserData = async () => {
      try {
        await userDataService().delete(removeId.value);
        const message = t$('gatewayApp.userData.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveUserDatas();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      userDatas,
      handleSyncList,
      isFetching,
      retrieveUserDatas,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeUserData,
      t$,
    };
  },
});

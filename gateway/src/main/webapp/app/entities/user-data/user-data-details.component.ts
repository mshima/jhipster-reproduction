import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IUserData } from '@/shared/model/user-data.model';

import UserDataService from './user-data.service';

export default defineComponent({
  name: 'UserDataDetails',
  setup() {
    const userDataService = inject('userDataService', () => new UserDataService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const userData: Ref<IUserData> = ref({});

    const retrieveUserData = async userDataId => {
      try {
        const res = await userDataService().find(userDataId);
        userData.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.userDataId) {
      retrieveUserData(route.params.userDataId);
    }

    return {
      alertService,
      userData,

      previousState,
      t$: useI18n().t,
    };
  },
});

import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type IUserData, UserData } from '@/shared/model/user-data.model';

import UserDataService from './user-data.service';

export default defineComponent({
  name: 'UserDataUpdate',
  setup() {
    const userDataService = inject('userDataService', () => new UserDataService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const userData: Ref<IUserData> = ref(new UserData());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      address: {},
    };
    const v$ = useVuelidate(validationRules, userData as any);
    v$.value.$validate();

    return {
      userDataService,
      alertService,
      userData,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.userData.id) {
        this.userDataService()
          .update(this.userData)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('gatewayApp.userData.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.userDataService()
          .create(this.userData)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('gatewayApp.userData.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});

import { type Component, defineComponent, provide } from 'vue';
import { useI18n } from 'vue-i18n';

import { BApp } from 'bootstrap-vue-next';

import JhiFooter from '@/core/jhi-footer/jhi-footer.vue';
import JhiNavbar from '@/core/jhi-navbar/jhi-navbar.vue';
import Ribbon from '@/core/ribbon/ribbon.vue';
import { useAlertService } from '@/shared/alert/alert.service';
import '@/shared/config/dayjs';

export default defineComponent({
  name: 'App',
  components: {
    BApp: BApp as Component,
    Ribbon,
    JhiNavbar,
    JhiFooter,
  },
  setup() {
    provide('alertService', useAlertService());

    return {
      t$: useI18n().t,
    };
  },
});

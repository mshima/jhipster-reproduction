import { type Ref, computed, defineAsyncComponent, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';

import { loadRemote } from '@module-federation/enhanced/runtime';
import { storeToRefs } from 'pinia';

import type AccountService from '@/account/account.service';
import type LoginService from '@/account/login.service';
import languages from '@/shared/config/languages';
import { useStore } from '@/store';

export default defineComponent({
  name: 'JhiNavbar',
  components: {
    // Load entities menu exposed by microfrontend
    // 'entities-menu': EntitiesMenu,
    'gateway-menu': defineAsyncComponent(() => {
      return loadRemote<any>('gateway/entities-menu').catch(() => import('@/core/error/error-loading.vue'));
    }),
    'blog-menu': defineAsyncComponent(() => {
      return loadRemote<any>('blog/entities-menu').catch(() => import('@/core/error/error-loading.vue'));
    }),
    'notification-menu': defineAsyncComponent(() => {
      return loadRemote<any>('notification/entities-menu').catch(() => import('@/core/error/error-loading.vue'));
    }),
  },
  setup() {
    const loginService = inject<LoginService>('loginService');
    const { login } = loginService;

    const accountService = inject<AccountService>('accountService');
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);
    const changeLanguage = inject<(string) => Promise<void>>('changeLanguage');

    const isActiveLanguage = (key: string) => key === currentLanguage.value;

    const router = useRouter();
    const store = useStore();

    const version = `v${APP_VERSION}`;
    const hasAnyAuthorityValues: Ref = ref({});

    const openAPIEnabled = computed(() => store.activeProfiles.includes('api-docs'));
    const inProduction = computed(() => store.activeProfiles.includes('prod'));
    const { authenticated } = storeToRefs(store);

    const subIsActive = (input: string | string[]) => {
      const paths = Array.isArray(input) ? input : [input];
      // current path starts with this path string
      return paths.some(path => router.currentRoute.value.path.startsWith(path));
    };

    const logout = async () => {
      const response = await loginService.logout();
      store.logout();

      const next = response.data?.logoutUrl;

      if (next) {
        if (/^https?:\/\//i.test(next)) {
          window.location.href = next;
          return;
        }
        await router.push(next);
        return;
      }

      if (router.currentRoute.value.path !== '/') {
        await router.push('/');
      }
    };

    return {
      logout,
      subIsActive,
      accountService,
      login,
      changeLanguage,
      languages: languages(),
      isActiveLanguage,
      version,
      currentLanguage,
      hasAnyAuthorityValues,
      openAPIEnabled,
      inProduction,
      authenticated,
      t$: useI18n().t,
    };
  },
  methods: {
    hasAnyAuthority(authorities: any): boolean {
      this.accountService.hasAnyAuthorityAndCheckAuth(authorities).then(value => {
        if (this.hasAnyAuthorityValues[authorities] !== value) {
          this.hasAnyAuthorityValues = { ...this.hasAnyAuthorityValues, [authorities]: value };
        }
      });
      return this.hasAnyAuthorityValues[authorities] ?? false;
    },
  },
});

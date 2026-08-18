import { beforeEach, describe, expect, it, vi } from 'vitest';
import { computed } from 'vue';
import { type Router } from 'vue-router';

import { createTestingPinia } from '@pinia/testing';
import { shallowMount } from '@vue/test-utils';

import type LoginService from '@/account/login.service';
import { createRouter } from '@/router';
import { useStore } from '@/store';

import JhiNavbar from './jhi-navbar.vue';

vi.mock('@module-federation/enhanced/runtime', () => ({
  loadRemote: vi.fn(() => Promise.reject(new Error('Test only'))),
}));

type JhiNavbarComponentType = InstanceType<typeof JhiNavbar>;

const pinia = createTestingPinia({ stubActions: false });
const store = useStore();

describe('JhiNavbar', () => {
  let jhiNavbar: JhiNavbarComponentType;
  let loginService: LoginService;
  const accountService = { hasAnyAuthorityAndCheckAuth: vi.fn().mockImplementation(() => Promise.resolve(true)) };
  const changeLanguage = vi.fn();
  let router: Router;

  beforeEach(() => {
    router = createRouter();
    loginService = { login: vi.fn(), logout: vi.fn() };
    const wrapper = shallowMount(JhiNavbar, {
      global: {
        plugins: [pinia, router],
        stubs: {
          'font-awesome-icon': true,
          'b-navbar': true,
          'b-navbar-nav': true,
          'b-dropdown-item': true,
          'b-collapse': true,
          'b-nav-item': true,
          'b-nav-item-dropdown': true,
          'b-navbar-toggle': true,
          'b-navbar-brand': true,
        },
        provide: {
          loginService,
          currentLanguage: computed(() => 'foo'),
          changeLanguage,
          accountService,
        },
      },
    });
    jhiNavbar = wrapper.vm;
  });

  it('should not have user data set', () => {
    expect(jhiNavbar.authenticated).toBeFalsy();
    expect(jhiNavbar.openAPIEnabled).toBeFalsy();
    expect(jhiNavbar.inProduction).toBeFalsy();
  });

  it('should have user data set after authentication', () => {
    store.setAuthentication({ login: 'test' });

    expect(jhiNavbar.authenticated).toBeTruthy();
  });

  it('should have profile info set after info retrieved', () => {
    store.setActiveProfiles(['prod', 'api-docs']);

    expect(jhiNavbar.openAPIEnabled).toBeTruthy();
    expect(jhiNavbar.inProduction).toBeTruthy();
  });

  it('should use login service', () => {
    jhiNavbar.login();

    expect(loginService.login).toHaveBeenCalled();
  });

  it('should use account service', () => {
    jhiNavbar.hasAnyAuthority('auth');

    expect(accountService.hasAnyAuthorityAndCheckAuth).toHaveBeenCalled();
  });

  it('logout should clear credentials and return to the home page when no provider URL is returned', async () => {
    store.setAuthentication({ login: 'test' });
    await router.push('/forbidden');
    (loginService.logout as any).mockReturnValue(Promise.resolve({}));

    await jhiNavbar.logout();

    expect(loginService.logout).toHaveBeenCalled();
    expect(jhiNavbar.authenticated).toBeFalsy();
    expect(router.currentRoute.value.path).toBe('/');
  });

  it('logout should not navigate with router for external url', async () => {
    store.setAuthentication({ login: 'test' });
    const logoutUrl = 'http://keycloak:9080/logout';
    (loginService.logout as any).mockReturnValue(Promise.resolve({ data: { logoutUrl } }));
    const routerPushSpy = vi.spyOn(router, 'push');

    await jhiNavbar.logout();

    expect(loginService.logout).toHaveBeenCalled();
    expect(routerPushSpy).not.toHaveBeenCalled();
  });

  it('should determine active route', async () => {
    await router.push('/forbidden');

    expect(jhiNavbar.subIsActive('/titi')).toBeFalsy();
    expect(jhiNavbar.subIsActive('/forbidden')).toBeTruthy();
    expect(jhiNavbar.subIsActive(['/forbidden', 'forbidden'])).toBeTruthy();
  });

  it('should call translationService when changing language', () => {
    jhiNavbar.changeLanguage('fr');

    expect(changeLanguage).toHaveBeenCalled();
  });

  it('should check for correct language', () => {
    expect(jhiNavbar.isActiveLanguage('en')).toBeFalsy();
    expect(jhiNavbar.isActiveLanguage('foo')).toBeTruthy();
  });
});

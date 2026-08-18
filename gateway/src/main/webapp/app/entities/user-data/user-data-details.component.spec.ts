import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import UserDataDetails from './user-data-details.vue';

type UserDataDetailsComponentType = InstanceType<typeof UserDataDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const userDataSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('UserData Management Detail Component', () => {
    let userDataServiceStub: any;
    let mountOptions: MountingOptions<UserDataDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      userDataServiceStub = {
        find: vi.fn(),
      };

      alertService = new AlertService({
        i18n: { t: vi.fn() } as any,
        toast: {
          create: vi.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          userDataService: () => userDataServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        userDataServiceStub.find.mockResolvedValue(userDataSample);
        route = {
          params: {
            userDataId: `${123}`,
          },
        };
        const wrapper = shallowMount(UserDataDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.userData).toMatchObject(userDataSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        userDataServiceStub.find.mockResolvedValue(userDataSample);
        const wrapper = shallowMount(UserDataDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

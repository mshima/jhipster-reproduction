import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import NotificationDetails from './notification-details.vue';

type NotificationDetailsComponentType = InstanceType<typeof NotificationDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const notificationSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('Notification Management Detail Component', () => {
    let notificationServiceStub: any;
    let mountOptions: MountingOptions<NotificationDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      notificationServiceStub = {
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
          notificationService: () => notificationServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        notificationServiceStub.find.mockResolvedValue(notificationSample);
        route = {
          params: {
            notificationId: `${123}`,
          },
        };
        const wrapper = shallowMount(NotificationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.notification).toMatchObject(notificationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        notificationServiceStub.find.mockResolvedValue(notificationSample);
        const wrapper = shallowMount(NotificationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

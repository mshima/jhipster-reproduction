import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import Notification from './notification.vue';

type NotificationComponentType = InstanceType<typeof Notification>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Notification Management Component', () => {
    let notificationServiceStub: any;
    let mountOptions: MountingOptions<NotificationComponentType>['global'];

    beforeEach(() => {
      notificationServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      notificationServiceStub.retrieve.mockResolvedValue({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vi.fn() } as any,
        toast: {
          create: vi.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          notificationService: () => notificationServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        notificationServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Notification, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(notificationServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.notifications[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: NotificationComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Notification, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        notificationServiceStub.retrieve.mockReset();
        notificationServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        notificationServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeNotification();
        await comp.$nextTick(); // clear components

        // THEN
        expect(notificationServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(notificationServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});

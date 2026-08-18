import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import UserData from './user-data.vue';

type UserDataComponentType = InstanceType<typeof UserData>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('UserData Management Component', () => {
    let userDataServiceStub: any;
    let mountOptions: MountingOptions<UserDataComponentType>['global'];

    beforeEach(() => {
      userDataServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      userDataServiceStub.retrieve.mockResolvedValue({ headers: {} });

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
          userDataService: () => userDataServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        userDataServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(UserData, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(userDataServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.userDatas[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: UserDataComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(UserData, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        userDataServiceStub.retrieve.mockReset();
        userDataServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        userDataServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeUserData();
        await comp.$nextTick(); // clear components

        // THEN
        expect(userDataServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(userDataServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});

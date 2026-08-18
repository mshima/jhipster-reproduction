import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import UserDataUpdate from './user-data-update.vue';

type UserDataUpdateComponentType = InstanceType<typeof UserDataUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const userDataSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<UserDataUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('UserData Management Update Component', () => {
    let comp: UserDataUpdateComponentType;
    let userDataServiceStub: any;

    beforeEach(() => {
      route = {};
      userDataServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      userDataServiceStub.retrieve.mockResolvedValueOnce([]);

      alertService = new AlertService({
        i18n: { t: vi.fn() } as any,
        toast: {
          create: vi.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          userDataService: () => userDataServiceStub,
        },
      };
    });

    afterEach(() => {
      vi.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(UserDataUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.userData = userDataSample;
        userDataServiceStub.update.mockResolvedValue(userDataSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(userDataServiceStub.update).toHaveBeenCalledWith(userDataSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        userDataServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(UserDataUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.userData = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(userDataServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        userDataServiceStub.find.mockResolvedValue(userDataSample);
        userDataServiceStub.retrieve.mockResolvedValue([userDataSample]);

        // WHEN
        route = {
          params: {
            userDataId: `${userDataSample.id}`,
          },
        };
        const wrapper = shallowMount(UserDataUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.userData).toMatchObject(userDataSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        userDataServiceStub.find.mockResolvedValue(userDataSample);
        const wrapper = shallowMount(UserDataUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

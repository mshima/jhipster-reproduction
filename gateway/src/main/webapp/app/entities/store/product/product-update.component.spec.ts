import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import ProductUpdate from './product-update.vue';

type ProductUpdateComponentType = InstanceType<typeof ProductUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const productSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ProductUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Product Management Update Component', () => {
    let comp: ProductUpdateComponentType;
    let productServiceStub: any;

    beforeEach(() => {
      route = {};
      productServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      productServiceStub.retrieve.mockResolvedValueOnce([]);

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
          productService: () => productServiceStub,
        },
      };
    });

    afterEach(() => {
      vi.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ProductUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.product = productSample;
        productServiceStub.update.mockResolvedValue(productSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(productServiceStub.update).toHaveBeenCalledWith(productSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        productServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(ProductUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.product = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(productServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        productServiceStub.find.mockResolvedValue(productSample);
        productServiceStub.retrieve.mockResolvedValue([productSample]);

        // WHEN
        route = {
          params: {
            productId: `${productSample.id}`,
          },
        };
        const wrapper = shallowMount(ProductUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.product).toMatchObject(productSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        productServiceStub.find.mockResolvedValue(productSample);
        const wrapper = shallowMount(ProductUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import BlogUpdate from './blog-update.vue';

type BlogUpdateComponentType = InstanceType<typeof BlogUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const blogSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<BlogUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Blog Management Update Component', () => {
    let comp: BlogUpdateComponentType;
    let blogServiceStub: any;

    beforeEach(() => {
      route = {};
      blogServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      blogServiceStub.retrieve.mockResolvedValueOnce([]);

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
          blogService: () => blogServiceStub,

          userService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
        },
      };
    });

    afterEach(() => {
      vi.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(BlogUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.blog = blogSample;
        blogServiceStub.update.mockResolvedValue(blogSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(blogServiceStub.update).toHaveBeenCalledWith(blogSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        blogServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(BlogUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.blog = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(blogServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        blogServiceStub.find.mockResolvedValue(blogSample);
        blogServiceStub.retrieve.mockResolvedValue([blogSample]);

        // WHEN
        route = {
          params: {
            blogId: `${blogSample.id}`,
          },
        };
        const wrapper = shallowMount(BlogUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.blog).toMatchObject(blogSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        blogServiceStub.find.mockResolvedValue(blogSample);
        const wrapper = shallowMount(BlogUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

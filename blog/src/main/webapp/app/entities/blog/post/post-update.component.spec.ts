import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PostUpdate from './post-update.vue';

type PostUpdateComponentType = InstanceType<typeof PostUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const postSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PostUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Post Management Update Component', () => {
    let comp: PostUpdateComponentType;
    let postServiceStub: any;

    beforeEach(() => {
      route = {};
      postServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      postServiceStub.retrieve.mockResolvedValueOnce([]);

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
          postService: () => postServiceStub,
          blogService: () => ({
            retrieve: vi.fn().mockResolvedValue({}),
          }),
          tagService: () => ({
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
        const wrapper = shallowMount(PostUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.post = postSample;
        postServiceStub.update.mockResolvedValue(postSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(postServiceStub.update).toHaveBeenCalledWith(postSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        postServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(PostUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.post = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(postServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        postServiceStub.find.mockResolvedValue(postSample);
        postServiceStub.retrieve.mockResolvedValue([postSample]);

        // WHEN
        route = {
          params: {
            postId: `${postSample.id}`,
          },
        };
        const wrapper = shallowMount(PostUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.post).toMatchObject(postSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        postServiceStub.find.mockResolvedValue(postSample);
        const wrapper = shallowMount(PostUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import TagUpdate from './tag-update.vue';

type TagUpdateComponentType = InstanceType<typeof TagUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tagSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TagUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Tag Management Update Component', () => {
    let comp: TagUpdateComponentType;
    let tagServiceStub: any;

    beforeEach(() => {
      route = {};
      tagServiceStub = {
        retrieve: vi.fn(),
        find: vi.fn(),
        update: vi.fn(),
        create: vi.fn(),
      };
      tagServiceStub.retrieve.mockResolvedValueOnce([]);

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
          tagService: () => tagServiceStub,
          postService: () => ({
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
        const wrapper = shallowMount(TagUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tag = tagSample;
        tagServiceStub.update.mockResolvedValue(tagSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tagServiceStub.update).toHaveBeenCalledWith(tagSample);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        tagServiceStub.create.mockResolvedValue(entity);
        const wrapper = shallowMount(TagUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tag = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tagServiceStub.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        tagServiceStub.find.mockResolvedValue(tagSample);
        tagServiceStub.retrieve.mockResolvedValue([tagSample]);

        // WHEN
        route = {
          params: {
            tagId: `${tagSample.id}`,
          },
        };
        const wrapper = shallowMount(TagUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.tag).toMatchObject(tagSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tagServiceStub.find.mockResolvedValue(tagSample);
        const wrapper = shallowMount(TagUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

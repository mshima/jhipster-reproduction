import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import TagDetails from './tag-details.vue';

type TagDetailsComponentType = InstanceType<typeof TagDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tagSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('Tag Management Detail Component', () => {
    let tagServiceStub: any;
    let mountOptions: MountingOptions<TagDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      tagServiceStub = {
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
          tagService: () => tagServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        tagServiceStub.find.mockResolvedValue(tagSample);
        route = {
          params: {
            tagId: `${123}`,
          },
        };
        const wrapper = shallowMount(TagDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.tag).toMatchObject(tagSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tagServiceStub.find.mockResolvedValue(tagSample);
        const wrapper = shallowMount(TagDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

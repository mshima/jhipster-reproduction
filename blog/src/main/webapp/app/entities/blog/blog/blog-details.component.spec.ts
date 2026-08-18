import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import BlogDetails from './blog-details.vue';

type BlogDetailsComponentType = InstanceType<typeof BlogDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const blogSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('Blog Management Detail Component', () => {
    let blogServiceStub: any;
    let mountOptions: MountingOptions<BlogDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      blogServiceStub = {
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
          blogService: () => blogServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        blogServiceStub.find.mockResolvedValue(blogSample);
        route = {
          params: {
            blogId: `${123}`,
          },
        };
        const wrapper = shallowMount(BlogDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.blog).toMatchObject(blogSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        blogServiceStub.find.mockResolvedValue(blogSample);
        const wrapper = shallowMount(BlogDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

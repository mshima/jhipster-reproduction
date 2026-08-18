import { beforeEach, describe, expect, it, vi } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import PostDetails from './post-details.vue';

type PostDetailsComponentType = InstanceType<typeof PostDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const postSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('Post Management Detail Component', () => {
    let postServiceStub: any;
    let mountOptions: MountingOptions<PostDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      postServiceStub = {
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
          postService: () => postServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        postServiceStub.find.mockResolvedValue(postSample);
        route = {
          params: {
            postId: `${123}`,
          },
        };
        const wrapper = shallowMount(PostDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.post).toMatchObject(postSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        postServiceStub.find.mockResolvedValue(postSample);
        const wrapper = shallowMount(PostDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

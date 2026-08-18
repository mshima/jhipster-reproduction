import { beforeEach, describe, expect, it } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';

import AlertService from '@/shared/alert/alert.service';

import Blog from './blog.vue';

type BlogComponentType = InstanceType<typeof Blog>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Blog Management Component', () => {
    let blogServiceStub: any;
    let mountOptions: MountingOptions<BlogComponentType>['global'];

    beforeEach(() => {
      blogServiceStub = {
        retrieve: vi.fn(),
        delete: vi.fn(),
      };
      blogServiceStub.retrieve.mockResolvedValue({ headers: {} });

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
          blogService: () => blogServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        blogServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Blog, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(blogServiceStub.retrieve).toHaveBeenCalledOnce();
        expect(comp.blogs[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: BlogComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Blog, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        blogServiceStub.retrieve.mockReset();
        blogServiceStub.retrieve.mockResolvedValue({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        blogServiceStub.delete.mockResolvedValue({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeBlog();
        await comp.$nextTick(); // clear components

        // THEN
        expect(blogServiceStub.delete).toHaveBeenCalled();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(blogServiceStub.retrieve).toHaveBeenCalledTimes(1);
      });
    });
  });
});

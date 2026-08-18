import { beforeEach, describe, expect, it, vi } from 'vitest';

import { shallowMount } from '@vue/test-utils';

import GatewayService from './gateway.service';
import Gateway from './gateway.vue';

type GatewayComponentType = InstanceType<typeof Gateway>;

describe('Gateway Component', () => {
  let wrapper;
  let comp: GatewayComponentType;

  beforeEach(() => {
    const gatewayService = new GatewayService();
    vi.spyOn(gatewayService, 'findAll').mockResolvedValue({ data: [] });
    wrapper = shallowMount(Gateway, {
      global: {
        stubs: {
          'font-awesome-icon': true,
        },
        provide: { gatewayService },
      },
    });
    comp = wrapper.vm;
  });

  it('should be a Vue instance', () => {
    expect(wrapper.get('#gateway-page-heading'));
  });
});

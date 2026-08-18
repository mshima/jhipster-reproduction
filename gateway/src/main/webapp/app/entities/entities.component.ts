import { defineComponent, provide } from 'vue';

import UserService from '@/entities/user/user.service';

import ProductService from './store/product/product.service';
import UserDataService from './user-data/user-data.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('userDataService', () => new UserDataService());
    provide('productService', () => new ProductService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});

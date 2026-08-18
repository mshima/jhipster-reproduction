import { Authority } from '@/shared/jhipster/constants';
const Entities = () => import('@/entities/entities.vue');

const UserData = () => import('@/entities/user-data/user-data.vue');
const UserDataUpdate = () => import('@/entities/user-data/user-data-update.vue');
const UserDataDetails = () => import('@/entities/user-data/user-data-details.vue');

const Product = () => import('@/entities/store/product/product.vue');
const ProductUpdate = () => import('@/entities/store/product/product-update.vue');
const ProductDetails = () => import('@/entities/store/product/product-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'user-data',
      name: 'UserData',
      component: UserData,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-data/new',
      name: 'UserDataCreate',
      component: UserDataUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-data/:userDataId/edit',
      name: 'UserDataEdit',
      component: UserDataUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-data/:userDataId/view',
      name: 'UserDataView',
      component: UserDataDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product',
      name: 'Product',
      component: Product,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product/new',
      name: 'ProductCreate',
      component: ProductUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product/:productId/edit',
      name: 'ProductEdit',
      component: ProductUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product/:productId/view',
      name: 'ProductView',
      component: ProductDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};

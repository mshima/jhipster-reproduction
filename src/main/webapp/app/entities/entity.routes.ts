import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'sampleWebfluxPsqlApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'operation',
    data: { pageTitle: 'sampleWebfluxPsqlApp.testRootOperation.home.title' },
    loadChildren: () => import('./test-root/operation/operation.routes'),
  },
  {
    path: 'field-test-entity',
    data: { pageTitle: 'sampleWebfluxPsqlApp.fieldTestEntity.home.title' },
    loadChildren: () => import('./field-test-entity/field-test-entity.routes'),
  },
  {
    path: 'field-test-infinite-scroll-entity',
    data: { pageTitle: 'sampleWebfluxPsqlApp.fieldTestInfiniteScrollEntity.home.title' },
    loadChildren: () => import('./field-test-infinite-scroll-entity/field-test-infinite-scroll-entity.routes'),
  },
  {
    path: 'field-test-mapstruct-and-service-class-entity',
    data: { pageTitle: 'sampleWebfluxPsqlApp.fieldTestMapstructAndServiceClassEntity.home.title' },
    loadChildren: () => import('./field-test-mapstruct-and-service-class-entity/field-test-mapstruct-and-service-class-entity.routes'),
  },
  {
    path: 'field-test-pagination-entity',
    data: { pageTitle: 'sampleWebfluxPsqlApp.fieldTestPaginationEntity.home.title' },
    loadChildren: () => import('./field-test-pagination-entity/field-test-pagination-entity.routes'),
  },
  {
    path: 'field-test-service-class-and-jpa-filtering-entity',
    data: { pageTitle: 'sampleWebfluxPsqlApp.fieldTestServiceClassAndJpaFilteringEntity.home.title' },
    loadChildren: () =>
      import('./field-test-service-class-and-jpa-filtering-entity/field-test-service-class-and-jpa-filtering-entity.routes'),
  },
  {
    path: 'field-test-service-impl-entity',
    data: { pageTitle: 'sampleWebfluxPsqlApp.fieldTestServiceImplEntity.home.title' },
    loadChildren: () => import('./field-test-service-impl-entity/field-test-service-impl-entity.routes'),
  },
  {
    path: 'entity-with-dto',
    data: { pageTitle: 'sampleWebfluxPsqlApp.entityWithDTO.home.title' },
    loadChildren: () => import('./entity-with-dto/entity-with-dto.routes'),
  },
  {
    path: 'entity-with-service-class-and-pagination',
    data: { pageTitle: 'sampleWebfluxPsqlApp.entityWithServiceClassAndPagination.home.title' },
    loadChildren: () => import('./entity-with-service-class-and-pagination/entity-with-service-class-and-pagination.routes'),
  },
  {
    path: 'entity-with-service-impl-and-pagination',
    data: { pageTitle: 'sampleWebfluxPsqlApp.entityWithServiceImplAndPagination.home.title' },
    loadChildren: () => import('./entity-with-service-impl-and-pagination/entity-with-service-impl-and-pagination.routes'),
  },
  {
    path: 'entity-with-service-impl-and-dto',
    data: { pageTitle: 'sampleWebfluxPsqlApp.entityWithServiceImplAndDTO.home.title' },
    loadChildren: () => import('./entity-with-service-impl-and-dto/entity-with-service-impl-and-dto.routes'),
  },
  {
    path: 'entity-with-pagination-and-dto',
    data: { pageTitle: 'sampleWebfluxPsqlApp.entityWithPaginationAndDTO.home.title' },
    loadChildren: () => import('./entity-with-pagination-and-dto/entity-with-pagination-and-dto.routes'),
  },
  {
    path: 'entity-with-service-class-pagination-and-dto',
    data: { pageTitle: 'sampleWebfluxPsqlApp.entityWithServiceClassPaginationAndDTO.home.title' },
    loadChildren: () => import('./entity-with-service-class-pagination-and-dto/entity-with-service-class-pagination-and-dto.routes'),
  },
  {
    path: 'entity-with-service-impl-pagination-and-dto',
    data: { pageTitle: 'sampleWebfluxPsqlApp.entityWithServiceImplPaginationAndDTO.home.title' },
    loadChildren: () => import('./entity-with-service-impl-pagination-and-dto/entity-with-service-impl-pagination-and-dto.routes'),
  },
  {
    path: 'maps-id-user-profile-with-dto',
    data: { pageTitle: 'sampleWebfluxPsqlApp.mapsIdUserProfileWithDTO.home.title' },
    loadChildren: () => import('./maps-id-user-profile-with-dto/maps-id-user-profile-with-dto.routes'),
  },
  {
    path: 'field-test-enum-with-value',
    data: { pageTitle: 'sampleWebfluxPsqlApp.fieldTestEnumWithValue.home.title' },
    loadChildren: () => import('./field-test-enum-with-value/field-test-enum-with-value.routes'),
  },
  {
    path: 'bank-account-my-suffix',
    data: { pageTitle: 'sampleWebfluxPsqlApp.testRootBankAccount.home.title' },
    loadChildren: () => import('./test-root/bank-account-my-suffix/bank-account-my-suffix.routes'),
  },
  {
    path: 'label',
    data: { pageTitle: 'sampleWebfluxPsqlApp.testRootLabel.home.title' },
    loadChildren: () => import('./test-root/label/label.routes'),
  },
  {
    path: 'user-management',
    data: { pageTitle: 'userManagement.home.title' },
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  // jhipster-needle-add-entity-route - JHipster will add entity modules routes here
];

export default routes;

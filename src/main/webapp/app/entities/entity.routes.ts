import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'sampleWebfluxH2MemApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'operation',
    data: { pageTitle: 'sampleWebfluxH2MemApp.testRootOperation.home.title' },
    loadChildren: () => import('./test-root/operation/operation.routes'),
  },
  {
    path: 'custom-package-parent',
    data: { pageTitle: 'sampleWebfluxH2MemApp.customPackageParent.home.title' },
    loadChildren: () => import('./custom-package-parent/custom-package-parent.routes'),
  },
  {
    path: 'custom-package-child',
    data: { pageTitle: 'sampleWebfluxH2MemApp.customPackageChild.home.title' },
    loadChildren: () => import('./custom-package-child/custom-package-child.routes'),
  },
  {
    path: 'bank-account-my-suffix',
    data: { pageTitle: 'sampleWebfluxH2MemApp.testRootBankAccount.home.title' },
    loadChildren: () => import('./test-root/bank-account-my-suffix/bank-account-my-suffix.routes'),
  },
  {
    path: 'label',
    data: { pageTitle: 'sampleWebfluxH2MemApp.testRootLabel.home.title' },
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

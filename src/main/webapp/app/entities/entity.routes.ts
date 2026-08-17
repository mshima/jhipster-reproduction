import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'jhipsterApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'custom-package-parent',
    data: { pageTitle: 'jhipsterApp.customPackageParent.home.title' },
    loadChildren: () => import('./custom-package-parent/custom-package-parent.routes'),
  },
  {
    path: 'custom-package-child',
    data: { pageTitle: 'jhipsterApp.customPackageChild.home.title' },
    loadChildren: () => import('./custom-package-child/custom-package-child.routes'),
  },
  {
    path: 'user-management',
    data: { pageTitle: 'userManagement.home.title' },
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  // jhipster-needle-add-entity-route - JHipster will add entity modules routes here
];

export default routes;

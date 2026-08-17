import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { userRouteAccessService } from 'app/core/auth/user-route-access.service';

import CustomPackageParentResolve from './route/custom-package-parent-routing-resolve.service';

const customPackageParentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/custom-package-parent').then(m => m.CustomPackageParent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/custom-package-parent-detail').then(m => m.CustomPackageParentDetail),
    resolve: {
      customPackageParent: CustomPackageParentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/custom-package-parent-update').then(m => m.CustomPackageParentUpdate),
    resolve: {
      customPackageParent: CustomPackageParentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/custom-package-parent-update').then(m => m.CustomPackageParentUpdate),
    resolve: {
      customPackageParent: CustomPackageParentResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default customPackageParentRoute;

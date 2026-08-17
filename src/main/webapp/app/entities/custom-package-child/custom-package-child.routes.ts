import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { userRouteAccessService } from 'app/core/auth/user-route-access.service';

import CustomPackageChildResolve from './route/custom-package-child-routing-resolve.service';

const customPackageChildRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/custom-package-child').then(m => m.CustomPackageChild),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/custom-package-child-detail').then(m => m.CustomPackageChildDetail),
    resolve: {
      customPackageChild: CustomPackageChildResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/custom-package-child-update').then(m => m.CustomPackageChildUpdate),
    resolve: {
      customPackageChild: CustomPackageChildResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/custom-package-child-update').then(m => m.CustomPackageChildUpdate),
    resolve: {
      customPackageChild: CustomPackageChildResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default customPackageChildRoute;

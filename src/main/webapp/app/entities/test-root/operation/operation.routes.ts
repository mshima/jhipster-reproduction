import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import OperationResolve from './route/operation-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const operationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/operation').then(m => m.Operation),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/operation-detail').then(m => m.OperationDetail),
    resolve: {
      operation: OperationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/operation-update').then(m => m.OperationUpdate),
    resolve: {
      operation: OperationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/operation-update').then(m => m.OperationUpdate),
    resolve: {
      operation: OperationResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default operationRoute;

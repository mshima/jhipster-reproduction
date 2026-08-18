import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import EntityWithServiceClassAndPaginationResolve from './route/entity-with-service-class-and-pagination-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const entityWithServiceClassAndPaginationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/entity-with-service-class-and-pagination').then(m => m.EntityWithServiceClassAndPagination),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/entity-with-service-class-and-pagination-detail').then(m => m.EntityWithServiceClassAndPaginationDetail),
    resolve: {
      entityWithServiceClassAndPagination: EntityWithServiceClassAndPaginationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/entity-with-service-class-and-pagination-update').then(m => m.EntityWithServiceClassAndPaginationUpdate),
    resolve: {
      entityWithServiceClassAndPagination: EntityWithServiceClassAndPaginationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/entity-with-service-class-and-pagination-update').then(m => m.EntityWithServiceClassAndPaginationUpdate),
    resolve: {
      entityWithServiceClassAndPagination: EntityWithServiceClassAndPaginationResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default entityWithServiceClassAndPaginationRoute;

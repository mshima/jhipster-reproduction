import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import EntityWithServiceImplAndPaginationResolve from './route/entity-with-service-impl-and-pagination-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const entityWithServiceImplAndPaginationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/entity-with-service-impl-and-pagination').then(m => m.EntityWithServiceImplAndPagination),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/entity-with-service-impl-and-pagination-detail').then(m => m.EntityWithServiceImplAndPaginationDetail),
    resolve: {
      entityWithServiceImplAndPagination: EntityWithServiceImplAndPaginationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/entity-with-service-impl-and-pagination-update').then(m => m.EntityWithServiceImplAndPaginationUpdate),
    resolve: {
      entityWithServiceImplAndPagination: EntityWithServiceImplAndPaginationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/entity-with-service-impl-and-pagination-update').then(m => m.EntityWithServiceImplAndPaginationUpdate),
    resolve: {
      entityWithServiceImplAndPagination: EntityWithServiceImplAndPaginationResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default entityWithServiceImplAndPaginationRoute;

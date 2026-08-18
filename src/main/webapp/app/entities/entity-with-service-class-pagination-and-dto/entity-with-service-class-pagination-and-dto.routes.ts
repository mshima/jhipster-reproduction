import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import EntityWithServiceClassPaginationAndDTOResolve from './route/entity-with-service-class-pagination-and-dto-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const entityWithServiceClassPaginationAndDTORoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/entity-with-service-class-pagination-and-dto').then(m => m.EntityWithServiceClassPaginationAndDTO),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/entity-with-service-class-pagination-and-dto-detail').then(m => m.EntityWithServiceClassPaginationAndDTODetail),
    resolve: {
      entityWithServiceClassPaginationAndDTO: EntityWithServiceClassPaginationAndDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/entity-with-service-class-pagination-and-dto-update').then(m => m.EntityWithServiceClassPaginationAndDTOUpdate),
    resolve: {
      entityWithServiceClassPaginationAndDTO: EntityWithServiceClassPaginationAndDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/entity-with-service-class-pagination-and-dto-update').then(m => m.EntityWithServiceClassPaginationAndDTOUpdate),
    resolve: {
      entityWithServiceClassPaginationAndDTO: EntityWithServiceClassPaginationAndDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default entityWithServiceClassPaginationAndDTORoute;

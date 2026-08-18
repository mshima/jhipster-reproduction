import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import EntityWithPaginationAndDTOResolve from './route/entity-with-pagination-and-dto-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const entityWithPaginationAndDTORoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/entity-with-pagination-and-dto').then(m => m.EntityWithPaginationAndDTO),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/entity-with-pagination-and-dto-detail').then(m => m.EntityWithPaginationAndDTODetail),
    resolve: {
      entityWithPaginationAndDTO: EntityWithPaginationAndDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/entity-with-pagination-and-dto-update').then(m => m.EntityWithPaginationAndDTOUpdate),
    resolve: {
      entityWithPaginationAndDTO: EntityWithPaginationAndDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/entity-with-pagination-and-dto-update').then(m => m.EntityWithPaginationAndDTOUpdate),
    resolve: {
      entityWithPaginationAndDTO: EntityWithPaginationAndDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default entityWithPaginationAndDTORoute;

import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import EntityWithServiceImplPaginationAndDTOResolve from './route/entity-with-service-impl-pagination-and-dto-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const entityWithServiceImplPaginationAndDTORoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/entity-with-service-impl-pagination-and-dto').then(m => m.EntityWithServiceImplPaginationAndDTO),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/entity-with-service-impl-pagination-and-dto-detail').then(m => m.EntityWithServiceImplPaginationAndDTODetail),
    resolve: {
      entityWithServiceImplPaginationAndDTO: EntityWithServiceImplPaginationAndDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/entity-with-service-impl-pagination-and-dto-update').then(m => m.EntityWithServiceImplPaginationAndDTOUpdate),
    resolve: {
      entityWithServiceImplPaginationAndDTO: EntityWithServiceImplPaginationAndDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/entity-with-service-impl-pagination-and-dto-update').then(m => m.EntityWithServiceImplPaginationAndDTOUpdate),
    resolve: {
      entityWithServiceImplPaginationAndDTO: EntityWithServiceImplPaginationAndDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default entityWithServiceImplPaginationAndDTORoute;

import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import EntityWithServiceImplAndDTOResolve from './route/entity-with-service-impl-and-dto-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const entityWithServiceImplAndDTORoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/entity-with-service-impl-and-dto').then(m => m.EntityWithServiceImplAndDTO),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/entity-with-service-impl-and-dto-detail').then(m => m.EntityWithServiceImplAndDTODetail),
    resolve: {
      entityWithServiceImplAndDTO: EntityWithServiceImplAndDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/entity-with-service-impl-and-dto-update').then(m => m.EntityWithServiceImplAndDTOUpdate),
    resolve: {
      entityWithServiceImplAndDTO: EntityWithServiceImplAndDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/entity-with-service-impl-and-dto-update').then(m => m.EntityWithServiceImplAndDTOUpdate),
    resolve: {
      entityWithServiceImplAndDTO: EntityWithServiceImplAndDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default entityWithServiceImplAndDTORoute;

import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import EntityWithDTOResolve from './route/entity-with-dto-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const entityWithDTORoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/entity-with-dto').then(m => m.EntityWithDTO),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/entity-with-dto-detail').then(m => m.EntityWithDTODetail),
    resolve: {
      entityWithDTO: EntityWithDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/entity-with-dto-update').then(m => m.EntityWithDTOUpdate),
    resolve: {
      entityWithDTO: EntityWithDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/entity-with-dto-update').then(m => m.EntityWithDTOUpdate),
    resolve: {
      entityWithDTO: EntityWithDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default entityWithDTORoute;

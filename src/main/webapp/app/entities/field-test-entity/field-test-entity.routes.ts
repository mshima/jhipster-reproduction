import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import FieldTestEntityResolve from './route/field-test-entity-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fieldTestEntityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/field-test-entity').then(m => m.FieldTestEntity),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/field-test-entity-detail').then(m => m.FieldTestEntityDetail),
    resolve: {
      fieldTestEntity: FieldTestEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/field-test-entity-update').then(m => m.FieldTestEntityUpdate),
    resolve: {
      fieldTestEntity: FieldTestEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/field-test-entity-update').then(m => m.FieldTestEntityUpdate),
    resolve: {
      fieldTestEntity: FieldTestEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default fieldTestEntityRoute;

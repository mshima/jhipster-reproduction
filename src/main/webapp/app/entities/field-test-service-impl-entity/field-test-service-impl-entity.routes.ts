import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import FieldTestServiceImplEntityResolve from './route/field-test-service-impl-entity-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fieldTestServiceImplEntityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/field-test-service-impl-entity').then(m => m.FieldTestServiceImplEntity),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/field-test-service-impl-entity-detail').then(m => m.FieldTestServiceImplEntityDetail),
    resolve: {
      fieldTestServiceImplEntity: FieldTestServiceImplEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/field-test-service-impl-entity-update').then(m => m.FieldTestServiceImplEntityUpdate),
    resolve: {
      fieldTestServiceImplEntity: FieldTestServiceImplEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/field-test-service-impl-entity-update').then(m => m.FieldTestServiceImplEntityUpdate),
    resolve: {
      fieldTestServiceImplEntity: FieldTestServiceImplEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default fieldTestServiceImplEntityRoute;

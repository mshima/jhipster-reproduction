import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import FieldTestEnumWithValueResolve from './route/field-test-enum-with-value-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fieldTestEnumWithValueRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/field-test-enum-with-value').then(m => m.FieldTestEnumWithValue),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/field-test-enum-with-value-detail').then(m => m.FieldTestEnumWithValueDetail),
    resolve: {
      fieldTestEnumWithValue: FieldTestEnumWithValueResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/field-test-enum-with-value-update').then(m => m.FieldTestEnumWithValueUpdate),
    resolve: {
      fieldTestEnumWithValue: FieldTestEnumWithValueResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/field-test-enum-with-value-update').then(m => m.FieldTestEnumWithValueUpdate),
    resolve: {
      fieldTestEnumWithValue: FieldTestEnumWithValueResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default fieldTestEnumWithValueRoute;

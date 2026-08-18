import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import FieldTestPaginationEntityResolve from './route/field-test-pagination-entity-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fieldTestPaginationEntityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/field-test-pagination-entity').then(m => m.FieldTestPaginationEntity),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/field-test-pagination-entity-detail').then(m => m.FieldTestPaginationEntityDetail),
    resolve: {
      fieldTestPaginationEntity: FieldTestPaginationEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/field-test-pagination-entity-update').then(m => m.FieldTestPaginationEntityUpdate),
    resolve: {
      fieldTestPaginationEntity: FieldTestPaginationEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/field-test-pagination-entity-update').then(m => m.FieldTestPaginationEntityUpdate),
    resolve: {
      fieldTestPaginationEntity: FieldTestPaginationEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default fieldTestPaginationEntityRoute;

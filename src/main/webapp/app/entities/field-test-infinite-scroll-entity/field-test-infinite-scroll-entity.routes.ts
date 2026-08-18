import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import FieldTestInfiniteScrollEntityResolve from './route/field-test-infinite-scroll-entity-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fieldTestInfiniteScrollEntityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/field-test-infinite-scroll-entity').then(m => m.FieldTestInfiniteScrollEntity),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/field-test-infinite-scroll-entity-detail').then(m => m.FieldTestInfiniteScrollEntityDetail),
    resolve: {
      fieldTestInfiniteScrollEntity: FieldTestInfiniteScrollEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/field-test-infinite-scroll-entity-update').then(m => m.FieldTestInfiniteScrollEntityUpdate),
    resolve: {
      fieldTestInfiniteScrollEntity: FieldTestInfiniteScrollEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/field-test-infinite-scroll-entity-update').then(m => m.FieldTestInfiniteScrollEntityUpdate),
    resolve: {
      fieldTestInfiniteScrollEntity: FieldTestInfiniteScrollEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default fieldTestInfiniteScrollEntityRoute;

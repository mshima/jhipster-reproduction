import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import FieldTestServiceClassAndJpaFilteringEntityResolve from './route/field-test-service-class-and-jpa-filtering-entity-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fieldTestServiceClassAndJpaFilteringEntityRoute: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./list/field-test-service-class-and-jpa-filtering-entity').then(m => m.FieldTestServiceClassAndJpaFilteringEntity),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/field-test-service-class-and-jpa-filtering-entity-detail').then(
        m => m.FieldTestServiceClassAndJpaFilteringEntityDetail,
      ),
    resolve: {
      fieldTestServiceClassAndJpaFilteringEntity: FieldTestServiceClassAndJpaFilteringEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/field-test-service-class-and-jpa-filtering-entity-update').then(
        m => m.FieldTestServiceClassAndJpaFilteringEntityUpdate,
      ),
    resolve: {
      fieldTestServiceClassAndJpaFilteringEntity: FieldTestServiceClassAndJpaFilteringEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/field-test-service-class-and-jpa-filtering-entity-update').then(
        m => m.FieldTestServiceClassAndJpaFilteringEntityUpdate,
      ),
    resolve: {
      fieldTestServiceClassAndJpaFilteringEntity: FieldTestServiceClassAndJpaFilteringEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default fieldTestServiceClassAndJpaFilteringEntityRoute;

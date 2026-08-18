import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import FieldTestMapstructAndServiceClassEntityResolve from './route/field-test-mapstruct-and-service-class-entity-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fieldTestMapstructAndServiceClassEntityRoute: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./list/field-test-mapstruct-and-service-class-entity').then(m => m.FieldTestMapstructAndServiceClassEntity),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/field-test-mapstruct-and-service-class-entity-detail').then(m => m.FieldTestMapstructAndServiceClassEntityDetail),
    resolve: {
      fieldTestMapstructAndServiceClassEntity: FieldTestMapstructAndServiceClassEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/field-test-mapstruct-and-service-class-entity-update').then(m => m.FieldTestMapstructAndServiceClassEntityUpdate),
    resolve: {
      fieldTestMapstructAndServiceClassEntity: FieldTestMapstructAndServiceClassEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/field-test-mapstruct-and-service-class-entity-update').then(m => m.FieldTestMapstructAndServiceClassEntityUpdate),
    resolve: {
      fieldTestMapstructAndServiceClassEntity: FieldTestMapstructAndServiceClassEntityResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default fieldTestMapstructAndServiceClassEntityRoute;

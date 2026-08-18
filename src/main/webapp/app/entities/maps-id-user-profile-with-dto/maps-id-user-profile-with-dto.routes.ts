import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import MapsIdUserProfileWithDTOResolve from './route/maps-id-user-profile-with-dto-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const mapsIdUserProfileWithDTORoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/maps-id-user-profile-with-dto').then(m => m.MapsIdUserProfileWithDTO),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/maps-id-user-profile-with-dto-detail').then(m => m.MapsIdUserProfileWithDTODetail),
    resolve: {
      mapsIdUserProfileWithDTO: MapsIdUserProfileWithDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/maps-id-user-profile-with-dto-update').then(m => m.MapsIdUserProfileWithDTOUpdate),
    resolve: {
      mapsIdUserProfileWithDTO: MapsIdUserProfileWithDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/maps-id-user-profile-with-dto-update').then(m => m.MapsIdUserProfileWithDTOUpdate),
    resolve: {
      mapsIdUserProfileWithDTO: MapsIdUserProfileWithDTOResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default mapsIdUserProfileWithDTORoute;

import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import LabelResolve from './route/label-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const labelRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/label').then(m => m.Label),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/label-detail').then(m => m.LabelDetail),
    resolve: {
      label: LabelResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default labelRoute;

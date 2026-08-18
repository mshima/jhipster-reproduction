import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth/user-route-access.service';
import BankAccountMySuffixResolve from './route/bank-account-my-suffix-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const bankAccountRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bank-account-my-suffix').then(m => m.BankAccountMySuffix),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bank-account-my-suffix-detail').then(m => m.BankAccountMySuffixDetail),
    resolve: {
      bankAccount: BankAccountMySuffixResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bank-account-my-suffix-update').then(m => m.BankAccountMySuffixUpdate),
    resolve: {
      bankAccount: BankAccountMySuffixResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/bank-account-my-suffix-update').then(m => m.BankAccountMySuffixUpdate),
    resolve: {
      bankAccount: BankAccountMySuffixResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default bankAccountRoute;

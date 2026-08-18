import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IBankAccountMySuffix } from '../bank-account-my-suffix.model';
import { BankAccountMySuffixService } from '../service/bank-account-my-suffix.service';

const bankAccountResolve = (route: ActivatedRouteSnapshot): Observable<null | IBankAccountMySuffix> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(BankAccountMySuffixService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default bankAccountResolve;

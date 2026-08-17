import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICustomPackageChild } from '../custom-package-child.model';
import { CustomPackageChildService } from '../service/custom-package-child.service';

const customPackageChildResolve = (route: ActivatedRouteSnapshot): Observable<null | ICustomPackageChild> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CustomPackageChildService);
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

export default customPackageChildResolve;

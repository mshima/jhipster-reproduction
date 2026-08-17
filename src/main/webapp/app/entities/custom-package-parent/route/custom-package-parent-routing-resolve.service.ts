import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICustomPackageParent } from '../custom-package-parent.model';
import { CustomPackageParentService } from '../service/custom-package-parent.service';

const customPackageParentResolve = (route: ActivatedRouteSnapshot): Observable<null | ICustomPackageParent> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CustomPackageParentService);
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

export default customPackageParentResolve;

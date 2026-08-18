import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { catchError, EMPTY, Observable, of } from 'rxjs';

import { IFieldTestServiceImplEntity } from '../field-test-service-impl-entity.model';
import { FieldTestServiceImplEntityService } from '../service/field-test-service-impl-entity.service';

const fieldTestServiceImplEntityResolve = (route: ActivatedRouteSnapshot): Observable<null | IFieldTestServiceImplEntity> => {
  const id = route.params['id'];
  if (id) {
    const router = inject(Router);
    const service = inject(FieldTestServiceImplEntityService);
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

export default fieldTestServiceImplEntityResolve;

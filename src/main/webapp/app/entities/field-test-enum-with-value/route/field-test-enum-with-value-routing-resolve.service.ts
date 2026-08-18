import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { catchError, EMPTY, Observable, of } from 'rxjs';

import { IFieldTestEnumWithValue } from '../field-test-enum-with-value.model';
import { FieldTestEnumWithValueService } from '../service/field-test-enum-with-value.service';

const fieldTestEnumWithValueResolve = (route: ActivatedRouteSnapshot): Observable<null | IFieldTestEnumWithValue> => {
  const id = route.params['id'];
  if (id) {
    const router = inject(Router);
    const service = inject(FieldTestEnumWithValueService);
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

export default fieldTestEnumWithValueResolve;

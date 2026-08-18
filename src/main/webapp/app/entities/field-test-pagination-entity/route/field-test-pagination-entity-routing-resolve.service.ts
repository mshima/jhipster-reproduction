import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { catchError, EMPTY, Observable, of } from 'rxjs';

import { IFieldTestPaginationEntity } from '../field-test-pagination-entity.model';
import { FieldTestPaginationEntityService } from '../service/field-test-pagination-entity.service';

const fieldTestPaginationEntityResolve = (route: ActivatedRouteSnapshot): Observable<null | IFieldTestPaginationEntity> => {
  const id = route.params['id'];
  if (id) {
    const router = inject(Router);
    const service = inject(FieldTestPaginationEntityService);
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

export default fieldTestPaginationEntityResolve;

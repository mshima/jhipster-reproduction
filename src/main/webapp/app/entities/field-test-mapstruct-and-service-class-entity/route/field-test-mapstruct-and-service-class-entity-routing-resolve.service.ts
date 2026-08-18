import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { catchError, EMPTY, Observable, of } from 'rxjs';

import { IFieldTestMapstructAndServiceClassEntity } from '../field-test-mapstruct-and-service-class-entity.model';
import { FieldTestMapstructAndServiceClassEntityService } from '../service/field-test-mapstruct-and-service-class-entity.service';

const fieldTestMapstructAndServiceClassEntityResolve = (
  route: ActivatedRouteSnapshot,
): Observable<null | IFieldTestMapstructAndServiceClassEntity> => {
  const id = route.params['id'];
  if (id) {
    const router = inject(Router);
    const service = inject(FieldTestMapstructAndServiceClassEntityService);
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

export default fieldTestMapstructAndServiceClassEntityResolve;

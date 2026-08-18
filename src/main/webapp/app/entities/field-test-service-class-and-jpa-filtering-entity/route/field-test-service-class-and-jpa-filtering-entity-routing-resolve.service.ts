import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { catchError, EMPTY, Observable, of } from 'rxjs';

import { IFieldTestServiceClassAndJpaFilteringEntity } from '../field-test-service-class-and-jpa-filtering-entity.model';
import { FieldTestServiceClassAndJpaFilteringEntityService } from '../service/field-test-service-class-and-jpa-filtering-entity.service';

const fieldTestServiceClassAndJpaFilteringEntityResolve = (
  route: ActivatedRouteSnapshot,
): Observable<null | IFieldTestServiceClassAndJpaFilteringEntity> => {
  const id = route.params['id'];
  if (id) {
    const router = inject(Router);
    const service = inject(FieldTestServiceClassAndJpaFilteringEntityService);
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

export default fieldTestServiceClassAndJpaFilteringEntityResolve;

import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { catchError, EMPTY, Observable, of } from 'rxjs';

import { IEntityWithServiceClassAndPagination } from '../entity-with-service-class-and-pagination.model';
import { EntityWithServiceClassAndPaginationService } from '../service/entity-with-service-class-and-pagination.service';

const entityWithServiceClassAndPaginationResolve = (
  route: ActivatedRouteSnapshot,
): Observable<null | IEntityWithServiceClassAndPagination> => {
  const id = route.params['id'];
  if (id) {
    const router = inject(Router);
    const service = inject(EntityWithServiceClassAndPaginationService);
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

export default entityWithServiceClassAndPaginationResolve;

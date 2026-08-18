import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { catchError, EMPTY, Observable, of } from 'rxjs';

import { IEntityWithServiceImplAndPagination } from '../entity-with-service-impl-and-pagination.model';
import { EntityWithServiceImplAndPaginationService } from '../service/entity-with-service-impl-and-pagination.service';

const entityWithServiceImplAndPaginationResolve = (
  route: ActivatedRouteSnapshot,
): Observable<null | IEntityWithServiceImplAndPagination> => {
  const id = route.params['id'];
  if (id) {
    const router = inject(Router);
    const service = inject(EntityWithServiceImplAndPaginationService);
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

export default entityWithServiceImplAndPaginationResolve;

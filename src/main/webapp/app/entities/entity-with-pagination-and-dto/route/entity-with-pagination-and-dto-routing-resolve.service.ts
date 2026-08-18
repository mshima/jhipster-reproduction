import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { catchError, EMPTY, Observable, of } from 'rxjs';

import { IEntityWithPaginationAndDTO } from '../entity-with-pagination-and-dto.model';
import { EntityWithPaginationAndDTOService } from '../service/entity-with-pagination-and-dto.service';

const entityWithPaginationAndDTOResolve = (route: ActivatedRouteSnapshot): Observable<null | IEntityWithPaginationAndDTO> => {
  const id = route.params['id'];
  if (id) {
    const router = inject(Router);
    const service = inject(EntityWithPaginationAndDTOService);
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

export default entityWithPaginationAndDTOResolve;

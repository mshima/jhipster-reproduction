import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { catchError, EMPTY, Observable, of } from 'rxjs';

import { IEntityWithDTO } from '../entity-with-dto.model';
import { EntityWithDTOService } from '../service/entity-with-dto.service';

const entityWithDTOResolve = (route: ActivatedRouteSnapshot): Observable<null | IEntityWithDTO> => {
  const id = route.params['id'];
  if (id) {
    const router = inject(Router);
    const service = inject(EntityWithDTOService);
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

export default entityWithDTOResolve;

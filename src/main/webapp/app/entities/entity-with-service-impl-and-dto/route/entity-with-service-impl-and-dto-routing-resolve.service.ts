import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { catchError, EMPTY, Observable, of } from 'rxjs';

import { IEntityWithServiceImplAndDTO } from '../entity-with-service-impl-and-dto.model';
import { EntityWithServiceImplAndDTOService } from '../service/entity-with-service-impl-and-dto.service';

const entityWithServiceImplAndDTOResolve = (route: ActivatedRouteSnapshot): Observable<null | IEntityWithServiceImplAndDTO> => {
  const id = route.params['id'];
  if (id) {
    const router = inject(Router);
    const service = inject(EntityWithServiceImplAndDTOService);
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

export default entityWithServiceImplAndDTOResolve;

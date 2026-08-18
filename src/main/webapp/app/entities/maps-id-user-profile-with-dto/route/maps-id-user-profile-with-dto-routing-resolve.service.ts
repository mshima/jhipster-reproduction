import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { catchError, EMPTY, Observable, of } from 'rxjs';

import { IMapsIdUserProfileWithDTO } from '../maps-id-user-profile-with-dto.model';
import { MapsIdUserProfileWithDTOService } from '../service/maps-id-user-profile-with-dto.service';

const mapsIdUserProfileWithDTOResolve = (route: ActivatedRouteSnapshot): Observable<null | IMapsIdUserProfileWithDTO> => {
  const id = route.params['id'];
  if (id) {
    const router = inject(Router);
    const service = inject(MapsIdUserProfileWithDTOService);
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

export default mapsIdUserProfileWithDTOResolve;

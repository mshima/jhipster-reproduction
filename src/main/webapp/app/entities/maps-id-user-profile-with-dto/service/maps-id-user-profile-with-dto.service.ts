import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMapsIdUserProfileWithDTO, NewMapsIdUserProfileWithDTO } from '../maps-id-user-profile-with-dto.model';

export type PartialUpdateMapsIdUserProfileWithDTO = Partial<IMapsIdUserProfileWithDTO> & Pick<IMapsIdUserProfileWithDTO, 'id'>;

type RestOf<T extends IMapsIdUserProfileWithDTO | NewMapsIdUserProfileWithDTO> = Omit<T, 'dateOfBirth'> & {
  dateOfBirth?: string | null;
};

export type RestMapsIdUserProfileWithDTO = RestOf<IMapsIdUserProfileWithDTO>;

export type NewRestMapsIdUserProfileWithDTO = RestOf<NewMapsIdUserProfileWithDTO>;

export type PartialUpdateRestMapsIdUserProfileWithDTO = RestOf<PartialUpdateMapsIdUserProfileWithDTO>;

@Service()
export class MapsIdUserProfileWithDTOSService {
  readonly mapsIdUserProfileWithDTOSParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly mapsIdUserProfileWithDTOSResource = httpResource<RestMapsIdUserProfileWithDTO[]>(() => {
    const params = this.mapsIdUserProfileWithDTOSParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of mapsIdUserProfileWithDTO that have been fetched. It is updated when the mapsIdUserProfileWithDTOSResource emits a new value.
   * In case of error while fetching the mapsIdUserProfileWithDTOS, the signal is set to an empty array.
   */
  readonly mapsIdUserProfileWithDTOS = computed(() =>
    (this.mapsIdUserProfileWithDTOSResource.hasValue() ? this.mapsIdUserProfileWithDTOSResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/maps-id-user-profile-with-dtos');

  protected convertValueFromServer(restMapsIdUserProfileWithDTO: RestMapsIdUserProfileWithDTO): IMapsIdUserProfileWithDTO {
    return {
      ...restMapsIdUserProfileWithDTO,
      dateOfBirth: restMapsIdUserProfileWithDTO.dateOfBirth ? dayjs(restMapsIdUserProfileWithDTO.dateOfBirth) : undefined,
    };
  }
}

@Service()
export class MapsIdUserProfileWithDTOService extends MapsIdUserProfileWithDTOSService {
  protected readonly http = inject(HttpClient);

  create(mapsIdUserProfileWithDTO: NewMapsIdUserProfileWithDTO): Observable<IMapsIdUserProfileWithDTO> {
    const copy = this.convertValueFromClient(mapsIdUserProfileWithDTO);
    return this.http.post<RestMapsIdUserProfileWithDTO>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(mapsIdUserProfileWithDTO: IMapsIdUserProfileWithDTO): Observable<IMapsIdUserProfileWithDTO> {
    const copy = this.convertValueFromClient(mapsIdUserProfileWithDTO);
    return this.http
      .put<RestMapsIdUserProfileWithDTO>(
        `${this.resourceUrl}/${encodeURIComponent(this.getMapsIdUserProfileWithDTOIdentifier(mapsIdUserProfileWithDTO))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(mapsIdUserProfileWithDTO: PartialUpdateMapsIdUserProfileWithDTO): Observable<IMapsIdUserProfileWithDTO> {
    const copy = this.convertValueFromClient(mapsIdUserProfileWithDTO);
    return this.http
      .patch<RestMapsIdUserProfileWithDTO>(
        `${this.resourceUrl}/${encodeURIComponent(this.getMapsIdUserProfileWithDTOIdentifier(mapsIdUserProfileWithDTO))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IMapsIdUserProfileWithDTO> {
    return this.http
      .get<RestMapsIdUserProfileWithDTO>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IMapsIdUserProfileWithDTO[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMapsIdUserProfileWithDTO[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getMapsIdUserProfileWithDTOIdentifier(mapsIdUserProfileWithDTO: Pick<IMapsIdUserProfileWithDTO, 'id'>): number {
    return mapsIdUserProfileWithDTO.id;
  }

  compareMapsIdUserProfileWithDTO(
    o1: Pick<IMapsIdUserProfileWithDTO, 'id'> | null,
    o2: Pick<IMapsIdUserProfileWithDTO, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getMapsIdUserProfileWithDTOIdentifier(o1) === this.getMapsIdUserProfileWithDTOIdentifier(o2) : o1 === o2;
  }

  addMapsIdUserProfileWithDTOToCollectionIfMissing<Type extends Pick<IMapsIdUserProfileWithDTO, 'id'>>(
    mapsIdUserProfileWithDTOCollection: Type[],
    ...mapsIdUserProfileWithDTOSToCheck: (Type | null | undefined)[]
  ): Type[] {
    const mapsIdUserProfileWithDTOS: Type[] = mapsIdUserProfileWithDTOSToCheck.filter(
      mapsIdUserProfileWithDTOItem => mapsIdUserProfileWithDTOItem !== null && mapsIdUserProfileWithDTOItem !== undefined,
    );
    if (mapsIdUserProfileWithDTOS.length > 0) {
      const mapsIdUserProfileWithDTOCollectionIdentifiers = mapsIdUserProfileWithDTOCollection.map(mapsIdUserProfileWithDTOItem =>
        this.getMapsIdUserProfileWithDTOIdentifier(mapsIdUserProfileWithDTOItem),
      );
      const mapsIdUserProfileWithDTOSToAdd = mapsIdUserProfileWithDTOS.filter(mapsIdUserProfileWithDTOItem => {
        const mapsIdUserProfileWithDTOIdentifier = this.getMapsIdUserProfileWithDTOIdentifier(mapsIdUserProfileWithDTOItem);
        if (mapsIdUserProfileWithDTOCollectionIdentifiers.includes(mapsIdUserProfileWithDTOIdentifier)) {
          return false;
        }
        mapsIdUserProfileWithDTOCollectionIdentifiers.push(mapsIdUserProfileWithDTOIdentifier);
        return true;
      });
      return [...mapsIdUserProfileWithDTOSToAdd, ...mapsIdUserProfileWithDTOCollection];
    }
    return mapsIdUserProfileWithDTOCollection;
  }

  protected convertValueFromClient<
    T extends IMapsIdUserProfileWithDTO | NewMapsIdUserProfileWithDTO | PartialUpdateMapsIdUserProfileWithDTO,
  >(mapsIdUserProfileWithDTO: T): RestOf<T> {
    return {
      ...mapsIdUserProfileWithDTO,
      dateOfBirth: mapsIdUserProfileWithDTO.dateOfBirth?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestMapsIdUserProfileWithDTO): IMapsIdUserProfileWithDTO {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestMapsIdUserProfileWithDTO[]): IMapsIdUserProfileWithDTO[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}

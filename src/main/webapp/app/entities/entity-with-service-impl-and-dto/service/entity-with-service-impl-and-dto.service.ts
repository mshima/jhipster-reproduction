import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEntityWithServiceImplAndDTO, NewEntityWithServiceImplAndDTO } from '../entity-with-service-impl-and-dto.model';

export type PartialUpdateEntityWithServiceImplAndDTO = Partial<IEntityWithServiceImplAndDTO> & Pick<IEntityWithServiceImplAndDTO, 'id'>;

@Service()
export class EntityWithServiceImplAndDTOSService {
  readonly entityWithServiceImplAndDTOSParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly entityWithServiceImplAndDTOSResource = httpResource<IEntityWithServiceImplAndDTO[]>(() => {
    const params = this.entityWithServiceImplAndDTOSParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of entityWithServiceImplAndDTO that have been fetched. It is updated when the entityWithServiceImplAndDTOSResource emits a new value.
   * In case of error while fetching the entityWithServiceImplAndDTOS, the signal is set to an empty array.
   */
  readonly entityWithServiceImplAndDTOS = computed(() =>
    this.entityWithServiceImplAndDTOSResource.hasValue() ? this.entityWithServiceImplAndDTOSResource.value() : [],
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/entity-with-service-impl-and-dtos');
}

@Service()
export class EntityWithServiceImplAndDTOService extends EntityWithServiceImplAndDTOSService {
  protected readonly http = inject(HttpClient);

  create(entityWithServiceImplAndDTO: NewEntityWithServiceImplAndDTO): Observable<IEntityWithServiceImplAndDTO> {
    return this.http.post<IEntityWithServiceImplAndDTO>(this.resourceUrl, entityWithServiceImplAndDTO);
  }

  update(entityWithServiceImplAndDTO: IEntityWithServiceImplAndDTO): Observable<IEntityWithServiceImplAndDTO> {
    return this.http.put<IEntityWithServiceImplAndDTO>(
      `${this.resourceUrl}/${encodeURIComponent(this.getEntityWithServiceImplAndDTOIdentifier(entityWithServiceImplAndDTO))}`,
      entityWithServiceImplAndDTO,
    );
  }

  partialUpdate(entityWithServiceImplAndDTO: PartialUpdateEntityWithServiceImplAndDTO): Observable<IEntityWithServiceImplAndDTO> {
    return this.http.patch<IEntityWithServiceImplAndDTO>(
      `${this.resourceUrl}/${encodeURIComponent(this.getEntityWithServiceImplAndDTOIdentifier(entityWithServiceImplAndDTO))}`,
      entityWithServiceImplAndDTO,
    );
  }

  find(id: number): Observable<IEntityWithServiceImplAndDTO> {
    return this.http.get<IEntityWithServiceImplAndDTO>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IEntityWithServiceImplAndDTO[]>> {
    const options = createRequestOption(req);
    return this.http.get<IEntityWithServiceImplAndDTO[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getEntityWithServiceImplAndDTOIdentifier(entityWithServiceImplAndDTO: Pick<IEntityWithServiceImplAndDTO, 'id'>): number {
    return entityWithServiceImplAndDTO.id;
  }

  compareEntityWithServiceImplAndDTO(
    o1: Pick<IEntityWithServiceImplAndDTO, 'id'> | null,
    o2: Pick<IEntityWithServiceImplAndDTO, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getEntityWithServiceImplAndDTOIdentifier(o1) === this.getEntityWithServiceImplAndDTOIdentifier(o2) : o1 === o2;
  }

  addEntityWithServiceImplAndDTOToCollectionIfMissing<Type extends Pick<IEntityWithServiceImplAndDTO, 'id'>>(
    entityWithServiceImplAndDTOCollection: Type[],
    ...entityWithServiceImplAndDTOSToCheck: (Type | null | undefined)[]
  ): Type[] {
    const entityWithServiceImplAndDTOS: Type[] = entityWithServiceImplAndDTOSToCheck.filter(
      entityWithServiceImplAndDTOItem => entityWithServiceImplAndDTOItem !== null && entityWithServiceImplAndDTOItem !== undefined,
    );
    if (entityWithServiceImplAndDTOS.length > 0) {
      const entityWithServiceImplAndDTOCollectionIdentifiers = entityWithServiceImplAndDTOCollection.map(entityWithServiceImplAndDTOItem =>
        this.getEntityWithServiceImplAndDTOIdentifier(entityWithServiceImplAndDTOItem),
      );
      const entityWithServiceImplAndDTOSToAdd = entityWithServiceImplAndDTOS.filter(entityWithServiceImplAndDTOItem => {
        const entityWithServiceImplAndDTOIdentifier = this.getEntityWithServiceImplAndDTOIdentifier(entityWithServiceImplAndDTOItem);
        if (entityWithServiceImplAndDTOCollectionIdentifiers.includes(entityWithServiceImplAndDTOIdentifier)) {
          return false;
        }
        entityWithServiceImplAndDTOCollectionIdentifiers.push(entityWithServiceImplAndDTOIdentifier);
        return true;
      });
      return [...entityWithServiceImplAndDTOSToAdd, ...entityWithServiceImplAndDTOCollection];
    }
    return entityWithServiceImplAndDTOCollection;
  }
}

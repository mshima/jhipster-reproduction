import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEntityWithPaginationAndDTO, NewEntityWithPaginationAndDTO } from '../entity-with-pagination-and-dto.model';

export type PartialUpdateEntityWithPaginationAndDTO = Partial<IEntityWithPaginationAndDTO> & Pick<IEntityWithPaginationAndDTO, 'id'>;

@Service()
export class EntityWithPaginationAndDTOSService {
  readonly entityWithPaginationAndDTOSParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly entityWithPaginationAndDTOSResource = httpResource<IEntityWithPaginationAndDTO[]>(() => {
    const params = this.entityWithPaginationAndDTOSParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of entityWithPaginationAndDTO that have been fetched. It is updated when the entityWithPaginationAndDTOSResource emits a new value.
   * In case of error while fetching the entityWithPaginationAndDTOS, the signal is set to an empty array.
   */
  readonly entityWithPaginationAndDTOS = computed(() =>
    this.entityWithPaginationAndDTOSResource.hasValue() ? this.entityWithPaginationAndDTOSResource.value() : [],
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/entity-with-pagination-and-dtos');
}

@Service()
export class EntityWithPaginationAndDTOService extends EntityWithPaginationAndDTOSService {
  protected readonly http = inject(HttpClient);

  create(entityWithPaginationAndDTO: NewEntityWithPaginationAndDTO): Observable<IEntityWithPaginationAndDTO> {
    return this.http.post<IEntityWithPaginationAndDTO>(this.resourceUrl, entityWithPaginationAndDTO);
  }

  update(entityWithPaginationAndDTO: IEntityWithPaginationAndDTO): Observable<IEntityWithPaginationAndDTO> {
    return this.http.put<IEntityWithPaginationAndDTO>(
      `${this.resourceUrl}/${encodeURIComponent(this.getEntityWithPaginationAndDTOIdentifier(entityWithPaginationAndDTO))}`,
      entityWithPaginationAndDTO,
    );
  }

  partialUpdate(entityWithPaginationAndDTO: PartialUpdateEntityWithPaginationAndDTO): Observable<IEntityWithPaginationAndDTO> {
    return this.http.patch<IEntityWithPaginationAndDTO>(
      `${this.resourceUrl}/${encodeURIComponent(this.getEntityWithPaginationAndDTOIdentifier(entityWithPaginationAndDTO))}`,
      entityWithPaginationAndDTO,
    );
  }

  find(id: number): Observable<IEntityWithPaginationAndDTO> {
    return this.http.get<IEntityWithPaginationAndDTO>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IEntityWithPaginationAndDTO[]>> {
    const options = createRequestOption(req);
    return this.http.get<IEntityWithPaginationAndDTO[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getEntityWithPaginationAndDTOIdentifier(entityWithPaginationAndDTO: Pick<IEntityWithPaginationAndDTO, 'id'>): number {
    return entityWithPaginationAndDTO.id;
  }

  compareEntityWithPaginationAndDTO(
    o1: Pick<IEntityWithPaginationAndDTO, 'id'> | null,
    o2: Pick<IEntityWithPaginationAndDTO, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getEntityWithPaginationAndDTOIdentifier(o1) === this.getEntityWithPaginationAndDTOIdentifier(o2) : o1 === o2;
  }

  addEntityWithPaginationAndDTOToCollectionIfMissing<Type extends Pick<IEntityWithPaginationAndDTO, 'id'>>(
    entityWithPaginationAndDTOCollection: Type[],
    ...entityWithPaginationAndDTOSToCheck: (Type | null | undefined)[]
  ): Type[] {
    const entityWithPaginationAndDTOS: Type[] = entityWithPaginationAndDTOSToCheck.filter(
      entityWithPaginationAndDTOItem => entityWithPaginationAndDTOItem !== null && entityWithPaginationAndDTOItem !== undefined,
    );
    if (entityWithPaginationAndDTOS.length > 0) {
      const entityWithPaginationAndDTOCollectionIdentifiers = entityWithPaginationAndDTOCollection.map(entityWithPaginationAndDTOItem =>
        this.getEntityWithPaginationAndDTOIdentifier(entityWithPaginationAndDTOItem),
      );
      const entityWithPaginationAndDTOSToAdd = entityWithPaginationAndDTOS.filter(entityWithPaginationAndDTOItem => {
        const entityWithPaginationAndDTOIdentifier = this.getEntityWithPaginationAndDTOIdentifier(entityWithPaginationAndDTOItem);
        if (entityWithPaginationAndDTOCollectionIdentifiers.includes(entityWithPaginationAndDTOIdentifier)) {
          return false;
        }
        entityWithPaginationAndDTOCollectionIdentifiers.push(entityWithPaginationAndDTOIdentifier);
        return true;
      });
      return [...entityWithPaginationAndDTOSToAdd, ...entityWithPaginationAndDTOCollection];
    }
    return entityWithPaginationAndDTOCollection;
  }
}

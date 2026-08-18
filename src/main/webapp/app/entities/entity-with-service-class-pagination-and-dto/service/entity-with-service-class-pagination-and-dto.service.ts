import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import {
  IEntityWithServiceClassPaginationAndDTO,
  NewEntityWithServiceClassPaginationAndDTO,
} from '../entity-with-service-class-pagination-and-dto.model';

export type PartialUpdateEntityWithServiceClassPaginationAndDTO = Partial<IEntityWithServiceClassPaginationAndDTO> &
  Pick<IEntityWithServiceClassPaginationAndDTO, 'id'>;

@Service()
export class EntityWithServiceClassPaginationAndDTOSService {
  readonly entityWithServiceClassPaginationAndDTOSParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly entityWithServiceClassPaginationAndDTOSResource = httpResource<IEntityWithServiceClassPaginationAndDTO[]>(() => {
    const params = this.entityWithServiceClassPaginationAndDTOSParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of entityWithServiceClassPaginationAndDTO that have been fetched. It is updated when the entityWithServiceClassPaginationAndDTOSResource emits a new value.
   * In case of error while fetching the entityWithServiceClassPaginationAndDTOS, the signal is set to an empty array.
   */
  readonly entityWithServiceClassPaginationAndDTOS = computed(() =>
    this.entityWithServiceClassPaginationAndDTOSResource.hasValue() ? this.entityWithServiceClassPaginationAndDTOSResource.value() : [],
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/entity-with-service-class-pagination-and-dtos');
}

@Service()
export class EntityWithServiceClassPaginationAndDTOService extends EntityWithServiceClassPaginationAndDTOSService {
  protected readonly http = inject(HttpClient);

  create(
    entityWithServiceClassPaginationAndDTO: NewEntityWithServiceClassPaginationAndDTO,
  ): Observable<IEntityWithServiceClassPaginationAndDTO> {
    return this.http.post<IEntityWithServiceClassPaginationAndDTO>(this.resourceUrl, entityWithServiceClassPaginationAndDTO);
  }

  update(
    entityWithServiceClassPaginationAndDTO: IEntityWithServiceClassPaginationAndDTO,
  ): Observable<IEntityWithServiceClassPaginationAndDTO> {
    return this.http.put<IEntityWithServiceClassPaginationAndDTO>(
      `${this.resourceUrl}/${encodeURIComponent(this.getEntityWithServiceClassPaginationAndDTOIdentifier(entityWithServiceClassPaginationAndDTO))}`,
      entityWithServiceClassPaginationAndDTO,
    );
  }

  partialUpdate(
    entityWithServiceClassPaginationAndDTO: PartialUpdateEntityWithServiceClassPaginationAndDTO,
  ): Observable<IEntityWithServiceClassPaginationAndDTO> {
    return this.http.patch<IEntityWithServiceClassPaginationAndDTO>(
      `${this.resourceUrl}/${encodeURIComponent(this.getEntityWithServiceClassPaginationAndDTOIdentifier(entityWithServiceClassPaginationAndDTO))}`,
      entityWithServiceClassPaginationAndDTO,
    );
  }

  find(id: number): Observable<IEntityWithServiceClassPaginationAndDTO> {
    return this.http.get<IEntityWithServiceClassPaginationAndDTO>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IEntityWithServiceClassPaginationAndDTO[]>> {
    const options = createRequestOption(req);
    return this.http.get<IEntityWithServiceClassPaginationAndDTO[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getEntityWithServiceClassPaginationAndDTOIdentifier(
    entityWithServiceClassPaginationAndDTO: Pick<IEntityWithServiceClassPaginationAndDTO, 'id'>,
  ): number {
    return entityWithServiceClassPaginationAndDTO.id;
  }

  compareEntityWithServiceClassPaginationAndDTO(
    o1: Pick<IEntityWithServiceClassPaginationAndDTO, 'id'> | null,
    o2: Pick<IEntityWithServiceClassPaginationAndDTO, 'id'> | null,
  ): boolean {
    return o1 && o2
      ? this.getEntityWithServiceClassPaginationAndDTOIdentifier(o1) === this.getEntityWithServiceClassPaginationAndDTOIdentifier(o2)
      : o1 === o2;
  }

  addEntityWithServiceClassPaginationAndDTOToCollectionIfMissing<Type extends Pick<IEntityWithServiceClassPaginationAndDTO, 'id'>>(
    entityWithServiceClassPaginationAndDTOCollection: Type[],
    ...entityWithServiceClassPaginationAndDTOSToCheck: (Type | null | undefined)[]
  ): Type[] {
    const entityWithServiceClassPaginationAndDTOS: Type[] = entityWithServiceClassPaginationAndDTOSToCheck.filter(
      entityWithServiceClassPaginationAndDTOItem =>
        entityWithServiceClassPaginationAndDTOItem !== null && entityWithServiceClassPaginationAndDTOItem !== undefined,
    );
    if (entityWithServiceClassPaginationAndDTOS.length > 0) {
      const entityWithServiceClassPaginationAndDTOCollectionIdentifiers = entityWithServiceClassPaginationAndDTOCollection.map(
        entityWithServiceClassPaginationAndDTOItem =>
          this.getEntityWithServiceClassPaginationAndDTOIdentifier(entityWithServiceClassPaginationAndDTOItem),
      );
      const entityWithServiceClassPaginationAndDTOSToAdd = entityWithServiceClassPaginationAndDTOS.filter(
        entityWithServiceClassPaginationAndDTOItem => {
          const entityWithServiceClassPaginationAndDTOIdentifier = this.getEntityWithServiceClassPaginationAndDTOIdentifier(
            entityWithServiceClassPaginationAndDTOItem,
          );
          if (entityWithServiceClassPaginationAndDTOCollectionIdentifiers.includes(entityWithServiceClassPaginationAndDTOIdentifier)) {
            return false;
          }
          entityWithServiceClassPaginationAndDTOCollectionIdentifiers.push(entityWithServiceClassPaginationAndDTOIdentifier);
          return true;
        },
      );
      return [...entityWithServiceClassPaginationAndDTOSToAdd, ...entityWithServiceClassPaginationAndDTOCollection];
    }
    return entityWithServiceClassPaginationAndDTOCollection;
  }
}

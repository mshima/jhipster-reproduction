import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import {
  IEntityWithServiceImplPaginationAndDTO,
  NewEntityWithServiceImplPaginationAndDTO,
} from '../entity-with-service-impl-pagination-and-dto.model';

export type PartialUpdateEntityWithServiceImplPaginationAndDTO = Partial<IEntityWithServiceImplPaginationAndDTO> &
  Pick<IEntityWithServiceImplPaginationAndDTO, 'id'>;

@Service()
export class EntityWithServiceImplPaginationAndDTOSService {
  readonly entityWithServiceImplPaginationAndDTOSParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly entityWithServiceImplPaginationAndDTOSResource = httpResource<IEntityWithServiceImplPaginationAndDTO[]>(() => {
    const params = this.entityWithServiceImplPaginationAndDTOSParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of entityWithServiceImplPaginationAndDTO that have been fetched. It is updated when the entityWithServiceImplPaginationAndDTOSResource emits a new value.
   * In case of error while fetching the entityWithServiceImplPaginationAndDTOS, the signal is set to an empty array.
   */
  readonly entityWithServiceImplPaginationAndDTOS = computed(() =>
    this.entityWithServiceImplPaginationAndDTOSResource.hasValue() ? this.entityWithServiceImplPaginationAndDTOSResource.value() : [],
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/entity-with-service-impl-pagination-and-dtos');
}

@Service()
export class EntityWithServiceImplPaginationAndDTOService extends EntityWithServiceImplPaginationAndDTOSService {
  protected readonly http = inject(HttpClient);

  create(
    entityWithServiceImplPaginationAndDTO: NewEntityWithServiceImplPaginationAndDTO,
  ): Observable<IEntityWithServiceImplPaginationAndDTO> {
    return this.http.post<IEntityWithServiceImplPaginationAndDTO>(this.resourceUrl, entityWithServiceImplPaginationAndDTO);
  }

  update(
    entityWithServiceImplPaginationAndDTO: IEntityWithServiceImplPaginationAndDTO,
  ): Observable<IEntityWithServiceImplPaginationAndDTO> {
    return this.http.put<IEntityWithServiceImplPaginationAndDTO>(
      `${this.resourceUrl}/${encodeURIComponent(this.getEntityWithServiceImplPaginationAndDTOIdentifier(entityWithServiceImplPaginationAndDTO))}`,
      entityWithServiceImplPaginationAndDTO,
    );
  }

  partialUpdate(
    entityWithServiceImplPaginationAndDTO: PartialUpdateEntityWithServiceImplPaginationAndDTO,
  ): Observable<IEntityWithServiceImplPaginationAndDTO> {
    return this.http.patch<IEntityWithServiceImplPaginationAndDTO>(
      `${this.resourceUrl}/${encodeURIComponent(this.getEntityWithServiceImplPaginationAndDTOIdentifier(entityWithServiceImplPaginationAndDTO))}`,
      entityWithServiceImplPaginationAndDTO,
    );
  }

  find(id: number): Observable<IEntityWithServiceImplPaginationAndDTO> {
    return this.http.get<IEntityWithServiceImplPaginationAndDTO>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IEntityWithServiceImplPaginationAndDTO[]>> {
    const options = createRequestOption(req);
    return this.http.get<IEntityWithServiceImplPaginationAndDTO[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getEntityWithServiceImplPaginationAndDTOIdentifier(
    entityWithServiceImplPaginationAndDTO: Pick<IEntityWithServiceImplPaginationAndDTO, 'id'>,
  ): number {
    return entityWithServiceImplPaginationAndDTO.id;
  }

  compareEntityWithServiceImplPaginationAndDTO(
    o1: Pick<IEntityWithServiceImplPaginationAndDTO, 'id'> | null,
    o2: Pick<IEntityWithServiceImplPaginationAndDTO, 'id'> | null,
  ): boolean {
    return o1 && o2
      ? this.getEntityWithServiceImplPaginationAndDTOIdentifier(o1) === this.getEntityWithServiceImplPaginationAndDTOIdentifier(o2)
      : o1 === o2;
  }

  addEntityWithServiceImplPaginationAndDTOToCollectionIfMissing<Type extends Pick<IEntityWithServiceImplPaginationAndDTO, 'id'>>(
    entityWithServiceImplPaginationAndDTOCollection: Type[],
    ...entityWithServiceImplPaginationAndDTOSToCheck: (Type | null | undefined)[]
  ): Type[] {
    const entityWithServiceImplPaginationAndDTOS: Type[] = entityWithServiceImplPaginationAndDTOSToCheck.filter(
      entityWithServiceImplPaginationAndDTOItem =>
        entityWithServiceImplPaginationAndDTOItem !== null && entityWithServiceImplPaginationAndDTOItem !== undefined,
    );
    if (entityWithServiceImplPaginationAndDTOS.length > 0) {
      const entityWithServiceImplPaginationAndDTOCollectionIdentifiers = entityWithServiceImplPaginationAndDTOCollection.map(
        entityWithServiceImplPaginationAndDTOItem =>
          this.getEntityWithServiceImplPaginationAndDTOIdentifier(entityWithServiceImplPaginationAndDTOItem),
      );
      const entityWithServiceImplPaginationAndDTOSToAdd = entityWithServiceImplPaginationAndDTOS.filter(
        entityWithServiceImplPaginationAndDTOItem => {
          const entityWithServiceImplPaginationAndDTOIdentifier = this.getEntityWithServiceImplPaginationAndDTOIdentifier(
            entityWithServiceImplPaginationAndDTOItem,
          );
          if (entityWithServiceImplPaginationAndDTOCollectionIdentifiers.includes(entityWithServiceImplPaginationAndDTOIdentifier)) {
            return false;
          }
          entityWithServiceImplPaginationAndDTOCollectionIdentifiers.push(entityWithServiceImplPaginationAndDTOIdentifier);
          return true;
        },
      );
      return [...entityWithServiceImplPaginationAndDTOSToAdd, ...entityWithServiceImplPaginationAndDTOCollection];
    }
    return entityWithServiceImplPaginationAndDTOCollection;
  }
}

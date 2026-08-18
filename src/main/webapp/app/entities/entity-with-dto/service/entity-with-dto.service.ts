import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEntityWithDTO, NewEntityWithDTO } from '../entity-with-dto.model';

export type PartialUpdateEntityWithDTO = Partial<IEntityWithDTO> & Pick<IEntityWithDTO, 'id'>;

@Service()
export class EntityWithDTOSService {
  readonly entityWithDTOSParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly entityWithDTOSResource = httpResource<IEntityWithDTO[]>(() => {
    const params = this.entityWithDTOSParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of entityWithDTO that have been fetched. It is updated when the entityWithDTOSResource emits a new value.
   * In case of error while fetching the entityWithDTOS, the signal is set to an empty array.
   */
  readonly entityWithDTOS = computed(() => (this.entityWithDTOSResource.hasValue() ? this.entityWithDTOSResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/entity-with-dtos');
}

@Service()
export class EntityWithDTOService extends EntityWithDTOSService {
  protected readonly http = inject(HttpClient);

  create(entityWithDTO: NewEntityWithDTO): Observable<IEntityWithDTO> {
    return this.http.post<IEntityWithDTO>(this.resourceUrl, entityWithDTO);
  }

  update(entityWithDTO: IEntityWithDTO): Observable<IEntityWithDTO> {
    return this.http.put<IEntityWithDTO>(
      `${this.resourceUrl}/${encodeURIComponent(this.getEntityWithDTOIdentifier(entityWithDTO))}`,
      entityWithDTO,
    );
  }

  partialUpdate(entityWithDTO: PartialUpdateEntityWithDTO): Observable<IEntityWithDTO> {
    return this.http.patch<IEntityWithDTO>(
      `${this.resourceUrl}/${encodeURIComponent(this.getEntityWithDTOIdentifier(entityWithDTO))}`,
      entityWithDTO,
    );
  }

  find(id: number): Observable<IEntityWithDTO> {
    return this.http.get<IEntityWithDTO>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IEntityWithDTO[]>> {
    const options = createRequestOption(req);
    return this.http.get<IEntityWithDTO[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getEntityWithDTOIdentifier(entityWithDTO: Pick<IEntityWithDTO, 'id'>): number {
    return entityWithDTO.id;
  }

  compareEntityWithDTO(o1: Pick<IEntityWithDTO, 'id'> | null, o2: Pick<IEntityWithDTO, 'id'> | null): boolean {
    return o1 && o2 ? this.getEntityWithDTOIdentifier(o1) === this.getEntityWithDTOIdentifier(o2) : o1 === o2;
  }

  addEntityWithDTOToCollectionIfMissing<Type extends Pick<IEntityWithDTO, 'id'>>(
    entityWithDTOCollection: Type[],
    ...entityWithDTOSToCheck: (Type | null | undefined)[]
  ): Type[] {
    const entityWithDTOS: Type[] = entityWithDTOSToCheck.filter(
      entityWithDTOItem => entityWithDTOItem !== null && entityWithDTOItem !== undefined,
    );
    if (entityWithDTOS.length > 0) {
      const entityWithDTOCollectionIdentifiers = entityWithDTOCollection.map(entityWithDTOItem =>
        this.getEntityWithDTOIdentifier(entityWithDTOItem),
      );
      const entityWithDTOSToAdd = entityWithDTOS.filter(entityWithDTOItem => {
        const entityWithDTOIdentifier = this.getEntityWithDTOIdentifier(entityWithDTOItem);
        if (entityWithDTOCollectionIdentifiers.includes(entityWithDTOIdentifier)) {
          return false;
        }
        entityWithDTOCollectionIdentifiers.push(entityWithDTOIdentifier);
        return true;
      });
      return [...entityWithDTOSToAdd, ...entityWithDTOCollection];
    }
    return entityWithDTOCollection;
  }
}

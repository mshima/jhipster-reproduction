import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import {
  IEntityWithServiceClassAndPagination,
  NewEntityWithServiceClassAndPagination,
} from '../entity-with-service-class-and-pagination.model';

export type PartialUpdateEntityWithServiceClassAndPagination = Partial<IEntityWithServiceClassAndPagination> &
  Pick<IEntityWithServiceClassAndPagination, 'id'>;

@Service()
export class EntityWithServiceClassAndPaginationsService {
  readonly entityWithServiceClassAndPaginationsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly entityWithServiceClassAndPaginationsResource = httpResource<IEntityWithServiceClassAndPagination[]>(() => {
    const params = this.entityWithServiceClassAndPaginationsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of entityWithServiceClassAndPagination that have been fetched. It is updated when the entityWithServiceClassAndPaginationsResource emits a new value.
   * In case of error while fetching the entityWithServiceClassAndPaginations, the signal is set to an empty array.
   */
  readonly entityWithServiceClassAndPaginations = computed(() =>
    this.entityWithServiceClassAndPaginationsResource.hasValue() ? this.entityWithServiceClassAndPaginationsResource.value() : [],
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/entity-with-service-class-and-paginations');
}

@Service()
export class EntityWithServiceClassAndPaginationService extends EntityWithServiceClassAndPaginationsService {
  protected readonly http = inject(HttpClient);

  create(entityWithServiceClassAndPagination: NewEntityWithServiceClassAndPagination): Observable<IEntityWithServiceClassAndPagination> {
    return this.http.post<IEntityWithServiceClassAndPagination>(this.resourceUrl, entityWithServiceClassAndPagination);
  }

  update(entityWithServiceClassAndPagination: IEntityWithServiceClassAndPagination): Observable<IEntityWithServiceClassAndPagination> {
    return this.http.put<IEntityWithServiceClassAndPagination>(
      `${this.resourceUrl}/${encodeURIComponent(this.getEntityWithServiceClassAndPaginationIdentifier(entityWithServiceClassAndPagination))}`,
      entityWithServiceClassAndPagination,
    );
  }

  partialUpdate(
    entityWithServiceClassAndPagination: PartialUpdateEntityWithServiceClassAndPagination,
  ): Observable<IEntityWithServiceClassAndPagination> {
    return this.http.patch<IEntityWithServiceClassAndPagination>(
      `${this.resourceUrl}/${encodeURIComponent(this.getEntityWithServiceClassAndPaginationIdentifier(entityWithServiceClassAndPagination))}`,
      entityWithServiceClassAndPagination,
    );
  }

  find(id: number): Observable<IEntityWithServiceClassAndPagination> {
    return this.http.get<IEntityWithServiceClassAndPagination>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IEntityWithServiceClassAndPagination[]>> {
    const options = createRequestOption(req);
    return this.http.get<IEntityWithServiceClassAndPagination[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getEntityWithServiceClassAndPaginationIdentifier(
    entityWithServiceClassAndPagination: Pick<IEntityWithServiceClassAndPagination, 'id'>,
  ): number {
    return entityWithServiceClassAndPagination.id;
  }

  compareEntityWithServiceClassAndPagination(
    o1: Pick<IEntityWithServiceClassAndPagination, 'id'> | null,
    o2: Pick<IEntityWithServiceClassAndPagination, 'id'> | null,
  ): boolean {
    return o1 && o2
      ? this.getEntityWithServiceClassAndPaginationIdentifier(o1) === this.getEntityWithServiceClassAndPaginationIdentifier(o2)
      : o1 === o2;
  }

  addEntityWithServiceClassAndPaginationToCollectionIfMissing<Type extends Pick<IEntityWithServiceClassAndPagination, 'id'>>(
    entityWithServiceClassAndPaginationCollection: Type[],
    ...entityWithServiceClassAndPaginationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const entityWithServiceClassAndPaginations: Type[] = entityWithServiceClassAndPaginationsToCheck.filter(
      entityWithServiceClassAndPaginationItem =>
        entityWithServiceClassAndPaginationItem !== null && entityWithServiceClassAndPaginationItem !== undefined,
    );
    if (entityWithServiceClassAndPaginations.length > 0) {
      const entityWithServiceClassAndPaginationCollectionIdentifiers = entityWithServiceClassAndPaginationCollection.map(
        entityWithServiceClassAndPaginationItem =>
          this.getEntityWithServiceClassAndPaginationIdentifier(entityWithServiceClassAndPaginationItem),
      );
      const entityWithServiceClassAndPaginationsToAdd = entityWithServiceClassAndPaginations.filter(
        entityWithServiceClassAndPaginationItem => {
          const entityWithServiceClassAndPaginationIdentifier = this.getEntityWithServiceClassAndPaginationIdentifier(
            entityWithServiceClassAndPaginationItem,
          );
          if (entityWithServiceClassAndPaginationCollectionIdentifiers.includes(entityWithServiceClassAndPaginationIdentifier)) {
            return false;
          }
          entityWithServiceClassAndPaginationCollectionIdentifiers.push(entityWithServiceClassAndPaginationIdentifier);
          return true;
        },
      );
      return [...entityWithServiceClassAndPaginationsToAdd, ...entityWithServiceClassAndPaginationCollection];
    }
    return entityWithServiceClassAndPaginationCollection;
  }
}

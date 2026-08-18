import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import {
  IEntityWithServiceImplAndPagination,
  NewEntityWithServiceImplAndPagination,
} from '../entity-with-service-impl-and-pagination.model';

export type PartialUpdateEntityWithServiceImplAndPagination = Partial<IEntityWithServiceImplAndPagination> &
  Pick<IEntityWithServiceImplAndPagination, 'id'>;

@Service()
export class EntityWithServiceImplAndPaginationsService {
  readonly entityWithServiceImplAndPaginationsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly entityWithServiceImplAndPaginationsResource = httpResource<IEntityWithServiceImplAndPagination[]>(() => {
    const params = this.entityWithServiceImplAndPaginationsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of entityWithServiceImplAndPagination that have been fetched. It is updated when the entityWithServiceImplAndPaginationsResource emits a new value.
   * In case of error while fetching the entityWithServiceImplAndPaginations, the signal is set to an empty array.
   */
  readonly entityWithServiceImplAndPaginations = computed(() =>
    this.entityWithServiceImplAndPaginationsResource.hasValue() ? this.entityWithServiceImplAndPaginationsResource.value() : [],
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/entity-with-service-impl-and-paginations');
}

@Service()
export class EntityWithServiceImplAndPaginationService extends EntityWithServiceImplAndPaginationsService {
  protected readonly http = inject(HttpClient);

  create(entityWithServiceImplAndPagination: NewEntityWithServiceImplAndPagination): Observable<IEntityWithServiceImplAndPagination> {
    return this.http.post<IEntityWithServiceImplAndPagination>(this.resourceUrl, entityWithServiceImplAndPagination);
  }

  update(entityWithServiceImplAndPagination: IEntityWithServiceImplAndPagination): Observable<IEntityWithServiceImplAndPagination> {
    return this.http.put<IEntityWithServiceImplAndPagination>(
      `${this.resourceUrl}/${encodeURIComponent(this.getEntityWithServiceImplAndPaginationIdentifier(entityWithServiceImplAndPagination))}`,
      entityWithServiceImplAndPagination,
    );
  }

  partialUpdate(
    entityWithServiceImplAndPagination: PartialUpdateEntityWithServiceImplAndPagination,
  ): Observable<IEntityWithServiceImplAndPagination> {
    return this.http.patch<IEntityWithServiceImplAndPagination>(
      `${this.resourceUrl}/${encodeURIComponent(this.getEntityWithServiceImplAndPaginationIdentifier(entityWithServiceImplAndPagination))}`,
      entityWithServiceImplAndPagination,
    );
  }

  find(id: number): Observable<IEntityWithServiceImplAndPagination> {
    return this.http.get<IEntityWithServiceImplAndPagination>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IEntityWithServiceImplAndPagination[]>> {
    const options = createRequestOption(req);
    return this.http.get<IEntityWithServiceImplAndPagination[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getEntityWithServiceImplAndPaginationIdentifier(
    entityWithServiceImplAndPagination: Pick<IEntityWithServiceImplAndPagination, 'id'>,
  ): number {
    return entityWithServiceImplAndPagination.id;
  }

  compareEntityWithServiceImplAndPagination(
    o1: Pick<IEntityWithServiceImplAndPagination, 'id'> | null,
    o2: Pick<IEntityWithServiceImplAndPagination, 'id'> | null,
  ): boolean {
    return o1 && o2
      ? this.getEntityWithServiceImplAndPaginationIdentifier(o1) === this.getEntityWithServiceImplAndPaginationIdentifier(o2)
      : o1 === o2;
  }

  addEntityWithServiceImplAndPaginationToCollectionIfMissing<Type extends Pick<IEntityWithServiceImplAndPagination, 'id'>>(
    entityWithServiceImplAndPaginationCollection: Type[],
    ...entityWithServiceImplAndPaginationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const entityWithServiceImplAndPaginations: Type[] = entityWithServiceImplAndPaginationsToCheck.filter(
      entityWithServiceImplAndPaginationItem =>
        entityWithServiceImplAndPaginationItem !== null && entityWithServiceImplAndPaginationItem !== undefined,
    );
    if (entityWithServiceImplAndPaginations.length > 0) {
      const entityWithServiceImplAndPaginationCollectionIdentifiers = entityWithServiceImplAndPaginationCollection.map(
        entityWithServiceImplAndPaginationItem =>
          this.getEntityWithServiceImplAndPaginationIdentifier(entityWithServiceImplAndPaginationItem),
      );
      const entityWithServiceImplAndPaginationsToAdd = entityWithServiceImplAndPaginations.filter(
        entityWithServiceImplAndPaginationItem => {
          const entityWithServiceImplAndPaginationIdentifier = this.getEntityWithServiceImplAndPaginationIdentifier(
            entityWithServiceImplAndPaginationItem,
          );
          if (entityWithServiceImplAndPaginationCollectionIdentifiers.includes(entityWithServiceImplAndPaginationIdentifier)) {
            return false;
          }
          entityWithServiceImplAndPaginationCollectionIdentifiers.push(entityWithServiceImplAndPaginationIdentifier);
          return true;
        },
      );
      return [...entityWithServiceImplAndPaginationsToAdd, ...entityWithServiceImplAndPaginationCollection];
    }
    return entityWithServiceImplAndPaginationCollection;
  }
}

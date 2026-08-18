import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFieldTestPaginationEntity, NewFieldTestPaginationEntity } from '../field-test-pagination-entity.model';

export type PartialUpdateFieldTestPaginationEntity = Partial<IFieldTestPaginationEntity> & Pick<IFieldTestPaginationEntity, 'id'>;

type RestOf<T extends IFieldTestPaginationEntity | NewFieldTestPaginationEntity> = Omit<
  T,
  | 'localDateAlice'
  | 'localDateRequiredAlice'
  | 'instantAlice'
  | 'instanteRequiredAlice'
  | 'zonedDateTimeAlice'
  | 'zonedDateTimeRequiredAlice'
> & {
  localDateAlice?: string | null;
  localDateRequiredAlice?: string | null;
  instantAlice?: string | null;
  instanteRequiredAlice?: string | null;
  zonedDateTimeAlice?: string | null;
  zonedDateTimeRequiredAlice?: string | null;
};

export type RestFieldTestPaginationEntity = RestOf<IFieldTestPaginationEntity>;

export type NewRestFieldTestPaginationEntity = RestOf<NewFieldTestPaginationEntity>;

export type PartialUpdateRestFieldTestPaginationEntity = RestOf<PartialUpdateFieldTestPaginationEntity>;

@Service()
export class FieldTestPaginationEntitiesService {
  readonly fieldTestPaginationEntitiesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly fieldTestPaginationEntitiesResource = httpResource<RestFieldTestPaginationEntity[]>(() => {
    const params = this.fieldTestPaginationEntitiesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of fieldTestPaginationEntity that have been fetched. It is updated when the fieldTestPaginationEntitiesResource emits a new value.
   * In case of error while fetching the fieldTestPaginationEntities, the signal is set to an empty array.
   */
  readonly fieldTestPaginationEntities = computed(() =>
    (this.fieldTestPaginationEntitiesResource.hasValue() ? this.fieldTestPaginationEntitiesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/field-test-pagination-entities');

  protected convertValueFromServer(restFieldTestPaginationEntity: RestFieldTestPaginationEntity): IFieldTestPaginationEntity {
    return {
      ...restFieldTestPaginationEntity,
      localDateAlice: restFieldTestPaginationEntity.localDateAlice ? dayjs(restFieldTestPaginationEntity.localDateAlice) : undefined,
      localDateRequiredAlice: restFieldTestPaginationEntity.localDateRequiredAlice
        ? dayjs(restFieldTestPaginationEntity.localDateRequiredAlice)
        : undefined,
      instantAlice: restFieldTestPaginationEntity.instantAlice ? dayjs(restFieldTestPaginationEntity.instantAlice) : undefined,
      instanteRequiredAlice: restFieldTestPaginationEntity.instanteRequiredAlice
        ? dayjs(restFieldTestPaginationEntity.instanteRequiredAlice)
        : undefined,
      zonedDateTimeAlice: restFieldTestPaginationEntity.zonedDateTimeAlice
        ? dayjs(restFieldTestPaginationEntity.zonedDateTimeAlice)
        : undefined,
      zonedDateTimeRequiredAlice: restFieldTestPaginationEntity.zonedDateTimeRequiredAlice
        ? dayjs(restFieldTestPaginationEntity.zonedDateTimeRequiredAlice)
        : undefined,
    };
  }
}

@Service()
export class FieldTestPaginationEntityService extends FieldTestPaginationEntitiesService {
  protected readonly http = inject(HttpClient);

  create(fieldTestPaginationEntity: NewFieldTestPaginationEntity): Observable<IFieldTestPaginationEntity> {
    const copy = this.convertValueFromClient(fieldTestPaginationEntity);
    return this.http.post<RestFieldTestPaginationEntity>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(fieldTestPaginationEntity: IFieldTestPaginationEntity): Observable<IFieldTestPaginationEntity> {
    const copy = this.convertValueFromClient(fieldTestPaginationEntity);
    return this.http
      .put<RestFieldTestPaginationEntity>(
        `${this.resourceUrl}/${encodeURIComponent(this.getFieldTestPaginationEntityIdentifier(fieldTestPaginationEntity))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(fieldTestPaginationEntity: PartialUpdateFieldTestPaginationEntity): Observable<IFieldTestPaginationEntity> {
    const copy = this.convertValueFromClient(fieldTestPaginationEntity);
    return this.http
      .patch<RestFieldTestPaginationEntity>(
        `${this.resourceUrl}/${encodeURIComponent(this.getFieldTestPaginationEntityIdentifier(fieldTestPaginationEntity))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IFieldTestPaginationEntity> {
    return this.http
      .get<RestFieldTestPaginationEntity>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IFieldTestPaginationEntity[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFieldTestPaginationEntity[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFieldTestPaginationEntityIdentifier(fieldTestPaginationEntity: Pick<IFieldTestPaginationEntity, 'id'>): number {
    return fieldTestPaginationEntity.id;
  }

  compareFieldTestPaginationEntity(
    o1: Pick<IFieldTestPaginationEntity, 'id'> | null,
    o2: Pick<IFieldTestPaginationEntity, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getFieldTestPaginationEntityIdentifier(o1) === this.getFieldTestPaginationEntityIdentifier(o2) : o1 === o2;
  }

  addFieldTestPaginationEntityToCollectionIfMissing<Type extends Pick<IFieldTestPaginationEntity, 'id'>>(
    fieldTestPaginationEntityCollection: Type[],
    ...fieldTestPaginationEntitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fieldTestPaginationEntities: Type[] = fieldTestPaginationEntitiesToCheck.filter(
      fieldTestPaginationEntityItem => fieldTestPaginationEntityItem !== null && fieldTestPaginationEntityItem !== undefined,
    );
    if (fieldTestPaginationEntities.length > 0) {
      const fieldTestPaginationEntityCollectionIdentifiers = fieldTestPaginationEntityCollection.map(fieldTestPaginationEntityItem =>
        this.getFieldTestPaginationEntityIdentifier(fieldTestPaginationEntityItem),
      );
      const fieldTestPaginationEntitiesToAdd = fieldTestPaginationEntities.filter(fieldTestPaginationEntityItem => {
        const fieldTestPaginationEntityIdentifier = this.getFieldTestPaginationEntityIdentifier(fieldTestPaginationEntityItem);
        if (fieldTestPaginationEntityCollectionIdentifiers.includes(fieldTestPaginationEntityIdentifier)) {
          return false;
        }
        fieldTestPaginationEntityCollectionIdentifiers.push(fieldTestPaginationEntityIdentifier);
        return true;
      });
      return [...fieldTestPaginationEntitiesToAdd, ...fieldTestPaginationEntityCollection];
    }
    return fieldTestPaginationEntityCollection;
  }

  protected convertValueFromClient<
    T extends IFieldTestPaginationEntity | NewFieldTestPaginationEntity | PartialUpdateFieldTestPaginationEntity,
  >(fieldTestPaginationEntity: T): RestOf<T> {
    return {
      ...fieldTestPaginationEntity,
      localDateAlice: fieldTestPaginationEntity.localDateAlice?.format(DATE_FORMAT) ?? null,
      localDateRequiredAlice: fieldTestPaginationEntity.localDateRequiredAlice?.format(DATE_FORMAT) ?? null,
      instantAlice: fieldTestPaginationEntity.instantAlice?.toJSON() ?? null,
      instanteRequiredAlice: fieldTestPaginationEntity.instanteRequiredAlice?.toJSON() ?? null,
      zonedDateTimeAlice: fieldTestPaginationEntity.zonedDateTimeAlice?.toJSON() ?? null,
      zonedDateTimeRequiredAlice: fieldTestPaginationEntity.zonedDateTimeRequiredAlice?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestFieldTestPaginationEntity): IFieldTestPaginationEntity {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestFieldTestPaginationEntity[]): IFieldTestPaginationEntity[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}

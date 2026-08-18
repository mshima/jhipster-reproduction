import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import {
  IFieldTestServiceClassAndJpaFilteringEntity,
  NewFieldTestServiceClassAndJpaFilteringEntity,
} from '../field-test-service-class-and-jpa-filtering-entity.model';

export type PartialUpdateFieldTestServiceClassAndJpaFilteringEntity = Partial<IFieldTestServiceClassAndJpaFilteringEntity> &
  Pick<IFieldTestServiceClassAndJpaFilteringEntity, 'id'>;

type RestOf<T extends IFieldTestServiceClassAndJpaFilteringEntity | NewFieldTestServiceClassAndJpaFilteringEntity> = Omit<
  T,
  'localDateBob' | 'localDateRequiredBob' | 'instantBob' | 'instanteRequiredBob' | 'zonedDateTimeBob' | 'zonedDateTimeRequiredBob'
> & {
  localDateBob?: string | null;
  localDateRequiredBob?: string | null;
  instantBob?: string | null;
  instanteRequiredBob?: string | null;
  zonedDateTimeBob?: string | null;
  zonedDateTimeRequiredBob?: string | null;
};

export type RestFieldTestServiceClassAndJpaFilteringEntity = RestOf<IFieldTestServiceClassAndJpaFilteringEntity>;

export type NewRestFieldTestServiceClassAndJpaFilteringEntity = RestOf<NewFieldTestServiceClassAndJpaFilteringEntity>;

export type PartialUpdateRestFieldTestServiceClassAndJpaFilteringEntity = RestOf<PartialUpdateFieldTestServiceClassAndJpaFilteringEntity>;

@Service()
export class FieldTestServiceClassAndJpaFilteringEntitiesService {
  readonly fieldTestServiceClassAndJpaFilteringEntitiesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly fieldTestServiceClassAndJpaFilteringEntitiesResource = httpResource<RestFieldTestServiceClassAndJpaFilteringEntity[]>(() => {
    const params = this.fieldTestServiceClassAndJpaFilteringEntitiesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of fieldTestServiceClassAndJpaFilteringEntity that have been fetched. It is updated when the fieldTestServiceClassAndJpaFilteringEntitiesResource emits a new value.
   * In case of error while fetching the fieldTestServiceClassAndJpaFilteringEntities, the signal is set to an empty array.
   */
  readonly fieldTestServiceClassAndJpaFilteringEntities = computed(() =>
    (this.fieldTestServiceClassAndJpaFilteringEntitiesResource.hasValue()
      ? this.fieldTestServiceClassAndJpaFilteringEntitiesResource.value()
      : []
    ).map(item => this.convertValueFromServer(item)),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/field-test-service-class-and-jpa-filtering-entities');

  protected convertValueFromServer(
    restFieldTestServiceClassAndJpaFilteringEntity: RestFieldTestServiceClassAndJpaFilteringEntity,
  ): IFieldTestServiceClassAndJpaFilteringEntity {
    return {
      ...restFieldTestServiceClassAndJpaFilteringEntity,
      localDateBob: restFieldTestServiceClassAndJpaFilteringEntity.localDateBob
        ? dayjs(restFieldTestServiceClassAndJpaFilteringEntity.localDateBob)
        : undefined,
      localDateRequiredBob: restFieldTestServiceClassAndJpaFilteringEntity.localDateRequiredBob
        ? dayjs(restFieldTestServiceClassAndJpaFilteringEntity.localDateRequiredBob)
        : undefined,
      instantBob: restFieldTestServiceClassAndJpaFilteringEntity.instantBob
        ? dayjs(restFieldTestServiceClassAndJpaFilteringEntity.instantBob)
        : undefined,
      instanteRequiredBob: restFieldTestServiceClassAndJpaFilteringEntity.instanteRequiredBob
        ? dayjs(restFieldTestServiceClassAndJpaFilteringEntity.instanteRequiredBob)
        : undefined,
      zonedDateTimeBob: restFieldTestServiceClassAndJpaFilteringEntity.zonedDateTimeBob
        ? dayjs(restFieldTestServiceClassAndJpaFilteringEntity.zonedDateTimeBob)
        : undefined,
      zonedDateTimeRequiredBob: restFieldTestServiceClassAndJpaFilteringEntity.zonedDateTimeRequiredBob
        ? dayjs(restFieldTestServiceClassAndJpaFilteringEntity.zonedDateTimeRequiredBob)
        : undefined,
    };
  }
}

@Service()
export class FieldTestServiceClassAndJpaFilteringEntityService extends FieldTestServiceClassAndJpaFilteringEntitiesService {
  protected readonly http = inject(HttpClient);

  create(
    fieldTestServiceClassAndJpaFilteringEntity: NewFieldTestServiceClassAndJpaFilteringEntity,
  ): Observable<IFieldTestServiceClassAndJpaFilteringEntity> {
    const copy = this.convertValueFromClient(fieldTestServiceClassAndJpaFilteringEntity);
    return this.http
      .post<RestFieldTestServiceClassAndJpaFilteringEntity>(this.resourceUrl, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(
    fieldTestServiceClassAndJpaFilteringEntity: IFieldTestServiceClassAndJpaFilteringEntity,
  ): Observable<IFieldTestServiceClassAndJpaFilteringEntity> {
    const copy = this.convertValueFromClient(fieldTestServiceClassAndJpaFilteringEntity);
    return this.http
      .put<RestFieldTestServiceClassAndJpaFilteringEntity>(
        `${this.resourceUrl}/${encodeURIComponent(this.getFieldTestServiceClassAndJpaFilteringEntityIdentifier(fieldTestServiceClassAndJpaFilteringEntity))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(
    fieldTestServiceClassAndJpaFilteringEntity: PartialUpdateFieldTestServiceClassAndJpaFilteringEntity,
  ): Observable<IFieldTestServiceClassAndJpaFilteringEntity> {
    const copy = this.convertValueFromClient(fieldTestServiceClassAndJpaFilteringEntity);
    return this.http
      .patch<RestFieldTestServiceClassAndJpaFilteringEntity>(
        `${this.resourceUrl}/${encodeURIComponent(this.getFieldTestServiceClassAndJpaFilteringEntityIdentifier(fieldTestServiceClassAndJpaFilteringEntity))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IFieldTestServiceClassAndJpaFilteringEntity> {
    return this.http
      .get<RestFieldTestServiceClassAndJpaFilteringEntity>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IFieldTestServiceClassAndJpaFilteringEntity[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFieldTestServiceClassAndJpaFilteringEntity[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFieldTestServiceClassAndJpaFilteringEntityIdentifier(
    fieldTestServiceClassAndJpaFilteringEntity: Pick<IFieldTestServiceClassAndJpaFilteringEntity, 'id'>,
  ): number {
    return fieldTestServiceClassAndJpaFilteringEntity.id;
  }

  compareFieldTestServiceClassAndJpaFilteringEntity(
    o1: Pick<IFieldTestServiceClassAndJpaFilteringEntity, 'id'> | null,
    o2: Pick<IFieldTestServiceClassAndJpaFilteringEntity, 'id'> | null,
  ): boolean {
    return o1 && o2
      ? this.getFieldTestServiceClassAndJpaFilteringEntityIdentifier(o1) ===
          this.getFieldTestServiceClassAndJpaFilteringEntityIdentifier(o2)
      : o1 === o2;
  }

  addFieldTestServiceClassAndJpaFilteringEntityToCollectionIfMissing<Type extends Pick<IFieldTestServiceClassAndJpaFilteringEntity, 'id'>>(
    fieldTestServiceClassAndJpaFilteringEntityCollection: Type[],
    ...fieldTestServiceClassAndJpaFilteringEntitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fieldTestServiceClassAndJpaFilteringEntities: Type[] = fieldTestServiceClassAndJpaFilteringEntitiesToCheck.filter(
      fieldTestServiceClassAndJpaFilteringEntityItem =>
        fieldTestServiceClassAndJpaFilteringEntityItem !== null && fieldTestServiceClassAndJpaFilteringEntityItem !== undefined,
    );
    if (fieldTestServiceClassAndJpaFilteringEntities.length > 0) {
      const fieldTestServiceClassAndJpaFilteringEntityCollectionIdentifiers = fieldTestServiceClassAndJpaFilteringEntityCollection.map(
        fieldTestServiceClassAndJpaFilteringEntityItem =>
          this.getFieldTestServiceClassAndJpaFilteringEntityIdentifier(fieldTestServiceClassAndJpaFilteringEntityItem),
      );
      const fieldTestServiceClassAndJpaFilteringEntitiesToAdd = fieldTestServiceClassAndJpaFilteringEntities.filter(
        fieldTestServiceClassAndJpaFilteringEntityItem => {
          const fieldTestServiceClassAndJpaFilteringEntityIdentifier = this.getFieldTestServiceClassAndJpaFilteringEntityIdentifier(
            fieldTestServiceClassAndJpaFilteringEntityItem,
          );
          if (
            fieldTestServiceClassAndJpaFilteringEntityCollectionIdentifiers.includes(fieldTestServiceClassAndJpaFilteringEntityIdentifier)
          ) {
            return false;
          }
          fieldTestServiceClassAndJpaFilteringEntityCollectionIdentifiers.push(fieldTestServiceClassAndJpaFilteringEntityIdentifier);
          return true;
        },
      );
      return [...fieldTestServiceClassAndJpaFilteringEntitiesToAdd, ...fieldTestServiceClassAndJpaFilteringEntityCollection];
    }
    return fieldTestServiceClassAndJpaFilteringEntityCollection;
  }

  protected convertValueFromClient<
    T extends
      | IFieldTestServiceClassAndJpaFilteringEntity
      | NewFieldTestServiceClassAndJpaFilteringEntity
      | PartialUpdateFieldTestServiceClassAndJpaFilteringEntity,
  >(fieldTestServiceClassAndJpaFilteringEntity: T): RestOf<T> {
    return {
      ...fieldTestServiceClassAndJpaFilteringEntity,
      localDateBob: fieldTestServiceClassAndJpaFilteringEntity.localDateBob?.format(DATE_FORMAT) ?? null,
      localDateRequiredBob: fieldTestServiceClassAndJpaFilteringEntity.localDateRequiredBob?.format(DATE_FORMAT) ?? null,
      instantBob: fieldTestServiceClassAndJpaFilteringEntity.instantBob?.toJSON() ?? null,
      instanteRequiredBob: fieldTestServiceClassAndJpaFilteringEntity.instanteRequiredBob?.toJSON() ?? null,
      zonedDateTimeBob: fieldTestServiceClassAndJpaFilteringEntity.zonedDateTimeBob?.toJSON() ?? null,
      zonedDateTimeRequiredBob: fieldTestServiceClassAndJpaFilteringEntity.zonedDateTimeRequiredBob?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestFieldTestServiceClassAndJpaFilteringEntity): IFieldTestServiceClassAndJpaFilteringEntity {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(
    res: RestFieldTestServiceClassAndJpaFilteringEntity[],
  ): IFieldTestServiceClassAndJpaFilteringEntity[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}

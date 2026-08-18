import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFieldTestInfiniteScrollEntity, NewFieldTestInfiniteScrollEntity } from '../field-test-infinite-scroll-entity.model';

export type PartialUpdateFieldTestInfiniteScrollEntity = Partial<IFieldTestInfiniteScrollEntity> &
  Pick<IFieldTestInfiniteScrollEntity, 'id'>;

type RestOf<T extends IFieldTestInfiniteScrollEntity | NewFieldTestInfiniteScrollEntity> = Omit<
  T,
  'localDateHugo' | 'localDateRequiredHugo' | 'instantHugo' | 'instanteRequiredHugo' | 'zonedDateTimeHugo' | 'zonedDateTimeRequiredHugo'
> & {
  localDateHugo?: string | null;
  localDateRequiredHugo?: string | null;
  instantHugo?: string | null;
  instanteRequiredHugo?: string | null;
  zonedDateTimeHugo?: string | null;
  zonedDateTimeRequiredHugo?: string | null;
};

export type RestFieldTestInfiniteScrollEntity = RestOf<IFieldTestInfiniteScrollEntity>;

export type NewRestFieldTestInfiniteScrollEntity = RestOf<NewFieldTestInfiniteScrollEntity>;

export type PartialUpdateRestFieldTestInfiniteScrollEntity = RestOf<PartialUpdateFieldTestInfiniteScrollEntity>;

@Service()
export class FieldTestInfiniteScrollEntitiesService {
  readonly fieldTestInfiniteScrollEntitiesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly fieldTestInfiniteScrollEntitiesResource = httpResource<RestFieldTestInfiniteScrollEntity[]>(() => {
    const params = this.fieldTestInfiniteScrollEntitiesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of fieldTestInfiniteScrollEntity that have been fetched. It is updated when the fieldTestInfiniteScrollEntitiesResource emits a new value.
   * In case of error while fetching the fieldTestInfiniteScrollEntities, the signal is set to an empty array.
   */
  readonly fieldTestInfiniteScrollEntities = computed(() =>
    (this.fieldTestInfiniteScrollEntitiesResource.hasValue() ? this.fieldTestInfiniteScrollEntitiesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/field-test-infinite-scroll-entities');

  protected convertValueFromServer(restFieldTestInfiniteScrollEntity: RestFieldTestInfiniteScrollEntity): IFieldTestInfiniteScrollEntity {
    return {
      ...restFieldTestInfiniteScrollEntity,
      localDateHugo: restFieldTestInfiniteScrollEntity.localDateHugo ? dayjs(restFieldTestInfiniteScrollEntity.localDateHugo) : undefined,
      localDateRequiredHugo: restFieldTestInfiniteScrollEntity.localDateRequiredHugo
        ? dayjs(restFieldTestInfiniteScrollEntity.localDateRequiredHugo)
        : undefined,
      instantHugo: restFieldTestInfiniteScrollEntity.instantHugo ? dayjs(restFieldTestInfiniteScrollEntity.instantHugo) : undefined,
      instanteRequiredHugo: restFieldTestInfiniteScrollEntity.instanteRequiredHugo
        ? dayjs(restFieldTestInfiniteScrollEntity.instanteRequiredHugo)
        : undefined,
      zonedDateTimeHugo: restFieldTestInfiniteScrollEntity.zonedDateTimeHugo
        ? dayjs(restFieldTestInfiniteScrollEntity.zonedDateTimeHugo)
        : undefined,
      zonedDateTimeRequiredHugo: restFieldTestInfiniteScrollEntity.zonedDateTimeRequiredHugo
        ? dayjs(restFieldTestInfiniteScrollEntity.zonedDateTimeRequiredHugo)
        : undefined,
    };
  }
}

@Service()
export class FieldTestInfiniteScrollEntityService extends FieldTestInfiniteScrollEntitiesService {
  protected readonly http = inject(HttpClient);

  create(fieldTestInfiniteScrollEntity: NewFieldTestInfiniteScrollEntity): Observable<IFieldTestInfiniteScrollEntity> {
    const copy = this.convertValueFromClient(fieldTestInfiniteScrollEntity);
    return this.http.post<RestFieldTestInfiniteScrollEntity>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(fieldTestInfiniteScrollEntity: IFieldTestInfiniteScrollEntity): Observable<IFieldTestInfiniteScrollEntity> {
    const copy = this.convertValueFromClient(fieldTestInfiniteScrollEntity);
    return this.http
      .put<RestFieldTestInfiniteScrollEntity>(
        `${this.resourceUrl}/${encodeURIComponent(this.getFieldTestInfiniteScrollEntityIdentifier(fieldTestInfiniteScrollEntity))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(fieldTestInfiniteScrollEntity: PartialUpdateFieldTestInfiniteScrollEntity): Observable<IFieldTestInfiniteScrollEntity> {
    const copy = this.convertValueFromClient(fieldTestInfiniteScrollEntity);
    return this.http
      .patch<RestFieldTestInfiniteScrollEntity>(
        `${this.resourceUrl}/${encodeURIComponent(this.getFieldTestInfiniteScrollEntityIdentifier(fieldTestInfiniteScrollEntity))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IFieldTestInfiniteScrollEntity> {
    return this.http
      .get<RestFieldTestInfiniteScrollEntity>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IFieldTestInfiniteScrollEntity[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFieldTestInfiniteScrollEntity[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFieldTestInfiniteScrollEntityIdentifier(fieldTestInfiniteScrollEntity: Pick<IFieldTestInfiniteScrollEntity, 'id'>): number {
    return fieldTestInfiniteScrollEntity.id;
  }

  compareFieldTestInfiniteScrollEntity(
    o1: Pick<IFieldTestInfiniteScrollEntity, 'id'> | null,
    o2: Pick<IFieldTestInfiniteScrollEntity, 'id'> | null,
  ): boolean {
    return o1 && o2
      ? this.getFieldTestInfiniteScrollEntityIdentifier(o1) === this.getFieldTestInfiniteScrollEntityIdentifier(o2)
      : o1 === o2;
  }

  addFieldTestInfiniteScrollEntityToCollectionIfMissing<Type extends Pick<IFieldTestInfiniteScrollEntity, 'id'>>(
    fieldTestInfiniteScrollEntityCollection: Type[],
    ...fieldTestInfiniteScrollEntitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fieldTestInfiniteScrollEntities: Type[] = fieldTestInfiniteScrollEntitiesToCheck.filter(
      fieldTestInfiniteScrollEntityItem => fieldTestInfiniteScrollEntityItem !== null && fieldTestInfiniteScrollEntityItem !== undefined,
    );
    if (fieldTestInfiniteScrollEntities.length > 0) {
      const fieldTestInfiniteScrollEntityCollectionIdentifiers = fieldTestInfiniteScrollEntityCollection.map(
        fieldTestInfiniteScrollEntityItem => this.getFieldTestInfiniteScrollEntityIdentifier(fieldTestInfiniteScrollEntityItem),
      );
      const fieldTestInfiniteScrollEntitiesToAdd = fieldTestInfiniteScrollEntities.filter(fieldTestInfiniteScrollEntityItem => {
        const fieldTestInfiniteScrollEntityIdentifier = this.getFieldTestInfiniteScrollEntityIdentifier(fieldTestInfiniteScrollEntityItem);
        if (fieldTestInfiniteScrollEntityCollectionIdentifiers.includes(fieldTestInfiniteScrollEntityIdentifier)) {
          return false;
        }
        fieldTestInfiniteScrollEntityCollectionIdentifiers.push(fieldTestInfiniteScrollEntityIdentifier);
        return true;
      });
      return [...fieldTestInfiniteScrollEntitiesToAdd, ...fieldTestInfiniteScrollEntityCollection];
    }
    return fieldTestInfiniteScrollEntityCollection;
  }

  protected convertValueFromClient<
    T extends IFieldTestInfiniteScrollEntity | NewFieldTestInfiniteScrollEntity | PartialUpdateFieldTestInfiniteScrollEntity,
  >(fieldTestInfiniteScrollEntity: T): RestOf<T> {
    return {
      ...fieldTestInfiniteScrollEntity,
      localDateHugo: fieldTestInfiniteScrollEntity.localDateHugo?.format(DATE_FORMAT) ?? null,
      localDateRequiredHugo: fieldTestInfiniteScrollEntity.localDateRequiredHugo?.format(DATE_FORMAT) ?? null,
      instantHugo: fieldTestInfiniteScrollEntity.instantHugo?.toJSON() ?? null,
      instanteRequiredHugo: fieldTestInfiniteScrollEntity.instanteRequiredHugo?.toJSON() ?? null,
      zonedDateTimeHugo: fieldTestInfiniteScrollEntity.zonedDateTimeHugo?.toJSON() ?? null,
      zonedDateTimeRequiredHugo: fieldTestInfiniteScrollEntity.zonedDateTimeRequiredHugo?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestFieldTestInfiniteScrollEntity): IFieldTestInfiniteScrollEntity {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestFieldTestInfiniteScrollEntity[]): IFieldTestInfiniteScrollEntity[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}

import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFieldTestServiceImplEntity, NewFieldTestServiceImplEntity } from '../field-test-service-impl-entity.model';

export type PartialUpdateFieldTestServiceImplEntity = Partial<IFieldTestServiceImplEntity> & Pick<IFieldTestServiceImplEntity, 'id'>;

type RestOf<T extends IFieldTestServiceImplEntity | NewFieldTestServiceImplEntity> = Omit<
  T,
  'localDateMika' | 'localDateRequiredMika' | 'instantMika' | 'instanteRequiredMika' | 'zonedDateTimeMika' | 'zonedDateTimeRequiredMika'
> & {
  localDateMika?: string | null;
  localDateRequiredMika?: string | null;
  instantMika?: string | null;
  instanteRequiredMika?: string | null;
  zonedDateTimeMika?: string | null;
  zonedDateTimeRequiredMika?: string | null;
};

export type RestFieldTestServiceImplEntity = RestOf<IFieldTestServiceImplEntity>;

export type NewRestFieldTestServiceImplEntity = RestOf<NewFieldTestServiceImplEntity>;

export type PartialUpdateRestFieldTestServiceImplEntity = RestOf<PartialUpdateFieldTestServiceImplEntity>;

@Service()
export class FieldTestServiceImplEntitiesService {
  readonly fieldTestServiceImplEntitiesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly fieldTestServiceImplEntitiesResource = httpResource<RestFieldTestServiceImplEntity[]>(() => {
    const params = this.fieldTestServiceImplEntitiesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of fieldTestServiceImplEntity that have been fetched. It is updated when the fieldTestServiceImplEntitiesResource emits a new value.
   * In case of error while fetching the fieldTestServiceImplEntities, the signal is set to an empty array.
   */
  readonly fieldTestServiceImplEntities = computed(() =>
    (this.fieldTestServiceImplEntitiesResource.hasValue() ? this.fieldTestServiceImplEntitiesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/field-test-service-impl-entities');

  protected convertValueFromServer(restFieldTestServiceImplEntity: RestFieldTestServiceImplEntity): IFieldTestServiceImplEntity {
    return {
      ...restFieldTestServiceImplEntity,
      localDateMika: restFieldTestServiceImplEntity.localDateMika ? dayjs(restFieldTestServiceImplEntity.localDateMika) : undefined,
      localDateRequiredMika: restFieldTestServiceImplEntity.localDateRequiredMika
        ? dayjs(restFieldTestServiceImplEntity.localDateRequiredMika)
        : undefined,
      instantMika: restFieldTestServiceImplEntity.instantMika ? dayjs(restFieldTestServiceImplEntity.instantMika) : undefined,
      instanteRequiredMika: restFieldTestServiceImplEntity.instanteRequiredMika
        ? dayjs(restFieldTestServiceImplEntity.instanteRequiredMika)
        : undefined,
      zonedDateTimeMika: restFieldTestServiceImplEntity.zonedDateTimeMika
        ? dayjs(restFieldTestServiceImplEntity.zonedDateTimeMika)
        : undefined,
      zonedDateTimeRequiredMika: restFieldTestServiceImplEntity.zonedDateTimeRequiredMika
        ? dayjs(restFieldTestServiceImplEntity.zonedDateTimeRequiredMika)
        : undefined,
    };
  }
}

@Service()
export class FieldTestServiceImplEntityService extends FieldTestServiceImplEntitiesService {
  protected readonly http = inject(HttpClient);

  create(fieldTestServiceImplEntity: NewFieldTestServiceImplEntity): Observable<IFieldTestServiceImplEntity> {
    const copy = this.convertValueFromClient(fieldTestServiceImplEntity);
    return this.http.post<RestFieldTestServiceImplEntity>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(fieldTestServiceImplEntity: IFieldTestServiceImplEntity): Observable<IFieldTestServiceImplEntity> {
    const copy = this.convertValueFromClient(fieldTestServiceImplEntity);
    return this.http
      .put<RestFieldTestServiceImplEntity>(
        `${this.resourceUrl}/${encodeURIComponent(this.getFieldTestServiceImplEntityIdentifier(fieldTestServiceImplEntity))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(fieldTestServiceImplEntity: PartialUpdateFieldTestServiceImplEntity): Observable<IFieldTestServiceImplEntity> {
    const copy = this.convertValueFromClient(fieldTestServiceImplEntity);
    return this.http
      .patch<RestFieldTestServiceImplEntity>(
        `${this.resourceUrl}/${encodeURIComponent(this.getFieldTestServiceImplEntityIdentifier(fieldTestServiceImplEntity))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IFieldTestServiceImplEntity> {
    return this.http
      .get<RestFieldTestServiceImplEntity>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IFieldTestServiceImplEntity[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFieldTestServiceImplEntity[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFieldTestServiceImplEntityIdentifier(fieldTestServiceImplEntity: Pick<IFieldTestServiceImplEntity, 'id'>): number {
    return fieldTestServiceImplEntity.id;
  }

  compareFieldTestServiceImplEntity(
    o1: Pick<IFieldTestServiceImplEntity, 'id'> | null,
    o2: Pick<IFieldTestServiceImplEntity, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getFieldTestServiceImplEntityIdentifier(o1) === this.getFieldTestServiceImplEntityIdentifier(o2) : o1 === o2;
  }

  addFieldTestServiceImplEntityToCollectionIfMissing<Type extends Pick<IFieldTestServiceImplEntity, 'id'>>(
    fieldTestServiceImplEntityCollection: Type[],
    ...fieldTestServiceImplEntitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fieldTestServiceImplEntities: Type[] = fieldTestServiceImplEntitiesToCheck.filter(
      fieldTestServiceImplEntityItem => fieldTestServiceImplEntityItem !== null && fieldTestServiceImplEntityItem !== undefined,
    );
    if (fieldTestServiceImplEntities.length > 0) {
      const fieldTestServiceImplEntityCollectionIdentifiers = fieldTestServiceImplEntityCollection.map(fieldTestServiceImplEntityItem =>
        this.getFieldTestServiceImplEntityIdentifier(fieldTestServiceImplEntityItem),
      );
      const fieldTestServiceImplEntitiesToAdd = fieldTestServiceImplEntities.filter(fieldTestServiceImplEntityItem => {
        const fieldTestServiceImplEntityIdentifier = this.getFieldTestServiceImplEntityIdentifier(fieldTestServiceImplEntityItem);
        if (fieldTestServiceImplEntityCollectionIdentifiers.includes(fieldTestServiceImplEntityIdentifier)) {
          return false;
        }
        fieldTestServiceImplEntityCollectionIdentifiers.push(fieldTestServiceImplEntityIdentifier);
        return true;
      });
      return [...fieldTestServiceImplEntitiesToAdd, ...fieldTestServiceImplEntityCollection];
    }
    return fieldTestServiceImplEntityCollection;
  }

  protected convertValueFromClient<
    T extends IFieldTestServiceImplEntity | NewFieldTestServiceImplEntity | PartialUpdateFieldTestServiceImplEntity,
  >(fieldTestServiceImplEntity: T): RestOf<T> {
    return {
      ...fieldTestServiceImplEntity,
      localDateMika: fieldTestServiceImplEntity.localDateMika?.format(DATE_FORMAT) ?? null,
      localDateRequiredMika: fieldTestServiceImplEntity.localDateRequiredMika?.format(DATE_FORMAT) ?? null,
      instantMika: fieldTestServiceImplEntity.instantMika?.toJSON() ?? null,
      instanteRequiredMika: fieldTestServiceImplEntity.instanteRequiredMika?.toJSON() ?? null,
      zonedDateTimeMika: fieldTestServiceImplEntity.zonedDateTimeMika?.toJSON() ?? null,
      zonedDateTimeRequiredMika: fieldTestServiceImplEntity.zonedDateTimeRequiredMika?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestFieldTestServiceImplEntity): IFieldTestServiceImplEntity {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestFieldTestServiceImplEntity[]): IFieldTestServiceImplEntity[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}

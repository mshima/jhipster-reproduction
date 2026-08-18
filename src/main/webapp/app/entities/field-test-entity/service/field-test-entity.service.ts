import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFieldTestEntity, NewFieldTestEntity } from '../field-test-entity.model';

export type PartialUpdateFieldTestEntity = Partial<IFieldTestEntity> & Pick<IFieldTestEntity, 'id'>;

type RestOf<T extends IFieldTestEntity | NewFieldTestEntity> = Omit<
  T,
  'localDateTom' | 'localDateRequiredTom' | 'instantTom' | 'instantRequiredTom' | 'zonedDateTimeTom' | 'zonedDateTimeRequiredTom'
> & {
  localDateTom?: string | null;
  localDateRequiredTom?: string | null;
  instantTom?: string | null;
  instantRequiredTom?: string | null;
  zonedDateTimeTom?: string | null;
  zonedDateTimeRequiredTom?: string | null;
};

export type RestFieldTestEntity = RestOf<IFieldTestEntity>;

export type NewRestFieldTestEntity = RestOf<NewFieldTestEntity>;

export type PartialUpdateRestFieldTestEntity = RestOf<PartialUpdateFieldTestEntity>;

@Service()
export class FieldTestEntitiesService {
  readonly fieldTestEntitiesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly fieldTestEntitiesResource = httpResource<RestFieldTestEntity[]>(() => {
    const params = this.fieldTestEntitiesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of fieldTestEntity that have been fetched. It is updated when the fieldTestEntitiesResource emits a new value.
   * In case of error while fetching the fieldTestEntities, the signal is set to an empty array.
   */
  readonly fieldTestEntities = computed(() =>
    (this.fieldTestEntitiesResource.hasValue() ? this.fieldTestEntitiesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/field-test-entities');

  protected convertValueFromServer(restFieldTestEntity: RestFieldTestEntity): IFieldTestEntity {
    return {
      ...restFieldTestEntity,
      localDateTom: restFieldTestEntity.localDateTom ? dayjs(restFieldTestEntity.localDateTom) : undefined,
      localDateRequiredTom: restFieldTestEntity.localDateRequiredTom ? dayjs(restFieldTestEntity.localDateRequiredTom) : undefined,
      instantTom: restFieldTestEntity.instantTom ? dayjs(restFieldTestEntity.instantTom) : undefined,
      instantRequiredTom: restFieldTestEntity.instantRequiredTom ? dayjs(restFieldTestEntity.instantRequiredTom) : undefined,
      zonedDateTimeTom: restFieldTestEntity.zonedDateTimeTom ? dayjs(restFieldTestEntity.zonedDateTimeTom) : undefined,
      zonedDateTimeRequiredTom: restFieldTestEntity.zonedDateTimeRequiredTom
        ? dayjs(restFieldTestEntity.zonedDateTimeRequiredTom)
        : undefined,
    };
  }
}

@Service()
export class FieldTestEntityService extends FieldTestEntitiesService {
  protected readonly http = inject(HttpClient);

  create(fieldTestEntity: NewFieldTestEntity): Observable<IFieldTestEntity> {
    const copy = this.convertValueFromClient(fieldTestEntity);
    return this.http.post<RestFieldTestEntity>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(fieldTestEntity: IFieldTestEntity): Observable<IFieldTestEntity> {
    const copy = this.convertValueFromClient(fieldTestEntity);
    return this.http
      .put<RestFieldTestEntity>(`${this.resourceUrl}/${encodeURIComponent(this.getFieldTestEntityIdentifier(fieldTestEntity))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(fieldTestEntity: PartialUpdateFieldTestEntity): Observable<IFieldTestEntity> {
    const copy = this.convertValueFromClient(fieldTestEntity);
    return this.http
      .patch<RestFieldTestEntity>(`${this.resourceUrl}/${encodeURIComponent(this.getFieldTestEntityIdentifier(fieldTestEntity))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IFieldTestEntity> {
    return this.http
      .get<RestFieldTestEntity>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IFieldTestEntity[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFieldTestEntity[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFieldTestEntityIdentifier(fieldTestEntity: Pick<IFieldTestEntity, 'id'>): number {
    return fieldTestEntity.id;
  }

  compareFieldTestEntity(o1: Pick<IFieldTestEntity, 'id'> | null, o2: Pick<IFieldTestEntity, 'id'> | null): boolean {
    return o1 && o2 ? this.getFieldTestEntityIdentifier(o1) === this.getFieldTestEntityIdentifier(o2) : o1 === o2;
  }

  addFieldTestEntityToCollectionIfMissing<Type extends Pick<IFieldTestEntity, 'id'>>(
    fieldTestEntityCollection: Type[],
    ...fieldTestEntitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fieldTestEntities: Type[] = fieldTestEntitiesToCheck.filter(
      fieldTestEntityItem => fieldTestEntityItem !== null && fieldTestEntityItem !== undefined,
    );
    if (fieldTestEntities.length > 0) {
      const fieldTestEntityCollectionIdentifiers = fieldTestEntityCollection.map(fieldTestEntityItem =>
        this.getFieldTestEntityIdentifier(fieldTestEntityItem),
      );
      const fieldTestEntitiesToAdd = fieldTestEntities.filter(fieldTestEntityItem => {
        const fieldTestEntityIdentifier = this.getFieldTestEntityIdentifier(fieldTestEntityItem);
        if (fieldTestEntityCollectionIdentifiers.includes(fieldTestEntityIdentifier)) {
          return false;
        }
        fieldTestEntityCollectionIdentifiers.push(fieldTestEntityIdentifier);
        return true;
      });
      return [...fieldTestEntitiesToAdd, ...fieldTestEntityCollection];
    }
    return fieldTestEntityCollection;
  }

  protected convertValueFromClient<T extends IFieldTestEntity | NewFieldTestEntity | PartialUpdateFieldTestEntity>(
    fieldTestEntity: T,
  ): RestOf<T> {
    return {
      ...fieldTestEntity,
      localDateTom: fieldTestEntity.localDateTom?.format(DATE_FORMAT) ?? null,
      localDateRequiredTom: fieldTestEntity.localDateRequiredTom?.format(DATE_FORMAT) ?? null,
      instantTom: fieldTestEntity.instantTom?.toJSON() ?? null,
      instantRequiredTom: fieldTestEntity.instantRequiredTom?.toJSON() ?? null,
      zonedDateTimeTom: fieldTestEntity.zonedDateTimeTom?.toJSON() ?? null,
      zonedDateTimeRequiredTom: fieldTestEntity.zonedDateTimeRequiredTom?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestFieldTestEntity): IFieldTestEntity {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestFieldTestEntity[]): IFieldTestEntity[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}

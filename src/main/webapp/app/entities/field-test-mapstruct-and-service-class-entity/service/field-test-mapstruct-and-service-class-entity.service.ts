import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import {
  IFieldTestMapstructAndServiceClassEntity,
  NewFieldTestMapstructAndServiceClassEntity,
} from '../field-test-mapstruct-and-service-class-entity.model';

export type PartialUpdateFieldTestMapstructAndServiceClassEntity = Partial<IFieldTestMapstructAndServiceClassEntity> &
  Pick<IFieldTestMapstructAndServiceClassEntity, 'id'>;

type RestOf<T extends IFieldTestMapstructAndServiceClassEntity | NewFieldTestMapstructAndServiceClassEntity> = Omit<
  T,
  'localDateEva' | 'localDateRequiredEva' | 'instantEva' | 'instanteRequiredEva' | 'zonedDateTimeEva' | 'zonedDateTimeRequiredEva'
> & {
  localDateEva?: string | null;
  localDateRequiredEva?: string | null;
  instantEva?: string | null;
  instanteRequiredEva?: string | null;
  zonedDateTimeEva?: string | null;
  zonedDateTimeRequiredEva?: string | null;
};

export type RestFieldTestMapstructAndServiceClassEntity = RestOf<IFieldTestMapstructAndServiceClassEntity>;

export type NewRestFieldTestMapstructAndServiceClassEntity = RestOf<NewFieldTestMapstructAndServiceClassEntity>;

export type PartialUpdateRestFieldTestMapstructAndServiceClassEntity = RestOf<PartialUpdateFieldTestMapstructAndServiceClassEntity>;

@Service()
export class FieldTestMapstructAndServiceClassEntitiesService {
  readonly fieldTestMapstructAndServiceClassEntitiesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly fieldTestMapstructAndServiceClassEntitiesResource = httpResource<RestFieldTestMapstructAndServiceClassEntity[]>(() => {
    const params = this.fieldTestMapstructAndServiceClassEntitiesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of fieldTestMapstructAndServiceClassEntity that have been fetched. It is updated when the fieldTestMapstructAndServiceClassEntitiesResource emits a new value.
   * In case of error while fetching the fieldTestMapstructAndServiceClassEntities, the signal is set to an empty array.
   */
  readonly fieldTestMapstructAndServiceClassEntities = computed(() =>
    (this.fieldTestMapstructAndServiceClassEntitiesResource.hasValue()
      ? this.fieldTestMapstructAndServiceClassEntitiesResource.value()
      : []
    ).map(item => this.convertValueFromServer(item)),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/field-test-mapstruct-and-service-class-entities');

  protected convertValueFromServer(
    restFieldTestMapstructAndServiceClassEntity: RestFieldTestMapstructAndServiceClassEntity,
  ): IFieldTestMapstructAndServiceClassEntity {
    return {
      ...restFieldTestMapstructAndServiceClassEntity,
      localDateEva: restFieldTestMapstructAndServiceClassEntity.localDateEva
        ? dayjs(restFieldTestMapstructAndServiceClassEntity.localDateEva)
        : undefined,
      localDateRequiredEva: restFieldTestMapstructAndServiceClassEntity.localDateRequiredEva
        ? dayjs(restFieldTestMapstructAndServiceClassEntity.localDateRequiredEva)
        : undefined,
      instantEva: restFieldTestMapstructAndServiceClassEntity.instantEva
        ? dayjs(restFieldTestMapstructAndServiceClassEntity.instantEva)
        : undefined,
      instanteRequiredEva: restFieldTestMapstructAndServiceClassEntity.instanteRequiredEva
        ? dayjs(restFieldTestMapstructAndServiceClassEntity.instanteRequiredEva)
        : undefined,
      zonedDateTimeEva: restFieldTestMapstructAndServiceClassEntity.zonedDateTimeEva
        ? dayjs(restFieldTestMapstructAndServiceClassEntity.zonedDateTimeEva)
        : undefined,
      zonedDateTimeRequiredEva: restFieldTestMapstructAndServiceClassEntity.zonedDateTimeRequiredEva
        ? dayjs(restFieldTestMapstructAndServiceClassEntity.zonedDateTimeRequiredEva)
        : undefined,
    };
  }
}

@Service()
export class FieldTestMapstructAndServiceClassEntityService extends FieldTestMapstructAndServiceClassEntitiesService {
  protected readonly http = inject(HttpClient);

  create(
    fieldTestMapstructAndServiceClassEntity: NewFieldTestMapstructAndServiceClassEntity,
  ): Observable<IFieldTestMapstructAndServiceClassEntity> {
    const copy = this.convertValueFromClient(fieldTestMapstructAndServiceClassEntity);
    return this.http
      .post<RestFieldTestMapstructAndServiceClassEntity>(this.resourceUrl, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(
    fieldTestMapstructAndServiceClassEntity: IFieldTestMapstructAndServiceClassEntity,
  ): Observable<IFieldTestMapstructAndServiceClassEntity> {
    const copy = this.convertValueFromClient(fieldTestMapstructAndServiceClassEntity);
    return this.http
      .put<RestFieldTestMapstructAndServiceClassEntity>(
        `${this.resourceUrl}/${encodeURIComponent(this.getFieldTestMapstructAndServiceClassEntityIdentifier(fieldTestMapstructAndServiceClassEntity))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(
    fieldTestMapstructAndServiceClassEntity: PartialUpdateFieldTestMapstructAndServiceClassEntity,
  ): Observable<IFieldTestMapstructAndServiceClassEntity> {
    const copy = this.convertValueFromClient(fieldTestMapstructAndServiceClassEntity);
    return this.http
      .patch<RestFieldTestMapstructAndServiceClassEntity>(
        `${this.resourceUrl}/${encodeURIComponent(this.getFieldTestMapstructAndServiceClassEntityIdentifier(fieldTestMapstructAndServiceClassEntity))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IFieldTestMapstructAndServiceClassEntity> {
    return this.http
      .get<RestFieldTestMapstructAndServiceClassEntity>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IFieldTestMapstructAndServiceClassEntity[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFieldTestMapstructAndServiceClassEntity[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFieldTestMapstructAndServiceClassEntityIdentifier(
    fieldTestMapstructAndServiceClassEntity: Pick<IFieldTestMapstructAndServiceClassEntity, 'id'>,
  ): number {
    return fieldTestMapstructAndServiceClassEntity.id;
  }

  compareFieldTestMapstructAndServiceClassEntity(
    o1: Pick<IFieldTestMapstructAndServiceClassEntity, 'id'> | null,
    o2: Pick<IFieldTestMapstructAndServiceClassEntity, 'id'> | null,
  ): boolean {
    return o1 && o2
      ? this.getFieldTestMapstructAndServiceClassEntityIdentifier(o1) === this.getFieldTestMapstructAndServiceClassEntityIdentifier(o2)
      : o1 === o2;
  }

  addFieldTestMapstructAndServiceClassEntityToCollectionIfMissing<Type extends Pick<IFieldTestMapstructAndServiceClassEntity, 'id'>>(
    fieldTestMapstructAndServiceClassEntityCollection: Type[],
    ...fieldTestMapstructAndServiceClassEntitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fieldTestMapstructAndServiceClassEntities: Type[] = fieldTestMapstructAndServiceClassEntitiesToCheck.filter(
      fieldTestMapstructAndServiceClassEntityItem =>
        fieldTestMapstructAndServiceClassEntityItem !== null && fieldTestMapstructAndServiceClassEntityItem !== undefined,
    );
    if (fieldTestMapstructAndServiceClassEntities.length > 0) {
      const fieldTestMapstructAndServiceClassEntityCollectionIdentifiers = fieldTestMapstructAndServiceClassEntityCollection.map(
        fieldTestMapstructAndServiceClassEntityItem =>
          this.getFieldTestMapstructAndServiceClassEntityIdentifier(fieldTestMapstructAndServiceClassEntityItem),
      );
      const fieldTestMapstructAndServiceClassEntitiesToAdd = fieldTestMapstructAndServiceClassEntities.filter(
        fieldTestMapstructAndServiceClassEntityItem => {
          const fieldTestMapstructAndServiceClassEntityIdentifier = this.getFieldTestMapstructAndServiceClassEntityIdentifier(
            fieldTestMapstructAndServiceClassEntityItem,
          );
          if (fieldTestMapstructAndServiceClassEntityCollectionIdentifiers.includes(fieldTestMapstructAndServiceClassEntityIdentifier)) {
            return false;
          }
          fieldTestMapstructAndServiceClassEntityCollectionIdentifiers.push(fieldTestMapstructAndServiceClassEntityIdentifier);
          return true;
        },
      );
      return [...fieldTestMapstructAndServiceClassEntitiesToAdd, ...fieldTestMapstructAndServiceClassEntityCollection];
    }
    return fieldTestMapstructAndServiceClassEntityCollection;
  }

  protected convertValueFromClient<
    T extends
      | IFieldTestMapstructAndServiceClassEntity
      | NewFieldTestMapstructAndServiceClassEntity
      | PartialUpdateFieldTestMapstructAndServiceClassEntity,
  >(fieldTestMapstructAndServiceClassEntity: T): RestOf<T> {
    return {
      ...fieldTestMapstructAndServiceClassEntity,
      localDateEva: fieldTestMapstructAndServiceClassEntity.localDateEva?.format(DATE_FORMAT) ?? null,
      localDateRequiredEva: fieldTestMapstructAndServiceClassEntity.localDateRequiredEva?.format(DATE_FORMAT) ?? null,
      instantEva: fieldTestMapstructAndServiceClassEntity.instantEva?.toJSON() ?? null,
      instanteRequiredEva: fieldTestMapstructAndServiceClassEntity.instanteRequiredEva?.toJSON() ?? null,
      zonedDateTimeEva: fieldTestMapstructAndServiceClassEntity.zonedDateTimeEva?.toJSON() ?? null,
      zonedDateTimeRequiredEva: fieldTestMapstructAndServiceClassEntity.zonedDateTimeRequiredEva?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestFieldTestMapstructAndServiceClassEntity): IFieldTestMapstructAndServiceClassEntity {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestFieldTestMapstructAndServiceClassEntity[]): IFieldTestMapstructAndServiceClassEntity[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}

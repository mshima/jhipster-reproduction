import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFieldTestEnumWithValue, NewFieldTestEnumWithValue } from '../field-test-enum-with-value.model';

export type PartialUpdateFieldTestEnumWithValue = Partial<IFieldTestEnumWithValue> & Pick<IFieldTestEnumWithValue, 'id'>;

@Service()
export class FieldTestEnumWithValuesService {
  readonly fieldTestEnumWithValuesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly fieldTestEnumWithValuesResource = httpResource<IFieldTestEnumWithValue[]>(() => {
    const params = this.fieldTestEnumWithValuesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of fieldTestEnumWithValue that have been fetched. It is updated when the fieldTestEnumWithValuesResource emits a new value.
   * In case of error while fetching the fieldTestEnumWithValues, the signal is set to an empty array.
   */
  readonly fieldTestEnumWithValues = computed(() =>
    this.fieldTestEnumWithValuesResource.hasValue() ? this.fieldTestEnumWithValuesResource.value() : [],
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/field-test-enum-with-values');
}

@Service()
export class FieldTestEnumWithValueService extends FieldTestEnumWithValuesService {
  protected readonly http = inject(HttpClient);

  create(fieldTestEnumWithValue: NewFieldTestEnumWithValue): Observable<IFieldTestEnumWithValue> {
    return this.http.post<IFieldTestEnumWithValue>(this.resourceUrl, fieldTestEnumWithValue);
  }

  update(fieldTestEnumWithValue: IFieldTestEnumWithValue): Observable<IFieldTestEnumWithValue> {
    return this.http.put<IFieldTestEnumWithValue>(
      `${this.resourceUrl}/${encodeURIComponent(this.getFieldTestEnumWithValueIdentifier(fieldTestEnumWithValue))}`,
      fieldTestEnumWithValue,
    );
  }

  partialUpdate(fieldTestEnumWithValue: PartialUpdateFieldTestEnumWithValue): Observable<IFieldTestEnumWithValue> {
    return this.http.patch<IFieldTestEnumWithValue>(
      `${this.resourceUrl}/${encodeURIComponent(this.getFieldTestEnumWithValueIdentifier(fieldTestEnumWithValue))}`,
      fieldTestEnumWithValue,
    );
  }

  find(id: number): Observable<IFieldTestEnumWithValue> {
    return this.http.get<IFieldTestEnumWithValue>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IFieldTestEnumWithValue[]>> {
    const options = createRequestOption(req);
    return this.http.get<IFieldTestEnumWithValue[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFieldTestEnumWithValueIdentifier(fieldTestEnumWithValue: Pick<IFieldTestEnumWithValue, 'id'>): number {
    return fieldTestEnumWithValue.id;
  }

  compareFieldTestEnumWithValue(o1: Pick<IFieldTestEnumWithValue, 'id'> | null, o2: Pick<IFieldTestEnumWithValue, 'id'> | null): boolean {
    return o1 && o2 ? this.getFieldTestEnumWithValueIdentifier(o1) === this.getFieldTestEnumWithValueIdentifier(o2) : o1 === o2;
  }

  addFieldTestEnumWithValueToCollectionIfMissing<Type extends Pick<IFieldTestEnumWithValue, 'id'>>(
    fieldTestEnumWithValueCollection: Type[],
    ...fieldTestEnumWithValuesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fieldTestEnumWithValues: Type[] = fieldTestEnumWithValuesToCheck.filter(
      fieldTestEnumWithValueItem => fieldTestEnumWithValueItem !== null && fieldTestEnumWithValueItem !== undefined,
    );
    if (fieldTestEnumWithValues.length > 0) {
      const fieldTestEnumWithValueCollectionIdentifiers = fieldTestEnumWithValueCollection.map(fieldTestEnumWithValueItem =>
        this.getFieldTestEnumWithValueIdentifier(fieldTestEnumWithValueItem),
      );
      const fieldTestEnumWithValuesToAdd = fieldTestEnumWithValues.filter(fieldTestEnumWithValueItem => {
        const fieldTestEnumWithValueIdentifier = this.getFieldTestEnumWithValueIdentifier(fieldTestEnumWithValueItem);
        if (fieldTestEnumWithValueCollectionIdentifiers.includes(fieldTestEnumWithValueIdentifier)) {
          return false;
        }
        fieldTestEnumWithValueCollectionIdentifiers.push(fieldTestEnumWithValueIdentifier);
        return true;
      });
      return [...fieldTestEnumWithValuesToAdd, ...fieldTestEnumWithValueCollection];
    }
    return fieldTestEnumWithValueCollection;
  }
}

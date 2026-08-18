import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { asapScheduler, catchError, Observable, scheduled } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILabel } from '../label.model';

@Service()
export class LabelsService {
  readonly labelsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly labelsResource = httpResource<ILabel[]>(() => {
    const params = this.labelsParams();
    if (!params) {
      return undefined;
    }
    return { url: params.query ? this.resourceSearchUrl : this.resourceUrl, params };
  });
  /**
   * This signal holds the list of label that have been fetched. It is updated when the labelsResource emits a new value.
   * In case of error while fetching the labels, the signal is set to an empty array.
   */
  readonly labels = computed(() => (this.labelsResource.hasValue() ? this.labelsResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/labels');
  protected readonly resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/labels/_search');
}

@Service()
export class LabelService extends LabelsService {
  protected readonly http = inject(HttpClient);

  find(id: number): Observable<ILabel> {
    return this.http.get<ILabel>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ILabel[]>> {
    const options = createRequestOption(req);
    return this.http.get<ILabel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<ILabel[]> {
    const options = createRequestOption(req);
    return this.http.get<ILabel[]>(this.resourceSearchUrl, { params: options }).pipe(catchError(() => scheduled([], asapScheduler)));
  }

  getLabelIdentifier(label: Pick<ILabel, 'id'>): number {
    return label.id;
  }

  compareLabel(o1: Pick<ILabel, 'id'> | null, o2: Pick<ILabel, 'id'> | null): boolean {
    return o1 && o2 ? this.getLabelIdentifier(o1) === this.getLabelIdentifier(o2) : o1 === o2;
  }

  addLabelToCollectionIfMissing<Type extends Pick<ILabel, 'id'>>(
    labelCollection: Type[],
    ...labelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const labels: Type[] = labelsToCheck.filter(labelItem => labelItem !== null && labelItem !== undefined);
    if (labels.length > 0) {
      const labelCollectionIdentifiers = labelCollection.map(labelItem => this.getLabelIdentifier(labelItem));
      const labelsToAdd = labels.filter(labelItem => {
        const labelIdentifier = this.getLabelIdentifier(labelItem);
        if (labelCollectionIdentifiers.includes(labelIdentifier)) {
          return false;
        }
        labelCollectionIdentifiers.push(labelIdentifier);
        return true;
      });
      return [...labelsToAdd, ...labelCollection];
    }
    return labelCollection;
  }
}

import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICustomPackageParent, NewCustomPackageParent } from '../custom-package-parent.model';

export type PartialUpdateCustomPackageParent = Partial<ICustomPackageParent> & Pick<ICustomPackageParent, 'id'>;

@Service()
export class CustomPackageParentsService {
  readonly customPackageParentsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly customPackageParentsResource = httpResource<ICustomPackageParent[]>(() => {
    const params = this.customPackageParentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of customPackageParent that have been fetched. It is updated when the customPackageParentsResource emits a new value.
   * In case of error while fetching the customPackageParents, the signal is set to an empty array.
   */
  readonly customPackageParents = computed(() =>
    this.customPackageParentsResource.hasValue() ? this.customPackageParentsResource.value() : [],
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/custom-package-parents');
}

@Service()
export class CustomPackageParentService extends CustomPackageParentsService {
  protected readonly http = inject(HttpClient);

  create(customPackageParent: NewCustomPackageParent): Observable<ICustomPackageParent> {
    return this.http.post<ICustomPackageParent>(this.resourceUrl, customPackageParent);
  }

  update(customPackageParent: ICustomPackageParent): Observable<ICustomPackageParent> {
    return this.http.put<ICustomPackageParent>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCustomPackageParentIdentifier(customPackageParent))}`,
      customPackageParent,
    );
  }

  partialUpdate(customPackageParent: PartialUpdateCustomPackageParent): Observable<ICustomPackageParent> {
    return this.http.patch<ICustomPackageParent>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCustomPackageParentIdentifier(customPackageParent))}`,
      customPackageParent,
    );
  }

  find(id: number): Observable<ICustomPackageParent> {
    return this.http.get<ICustomPackageParent>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ICustomPackageParent[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICustomPackageParent[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCustomPackageParentIdentifier(customPackageParent: Pick<ICustomPackageParent, 'id'>): number {
    return customPackageParent.id;
  }

  compareCustomPackageParent(o1: Pick<ICustomPackageParent, 'id'> | null, o2: Pick<ICustomPackageParent, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustomPackageParentIdentifier(o1) === this.getCustomPackageParentIdentifier(o2) : o1 === o2;
  }

  addCustomPackageParentToCollectionIfMissing<Type extends Pick<ICustomPackageParent, 'id'>>(
    customPackageParentCollection: Type[],
    ...customPackageParentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const customPackageParents: Type[] = customPackageParentsToCheck.filter(
      customPackageParentItem => customPackageParentItem !== null && customPackageParentItem !== undefined,
    );
    if (customPackageParents.length > 0) {
      const customPackageParentCollectionIdentifiers = customPackageParentCollection.map(customPackageParentItem =>
        this.getCustomPackageParentIdentifier(customPackageParentItem),
      );
      const customPackageParentsToAdd = customPackageParents.filter(customPackageParentItem => {
        const customPackageParentIdentifier = this.getCustomPackageParentIdentifier(customPackageParentItem);
        if (customPackageParentCollectionIdentifiers.includes(customPackageParentIdentifier)) {
          return false;
        }
        customPackageParentCollectionIdentifiers.push(customPackageParentIdentifier);
        return true;
      });
      return [...customPackageParentsToAdd, ...customPackageParentCollection];
    }
    return customPackageParentCollection;
  }
}

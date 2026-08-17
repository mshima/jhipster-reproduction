import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICustomPackageChild, NewCustomPackageChild } from '../custom-package-child.model';

export type PartialUpdateCustomPackageChild = Partial<ICustomPackageChild> & Pick<ICustomPackageChild, 'id'>;

@Service()
export class CustomPackageChildrenService {
  readonly customPackageChildrenParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly customPackageChildrenResource = httpResource<ICustomPackageChild[]>(() => {
    const params = this.customPackageChildrenParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of customPackageChild that have been fetched. It is updated when the customPackageChildrenResource emits a new value.
   * In case of error while fetching the customPackageChildren, the signal is set to an empty array.
   */
  readonly customPackageChildren = computed(() =>
    this.customPackageChildrenResource.hasValue() ? this.customPackageChildrenResource.value() : [],
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/custom-package-children');
}

@Service()
export class CustomPackageChildService extends CustomPackageChildrenService {
  protected readonly http = inject(HttpClient);

  create(customPackageChild: NewCustomPackageChild): Observable<ICustomPackageChild> {
    return this.http.post<ICustomPackageChild>(this.resourceUrl, customPackageChild);
  }

  update(customPackageChild: ICustomPackageChild): Observable<ICustomPackageChild> {
    return this.http.put<ICustomPackageChild>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCustomPackageChildIdentifier(customPackageChild))}`,
      customPackageChild,
    );
  }

  partialUpdate(customPackageChild: PartialUpdateCustomPackageChild): Observable<ICustomPackageChild> {
    return this.http.patch<ICustomPackageChild>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCustomPackageChildIdentifier(customPackageChild))}`,
      customPackageChild,
    );
  }

  find(id: number): Observable<ICustomPackageChild> {
    return this.http.get<ICustomPackageChild>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ICustomPackageChild[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICustomPackageChild[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCustomPackageChildIdentifier(customPackageChild: Pick<ICustomPackageChild, 'id'>): number {
    return customPackageChild.id;
  }

  compareCustomPackageChild(o1: Pick<ICustomPackageChild, 'id'> | null, o2: Pick<ICustomPackageChild, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustomPackageChildIdentifier(o1) === this.getCustomPackageChildIdentifier(o2) : o1 === o2;
  }

  addCustomPackageChildToCollectionIfMissing<Type extends Pick<ICustomPackageChild, 'id'>>(
    customPackageChildCollection: Type[],
    ...customPackageChildrenToCheck: (Type | null | undefined)[]
  ): Type[] {
    const customPackageChildren: Type[] = customPackageChildrenToCheck.filter(
      customPackageChildItem => customPackageChildItem !== null && customPackageChildItem !== undefined,
    );
    if (customPackageChildren.length > 0) {
      const customPackageChildCollectionIdentifiers = customPackageChildCollection.map(customPackageChildItem =>
        this.getCustomPackageChildIdentifier(customPackageChildItem),
      );
      const customPackageChildrenToAdd = customPackageChildren.filter(customPackageChildItem => {
        const customPackageChildIdentifier = this.getCustomPackageChildIdentifier(customPackageChildItem);
        if (customPackageChildCollectionIdentifiers.includes(customPackageChildIdentifier)) {
          return false;
        }
        customPackageChildCollectionIdentifiers.push(customPackageChildIdentifier);
        return true;
      });
      return [...customPackageChildrenToAdd, ...customPackageChildCollection];
    }
    return customPackageChildCollection;
  }
}

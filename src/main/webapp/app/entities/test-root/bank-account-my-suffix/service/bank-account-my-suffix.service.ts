import { computed, inject, Service, signal } from '@angular/core';
import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBankAccountMySuffix, NewBankAccountMySuffix } from '../bank-account-my-suffix.model';

export type PartialUpdateBankAccountMySuffix = Partial<IBankAccountMySuffix> & Pick<IBankAccountMySuffix, 'id'>;

type RestOf<T extends IBankAccountMySuffix | NewBankAccountMySuffix> = Omit<T, 'openingDay' | 'lastOperationDate'> & {
  openingDay?: string | null;
  lastOperationDate?: string | null;
};

export type RestBankAccountMySuffix = RestOf<IBankAccountMySuffix>;

export type NewRestBankAccountMySuffix = RestOf<NewBankAccountMySuffix>;

export type PartialUpdateRestBankAccountMySuffix = RestOf<PartialUpdateBankAccountMySuffix>;

@Service()
export class BankAccountMySuffixesService {
  readonly bankAccountsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly bankAccountsResource = httpResource<RestBankAccountMySuffix[]>(() => {
    const params = this.bankAccountsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of bankAccount that have been fetched. It is updated when the bankAccountsResource emits a new value.
   * In case of error while fetching the bankAccounts, the signal is set to an empty array.
   */
  readonly bankAccounts = computed(() =>
    (this.bankAccountsResource.hasValue() ? this.bankAccountsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/bank-accounts');

  protected convertValueFromServer(restBankAccountMySuffix: RestBankAccountMySuffix): IBankAccountMySuffix {
    return {
      ...restBankAccountMySuffix,
      openingDay: restBankAccountMySuffix.openingDay ? dayjs(restBankAccountMySuffix.openingDay) : undefined,
      lastOperationDate: restBankAccountMySuffix.lastOperationDate ? dayjs(restBankAccountMySuffix.lastOperationDate) : undefined,
    };
  }
}

@Service()
export class BankAccountMySuffixService extends BankAccountMySuffixesService {
  protected readonly http = inject(HttpClient);

  create(bankAccount: NewBankAccountMySuffix): Observable<IBankAccountMySuffix> {
    const copy = this.convertValueFromClient(bankAccount);
    return this.http.post<RestBankAccountMySuffix>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(bankAccount: IBankAccountMySuffix): Observable<IBankAccountMySuffix> {
    const copy = this.convertValueFromClient(bankAccount);
    return this.http
      .put<RestBankAccountMySuffix>(`${this.resourceUrl}/${encodeURIComponent(this.getBankAccountMySuffixIdentifier(bankAccount))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(bankAccount: PartialUpdateBankAccountMySuffix): Observable<IBankAccountMySuffix> {
    const copy = this.convertValueFromClient(bankAccount);
    return this.http
      .patch<RestBankAccountMySuffix>(`${this.resourceUrl}/${encodeURIComponent(this.getBankAccountMySuffixIdentifier(bankAccount))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IBankAccountMySuffix> {
    return this.http
      .get<RestBankAccountMySuffix>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IBankAccountMySuffix[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBankAccountMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getBankAccountMySuffixIdentifier(bankAccount: Pick<IBankAccountMySuffix, 'id'>): number {
    return bankAccount.id;
  }

  compareBankAccountMySuffix(o1: Pick<IBankAccountMySuffix, 'id'> | null, o2: Pick<IBankAccountMySuffix, 'id'> | null): boolean {
    return o1 && o2 ? this.getBankAccountMySuffixIdentifier(o1) === this.getBankAccountMySuffixIdentifier(o2) : o1 === o2;
  }

  addBankAccountMySuffixToCollectionIfMissing<Type extends Pick<IBankAccountMySuffix, 'id'>>(
    bankAccountCollection: Type[],
    ...bankAccountsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bankAccounts: Type[] = bankAccountsToCheck.filter(bankAccountItem => bankAccountItem !== null && bankAccountItem !== undefined);
    if (bankAccounts.length > 0) {
      const bankAccountCollectionIdentifiers = bankAccountCollection.map(bankAccountItem =>
        this.getBankAccountMySuffixIdentifier(bankAccountItem),
      );
      const bankAccountsToAdd = bankAccounts.filter(bankAccountItem => {
        const bankAccountIdentifier = this.getBankAccountMySuffixIdentifier(bankAccountItem);
        if (bankAccountCollectionIdentifiers.includes(bankAccountIdentifier)) {
          return false;
        }
        bankAccountCollectionIdentifiers.push(bankAccountIdentifier);
        return true;
      });
      return [...bankAccountsToAdd, ...bankAccountCollection];
    }
    return bankAccountCollection;
  }

  protected convertValueFromClient<T extends IBankAccountMySuffix | NewBankAccountMySuffix | PartialUpdateBankAccountMySuffix>(
    bankAccount: T,
  ): RestOf<T> {
    return {
      ...bankAccount,
      openingDay: bankAccount.openingDay?.format(DATE_FORMAT) ?? null,
      lastOperationDate: bankAccount.lastOperationDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestBankAccountMySuffix): IBankAccountMySuffix {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestBankAccountMySuffix[]): IBankAccountMySuffix[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}

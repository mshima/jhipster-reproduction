import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBankAccountMySuffix } from '../bank-account-my-suffix.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../bank-account-my-suffix.test-samples';

import { BankAccountMySuffixService, RestBankAccountMySuffix } from './bank-account-my-suffix.service';

const requireRestSample: RestBankAccountMySuffix = {
  ...sampleWithRequiredData,
  openingDay: sampleWithRequiredData.openingDay?.format(DATE_FORMAT),
  lastOperationDate: sampleWithRequiredData.lastOperationDate?.toJSON(),
};

describe('BankAccountMySuffix Service', () => {
  let service: BankAccountMySuffixService;
  let httpMock: HttpTestingController;
  let expectedResult: IBankAccountMySuffix | IBankAccountMySuffix[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BankAccountMySuffixService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a BankAccountMySuffix', () => {
      const bankAccount = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bankAccount).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BankAccountMySuffix', () => {
      const bankAccount = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bankAccount).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BankAccountMySuffix', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BankAccountMySuffix', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BankAccountMySuffix', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    it('should handle exceptions for searching a BankAccountMySuffix', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBeNull();
    });

    describe('addBankAccountMySuffixToCollectionIfMissing', () => {
      it('should add a BankAccountMySuffix to an empty array', () => {
        const bankAccount: IBankAccountMySuffix = sampleWithRequiredData;
        expectedResult = service.addBankAccountMySuffixToCollectionIfMissing([], bankAccount);
        expect(expectedResult).toEqual([bankAccount]);
      });

      it('should not add a BankAccountMySuffix to an array that contains it', () => {
        const bankAccount: IBankAccountMySuffix = sampleWithRequiredData;
        const bankAccountCollection: IBankAccountMySuffix[] = [
          {
            ...bankAccount,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBankAccountMySuffixToCollectionIfMissing(bankAccountCollection, bankAccount);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BankAccountMySuffix to an array that doesn't contain it", () => {
        const bankAccount: IBankAccountMySuffix = sampleWithRequiredData;
        const bankAccountCollection: IBankAccountMySuffix[] = [sampleWithPartialData];
        expectedResult = service.addBankAccountMySuffixToCollectionIfMissing(bankAccountCollection, bankAccount);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bankAccount);
      });

      it('should add only unique BankAccountMySuffix to an array', () => {
        const bankAccountArray: IBankAccountMySuffix[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bankAccountCollection: IBankAccountMySuffix[] = [sampleWithRequiredData];
        expectedResult = service.addBankAccountMySuffixToCollectionIfMissing(bankAccountCollection, ...bankAccountArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bankAccount: IBankAccountMySuffix = sampleWithRequiredData;
        const bankAccount2: IBankAccountMySuffix = sampleWithPartialData;
        expectedResult = service.addBankAccountMySuffixToCollectionIfMissing([], bankAccount, bankAccount2);
        expect(expectedResult).toEqual([bankAccount, bankAccount2]);
      });

      it('should accept null and undefined values', () => {
        const bankAccount: IBankAccountMySuffix = sampleWithRequiredData;
        expectedResult = service.addBankAccountMySuffixToCollectionIfMissing([], null, bankAccount, undefined);
        expect(expectedResult).toEqual([bankAccount]);
      });

      it('should return initial array if no BankAccountMySuffix is added', () => {
        const bankAccountCollection: IBankAccountMySuffix[] = [sampleWithRequiredData];
        expectedResult = service.addBankAccountMySuffixToCollectionIfMissing(bankAccountCollection, undefined, null);
        expect(expectedResult).toEqual(bankAccountCollection);
      });
    });

    describe('compareBankAccountMySuffix', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBankAccountMySuffix(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 22720 };
        const entity2 = null;

        const compareResult1 = service.compareBankAccountMySuffix(entity1, entity2);
        const compareResult2 = service.compareBankAccountMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 22720 };
        const entity2 = { id: 22583 };

        const compareResult1 = service.compareBankAccountMySuffix(entity1, entity2);
        const compareResult2 = service.compareBankAccountMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 22720 };
        const entity2 = { id: 22720 };

        const compareResult1 = service.compareBankAccountMySuffix(entity1, entity2);
        const compareResult2 = service.compareBankAccountMySuffix(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

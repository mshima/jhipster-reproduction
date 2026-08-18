import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';

import { IFieldTestEnumWithValue } from '../field-test-enum-with-value.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../field-test-enum-with-value.test-samples';

import { FieldTestEnumWithValueService } from './field-test-enum-with-value.service';

const requireRestSample: IFieldTestEnumWithValue = {
  ...sampleWithRequiredData,
};

describe('FieldTestEnumWithValue Service', () => {
  let service: FieldTestEnumWithValueService;
  let httpMock: HttpTestingController;
  let expectedResult: IFieldTestEnumWithValue | IFieldTestEnumWithValue[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FieldTestEnumWithValueService);
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

    it('should create a FieldTestEnumWithValue', () => {
      const fieldTestEnumWithValue = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fieldTestEnumWithValue).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FieldTestEnumWithValue', () => {
      const fieldTestEnumWithValue = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fieldTestEnumWithValue).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FieldTestEnumWithValue', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FieldTestEnumWithValue', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FieldTestEnumWithValue', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addFieldTestEnumWithValueToCollectionIfMissing', () => {
      it('should add a FieldTestEnumWithValue to an empty array', () => {
        const fieldTestEnumWithValue: IFieldTestEnumWithValue = sampleWithRequiredData;
        expectedResult = service.addFieldTestEnumWithValueToCollectionIfMissing([], fieldTestEnumWithValue);
        expect(expectedResult).toEqual([fieldTestEnumWithValue]);
      });

      it('should not add a FieldTestEnumWithValue to an array that contains it', () => {
        const fieldTestEnumWithValue: IFieldTestEnumWithValue = sampleWithRequiredData;
        const fieldTestEnumWithValueCollection: IFieldTestEnumWithValue[] = [
          {
            ...fieldTestEnumWithValue,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFieldTestEnumWithValueToCollectionIfMissing(fieldTestEnumWithValueCollection, fieldTestEnumWithValue);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FieldTestEnumWithValue to an array that doesn't contain it", () => {
        const fieldTestEnumWithValue: IFieldTestEnumWithValue = sampleWithRequiredData;
        const fieldTestEnumWithValueCollection: IFieldTestEnumWithValue[] = [sampleWithPartialData];
        expectedResult = service.addFieldTestEnumWithValueToCollectionIfMissing(fieldTestEnumWithValueCollection, fieldTestEnumWithValue);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fieldTestEnumWithValue);
      });

      it('should add only unique FieldTestEnumWithValue to an array', () => {
        const fieldTestEnumWithValueArray: IFieldTestEnumWithValue[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fieldTestEnumWithValueCollection: IFieldTestEnumWithValue[] = [sampleWithRequiredData];
        expectedResult = service.addFieldTestEnumWithValueToCollectionIfMissing(
          fieldTestEnumWithValueCollection,
          ...fieldTestEnumWithValueArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fieldTestEnumWithValue: IFieldTestEnumWithValue = sampleWithRequiredData;
        const fieldTestEnumWithValue2: IFieldTestEnumWithValue = sampleWithPartialData;
        expectedResult = service.addFieldTestEnumWithValueToCollectionIfMissing([], fieldTestEnumWithValue, fieldTestEnumWithValue2);
        expect(expectedResult).toEqual([fieldTestEnumWithValue, fieldTestEnumWithValue2]);
      });

      it('should accept null and undefined values', () => {
        const fieldTestEnumWithValue: IFieldTestEnumWithValue = sampleWithRequiredData;
        expectedResult = service.addFieldTestEnumWithValueToCollectionIfMissing([], null, fieldTestEnumWithValue, undefined);
        expect(expectedResult).toEqual([fieldTestEnumWithValue]);
      });

      it('should return initial array if no FieldTestEnumWithValue is added', () => {
        const fieldTestEnumWithValueCollection: IFieldTestEnumWithValue[] = [sampleWithRequiredData];
        expectedResult = service.addFieldTestEnumWithValueToCollectionIfMissing(fieldTestEnumWithValueCollection, undefined, null);
        expect(expectedResult).toEqual(fieldTestEnumWithValueCollection);
      });
    });

    describe('compareFieldTestEnumWithValue', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFieldTestEnumWithValue(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6834 };
        const entity2 = null;

        const compareResult1 = service.compareFieldTestEnumWithValue(entity1, entity2);
        const compareResult2 = service.compareFieldTestEnumWithValue(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6834 };
        const entity2 = { id: 1054 };

        const compareResult1 = service.compareFieldTestEnumWithValue(entity1, entity2);
        const compareResult2 = service.compareFieldTestEnumWithValue(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 6834 };
        const entity2 = { id: 6834 };

        const compareResult1 = service.compareFieldTestEnumWithValue(entity1, entity2);
        const compareResult2 = service.compareFieldTestEnumWithValue(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

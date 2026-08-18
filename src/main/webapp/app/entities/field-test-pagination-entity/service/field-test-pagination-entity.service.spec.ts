import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFieldTestPaginationEntity } from '../field-test-pagination-entity.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../field-test-pagination-entity.test-samples';

import { FieldTestPaginationEntityService, RestFieldTestPaginationEntity } from './field-test-pagination-entity.service';

const requireRestSample: RestFieldTestPaginationEntity = {
  ...sampleWithRequiredData,
  localDateAlice: sampleWithRequiredData.localDateAlice?.format(DATE_FORMAT),
  localDateRequiredAlice: sampleWithRequiredData.localDateRequiredAlice?.format(DATE_FORMAT),
  instantAlice: sampleWithRequiredData.instantAlice?.toJSON(),
  instanteRequiredAlice: sampleWithRequiredData.instanteRequiredAlice?.toJSON(),
  zonedDateTimeAlice: sampleWithRequiredData.zonedDateTimeAlice?.toJSON(),
  zonedDateTimeRequiredAlice: sampleWithRequiredData.zonedDateTimeRequiredAlice?.toJSON(),
};

describe('FieldTestPaginationEntity Service', () => {
  let service: FieldTestPaginationEntityService;
  let httpMock: HttpTestingController;
  let expectedResult: IFieldTestPaginationEntity | IFieldTestPaginationEntity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FieldTestPaginationEntityService);
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

    it('should create a FieldTestPaginationEntity', () => {
      const fieldTestPaginationEntity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fieldTestPaginationEntity).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FieldTestPaginationEntity', () => {
      const fieldTestPaginationEntity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fieldTestPaginationEntity).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FieldTestPaginationEntity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FieldTestPaginationEntity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FieldTestPaginationEntity', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addFieldTestPaginationEntityToCollectionIfMissing', () => {
      it('should add a FieldTestPaginationEntity to an empty array', () => {
        const fieldTestPaginationEntity: IFieldTestPaginationEntity = sampleWithRequiredData;
        expectedResult = service.addFieldTestPaginationEntityToCollectionIfMissing([], fieldTestPaginationEntity);
        expect(expectedResult).toEqual([fieldTestPaginationEntity]);
      });

      it('should not add a FieldTestPaginationEntity to an array that contains it', () => {
        const fieldTestPaginationEntity: IFieldTestPaginationEntity = sampleWithRequiredData;
        const fieldTestPaginationEntityCollection: IFieldTestPaginationEntity[] = [
          {
            ...fieldTestPaginationEntity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFieldTestPaginationEntityToCollectionIfMissing(
          fieldTestPaginationEntityCollection,
          fieldTestPaginationEntity,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FieldTestPaginationEntity to an array that doesn't contain it", () => {
        const fieldTestPaginationEntity: IFieldTestPaginationEntity = sampleWithRequiredData;
        const fieldTestPaginationEntityCollection: IFieldTestPaginationEntity[] = [sampleWithPartialData];
        expectedResult = service.addFieldTestPaginationEntityToCollectionIfMissing(
          fieldTestPaginationEntityCollection,
          fieldTestPaginationEntity,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fieldTestPaginationEntity);
      });

      it('should add only unique FieldTestPaginationEntity to an array', () => {
        const fieldTestPaginationEntityArray: IFieldTestPaginationEntity[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const fieldTestPaginationEntityCollection: IFieldTestPaginationEntity[] = [sampleWithRequiredData];
        expectedResult = service.addFieldTestPaginationEntityToCollectionIfMissing(
          fieldTestPaginationEntityCollection,
          ...fieldTestPaginationEntityArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fieldTestPaginationEntity: IFieldTestPaginationEntity = sampleWithRequiredData;
        const fieldTestPaginationEntity2: IFieldTestPaginationEntity = sampleWithPartialData;
        expectedResult = service.addFieldTestPaginationEntityToCollectionIfMissing(
          [],
          fieldTestPaginationEntity,
          fieldTestPaginationEntity2,
        );
        expect(expectedResult).toEqual([fieldTestPaginationEntity, fieldTestPaginationEntity2]);
      });

      it('should accept null and undefined values', () => {
        const fieldTestPaginationEntity: IFieldTestPaginationEntity = sampleWithRequiredData;
        expectedResult = service.addFieldTestPaginationEntityToCollectionIfMissing([], null, fieldTestPaginationEntity, undefined);
        expect(expectedResult).toEqual([fieldTestPaginationEntity]);
      });

      it('should return initial array if no FieldTestPaginationEntity is added', () => {
        const fieldTestPaginationEntityCollection: IFieldTestPaginationEntity[] = [sampleWithRequiredData];
        expectedResult = service.addFieldTestPaginationEntityToCollectionIfMissing(fieldTestPaginationEntityCollection, undefined, null);
        expect(expectedResult).toEqual(fieldTestPaginationEntityCollection);
      });
    });

    describe('compareFieldTestPaginationEntity', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFieldTestPaginationEntity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 16266 };
        const entity2 = null;

        const compareResult1 = service.compareFieldTestPaginationEntity(entity1, entity2);
        const compareResult2 = service.compareFieldTestPaginationEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 16266 };
        const entity2 = { id: 2959 };

        const compareResult1 = service.compareFieldTestPaginationEntity(entity1, entity2);
        const compareResult2 = service.compareFieldTestPaginationEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 16266 };
        const entity2 = { id: 16266 };

        const compareResult1 = service.compareFieldTestPaginationEntity(entity1, entity2);
        const compareResult2 = service.compareFieldTestPaginationEntity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

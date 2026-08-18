import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';

import { IEntityWithServiceClassAndPagination } from '../entity-with-service-class-and-pagination.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../entity-with-service-class-and-pagination.test-samples';

import { EntityWithServiceClassAndPaginationService } from './entity-with-service-class-and-pagination.service';

const requireRestSample: IEntityWithServiceClassAndPagination = {
  ...sampleWithRequiredData,
};

describe('EntityWithServiceClassAndPagination Service', () => {
  let service: EntityWithServiceClassAndPaginationService;
  let httpMock: HttpTestingController;
  let expectedResult: IEntityWithServiceClassAndPagination | IEntityWithServiceClassAndPagination[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EntityWithServiceClassAndPaginationService);
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

    it('should create a EntityWithServiceClassAndPagination', () => {
      const entityWithServiceClassAndPagination = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(entityWithServiceClassAndPagination).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EntityWithServiceClassAndPagination', () => {
      const entityWithServiceClassAndPagination = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(entityWithServiceClassAndPagination).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EntityWithServiceClassAndPagination', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EntityWithServiceClassAndPagination', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EntityWithServiceClassAndPagination', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addEntityWithServiceClassAndPaginationToCollectionIfMissing', () => {
      it('should add a EntityWithServiceClassAndPagination to an empty array', () => {
        const entityWithServiceClassAndPagination: IEntityWithServiceClassAndPagination = sampleWithRequiredData;
        expectedResult = service.addEntityWithServiceClassAndPaginationToCollectionIfMissing([], entityWithServiceClassAndPagination);
        expect(expectedResult).toEqual([entityWithServiceClassAndPagination]);
      });

      it('should not add a EntityWithServiceClassAndPagination to an array that contains it', () => {
        const entityWithServiceClassAndPagination: IEntityWithServiceClassAndPagination = sampleWithRequiredData;
        const entityWithServiceClassAndPaginationCollection: IEntityWithServiceClassAndPagination[] = [
          {
            ...entityWithServiceClassAndPagination,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEntityWithServiceClassAndPaginationToCollectionIfMissing(
          entityWithServiceClassAndPaginationCollection,
          entityWithServiceClassAndPagination,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EntityWithServiceClassAndPagination to an array that doesn't contain it", () => {
        const entityWithServiceClassAndPagination: IEntityWithServiceClassAndPagination = sampleWithRequiredData;
        const entityWithServiceClassAndPaginationCollection: IEntityWithServiceClassAndPagination[] = [sampleWithPartialData];
        expectedResult = service.addEntityWithServiceClassAndPaginationToCollectionIfMissing(
          entityWithServiceClassAndPaginationCollection,
          entityWithServiceClassAndPagination,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(entityWithServiceClassAndPagination);
      });

      it('should add only unique EntityWithServiceClassAndPagination to an array', () => {
        const entityWithServiceClassAndPaginationArray: IEntityWithServiceClassAndPagination[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const entityWithServiceClassAndPaginationCollection: IEntityWithServiceClassAndPagination[] = [sampleWithRequiredData];
        expectedResult = service.addEntityWithServiceClassAndPaginationToCollectionIfMissing(
          entityWithServiceClassAndPaginationCollection,
          ...entityWithServiceClassAndPaginationArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const entityWithServiceClassAndPagination: IEntityWithServiceClassAndPagination = sampleWithRequiredData;
        const entityWithServiceClassAndPagination2: IEntityWithServiceClassAndPagination = sampleWithPartialData;
        expectedResult = service.addEntityWithServiceClassAndPaginationToCollectionIfMissing(
          [],
          entityWithServiceClassAndPagination,
          entityWithServiceClassAndPagination2,
        );
        expect(expectedResult).toEqual([entityWithServiceClassAndPagination, entityWithServiceClassAndPagination2]);
      });

      it('should accept null and undefined values', () => {
        const entityWithServiceClassAndPagination: IEntityWithServiceClassAndPagination = sampleWithRequiredData;
        expectedResult = service.addEntityWithServiceClassAndPaginationToCollectionIfMissing(
          [],
          null,
          entityWithServiceClassAndPagination,
          undefined,
        );
        expect(expectedResult).toEqual([entityWithServiceClassAndPagination]);
      });

      it('should return initial array if no EntityWithServiceClassAndPagination is added', () => {
        const entityWithServiceClassAndPaginationCollection: IEntityWithServiceClassAndPagination[] = [sampleWithRequiredData];
        expectedResult = service.addEntityWithServiceClassAndPaginationToCollectionIfMissing(
          entityWithServiceClassAndPaginationCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(entityWithServiceClassAndPaginationCollection);
      });
    });

    describe('compareEntityWithServiceClassAndPagination', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEntityWithServiceClassAndPagination(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 11699 };
        const entity2 = null;

        const compareResult1 = service.compareEntityWithServiceClassAndPagination(entity1, entity2);
        const compareResult2 = service.compareEntityWithServiceClassAndPagination(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 11699 };
        const entity2 = { id: 16307 };

        const compareResult1 = service.compareEntityWithServiceClassAndPagination(entity1, entity2);
        const compareResult2 = service.compareEntityWithServiceClassAndPagination(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 11699 };
        const entity2 = { id: 11699 };

        const compareResult1 = service.compareEntityWithServiceClassAndPagination(entity1, entity2);
        const compareResult2 = service.compareEntityWithServiceClassAndPagination(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

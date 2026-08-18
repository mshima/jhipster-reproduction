import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';

import { IEntityWithServiceImplAndPagination } from '../entity-with-service-impl-and-pagination.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../entity-with-service-impl-and-pagination.test-samples';

import { EntityWithServiceImplAndPaginationService } from './entity-with-service-impl-and-pagination.service';

const requireRestSample: IEntityWithServiceImplAndPagination = {
  ...sampleWithRequiredData,
};

describe('EntityWithServiceImplAndPagination Service', () => {
  let service: EntityWithServiceImplAndPaginationService;
  let httpMock: HttpTestingController;
  let expectedResult: IEntityWithServiceImplAndPagination | IEntityWithServiceImplAndPagination[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EntityWithServiceImplAndPaginationService);
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

    it('should create a EntityWithServiceImplAndPagination', () => {
      const entityWithServiceImplAndPagination = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(entityWithServiceImplAndPagination).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EntityWithServiceImplAndPagination', () => {
      const entityWithServiceImplAndPagination = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(entityWithServiceImplAndPagination).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EntityWithServiceImplAndPagination', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EntityWithServiceImplAndPagination', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EntityWithServiceImplAndPagination', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addEntityWithServiceImplAndPaginationToCollectionIfMissing', () => {
      it('should add a EntityWithServiceImplAndPagination to an empty array', () => {
        const entityWithServiceImplAndPagination: IEntityWithServiceImplAndPagination = sampleWithRequiredData;
        expectedResult = service.addEntityWithServiceImplAndPaginationToCollectionIfMissing([], entityWithServiceImplAndPagination);
        expect(expectedResult).toEqual([entityWithServiceImplAndPagination]);
      });

      it('should not add a EntityWithServiceImplAndPagination to an array that contains it', () => {
        const entityWithServiceImplAndPagination: IEntityWithServiceImplAndPagination = sampleWithRequiredData;
        const entityWithServiceImplAndPaginationCollection: IEntityWithServiceImplAndPagination[] = [
          {
            ...entityWithServiceImplAndPagination,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEntityWithServiceImplAndPaginationToCollectionIfMissing(
          entityWithServiceImplAndPaginationCollection,
          entityWithServiceImplAndPagination,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EntityWithServiceImplAndPagination to an array that doesn't contain it", () => {
        const entityWithServiceImplAndPagination: IEntityWithServiceImplAndPagination = sampleWithRequiredData;
        const entityWithServiceImplAndPaginationCollection: IEntityWithServiceImplAndPagination[] = [sampleWithPartialData];
        expectedResult = service.addEntityWithServiceImplAndPaginationToCollectionIfMissing(
          entityWithServiceImplAndPaginationCollection,
          entityWithServiceImplAndPagination,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(entityWithServiceImplAndPagination);
      });

      it('should add only unique EntityWithServiceImplAndPagination to an array', () => {
        const entityWithServiceImplAndPaginationArray: IEntityWithServiceImplAndPagination[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const entityWithServiceImplAndPaginationCollection: IEntityWithServiceImplAndPagination[] = [sampleWithRequiredData];
        expectedResult = service.addEntityWithServiceImplAndPaginationToCollectionIfMissing(
          entityWithServiceImplAndPaginationCollection,
          ...entityWithServiceImplAndPaginationArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const entityWithServiceImplAndPagination: IEntityWithServiceImplAndPagination = sampleWithRequiredData;
        const entityWithServiceImplAndPagination2: IEntityWithServiceImplAndPagination = sampleWithPartialData;
        expectedResult = service.addEntityWithServiceImplAndPaginationToCollectionIfMissing(
          [],
          entityWithServiceImplAndPagination,
          entityWithServiceImplAndPagination2,
        );
        expect(expectedResult).toEqual([entityWithServiceImplAndPagination, entityWithServiceImplAndPagination2]);
      });

      it('should accept null and undefined values', () => {
        const entityWithServiceImplAndPagination: IEntityWithServiceImplAndPagination = sampleWithRequiredData;
        expectedResult = service.addEntityWithServiceImplAndPaginationToCollectionIfMissing(
          [],
          null,
          entityWithServiceImplAndPagination,
          undefined,
        );
        expect(expectedResult).toEqual([entityWithServiceImplAndPagination]);
      });

      it('should return initial array if no EntityWithServiceImplAndPagination is added', () => {
        const entityWithServiceImplAndPaginationCollection: IEntityWithServiceImplAndPagination[] = [sampleWithRequiredData];
        expectedResult = service.addEntityWithServiceImplAndPaginationToCollectionIfMissing(
          entityWithServiceImplAndPaginationCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(entityWithServiceImplAndPaginationCollection);
      });
    });

    describe('compareEntityWithServiceImplAndPagination', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEntityWithServiceImplAndPagination(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 27409 };
        const entity2 = null;

        const compareResult1 = service.compareEntityWithServiceImplAndPagination(entity1, entity2);
        const compareResult2 = service.compareEntityWithServiceImplAndPagination(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 27409 };
        const entity2 = { id: 3861 };

        const compareResult1 = service.compareEntityWithServiceImplAndPagination(entity1, entity2);
        const compareResult2 = service.compareEntityWithServiceImplAndPagination(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 27409 };
        const entity2 = { id: 27409 };

        const compareResult1 = service.compareEntityWithServiceImplAndPagination(entity1, entity2);
        const compareResult2 = service.compareEntityWithServiceImplAndPagination(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

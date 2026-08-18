import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';

import { IEntityWithServiceImplPaginationAndDTO } from '../entity-with-service-impl-pagination-and-dto.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../entity-with-service-impl-pagination-and-dto.test-samples';

import { EntityWithServiceImplPaginationAndDTOService } from './entity-with-service-impl-pagination-and-dto.service';

const requireRestSample: IEntityWithServiceImplPaginationAndDTO = {
  ...sampleWithRequiredData,
};

describe('EntityWithServiceImplPaginationAndDTO Service', () => {
  let service: EntityWithServiceImplPaginationAndDTOService;
  let httpMock: HttpTestingController;
  let expectedResult: IEntityWithServiceImplPaginationAndDTO | IEntityWithServiceImplPaginationAndDTO[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EntityWithServiceImplPaginationAndDTOService);
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

    it('should create a EntityWithServiceImplPaginationAndDTO', () => {
      const entityWithServiceImplPaginationAndDTO = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(entityWithServiceImplPaginationAndDTO).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EntityWithServiceImplPaginationAndDTO', () => {
      const entityWithServiceImplPaginationAndDTO = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(entityWithServiceImplPaginationAndDTO).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EntityWithServiceImplPaginationAndDTO', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EntityWithServiceImplPaginationAndDTO', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EntityWithServiceImplPaginationAndDTO', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addEntityWithServiceImplPaginationAndDTOToCollectionIfMissing', () => {
      it('should add a EntityWithServiceImplPaginationAndDTO to an empty array', () => {
        const entityWithServiceImplPaginationAndDTO: IEntityWithServiceImplPaginationAndDTO = sampleWithRequiredData;
        expectedResult = service.addEntityWithServiceImplPaginationAndDTOToCollectionIfMissing([], entityWithServiceImplPaginationAndDTO);
        expect(expectedResult).toEqual([entityWithServiceImplPaginationAndDTO]);
      });

      it('should not add a EntityWithServiceImplPaginationAndDTO to an array that contains it', () => {
        const entityWithServiceImplPaginationAndDTO: IEntityWithServiceImplPaginationAndDTO = sampleWithRequiredData;
        const entityWithServiceImplPaginationAndDTOCollection: IEntityWithServiceImplPaginationAndDTO[] = [
          {
            ...entityWithServiceImplPaginationAndDTO,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEntityWithServiceImplPaginationAndDTOToCollectionIfMissing(
          entityWithServiceImplPaginationAndDTOCollection,
          entityWithServiceImplPaginationAndDTO,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EntityWithServiceImplPaginationAndDTO to an array that doesn't contain it", () => {
        const entityWithServiceImplPaginationAndDTO: IEntityWithServiceImplPaginationAndDTO = sampleWithRequiredData;
        const entityWithServiceImplPaginationAndDTOCollection: IEntityWithServiceImplPaginationAndDTO[] = [sampleWithPartialData];
        expectedResult = service.addEntityWithServiceImplPaginationAndDTOToCollectionIfMissing(
          entityWithServiceImplPaginationAndDTOCollection,
          entityWithServiceImplPaginationAndDTO,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(entityWithServiceImplPaginationAndDTO);
      });

      it('should add only unique EntityWithServiceImplPaginationAndDTO to an array', () => {
        const entityWithServiceImplPaginationAndDTOArray: IEntityWithServiceImplPaginationAndDTO[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const entityWithServiceImplPaginationAndDTOCollection: IEntityWithServiceImplPaginationAndDTO[] = [sampleWithRequiredData];
        expectedResult = service.addEntityWithServiceImplPaginationAndDTOToCollectionIfMissing(
          entityWithServiceImplPaginationAndDTOCollection,
          ...entityWithServiceImplPaginationAndDTOArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const entityWithServiceImplPaginationAndDTO: IEntityWithServiceImplPaginationAndDTO = sampleWithRequiredData;
        const entityWithServiceImplPaginationAndDTO2: IEntityWithServiceImplPaginationAndDTO = sampleWithPartialData;
        expectedResult = service.addEntityWithServiceImplPaginationAndDTOToCollectionIfMissing(
          [],
          entityWithServiceImplPaginationAndDTO,
          entityWithServiceImplPaginationAndDTO2,
        );
        expect(expectedResult).toEqual([entityWithServiceImplPaginationAndDTO, entityWithServiceImplPaginationAndDTO2]);
      });

      it('should accept null and undefined values', () => {
        const entityWithServiceImplPaginationAndDTO: IEntityWithServiceImplPaginationAndDTO = sampleWithRequiredData;
        expectedResult = service.addEntityWithServiceImplPaginationAndDTOToCollectionIfMissing(
          [],
          null,
          entityWithServiceImplPaginationAndDTO,
          undefined,
        );
        expect(expectedResult).toEqual([entityWithServiceImplPaginationAndDTO]);
      });

      it('should return initial array if no EntityWithServiceImplPaginationAndDTO is added', () => {
        const entityWithServiceImplPaginationAndDTOCollection: IEntityWithServiceImplPaginationAndDTO[] = [sampleWithRequiredData];
        expectedResult = service.addEntityWithServiceImplPaginationAndDTOToCollectionIfMissing(
          entityWithServiceImplPaginationAndDTOCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(entityWithServiceImplPaginationAndDTOCollection);
      });
    });

    describe('compareEntityWithServiceImplPaginationAndDTO', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEntityWithServiceImplPaginationAndDTO(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 19834 };
        const entity2 = null;

        const compareResult1 = service.compareEntityWithServiceImplPaginationAndDTO(entity1, entity2);
        const compareResult2 = service.compareEntityWithServiceImplPaginationAndDTO(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 19834 };
        const entity2 = { id: 11915 };

        const compareResult1 = service.compareEntityWithServiceImplPaginationAndDTO(entity1, entity2);
        const compareResult2 = service.compareEntityWithServiceImplPaginationAndDTO(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 19834 };
        const entity2 = { id: 19834 };

        const compareResult1 = service.compareEntityWithServiceImplPaginationAndDTO(entity1, entity2);
        const compareResult2 = service.compareEntityWithServiceImplPaginationAndDTO(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

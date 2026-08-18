import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';

import { IEntityWithServiceImplAndDTO } from '../entity-with-service-impl-and-dto.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../entity-with-service-impl-and-dto.test-samples';

import { EntityWithServiceImplAndDTOService } from './entity-with-service-impl-and-dto.service';

const requireRestSample: IEntityWithServiceImplAndDTO = {
  ...sampleWithRequiredData,
};

describe('EntityWithServiceImplAndDTO Service', () => {
  let service: EntityWithServiceImplAndDTOService;
  let httpMock: HttpTestingController;
  let expectedResult: IEntityWithServiceImplAndDTO | IEntityWithServiceImplAndDTO[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EntityWithServiceImplAndDTOService);
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

    it('should create a EntityWithServiceImplAndDTO', () => {
      const entityWithServiceImplAndDTO = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(entityWithServiceImplAndDTO).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EntityWithServiceImplAndDTO', () => {
      const entityWithServiceImplAndDTO = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(entityWithServiceImplAndDTO).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EntityWithServiceImplAndDTO', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EntityWithServiceImplAndDTO', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EntityWithServiceImplAndDTO', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addEntityWithServiceImplAndDTOToCollectionIfMissing', () => {
      it('should add a EntityWithServiceImplAndDTO to an empty array', () => {
        const entityWithServiceImplAndDTO: IEntityWithServiceImplAndDTO = sampleWithRequiredData;
        expectedResult = service.addEntityWithServiceImplAndDTOToCollectionIfMissing([], entityWithServiceImplAndDTO);
        expect(expectedResult).toEqual([entityWithServiceImplAndDTO]);
      });

      it('should not add a EntityWithServiceImplAndDTO to an array that contains it', () => {
        const entityWithServiceImplAndDTO: IEntityWithServiceImplAndDTO = sampleWithRequiredData;
        const entityWithServiceImplAndDTOCollection: IEntityWithServiceImplAndDTO[] = [
          {
            ...entityWithServiceImplAndDTO,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEntityWithServiceImplAndDTOToCollectionIfMissing(
          entityWithServiceImplAndDTOCollection,
          entityWithServiceImplAndDTO,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EntityWithServiceImplAndDTO to an array that doesn't contain it", () => {
        const entityWithServiceImplAndDTO: IEntityWithServiceImplAndDTO = sampleWithRequiredData;
        const entityWithServiceImplAndDTOCollection: IEntityWithServiceImplAndDTO[] = [sampleWithPartialData];
        expectedResult = service.addEntityWithServiceImplAndDTOToCollectionIfMissing(
          entityWithServiceImplAndDTOCollection,
          entityWithServiceImplAndDTO,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(entityWithServiceImplAndDTO);
      });

      it('should add only unique EntityWithServiceImplAndDTO to an array', () => {
        const entityWithServiceImplAndDTOArray: IEntityWithServiceImplAndDTO[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const entityWithServiceImplAndDTOCollection: IEntityWithServiceImplAndDTO[] = [sampleWithRequiredData];
        expectedResult = service.addEntityWithServiceImplAndDTOToCollectionIfMissing(
          entityWithServiceImplAndDTOCollection,
          ...entityWithServiceImplAndDTOArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const entityWithServiceImplAndDTO: IEntityWithServiceImplAndDTO = sampleWithRequiredData;
        const entityWithServiceImplAndDTO2: IEntityWithServiceImplAndDTO = sampleWithPartialData;
        expectedResult = service.addEntityWithServiceImplAndDTOToCollectionIfMissing(
          [],
          entityWithServiceImplAndDTO,
          entityWithServiceImplAndDTO2,
        );
        expect(expectedResult).toEqual([entityWithServiceImplAndDTO, entityWithServiceImplAndDTO2]);
      });

      it('should accept null and undefined values', () => {
        const entityWithServiceImplAndDTO: IEntityWithServiceImplAndDTO = sampleWithRequiredData;
        expectedResult = service.addEntityWithServiceImplAndDTOToCollectionIfMissing([], null, entityWithServiceImplAndDTO, undefined);
        expect(expectedResult).toEqual([entityWithServiceImplAndDTO]);
      });

      it('should return initial array if no EntityWithServiceImplAndDTO is added', () => {
        const entityWithServiceImplAndDTOCollection: IEntityWithServiceImplAndDTO[] = [sampleWithRequiredData];
        expectedResult = service.addEntityWithServiceImplAndDTOToCollectionIfMissing(
          entityWithServiceImplAndDTOCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(entityWithServiceImplAndDTOCollection);
      });
    });

    describe('compareEntityWithServiceImplAndDTO', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEntityWithServiceImplAndDTO(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 30188 };
        const entity2 = null;

        const compareResult1 = service.compareEntityWithServiceImplAndDTO(entity1, entity2);
        const compareResult2 = service.compareEntityWithServiceImplAndDTO(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 30188 };
        const entity2 = { id: 772 };

        const compareResult1 = service.compareEntityWithServiceImplAndDTO(entity1, entity2);
        const compareResult2 = service.compareEntityWithServiceImplAndDTO(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 30188 };
        const entity2 = { id: 30188 };

        const compareResult1 = service.compareEntityWithServiceImplAndDTO(entity1, entity2);
        const compareResult2 = service.compareEntityWithServiceImplAndDTO(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

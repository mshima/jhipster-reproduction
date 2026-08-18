import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';

import { IEntityWithDTO } from '../entity-with-dto.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../entity-with-dto.test-samples';

import { EntityWithDTOService } from './entity-with-dto.service';

const requireRestSample: IEntityWithDTO = {
  ...sampleWithRequiredData,
};

describe('EntityWithDTO Service', () => {
  let service: EntityWithDTOService;
  let httpMock: HttpTestingController;
  let expectedResult: IEntityWithDTO | IEntityWithDTO[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EntityWithDTOService);
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

    it('should create a EntityWithDTO', () => {
      const entityWithDTO = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(entityWithDTO).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EntityWithDTO', () => {
      const entityWithDTO = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(entityWithDTO).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EntityWithDTO', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EntityWithDTO', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EntityWithDTO', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addEntityWithDTOToCollectionIfMissing', () => {
      it('should add a EntityWithDTO to an empty array', () => {
        const entityWithDTO: IEntityWithDTO = sampleWithRequiredData;
        expectedResult = service.addEntityWithDTOToCollectionIfMissing([], entityWithDTO);
        expect(expectedResult).toEqual([entityWithDTO]);
      });

      it('should not add a EntityWithDTO to an array that contains it', () => {
        const entityWithDTO: IEntityWithDTO = sampleWithRequiredData;
        const entityWithDTOCollection: IEntityWithDTO[] = [
          {
            ...entityWithDTO,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEntityWithDTOToCollectionIfMissing(entityWithDTOCollection, entityWithDTO);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EntityWithDTO to an array that doesn't contain it", () => {
        const entityWithDTO: IEntityWithDTO = sampleWithRequiredData;
        const entityWithDTOCollection: IEntityWithDTO[] = [sampleWithPartialData];
        expectedResult = service.addEntityWithDTOToCollectionIfMissing(entityWithDTOCollection, entityWithDTO);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(entityWithDTO);
      });

      it('should add only unique EntityWithDTO to an array', () => {
        const entityWithDTOArray: IEntityWithDTO[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const entityWithDTOCollection: IEntityWithDTO[] = [sampleWithRequiredData];
        expectedResult = service.addEntityWithDTOToCollectionIfMissing(entityWithDTOCollection, ...entityWithDTOArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const entityWithDTO: IEntityWithDTO = sampleWithRequiredData;
        const entityWithDTO2: IEntityWithDTO = sampleWithPartialData;
        expectedResult = service.addEntityWithDTOToCollectionIfMissing([], entityWithDTO, entityWithDTO2);
        expect(expectedResult).toEqual([entityWithDTO, entityWithDTO2]);
      });

      it('should accept null and undefined values', () => {
        const entityWithDTO: IEntityWithDTO = sampleWithRequiredData;
        expectedResult = service.addEntityWithDTOToCollectionIfMissing([], null, entityWithDTO, undefined);
        expect(expectedResult).toEqual([entityWithDTO]);
      });

      it('should return initial array if no EntityWithDTO is added', () => {
        const entityWithDTOCollection: IEntityWithDTO[] = [sampleWithRequiredData];
        expectedResult = service.addEntityWithDTOToCollectionIfMissing(entityWithDTOCollection, undefined, null);
        expect(expectedResult).toEqual(entityWithDTOCollection);
      });
    });

    describe('compareEntityWithDTO', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEntityWithDTO(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 13981 };
        const entity2 = null;

        const compareResult1 = service.compareEntityWithDTO(entity1, entity2);
        const compareResult2 = service.compareEntityWithDTO(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 13981 };
        const entity2 = { id: 29009 };

        const compareResult1 = service.compareEntityWithDTO(entity1, entity2);
        const compareResult2 = service.compareEntityWithDTO(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 13981 };
        const entity2 = { id: 13981 };

        const compareResult1 = service.compareEntityWithDTO(entity1, entity2);
        const compareResult2 = service.compareEntityWithDTO(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

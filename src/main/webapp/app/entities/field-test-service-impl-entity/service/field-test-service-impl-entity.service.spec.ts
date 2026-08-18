import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFieldTestServiceImplEntity } from '../field-test-service-impl-entity.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../field-test-service-impl-entity.test-samples';

import { FieldTestServiceImplEntityService, RestFieldTestServiceImplEntity } from './field-test-service-impl-entity.service';

const requireRestSample: RestFieldTestServiceImplEntity = {
  ...sampleWithRequiredData,
  localDateMika: sampleWithRequiredData.localDateMika?.format(DATE_FORMAT),
  localDateRequiredMika: sampleWithRequiredData.localDateRequiredMika?.format(DATE_FORMAT),
  instantMika: sampleWithRequiredData.instantMika?.toJSON(),
  instanteRequiredMika: sampleWithRequiredData.instanteRequiredMika?.toJSON(),
  zonedDateTimeMika: sampleWithRequiredData.zonedDateTimeMika?.toJSON(),
  zonedDateTimeRequiredMika: sampleWithRequiredData.zonedDateTimeRequiredMika?.toJSON(),
};

describe('FieldTestServiceImplEntity Service', () => {
  let service: FieldTestServiceImplEntityService;
  let httpMock: HttpTestingController;
  let expectedResult: IFieldTestServiceImplEntity | IFieldTestServiceImplEntity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FieldTestServiceImplEntityService);
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

    it('should create a FieldTestServiceImplEntity', () => {
      const fieldTestServiceImplEntity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fieldTestServiceImplEntity).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FieldTestServiceImplEntity', () => {
      const fieldTestServiceImplEntity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fieldTestServiceImplEntity).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FieldTestServiceImplEntity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FieldTestServiceImplEntity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FieldTestServiceImplEntity', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addFieldTestServiceImplEntityToCollectionIfMissing', () => {
      it('should add a FieldTestServiceImplEntity to an empty array', () => {
        const fieldTestServiceImplEntity: IFieldTestServiceImplEntity = sampleWithRequiredData;
        expectedResult = service.addFieldTestServiceImplEntityToCollectionIfMissing([], fieldTestServiceImplEntity);
        expect(expectedResult).toEqual([fieldTestServiceImplEntity]);
      });

      it('should not add a FieldTestServiceImplEntity to an array that contains it', () => {
        const fieldTestServiceImplEntity: IFieldTestServiceImplEntity = sampleWithRequiredData;
        const fieldTestServiceImplEntityCollection: IFieldTestServiceImplEntity[] = [
          {
            ...fieldTestServiceImplEntity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFieldTestServiceImplEntityToCollectionIfMissing(
          fieldTestServiceImplEntityCollection,
          fieldTestServiceImplEntity,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FieldTestServiceImplEntity to an array that doesn't contain it", () => {
        const fieldTestServiceImplEntity: IFieldTestServiceImplEntity = sampleWithRequiredData;
        const fieldTestServiceImplEntityCollection: IFieldTestServiceImplEntity[] = [sampleWithPartialData];
        expectedResult = service.addFieldTestServiceImplEntityToCollectionIfMissing(
          fieldTestServiceImplEntityCollection,
          fieldTestServiceImplEntity,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fieldTestServiceImplEntity);
      });

      it('should add only unique FieldTestServiceImplEntity to an array', () => {
        const fieldTestServiceImplEntityArray: IFieldTestServiceImplEntity[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const fieldTestServiceImplEntityCollection: IFieldTestServiceImplEntity[] = [sampleWithRequiredData];
        expectedResult = service.addFieldTestServiceImplEntityToCollectionIfMissing(
          fieldTestServiceImplEntityCollection,
          ...fieldTestServiceImplEntityArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fieldTestServiceImplEntity: IFieldTestServiceImplEntity = sampleWithRequiredData;
        const fieldTestServiceImplEntity2: IFieldTestServiceImplEntity = sampleWithPartialData;
        expectedResult = service.addFieldTestServiceImplEntityToCollectionIfMissing(
          [],
          fieldTestServiceImplEntity,
          fieldTestServiceImplEntity2,
        );
        expect(expectedResult).toEqual([fieldTestServiceImplEntity, fieldTestServiceImplEntity2]);
      });

      it('should accept null and undefined values', () => {
        const fieldTestServiceImplEntity: IFieldTestServiceImplEntity = sampleWithRequiredData;
        expectedResult = service.addFieldTestServiceImplEntityToCollectionIfMissing([], null, fieldTestServiceImplEntity, undefined);
        expect(expectedResult).toEqual([fieldTestServiceImplEntity]);
      });

      it('should return initial array if no FieldTestServiceImplEntity is added', () => {
        const fieldTestServiceImplEntityCollection: IFieldTestServiceImplEntity[] = [sampleWithRequiredData];
        expectedResult = service.addFieldTestServiceImplEntityToCollectionIfMissing(fieldTestServiceImplEntityCollection, undefined, null);
        expect(expectedResult).toEqual(fieldTestServiceImplEntityCollection);
      });
    });

    describe('compareFieldTestServiceImplEntity', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFieldTestServiceImplEntity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 20444 };
        const entity2 = null;

        const compareResult1 = service.compareFieldTestServiceImplEntity(entity1, entity2);
        const compareResult2 = service.compareFieldTestServiceImplEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 20444 };
        const entity2 = { id: 7459 };

        const compareResult1 = service.compareFieldTestServiceImplEntity(entity1, entity2);
        const compareResult2 = service.compareFieldTestServiceImplEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 20444 };
        const entity2 = { id: 20444 };

        const compareResult1 = service.compareFieldTestServiceImplEntity(entity1, entity2);
        const compareResult2 = service.compareFieldTestServiceImplEntity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

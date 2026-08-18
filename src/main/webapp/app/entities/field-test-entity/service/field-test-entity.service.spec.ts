import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFieldTestEntity } from '../field-test-entity.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../field-test-entity.test-samples';

import { FieldTestEntityService, RestFieldTestEntity } from './field-test-entity.service';

const requireRestSample: RestFieldTestEntity = {
  ...sampleWithRequiredData,
  localDateTom: sampleWithRequiredData.localDateTom?.format(DATE_FORMAT),
  localDateRequiredTom: sampleWithRequiredData.localDateRequiredTom?.format(DATE_FORMAT),
  instantTom: sampleWithRequiredData.instantTom?.toJSON(),
  instantRequiredTom: sampleWithRequiredData.instantRequiredTom?.toJSON(),
  zonedDateTimeTom: sampleWithRequiredData.zonedDateTimeTom?.toJSON(),
  zonedDateTimeRequiredTom: sampleWithRequiredData.zonedDateTimeRequiredTom?.toJSON(),
};

describe('FieldTestEntity Service', () => {
  let service: FieldTestEntityService;
  let httpMock: HttpTestingController;
  let expectedResult: IFieldTestEntity | IFieldTestEntity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FieldTestEntityService);
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

    it('should create a FieldTestEntity', () => {
      const fieldTestEntity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fieldTestEntity).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FieldTestEntity', () => {
      const fieldTestEntity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fieldTestEntity).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FieldTestEntity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FieldTestEntity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FieldTestEntity', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addFieldTestEntityToCollectionIfMissing', () => {
      it('should add a FieldTestEntity to an empty array', () => {
        const fieldTestEntity: IFieldTestEntity = sampleWithRequiredData;
        expectedResult = service.addFieldTestEntityToCollectionIfMissing([], fieldTestEntity);
        expect(expectedResult).toEqual([fieldTestEntity]);
      });

      it('should not add a FieldTestEntity to an array that contains it', () => {
        const fieldTestEntity: IFieldTestEntity = sampleWithRequiredData;
        const fieldTestEntityCollection: IFieldTestEntity[] = [
          {
            ...fieldTestEntity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFieldTestEntityToCollectionIfMissing(fieldTestEntityCollection, fieldTestEntity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FieldTestEntity to an array that doesn't contain it", () => {
        const fieldTestEntity: IFieldTestEntity = sampleWithRequiredData;
        const fieldTestEntityCollection: IFieldTestEntity[] = [sampleWithPartialData];
        expectedResult = service.addFieldTestEntityToCollectionIfMissing(fieldTestEntityCollection, fieldTestEntity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fieldTestEntity);
      });

      it('should add only unique FieldTestEntity to an array', () => {
        const fieldTestEntityArray: IFieldTestEntity[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fieldTestEntityCollection: IFieldTestEntity[] = [sampleWithRequiredData];
        expectedResult = service.addFieldTestEntityToCollectionIfMissing(fieldTestEntityCollection, ...fieldTestEntityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fieldTestEntity: IFieldTestEntity = sampleWithRequiredData;
        const fieldTestEntity2: IFieldTestEntity = sampleWithPartialData;
        expectedResult = service.addFieldTestEntityToCollectionIfMissing([], fieldTestEntity, fieldTestEntity2);
        expect(expectedResult).toEqual([fieldTestEntity, fieldTestEntity2]);
      });

      it('should accept null and undefined values', () => {
        const fieldTestEntity: IFieldTestEntity = sampleWithRequiredData;
        expectedResult = service.addFieldTestEntityToCollectionIfMissing([], null, fieldTestEntity, undefined);
        expect(expectedResult).toEqual([fieldTestEntity]);
      });

      it('should return initial array if no FieldTestEntity is added', () => {
        const fieldTestEntityCollection: IFieldTestEntity[] = [sampleWithRequiredData];
        expectedResult = service.addFieldTestEntityToCollectionIfMissing(fieldTestEntityCollection, undefined, null);
        expect(expectedResult).toEqual(fieldTestEntityCollection);
      });
    });

    describe('compareFieldTestEntity', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFieldTestEntity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7565 };
        const entity2 = null;

        const compareResult1 = service.compareFieldTestEntity(entity1, entity2);
        const compareResult2 = service.compareFieldTestEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7565 };
        const entity2 = { id: 23260 };

        const compareResult1 = service.compareFieldTestEntity(entity1, entity2);
        const compareResult2 = service.compareFieldTestEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 7565 };
        const entity2 = { id: 7565 };

        const compareResult1 = service.compareFieldTestEntity(entity1, entity2);
        const compareResult2 = service.compareFieldTestEntity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

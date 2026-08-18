import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFieldTestInfiniteScrollEntity } from '../field-test-infinite-scroll-entity.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../field-test-infinite-scroll-entity.test-samples';

import { FieldTestInfiniteScrollEntityService, RestFieldTestInfiniteScrollEntity } from './field-test-infinite-scroll-entity.service';

const requireRestSample: RestFieldTestInfiniteScrollEntity = {
  ...sampleWithRequiredData,
  localDateHugo: sampleWithRequiredData.localDateHugo?.format(DATE_FORMAT),
  localDateRequiredHugo: sampleWithRequiredData.localDateRequiredHugo?.format(DATE_FORMAT),
  instantHugo: sampleWithRequiredData.instantHugo?.toJSON(),
  instanteRequiredHugo: sampleWithRequiredData.instanteRequiredHugo?.toJSON(),
  zonedDateTimeHugo: sampleWithRequiredData.zonedDateTimeHugo?.toJSON(),
  zonedDateTimeRequiredHugo: sampleWithRequiredData.zonedDateTimeRequiredHugo?.toJSON(),
};

describe('FieldTestInfiniteScrollEntity Service', () => {
  let service: FieldTestInfiniteScrollEntityService;
  let httpMock: HttpTestingController;
  let expectedResult: IFieldTestInfiniteScrollEntity | IFieldTestInfiniteScrollEntity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FieldTestInfiniteScrollEntityService);
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

    it('should create a FieldTestInfiniteScrollEntity', () => {
      const fieldTestInfiniteScrollEntity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fieldTestInfiniteScrollEntity).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FieldTestInfiniteScrollEntity', () => {
      const fieldTestInfiniteScrollEntity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fieldTestInfiniteScrollEntity).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FieldTestInfiniteScrollEntity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FieldTestInfiniteScrollEntity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FieldTestInfiniteScrollEntity', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addFieldTestInfiniteScrollEntityToCollectionIfMissing', () => {
      it('should add a FieldTestInfiniteScrollEntity to an empty array', () => {
        const fieldTestInfiniteScrollEntity: IFieldTestInfiniteScrollEntity = sampleWithRequiredData;
        expectedResult = service.addFieldTestInfiniteScrollEntityToCollectionIfMissing([], fieldTestInfiniteScrollEntity);
        expect(expectedResult).toEqual([fieldTestInfiniteScrollEntity]);
      });

      it('should not add a FieldTestInfiniteScrollEntity to an array that contains it', () => {
        const fieldTestInfiniteScrollEntity: IFieldTestInfiniteScrollEntity = sampleWithRequiredData;
        const fieldTestInfiniteScrollEntityCollection: IFieldTestInfiniteScrollEntity[] = [
          {
            ...fieldTestInfiniteScrollEntity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFieldTestInfiniteScrollEntityToCollectionIfMissing(
          fieldTestInfiniteScrollEntityCollection,
          fieldTestInfiniteScrollEntity,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FieldTestInfiniteScrollEntity to an array that doesn't contain it", () => {
        const fieldTestInfiniteScrollEntity: IFieldTestInfiniteScrollEntity = sampleWithRequiredData;
        const fieldTestInfiniteScrollEntityCollection: IFieldTestInfiniteScrollEntity[] = [sampleWithPartialData];
        expectedResult = service.addFieldTestInfiniteScrollEntityToCollectionIfMissing(
          fieldTestInfiniteScrollEntityCollection,
          fieldTestInfiniteScrollEntity,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fieldTestInfiniteScrollEntity);
      });

      it('should add only unique FieldTestInfiniteScrollEntity to an array', () => {
        const fieldTestInfiniteScrollEntityArray: IFieldTestInfiniteScrollEntity[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const fieldTestInfiniteScrollEntityCollection: IFieldTestInfiniteScrollEntity[] = [sampleWithRequiredData];
        expectedResult = service.addFieldTestInfiniteScrollEntityToCollectionIfMissing(
          fieldTestInfiniteScrollEntityCollection,
          ...fieldTestInfiniteScrollEntityArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fieldTestInfiniteScrollEntity: IFieldTestInfiniteScrollEntity = sampleWithRequiredData;
        const fieldTestInfiniteScrollEntity2: IFieldTestInfiniteScrollEntity = sampleWithPartialData;
        expectedResult = service.addFieldTestInfiniteScrollEntityToCollectionIfMissing(
          [],
          fieldTestInfiniteScrollEntity,
          fieldTestInfiniteScrollEntity2,
        );
        expect(expectedResult).toEqual([fieldTestInfiniteScrollEntity, fieldTestInfiniteScrollEntity2]);
      });

      it('should accept null and undefined values', () => {
        const fieldTestInfiniteScrollEntity: IFieldTestInfiniteScrollEntity = sampleWithRequiredData;
        expectedResult = service.addFieldTestInfiniteScrollEntityToCollectionIfMissing([], null, fieldTestInfiniteScrollEntity, undefined);
        expect(expectedResult).toEqual([fieldTestInfiniteScrollEntity]);
      });

      it('should return initial array if no FieldTestInfiniteScrollEntity is added', () => {
        const fieldTestInfiniteScrollEntityCollection: IFieldTestInfiniteScrollEntity[] = [sampleWithRequiredData];
        expectedResult = service.addFieldTestInfiniteScrollEntityToCollectionIfMissing(
          fieldTestInfiniteScrollEntityCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(fieldTestInfiniteScrollEntityCollection);
      });
    });

    describe('compareFieldTestInfiniteScrollEntity', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFieldTestInfiniteScrollEntity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 20874 };
        const entity2 = null;

        const compareResult1 = service.compareFieldTestInfiniteScrollEntity(entity1, entity2);
        const compareResult2 = service.compareFieldTestInfiniteScrollEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 20874 };
        const entity2 = { id: 12964 };

        const compareResult1 = service.compareFieldTestInfiniteScrollEntity(entity1, entity2);
        const compareResult2 = service.compareFieldTestInfiniteScrollEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 20874 };
        const entity2 = { id: 20874 };

        const compareResult1 = service.compareFieldTestInfiniteScrollEntity(entity1, entity2);
        const compareResult2 = service.compareFieldTestInfiniteScrollEntity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

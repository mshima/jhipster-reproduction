import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICustomPackageChild } from '../custom-package-child.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../custom-package-child.test-samples';

import { CustomPackageChildService } from './custom-package-child.service';

const requireRestSample: ICustomPackageChild = {
  ...sampleWithRequiredData,
};

describe('CustomPackageChild Service', () => {
  let service: CustomPackageChildService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustomPackageChild | ICustomPackageChild[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CustomPackageChildService);
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

    it('should create a CustomPackageChild', () => {
      const customPackageChild = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(customPackageChild).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustomPackageChild', () => {
      const customPackageChild = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(customPackageChild).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustomPackageChild', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustomPackageChild', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustomPackageChild', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCustomPackageChildToCollectionIfMissing', () => {
      it('should add a CustomPackageChild to an empty array', () => {
        const customPackageChild: ICustomPackageChild = sampleWithRequiredData;
        expectedResult = service.addCustomPackageChildToCollectionIfMissing([], customPackageChild);
        expect(expectedResult).toEqual([customPackageChild]);
      });

      it('should not add a CustomPackageChild to an array that contains it', () => {
        const customPackageChild: ICustomPackageChild = sampleWithRequiredData;
        const customPackageChildCollection: ICustomPackageChild[] = [
          {
            ...customPackageChild,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustomPackageChildToCollectionIfMissing(customPackageChildCollection, customPackageChild);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustomPackageChild to an array that doesn't contain it", () => {
        const customPackageChild: ICustomPackageChild = sampleWithRequiredData;
        const customPackageChildCollection: ICustomPackageChild[] = [sampleWithPartialData];
        expectedResult = service.addCustomPackageChildToCollectionIfMissing(customPackageChildCollection, customPackageChild);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customPackageChild);
      });

      it('should add only unique CustomPackageChild to an array', () => {
        const customPackageChildArray: ICustomPackageChild[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const customPackageChildCollection: ICustomPackageChild[] = [sampleWithRequiredData];
        expectedResult = service.addCustomPackageChildToCollectionIfMissing(customPackageChildCollection, ...customPackageChildArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customPackageChild: ICustomPackageChild = sampleWithRequiredData;
        const customPackageChild2: ICustomPackageChild = sampleWithPartialData;
        expectedResult = service.addCustomPackageChildToCollectionIfMissing([], customPackageChild, customPackageChild2);
        expect(expectedResult).toEqual([customPackageChild, customPackageChild2]);
      });

      it('should accept null and undefined values', () => {
        const customPackageChild: ICustomPackageChild = sampleWithRequiredData;
        expectedResult = service.addCustomPackageChildToCollectionIfMissing([], null, customPackageChild, undefined);
        expect(expectedResult).toEqual([customPackageChild]);
      });

      it('should return initial array if no CustomPackageChild is added', () => {
        const customPackageChildCollection: ICustomPackageChild[] = [sampleWithRequiredData];
        expectedResult = service.addCustomPackageChildToCollectionIfMissing(customPackageChildCollection, undefined, null);
        expect(expectedResult).toEqual(customPackageChildCollection);
      });
    });

    describe('compareCustomPackageChild', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustomPackageChild(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 27070 };
        const entity2 = null;

        const compareResult1 = service.compareCustomPackageChild(entity1, entity2);
        const compareResult2 = service.compareCustomPackageChild(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 27070 };
        const entity2 = { id: 30280 };

        const compareResult1 = service.compareCustomPackageChild(entity1, entity2);
        const compareResult2 = service.compareCustomPackageChild(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 27070 };
        const entity2 = { id: 27070 };

        const compareResult1 = service.compareCustomPackageChild(entity1, entity2);
        const compareResult2 = service.compareCustomPackageChild(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

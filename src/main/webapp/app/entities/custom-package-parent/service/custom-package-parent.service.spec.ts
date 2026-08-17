import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICustomPackageParent } from '../custom-package-parent.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../custom-package-parent.test-samples';

import { CustomPackageParentService } from './custom-package-parent.service';

const requireRestSample: ICustomPackageParent = {
  ...sampleWithRequiredData,
};

describe('CustomPackageParent Service', () => {
  let service: CustomPackageParentService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustomPackageParent | ICustomPackageParent[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CustomPackageParentService);
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

    it('should create a CustomPackageParent', () => {
      const customPackageParent = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(customPackageParent).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustomPackageParent', () => {
      const customPackageParent = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(customPackageParent).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustomPackageParent', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustomPackageParent', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustomPackageParent', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCustomPackageParentToCollectionIfMissing', () => {
      it('should add a CustomPackageParent to an empty array', () => {
        const customPackageParent: ICustomPackageParent = sampleWithRequiredData;
        expectedResult = service.addCustomPackageParentToCollectionIfMissing([], customPackageParent);
        expect(expectedResult).toEqual([customPackageParent]);
      });

      it('should not add a CustomPackageParent to an array that contains it', () => {
        const customPackageParent: ICustomPackageParent = sampleWithRequiredData;
        const customPackageParentCollection: ICustomPackageParent[] = [
          {
            ...customPackageParent,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustomPackageParentToCollectionIfMissing(customPackageParentCollection, customPackageParent);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustomPackageParent to an array that doesn't contain it", () => {
        const customPackageParent: ICustomPackageParent = sampleWithRequiredData;
        const customPackageParentCollection: ICustomPackageParent[] = [sampleWithPartialData];
        expectedResult = service.addCustomPackageParentToCollectionIfMissing(customPackageParentCollection, customPackageParent);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customPackageParent);
      });

      it('should add only unique CustomPackageParent to an array', () => {
        const customPackageParentArray: ICustomPackageParent[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const customPackageParentCollection: ICustomPackageParent[] = [sampleWithRequiredData];
        expectedResult = service.addCustomPackageParentToCollectionIfMissing(customPackageParentCollection, ...customPackageParentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customPackageParent: ICustomPackageParent = sampleWithRequiredData;
        const customPackageParent2: ICustomPackageParent = sampleWithPartialData;
        expectedResult = service.addCustomPackageParentToCollectionIfMissing([], customPackageParent, customPackageParent2);
        expect(expectedResult).toEqual([customPackageParent, customPackageParent2]);
      });

      it('should accept null and undefined values', () => {
        const customPackageParent: ICustomPackageParent = sampleWithRequiredData;
        expectedResult = service.addCustomPackageParentToCollectionIfMissing([], null, customPackageParent, undefined);
        expect(expectedResult).toEqual([customPackageParent]);
      });

      it('should return initial array if no CustomPackageParent is added', () => {
        const customPackageParentCollection: ICustomPackageParent[] = [sampleWithRequiredData];
        expectedResult = service.addCustomPackageParentToCollectionIfMissing(customPackageParentCollection, undefined, null);
        expect(expectedResult).toEqual(customPackageParentCollection);
      });
    });

    describe('compareCustomPackageParent', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustomPackageParent(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 26792 };
        const entity2 = null;

        const compareResult1 = service.compareCustomPackageParent(entity1, entity2);
        const compareResult2 = service.compareCustomPackageParent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 26792 };
        const entity2 = { id: 3080 };

        const compareResult1 = service.compareCustomPackageParent(entity1, entity2);
        const compareResult2 = service.compareCustomPackageParent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 26792 };
        const entity2 = { id: 26792 };

        const compareResult1 = service.compareCustomPackageParent(entity1, entity2);
        const compareResult2 = service.compareCustomPackageParent(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

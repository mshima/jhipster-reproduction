import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';

import { IMapsIdUserProfileWithDTO } from '../maps-id-user-profile-with-dto.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../maps-id-user-profile-with-dto.test-samples';

import { MapsIdUserProfileWithDTOService, RestMapsIdUserProfileWithDTO } from './maps-id-user-profile-with-dto.service';

const requireRestSample: RestMapsIdUserProfileWithDTO = {
  ...sampleWithRequiredData,
  dateOfBirth: sampleWithRequiredData.dateOfBirth?.toJSON(),
};

describe('MapsIdUserProfileWithDTO Service', () => {
  let service: MapsIdUserProfileWithDTOService;
  let httpMock: HttpTestingController;
  let expectedResult: IMapsIdUserProfileWithDTO | IMapsIdUserProfileWithDTO[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MapsIdUserProfileWithDTOService);
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

    it('should create a MapsIdUserProfileWithDTO', () => {
      const mapsIdUserProfileWithDTO = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(mapsIdUserProfileWithDTO).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MapsIdUserProfileWithDTO', () => {
      const mapsIdUserProfileWithDTO = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(mapsIdUserProfileWithDTO).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MapsIdUserProfileWithDTO', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MapsIdUserProfileWithDTO', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MapsIdUserProfileWithDTO', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addMapsIdUserProfileWithDTOToCollectionIfMissing', () => {
      it('should add a MapsIdUserProfileWithDTO to an empty array', () => {
        const mapsIdUserProfileWithDTO: IMapsIdUserProfileWithDTO = sampleWithRequiredData;
        expectedResult = service.addMapsIdUserProfileWithDTOToCollectionIfMissing([], mapsIdUserProfileWithDTO);
        expect(expectedResult).toEqual([mapsIdUserProfileWithDTO]);
      });

      it('should not add a MapsIdUserProfileWithDTO to an array that contains it', () => {
        const mapsIdUserProfileWithDTO: IMapsIdUserProfileWithDTO = sampleWithRequiredData;
        const mapsIdUserProfileWithDTOCollection: IMapsIdUserProfileWithDTO[] = [
          {
            ...mapsIdUserProfileWithDTO,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMapsIdUserProfileWithDTOToCollectionIfMissing(
          mapsIdUserProfileWithDTOCollection,
          mapsIdUserProfileWithDTO,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MapsIdUserProfileWithDTO to an array that doesn't contain it", () => {
        const mapsIdUserProfileWithDTO: IMapsIdUserProfileWithDTO = sampleWithRequiredData;
        const mapsIdUserProfileWithDTOCollection: IMapsIdUserProfileWithDTO[] = [sampleWithPartialData];
        expectedResult = service.addMapsIdUserProfileWithDTOToCollectionIfMissing(
          mapsIdUserProfileWithDTOCollection,
          mapsIdUserProfileWithDTO,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mapsIdUserProfileWithDTO);
      });

      it('should add only unique MapsIdUserProfileWithDTO to an array', () => {
        const mapsIdUserProfileWithDTOArray: IMapsIdUserProfileWithDTO[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const mapsIdUserProfileWithDTOCollection: IMapsIdUserProfileWithDTO[] = [sampleWithRequiredData];
        expectedResult = service.addMapsIdUserProfileWithDTOToCollectionIfMissing(
          mapsIdUserProfileWithDTOCollection,
          ...mapsIdUserProfileWithDTOArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mapsIdUserProfileWithDTO: IMapsIdUserProfileWithDTO = sampleWithRequiredData;
        const mapsIdUserProfileWithDTO2: IMapsIdUserProfileWithDTO = sampleWithPartialData;
        expectedResult = service.addMapsIdUserProfileWithDTOToCollectionIfMissing([], mapsIdUserProfileWithDTO, mapsIdUserProfileWithDTO2);
        expect(expectedResult).toEqual([mapsIdUserProfileWithDTO, mapsIdUserProfileWithDTO2]);
      });

      it('should accept null and undefined values', () => {
        const mapsIdUserProfileWithDTO: IMapsIdUserProfileWithDTO = sampleWithRequiredData;
        expectedResult = service.addMapsIdUserProfileWithDTOToCollectionIfMissing([], null, mapsIdUserProfileWithDTO, undefined);
        expect(expectedResult).toEqual([mapsIdUserProfileWithDTO]);
      });

      it('should return initial array if no MapsIdUserProfileWithDTO is added', () => {
        const mapsIdUserProfileWithDTOCollection: IMapsIdUserProfileWithDTO[] = [sampleWithRequiredData];
        expectedResult = service.addMapsIdUserProfileWithDTOToCollectionIfMissing(mapsIdUserProfileWithDTOCollection, undefined, null);
        expect(expectedResult).toEqual(mapsIdUserProfileWithDTOCollection);
      });
    });

    describe('compareMapsIdUserProfileWithDTO', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMapsIdUserProfileWithDTO(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8289 };
        const entity2 = null;

        const compareResult1 = service.compareMapsIdUserProfileWithDTO(entity1, entity2);
        const compareResult2 = service.compareMapsIdUserProfileWithDTO(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8289 };
        const entity2 = { id: 13667 };

        const compareResult1 = service.compareMapsIdUserProfileWithDTO(entity1, entity2);
        const compareResult2 = service.compareMapsIdUserProfileWithDTO(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 8289 };
        const entity2 = { id: 8289 };

        const compareResult1 = service.compareMapsIdUserProfileWithDTO(entity1, entity2);
        const compareResult2 = service.compareMapsIdUserProfileWithDTO(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

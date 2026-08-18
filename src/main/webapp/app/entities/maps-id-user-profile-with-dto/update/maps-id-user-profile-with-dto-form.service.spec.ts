import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../maps-id-user-profile-with-dto.test-samples';

import { MapsIdUserProfileWithDTOFormService } from './maps-id-user-profile-with-dto-form.service';

describe('MapsIdUserProfileWithDTO Form Service', () => {
  let service: MapsIdUserProfileWithDTOFormService;

  beforeEach(() => {
    service = TestBed.inject(MapsIdUserProfileWithDTOFormService);
  });

  describe('Service methods', () => {
    describe('createMapsIdUserProfileWithDTOFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMapsIdUserProfileWithDTOFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateOfBirth: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IMapsIdUserProfileWithDTO should create a new form with FormGroup', () => {
        const formGroup = service.createMapsIdUserProfileWithDTOFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateOfBirth: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getMapsIdUserProfileWithDTO', () => {
      it('should return NewMapsIdUserProfileWithDTO for default MapsIdUserProfileWithDTO initial value', () => {
        const formGroup = service.createMapsIdUserProfileWithDTOFormGroup(sampleWithNewData);

        const mapsIdUserProfileWithDTO = service.getMapsIdUserProfileWithDTO(formGroup);

        expect(mapsIdUserProfileWithDTO).toMatchObject(sampleWithNewData);
      });

      it('should return NewMapsIdUserProfileWithDTO for empty MapsIdUserProfileWithDTO initial value', () => {
        const formGroup = service.createMapsIdUserProfileWithDTOFormGroup();

        const mapsIdUserProfileWithDTO = service.getMapsIdUserProfileWithDTO(formGroup);

        expect(mapsIdUserProfileWithDTO).toMatchObject({});
      });

      it('should return IMapsIdUserProfileWithDTO', () => {
        const formGroup = service.createMapsIdUserProfileWithDTOFormGroup(sampleWithRequiredData);

        const mapsIdUserProfileWithDTO = service.getMapsIdUserProfileWithDTO(formGroup);

        expect(mapsIdUserProfileWithDTO).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMapsIdUserProfileWithDTO should not enable id FormControl', () => {
        const formGroup = service.createMapsIdUserProfileWithDTOFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMapsIdUserProfileWithDTO should disable id FormControl', () => {
        const formGroup = service.createMapsIdUserProfileWithDTOFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

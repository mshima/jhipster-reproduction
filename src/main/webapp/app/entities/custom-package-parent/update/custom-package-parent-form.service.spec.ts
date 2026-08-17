import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../custom-package-parent.test-samples';

import { CustomPackageParentFormService } from './custom-package-parent-form.service';

describe('CustomPackageParent Form Service', () => {
  let service: CustomPackageParentFormService;

  beforeEach(() => {
    service = TestBed.inject(CustomPackageParentFormService);
  });

  describe('Service methods', () => {
    describe('createCustomPackageParentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustomPackageParentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            parentName: expect.any(Object),
          }),
        );
      });

      it('passing ICustomPackageParent should create a new form with FormGroup', () => {
        const formGroup = service.createCustomPackageParentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            parentName: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustomPackageParent', () => {
      it('should return NewCustomPackageParent for default CustomPackageParent initial value', () => {
        const formGroup = service.createCustomPackageParentFormGroup(sampleWithNewData);

        const customPackageParent = service.getCustomPackageParent(formGroup);

        expect(customPackageParent).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustomPackageParent for empty CustomPackageParent initial value', () => {
        const formGroup = service.createCustomPackageParentFormGroup();

        const customPackageParent = service.getCustomPackageParent(formGroup);

        expect(customPackageParent).toMatchObject({});
      });

      it('should return ICustomPackageParent', () => {
        const formGroup = service.createCustomPackageParentFormGroup(sampleWithRequiredData);

        const customPackageParent = service.getCustomPackageParent(formGroup);

        expect(customPackageParent).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustomPackageParent should not enable id FormControl', () => {
        const formGroup = service.createCustomPackageParentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustomPackageParent should disable id FormControl', () => {
        const formGroup = service.createCustomPackageParentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

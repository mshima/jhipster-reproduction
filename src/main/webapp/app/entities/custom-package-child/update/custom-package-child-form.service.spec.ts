import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../custom-package-child.test-samples';

import { CustomPackageChildFormService } from './custom-package-child-form.service';

describe('CustomPackageChild Form Service', () => {
  let service: CustomPackageChildFormService;

  beforeEach(() => {
    service = TestBed.inject(CustomPackageChildFormService);
  });

  describe('Service methods', () => {
    describe('createCustomPackageChildFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustomPackageChildFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            childName: expect.any(Object),
            user: expect.any(Object),
            customPackageParent: expect.any(Object),
          }),
        );
      });

      it('passing ICustomPackageChild should create a new form with FormGroup', () => {
        const formGroup = service.createCustomPackageChildFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            childName: expect.any(Object),
            user: expect.any(Object),
            customPackageParent: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustomPackageChild', () => {
      it('should return NewCustomPackageChild for default CustomPackageChild initial value', () => {
        const formGroup = service.createCustomPackageChildFormGroup(sampleWithNewData);

        const customPackageChild = service.getCustomPackageChild(formGroup);

        expect(customPackageChild).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustomPackageChild for empty CustomPackageChild initial value', () => {
        const formGroup = service.createCustomPackageChildFormGroup();

        const customPackageChild = service.getCustomPackageChild(formGroup);

        expect(customPackageChild).toMatchObject({});
      });

      it('should return ICustomPackageChild', () => {
        const formGroup = service.createCustomPackageChildFormGroup(sampleWithRequiredData);

        const customPackageChild = service.getCustomPackageChild(formGroup);

        expect(customPackageChild).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustomPackageChild should not enable id FormControl', () => {
        const formGroup = service.createCustomPackageChildFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustomPackageChild should disable id FormControl', () => {
        const formGroup = service.createCustomPackageChildFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

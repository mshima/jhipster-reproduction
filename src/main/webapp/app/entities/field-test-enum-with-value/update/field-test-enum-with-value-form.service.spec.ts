import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../field-test-enum-with-value.test-samples';

import { FieldTestEnumWithValueFormService } from './field-test-enum-with-value-form.service';

describe('FieldTestEnumWithValue Form Service', () => {
  let service: FieldTestEnumWithValueFormService;

  beforeEach(() => {
    service = TestBed.inject(FieldTestEnumWithValueFormService);
  });

  describe('Service methods', () => {
    describe('createFieldTestEnumWithValueFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFieldTestEnumWithValueFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            myFieldA: expect.any(Object),
            myFieldB: expect.any(Object),
            myFieldC: expect.any(Object),
            myFieldD: expect.any(Object),
            myFieldE: expect.any(Object),
          }),
        );
      });

      it('passing IFieldTestEnumWithValue should create a new form with FormGroup', () => {
        const formGroup = service.createFieldTestEnumWithValueFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            myFieldA: expect.any(Object),
            myFieldB: expect.any(Object),
            myFieldC: expect.any(Object),
            myFieldD: expect.any(Object),
            myFieldE: expect.any(Object),
          }),
        );
      });
    });

    describe('getFieldTestEnumWithValue', () => {
      it('should return NewFieldTestEnumWithValue for default FieldTestEnumWithValue initial value', () => {
        const formGroup = service.createFieldTestEnumWithValueFormGroup(sampleWithNewData);

        const fieldTestEnumWithValue = service.getFieldTestEnumWithValue(formGroup);

        expect(fieldTestEnumWithValue).toMatchObject(sampleWithNewData);
      });

      it('should return NewFieldTestEnumWithValue for empty FieldTestEnumWithValue initial value', () => {
        const formGroup = service.createFieldTestEnumWithValueFormGroup();

        const fieldTestEnumWithValue = service.getFieldTestEnumWithValue(formGroup);

        expect(fieldTestEnumWithValue).toMatchObject({});
      });

      it('should return IFieldTestEnumWithValue', () => {
        const formGroup = service.createFieldTestEnumWithValueFormGroup(sampleWithRequiredData);

        const fieldTestEnumWithValue = service.getFieldTestEnumWithValue(formGroup);

        expect(fieldTestEnumWithValue).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFieldTestEnumWithValue should not enable id FormControl', () => {
        const formGroup = service.createFieldTestEnumWithValueFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFieldTestEnumWithValue should disable id FormControl', () => {
        const formGroup = service.createFieldTestEnumWithValueFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

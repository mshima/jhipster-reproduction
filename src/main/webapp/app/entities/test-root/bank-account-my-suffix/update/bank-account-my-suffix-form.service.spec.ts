import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../bank-account-my-suffix.test-samples';

import { BankAccountMySuffixFormService } from './bank-account-my-suffix-form.service';

describe('BankAccountMySuffix Form Service', () => {
  let service: BankAccountMySuffixFormService;

  beforeEach(() => {
    service = TestBed.inject(BankAccountMySuffixFormService);
  });

  describe('Service methods', () => {
    describe('createBankAccountMySuffixFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBankAccountMySuffixFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            guid: expect.any(Object),
            bankNumber: expect.any(Object),
            agencyNumber: expect.any(Object),
            lastOperationDuration: expect.any(Object),
            meanOperationDuration: expect.any(Object),
            meanQueueDuration: expect.any(Object),
            balance: expect.any(Object),
            openingDay: expect.any(Object),
            lastOperationDate: expect.any(Object),
            active: expect.any(Object),
            accountType: expect.any(Object),
            attachment: expect.any(Object),
            description: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IBankAccountMySuffix should create a new form with FormGroup', () => {
        const formGroup = service.createBankAccountMySuffixFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            guid: expect.any(Object),
            bankNumber: expect.any(Object),
            agencyNumber: expect.any(Object),
            lastOperationDuration: expect.any(Object),
            meanOperationDuration: expect.any(Object),
            meanQueueDuration: expect.any(Object),
            balance: expect.any(Object),
            openingDay: expect.any(Object),
            lastOperationDate: expect.any(Object),
            active: expect.any(Object),
            accountType: expect.any(Object),
            attachment: expect.any(Object),
            description: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getBankAccountMySuffix', () => {
      it('should return NewBankAccountMySuffix for default BankAccountMySuffix initial value', () => {
        const formGroup = service.createBankAccountMySuffixFormGroup(sampleWithNewData);

        const bankAccount = service.getBankAccountMySuffix(formGroup);

        expect(bankAccount).toMatchObject(sampleWithNewData);
      });

      it('should return NewBankAccountMySuffix for empty BankAccountMySuffix initial value', () => {
        const formGroup = service.createBankAccountMySuffixFormGroup();

        const bankAccount = service.getBankAccountMySuffix(formGroup);

        expect(bankAccount).toMatchObject({});
      });

      it('should return IBankAccountMySuffix', () => {
        const formGroup = service.createBankAccountMySuffixFormGroup(sampleWithRequiredData);

        const bankAccount = service.getBankAccountMySuffix(formGroup);

        expect(bankAccount).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBankAccountMySuffix should not enable id FormControl', () => {
        const formGroup = service.createBankAccountMySuffixFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBankAccountMySuffix should disable id FormControl', () => {
        const formGroup = service.createBankAccountMySuffixFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

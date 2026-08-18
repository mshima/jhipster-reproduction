import { Service } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBankAccountMySuffix, NewBankAccountMySuffix } from '../bank-account-my-suffix.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBankAccountMySuffix for edit and NewBankAccountMySuffixFormGroupInput for create.
 */
type BankAccountMySuffixFormGroupInput = IBankAccountMySuffix | PartialWithRequiredKeyOf<NewBankAccountMySuffix>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBankAccountMySuffix | NewBankAccountMySuffix> = Omit<T, 'lastOperationDate'> & {
  lastOperationDate?: string | null;
};

type BankAccountMySuffixFormRawValue = FormValueOf<IBankAccountMySuffix>;

type NewBankAccountMySuffixFormRawValue = FormValueOf<NewBankAccountMySuffix>;

type BankAccountMySuffixFormDefaults = Pick<NewBankAccountMySuffix, 'id' | 'lastOperationDate' | 'active'>;

type BankAccountMySuffixFormGroupContent = {
  id: FormControl<BankAccountMySuffixFormRawValue['id'] | NewBankAccountMySuffix['id']>;
  name: FormControl<BankAccountMySuffixFormRawValue['name']>;
  guid: FormControl<BankAccountMySuffixFormRawValue['guid']>;
  bankNumber: FormControl<BankAccountMySuffixFormRawValue['bankNumber']>;
  agencyNumber: FormControl<BankAccountMySuffixFormRawValue['agencyNumber']>;
  lastOperationDuration: FormControl<BankAccountMySuffixFormRawValue['lastOperationDuration']>;
  meanOperationDuration: FormControl<BankAccountMySuffixFormRawValue['meanOperationDuration']>;
  meanQueueDuration: FormControl<BankAccountMySuffixFormRawValue['meanQueueDuration']>;
  balance: FormControl<BankAccountMySuffixFormRawValue['balance']>;
  openingDay: FormControl<BankAccountMySuffixFormRawValue['openingDay']>;
  lastOperationDate: FormControl<BankAccountMySuffixFormRawValue['lastOperationDate']>;
  active: FormControl<BankAccountMySuffixFormRawValue['active']>;
  accountType: FormControl<BankAccountMySuffixFormRawValue['accountType']>;
  attachment: FormControl<BankAccountMySuffixFormRawValue['attachment']>;
  attachmentContentType: FormControl<BankAccountMySuffixFormRawValue['attachmentContentType']>;
  description: FormControl<BankAccountMySuffixFormRawValue['description']>;
  user: FormControl<BankAccountMySuffixFormRawValue['user']>;
};

export type BankAccountMySuffixFormGroup = FormGroup<BankAccountMySuffixFormGroupContent>;

@Service()
export class BankAccountMySuffixFormService {
  createBankAccountMySuffixFormGroup(bankAccount?: BankAccountMySuffixFormGroupInput): BankAccountMySuffixFormGroup {
    const bankAccountRawValue = this.convertBankAccountMySuffixToBankAccountMySuffixRawValue({
      ...this.getFormDefaults(),
      ...(bankAccount ?? { id: null }),
    });

    return new FormGroup<BankAccountMySuffixFormGroupContent>({
      id: new FormControl(
        { value: bankAccountRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(bankAccountRawValue.name, {
        validators: [Validators.required],
      }),
      guid: new FormControl(bankAccountRawValue.guid),
      bankNumber: new FormControl(bankAccountRawValue.bankNumber),
      agencyNumber: new FormControl(bankAccountRawValue.agencyNumber),
      lastOperationDuration: new FormControl(bankAccountRawValue.lastOperationDuration),
      meanOperationDuration: new FormControl(bankAccountRawValue.meanOperationDuration),
      meanQueueDuration: new FormControl(bankAccountRawValue.meanQueueDuration),
      balance: new FormControl(bankAccountRawValue.balance, {
        validators: [Validators.required],
      }),
      openingDay: new FormControl(bankAccountRawValue.openingDay),
      lastOperationDate: new FormControl(bankAccountRawValue.lastOperationDate),
      active: new FormControl(bankAccountRawValue.active),
      accountType: new FormControl(bankAccountRawValue.accountType),
      attachment: new FormControl(bankAccountRawValue.attachment),
      attachmentContentType: new FormControl(bankAccountRawValue.attachmentContentType),
      description: new FormControl(bankAccountRawValue.description),
      user: new FormControl(bankAccountRawValue.user),
    });
  }

  getBankAccountMySuffix(form: BankAccountMySuffixFormGroup): IBankAccountMySuffix | NewBankAccountMySuffix {
    return this.convertBankAccountMySuffixRawValueToBankAccountMySuffix(form.getRawValue());
  }

  resetForm(form: BankAccountMySuffixFormGroup, bankAccount: BankAccountMySuffixFormGroupInput): void {
    const bankAccountRawValue = this.convertBankAccountMySuffixToBankAccountMySuffixRawValue({ ...this.getFormDefaults(), ...bankAccount });
    form.reset({
      ...bankAccountRawValue,
      id: { value: bankAccountRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): BankAccountMySuffixFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastOperationDate: currentTime,
      active: false,
    };
  }

  private convertBankAccountMySuffixRawValueToBankAccountMySuffix(
    rawBankAccountMySuffix: BankAccountMySuffixFormRawValue | NewBankAccountMySuffixFormRawValue,
  ): IBankAccountMySuffix | NewBankAccountMySuffix {
    return {
      ...rawBankAccountMySuffix,
      lastOperationDate: dayjs(rawBankAccountMySuffix.lastOperationDate, DATE_TIME_FORMAT),
    };
  }

  private convertBankAccountMySuffixToBankAccountMySuffixRawValue(
    bankAccount: IBankAccountMySuffix | (Partial<NewBankAccountMySuffix> & BankAccountMySuffixFormDefaults),
  ): BankAccountMySuffixFormRawValue | PartialWithRequiredKeyOf<NewBankAccountMySuffixFormRawValue> {
    return {
      ...bankAccount,
      lastOperationDate: bankAccount.lastOperationDate ? bankAccount.lastOperationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}

import { Service } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFieldTestEnumWithValue, NewFieldTestEnumWithValue } from '../field-test-enum-with-value.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFieldTestEnumWithValue for edit and NewFieldTestEnumWithValueFormGroupInput for create.
 */
type FieldTestEnumWithValueFormGroupInput = IFieldTestEnumWithValue | PartialWithRequiredKeyOf<NewFieldTestEnumWithValue>;

type FieldTestEnumWithValueFormDefaults = Pick<NewFieldTestEnumWithValue, 'id'>;

type FieldTestEnumWithValueFormGroupContent = {
  id: FormControl<IFieldTestEnumWithValue['id'] | NewFieldTestEnumWithValue['id']>;
  myFieldA: FormControl<IFieldTestEnumWithValue['myFieldA']>;
  myFieldB: FormControl<IFieldTestEnumWithValue['myFieldB']>;
  myFieldC: FormControl<IFieldTestEnumWithValue['myFieldC']>;
  myFieldD: FormControl<IFieldTestEnumWithValue['myFieldD']>;
  myFieldE: FormControl<IFieldTestEnumWithValue['myFieldE']>;
};

export type FieldTestEnumWithValueFormGroup = FormGroup<FieldTestEnumWithValueFormGroupContent>;

@Service()
export class FieldTestEnumWithValueFormService {
  createFieldTestEnumWithValueFormGroup(fieldTestEnumWithValue?: FieldTestEnumWithValueFormGroupInput): FieldTestEnumWithValueFormGroup {
    const fieldTestEnumWithValueRawValue = {
      ...this.getFormDefaults(),
      ...(fieldTestEnumWithValue ?? { id: null }),
    };

    return new FormGroup<FieldTestEnumWithValueFormGroupContent>({
      id: new FormControl(
        { value: fieldTestEnumWithValueRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      myFieldA: new FormControl(fieldTestEnumWithValueRawValue.myFieldA),
      myFieldB: new FormControl(fieldTestEnumWithValueRawValue.myFieldB),
      myFieldC: new FormControl(fieldTestEnumWithValueRawValue.myFieldC),
      myFieldD: new FormControl(fieldTestEnumWithValueRawValue.myFieldD),
      myFieldE: new FormControl(fieldTestEnumWithValueRawValue.myFieldE),
    });
  }

  getFieldTestEnumWithValue(form: FieldTestEnumWithValueFormGroup): IFieldTestEnumWithValue | NewFieldTestEnumWithValue {
    return form.getRawValue();
  }

  resetForm(form: FieldTestEnumWithValueFormGroup, fieldTestEnumWithValue: FieldTestEnumWithValueFormGroupInput): void {
    const fieldTestEnumWithValueRawValue = { ...this.getFormDefaults(), ...fieldTestEnumWithValue };
    form.reset({
      ...fieldTestEnumWithValueRawValue,
      id: { value: fieldTestEnumWithValueRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): FieldTestEnumWithValueFormDefaults {
    return {
      id: null,
    };
  }
}

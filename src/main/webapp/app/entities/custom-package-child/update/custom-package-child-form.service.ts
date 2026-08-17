import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICustomPackageChild, NewCustomPackageChild } from '../custom-package-child.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomPackageChild for edit and NewCustomPackageChildFormGroupInput for create.
 */
type CustomPackageChildFormGroupInput = ICustomPackageChild | PartialWithRequiredKeyOf<NewCustomPackageChild>;

type CustomPackageChildFormDefaults = Pick<NewCustomPackageChild, 'id'>;

type CustomPackageChildFormGroupContent = {
  id: FormControl<ICustomPackageChild['id'] | NewCustomPackageChild['id']>;
  childName: FormControl<ICustomPackageChild['childName']>;
  user: FormControl<ICustomPackageChild['user']>;
  customPackageParent: FormControl<ICustomPackageChild['customPackageParent']>;
};

export type CustomPackageChildFormGroup = FormGroup<CustomPackageChildFormGroupContent>;

@Service()
export class CustomPackageChildFormService {
  createCustomPackageChildFormGroup(customPackageChild?: CustomPackageChildFormGroupInput): CustomPackageChildFormGroup {
    const customPackageChildRawValue = {
      ...this.getFormDefaults(),
      ...(customPackageChild ?? { id: null }),
    };

    return new FormGroup<CustomPackageChildFormGroupContent>({
      id: new FormControl(
        { value: customPackageChildRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      childName: new FormControl(customPackageChildRawValue.childName),
      user: new FormControl(customPackageChildRawValue.user),
      customPackageParent: new FormControl(customPackageChildRawValue.customPackageParent),
    });
  }

  getCustomPackageChild(form: CustomPackageChildFormGroup): ICustomPackageChild | NewCustomPackageChild {
    return form.getRawValue();
  }

  resetForm(form: CustomPackageChildFormGroup, customPackageChild: CustomPackageChildFormGroupInput): void {
    const customPackageChildRawValue = { ...this.getFormDefaults(), ...customPackageChild };
    form.reset({
      ...customPackageChildRawValue,
      id: { value: customPackageChildRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CustomPackageChildFormDefaults {
    return {
      id: null,
    };
  }
}

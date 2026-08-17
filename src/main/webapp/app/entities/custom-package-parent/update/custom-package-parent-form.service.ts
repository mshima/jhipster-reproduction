import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICustomPackageParent, NewCustomPackageParent } from '../custom-package-parent.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomPackageParent for edit and NewCustomPackageParentFormGroupInput for create.
 */
type CustomPackageParentFormGroupInput = ICustomPackageParent | PartialWithRequiredKeyOf<NewCustomPackageParent>;

type CustomPackageParentFormDefaults = Pick<NewCustomPackageParent, 'id'>;

type CustomPackageParentFormGroupContent = {
  id: FormControl<ICustomPackageParent['id'] | NewCustomPackageParent['id']>;
  parentName: FormControl<ICustomPackageParent['parentName']>;
};

export type CustomPackageParentFormGroup = FormGroup<CustomPackageParentFormGroupContent>;

@Service()
export class CustomPackageParentFormService {
  createCustomPackageParentFormGroup(customPackageParent?: CustomPackageParentFormGroupInput): CustomPackageParentFormGroup {
    const customPackageParentRawValue = {
      ...this.getFormDefaults(),
      ...(customPackageParent ?? { id: null }),
    };

    return new FormGroup<CustomPackageParentFormGroupContent>({
      id: new FormControl(
        { value: customPackageParentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      parentName: new FormControl(customPackageParentRawValue.parentName),
    });
  }

  getCustomPackageParent(form: CustomPackageParentFormGroup): ICustomPackageParent | NewCustomPackageParent {
    return form.getRawValue();
  }

  resetForm(form: CustomPackageParentFormGroup, customPackageParent: CustomPackageParentFormGroupInput): void {
    const customPackageParentRawValue = { ...this.getFormDefaults(), ...customPackageParent };
    form.reset({
      ...customPackageParentRawValue,
      id: { value: customPackageParentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CustomPackageParentFormDefaults {
    return {
      id: null,
    };
  }
}

import { Service } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEntityWithDTO, NewEntityWithDTO } from '../entity-with-dto.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEntityWithDTO for edit and NewEntityWithDTOFormGroupInput for create.
 */
type EntityWithDTOFormGroupInput = IEntityWithDTO | PartialWithRequiredKeyOf<NewEntityWithDTO>;

type EntityWithDTOFormDefaults = Pick<NewEntityWithDTO, 'id'>;

type EntityWithDTOFormGroupContent = {
  id: FormControl<IEntityWithDTO['id'] | NewEntityWithDTO['id']>;
  emma: FormControl<IEntityWithDTO['emma']>;
};

export type EntityWithDTOFormGroup = FormGroup<EntityWithDTOFormGroupContent>;

@Service()
export class EntityWithDTOFormService {
  createEntityWithDTOFormGroup(entityWithDTO?: EntityWithDTOFormGroupInput): EntityWithDTOFormGroup {
    const entityWithDTORawValue = {
      ...this.getFormDefaults(),
      ...(entityWithDTO ?? { id: null }),
    };

    return new FormGroup<EntityWithDTOFormGroupContent>({
      id: new FormControl(
        { value: entityWithDTORawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      emma: new FormControl(entityWithDTORawValue.emma),
    });
  }

  getEntityWithDTO(form: EntityWithDTOFormGroup): IEntityWithDTO | NewEntityWithDTO {
    return form.getRawValue();
  }

  resetForm(form: EntityWithDTOFormGroup, entityWithDTO: EntityWithDTOFormGroupInput): void {
    const entityWithDTORawValue = { ...this.getFormDefaults(), ...entityWithDTO };
    form.reset({
      ...entityWithDTORawValue,
      id: { value: entityWithDTORawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): EntityWithDTOFormDefaults {
    return {
      id: null,
    };
  }
}

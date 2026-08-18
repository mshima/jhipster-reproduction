import { Service } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEntityWithPaginationAndDTO, NewEntityWithPaginationAndDTO } from '../entity-with-pagination-and-dto.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEntityWithPaginationAndDTO for edit and NewEntityWithPaginationAndDTOFormGroupInput for create.
 */
type EntityWithPaginationAndDTOFormGroupInput = IEntityWithPaginationAndDTO | PartialWithRequiredKeyOf<NewEntityWithPaginationAndDTO>;

type EntityWithPaginationAndDTOFormDefaults = Pick<NewEntityWithPaginationAndDTO, 'id'>;

type EntityWithPaginationAndDTOFormGroupContent = {
  id: FormControl<IEntityWithPaginationAndDTO['id'] | NewEntityWithPaginationAndDTO['id']>;
  lea: FormControl<IEntityWithPaginationAndDTO['lea']>;
};

export type EntityWithPaginationAndDTOFormGroup = FormGroup<EntityWithPaginationAndDTOFormGroupContent>;

@Service()
export class EntityWithPaginationAndDTOFormService {
  createEntityWithPaginationAndDTOFormGroup(
    entityWithPaginationAndDTO?: EntityWithPaginationAndDTOFormGroupInput,
  ): EntityWithPaginationAndDTOFormGroup {
    const entityWithPaginationAndDTORawValue = {
      ...this.getFormDefaults(),
      ...(entityWithPaginationAndDTO ?? { id: null }),
    };

    return new FormGroup<EntityWithPaginationAndDTOFormGroupContent>({
      id: new FormControl(
        { value: entityWithPaginationAndDTORawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      lea: new FormControl(entityWithPaginationAndDTORawValue.lea),
    });
  }

  getEntityWithPaginationAndDTO(form: EntityWithPaginationAndDTOFormGroup): IEntityWithPaginationAndDTO | NewEntityWithPaginationAndDTO {
    return form.getRawValue();
  }

  resetForm(form: EntityWithPaginationAndDTOFormGroup, entityWithPaginationAndDTO: EntityWithPaginationAndDTOFormGroupInput): void {
    const entityWithPaginationAndDTORawValue = { ...this.getFormDefaults(), ...entityWithPaginationAndDTO };
    form.reset({
      ...entityWithPaginationAndDTORawValue,
      id: { value: entityWithPaginationAndDTORawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): EntityWithPaginationAndDTOFormDefaults {
    return {
      id: null,
    };
  }
}

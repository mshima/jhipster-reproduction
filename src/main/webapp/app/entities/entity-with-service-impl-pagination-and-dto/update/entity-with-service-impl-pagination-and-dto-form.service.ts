import { Service } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import {
  IEntityWithServiceImplPaginationAndDTO,
  NewEntityWithServiceImplPaginationAndDTO,
} from '../entity-with-service-impl-pagination-and-dto.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEntityWithServiceImplPaginationAndDTO for edit and NewEntityWithServiceImplPaginationAndDTOFormGroupInput for create.
 */
type EntityWithServiceImplPaginationAndDTOFormGroupInput =
  IEntityWithServiceImplPaginationAndDTO | PartialWithRequiredKeyOf<NewEntityWithServiceImplPaginationAndDTO>;

type EntityWithServiceImplPaginationAndDTOFormDefaults = Pick<NewEntityWithServiceImplPaginationAndDTO, 'id'>;

type EntityWithServiceImplPaginationAndDTOFormGroupContent = {
  id: FormControl<IEntityWithServiceImplPaginationAndDTO['id'] | NewEntityWithServiceImplPaginationAndDTO['id']>;
  theo: FormControl<IEntityWithServiceImplPaginationAndDTO['theo']>;
};

export type EntityWithServiceImplPaginationAndDTOFormGroup = FormGroup<EntityWithServiceImplPaginationAndDTOFormGroupContent>;

@Service()
export class EntityWithServiceImplPaginationAndDTOFormService {
  createEntityWithServiceImplPaginationAndDTOFormGroup(
    entityWithServiceImplPaginationAndDTO?: EntityWithServiceImplPaginationAndDTOFormGroupInput,
  ): EntityWithServiceImplPaginationAndDTOFormGroup {
    const entityWithServiceImplPaginationAndDTORawValue = {
      ...this.getFormDefaults(),
      ...(entityWithServiceImplPaginationAndDTO ?? { id: null }),
    };

    return new FormGroup<EntityWithServiceImplPaginationAndDTOFormGroupContent>({
      id: new FormControl(
        { value: entityWithServiceImplPaginationAndDTORawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      theo: new FormControl(entityWithServiceImplPaginationAndDTORawValue.theo),
    });
  }

  getEntityWithServiceImplPaginationAndDTO(
    form: EntityWithServiceImplPaginationAndDTOFormGroup,
  ): IEntityWithServiceImplPaginationAndDTO | NewEntityWithServiceImplPaginationAndDTO {
    return form.getRawValue();
  }

  resetForm(
    form: EntityWithServiceImplPaginationAndDTOFormGroup,
    entityWithServiceImplPaginationAndDTO: EntityWithServiceImplPaginationAndDTOFormGroupInput,
  ): void {
    const entityWithServiceImplPaginationAndDTORawValue = { ...this.getFormDefaults(), ...entityWithServiceImplPaginationAndDTO };
    form.reset({
      ...entityWithServiceImplPaginationAndDTORawValue,
      id: { value: entityWithServiceImplPaginationAndDTORawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): EntityWithServiceImplPaginationAndDTOFormDefaults {
    return {
      id: null,
    };
  }
}

import { Service } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import {
  IEntityWithServiceClassAndPagination,
  NewEntityWithServiceClassAndPagination,
} from '../entity-with-service-class-and-pagination.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEntityWithServiceClassAndPagination for edit and NewEntityWithServiceClassAndPaginationFormGroupInput for create.
 */
type EntityWithServiceClassAndPaginationFormGroupInput =
  IEntityWithServiceClassAndPagination | PartialWithRequiredKeyOf<NewEntityWithServiceClassAndPagination>;

type EntityWithServiceClassAndPaginationFormDefaults = Pick<NewEntityWithServiceClassAndPagination, 'id'>;

type EntityWithServiceClassAndPaginationFormGroupContent = {
  id: FormControl<IEntityWithServiceClassAndPagination['id'] | NewEntityWithServiceClassAndPagination['id']>;
  enzo: FormControl<IEntityWithServiceClassAndPagination['enzo']>;
};

export type EntityWithServiceClassAndPaginationFormGroup = FormGroup<EntityWithServiceClassAndPaginationFormGroupContent>;

@Service()
export class EntityWithServiceClassAndPaginationFormService {
  createEntityWithServiceClassAndPaginationFormGroup(
    entityWithServiceClassAndPagination?: EntityWithServiceClassAndPaginationFormGroupInput,
  ): EntityWithServiceClassAndPaginationFormGroup {
    const entityWithServiceClassAndPaginationRawValue = {
      ...this.getFormDefaults(),
      ...(entityWithServiceClassAndPagination ?? { id: null }),
    };

    return new FormGroup<EntityWithServiceClassAndPaginationFormGroupContent>({
      id: new FormControl(
        { value: entityWithServiceClassAndPaginationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      enzo: new FormControl(entityWithServiceClassAndPaginationRawValue.enzo),
    });
  }

  getEntityWithServiceClassAndPagination(
    form: EntityWithServiceClassAndPaginationFormGroup,
  ): IEntityWithServiceClassAndPagination | NewEntityWithServiceClassAndPagination {
    return form.getRawValue();
  }

  resetForm(
    form: EntityWithServiceClassAndPaginationFormGroup,
    entityWithServiceClassAndPagination: EntityWithServiceClassAndPaginationFormGroupInput,
  ): void {
    const entityWithServiceClassAndPaginationRawValue = { ...this.getFormDefaults(), ...entityWithServiceClassAndPagination };
    form.reset({
      ...entityWithServiceClassAndPaginationRawValue,
      id: { value: entityWithServiceClassAndPaginationRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): EntityWithServiceClassAndPaginationFormDefaults {
    return {
      id: null,
    };
  }
}

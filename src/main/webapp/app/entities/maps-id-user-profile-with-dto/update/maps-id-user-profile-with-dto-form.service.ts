import { Service } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMapsIdUserProfileWithDTO, NewMapsIdUserProfileWithDTO } from '../maps-id-user-profile-with-dto.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMapsIdUserProfileWithDTO for edit and NewMapsIdUserProfileWithDTOFormGroupInput for create.
 */
type MapsIdUserProfileWithDTOFormGroupInput = IMapsIdUserProfileWithDTO | PartialWithRequiredKeyOf<NewMapsIdUserProfileWithDTO>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMapsIdUserProfileWithDTO | NewMapsIdUserProfileWithDTO> = Omit<T, 'dateOfBirth'> & {
  dateOfBirth?: string | null;
};

type MapsIdUserProfileWithDTOFormRawValue = FormValueOf<IMapsIdUserProfileWithDTO>;

type NewMapsIdUserProfileWithDTOFormRawValue = FormValueOf<NewMapsIdUserProfileWithDTO>;

type MapsIdUserProfileWithDTOFormDefaults = Pick<NewMapsIdUserProfileWithDTO, 'id' | 'dateOfBirth'>;

type MapsIdUserProfileWithDTOFormGroupContent = {
  id: FormControl<MapsIdUserProfileWithDTOFormRawValue['id'] | NewMapsIdUserProfileWithDTO['id']>;
  dateOfBirth: FormControl<MapsIdUserProfileWithDTOFormRawValue['dateOfBirth']>;
  user: FormControl<MapsIdUserProfileWithDTOFormRawValue['user']>;
};

export type MapsIdUserProfileWithDTOFormGroup = FormGroup<MapsIdUserProfileWithDTOFormGroupContent>;

@Service()
export class MapsIdUserProfileWithDTOFormService {
  createMapsIdUserProfileWithDTOFormGroup(
    mapsIdUserProfileWithDTO?: MapsIdUserProfileWithDTOFormGroupInput,
  ): MapsIdUserProfileWithDTOFormGroup {
    const mapsIdUserProfileWithDTORawValue = this.convertMapsIdUserProfileWithDTOToMapsIdUserProfileWithDTORawValue({
      ...this.getFormDefaults(),
      ...(mapsIdUserProfileWithDTO ?? { id: null }),
    });

    return new FormGroup<MapsIdUserProfileWithDTOFormGroupContent>({
      id: new FormControl(
        { value: mapsIdUserProfileWithDTORawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateOfBirth: new FormControl(mapsIdUserProfileWithDTORawValue.dateOfBirth),
      user: new FormControl(mapsIdUserProfileWithDTORawValue.user),
    });
  }

  getMapsIdUserProfileWithDTO(form: MapsIdUserProfileWithDTOFormGroup): IMapsIdUserProfileWithDTO | NewMapsIdUserProfileWithDTO {
    return this.convertMapsIdUserProfileWithDTORawValueToMapsIdUserProfileWithDTO(form.getRawValue());
  }

  resetForm(form: MapsIdUserProfileWithDTOFormGroup, mapsIdUserProfileWithDTO: MapsIdUserProfileWithDTOFormGroupInput): void {
    const mapsIdUserProfileWithDTORawValue = this.convertMapsIdUserProfileWithDTOToMapsIdUserProfileWithDTORawValue({
      ...this.getFormDefaults(),
      ...mapsIdUserProfileWithDTO,
    });
    form.reset({
      ...mapsIdUserProfileWithDTORawValue,
      id: { value: mapsIdUserProfileWithDTORawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): MapsIdUserProfileWithDTOFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateOfBirth: currentTime,
    };
  }

  private convertMapsIdUserProfileWithDTORawValueToMapsIdUserProfileWithDTO(
    rawMapsIdUserProfileWithDTO: MapsIdUserProfileWithDTOFormRawValue | NewMapsIdUserProfileWithDTOFormRawValue,
  ): IMapsIdUserProfileWithDTO | NewMapsIdUserProfileWithDTO {
    return {
      ...rawMapsIdUserProfileWithDTO,
      dateOfBirth: dayjs(rawMapsIdUserProfileWithDTO.dateOfBirth, DATE_TIME_FORMAT),
    };
  }

  private convertMapsIdUserProfileWithDTOToMapsIdUserProfileWithDTORawValue(
    mapsIdUserProfileWithDTO: IMapsIdUserProfileWithDTO | (Partial<NewMapsIdUserProfileWithDTO> & MapsIdUserProfileWithDTOFormDefaults),
  ): MapsIdUserProfileWithDTOFormRawValue | PartialWithRequiredKeyOf<NewMapsIdUserProfileWithDTOFormRawValue> {
    return {
      ...mapsIdUserProfileWithDTO,
      dateOfBirth: mapsIdUserProfileWithDTO.dateOfBirth ? mapsIdUserProfileWithDTO.dateOfBirth.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}

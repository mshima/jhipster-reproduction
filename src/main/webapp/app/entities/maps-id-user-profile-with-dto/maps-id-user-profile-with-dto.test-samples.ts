import dayjs from 'dayjs/esm';

import { IMapsIdUserProfileWithDTO, NewMapsIdUserProfileWithDTO } from './maps-id-user-profile-with-dto.model';

export const sampleWithRequiredData: IMapsIdUserProfileWithDTO = {
  id: 9734,
};

export const sampleWithPartialData: IMapsIdUserProfileWithDTO = {
  id: 9060,
};

export const sampleWithFullData: IMapsIdUserProfileWithDTO = {
  id: 4632,
  dateOfBirth: dayjs('2019-01-16T09:43'),
};

export const sampleWithNewData: NewMapsIdUserProfileWithDTO = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

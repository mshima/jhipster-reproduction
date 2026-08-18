import { IEntityWithDTO, NewEntityWithDTO } from './entity-with-dto.model';

export const sampleWithRequiredData: IEntityWithDTO = {
  id: 12612,
};

export const sampleWithPartialData: IEntityWithDTO = {
  id: 527,
};

export const sampleWithFullData: IEntityWithDTO = {
  id: 6569,
  emma: 'doubtfully but allegation',
};

export const sampleWithNewData: NewEntityWithDTO = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

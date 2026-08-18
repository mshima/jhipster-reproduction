import { IEntityWithServiceImplAndDTO, NewEntityWithServiceImplAndDTO } from './entity-with-service-impl-and-dto.model';

export const sampleWithRequiredData: IEntityWithServiceImplAndDTO = {
  id: 10776,
};

export const sampleWithPartialData: IEntityWithServiceImplAndDTO = {
  id: 18599,
  louis: 'knit governance versus',
};

export const sampleWithFullData: IEntityWithServiceImplAndDTO = {
  id: 7063,
  louis: 'gee',
};

export const sampleWithNewData: NewEntityWithServiceImplAndDTO = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

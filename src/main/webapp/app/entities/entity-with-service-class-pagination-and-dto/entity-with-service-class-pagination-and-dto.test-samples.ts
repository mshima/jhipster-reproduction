import {
  IEntityWithServiceClassPaginationAndDTO,
  NewEntityWithServiceClassPaginationAndDTO,
} from './entity-with-service-class-pagination-and-dto.model';

export const sampleWithRequiredData: IEntityWithServiceClassPaginationAndDTO = {
  id: 3884,
};

export const sampleWithPartialData: IEntityWithServiceClassPaginationAndDTO = {
  id: 5543,
  lena: 'eek requite mmm',
};

export const sampleWithFullData: IEntityWithServiceClassPaginationAndDTO = {
  id: 8552,
  lena: 'fort er peppery',
};

export const sampleWithNewData: NewEntityWithServiceClassPaginationAndDTO = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

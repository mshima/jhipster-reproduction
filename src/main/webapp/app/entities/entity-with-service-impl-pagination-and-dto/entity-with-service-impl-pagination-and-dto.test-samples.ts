import {
  IEntityWithServiceImplPaginationAndDTO,
  NewEntityWithServiceImplPaginationAndDTO,
} from './entity-with-service-impl-pagination-and-dto.model';

export const sampleWithRequiredData: IEntityWithServiceImplPaginationAndDTO = {
  id: 30173,
};

export const sampleWithPartialData: IEntityWithServiceImplPaginationAndDTO = {
  id: 30670,
};

export const sampleWithFullData: IEntityWithServiceImplPaginationAndDTO = {
  id: 27691,
  theo: 'jury over likewise',
};

export const sampleWithNewData: NewEntityWithServiceImplPaginationAndDTO = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

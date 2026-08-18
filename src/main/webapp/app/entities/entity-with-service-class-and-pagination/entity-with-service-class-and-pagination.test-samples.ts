import {
  IEntityWithServiceClassAndPagination,
  NewEntityWithServiceClassAndPagination,
} from './entity-with-service-class-and-pagination.model';

export const sampleWithRequiredData: IEntityWithServiceClassAndPagination = {
  id: 18236,
};

export const sampleWithPartialData: IEntityWithServiceClassAndPagination = {
  id: 3477,
  enzo: 'vicinity allegation coolly',
};

export const sampleWithFullData: IEntityWithServiceClassAndPagination = {
  id: 17629,
  enzo: 'practical',
};

export const sampleWithNewData: NewEntityWithServiceClassAndPagination = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

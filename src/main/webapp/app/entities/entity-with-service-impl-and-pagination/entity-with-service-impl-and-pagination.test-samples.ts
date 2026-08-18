import {
  IEntityWithServiceImplAndPagination,
  NewEntityWithServiceImplAndPagination,
} from './entity-with-service-impl-and-pagination.model';

export const sampleWithRequiredData: IEntityWithServiceImplAndPagination = {
  id: 29572,
};

export const sampleWithPartialData: IEntityWithServiceImplAndPagination = {
  id: 19776,
  hugo: 'an that',
};

export const sampleWithFullData: IEntityWithServiceImplAndPagination = {
  id: 9374,
  hugo: 'upwardly hamburger storyboard',
};

export const sampleWithNewData: NewEntityWithServiceImplAndPagination = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

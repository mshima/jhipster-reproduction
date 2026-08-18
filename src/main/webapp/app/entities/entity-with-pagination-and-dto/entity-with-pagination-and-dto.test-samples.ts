import { IEntityWithPaginationAndDTO, NewEntityWithPaginationAndDTO } from './entity-with-pagination-and-dto.model';

export const sampleWithRequiredData: IEntityWithPaginationAndDTO = {
  id: 25768,
};

export const sampleWithPartialData: IEntityWithPaginationAndDTO = {
  id: 6130,
  lea: 'wherever',
};

export const sampleWithFullData: IEntityWithPaginationAndDTO = {
  id: 13234,
  lea: 'abandoned',
};

export const sampleWithNewData: NewEntityWithPaginationAndDTO = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

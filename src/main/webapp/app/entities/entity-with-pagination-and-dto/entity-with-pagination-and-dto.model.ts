export interface IEntityWithPaginationAndDTO {
  id: number;
  lea?: string | null;
}

export type NewEntityWithPaginationAndDTO = Omit<IEntityWithPaginationAndDTO, 'id'> & { id: null };

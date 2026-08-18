export interface IEntityWithServiceClassPaginationAndDTO {
  id: number;
  lena?: string | null;
}

export type NewEntityWithServiceClassPaginationAndDTO = Omit<IEntityWithServiceClassPaginationAndDTO, 'id'> & { id: null };

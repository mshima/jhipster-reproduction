export interface IEntityWithServiceImplPaginationAndDTO {
  id: number;
  theo?: string | null;
}

export type NewEntityWithServiceImplPaginationAndDTO = Omit<IEntityWithServiceImplPaginationAndDTO, 'id'> & { id: null };

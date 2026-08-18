export interface IEntityWithServiceClassAndPagination {
  id: number;
  enzo?: string | null;
}

export type NewEntityWithServiceClassAndPagination = Omit<IEntityWithServiceClassAndPagination, 'id'> & { id: null };

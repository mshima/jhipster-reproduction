export interface IEntityWithServiceImplAndPagination {
  id: number;
  hugo?: string | null;
}

export type NewEntityWithServiceImplAndPagination = Omit<IEntityWithServiceImplAndPagination, 'id'> & { id: null };

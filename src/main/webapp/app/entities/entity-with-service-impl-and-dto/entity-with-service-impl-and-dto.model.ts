export interface IEntityWithServiceImplAndDTO {
  id: number;
  louis?: string | null;
}

export type NewEntityWithServiceImplAndDTO = Omit<IEntityWithServiceImplAndDTO, 'id'> & { id: null };

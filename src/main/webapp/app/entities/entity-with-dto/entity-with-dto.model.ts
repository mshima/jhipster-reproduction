export interface IEntityWithDTO {
  id: number;
  emma?: string | null;
}

export type NewEntityWithDTO = Omit<IEntityWithDTO, 'id'> & { id: null };

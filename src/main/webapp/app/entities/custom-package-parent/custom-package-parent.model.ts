export interface ICustomPackageParent {
  id: number;
  parentName?: string | null;
}

export type NewCustomPackageParent = Omit<ICustomPackageParent, 'id'> & { id: null };

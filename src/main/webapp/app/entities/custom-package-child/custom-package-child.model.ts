import { ICustomPackageParent } from 'app/entities/custom-package-parent/custom-package-parent.model';
import { IUser } from 'app/entities/user/user.model';

export interface ICustomPackageChild {
  id: number;
  childName?: string | null;
  user?: Pick<IUser, 'id'> | null;
  customPackageParent?: Pick<ICustomPackageParent, 'id'> | null;
}

export type NewCustomPackageChild = Omit<ICustomPackageChild, 'id'> & { id: null };

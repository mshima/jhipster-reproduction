import { IUser } from 'app/shared/model/user.model';

export interface INotification {
  id?: number;
  title?: string;
  user?: IUser | null;
}

export const defaultValue: Readonly<INotification> = {};

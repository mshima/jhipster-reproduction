import { type IUser } from '@/shared/model/user.model';

export interface INotification {
  id?: number;
  title?: string;
  user?: IUser | null;
}

export class Notification implements INotification {
  constructor(
    public id?: number,
    public title?: string,
    public user?: IUser | null,
  ) {}
}

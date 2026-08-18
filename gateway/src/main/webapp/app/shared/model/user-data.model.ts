export interface IUserData {
  id?: number;
  address?: string | null;
}

export class UserData implements IUserData {
  constructor(
    public id?: number,
    public address?: string | null,
  ) {}
}

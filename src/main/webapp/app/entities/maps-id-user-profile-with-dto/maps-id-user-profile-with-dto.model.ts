import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IMapsIdUserProfileWithDTO {
  id: number;
  dateOfBirth?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewMapsIdUserProfileWithDTO = Omit<IMapsIdUserProfileWithDTO, 'id'> & { id: null };

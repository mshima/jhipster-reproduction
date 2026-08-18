import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IOperation } from 'app/entities/test-root/operation/operation.model';
import { BankAccountType } from 'app/entities/enumerations/bank-account-type.model';

export interface IBankAccountMySuffix {
  id: number;
  name?: string | null;
  guid?: string | null;
  bankNumber?: number | null;
  agencyNumber?: number | null;
  lastOperationDuration?: number | null;
  meanOperationDuration?: number | null;
  meanQueueDuration?: string | null;
  balance?: number | null;
  openingDay?: dayjs.Dayjs | null;
  lastOperationDate?: dayjs.Dayjs | null;
  active?: boolean | null;
  accountType?: keyof typeof BankAccountType | null;
  attachment?: string | null;
  attachmentContentType?: string | null;
  description?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewBankAccountMySuffix = Omit<IBankAccountMySuffix, 'id'> & { id: null };

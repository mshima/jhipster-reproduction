import dayjs from 'dayjs/esm';

import { IBankAccountMySuffix } from 'app/entities/test-root/bank-account-my-suffix/bank-account-my-suffix.model';
import { ILabel } from 'app/entities/test-root/label/label.model';

export interface IOperation {
  id: number;
  date?: dayjs.Dayjs | null;
  description?: string | null;
  amount?: number | null;
  bankAccount?: IBankAccountMySuffix | null;
  labels?: ILabel[] | null;
}

export type NewOperation = Omit<IOperation, 'id'> & { id: null };

import { IOperation } from 'app/entities/test-root/operation/operation.model';

export interface ILabel {
  id: number;
  labelName?: string | null;
  operations?: IOperation[] | null;
}

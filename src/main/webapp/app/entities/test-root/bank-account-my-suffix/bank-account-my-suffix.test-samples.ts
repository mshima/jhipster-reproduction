import dayjs from 'dayjs/esm';

import { BankAccountType } from 'app/entities/enumerations/bank-account-type.model';

import { IBankAccountMySuffix, NewBankAccountMySuffix } from './bank-account-my-suffix.model';

export const sampleWithRequiredData: IBankAccountMySuffix = {
  id: 17167,
  name: 'um via',
  balance: 2293.59,
};

export const sampleWithPartialData: IBankAccountMySuffix = {
  id: 14763,
  name: 'whose',
  agencyNumber: 23808,
  lastOperationDuration: 8250.29,
  meanQueueDuration: '15563',
  balance: 5608.39,
  active: false,
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IBankAccountMySuffix = {
  id: 13483,
  name: 'gigantic',
  guid: 'ca608073-0d62-46c3-b1ee-97681868641f',
  bankNumber: 888,
  agencyNumber: 4285,
  lastOperationDuration: 5575.41,
  meanOperationDuration: 22816.15,
  meanQueueDuration: '26181',
  balance: 1207.54,
  openingDay: dayjs('2020-08-04'),
  lastOperationDate: dayjs('2020-08-04T05:58'),
  active: true,
  accountType: 'LOAN',
  attachment: '../fake-data/blob/hipster.png',
  attachmentContentType: 'unknown',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewBankAccountMySuffix = {
  name: 'clonk porter',
  balance: 26302.97,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

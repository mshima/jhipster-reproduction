import { MyEnumA } from 'app/entities/enumerations/my-enum-a.model';
import { MyEnumB } from 'app/entities/enumerations/my-enum-b.model';
import { MyEnumC } from 'app/entities/enumerations/my-enum-c.model';
import { MyEnumD } from 'app/entities/enumerations/my-enum-d.model';
import { MyEnumE } from 'app/entities/enumerations/my-enum-e.model';

import { IFieldTestEnumWithValue, NewFieldTestEnumWithValue } from './field-test-enum-with-value.model';

export const sampleWithRequiredData: IFieldTestEnumWithValue = {
  id: 32053,
};

export const sampleWithPartialData: IFieldTestEnumWithValue = {
  id: 26086,
  myFieldA: 'AAA',
  myFieldC: 'BBB',
  myFieldD: 'AAA',
  myFieldE: 'AAA',
};

export const sampleWithFullData: IFieldTestEnumWithValue = {
  id: 9323,
  myFieldA: 'AAA',
  myFieldB: 'AAA',
  myFieldC: 'AAA',
  myFieldD: 'AAA',
  myFieldE: 'BBB',
};

export const sampleWithNewData: NewFieldTestEnumWithValue = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

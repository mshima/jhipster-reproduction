import { MyEnumA } from 'app/entities/enumerations/my-enum-a.model';
import { MyEnumB } from 'app/entities/enumerations/my-enum-b.model';
import { MyEnumC } from 'app/entities/enumerations/my-enum-c.model';
import { MyEnumD } from 'app/entities/enumerations/my-enum-d.model';
import { MyEnumE } from 'app/entities/enumerations/my-enum-e.model';

export interface IFieldTestEnumWithValue {
  id: number;
  myFieldA?: keyof typeof MyEnumA | null;
  myFieldB?: keyof typeof MyEnumB | null;
  myFieldC?: keyof typeof MyEnumC | null;
  myFieldD?: keyof typeof MyEnumD | null;
  myFieldE?: keyof typeof MyEnumE | null;
}

export type NewFieldTestEnumWithValue = Omit<IFieldTestEnumWithValue, 'id'> & { id: null };

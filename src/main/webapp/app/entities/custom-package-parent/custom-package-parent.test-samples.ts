import { ICustomPackageParent, NewCustomPackageParent } from './custom-package-parent.model';

export const sampleWithRequiredData: ICustomPackageParent = {
  id: 1006,
};

export const sampleWithPartialData: ICustomPackageParent = {
  id: 13095,
  parentName: 'reassuringly goodwill',
};

export const sampleWithFullData: ICustomPackageParent = {
  id: 2278,
  parentName: 'before tidy',
};

export const sampleWithNewData: NewCustomPackageParent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

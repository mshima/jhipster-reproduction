import { ICustomPackageChild, NewCustomPackageChild } from './custom-package-child.model';

export const sampleWithRequiredData: ICustomPackageChild = {
  id: 17578,
};

export const sampleWithPartialData: ICustomPackageChild = {
  id: 30533,
  childName: 'pantyhose tackle make',
};

export const sampleWithFullData: ICustomPackageChild = {
  id: 10731,
  childName: 'ack',
};

export const sampleWithNewData: NewCustomPackageChild = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

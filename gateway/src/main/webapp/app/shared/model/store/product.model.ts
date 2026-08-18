export interface IProduct {
  id?: number;
  title?: string;
  price?: number;
  imageContentType?: string | null;
  image?: string | null;
}

export const defaultValue: Readonly<IProduct> = {};

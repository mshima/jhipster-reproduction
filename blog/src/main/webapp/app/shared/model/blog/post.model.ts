import { type IBlog } from '@/shared/model/blog/blog.model';
import { type ITag } from '@/shared/model/blog/tag.model';

export interface IPost {
  id?: number;
  title?: string;
  blog?: IBlog | null;
  tags?: ITag[] | null;
}

export class Post implements IPost {
  constructor(
    public id?: number,
    public title?: string,
    public blog?: IBlog | null,
    public tags?: ITag[] | null,
  ) {}
}

import { defineComponent, provide } from 'vue';

import UserService from '@/entities/user/user.service';

import BlogService from './blog/blog/blog.service';
import PostService from './blog/post/post.service';
import TagService from './blog/tag/tag.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('blogService', () => new BlogService());
    provide('postService', () => new PostService());
    provide('tagService', () => new TagService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});

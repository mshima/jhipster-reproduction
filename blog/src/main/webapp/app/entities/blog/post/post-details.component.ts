import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IPost } from '@/shared/model/blog/post.model';

import PostService from './post.service';

export default defineComponent({
  name: 'PostDetails',
  setup() {
    const postService = inject('postService', () => new PostService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const post: Ref<IPost> = ref({});

    const retrievePost = async postId => {
      try {
        const res = await postService().find(postId);
        post.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.postId) {
      retrievePost(route.params.postId);
    }

    return {
      alertService,
      post,

      previousState,
      t$: useI18n().t,
    };
  },
});

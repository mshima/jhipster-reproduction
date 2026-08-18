<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="blogApp.blogPost.home.createOrEditLabel" data-cy="PostCreateUpdateHeading">
          {{ t$('blogApp.blogPost.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="post.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="post.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="post">{{ t$('blogApp.blogPost.title') }}</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="post-title"
              data-cy="title"
              :class="{ valid: !v$.title.$invalid, invalid: v$.title.$invalid }"
              v-model="v$.title.$model"
              required
            />
            <div v-if="v$.title.$anyDirty && v$.title.$invalid">
              <small class="form-text text-danger" v-for="error of v$.title.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="post">{{ t$('blogApp.blogPost.blog') }}</label>
            <select class="form-control" id="post-blog" data-cy="blog" name="blog" v-model="post.blog">
              <option :value="null"></option>
              <option
                :value="post.blog && blogOption.id === post.blog.id ? post.blog : blogOption"
                v-for="blogOption in blogs"
                :key="blogOption.id"
              >
                {{ blogOption.name }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label for="post">{{ t$('blogApp.blogPost.tag') }}</label>
            <select
              class="form-control"
              id="post-tags"
              data-cy="tag"
              multiple
              name="tag"
              v-if="post.tags !== undefined"
              v-model="post.tags"
            >
              <option :value="getSelected(post.tags, tagOption, 'id')" v-for="tagOption in tags" :key="tagOption.id">
                {{ tagOption.name }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.cancel') }}</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.save') }}</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./post-update.component.ts"></script>

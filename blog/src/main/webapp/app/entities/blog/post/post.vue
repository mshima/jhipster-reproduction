<template>
  <div>
    <h2 id="page-heading" data-cy="PostHeading">
      <span id="post">{{ t$('blogApp.blogPost.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('blogApp.blogPost.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'PostCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-post">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('blogApp.blogPost.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && posts?.length === 0">
      <span>{{ t$('blogApp.blogPost.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="posts?.length > 0">
      <table class="table table-striped" aria-describedby="posts">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ t$('global.field.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('title')">
              <span>{{ t$('blogApp.blogPost.title') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'title'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('blog.name')">
              <span>{{ t$('blogApp.blogPost.blog') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'blog.name'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="post in posts" :key="post.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PostView', params: { postId: post.id } }">{{ post.id }}</router-link>
            </td>
            <td>{{ post.title }}</td>
            <td>
              <div v-if="post.blog">
                <router-link :to="{ name: 'BlogView', params: { blogId: post.blog.id } }">{{ post.blog.name }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'PostView', params: { postId: post.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PostEdit', params: { postId: post.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(post)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">{{ t$('entity.action.delete') }}</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
        <span ref="infiniteScrollEl"></span>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #title>
        <span id="blogApp.blogPost.delete.question" data-cy="postDeleteDialogHeading">{{ t$('entity.delete.title') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-post-heading">{{ t$('blogApp.blogPost.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-post"
            data-cy="entityConfirmDeleteButton"
            @click="removePost"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./post.component.ts"></script>

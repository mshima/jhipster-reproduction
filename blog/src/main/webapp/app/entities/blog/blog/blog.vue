<template>
  <div>
    <h2 id="page-heading" data-cy="BlogHeading">
      <span id="blog">{{ t$('blogApp.blogBlog.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('blogApp.blogBlog.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'BlogCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-blog">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('blogApp.blogBlog.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && blogs?.length === 0">
      <span>{{ t$('blogApp.blogBlog.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="blogs?.length > 0">
      <table class="table table-striped" aria-describedby="blogs">
        <thead>
          <tr>
            <th scope="col">
              <span>{{ t$('global.field.id') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('blogApp.blogBlog.name') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('blogApp.blogBlog.handle') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('blogApp.blogBlog.user') }}</span>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="blog in blogs" :key="blog.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'BlogView', params: { blogId: blog.id } }">{{ blog.id }}</router-link>
            </td>
            <td>{{ blog.name }}</td>
            <td>{{ blog.handle }}</td>
            <td>
              {{ blog.user ? blog.user.login : '' }}
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'BlogView', params: { blogId: blog.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'BlogEdit', params: { blogId: blog.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(blog)"
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
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #title>
        <span id="blogApp.blogBlog.delete.question" data-cy="blogDeleteDialogHeading">{{ t$('entity.delete.title') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-blog-heading">{{ t$('blogApp.blogBlog.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-blog"
            data-cy="entityConfirmDeleteButton"
            @click="removeBlog"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./blog.component.ts"></script>

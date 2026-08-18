<template>
  <div>
    <h2 id="page-heading" data-cy="NotificationHeading">
      <span id="notification">{{ t$('notificationApp.notificationNotification.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('notificationApp.notificationNotification.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'NotificationCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-notification"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('notificationApp.notificationNotification.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && notifications?.length === 0">
      <span>{{ t$('notificationApp.notificationNotification.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="notifications?.length > 0">
      <table class="table table-striped" aria-describedby="notifications">
        <thead>
          <tr>
            <th scope="col">
              <span>{{ t$('global.field.id') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('notificationApp.notificationNotification.title') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('notificationApp.notificationNotification.user') }}</span>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="notification in notifications" :key="notification.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'NotificationView', params: { notificationId: notification.id } }">{{
                notification.id
              }}</router-link>
            </td>
            <td>{{ notification.title }}</td>
            <td>
              {{ notification.user ? notification.user.login : '' }}
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'NotificationView', params: { notificationId: notification.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'NotificationEdit', params: { notificationId: notification.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(notification)"
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
        <span id="notificationApp.notificationNotification.delete.question" data-cy="notificationDeleteDialogHeading">{{
          t$('entity.delete.title')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-notification-heading">{{ t$('notificationApp.notificationNotification.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-notification"
            data-cy="entityConfirmDeleteButton"
            @click="removeNotification"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./notification.component.ts"></script>

<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="notification">
        <h2 class="jh-entity-heading" data-cy="notificationDetailsHeading">
          <span>{{ t$('notificationApp.notificationNotification.detail.title') }}</span> {{ notification.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('notificationApp.notificationNotification.title') }}</span>
          </dt>
          <dd>
            <span>{{ notification.title }}</span>
          </dd>
          <dt>
            <span>{{ t$('notificationApp.notificationNotification.user') }}</span>
          </dt>
          <dd>
            {{ notification.user ? notification.user.login : '' }}
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link
          v-if="notification.id"
          :to="{ name: 'NotificationEdit', params: { notificationId: notification.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./notification-details.component.ts"></script>

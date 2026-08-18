<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="notificationApp.notificationNotification.home.createOrEditLabel" data-cy="NotificationCreateUpdateHeading">
          {{ t$('notificationApp.notificationNotification.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="notification.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="notification.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="notification">{{ t$('notificationApp.notificationNotification.title') }}</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="notification-title"
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
            <label class="form-control-label" for="notification">{{ t$('notificationApp.notificationNotification.user') }}</label>
            <select class="form-control" id="notification-user" data-cy="user" name="user" v-model="notification.user">
              <option :value="null"></option>
              <option
                :value="notification.user && userOption.id === notification.user.id ? notification.user : userOption"
                v-for="userOption in users"
                :key="userOption.id"
              >
                {{ userOption.login }}
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
<script lang="ts" src="./notification-update.component.ts"></script>

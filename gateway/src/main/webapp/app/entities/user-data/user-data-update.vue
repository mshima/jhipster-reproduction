<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="gatewayApp.userData.home.createOrEditLabel" data-cy="UserDataCreateUpdateHeading">
          {{ t$('gatewayApp.userData.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="userData.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="userData.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="user-data">{{ t$('gatewayApp.userData.address') }}</label>
            <input
              type="text"
              class="form-control"
              name="address"
              id="user-data-address"
              data-cy="address"
              :class="{ valid: !v$.address.$invalid, invalid: v$.address.$invalid }"
              v-model="v$.address.$model"
            />
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
<script lang="ts" src="./user-data-update.component.ts"></script>

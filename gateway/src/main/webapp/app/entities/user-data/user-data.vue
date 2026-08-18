<template>
  <div>
    <h2 id="page-heading" data-cy="UserDataHeading">
      <span id="user-data">{{ t$('gatewayApp.userData.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('gatewayApp.userData.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'UserDataCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-user-data"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('gatewayApp.userData.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && userDatas?.length === 0">
      <span>{{ t$('gatewayApp.userData.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="userDatas?.length > 0">
      <table class="table table-striped" aria-describedby="userDatas">
        <thead>
          <tr>
            <th scope="col">
              <span>{{ t$('global.field.id') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('gatewayApp.userData.address') }}</span>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="userData in userDatas" :key="userData.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'UserDataView', params: { userDataId: userData.id } }">{{ userData.id }}</router-link>
            </td>
            <td>{{ userData.address }}</td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'UserDataView', params: { userDataId: userData.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'UserDataEdit', params: { userDataId: userData.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(userData)"
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
        <span id="gatewayApp.userData.delete.question" data-cy="userDataDeleteDialogHeading">{{ t$('entity.delete.title') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-userData-heading">{{ t$('gatewayApp.userData.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-userData"
            data-cy="entityConfirmDeleteButton"
            @click="removeUserData"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./user-data.component.ts"></script>

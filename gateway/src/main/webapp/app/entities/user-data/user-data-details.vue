<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="userData">
        <h2 class="jh-entity-heading" data-cy="userDataDetailsHeading">
          <span>{{ t$('gatewayApp.userData.detail.title') }}</span> {{ userData.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('gatewayApp.userData.address') }}</span>
          </dt>
          <dd>
            <span>{{ userData.address }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link v-if="userData.id" :to="{ name: 'UserDataEdit', params: { userDataId: userData.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./user-data-details.component.ts"></script>

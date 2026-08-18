<template>
  <div>
    <b-dropdown-item to="/user-data">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.userData') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/product">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.storeProduct') }}</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>

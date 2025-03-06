<template>
  <component :is="layout">
    <router-view />
  </component>
</template>

<script>
import DefaultLayout from './layouts/DefaultLayout.vue';
import MinimalLayout from './layouts/MinimalLayout.vue';

export default {
  components: {
    DefaultLayout,
    MinimalLayout,
  },
  computed: {
    layout() {
      return `${this.$route.meta.layout || 'default'}-layout`;
    },
  },
};
</script>

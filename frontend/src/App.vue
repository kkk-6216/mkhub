<template>
  <component :is="layout">
    <router-view />
  </component>
</template>

<script>
import DefaultLayout from './layouts/DefaultLayout.vue';
import MinimalLayout from './layouts/MinimalLayout.vue';
import ProfileLayout from './layouts/ProfileLayout.vue';

export default {
  components: {
    DefaultLayout,
    MinimalLayout,
    ProfileLayout,
  },
  computed: {
    layout() {
      return `${this.$route.meta.layout || 'default'}-layout`;
    },
  },
};
</script>

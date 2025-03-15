<template>
  <component :is="layout">
    <router-view />
  </component>
</template>

<script>
import DefaultLayout from './layouts/DefaultLayout.vue';
import AdminLayout from './layouts/AdminLayout.vue';
import MinimalLayout from './layouts/MinimalLayout.vue';
import ProfileLayout from './layouts/ProfileLayout.vue';

export default {
  computed: {
    layout() {
      const layouts = {
        default: DefaultLayout,
        admin: AdminLayout,
        minimal: MinimalLayout,
        profile: ProfileLayout,
      };
      return layouts[this.$route.meta.layout] || DefaultLayout;
    }
  }
};
</script>

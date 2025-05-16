<template>
  <div class="flex h-screen">
    <!-- Боковая панель -->
    <ModeratorSidebar class="w-32" />

    <!-- Основной контент -->
    <div class="flex-1 w-full overflow-x-hidden overflow-y-auto p-0">
      <router-view />
    </div>
  </div>
</template>

<script>
import ModeratorSidebar from '../components/sidebar/ModeratorSidebar.vue';

export default {
  name: 'ModeratorLayout',
  components: {
    ModeratorSidebar
  },
  // Нет setup(), так как нет логики, требующей Composition API
};
</script>
<template>
  <div class="flex h-screen ">
    <!-- Боковая панель -->
    <Sidebar class="min-w-[8rem] max-w-[10rem] h-full" />

    <!-- Основной контент -->
    <div class="flex-1 w-full min-w-0 overflow-x-hidden overflow-y-auto p-0 relative">
      <router-view />
    </div>
  </div>
</template>

<script>
import Sidebar from '../components/sidebar/Sidebar.vue';

export default {
  name: 'DefaultLayout',
  components: {
   Sidebar
  },
  // Нет setup(), так как нет логики, требующей Composition API
};
</script>
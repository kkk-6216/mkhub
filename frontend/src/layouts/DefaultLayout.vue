<template>
  <div class="flex h-screen bg-gray-100">
    <!-- Боковая панель -->
    <Sidebar class="w-32" />

    <!-- Основной контент -->
    <div class="flex-1 w-full overflow-x-hidden overflow-y-auto p-0">
      <router-view />
    </div>
  </div>
</template>

<script>
import Sidebar from '../components/Sidebar.vue';

export default {
  name: 'DefaultLayout',
  components: {
    Sidebar,
  },
  // Нет setup(), так как нет логики, требующей Composition API
};
</script>

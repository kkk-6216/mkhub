<template>
  <div class="flex h-screen">
    <!-- Боковая панель -->
    <AdminSidebar class="w-32" />

    <!-- Основной контент -->
    <div class="flex-1 w-full overflow-x-hidden overflow-y-auto p-0">
      <router-view />
    </div>
  </div>
</template>

<script>
import AdminSidebar from '../components/Sidebar/AdminSidebar.vue';

export default {
  name: 'AdminLayout',
  components: {
    AdminSidebar
  },
  // Нет setup(), так как нет логики, требующей Composition API
};
</script>
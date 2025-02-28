<template>
  <div class="h-screen bg-gray-100">
    <!-- Основной контент (без боковой панели) -->
    <div class="flex-1 overflow-x-hidden overflow-y-auto p-4">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  name: 'MinimalLayout',
  // Нет setup(), так как нет логики, требующей Composition API
};
</script>
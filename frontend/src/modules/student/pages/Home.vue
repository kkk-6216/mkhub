<template>
  <div class="relative flex items-center justify-center h-screen"> <!-- h-screen для увеличения высоты на весь экран -->
    <div class="text-center w-full">
      <h1 class="text-5xl font-bold text-dark mb-4">Добро пожаловать!</h1>
      <p class="text-gray-600 text-lg p-4">Рады видеть вас.</p>
      <SearchComponent />
    </div>
  </div>
</template>

<script>

import SearchComponent from "@/modules/student/components/SearchComponent.vue";

export default {
  name: 'WelcomePage',
  components: {
    SearchComponent,
  },
};
</script>

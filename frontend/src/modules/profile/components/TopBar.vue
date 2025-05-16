<template>
  <div>
    <!-- Блок с аватаром и информацией о пользователе -->
    <div class="flex items-center">
      <img
          :src="avatar || 'https://www.svgrepo.com/show/452030/avatar-default.svg'"
          alt="Аватар"
          class="w-20 h-20 rounded-lg object-cover border-2 border-gray-200 mr-6"
      />
      <div>
        <h2 class="text-main text-2xl font-semibold">{{ username }}</h2>
        <p class="text-main">{{ userRole }}</p>
      </div>
    </div>

    <!-- Вкладки -->
    <div class="border-b border-gray-200">
      <nav class="-mb-px flex space-x-4 sm:space-x-8 overflow-x-auto">

        <router-link
            v-for="link in profileLinks"
            :key="link.path"
            :to="link.path"
            class="py-4 px-1 border-b-2 font-medium text-sm text-gray-500 whitespace-nowrap border-transparent hover:text-main hover:border-main"
            active-class="!border-main !text-main"
            :exact-active-class="link.exact ? '!border-main !text-main' : ''"
        >
          {{ link.label }}
        </router-link>

      </nav>
    </div>
  </div>
</template>

<script>
import {useAuthStore} from "@/store/auth.js";

export default {
  name: "TopBar",
  data() {
    return {
      profileLinks: [
        { path: "/profile", label: "Профиль", exact: true },
        { path: "/profile/my-details", label: "Мои данные"},
        { path: "/profile/password", label: "Пароль"},
        { path: "/profile/notifications", label: "Уведомления"},
      ],
    }
  },
  methods: {

  },
  computed: {
    user() {
      return this.authStore.user;
    },
    username() {
      return this.authStore.user?.sub || 'Имя пользователя';
    },
    userRole() {
      const roleMap = {
        ROLE_STUDENT: "Студент",
        ROLE_MODERATOR: "Модератор",
        ROLE_ADMIN: "Админ",
      };
      return roleMap[this.user?.role] || "Неизвестная роль";
    },
    avatar() {
      return this.authStore.userAvatar;
    }
  },
  beforeCreate() {
    this.authStore = useAuthStore();
  }
}
</script>

<style scoped>

</style>
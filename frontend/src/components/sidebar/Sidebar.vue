<template>
  <div class="h-screen flex flex-col overflow-hidden">
    <!-- sidebar -->
    <aside
        class="border-r border-[#eaeaea] fixed left-0 top-5 bottom-5 h-auto transition-all duration-300 ease-in-out flex flex-col"
        :class="{ 'w-64 px-5': !isCollapsed, 'w-16 px-3': isCollapsed }"
    >
      <!-- Logo -->

      <div
          @click="goTo('/home')"
          class="flex items-center  mb-0  pt-2 cursor-pointer"
          :class="{ 'justify-center pl-0': isCollapsed, 'pl-4': !isCollapsed }"
      >
        <span class="text-[20px] text-main uppercase font-bold">
          <span v-if="isCollapsed">MK</span>
          <span v-else>MKHUB</span>
        </span>
      </div>
      <!-- Navigation -->
      <nav class="flex-grow flex flex-col justify-center min-w-0 overflow-y-auto">
        <ul>
          <MenuItem
              v-for="item in filteredMenuItems"
              :key="item.to"
              :isCollapsed="isCollapsed"
              :isActive="isActive(item.to)"
              :iconClass="item.iconClass"
              :icon="item.icon"
              :label="item.label"
              @click="goTo(item.to)"
          >
            <template #icon>
              <component :is="item.icon"></component>
            </template>
          </MenuItem>
        </ul>
      </nav>

      <!-- Вход/Выход -->
      <div>
        <ul>
          <!-- Если пользователь НЕ аутентифицирован, показываем кнопку "Войти" -->
          <li v-if="!isAuthenticated">
            <DefaultButton
                label="Войти"
                variant="primary"
                fullWidth
                @click="goTo('/login')"
            />
          </li>

          <li class="relative p-2" v-else>
            <transition name="fade">
              <div
                  v-if="isOpen"
                  class="absolute bottom-full left-0 w-full mb-2"
              >
                <DefaultButton
                    label="Выйти"
                    variant="danger"
                    fullWidth
                    @click="logout"
                />
              </div>
            </transition>

            <div class="flex items-center justify-between cursor-pointer">
              <div class="flex items-center space-x-3" @click="goTo('/profile')">
                <img
                    :src="avatar || 'https://www.svgrepo.com/show/452030/avatar-default.svg'"
                    alt="Avatar"
                    class="w-10 h-10 rounded-full"
                />
                <div>
                  <p class="text-dark font-semibold">{{ user.sub }}</p>
                  <p class="text-gray-500 text-sm">{{ userRole }}</p>
                </div>
              </div>

              <!-- Иконка стрелки -->

              <img @click="toggleDropdown" width="24" height="24" src="https://img.icons8.com/windows/32/menu-2.png" alt="menu-2"/>

            </div>
          </li>
        </ul>
      </div>
    </aside>
  </div>
</template>

<script>

import { useAuthStore } from '@/store/auth.js';
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import HomeIcon from "@/components/icons/SidebarIcons/HomeIcon.vue";
import CoursesIcon from "@/components/icons/SidebarIcons/CoursesIcon.vue";
import ResourcesIcon from "@/components/icons/SidebarIcons/ResourcesIcon.vue";
import MessagesIcon from "@/components/icons/SidebarIcons/MessagesIcon.vue";
import SettingsIcon from "@/components/icons/SidebarIcons/SettingsIcon.vue";
import { markRaw } from "vue";
import MenuItem from "@/components/items/MenuItem.vue";

export default {
  name: 'Sidebar',
  components: {
    DefaultButton,
    MenuItem,
  },

  data() {
    return {
      isCollapsed: false,
      screenWidth: window.innerWidth,
      initialWidth: window.innerWidth,
      isOpen: false,
      menuItems: [
        { to: '/home', label: 'Главная', icon: markRaw(HomeIcon), iconClass: 'mdi-view-dashboard', roles: ['ROLE_STUDENT', 'ROLE_ADMIN','ROLE_MODERATOR'], public: true },
        { to: '/courses', label: 'Курсы', icon: markRaw(CoursesIcon), iconClass: 'mdi-book-open-variant', roles: ['ROLE_STUDENT', 'ROLE_ADMIN','ROLE_MODERATOR'], public: true },
        { to: '/resources', label: 'Ресурсы', icon: markRaw(ResourcesIcon), iconClass: 'mdi-folder-open', roles: ['ROLE_STUDENT', 'ROLE_ADMIN','ROLE_MODERATOR'], public: true },
        { to: '/messages', label: 'Сообщения', icon: markRaw(MessagesIcon), iconClass: 'mdi-message', roles: ['ROLE_STUDENT', 'ROLE_ADMIN','ROLE_MODERATOR'] },
        { to: '/settings', label: 'Настройки', icon: markRaw(SettingsIcon), iconClass: 'mdi-cog', roles: ['ROLE_STUDENT'] },
        { to: '/admin/users', label: 'Панель управления', icon: markRaw(SettingsIcon), iconClass: 'mdi-cog', roles: ['ROLE_ADMIN'] },
        { to: '/moderator/dashboard', label: 'Панель управления', icon: markRaw(SettingsIcon), iconClass: 'mdi-cog', roles: ['ROLE_MODERATOR'] },
      ]
    };
  },

  mounted() {
    this.initialWidth = window.innerWidth;
    window.addEventListener('resize', this.updateScreenWidth);
    this.updateScreenWidth();
  },

  beforeUnmount() {
    window.removeEventListener('resize', this.updateScreenWidth);
  },

  methods: {
    updateScreenWidth() {
      this.screenWidth = window.innerWidth;
      this.isCollapsed = this.screenWidth < (this.initialWidth * 2) / 3;
    },
    goTo(path) {
      this.$router.push(path);
      this.isOpen = false;
    },
    isActive(path) {
      return this.currentPath === path;
    },
    toggleDropdown() {
      this.isOpen = !this.isOpen;
    },
    async logout() {
      await this.authStore.logout();
    },
  },

  computed: {
    currentPath() {
      return this.$route.path;
    },
    userRole() {
      const roleMap = {
        ROLE_STUDENT: "Студент",
        ROLE_MODERATOR: "Модератор",
        ROLE_ADMIN: "Админ",
      };
      return roleMap[this.user?.role] || "Неизвестная роль";
    },
    isAuthenticated() {
      return !!this.authStore.accessToken
    },
    user() {
      return this.authStore.user;
    },
    avatar() {
      return this.authStore.userAvatar;
    },
    filteredMenuItems() {
      if (!this.isAuthenticated) {
        return this.menuItems.filter(item => item.public === true);
      } else {
        if (this.user && this.user.role) {
          return this.menuItems.filter(item => item.roles && item.roles.includes(this.user.role));
        } else {
          return [];
        }
      }
    }
  },

  beforeCreate() {
    this.authStore = useAuthStore();
  },
};
</script>
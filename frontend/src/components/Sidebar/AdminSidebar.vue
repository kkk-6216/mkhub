<template>
  <div class="h-screen flex flex-col overflow-hidden">
    <!-- Sidebar -->
    <aside
        class="border-r border-[#eaeaea] fixed left-0 top-5 bottom-5 h-auto transition-all duration-300 ease-in-out flex flex-col"
        :class="{ 'w-64 px-5': !isCollapsed, 'w-16 px-3': isCollapsed }"
    >
      <!-- Logo -->

      <div @click="goTo('/admin/monitoring')"
           class="flex items-center  mb-0  pt-2 cursor-pointer"
           :class="{'justify-center pl-0': isCollapsed, 'pl-4': !isCollapsed }"
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
              v-for="item in menuItems"
              :key="item.to"
              :isCollapsed="isCollapsed"
              :isActive="isActive(item.to)"
              :iconClass="item.iconClass"
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
                    :src="user.avatar || 'https://www.svgrepo.com/show/452030/avatar-default.svg'"
                    alt="Avatar"
                    class="w-10 h-10 rounded-xl"
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

import MenuItem from '@/components/MenuItem.vue';
import { useAuthStore } from '@/store/auth.js';
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import MonitoringIcon from "@/components/icons/AdminSidebarIcons/MonitoringIcon.vue";
import UsersIcon from "@/components/icons/AdminSidebarIcons/UsersIcon.vue";
import ApiGatewayIcon from "@/components/icons/AdminSidebarIcons/ApiGatewayIcon.vue";
import LogoutIcon from "@/components/icons/AdminSidebarIcons/LogoutIcon.vue";
import {markRaw} from "vue";

export default {
  name: 'AdminSidebar',
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
        { to: '/admin/monitoring', label: 'Мониторинг', icon: markRaw(MonitoringIcon), iconClass: 'mdi-monitor' },
        { to: '/admin/users', label: 'Пользователи', icon: markRaw(UsersIcon), iconClass: 'mdi-account' },
        { to: '/admin/api-gateway', label: 'API Gateway', icon: markRaw(ApiGatewayIcon), iconClass: 'mdi-api' },
        { to: '/home', label: 'Выйти', icon: markRaw(LogoutIcon), iconClass: 'mdi-logout' }
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
    }
  },

  beforeCreate() {
    this.authStore = useAuthStore();
  },
};
</script>


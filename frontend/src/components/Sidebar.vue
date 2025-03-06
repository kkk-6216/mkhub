<template>
  <div class="h-screen flex flex-col overflow-hidden">
    <!-- Sidebar -->
    <aside
      class="bg-gray-100 shadow-lg fixed left-0 top-0 h-screen transition-all duration-300 flex flex-col overflow-hidden"
      :class="{ 'w-64 p-6': !isCollapsed, 'w-16 p-4': isCollapsed }"
    >
      <!-- Logo -->
      <div
        class="flex items-center space-x-3 mb-6 pb-12 pt-4"
        :class="{ 'justify-center pl-0': isCollapsed, 'pl-6': !isCollapsed }"
      >
        <img
          :src="registerArt"
          alt="Register Illustration"
          class="max-w-full rounded-2xl"
          :class="{ 'max-h-12': isCollapsed, 'max-h-24': !isCollapsed }"
        />
      </div>

      <!-- Navigation -->
      <nav class="flex-grow">
        <ul>
          <MenuItem
            :isCollapsed="isCollapsed"
            :isActive="$route.path === '/home'"
            iconClass="mdi-view-dashboard"
            to="/home"
            label="Главная"
          >
            <template #icon>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                class="size-6"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="m2.25 12 8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25"
                />
              </svg>
            </template>
          </MenuItem>

          <!-- Курсы -->
          <MenuItem
            :isCollapsed="isCollapsed"
            :isActive="$route.path === '/courses'"
            iconClass="mdi-book-open-variant"
            to="/courses"
            label="Курсы"
          >
            <template #icon>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                class="size-6"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M4.26 10.147a60.438 60.438 0 0 0-.491 6.347A48.62 48.62 0 0 1 12 20.904a48.62 48.62 0 0 1 8.232-4.41 60.46 60.46 0 0 0-.491-6.347m-15.482 0a50.636 50.636 0 0 0-2.658-.813A59.906 59.906 0 0 1 12 3.493a59.903 59.903 0 0 1 10.399 5.84c-.896.248-1.783.52-2.658.814m-15.482 0A50.717 50.717 0 0 1 12 13.489a50.702 50.702 0 0 1 7.74-3.342M6.75 15a.75.75 0 1 0 0-1.5.75.75 0 0 0 0 1.5Zm0 0v-3.675A55.378 55.378 0 0 1 12 8.443m-7.007 11.55A5.981 5.981 0 0 0 6.75 15.75v-1.5"
                />
              </svg>
            </template>
          </MenuItem>

          <!-- Ресурсы -->
          <MenuItem
            :isCollapsed="isCollapsed"
            :isActive="$route.path === '/resources'"
            iconClass="mdi-folder-open"
            to="/resources"
            label="Ресурсы"
          >
            <template #icon>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                class="size-6"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M2.25 12.75V12A2.25 2.25 0 0 1 4.5 9.75h15A2.25 2.25 0 0 1 21.75 12v.75m-8.69-6.44-2.12-2.12a1.5 1.5 0 0 0-1.061-.44H4.5A2.25 2.25 0 0 0 2.25 6v12a2.25 2.25 0 0 0 2.25 2.25h15A2.25 2.25 0 0 0 21.75 18V9a2.25 2.25 0 0 0-2.25-2.25h-5.379a1.5 1.5 0 0 1-1.06-.44Z"
                />
              </svg>
            </template>
          </MenuItem>

          <!-- Сообщения -->
          <MenuItem
            :isCollapsed="isCollapsed"
            :isActive="$route.path === '/messages'"
            iconClass="mdi-message"
            to="/messages"
            label="Сообщения"
          >
            <template #icon>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                class="size-6"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M12 20.25c4.97 0 9-3.694 9-8.25s-4.03-8.25-9-8.25S3 7.444 3 12c0 2.104.859 4.023 2.273 5.48.432.447.74 1.04.586 1.641a4.483 4.483 0 0 1-.923 1.785A5.969 5.969 0 0 0 6 21c1.282 0 2.47-.402 3.445-1.087.81.22 1.668.337 2.555.337Z"
                />
              </svg>
            </template>
          </MenuItem>

          <!-- Настройки -->
          <MenuItem
            :isCollapsed="isCollapsed"
            :isActive="$route.path === '/settings'"
            iconClass="mdi-cog"
            to="/settings"
            label="Настройки"
          >
            <template #icon>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                class="size-6"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M9.594 3.94c.09-.542.56-.94 1.11-.94h2.593c.55 0 1.02.398 1.11.94l.213 1.281c.063.374.313.686.645.87.074.04.147.083.22.127.325.196.72.257 1.075.124l1.217-.456a1.125 1.125 0 0 1 1.37.49l1.296 2.247a1.125 1.125 0 0 1-.26 1.431l-1.003.827c-.293.241-.438.613-.43.992a7.723 7.723 0 0 1 0 .255c-.008.378.137.75.43.991l1.004.827c.424.35.534.955.26 1.43l-1.298 2.247a1.125 1.125 0 0 1-1.369.491l-1.217-.456c-.355-.133-.75-.072-1.076.124a6.47 6.47 0 0 1-.22.128c-.331.183-.581.495-.644.869l-.213 1.281c-.09.543-.56.94-1.11.94h-2.594c-.55 0-1.019-.398-1.11-.94l-.213-1.281c-.062-.374-.312-.686-.644-.87a6.52 6.52 0 0 1-.22-.127c-.325-.196-.72-.257-1.076-.124l-1.217.456a1.125 1.125 0 0 1-1.369-.49l-1.297-2.247a1.125 1.125 0 0 1 .26-1.431l1.004-.827c.292-.24.437-.613.43-.991a6.932 6.932 0 0 1 0-.255c.007-.38-.138-.751-.43-.992l-1.004-.827a1.125 1.125 0 0 1-.26-1.43l1.297-2.247a1.125 1.125 0 0 1 1.37-.491l1.216.456c.356.133.751.072 1.076-.124.072-.044.146-.086.22-.128.332-.183.582-.495.644-.869l.214-1.28Z"
                />
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"
                />
              </svg>
            </template>
          </MenuItem>
        </ul>
      </nav>

      <!-- Вход/Выход -->
      <div>
        <ul>
          <!-- Если пользователь НЕ аутентифицирован, показываем кнопку "Войти" -->
          <li
            v-if="!isAuthenticated"
            class="flex items-center p-2 rounded-lg text-gray-500 hover:text-gray-950 transition duration-200"
          >
            <router-link
              to="/login"
              class="w-full text-center py-2 rounded-md bg-main text-white hover:bg-gray-700"
            >
              Войти
            </router-link>
          </li>

          <!-- Если пользователь аутентифицирован, показываем его данные -->
          <li
            v-else
            class="flex items-center p-2 rounded-lg text-gray-500 hover:text-gray-950 transition duration-200"
          >
            <div class="flex items-center" v-if="!isCollapsed">
              <img
                :src="user?.avatar || 'https://via.placeholder.com/30'"
                alt="Аватар"
                class="rounded-full w-8 h-8 mr-2"
              />
              <span>{{ user?.name }} ({{ user?.role }})</span>
            </div>
            <div class="flex items-center" v-else>
              <img
                :src="user?.avatar || 'https://via.placeholder.com/30'"
                alt="Аватар"
                class="rounded-full w-8 h-8 mr-2"
              />
            </div>
          </li>

          <!-- Кнопка "Выйти" видна только аутентифицированным пользователям -->
          <li
            v-if="isAuthenticated"
            class="flex items-center p-2 rounded-lg text-gray-500 hover:text-gray-950 transition duration-200"
          >
            <a
              href="#"
              @click.prevent="logout"
              class="w-full text-center py-2 rounded-md bg-main text-white hover:bg-gray-500"
            >
              Выйти
            </a>
          </li>
        </ul>
      </div>
    </aside>
  </div>
</template>

<script>
import registerArt from '@/assets/images/logo.png';
import MenuItem from '@/modules/auth/components/MenuItem.vue';
import { useAuthStore } from '@/store/auth.js';
import { computed } from 'vue';

export default {
  name: 'Sidebar',
  components: {
    MenuItem,
  },
  setup() {
    const authStore = useAuthStore();

    // Вычисляемые свойства
    const isAuthenticated = computed(() => !!authStore.accessToken);
    const user = computed(() => authStore.user);

    const logout = async () => {
      await authStore.logout();
    };

    return {
      registerArt,
      isAuthenticated,
      user,
      logout,
    };
  },
  data() {
    return {
      isCollapsed: false,
      screenWidth: window.innerWidth,
      initialWidth: window.innerWidth,
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
  },
};
</script>

<style scoped></style>

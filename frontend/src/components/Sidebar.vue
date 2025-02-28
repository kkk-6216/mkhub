<template>
  <div class="h-screen flex flex-col overflow-hidden">

    <!-- Sidebar -->
    <aside
        class="bg-gray-100 shadow-lg fixed left-0 top-0 h-screen transition-all duration-300 flex flex-col overflow-hidden"
        :class="{'w-64 p-6': !isCollapsed, 'w-16 p-4': isCollapsed}"
    >
      <!-- Logo -->
      <div class="flex items-center space-x-3 mb-6" :class="{ 'justify-center pl-0': isCollapsed, 'pl-6': !isCollapsed }">
        <img :src="registerArt" alt="Register Illustration" class="max-w-full rounded-2xl" :class="{ 'max-h-12': isCollapsed, 'max-h-24': !isCollapsed }">
      </div>

      <!-- Navigation -->
      <nav class="flex-grow"> <!-- Make nav take available vertical space -->
        <ul>
          <li class="flex items-center p-2 rounded-lg text-gray-700 hover:bg-blue-100 transition">
            <i class="mdi mdi-view-dashboard mr-2" :class="{ 'mr-0': isCollapsed }">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                <path stroke-linecap="round" stroke-linejoin="round" d="m2.25 12 8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25" />
              </svg>
            </i>
            <router-link v-if="!isCollapsed" to="/dashboard">Dashboard</router-link>
          </li>

          <li class="flex items-center p-2 rounded-lg text-gray-700 hover:bg-blue-100 transition">
            <i class="mdi mdi-book-open-variant mr-2" :class="{ 'mr-0': isCollapsed }">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                <path stroke-linecap="round" stroke-linejoin="round" d="M4.26 10.147a60.438 60.438 0 0 0-.491 6.347A48.62 48.62 0 0 1 12 20.904a48.62 48.62 0 0 1 8.232-4.41 60.46 60.46 0 0 0-.491-6.347m-15.482 0a50.636 50.636 0 0 0-2.658-.813A59.906 59.906 0 0 1 12 3.493a59.903 59.903 0 0 1 10.399 5.84c-.896.248-1.783.52-2.658.814m-15.482 0A50.717 50.717 0 0 1 12 13.489a50.702 50.702 0 0 1 7.74-3.342M6.75 15a.75.75 0 1 0 0-1.5.75 0 0 0 0 1.5Zm0 0v-3.675A55.378 55.378 0 0 1 12 8.443m-7.007 11.55A5.981 5.981 0 0 0 6.75 15.75v-1.5" />
              </svg>
            </i>
            <router-link v-if="!isCollapsed" to="/courses">Courses</router-link>
          </li>

          <li class="flex items-center p-2 rounded-lg text-gray-700 hover:bg-blue-100 transition">
            <i class="mdi mdi-calendar mr-2" :class="{ 'mr-0': isCollapsed }">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 0 1 2.25-2.25h13.5A2.25 2.25 0 0 1 21 7.5v11.25m-18 0A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75m-18 0v-7.5A2.25 2.25 0 0 1 5.25 9h13.5A2.25 2.25 0 0 1 21 11.25v7.5" />
              </svg>
            </i>
            <router-link v-if="!isCollapsed" to="/schedule">Schedule</router-link>
          </li>

          <li class="flex items-center p-2 rounded-lg text-gray-700 hover:bg-blue-100 transition">
            <i class="mdi mdi-folder-open mr-2" :class="{ 'mr-0': isCollapsed }">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 12.75V12A2.25 2.25 0 0 1 4.5 9.75h15A2.25 2.25 0 0 1 21.75 12v.75m-8.69-6.44-2.12-2.12a1.5 1.5 0 0 0-1.061-.44H4.5A2.25 2.25 0 0 0 2.25 6v12a2.25 2.25 0 0 0 2.25 2.25h15A2.25 2.25 0 0 0 21.75 18V9a2.25 2.25 0 0 0-2.25-2.25h-5.379a1.5 1.5 0 0 1-1.06-.44Z" />
              </svg>
            </i>
            <router-link v-if="!isCollapsed" to="/resources">Resources</router-link>
          </li>

          <li class="flex items-center p-2 rounded-lg text-gray-700 hover:bg-blue-100 transition">
            <i class="mdi mdi-message mr-2" :class="{ 'mr-0': isCollapsed }">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 20.25c4.97 0 9-3.694 9-8.25s-4.03-8.25-9-8.25S3 7.444 3 12c0 2.104.859 4.023 2.273 5.48.432.447.74 1.04.586 1.641a4.483 4.483 0 0 1-.923 1.785A5.969 5.969 0 0 0 6 21c1.282 0 2.47-.402 3.445-1.087.81.22 1.668.337 2.555.337Z" />
              </svg>
            </i>
            <router-link v-if="!isCollapsed" to="/messages">Messages</router-link>
          </li>

          <li class="flex items-center p-2 rounded-lg text-gray-700 hover:bg-blue-100 transition">
            <i class="mdi mdi-cog mr-2" :class="{ 'mr-0': isCollapsed }">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                <path stroke-linecap="round" stroke-linejoin="round" d="M9.594 3.94c.09-.542.56-.94 1.11-.94h2.593c.55 0 1.02.398 1.11.94l.213 1.281c.063.374.313.686.645.87.074.04.147.083.22.127.325.196.72.257 1.075.124l1.217-.456a1.125 1.125 0 0 1 1.37.49l1.296 2.247a1.125 1.125 0 0 1-.26 1.431l-1.003.827c-.293.241-.438.613-.43.992a7.723 7.723 0 0 1 0 .255c-.008.378.137.75.43.991l1.004.827c.424.35.534.955.26 1.43l-1.298 2.247a1.125 1.125 0 0 1-1.369.491l-1.217-.456c-.355-.133-.75-.072-1.076.124a6.47 6.47 0 0 1-.22.128c-.331.183-.581.495-.644.869l-.213 1.281c-.09.543-.56.94-1.11.94h-2.594c-.55 0-1.019-.398-1.11-.94l-.213-1.281c-.062-.374-.312-.686-.644-.87a6.52 6.52 0 0 1-.22-.127c-.325-.196-.72-.257-1.076-.124l-1.217.456a1.125 1.125 0 0 1-1.369-.49l-1.297-2.247a1.125 1.125 0 0 1 .26-1.431l1.004-.827c.292-.24.437-.613.43-.991a6.932 6.932 0 0 1 0-.255c.007-.38-.138-.751-.43-.992l-1.004-.827a1.125 1.125 0 0 1-.26-1.43l1.297-2.247a1.125 1.125 0 0 1 1.37-.491l1.216.456c.356.133.751.072 1.076-.124.072-.044.146-.086.22-.128.332-.183.582-.495.644-.869l.214-1.28Z" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
              </svg>
            </i>
            <router-link v-if="!isCollapsed" to="/settings">Settings</router-link>
          </li>
        </ul>
      </nav>
      <!-- Sign In -->
      <div>
        <ul>
          <li class="flex items-center p-2 rounded-lg text-gray-700 hover:bg-blue-100 transition">
            <i class="mdi mdi-login mr-2" :class="{ 'mr-0': isCollapsed }">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                <path stroke-linecap="round" stroke-linejoin="round" d="M17.982 18.725A7.488 7.488 0 0 0 12 15.75a7.488 7.488 0 0 0-5.982 2.975m11.963 0a9 9 0 1 0-11.963 0m11.963 0A8.966 8.966 0 0 1 12 21a8.966 8.966 0 0 1-5.982-2.275M15 9.75a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
              </svg>
            </i>
            <router-link v-if="!isCollapsed" to="/login">Sign In</router-link>
          </li>
        </ul>
      </div>
    </aside>
  </div>
</template>

<script>
import registerArt from '@/assets/images/logo.png';

export default {
  name: 'Sidebar',
  data() {
    return {
      isCollapsed: false,
      screenWidth: window.innerWidth,
      initialWidth: window.innerWidth,
      registerArt: registerArt, // Убедитесь, что путь к изображению верен
    };
  },
  mounted() {
    this.initialWidth = window.innerWidth;
    window.addEventListener('resize', this.updateScreenWidth);
    this.updateScreenWidth(); // Initial check
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.updateScreenWidth);
  },
  methods: {
    updateScreenWidth() {
      this.screenWidth = window.innerWidth;

      // Collapse logic (2/3 screen)
      this.isCollapsed = this.screenWidth < (this.initialWidth * 2) / 3;
    },
  },
};
</script>

<style scoped>
aside {
  overflow: hidden; /* Ensure no scroll */
  display: flex;
  flex-direction: column;
}
nav {
  flex-grow: 1; /* Allow nav to take remaining space */
  overflow-y: auto; /*  add scroll if nav content overflows */
}

div {
  overflow: hidden; /* Ensure no scroll */
}
/* You can add custom styles here if needed */
</style>
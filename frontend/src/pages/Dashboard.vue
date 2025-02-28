<template>
  <div class="flex h-screen">

    <!-- Main Content -->
    <div class="flex-1 p-8">

      <!-- Header -->
      <div class="flex justify-between items-center mb-8">
        <h1 class="text-2xl font-semibold text-gray-800">My Courses</h1>
        <div class="flex items-center">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 mr-2">
            <path stroke-linecap="round" stroke-linejoin="round" d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z" />
          </svg>
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
            <path stroke-linecap="round" stroke-linejoin="round" d="M14.857 17.082a23.848 23.848 0 0 0 5.454-1.31A8.967 8.967 0 0 1 18 9.75V9A6 6 0 0 0 6 9v.75a8.967 8.967 0 0 1-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 0 1-5.714 0m5.714 0a3 3 0 1 1-5.714 0" />
          </svg>
        </div>
      </div>

      <!-- Tabs -->
      <div class="flex mb-4">
        <button class="px-4 py-2 bg-blue-500 text-white rounded-l-md focus:outline-none">Active</button>
        <button
            class="px-4 py-2 bg-gray-200 text-gray-700 rounded-r-md focus:outline-none hover:bg-gray-300">Completed</button>
      </div>

      <!-- Course Cards -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <!-- Card 1 -->
        <div class="bg-blue-900 rounded-md p-4 text-white">
          <div class="relative">
            <svg class="absolute top-0 left-0 fill-current text-blue-700 opacity-25" width="48" height="48"
                 viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path
                  d="M38 11C41.866 14.866 43.866 20.134 43 26C42.134 31.866 39.134 37.134 35 41"
                  stroke="white" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
              <path
                  d="M42 5C45.866 8.866 47.866 14.134 47 20C46.134 25.866 43.134 31.134 39 35"
                  stroke="white" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
              <path
                  d="M46 0C46.944 12.079 39.921 22.921 29 29C18.079 35.079 7.217 35.045 0 29"
                  stroke="white" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
            </svg>
            <h2 class="text-sm font-light mb-1">UX Design</h2>
            <h3 class="font-semibold text-lg">User Flow</h3>
          </div>
          <div class="flex justify-between mt-4">
            <span class="text-sm">4/8</span>
          </div>
          <div class="h-1 bg-blue-700 rounded-full mt-2">
            <div class="bg-blue-500 h-1 rounded-full w-1/2"></div>
          </div>
        </div>

        <!-- Card 2 -->
        <div class="bg-blue-900 rounded-md p-4 text-white">
          <div class="relative">
            <svg class="absolute top-0 left-0 fill-current text-blue-700 opacity-25" width="48" height="48"
                 viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path
                  d="M38 11C41.866 14.866 43.866 20.134 43 26C42.134 31.866 39.134 37.134 35 41"
                  stroke="white" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
              <path
                  d="M42 5C45.866 8.866 47.866 14.134 47 20C46.134 25.866 43.134 31.134 39 35"
                  stroke="white" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
              <path
                  d="M46 0C46.944 12.079 39.921 22.921 29 29C18.079 35.079 7.217 35.045 0 29"
                  stroke="white" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
            </svg>
            <h2 class="text-sm font-light mb-1">Marketing</h2>
            <h3 class="font-semibold text-lg">Strategy</h3>
          </div>
          <div class="flex justify-between mt-4">
            <span class="text-sm">9/10</span>
          </div>
          <div class="h-1 bg-blue-700 rounded-full mt-2">
            <div class="bg-blue-500 h-1 rounded-full w-9/10"></div>
          </div>
        </div>

        <!-- Card 3 -->
        <div class="bg-blue-900 rounded-md p-4 text-white">
          <div class="relative">
            <svg class="absolute top-0 left-0 fill-current text-blue-700 opacity-25" width="48" height="48"
                 viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path
                  d="M38 11C41.866 14.866 43.866 20.134 43 26C42.134 31.866 39.134 37.134 35 41"
                  stroke="white" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
              <path
                  d="M42 5C45.866 8.866 47.866 14.134 47 20C46.134 25.866 43.134 31.134 39 35"
                  stroke="white" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
              <path
                  d="M46 0C46.944 12.079 39.921 22.921 29 29C18.079 35.079 7.217 35.045 0 29"
                  stroke="white" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
            </svg>
            <h2 class="text-sm font-light mb-1">Magic</h2>
            <h3 class="font-semibold text-lg">Potion Making</h3>
          </div>
          <div class="flex justify-between mt-4">
            <span class="text-sm">5/7</span>
          </div>
          <div class="h-1 bg-blue-700 rounded-full mt-2">
            <div class="bg-blue-500 h-1 rounded-full w-6/7"></div>
          </div>
        </div>
      </div>
    </div>
    <Sidebar />
  </div>
</template>

<script>
import { useRouter } from 'vue-router';
import Sidebar from '../components/Sidebar.vue';  // Import the Sidebar component

export default {
  name: 'Dashboard',
  components: {   // Register the Sidebar component
    Sidebar
  },
  methods: {
    goToLogin() {
      this.$router.push('/login');
    },
  },
  beforeCreate() {
    this.$router = useRouter(); // Инициализация роутера внутри beforeCreate
  },
};
</script>
<template>
  <div class="container mx-auto flex h-auto ">
    <!-- sidebar -->
    <Sidebar class="min-w-[8rem] max-w-[10rem] h-full"/>

    <!-- Main Content -->
    <div class="flex-1 flex flex-col overflow-hidden">
      <!-- Topbar -->
      <TopBar class="p-4 m-4"/>

      <!-- Page Content -->
      <main class="flex-1 overflow-x-hidden overflow-y-auto m-4">
        <div class="px-6">
          <router-view />
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import Sidebar from '../components/sidebar/Sidebar.vue';
import TopBar from '../modules/profile/components/TopBar.vue';

export default {
  components: {
    Sidebar,
    TopBar,
  },
};
</script>
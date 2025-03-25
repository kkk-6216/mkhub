<template>
  <div class="border-b border-gray-200 pb-12 grid grid-cols-1 sm:grid-cols-3 gap-6">
    <!-- Left section with heading -->
    <div class="sm:col-span-1">
      <h2 class="text-base/7 font-semibold text-dark">Профиль</h2>
      <p class="mt-1 text-sm/6 text-gray-500">Эта информация будет отображаться публично, поэтому будьте осторожны с тем, чем делитесь.</p>
    </div>

    <!-- Right section with form -->
    <div class="sm:col-span-2">

      <div class="col-span-full">
        <label for="photo" class="block text-sm/6 font-medium text-dark">Фотография</label>
        <div class="mt-2 flex items-center gap-x-3">
          <img
              :src="userAvatar"
              alt="Аватар"
              class="w-20 h-20 rounded-lg object-cover border-2 border-gray-200 mr-6"
          />
        </div>
      </div>

      <div class="mt-10 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">

        <div class="sm:col-span-4">
          <label for="username" class="block text-sm/6 font-medium text-dark">Имя пользователя</label>
          <div class="mt-2">
            <div class="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
              <div class="shrink-0 text-base text-gray-500 select-none sm:text-sm/6"></div>
              <span class="block min-w-0 grow py-1.5 pr-3 pl-1 text-base text-dark sm:text-sm/6">{{ displayUserName }}</span>
            </div>
          </div>
        </div>

        <div class="col-span-full">
          <label for="about" class="block text-sm/6 font-medium text-dark">О себе</label>
          <div class="mt-2" v-if="about.length !== 0">
            <p class="block w-full rounded-md bg-white py-1.5 text-base text-dark sm:text-sm/6">{{ displayAbout }}</p>
          </div>
          <div class="mt-2" v-if="about.length === 0">
            <p class="block w-full rounded-md bg-white py-1.5 text-base text-gray-500 sm:text-sm/6">Пусто...</p>
          </div>
        </div>

      </div>
      <div class="sm:col-span-4 flex justify-end">
        <DefaultButton
            label="Редактировать"
            variant="primary"
            @click="goTo('/profile/edit-profile')"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { useAuthStore } from "@/store/auth.js";
import DefaultButton from "@/components/buttons/DefaultButton.vue";

export default {
  name: "Profile",
  components: {
    DefaultButton,
  },
  data() {
    return {

    };
  },
  beforeCreate() {
    this.authStore = useAuthStore();
  },
  computed: {
    user() {
      return this.authStore.user;
    },
    userName() {
      return this.user?.sub || 'Имя пользователя';
    },
    about() {
      return this.user?.about || '';
    },
    userAvatar() {
      console.log(this.authStore.userAvatar)
      return this.authStore.userAvatar || 'https://www.svgrepo.com/show/452030/avatar-default.svg';
    },
    displayUserName() {
      return this.userName;
    },
    displayAbout() {
      return this.about;
    },
  },
  methods: {
    goTo(path) {
      this.$router.push(path);
    },
  },
};
</script>
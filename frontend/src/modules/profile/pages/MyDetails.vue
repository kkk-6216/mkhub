<template>
  <div class="border-b border-gray-900/10 pb-12 grid grid-cols-1 sm:grid-cols-3 gap-6">
    <!-- Left section with heading -->
    <div class="sm:col-span-1">
      <h2 class="text-base/7 font-semibold text-gray-900">Мои данные</h2>
      <p class="text-sm/6 text-gray-600">Пожалуйста, укажите ваши личные данные</p>
    </div>

    <!-- Data display section -->
    <div class="sm:col-span-2">
      <div class="mt-6 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">

        <div class="sm:col-span-3">
          <label for="first-name" class="block text-sm/6 font-medium text-gray-900">Имя</label>
          <div class="mt-2">
            <div class="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
              <div class="shrink-0 text-base text-gray-500 select-none sm:text-sm/6"></div>
              <span id="first-name" class="block min-w-0 grow py-1.5 pr-3 pl-1 text-base text-gray-900 sm:text-sm/6">{{ firstName }}</span>
            </div>
          </div>
        </div>

        <div class="sm:col-span-3">
          <label for="last-name" class="block text-sm/6 font-medium text-gray-900">Фамилия</label>
          <div class="mt-2">
            <div class="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
              <div class="shrink-0 text-base text-gray-500 select-none sm:text-sm/6"></div>
              <span id="last-name" class="block min-w-0 grow py-1.5 pr-3 pl-1 text-base text-gray-900 sm:text-sm/6">{{ lastName }}</span>
            </div>
          </div>
        </div>

        <div class="sm:col-span-4">
          <label for="email" class="block text-sm/6 font-medium text-gray-900">Адрес электронной почты</label>
          <div class="mt-2">
            <div class="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
              <div class="shrink-0 text-base text-gray-500 select-none sm:text-sm/6"></div>
              <span id="email" class="block min-w-0 grow py-1.5 pr-3 pl-1 text-base text-gray-900 sm:text-sm/6">{{ email }}</span>
            </div>
          </div>
        </div>

        <div class="sm:col-span-3">
          <label for="phone-number" class="block text-sm/6 font-medium text-gray-900">Номер телефона</label>
          <div class="mt-2">
            <div class="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
              <div class="shrink-0 text-base text-gray-500 select-none sm:text-sm/6"></div>
              <span id="phone-number" class="block min-w-0 grow py-1.5 pr-3 pl-1 text-base text-gray-900 sm:text-sm/6">{{ phoneNumber }}</span>
            </div>
          </div>
        </div>

        <div class="sm:col-span-3">
          <label for="instagram" class="block text-sm/6 font-medium text-gray-900">Instagram</label>
          <div class="mt-2">
            <div class="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
              <div class="shrink-0 text-base text-gray-500 select-none sm:text-sm/6"></div>
              <span id="instagram" class="block min-w-0 grow py-1.5 pr-3 pl-1 text-base text-gray-900 sm:text-sm/6">{{ instagram }}</span>
            </div>
          </div>
        </div>

        <div class="sm:col-span-3">
          <label for="github" class="block text-sm/6 font-medium text-gray-900">GitHub</label>
          <div class="mt-2">
            <div class="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
              <div class="shrink-0 text-base text-gray-500 select-none sm:text-sm/6"></div>
              <span id="github" class="block min-w-0 grow py-1.5 pr-3 pl-1 text-base text-gray-900 sm:text-sm/6">{{ github }}</span>
            </div>
          </div>
        </div>

      </div>
    </div>

    <!-- Edit Button using DefaultButton -->
    <div class="sm:col-span-3 flex justify-end">
      <DefaultButton label="Редактировать" variant="primary" @click="goToEdit" />
    </div>

  </div>
</template>

<script>
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import { getUserData } from "@/modules/profile/service/user-details.js";
import { useAuthStore } from "@/store/auth.js";

export default {
  name: "MyDetails",
  components: {
    DefaultButton,
  },
  beforeCreate() {
    this.authStore = useAuthStore();
  },
  data() {
    return {
      firstName: '',
      lastName: '',
      email: '',
      phoneNumber: '',
      instagram: '',
      github: '',
    };
  },
  mounted() {
    this.fetchUserDetails();
  },
  methods: {
    goToEdit() {
      this.$router.push('/profile/my-details/edit');
    },
    async fetchUserDetails() {
      try {
        const userData = await getUserData(this.userId);

        this.firstName = userData.firstName || 'Не указано';
        this.lastName = userData.lastName || 'Не указано';
        this.email = userData.email || 'Не указано';
        this.phoneNumber = userData.phone || 'Не указано';
        this.instagram = userData.instagram || 'Не указано';
        this.github = userData.github || 'Не указано';

      } catch (error) {
        this.showAlert('error', `Ошибка при получении данных: ${error}`);
      }
    }
  },
  inject: ['showAlert'],
  computed: {
    userId() {
      return this.authStore.user?.id;
    }
  }
};
</script>

<style scoped>
/* Styles for the page */
</style>

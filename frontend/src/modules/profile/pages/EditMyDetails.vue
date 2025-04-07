<template>
  <div class="border-b border-gray-900/10 pb-12 grid grid-cols-1 sm:grid-cols-3 gap-6">
    <!-- Left section with title -->
    <div class="sm:col-span-1">
      <h2 class="text-base/7 font-semibold text-gray-900">Редактировать мои данные</h2>
      <p class="text-sm/6 text-gray-600">Измените ваши личные данные и сохраните изменения.</p>
    </div>

    <!-- Right section with form -->
    <div class="sm:col-span-2">

      <div class="mt-6 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
        <div class="sm:col-span-3">
          <label for="first-name" class="block text-sm/6 font-medium text-gray-900">Имя</label>
          <div class="mt-2">
            <input type="text" name="first-name" id="first-name" v-model="firstName" autocomplete="given-name" class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6" />
          </div>
        </div>

        <div class="sm:col-span-3">
          <label for="last-name" class="block text-sm/6 font-medium text-gray-900">Фамилия</label>
          <div class="mt-2">
            <input type="text" name="last-name" id="last-name" v-model="lastName" autocomplete="family-name" class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6" />
          </div>
        </div>

        <div class="sm:col-span-4">
          <label for="email" class="block text-sm/6 font-medium text-gray-900">Почта</label>
          <div class="mt-2">
            <input type="email" name="email" id="email" v-model="email" autocomplete="tel" class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6" />
          </div>
        </div>

        <div class="sm:col-span-4">
          <label for="phone-number" class="block text-sm/6 font-medium text-gray-900">Номер телефона</label>
          <div class="mt-2">
            <input type="tel" name="phone-number" id="phone-number" v-model="phone" autocomplete="tel" class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6" />
          </div>
        </div>

        <!-- Instagram moved below phone number, taking 3 columns -->
        <div class="sm:col-span-4">
          <label for="instagram" class="block text-sm/6 font-medium text-gray-900">Instagram</label>
          <div class="mt-2">
            <input type="text" name="instagram" id="instagram" v-model="instagram" class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6" />
          </div>
        </div>

        <div class="sm:col-span-4">
          <label for="github" class="block text-sm/6 font-medium text-gray-900">GitHub</label>
          <div class="mt-2">
            <input type="text" name="github" id="github" v-model="github" autocomplete="github" class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6" />
          </div>
        </div>

      </div>

      <!-- Save/Cancel Buttons using DefaultButton -->
      <div class="sm:col-span-4 flex justify-end space-x-2 mt-4">
        <DefaultButton label="Сохранить" variant="primary" @click="saveChanges" />
        <DefaultButton label="Отменить" variant="secondary" @click="cancelEdit" />
      </div>
    </div>
  </div>
</template>

<script>
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import { updateUserData, getUserData } from '@/modules/profile/service/user-details.js';
import {useAuthStore} from "@/store/auth.js";

export default {
  name: "EditMyDetails",
  components: {
    DefaultButton,
  },
  data() {
    return {
      firstName: '',
      lastName: '',
      email: '',
      phone: '',
      instagram: '',
      github: '',
    };
  },
  mounted() {
    this.fetchUserDetails();
  },
  methods: {
    async saveChanges() {
      try {
        const userData = {
          firstName: this.firstName,
          lastName: this.lastName,
          email: this.email,
          phone: this.phone,
          instagram: this.instagram,
          github: this.github,
        }
        await updateUserData(userData);
        this.showAlert('success', 'Информация обновлена успешно!');
      } catch (error) {
        this.showAlert('error', `Ошибка при обновлении информации: ${error}`);
      }
      this.$router.push('/profile/my-details');
    },
    cancelEdit() {
      this.$router.push('/profile/my-details');
    },

      async fetchUserDetails() {
        try {
          const userData = await getUserData();

          if (userData) {
            this.firstName = userData.firstName || '';
            this.lastName = userData.lastName || '';
            this.email = userData.email || '';
            this.phone = userData.phone || '';
            this.instagram = userData.instagram || '';
            this.github = userData.github || '';
          } else {
            console.error('Получены пустые данные для пользователя');
          }

        } catch (error) {
          this.showAlert('error', `Ошибка при обновлении информации: ${error}`);
        }
      }
    },
  inject: ['showAlert'],
  beforeCreate() {
    this.authStore = useAuthStore();
  },

};
</script>

<style scoped>

</style>
<template>
  <div class="border-b border-gray-900/10 pb-12 grid grid-cols-1 sm:grid-cols-3 gap-6">
    <!-- Левая часть с заголовком -->
    <div class="sm:col-span-1">
      <h2 class="text-base/7 font-semibold text-gray-900">Профиль</h2>
      <p class="mt-1 text-sm/6 text-gray-600">Эта информация будет отображаться публично, поэтому будьте осторожны с тем, чем делитесь.</p>
    </div>

    <!-- Правая часть с формой -->
    <div class="sm:col-span-2">

      <div class="col-span-full">
        <label for="photo" class="block text-sm/6 font-medium text-gray-900">Фотография</label>
        <div class="mt-2 flex items-center gap-x-3">
          <img
              :src="userAvatar"
              alt="Аватар"
              class="w-20 h-20 rounded-lg object-cover border-2 border-gray-200 mr-6"
          />
          <DefaultButton label="Изменить" variant="secondary" @click="changeAvatar" />
        </div>
      </div>

      <div class="mt-10 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">

        <div class="sm:col-span-4">
          <label for="username" class="block text-sm/6 font-medium text-gray-900">Имя пользователя</label>
          <div class="mt-2">
            <div class="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
              <div class="shrink-0 text-base text-gray-500 select-none sm:text-sm/6"></div>
              <input type="text" name="username" id="username" v-model="editableUserName" class="block min-w-0 grow py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-400 focus:outline-none sm:text-sm/6" placeholder="janesmith" />
            </div>
          </div>
        </div>

        <div class="col-span-full">
          <label for="about" class="block text-sm/6 font-medium text-gray-900">О себе</label>
          <div class="mt-2">
            <textarea name="about" id="about" rows="3" v-model="editableAbout" class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6" />
          </div>
          <p class="mt-3 text-sm/6 text-gray-600">Напишите несколько предложений о себе.</p>
        </div>

      </div>
      <div class="sm:col-span-4 flex justify-end space-x-2">
        <DefaultButton label="Сохранить" variant="primary" @click="saveChanges" />
        <DefaultButton label="Отменить" variant="secondary" @click="cancelEditing" />
      </div>
    </div>
  </div>
</template>

<script>
import { useAuthStore } from "@/store/auth.js";
import DefaultButton from "@/components/buttons/DefaultButton.vue";

export default {
  name: "EditProfile",
  components: {
    DefaultButton,
  },
  data() {
    return {
      authStore: useAuthStore(),
      editableUserName: '',
      editableAbout: '',
    };
  },
  computed: {
    user() {
      return this.authStore.user;
    },
    userAvatar() {
      return this.user?.avatar || 'https://www.svgrepo.com/show/452030/avatar-default.svg';
    },
  },
  mounted() {
    this.editableUserName = this.user?.sub || '';
    this.editableAbout = this.user?.about || '';
  },
  methods: {
    async saveChanges() {
      try {
        await this.authStore.updateUserProfile({
          userName: this.editableUserName,
          about: this.editableAbout,
        });
        this.$router.push('/profile'); // Redirect to profile page
      } catch (error) {
        console.error("Ошибка при сохранении профиля:", error);
        // Add error handling logic here, e.g., display a notification to the user
      }
    },
    cancelEditing() {
      this.$router.push('/profile'); // Redirect to profile page
    },
    changeAvatar() {
      // Replace with functionality to change avatar.
      alert('Functionality to change avatar will be implemented here');
      // Example: you can open dialog or etc..
    },
  },
};
</script>
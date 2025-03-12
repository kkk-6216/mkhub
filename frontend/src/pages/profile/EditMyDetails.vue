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
          <label for="email" class="block text-sm/6 font-medium text-gray-900">Адрес электронной почты</label>
          <div class="mt-2">
            <input id="email" name="email" type="email" v-model="email" autocomplete="email" class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6" />
          </div>
        </div>

        <div class="sm:col-span-3">
          <label for="phone-number" class="block text-sm/6 font-medium text-gray-900">Номер телефона</label>
          <div class="mt-2">
            <input type="tel" name="phone-number" id="phone-number" v-model="phoneNumber" autocomplete="tel" class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6" />
          </div>
        </div>

        <!-- Instagram moved below phone number, taking 3 columns -->
        <div class="sm:col-span-3">
          <label for="instagram" class="block text-sm/6 font-medium text-gray-900">Instagram</label>
          <div class="mt-2">
            <input type="text" name="instagram" id="instagram" v-model="instagram" class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6" />
          </div>
        </div>

        <div class="sm:col-span-3">
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
      phoneNumber: '',
      instagram: '',
      github: '',
    };
  },
  mounted() {
    // Simulate fetching data from an API or store
    setTimeout(() => {
      this.firstName = 'John';
      this.lastName = 'Doe';
      this.email = 'john.doe@example.com';
      this.phoneNumber = '+15551234567';
      this.instagram = 'johndoe';
      this.github = 'johndoe';
    }, 500);
  },
  methods: {
    saveChanges() {
      // Simulate saving data to an API or store
      console.log('Saving changes:', {
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email,
        phoneNumber: this.phoneNumber,
        instagram: this.instagram,
        github: this.github,
      });
      this.$router.push('/my-details');
    },
    cancelEdit() {
      this.$router.push('/my-details');
    },
  },
};
</script>

<style scoped>
/* Styles for the page */
</style>
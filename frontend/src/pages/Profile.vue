<template>
  <div class="p-6 bg-gray-50 min-h-screen">

    <h1 class="text-3xl font-bold text-main mb-6 ml-22">Профиль пользователя</h1>

    <div class="max-w-3xl mx-auto bg-white rounded-xl  p-8">

      <div class="flex flex-col md:flex-row items-center md:items-start space-y-8 md:space-y-0 md:space-x-8">
        <!-- Блок с аватаром -->
        <div class="w-full md:w-1/2 flex flex-col items-center">
          <div class="relative w-2/3 h-2/3">
            <img
                :src="user.avatar || 'https://via.placeholder.com/128'"
                alt="Аватар"
                class="w-full h-full rounded-lg object-cover border-2 border-indigo-100"
            />
          </div>
        </div>

        <!-- Блок с информацией -->
        <div class="w-full md:w-1/2 space-y-5">
          <div>
            <label class="block text-main text-sm font-bold">Логин:</label>
            <p class="text-gray-500">{{ user.login }}</p>
          </div>

          <div>
            <label class="block text-main text-sm font-bold">Имя:</label>
            <p class="text-gray-500">{{ user.firstName }}</p>
          </div>

          <div>
            <label class="block text-main text-sm font-bold">Фамилия:</label>
            <p class="text-gray-500">{{ user.lastName }}</p>
          </div>

          <div>
            <label class="block text-main text-sm font-bold">Email:</label>
            <p class="text-gray-500">{{ user.email }}</p>
          </div>

          <div>
            <label class="block text-main text-sm font-bold">Роль:</label>
            <p class="text-gray-500">{{ user.role }}</p>
          </div>

          <router-link to="/edit-profile" class="inline-block">
            <Button type="primary" label="Редактировать" />
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import Button from '@/components/buttons/DefaultButton.vue';

export default {
  name: 'Profile',
  components: {
    Button
  },
  setup() {
    const user = ref({
      firstName: 'Иван',
      lastName: 'Иванов',
      login: 'ivanov123',
      email: 'ivan@example.com',
      role: 'Администратор',
      avatar: 'https://www.svgrepo.com/show/452030/avatar-default.svg',
    });

    onMounted(() => {
      // Загрузите данные пользователя из API или локального хранилища
    });

    return {
      user
    };
  }
};
</script>
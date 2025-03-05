<template>
  <div class="flex items-center justify-center h-screen bg-white-100">
    <div class="flex w-screen h-screen flex-col items-center justify-center">
      <h2 class="text-3xl font-semibold mb-[20px] text-left">Вход</h2>
      <p class="text-gray-500 mb-6 text-left">Пожалуйста, войдите, чтобы продолжить</p>
      <form class="w-80" @submit.prevent="login">
        <!-- Имя пользователя -->
        <InputField
            v-model="username"
            :is-submitted="isSubmitted"
            placeholder="Имя пользователя"
        >
          <template #icon>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z" />
            </svg>
          </template>
        </InputField>

        <!-- Пароль -->
        <PasswordField
            v-model="password"
            :is-submitted="isSubmitted"
            :is-valid="passwordValid"
            placeholder="Пароль"
            @togglePassword="togglePassword"
        >
          <template #icon>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
              <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 10.5V6.75a4.5 4.5 0 1 0-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 0 0 2.25-2.25v-6.75a2.25 2.25 0 0 0-2.25-2.25H6.75a2.25 2.25 0 0 0-2.25 2.25v6.75a2.25 2.25 0 0 0 2.25 2.25Z" />
            </svg>
          </template>
          <template #toggleIcon>
            <svg v-if="showPassword" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
              <path stroke-linecap="round" stroke-linejoin="round" d="M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178Z" />
              <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
            </svg>
            <svg v-else xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3.98 8.223A10.477 10.477 0 0 0 1.934 12C3.226 16.338 7.244 19.5 12 19.5c.993 0 1.953-.138 2.863-.395M6.228 6.228A10.451 10.451 0 0 1 12 4.5c4.756 0 8.773 3.162 10.065 7.498a10.522 10.522 0 0 1-4.293 5.774M6.228 6.228 3 3m3.228 3.228 3.65 3.65m7.894 7.894L21 21m-3.228-3.228-3.65-3.65m0 0a3 3 0 1 0-4.243-4.243m4.242 4.242L9.88 9.88" />
            </svg>
          </template>
        </PasswordField>

        <button
            class="w-full bg-[#0d2856] text-white py-2 rounded-[10px]
                   hover:bg-[#092040] hover:scale-101
                   focus:outline-none focus:ring-2 focus:ring-gray-300
                   transition duration-300 ease-in-out"
            type="submit"
        >
          Войти
        </button>
        <p v-if="errorMessage" class="text-red-500 mt-2 text-center">{{ errorMessage }}</p>
      </form>
      <div class="w-[100px] h-[1px] m-5 bg-[#0d2856] rounded-[10px]"></div>

      <div>
        <p class="text-gray-500 mt-2 text-center">
          У меня нет аккаунта&nbsp;
          <router-link to="/register" class="text-[#0d2856]">Зарегистрироваться</router-link>
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import InputField from '@/modules/auth/components/InputField.vue';
import PasswordField from '@/modules/auth/components/PasswordField.vue';
import { useRouter } from 'vue-router';

export default {
  name: 'Login',
  components: {
    InputField,
    PasswordField,
  },
  data() {
    return {
      username: '',
      password: '',
      errorMessage: '',
      isSubmitted: false,
      showPassword: false,
    };
  },
  computed: {
    passwordValid() {
      return this.password.length >= 6;
    },
  },
  methods: {
    togglePassword() {
      this.showPassword = !this.showPassword;
    },
    async login() {
      this.isSubmitted = true;

      if (!this.username || !this.password) {
        this.errorMessage = 'Необходимо заполнить все поля.';
        return;
      }

      if (!this.passwordValid) {
        this.errorMessage = 'Длина пароля должна быть не менее 6 символов.';
        return;
      }

      try {
        // Здесь добавьте логику проверки пользователя (вместо простого alert)
        if (this.username === 'user' && this.password === 'password') {
          // Создайте объект пользователя (замените данными из вашей системы)
          const user = {
            name: "Фиона",
            role: "Пользователь",
            avatar: "https://i.pinimg.com/736x/8b/16/7a/8b167af653c2399dd93b952a48740620.jpg" // или путь к вашему аватару
          };

          // Сохраните данные пользователя в localStorage
          localStorage.setItem('user', JSON.stringify(user));

          // Перенаправьте на главную страницу (или другую нужную страницу)
          this.$router.push('/');
        } else {
          // Отобразите сообщение об ошибке, если учетные данные неверны
          this.errorMessage = 'Неверные имя пользователя или пароль.';
        }
      } catch (error) {
        console.error('Login error:', error);
        this.errorMessage = 'Произошла ошибка. Пожалуйста, попробуйте позже.';
      }
    },
  },
  beforeCreate() {
    this.$router = useRouter(); // Инициализация роутера внутри beforeCreate
  },
};
</script>
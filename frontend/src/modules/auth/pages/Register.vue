<template>
  <div class="flex items-center justify-center min-h-screen bg-white">
    <div class="flex w-screen h-screen flex-col items-center justify-center">
      <h2 class="text-3xl font-semibold mb-2 text-left">Регистрация</h2>
      <p class="text-gray-500 mb-6 text-left">Создать аккаунт</p>
      <form class="w-80" @submit.prevent="register">
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

        <!-- Email -->
        <InputField
            v-model="email"
            :is-submitted="isSubmitted"
            :is-valid="emailValid"
            type="email"
            placeholder="Email"
        >
          <template #icon>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
              <path stroke-linecap="round" stroke-linejoin="round" d="M21.75 6.75v10.5a2.25 2.25 0 0 1-2.25 2.25h-15a2.25 2.25 0 0 1-2.25-2.25V6.75m19.5 0A2.25 2.25 0 0 0 19.5 4.5h-15a2.25 2.25 0 0 0-2.25 2.25m19.5 0v.243a2.25 2.25 0 0 1-1.07 1.916l-7.5 4.615a2.25 2.25 0 0 1-2.36 0L3.32 8.91a2.25 2.25 0 0 1-1.07-1.916V6.75" />
            </svg>
          </template>
        </InputField>

        <!-- Пароль -->
        <PasswordField
            v-model="password"
            :is-submitted="isSubmitted"
            :is-valid="passwordValid"
            placeholder="Пароль"
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

        <!-- Подтверждение пароля -->
        <PasswordField
            v-model="confirmPassword"
            :is-submitted="isSubmitted"
            :is-valid="confirmPasswordMatch"
            placeholder="Подтвердите пароль"
        >
          <template #icon>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
              <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 10.5V6.75a4.5 4.5 0 1 0-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 0 0 2.25-2.25v-6.75a2.25 2.25 0 0 0-2.25-2.25H6.75a2.25 2.25 0 0 0-2.25 2.25v6.75a2.25 2.25 0 0 0 2.25 2.25Z" />
            </svg>
          </template>
          <template #toggleIcon>
            <svg v-if="showConfirmPassword" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
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
          Зарегистрироваться
        </button>

        <p v-if="errorMessage" class="text-red-500 mt-2 text-center">
          {{ errorMessage }}
        </p>
      </form>

      <div class="w-[100px] h-[1px] m-5 bg-[#0d2856] rounded-[10px]"></div>

      <p class="text-gray-500 mt-4 text-center">
        Уже есть аккаунт?&nbsp;
        <router-link to="/login" class="text-[#0d2856]">Войти</router-link>
      </p>
    </div>
  </div>
</template>

<script>
import InputField from '@/modules/auth/components/InputField.vue';
import PasswordField from '@/modules/auth/components/PasswordField.vue';
import { useRouter } from 'vue-router';

export default {
  name: 'Register',
  components: {
    InputField,
    PasswordField,
  },
  data() {
    return {
      username: '',
      phoneNumber: '',
      email: '',
      password: '',
      confirmPassword: '',
      errorMessage: '',
      isSubmitted: false,
      showPassword: false,
      showConfirmPassword: false,
    };
  },
  computed: {
    phoneNumberValid() {
      if (!this.phoneNumber) return false;
      const regex = /^\+996\d{9}$/;
      return regex.test(this.phoneNumber);
    },
    emailValid() {
      if (!this.email) return true;
      const regex = /^[^\s@]+@(gmail\.com|icloud\.com|manas\.edu\.kg)$/;
      return regex.test(this.email);
    },
    passwordValid() {
      return this.password.length >= 6;
    },
    confirmPasswordMatch() {
      return this.password === this.confirmPassword;
    },
  },
  methods: {
    togglePassword(field) {
      if (field === 'password') {
        this.showPassword = !this.showPassword;
      } else if (field === 'confirmPassword') {
        this.showConfirmPassword = !this.showConfirmPassword;
      }
    },
    async register() {
      this.isSubmitted = true;

      if (
          !this.username ||
          !this.phoneNumber ||
          !this.email ||
          !this.password ||
          !this.confirmPassword
      ) {
        this.errorMessage = 'Необходимо заполнить все поля.';
        return;
      }

      if (!this.phoneNumberValid) {
        this.errorMessage = 'Неверный формат номера телефона.';
        return;
      }

      if (!this.emailValid) {
        this.errorMessage =
            'Пожалуйста, используйте действительный адрес электронной почты @gmail.com, @icloud.com или @manas.edu.kg.';
        return;
      }

      if (!this.passwordValid) {
        this.errorMessage = 'Длина пароля должна быть не менее 6 символов.';
        return;
      }

      if (!this.confirmPasswordMatch) {
        this.errorMessage = 'Пароли не совпадают.';
        return;
      }

      try {
        // Здесь будет ваш код для отправки данных на сервер для регистрации
        // const response = await fetch('/api/register', {
        //   method: 'POST',
        //   headers: {
        //     'Content-Type': 'application/json'
        //   },
        //   body: JSON.stringify({ username, phoneNumber, email, password, confirmPassword })
        // });

        // if (response.ok) {
        //   // Успешная регистрация
        //   console.log('Регистрация успешна!');
        //   this.$router.push('/login'); // Перенаправление на страницу входа
        // } else {
        //   // Обработка ошибок
        //   const data = await response.json();
        //   this.errorMessage = data.message || 'Ошибка регистрации.';
        // }

        console.log('Данные для регистрации:', {
          username: this.username,
          phoneNumber: this.phoneNumber,
          email: this.email,
          password: this.password,
          confirmPassword: this.confirmPassword,
        });
        // Временное сообщение об успешной регистрации (пока нет реального backend)
        alert('Регистрация прошла успешно! (имитация)');
        this.$router.push('/login');
      } catch (error) {
        console.error('Ошибка при регистрации:', error);
        this.errorMessage =
            'Произошла ошибка при регистрации. Пожалуйста, попробуйте позже.';
      }
    },
  },
  beforeCreate() {
    this.$router = useRouter(); // Инициализация роутера внутри beforeCreate
  },
};
</script>
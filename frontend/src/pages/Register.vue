<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-100">
    <div class="flex w-screen h-screen">
      <div class="w-1/2 flex flex-col items-center justify-center">
        <h2 class="text-3xl bg-[#1B374D]font-semibold mb-2 text-left font-sans">Регистрация</h2>
        <p class="text-gray-500 mb-6 text-left font-sans">Создать аккаунт</p>
        <form class="w-80" @submit.prevent="register">
          <div class="relative mb-4">
            <input
                type="text"
                placeholder="Имя пользователя"
                class="w-full px-4 py-2 pl-10 border rounded-md focus:outline-none focus:ring-2 focus:ring-gray-300 text-gray-700"
                v-model="username"
                :class="{
                'border-red-500': !username && isSubmitted,
                'border-gray-300': username || !isSubmitted,
              }"
            />
            <span class="absolute left-3 top-2 text-gray-500">
              <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke-width="1.5"
                  stroke="currentColor"
                  class="size-6"
              >
                <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z"
                />
              </svg>
            </span>
          </div>
          <div class="relative mb-4">
            <input
                type="tel"
                placeholder="Номер телефона (+996xxxxxxxxx)"
                class="w-full px-4 py-2 pl-10 border rounded-md focus:outline-none focus:ring-2 focus:ring-gray-300 text-gray-700"
                v-model="phoneNumber"
                :class="{
                'border-red-500': !phoneNumberValid && isSubmitted,
                'border-gray-300': phoneNumberValid || !isSubmitted,
              }"
            />
            <span class="absolute left-3 top-2 text-gray-500">
              <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke-width="1.5"
                  stroke="currentColor"
                  class="size-6"
              >
                <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M2.25 6.75c0 8.284 6.716 15 15 15h2.25a2.25 2.25 0 0 0 2.25-2.25v-1.372c0-.516-.351-.966-.852-1.091l-4.423-1.106c-.44-.11-.902.055-1.173.417l-.97 1.293c-.282.376-.769.542-1.21.38a12.035 12.035 0 0 1-7.143-7.143c-.162-.441.004-.928.38-1.21l1.293-.97c.363-.271.527-.734.417-1.173L6.963 3.102a1.125 1.125 0 0 0-1.091-.852H4.5A2.25 2.25 0 0 0 2.25 4.5v2.25Z"
                />
              </svg>
            </span>
          </div>
          <div class="relative mb-4">
            <input
                type="email"
                placeholder="Email"
                class="w-full px-4 py-2 pl-10 border rounded-md focus:outline-none focus:ring-2 focus:ring-gray-300 text-gray-700"
                v-model="email"
                :class="{
                'border-red-500': (!email || !emailValid) && isSubmitted,
                'border-gray-300': (email && emailValid) || !isSubmitted,
              }"
            />
            <span class="absolute left-3 top-2 text-gray-500">
              <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke-width="1.5"
                  stroke="currentColor"
                  class="size-6"
              >
                <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M21.75 6.75v10.5a2.25 2.25 0 0 1-2.25 2.25h-15a2.25 2.25 0 0 1-2.25-2.25V6.75m19.5 0A2.25 2.25 0 0 0 19.5 4.5h-15a2.25 2.25 0 0 0-2.25 2.25m19.5 0v.243a2.25 2.25 0 0 1-1.07 1.916l-7.5 4.615a2.25 2.25 0 0 1-2.36 0L3.32 8.91a2.25 2.25 0 0 1-1.07-1.916V6.75"
                />
              </svg>
            </span>
          </div>
          <div class="relative mb-4">
            <input
                :type="showPassword ? 'text' : 'password'"
                placeholder="Пароль"
                class="w-full px-4 py-2 pl-10 border rounded-md focus:outline-none focus:ring-2 focus:ring-gray-300 text-gray-700"
                v-model="password"
                :class="{
                'border-red-500': (!password || !passwordValid) && isSubmitted,
                'border-gray-300': (password && passwordValid) || !isSubmitted,
              }"
            />
            <span class="absolute left-3 top-2 text-gray-500">
              <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke-width="1.5"
                  stroke="currentColor"
                  class="size-6"
              >
                <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M16.5 10.5V6.75a4.5 4.5 0 1 0-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 0 0 2.25-2.25v-6.75a2.25 2.25 0 0 0-2.25-2.25H6.75a2.25 2.25 0 0 0-2.25 2.25v6.75a2.25 2.25 0 0 0 2.25 2.25Z"
                />
              </svg>
            </span>
            <span
                class="absolute right-3 top-2 text-gray-500 cursor-pointer"
                @click="togglePassword('password')"
            >
              <template v-if="showPassword">
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke-width="1.5"
                    stroke="currentColor"
                    class="size-6"
                >
                  <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178Z"
                  />
                  <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"
                  />
                </svg>
              </template>
              <template v-else>
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke-width="1.5"
                    stroke="currentColor"
                    class="size-6"
                >
                  <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M3.98 8.223A10.477 10.477 0 0 0 1.934 12C3.226 16.338 7.244 19.5 12 19.5c.993 0 1.953-.138 2.863-.395M6.228 6.228A10.451 10.451 0 0 1 12 4.5c4.756 0 8.773 3.162 10.065 7.498a10.522 10.522 0 0 1-4.293 5.774M6.228 6.228 3 3m3.228 3.228 3.65 3.65m7.894 7.894L21 21m-3.228-3.228-3.65-3.65m0 0a3 3 0 1 0-4.243-4.243m4.242 4.242L9.88 9.88"
                  />
                </svg>
              </template>
            </span>
          </div>
          <div class="relative mb-4">
            <input
                :type="showConfirmPassword ? 'text' : 'password'"
                placeholder="Подтвердите пароль"
                class="w-full px-4 py-2 pl-10 border rounded-md focus:outline-none focus:ring-2 focus:ring-gray-300 text-gray-700"
                v-model="confirmPassword"
                :class="{
                'border-red-500': (!confirmPassword || !confirmPasswordMatch) && isSubmitted,
                'border-gray-300': (confirmPassword && confirmPasswordMatch) || !isSubmitted,
              }"
            />
            <span class="absolute left-3 top-2 text-gray-500">
              <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke-width="1.5"
                  stroke="currentColor"
                  class="size-6"
              >
                <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M16.5 10.5V6.75a4.5 4.5 0 1 0-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 0 0 2.25-2.25v-6.75a2.25 2.25 0 0 0-2.25-2.25H6.75a2.25 2.25 0 0 0-2.25 2.25v6.75a2.25 2.25 0 0 0 2.25 2.25Z"
                />
              </svg>
            </span>
            <span
                class="absolute right-3 top-2 text-gray-500 cursor-pointer"
                @click="togglePassword('confirmPassword')"
            >
              <template v-if="showConfirmPassword">
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke-width="1.5"
                    stroke="currentColor"
                    class="size-6"
                >
                  <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178Z"
                  />
                  <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"
                  />
                </svg>
              </template>
              <template v-else>
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke-width="1.5"
                    stroke="currentColor"
                    class="size-6"
                >
                  <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M3.98 8.223A10.477 10.477 0 0 0 1.934 12C3.226 16.338 7.244 19.5 12 19.5c.993 0 1.953-.138 2.863-.395M6.228 6.228A10.451 10.451 0 0 1 12 4.5c4.756 0 8.773 3.162 10.065 7.498a10.522 10.522 0 0 1-4.293 5.774M6.228 6.228 3 3m3.228 3.228 3.65 3.65m7.894 7.894L21 21m-3.228-3.228-3.65-3.65m0 0a3 3 0 1 0-4.243-4.243m4.242 4.242L9.88 9.88"
                  />
                </svg>
              </template>
            </span>
          </div>
          <button
              class="w-full bg-gray-300 text-gray-700 py-2 rounded-md hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-300"
              type="submit"
          >
            Зарегистрироваться
          </button>

          <p v-if="errorMessage" class="text-red-500 mt-2">
            {{ errorMessage }}
          </p>
        </form>
        <p class="text-gray-500 mt-4 text-center font-sans">
          Уже есть аккаунт?
          <router-link to="/login" class="text-blue-500">Войти</router-link>
        </p>
      </div>

      <div
          class="w-1/2 bg-[#1B374D] rounded-2xl flex items-center justify-center p-8 m-10"
      >
        <img
            :src="registerArt"
            alt="Иллюстрация регистрации"
            class="max-w-full rounded-2xl"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router';
import registerArt from '@/assets/images/картинка_2.png';

export default {
  name: 'Register',
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
      showConfirmPassword: false, // Add this line
      registerArt: registerArt, // Убедитесь, что путь к изображению верен
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
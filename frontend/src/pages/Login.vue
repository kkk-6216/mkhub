<template>
  <div class="flex items-center justify-center h-screen bg-gray-100">
    <div class="flex w-screen h-screen flex-col items-center justify-center">
      <h2 class="text-3xl font-semibold mb-2 text-left font-sans">Вход</h2>
      <p class="text-gray-500 mb-6 text-left font-sans">Пожалуйста, войдите, чтобы продолжить</p>
      <form class="w-80" @submit.prevent="login">
        <!-- Имя пользователя -->
        <InputField
            v-model="username"
            :is-submitted="isSubmitted"
            placeholder="Имя пользователя"
        >
          <template #icon>
            <div class="w-6 h-6 stroke-1 ">
              <img src="../assets/icons/LoginRegister-Icons/user-icon.svg" alt="User Icon" />
            </div>
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
            <div class="w-6 h-6 stroke-1 ">
              <img src="../assets/icons/LoginRegister-Icons/lock-closed-icon.svg" alt="User Icon"/>
            </div>
          </template>
          <template #toggleIcon>
            <div v-if="showPassword" class="w-6 h-6 stroke-1 ">
              <img src="../assets/icons/LoginRegister-Icons/eye-icon.svg" alt="User Icon"/>
            </div>
            <div v-else class="w-6 h-6 stroke-1 ">
              <img src="../assets/icons/LoginRegister-Icons/eye-slash-icon.svg" alt="User Icon"/>
            </div>
          </template>
        </PasswordField>

        <button
            class="w-full bg-gray-300 text-gray-700 py-2 rounded-md hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-300"
            type="submit"
        >
          Войти
        </button>
        <p v-if="errorMessage" class="text-red-500 mt-2">{{ errorMessage }}</p>
      </form>
      <div>
        <p class="text-gray-500 mt-4 text-center font-sans">
          У меня нет аккаунта
          <router-link to="/register" class="text-blue-500">Зарегистрироваться</router-link>
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
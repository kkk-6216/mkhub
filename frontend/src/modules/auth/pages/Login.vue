<template>
  <div class="flex items-center justify-center h-screen bg-white-100">
    <div class="flex w-screen h-screen flex-col items-center justify-center">
      <h2 class="text-3xl font-semibold mb-[20px] text-left">Вход</h2>
      <p class="text-gray-500 mb-6 text-left">
        Пожалуйста, войдите, чтобы продолжить
      </p>
      <form class="w-80" @submit.prevent="login">
        <!-- Имя пользователя -->
        <InputField
          v-model="username"
          :is-submitted="isSubmitted"
          placeholder="Имя пользователя"
        >
          <template #icon>
            <div class="w-6 h-6 stroke-1">
              <img
                src="../../../assets/icons/LoginRegister-Icons/user-icon.svg"
                alt="User Icon"
              />
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
            <div class="w-6 h-6 stroke-1">
              <img
                src="../../../assets/icons/LoginRegister-Icons/lock-closed-icon.svg"
                alt="User Icon"
              />
            </div>
          </template>
          <template #toggleIcon>
            <div v-if="showPassword" class="w-6 h-6 stroke-1">
              <img
                src="../../../assets/icons/LoginRegister-Icons/eye-icon.svg"
                alt="User Icon"
              />
            </div>
            <div v-else class="w-6 h-6 stroke-1">
              <img
                src="../../../assets/icons/LoginRegister-Icons/eye-slash-icon.svg"
                alt="User Icon"
              />
            </div>
          </template>
        </PasswordField>

        <button
          class="w-full bg-[#0d2856] text-white py-2 rounded-[10px] hover:bg-[#092040] hover:scale-101 focus:outline-none focus:ring-2 focus:ring-gray-300 transition duration-300 ease-in-out"
          type="submit"
        >
          Войти
        </button>
        <p v-if="errorMessage" class="text-red-500 mt-2 text-center">
          {{ errorMessage }}
        </p>
      </form>
      <div class="w-[100px] h-[1px] m-5 bg-[var(--main-color)] rounded-[10px]"></div>

      <div>
        <p class="text-gray-500 mt-2 text-center">
          У меня нет аккаунта&nbsp;
          <router-link to="/register" class="text-[var(--main-color)]"
            >Зарегистрироваться</router-link
          >
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import InputField from '@/modules/auth/components/InputField.vue';
import PasswordField from '@/modules/auth/components/PasswordField.vue';
import { useRouter } from 'vue-router';
import {useAuthStore} from "@/store/auth.js";

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
        await this.authStore.login({
          username: this.username,
          password: this.password
        });
        this.router.push("/");
      } catch (error) {
        if (error.response && error.response.status === 403) {
          this.errorMessage = "Неверные учетные данные";
        } else if (error.request) {
          this.errorMessage = "Сервер не отвечает. Попробуйте позже.";
        } else {
          this.errorMessage = "Возникла ошибка";
        }
      }
    },
  },
  beforeCreate() {
    this.authStore = useAuthStore();
    this.router = useRouter();
  },
};
</script>

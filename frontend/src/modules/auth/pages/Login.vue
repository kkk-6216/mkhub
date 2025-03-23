<template>
  <div class="flex items-center justify-center h-screen">
    <div class="flex w-screen h-screen flex-col items-center justify-center">
      <h2 class="text-3xl font-semibold mb-[20px] text-main text-left">Вход</h2>
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

        <DefaultButton label="Войти" type="submit" variant="primary" fullWidth></DefaultButton>

        <p v-if="errorMessage" class="text-red-500 mt-2 text-center">
          {{ errorMessage }}
        </p>

      </form>
      <div class="w-[100px] h-[1px] m-5 bg-main rounded-[10px]"></div>

      <div>
        <p class="text-gray-500 mt-2 text-center">
          У меня нет аккаунта&nbsp;
          <router-link to="/register" class="text-main"
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
import DefaultButton from "@/components/buttons/DefaultButton.vue";

export default {
  name: 'Login',
  components: {
    DefaultButton,
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

        // if (this.authStore.user?.role === "ROLE_ADMIN") {
        //   this.$router.push("/home");
        // } else {
        //   this.$router.push("/home");
        // }
        this.$router.push("/home");

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

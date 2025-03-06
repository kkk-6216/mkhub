<template>
  <div class="flex items-center justify-center min-h-screen bg-white">
    <div class="flex w-screen h-screen flex-col items-center justify-center">
      <h2 class="text-3xl font-semibold mb-2 text-left">Регистрация</h2>
      <p class="text-gray-500 mb-6 text-left">Создать аккаунт</p>
      <form class="w-80" method="post" @submit.prevent="register">
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

        <!-- Email -->
        <InputField
            v-model="email"
            :is-submitted="isSubmitted"
            :is-valid="emailValid"
            type="text"
            placeholder="Email"
        >
          <template #icon>
            <div class="w-6 h-6 stroke-1">
              <img
                src="../../../assets/icons/LoginRegister-Icons/email-icon.svg"
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
          @togglePassword="togglePassword('password')"
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

        <!-- Подтверждение пароля -->
        <PasswordField
          v-model="confirmPassword"
          :is-submitted="isSubmitted"
          :is-valid="confirmPasswordMatch"
          placeholder="Подтвердите пароль"
          @togglePassword="togglePassword('confirmPassword')"
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
            <div v-if="showConfirmPassword" class="w-6 h-6 stroke-1">
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
            class="w-full bg-[var(--main-color)] text-white py-2 rounded-[10px]
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

      <div class="w-[100px] h-[1px] m-5 bg-[var(--main-color)] rounded-[10px]"></div>

      <p class="text-gray-500 mt-4 text-center">
        Уже есть аккаунт?&nbsp;
        <router-link to="/login" class="text-[var(--main-color)]">Войти</router-link>
      </p>
    </div>
  </div>
</template>

<script>
import InputField from '@/modules/auth/components/InputField.vue';
import PasswordField from '@/modules/auth/components/PasswordField.vue';
import {useAuthStore} from "@/store/auth.js";
import {useRouter} from "vue-router";

export default {
  name: 'Register',
  components: {
    InputField,
    PasswordField,
  },
  data() {
    return {
      username: '',
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
          !this.email ||
          !this.password ||
          !this.confirmPassword
      ) {
        this.errorMessage = 'Необходимо заполнить все поля.';
        return;
      }

      if (!this.emailValid) {
        this.errorMessage =
            'Пожалуйста, используйте адрес электронной почты @gmail.com, @icloud.com или @manas.edu.kg.';
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
        await this.authStore.register({
          username: this.username,
          email: this.email,
          password: this.password
        });
        this.router.push("/");
      } catch (error) {
        console.log(error)
        if (error.response && error.response.status === 400) {
          this.errorMessage = error.response.data || "Ошибка регистрации";
        } else if (error.request) {
          this.errorMessage = "Сервер не отвечает. Попробуйте позже.";
        } else {
          this.errorMessage = "Произошла ошибка регистрации.";
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

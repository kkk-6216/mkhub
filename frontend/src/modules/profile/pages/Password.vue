<template>
  <div class="border-b border-gray-900/10 pb-12 grid grid-cols-1 sm:grid-cols-3 gap-6">
    <!-- Left section with heading -->
    <div class="sm:col-span-1">
      <h2 class="text-base/7 font-semibold text-gray-900">Пароль</h2>
      <p class="text-sm/6 text-gray-600">Пожалуйста, введите текущий пароль, чтобы изменить его.</p>
    </div>

    <!-- Right section with form -->
    <div class="sm:col-span-2">
      <form @submit.prevent="handleConfirmation">
        <div class="mt-6 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
          <div class="sm:col-span-4">
            <label for="currentPassword" class="block text-sm/6 font-medium text-gray-900">Текущий пароль</label>
            <div class="mt-2">
              <input type="password" id="currentPassword" v-model="currentPassword" autocomplete="current-password"
                     class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
                     placeholder="••••••••" required/>
            </div>
          </div>

          <div class="sm:col-span-4">
            <label for="newPassword" class="block text-sm/6 font-medium text-gray-900">Новый пароль</label>
            <div class="mt-2">
              <input type="password" id="newPassword" v-model="newPassword" autocomplete="new-password"
                     class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
                     placeholder="••••••••" required/>
              <p v-if="newPasswordError" class="text-sm/6 text-red-500 mt-1">{{ newPasswordError }}</p>
            </div>
          </div>

          <div class="sm:col-span-4">
            <label for="confirmPassword" class="block text-sm/6 font-medium text-gray-900">Подтвердите новый пароль</label>
            <div class="mt-2">
              <input type="password" id="confirmPassword" v-model="confirmPassword" autocomplete="new-password"
                     class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
                     placeholder="••••••••" required/>
              <p v-if="confirmPasswordError" class="text-sm/6 text-red-500 mt-1">{{ confirmPasswordError }}</p>
            </div>
          </div>

          <div class="sm:col-span-4 flex justify-end">
            <DefaultButton label="Изменить пароль" variant="primary" :disabled="isButtonDisabled" @click="handleConfirmation"/>
          </div>
        </div>
      </form>

      <!-- Confirmation Modal -->
      <ConfirmationModal
          :show="showConfirmationModal"
          title="Изменить пароль"
          :message="confirmationMessage"
          confirm-text="Изменить"
          cancel-text="Отмена"
          @confirm="handleSubmit"
          @cancel="closeConfirmationModal"
      />

    </div>
  </div>
</template>

<script>
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import ConfirmationModal from "@/modules/profile/components/ConfirmationModal.vue";
import {useAuthStore} from "@/store/auth.js";

export default {
  name: 'Password',
  components: {
    DefaultButton,
    ConfirmationModal,  // Register
  },
  inject: ['showAlert'],
  data() {
    return {
      currentPassword: '',
      newPassword: '',
      confirmPassword: '',
      showConfirmationModal: false,
      newPasswordError: '',
      confirmPasswordError: '',
      isFormValid: false,
    };
  },
  beforeCreate() {
    this.authStore = useAuthStore();
  },
  computed: {
    username() {
      return this.authStore.user?.sub || 'Имя пользователя';
    },
    isButtonDisabled() {
      return !this.isFormValid || !this.currentPassword || !this.newPassword || !this.confirmPassword;
    },
    confirmationMessage() {
      if (!this.isFormValid) {
        return 'Пожалуйста, исправьте ошибки в форме.';
      }
      return 'Вы уверены, что хотите изменить пароль? Это действие нельзя отменить.';
    }
  },
  watch: {
    newPassword() {
      this.validateNewPassword();
      this.validateConfirmPassword();
      this.updateFormValidity();
    },
    confirmPassword() {
      this.validateConfirmPassword();
      this.updateFormValidity();
    }
  },
  methods: {
    validateNewPassword() {
      if (this.newPassword.length < 6) {
        this.newPasswordError = 'Пароль должен содержать не менее 6 символов.';
      } else {
        this.newPasswordError = '';
      }
    },
    validateConfirmPassword() {
      if (this.newPassword !== this.confirmPassword && this.confirmPassword.length > 0) {
        this.confirmPasswordError = 'Пароли не совпадают.';
      } else {
        this.confirmPasswordError = '';
      }
    },
    updateFormValidity() {
      this.isFormValid = !this.newPasswordError && !this.confirmPasswordError;
    },
    handleConfirmation() {
      if (this.isFormValid) {
        this.showConfirmationModal = true;
      } else {
        this.showAlert('error', 'Пожалуйста, заполните форму корректно.');
      }

    },
    closeConfirmationModal() {
      this.showConfirmationModal = false;
    },
    async handleSubmit() {
      this.newPasswordError = '';
      this.confirmPasswordError = '';
      this.isFormValid = true;

      const passwordData = {
        username: this.username,
        oldPassword: this.currentPassword,
        newPassword: this.newPassword,
        confirmNewPassword: this.confirmPassword,
      };

      const result = await this.authStore.changePassword(passwordData);

      if (result.success) {

        this.currentPassword = '';
        this.newPassword = '';
        this.confirmPassword = '';
        this.showConfirmationModal = false;
        this.newPasswordError = '';
        this.confirmPasswordError = '';
        this.isFormValid = false;

        this.showAlert('success', 'Пароль успешно изменён.');
      } else {

        if (result.errors) {
          if (result.errors.newPassword) {
            this.newPasswordError = result.errors.newPassword;
          }
          if (result.errors.confirmNewPassword) {
            this.confirmPasswordError = result.errors.confirmNewPassword;
          }
          if (result.errors.oldPassword) {
            this.showAlert('error', result.errors.oldPassword);
          }
          if (result.errors.error) {
            this.showAlert('error', result.errors.error);
          }
        } else {
          this.showAlert('error', 'Произошла ошибка при смене пароля.');
        }

        this.closeConfirmationModal();
      }
    },

  },
}
</script>

<style scoped>
/* Styles for the page */
</style>
import { defineStore } from 'pinia';
import { jwtDecode } from 'jwt-decode';

import apiClient from '../api/axiosInstance.js';
import router from "@/router/index.js";

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: localStorage.getItem('accessToken')
      ? jwtDecode(localStorage.getItem('accessToken'))
      : null,
    accessToken: localStorage.getItem('accessToken') || null,
    refreshToken: localStorage.getItem('refreshToken') || null,
    userAvatar: localStorage.getItem('userAvatar') || null,
  }),

  actions: {
    async login(credentials) {
      try {
        const response = await apiClient.post('/auth/sign-in', credentials);

        this.accessToken = response.data.accessToken;
        this.refreshToken = response.data.refreshToken;

        localStorage.setItem('accessToken', this.accessToken);
        localStorage.setItem('refreshToken', this.refreshToken);

        this.user = jwtDecode(this.accessToken);
        await this.getAvatar();

      } catch (error) {
        throw error;
      }
    },

    async register(userData) {
      try {
        const response = await apiClient.post('/auth/sign-up', userData);
        console.log(response);
        this.accessToken = response.data.accessToken;
        this.refreshToken = response.data.refreshToken;

        localStorage.setItem('accessToken', this.accessToken);
        localStorage.setItem('refreshToken', this.refreshToken);
        this.user = jwtDecode(this.accessToken);

        await this.getAvatar();

      } catch (error) {
        throw error;
      }
    },

    async refreshAuthToken() {
      try {
        const response = await apiClient.post('/auth/refresh', {
          refreshToken: this.refreshToken,
        });

        if (!response.data.accessToken) {
          console.warn("Не получен новый accessToken");
          await this.logout();
          return null;
        }

        this.accessToken = response.data.accessToken;
        localStorage.setItem('accessToken', this.accessToken);

        if (response.data.refreshToken) {
          this.refreshToken = response.data.refreshToken;
          localStorage.setItem('refreshToken', this.refreshToken);
        }

        try {
          this.user = jwtDecode(this.accessToken);
        } catch (decodeError) {
          console.error("Ошибка декодирования токена:", decodeError.message);
          await this.logout();
          return null;
        }

        return response;
      } catch (error) {
        console.warn("Ошибка обновления токена:", error.message);
        return null;
      }
    },

    async logout() {
      try {
        const decoded = jwtDecode(this.refreshToken);
        const now = Math.floor(Date.now() / 1000);
        if (!decoded.exp || decoded.exp < now) {
          return;
        }

        if (this.accessToken) {
          await apiClient.post('/auth/sign-out', {
            refreshToken: this.refreshToken,
          });
        }
      } catch (error) {
        console.error("Ошибка при выходе:", error.message);
      } finally {
        this.user = null;
        this.accessToken = null;
        this.refreshToken = null;
        this.userAvatar = null;
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('userAvatar');

        if (router.currentRoute.value.path !== "/login") {
          await router.push("/login");
        }
      }
    },

    async uploadAvatar(formData) {
      try {
        const response = await apiClient.post("/files/avatar", formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });
        console.log(response);

        this.userAvatar = response.data.imageUrl;
        localStorage.setItem('userAvatar', this.userAvatar);

      } catch (error) {
        console.error("Ошибка загрузки файла:", error);
      }
    },

    async updateAvatar(formData) {
      try {
        const response = await apiClient.put("/files/avatar", formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });
        console.log(response);

        this.userAvatar = response.data.imageUrl;
        localStorage.setItem('userAvatar', this.userAvatar);

      } catch (error) {
        console.error("Ошибка загрузки файла:", error);
      }
    },

    async getAvatar() {
      try {
        const response = await apiClient.get("/files/avatar/link");
        console.log(response);

        this.userAvatar = response.data.imageUrl;
        localStorage.setItem('userAvatar', this.userAvatar);

      } catch (error) {
        console.error("Ошибка загрузки файла:", error);
      }
    },

    async changePassword(passwordData) {
      try {
        const response = await apiClient.put("/auth/change-password", passwordData);

        return { success: true, message: response.data };

      } catch (error) {
        if (error.response && error.response.data) {
          return {
            success: false,
            errors: error.response.data,
          };
        } else {
          return {
            success: false,
            errors: { error: "Неизвестная ошибка при смене пароля" },
          };
        }
      }
    },



  },
});

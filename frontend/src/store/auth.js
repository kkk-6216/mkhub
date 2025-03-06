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
      } catch (error) {
        throw error;
      }
    },

    async refreshToken() {
      if (!this.refreshToken) {
        console.warn("Нет refreshToken, выходим из системы");
        await this.logout();
        return;
      }

      try {
        const response = await apiClient.post('/auth/refresh', {
          refreshToken: this.refreshToken,
        });

        if (!response.data.accessToken) {
          throw new Error("Не получен новый accessToken");
        }

        this.accessToken = response.data.accessToken;

        localStorage.setItem('accessToken', this.accessToken);
        this.user = jwtDecode(this.accessToken);

        if (response.data.refreshToken) {
          this.refreshToken = response.data.refreshToken;
          localStorage.setItem('refreshToken', this.refreshToken);
        }
      } catch (error) {
        console.warn("Ошибка обновления токена:", error.message);

        if (error.response?.status === 401) {
          console.warn("Сервер вернул 401, выходим из системы...");
          await this.logout();
        }

        throw new Error("Сессия истекла. Пожалуйста, войдите заново.");
      }
    },

    async logout() {
      try {
        if (!this.accessToken) {
          console.warn("AccessToken отсутствует. Пробуем обновить перед выходом...");
          await this.refreshToken();
        }

        if (this.accessToken) {
          await apiClient.post('/auth/sign-out', {
            refreshToken: this.refreshToken,
          }, {
            headers: {
              Authorization: `Bearer ${this.accessToken}`
            }
          });
        }
      } catch (error) {
        console.error("Ошибка при выходе:", error.message);
      }

      this.user = null;
      this.accessToken = null;
      this.refreshToken = null;
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');

      await router.push("/login");
    }

  },
});

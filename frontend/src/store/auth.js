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
        if (this.accessToken) {
          await apiClient.post('/auth/sign-out', {
            refreshToken: this.refreshToken,
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

      if (router.currentRoute.value.path !== "/login") {
        await router.push("/login");
      }
    }


  },
});

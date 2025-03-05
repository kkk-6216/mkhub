    import { defineStore } from 'pinia';
    import { jwtDecode } from 'jwt-decode';

    import apiClient from "../api/axiosInstance.js";

    export const useAuthStore = defineStore('auth', {
        state: () => ({
            user: localStorage.getItem('accessToken') ? jwtDecode(localStorage.getItem('accessToken')) : null,
            accessToken: localStorage.getItem('accessToken') || null,
            refreshToken: localStorage.getItem('refreshToken') || null,
        }),

        actions: {
            async login(credentials) {
                try {
                    const response = await apiClient.post('/auth/sign-in', credentials);
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

            async register(userData) {
                try {
                    const response = await apiClient.post('/auth/sign-up', userData);
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
                try {
                    const response = await apiClient.post('/auth/refresh', { refreshToken: this.refreshToken });
                    this.accessToken = response.data.accessToken;

                    localStorage.setItem('accessToken', this.accessToken);
                    this.user = jwtDecode(this.accessToken);

                    if (response.data.refreshToken) {
                        this.refreshToken = response.data.refreshToken;
                        localStorage.setItem('refreshToken', this.refreshToken);
                    }
                } catch (error) {
                    this.logout();
                }
            },

            async logout() {
                try {
                    await apiClient.post('/auth/sign-out', { refreshToken: this.refreshToken });
                } catch (error) {
                    console.error('Ошибка при выходе:', error);
                }
                this.user = null;
                this.accessToken = null;
                this.refreshToken = null;
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
            }
        }
    });
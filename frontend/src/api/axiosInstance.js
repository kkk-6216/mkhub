import axios from 'axios';
import {useAuthStore} from "@/store/auth.js";
import router from "@/router/index.js";

const apiClient = axios.create({
  baseURL: 'http://localhost:8000/',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Перехватчик запросов: добавляет accessToken
apiClient.interceptors.request.use(
    (config) => {
      const authStore = useAuthStore();
      if (authStore.accessToken) {
        config.headers["Authorization"] = `Bearer ${authStore.accessToken}`;
      }
      return config;
    },
    (error) => Promise.reject(error)
);

let isRefreshing = false;
let refreshSubscribers = [];

apiClient.interceptors.response.use(
    (response) => response,
    async (error) => {
        const authStore = useAuthStore();
        const originalRequest = error.config;

        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            console.log(1)
            if (!authStore.refreshToken || isRefreshing) {
                console.warn("Нет refreshToken. Выход из системы.");
                await authStore.logout();
                await router.push("/login");
                return Promise.reject(error);
            }
            console.log(2)
            if (!isRefreshing) {
                isRefreshing = true;
                console.log(3)
                try {
                    await authStore.refreshAuthToken();
                    isRefreshing = false;
                    refreshSubscribers.forEach((cb) => cb(authStore.accessToken));
                    refreshSubscribers = [];
                    originalRequest.headers["Authorization"] = `Bearer ${authStore.accessToken}`;
                    return apiClient(originalRequest);
                } catch (refreshError) {
                    isRefreshing = false;
                    await authStore.logout();
                    await router.push("/login");
                    return Promise.reject(refreshError);
                }
            } else {
                return new Promise((resolve) => {
                    refreshSubscribers.push((token) => {
                        originalRequest.headers["Authorization"] = `Bearer ${token}`;
                        resolve(apiClient(originalRequest));
                    });
                });
            }
        }

        return Promise.reject(error);
    }
);


export default apiClient;

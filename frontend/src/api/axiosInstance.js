import axios from 'axios';
import {useAuthStore} from "@/store/auth.js";
import router from "@/router/index.js";

const apiClient = axios.create({
  baseURL: 'http://localhost:8000/api/',
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

apiClient.interceptors.response.use(
    (response) => response,
    async (error) => {
      const authStore = useAuthStore();
      const originalRequest = error.config;


      if (error.response?.status === 401 && !originalRequest._retry) {
        originalRequest._retry = true;

          try {
              if (!authStore.refreshToken) {
                  console.warn("Нет refreshToken. Выход из системы.");
                  await authStore.logout();
                  await router.push("/login");
                  return;
              }

              await authStore.refreshAuthToken();
              originalRequest.headers["Authorization"] = `Bearer ${authStore.accessToken}`;
              return apiClient(originalRequest);
          } catch (refreshError) {
              await authStore.logout();
              await router.push("/login");
          }
      }

      return Promise.reject(error);
    }
);

export default apiClient;

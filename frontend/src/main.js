import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import "./styles/index.css";
import { createPinia } from "pinia";
import { useAuthStore } from "@/store/auth.js";

const pinia = createPinia();
const app = createApp(App);

app.use(pinia);
app.use(router);

const authStore = useAuthStore();

(async () => {
    if (authStore.accessToken && authStore.refreshToken) {
        try {
            await authStore.refreshToken();
        } catch (error) {
            console.warn("Ошибка обновления токена:", error.message);
            await authStore.logout();
        }
    } else {
        console.warn("Не найден accessToken или refreshToken, очищаем localStorage.");
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
    }
    app.mount("#app");
})();

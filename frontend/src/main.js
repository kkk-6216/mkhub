import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import "./styles/index.css";
import { createPinia } from "pinia";
import { useAuthStore } from "@/store/auth.js";
import {jwtDecode} from "jwt-decode";

const pinia = createPinia();
const app = createApp(App);

app.use(pinia);
app.use(router);

const authStore = useAuthStore();

(async () => {

    if (authStore.accessToken && authStore.refreshToken) {
        try {
            const decoded = jwtDecode(authStore.accessToken);
            const now = Math.floor(Date.now() / 1000);

            if (decoded.exp < now) {
                await authStore.refreshAuthToken();
            }
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

import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import "./styles/index.css";
import { createPinia } from "pinia";
import { useAuthStore } from "@/store/auth.js";
import { jwtDecode } from "jwt-decode";

const pinia = createPinia();
const app = createApp(App);

app.use(pinia);
app.use(router);

const authStore = useAuthStore();

async function initializeAuth() {
    const { accessToken, refreshToken, logout, refreshAuthToken } = authStore;

    if (accessToken && refreshToken) {
        try {
            const decoded = jwtDecode(accessToken);
            const now = Math.floor(Date.now() / 1000);
            if (!decoded.exp || decoded.exp < now) {
                await refreshAuthToken();
            }
        } catch (e) {
            console.warn("Ошибка авторизации:", e.message);
            await logout();
        }
    } else {
        console.warn("Нет токенов — очищаем localStorage");
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
    }
}

(async () => {
    await initializeAuth();
    app.mount("#app");
})();

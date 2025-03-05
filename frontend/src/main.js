import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // Импорт роутера
import './styles/index.css'


const app = createApp(App)

// Установка Vue Router
app.use(router)

app.mount('#app')
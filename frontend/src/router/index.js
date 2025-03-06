
import { createRouter, createWebHistory } from 'vue-router';

import Login from '../modules/auth/pages/Login.vue';
import Register from '../modules/auth/pages/Register.vue';
import Home from '../pages/Home.vue';
import Courses from '../pages/Courses.vue';
import Resources from '../pages/Resources.vue';
import Messages from '../pages/Messages.vue';
import Settings from '../pages/Settings.vue';
import {useAuthStore} from "@/store/auth.js";
import {defineAsyncComponent} from "vue";

const DefaultLayout = defineAsyncComponent(() => import("@/layouts/DefaultLayout.vue"));

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: {
            layout: 'minimal',
            requiresGuest: true
        }

    },
    {
        path: '/register',
        name: 'Register',
        component: Register,
        meta: {
            layout: 'minimal',
            requiresGuest: true
        }
    },
    {
        path: '/',
        component: DefaultLayout,
        children: [
            {
                path: 'home',
                name: 'Home',
                component: Home
            },
            {
                path: 'courses', // /courses
                name: 'Courses',
                component: Courses,
                meta: { requiresAuth: true }
            },
            {
                path: 'resources', // /resources
                name: 'Resources',
                component: Resources,
                meta: { requiresAuth: true }
            },
            {
                path: 'messages', // /messages
                name: 'Messages',
                component: Messages,
                meta: { requiresAuth: true }
            },
            {
                path: 'settings', // /settings
                name: 'Settings',
                component: Settings,
                meta: { requiresAuth: true }
            },
            {
                path: '',
                redirect: '/home'
            }
        ]
    },

];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach(async (to, from, next) => {
    const authStore = useAuthStore();
    const isAuthenticated = !!authStore.accessToken;

    if (to.meta.requiresAuth && !isAuthenticated) {
        next("/login");
    } else if (to.meta.requiresGuest && isAuthenticated) {
        next("/");
    } else {
        next();
    }
});

export default router;

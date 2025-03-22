
import { createRouter, createWebHistory } from 'vue-router';

import Home from '../pages/Home.vue';
import Courses from '../pages/Courses.vue';
import Resources from '../pages/Resources.vue';
import Messages from '../pages/Messages.vue';
import Settings from '../pages/Settings.vue';
import {useAuthStore} from "@/store/auth.js";

import authRoutes from '@/modules/auth/router/index.js';
import Faculty from "@/modules/faculty/pages/Faculty.vue";
import Department from "../modules/Department/pages/Department.vue";

const routes = [
    ...authRoutes,
    {
        path: '/',
        component: () => import("@/layouts/DefaultLayout.vue"),
        children: [
            {
                path: 'home',
                name: 'Home',
                component: Home
            },
            {
                path: 'courses',
                name: 'Courses',
                component: Courses,
                meta: { requiresAuth: true }
            },
            {
                path: 'resources',
                name: 'Resources',
                component: Resources,
                meta: { requiresAuth: true }
            },
            {
                path: 'messages',
                name: 'Messages',
                component: Messages,
                meta: { requiresAuth: true }
            },
            {
                path: 'faculties',
                name: 'Faculty',
                component: Faculty,
                meta: { requiresAuth: true }
            },
            {
                path: 'directions',
                name: 'Department',
                component: Department,
                meta: { requiresAuth: true }
            },
            {
                path: '',
                redirect: '/home'
            },
        ]
    },
    {
        path: "/:pathMatch(.*)*",
        name: "NotFound",
        component: () => import("@/pages/NotFound.vue"),
        meta: {
            layout: 'minimal',
        },
        beforeEnter: (to, from, next) => {
            const isAuthenticated = localStorage.getItem("accessToken");
            if (!isAuthenticated) {
                next("/login");
            } else {
                next();
            }
        },
    }

];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach(async (to, from, next) => {
    const authStore = useAuthStore();
    const isAuthenticated = !!authStore.accessToken;

    if (to.meta.requiresAuth && !isAuthenticated) {
        next({ path: "/login", query: { redirect: to.fullPath } });
    } else if (to.meta.requiresGuest && isAuthenticated) {
        next("/");
    } else {
        next();
    }
});

export default router;

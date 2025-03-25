
import { createRouter, createWebHistory } from 'vue-router';

import Home from '../pages/student/Home.vue';
import Courses from '../pages/student/Courses.vue';
import Resources from '../pages/student/Resources.vue';
import Messages from '../pages/student/Messages.vue';
import Settings from '../pages/student/Settings.vue';
import {useAuthStore} from "@/store/auth.js";

 import UsersAdminPage from '../pages/admin/UsersAdminPage.vue';
// import ContentPage from '../pages/admin/ContentPage.vue';
// import FacultiesPage from '../pages/admin/FacultiesPage.vue';
// import DepartmentPage from '../pages/admin/DepartmentPage.vue';
import MonitoringPage from '../pages/admin/MonitoringPage.vue';
import ApiGatewayPage from '../pages/admin/ApiGatewayPage.vue';

import DashboardPage from '../pages/moderator/DashboardPage.vue';
 import UsersPage from '../pages/moderator/UsersPage.vue';
import DepartmentPage from '../pages/moderator/DepartmentPage.vue';
import FacultiesPage from '../pages/moderator/FacultiesPage.vue';
import ModerationPage from '../pages/moderator/ModerationPage.vue';
import RequestsPage from '../pages/moderator/RequestsPage.vue';

import authRoutes from '@/modules/auth/router/index.js';
import profileRoutes from '@/modules/profile/router/index.js';

const routes = [
    ...authRoutes,
    ...profileRoutes,
    {
        path: '/',
        component: () => import("@/layouts/DefaultLayout.vue"),
        meta: { layout: "default" },
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
                path: 'settings',
                name: 'Settings',
                component: Settings,
                meta: { requiresAuth: true }
            },
            {
                path: '',
                redirect: '/home'
            },
        ]
    },
    {
        path: '/moderator',
        component: () => import("@/layouts/ModeratorLayout.vue"),
        meta: { requiresAuth: true, roles: ['ROLE_MODERATOR'], layout: "moderator" },
        children: [
            {
                path: '/moderator/dashboard',
                name: 'DashboardPage',
                component: DashboardPage,
            },
            {
                path: '/moderator/users',
                name: 'UsersPage',
                component: UsersPage,
            },
            {
                path: '/moderator/faculties',
                name: 'FacultiesPage',
                component: FacultiesPage,
            },
            {
                path: '/moderator/departments',
                name: 'DepartmentPage',
                component: DepartmentPage,
            },
            {
                path: '/moderator/moderation',
                name: 'ModerationPage',
                component: ModerationPage,
            },
            {
                path: '/moderator/requests',
                name: 'RequestsPage',
                component: RequestsPage,
            },
            {
                path: '',
                redirect: '/moderator/dashboard'
            },
        ]
    },
    {
        path: '/admin',
        component: () => import("@/layouts/AdminLayout.vue"),
        meta: { requiresAuth: true, roles: ['ROLE_ADMIN'], layout: "admin" },
        children: [
            {
                path: '/admin/monitoring',
                name: 'MonitoringPage',
                component: MonitoringPage,
            },
            {
                path: '/admin/users',
                name: 'UsersAdminPage',
                component: UsersAdminPage,
            },
            {
                path: '/admin/api-gateway',
                name: 'ApiGatewayPage',
                component: ApiGatewayPage,
            },
            {
                path: '',
                redirect: '/admin/monitoring'
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
    const user = authStore.user;

    // Проверяем аутентификацию
    if (to.meta.requiresAuth && !authStore.accessToken) {
        next("/login");
    }
    // Проверяем роль администратора
    else if (to.meta.roles && !to.meta.roles.includes(user?.role)) {
        next("/"); // Если нет прав, отправляем на главную
    }
    // Всё ок - продолжаем навигацию
    else {
        next();
    }
});

export default router;

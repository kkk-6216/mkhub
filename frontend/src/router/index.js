
import { createRouter, createWebHistory } from 'vue-router';

import Home from '../pages/student/Home.vue';
import Courses from '../pages/student/Courses.vue';
import Resources from '../pages/student/Resources.vue';
import Messages from '../pages/student/Messages.vue';
import Settings from '../pages/student/Settings.vue';
import {useAuthStore} from "@/store/auth.js";


import MyDetails from '../pages/profile/MyDetails.vue';
import EditMyDetails from '../pages/profile/EditMyDetails.vue';
import Profile from '../pages/profile/Profile.vue';
import EditProfile from '../pages/profile/EditProfile.vue';
import Password from '../pages/profile/Password.vue';
import Notifications from '../pages/profile/Notifications.vue';

import UsersPage from '../pages/admin/UsersPage.vue';
import ContentPage from '../pages/admin/ContentPage.vue';
import FacultiesPage from '../pages/admin/FacultiesPage.vue';
import DepartmentPage from '../pages/admin/DepartmentPage.vue';
import MonitoringPage from '../pages/admin/MonitoringPage.vue';
import ApiGatewayPage from '../pages/admin/ApiGatewayPage.vue';

import authRoutes from '@/modules/auth/router/index.js';

const routes = [
    ...authRoutes,
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
            },
            {
                path: 'resources',
                name: 'Resources',
                component: Resources,
            },
            {
                path: 'messages',
                name: 'Messages',
                component: Messages,
            },
            {
                path: 'settings',
                name: 'Settings',
                component: Settings,
            },
            {
                path: '',
                redirect: '/home'
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
                name: 'UsersPage',
                component: UsersPage,
            },
            {
                path: '/admin/content',
                name: 'ContentPage',
                component: ContentPage,
            },
            {
                path: '/admin/faculties',
                name: 'FacultiesPage',
                component: FacultiesPage,
            },
            {
                path: '/admin/departments',
                name: 'DepartmentPage',
                component: DepartmentPage,
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
        path: '/',
        component: () => import("@/layouts/ProfileLayout.vue"),
        children: [ // Define nested routes for settings pages
            {
                path: 'my-details',
                name: 'MyDetails',
                component: MyDetails,
            },
            {
                path: '/my-details/edit',
                name: 'EditMyDetails',
                component: EditMyDetails,
            },
            {
                path: 'profile',
                name: 'Profile',
                component: Profile,
            },
            {
                path: '/edit-profile',
                name: 'EditProfile',
                component: EditProfile,
            },
            {
                path: 'password',
                name: 'Password',
                component: Password,
            },
            {
                path: 'notifications',
                name: 'Notifications',
                component: Notifications,
            },
            {
                path: '',
                redirect: '/profile'
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

router.beforeEach((to, from, next) => {
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

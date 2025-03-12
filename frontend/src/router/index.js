
import { createRouter, createWebHistory } from 'vue-router';

import Home from '../pages/Home.vue';
import Courses from '../pages/Courses.vue';
import Resources from '../pages/Resources.vue';
import Messages from '../pages/Messages.vue';
import Settings from '../pages/Settings.vue';
import {useAuthStore} from "@/store/auth.js";


import MyDetails from '../pages/profile/MyDetails.vue';
import EditMyDetails from '../pages/profile/EditMyDetails.vue';
import Profile from '../pages/profile/Profile.vue';
import EditProfile from '../pages/profile/EditProfile.vue';
import Password from '../pages/profile/Password.vue';
import Notifications from '../pages/profile/Notifications.vue';

import authRoutes from '@/modules/auth/router/index.js';

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
        path: '/',
        component: () => import("@/layouts/ProfileLayout.vue"),
        children: [ // Define nested routes for settings pages
            {
                path: 'my-details',
                name: 'MyDetails',
                component: MyDetails,
                meta: { requiresAuth: true }
            },
            {
                path: '/my-details/edit',
                name: 'EditMyDetails',
                component: EditMyDetails,
                meta: { requiresAuth: true }
            },
            {
                path: 'profile',
                name: 'Profile',
                component: Profile,
                meta: { requiresAuth: true }
            },
            {
                path: '/edit-profile',
                name: 'EditProfile',
                component: EditProfile,
                meta: { requiresAuth: true }
            },
            {
                path: 'password',
                name: 'Password',
                component: Password,
                meta: { requiresAuth: true }
            },
            {
                path: 'notifications',
                name: 'Notifications',
                component: Notifications,
                meta: { requiresAuth: true }
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

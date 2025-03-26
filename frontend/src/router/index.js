
import { createRouter, createWebHistory } from 'vue-router';

import {useAuthStore} from "@/store/auth.js";
import studentRouters from '@/modules/student/router/index.js'
import authRoutes from '@/modules/auth/router/index.js';
import profileRoutes from '@/modules/profile/router/index.js';
import adminRouters from '@/modules/admin/router/index.js'
import moderatorRouters from '@/modules/moderator/router/index.js'


const routes = [
    ...authRoutes,
    ...profileRoutes,
    studentRouters,
    adminRouters,
    moderatorRouters,
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

    if (to.meta.requiresAuth && !authStore.accessToken) {
        next("/login");
    } else if (to.meta.roles && !to.meta.roles.includes(user?.role)) {
        next("/");
    } else {
        next();
    }
});

export default router;

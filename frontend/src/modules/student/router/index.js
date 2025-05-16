
import Home from '@/modules/student/pages/Home.vue';
import Courses from '@/modules/student/pages/Courses.vue';
import Offers from '@/modules/student/pages/Offers.vue';
import Messages from '@/modules/student/pages/Messages.vue';
import Settings from '@/modules/student/pages/Settings.vue';

export default {
    path: '/',
    component: () => import("@/layouts/DefaultLayout.vue"),
    meta: { layout: "default" },
    children: [
        {
            path: 'home',
            name: 'Home',
            component: Home,
        },
        {
            path: 'courses',
            name: 'Courses',
            component: Courses,
            meta: { requiresAuth: true },
        },
        {
            path: 'offers',
            name: 'Offers',
            component: Offers,
            meta: { requiresAuth: true },
        },
        {
            path: 'messages',
            name: 'Messages',
            component: Messages,
            meta: { requiresAuth: true },
        },
        {
            path: 'settings',
            name: 'Settings',
            component: Settings,
            meta: { requiresAuth: true },
        },
        {
            path: '',
            redirect: '/home',
        },
    ]
};
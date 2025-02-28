import { createRouter, createWebHistory } from 'vue-router';
import Login from '../pages/Login.vue';
import Register from '../pages/Register.vue';
import DefaultLayout from '../layouts/DefaultLayout.vue';
import Dashboard from '../pages/Dashboard.vue';
import Courses from '../pages/Courses.vue';
import Schedule from '../pages/Schedule.vue';
import Resources from '../pages/Resources.vue';
import Messages from '../pages/Messages.vue';
import Settings from '../pages/Settings.vue';

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: { layout: 'minimal' } // Используем MinimalLayout
    },
    {
        path: '/register',
        name: 'Register',
        component: Register,
        meta: { layout: 'minimal' } // Используем MinimalLayout
    },
    {
        path: '/', //  Главная страница
        component: DefaultLayout,  //  Используем DefaultLayout
        children: [
            {
                path: 'dashboard', // /dashboard
                name: 'Dashboard',
                component: Dashboard
            },
            {
                path: 'courses', // /courses
                name: 'Courses',
                component: Courses
            },
            {
                path: 'schedule', // /schedule
                name: 'Schedule',
                component: Schedule
            },
            {
                path: 'resources', // /resources
                name: 'Resources',
                component: Resources
            },
            {
                path: 'messages', // /messages
                name: 'Messages',
                component: Messages
            },
            {
                path: 'settings', // /settings
                name: 'Settings',
                component: Settings
            },
            {
                path: '', //  Перенаправляем / на /dashboard.  Важно, чтобы это было последним дочерним маршрутом!
                redirect: '/dashboard'
            }
        ]
    },

];

const router = createRouter({
    history: createWebHistory(),
    routes
});

router.beforeEach((to, from, next) => {
    const layout = to.meta.layout || 'default';

    // Remove existing layout classes
    document.body.classList.remove('default-layout', 'minimal-layout');

    // Add new layout class
    document.body.classList.add(`${layout}-layout`);

    next();
});

export default router;
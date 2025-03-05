import { createRouter, createWebHistory } from 'vue-router';

import Login from '../modules/auth/pages/Login.vue';
import Register from '../modules/auth/pages/Register.vue';
import DefaultLayout from '../layouts/DefaultLayout.vue';
import Home from '../pages/Home.vue';
import Courses from '../pages/Courses.vue';
import Resources from '../pages/Resources.vue';
import Messages from '../pages/Messages.vue';
import Settings from '../pages/Settings.vue';
import AddFaculty from '../pages/AddFaculty.vue';

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
                path: 'home', // /home
                name: 'Home',
                component: Home
            },
            {
                path: 'courses', // /courses
                name: 'Courses',
                component: Courses
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
                path: 'AddFaculty', //
                name: 'AddFaculty',
                component: AddFaculty
            },
            {
                path: '', //  Перенаправляем / на /home.  Важно, чтобы это было последним дочерним маршрутом!
                redirect: '/home'
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
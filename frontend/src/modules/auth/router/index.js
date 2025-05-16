import Login from "@/modules/auth/pages/Login.vue";
import Register from "@/modules/auth/pages/Register.vue";

export default [
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
];
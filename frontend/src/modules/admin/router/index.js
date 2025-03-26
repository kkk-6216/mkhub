
import MonitoringPage from '@/modules/admin/pages/MonitoringPage.vue';
import ApiGatewayPage from '@/modules/admin/pages/ApiGatewayPage.vue';
import UsersAdminPage from '@/modules/admin/pages/UsersAdminPage.vue';

export default {
    path: '/admin',
    component: () => import("@/layouts/AdminLayout.vue"),
    meta: { requiresAuth: true, roles: ['ROLE_ADMIN'], layout: "admin" },
    children: [
        {
            path: 'monitoring',
            name: 'MonitoringPage',
            component: MonitoringPage,
        },
        {
            path: 'users',
            name: 'UsersAdminPage',
            component: UsersAdminPage,
        },
        {
            path: 'api-gateway',
            name: 'ApiGatewayPage',
            component: ApiGatewayPage,
        },
        {
            path: '',
            redirect: '/admin/monitoring'
        }
    ]
};
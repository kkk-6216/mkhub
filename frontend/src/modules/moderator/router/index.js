import DashboardPage from '@/modules/moderator/pages/DashboardPage.vue';
import UsersPage from '@/modules/moderator/pages/UsersPage.vue';
import DepartmentPage from '@/modules/moderator/pages/DepartmentPage.vue';
import FacultiesPage from '@/modules/moderator/pages/FacultiesPage.vue';
import ModerationPage from '@/modules/moderator/pages/ModerationPage.vue';
import RequestsPage from '@/modules/moderator/pages/RequestsPage.vue';
import CoursesPage from "@/modules/moderator/pages/CoursesPage.vue";
import LessonsPage from "@/modules/moderator/pages/LessonsPage.vue";

export default {
    path: '/moderator',
    component: () => import("@/layouts/ModeratorLayout.vue"),
    meta: { requiresAuth: true, roles: ['ROLE_MODERATOR'], layout: "moderator" },
    children: [
        {
            path: 'dashboard',
            name: 'DashboardPage',
            component: DashboardPage,
        },
        {
            path: 'users',
            name: 'UsersPage',
            component: UsersPage,
        },
        {
            path: 'faculties',
            name: 'FacultiesPage',
            component: FacultiesPage,
        },
        {
            path: 'departments',
            name: 'DepartmentPage',
            component: DepartmentPage,
        },
        {
            path: 'courses',
            name: 'CoursesPage',
            component: CoursesPage,
        },
        {
            path: 'lessons',
            name: 'LessonsPage',
            component: LessonsPage,
        },
        {
            path: 'moderation',
            name: 'ModerationPage',
            component: ModerationPage,
        },
        {
            path: 'requests',
            name: 'RequestsPage',
            component: RequestsPage,
        },
        {
            path: '',
            redirect: '/moderator/dashboard',
        },
    ]
};

import MyDetails from "@/modules/profile/pages/MyDetails.vue";
import EditMyDetails from "@/modules/profile/pages/EditMyDetails.vue";
import Profile from "@/modules/profile/pages/Profile.vue";
import EditProfile from "@/modules/profile/pages/EditProfile.vue";
import Password from "@/modules/profile/pages/Password.vue";
import Notifications from "@/modules/profile/pages/Notifications.vue";

export default [
    {
        path: '/profile',
        component: () => import("@/layouts/ProfileLayout.vue"),
        children: [
            {
                path: 'my-details',
                name: 'MyDetails',
                component: MyDetails,
                meta: { requiresAuth: true }
            },
            {
                path: 'my-details/edit',
                name: 'EditMyDetails',
                component: EditMyDetails,
                meta: { requiresAuth: true }
            },
            {
                path: '',
                name: 'Profile',
                component: Profile,
                meta: { requiresAuth: true }
            },
            {
                path: 'edit-profile',
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
                redirect: ''
            },
        ]
    }
];
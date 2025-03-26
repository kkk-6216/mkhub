<template>
  <div>
    <AlertComponent
        v-if="alert.visible"
        :type="alert.type"
        :message="alert.message"
    />

    <component :is="layout">
      <router-view />
    </component>
  </div>
</template>

<script>
import DefaultLayout from './layouts/DefaultLayout.vue';
import AdminLayout from './layouts/AdminLayout.vue';
import ModeratorLayout from './layouts/ModeratorLayout.vue';
import MinimalLayout from './layouts/MinimalLayout.vue';
import ProfileLayout from './layouts/ProfileLayout.vue';
import AlertComponent from "@/components/alerts/AlertComponent.vue";

export default {
  components: {AlertComponent},
  computed: {
    layout() {
      const layouts = {
        default: DefaultLayout,
        moderator: ModeratorLayout,
        admin: AdminLayout,
        minimal: MinimalLayout,
        profile: ProfileLayout,
      };
      return layouts[this.$route.meta.layout] || DefaultLayout;
    }
  },
  data() {
    return {
      alert: {
        visible: false,
        type: 'info',
        message: '',
      },
    };
  },
  provide() {
    return {
      showAlert: this.showAlert,
    };
  },
  methods: {
    showAlert(type, message) {
      this.alert.type = type;
      this.alert.message = message;
      this.alert.visible = true;

      setTimeout(() => {
        this.alert.visible = false;
      }, 4000);
    },
  },
};
</script>

<template>
  <div
      v-if="visible"
      :class="[
      'fixed z-100 top-4 left-1/2 transform -translate-x-1/2 flex items-center justify-center p-4 mb-4 rounded-lg',
      alertColors.bg,
      alertColors.text,
      alertColors.border,
    ]"
      role="alert"
  >
    <svg
        class="shrink-0 w-4 h-4"
        fill="currentColor"
        viewBox="0 0 20 20"
        xmlns="http://www.w3.org/2000/svg"
    >
      <path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM9.5 4a1.5 1.5 0 1 1 0 3 1.5 1.5 0 0 1 0-3ZM12 15H8a1 1 0 0 1 0-2h1v-3H8a1 1 0 0 1 0-2h2a1 1 0 0 1 1 1v4h1a1 1 0 0 1 0 2Z" />
    </svg>

    <div class="ms-3 text-sm font-medium">
      <slot>
        {{ message }}
      </slot>
    </div>

    <button
        type="button"
        class="ms-auto -mx-1.5 -my-1.5 rounded-lg focus:ring-2 p-1.5 hover:bg-opacity-75 inline-flex items-center justify-center h-8 w-8"
        :class="[alertColors.closeBg, alertColors.closeText, alertColors.closeRing]"
        @click="close"
    >
      <span class="sr-only">Закрыть</span>
      <svg
          class="w-3 h-3"
          fill="none"
          viewBox="0 0 14 14"
          xmlns="http://www.w3.org/2000/svg"
      >
        <path
            stroke="currentColor"
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"
        />
      </svg>
    </button>
  </div>
</template>

<script>
export default {
  name: "AlertComponent",
  props: {
    type: {
      type: String,
      default: "info", // info, success, error, warning, gray
      validator: function (value) {
        return ['info', 'error', 'success', 'warning', 'gray'].includes(value);
      }
    },
    message: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      visible: true,
    };
  },
  computed: {
    alertColors() {
      const types = {
        info: {
          bg: "bg-blue-50 light:bg-gray-800",
          text: "text-blue-800 light:text-blue-400",
          closeBg: "bg-blue-50 light:bg-gray-800",
          closeText: "text-blue-500 light:text-blue-400",
          closeRing: "focus:ring-blue-400",
          border: 'border border-blue-300',
        },
        success: {
          bg: "bg-green-50 light:bg-gray-800",
          text: "text-green-800 light:text-green-400",
          closeBg: "bg-green-50 light:bg-gray-800",
          closeText: "text-green-500 light:text-green-400",
          closeRing: "focus:ring-green-400",
          border: 'border border-green-300',
        },
        error: {
          bg: "bg-red-50 light:bg-gray-800",
          text: "text-red-800 light:text-red-400",
          closeBg: "bg-red-50 light:bg-gray-800",
          closeText: "text-red-500 light:text-red-400",
          closeRing: "focus:ring-red-400",
          border: 'border border-red-300',
        },
        warning: {
          bg: "bg-yellow-50 light:bg-gray-800",
          text: "text-yellow-800 light:text-yellow-300",
          closeBg: "bg-yellow-50 light:bg-gray-800",
          closeText: "text-yellow-500 light:text-yellow-300",
          closeRing: "focus:ring-yellow-400",
          border: 'border border-yellow-300',
        },
        gray: {
          bg: "bg-gray-50 light:bg-gray-800",
          text: "text-gray-800 light:text-gray-300",
          closeBg: "bg-gray-50 light:bg-gray-800",
          closeText: "text-gray-500 light:text-gray-300",
          closeRing: "focus:ring-gray-400",
          border: 'border border-gray-300',
        },
      };
      return types[this.type] || types.info;
    },
  },
  methods: {
    close() {
      this.visible = false;
    },
  },
};
</script>

<template>
  <button
      :type="type"
      :class="buttonClasses"
      :disabled="disabled"
  >
    {{ label }}
  </button>
</template>

<script>
export default {
  name: "DefaultButton",
  props: {
    label: {
      type: String,
      default: "Кнопка",
    },
    type: {
      type: String,
      default: "button",
    },
    size: {
      type: String,
      default: "md",
      validator: (val) => ["sm", "md", "lg"].includes(val),
    },
    variant: {
      type: String,
      default: "primary",
      validator: (val) => ["primary", "secondary", "danger"].includes(val),
    },
    fullWidth: {
      type: Boolean,
      default: false,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
  computed: {
    buttonClasses() {
      return [
        "rounded-[10px] focus:outline-none focus:ring-2 focus:ring-gray-300 transition duration-300 ease-in-out hover:scale-101",
        { "w-full": this.fullWidth },
        { "px-4 py-1 text-sm": this.size === "sm" },
        { "px-6 py-2 text-base": this.size === "md" },
        { "px-8 py-4 text-lg": this.size === "lg" },
        { "bg-main text-white hover:bg-main-hover": this.variant === "primary" },
        { "bg-gray-500 text-white hover:bg-gray-600": this.variant === "secondary" },
        { "bg-red-500 text-white hover:bg-red-600": this.variant === "danger" },
        { "opacity-50 cursor-not-allowed": this.disabled },
      ];
    },
  },
};
</script>

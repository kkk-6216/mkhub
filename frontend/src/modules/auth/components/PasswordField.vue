<template>
  <div class="relative mb-[20px]">
    <input
        :type="showPassword ? 'text' : 'password'"
        :placeholder="placeholder"
        :value="modelValue"
        @input="$emit('update:modelValue', $event.target.value)"
        :class="{
        'border-red-400 focus:ring-red-300': (!modelValue || !isValid) && isSubmitted,
        'border-gray-400 focus:ring-gray-300': (modelValue && isValid) || !isSubmitted,
      }"
        class="w-full px-4 py-2 pl-10 border rounded-[10px] focus:outline-none focus:ring-2 focus:ring-gray-300 text-gray-700"
    />
    <span class="absolute left-3 top-2 text-gray-500">
      <slot name="icon"></slot>
    </span>
    <span
        class="absolute right-3 top-2 text-gray-500 cursor-pointer"
        @click="togglePassword"
    >
      <slot name="toggleIcon"></slot>
    </span>
  </div>
</template>

<script>
export default {
  name: 'PasswordField',
  props: {
    placeholder: {
      type: String,
      default: '',
    },
    modelValue: {
      type: String,
      default: '',
    },
    isSubmitted: {
      type: Boolean,
      default: false,
    },
    isValid: {
      type: Boolean,
      default: true,
    },
  },
  emits: ['update:modelValue', 'togglePassword'],
  data() {
    return {
      showPassword: false,
    };
  },
  methods: {
    togglePassword() {
      this.showPassword = !this.showPassword;
      this.$emit('togglePassword');
    },
  },
};
</script>
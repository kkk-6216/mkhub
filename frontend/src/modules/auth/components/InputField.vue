<template>
  <div class="relative mb-4">
    <input
        :type="type"
        :placeholder="placeholder"
        :value="modelValue"
        @input="$emit('update:modelValue', $event.target.value)"
        :class="{
        'border-red-500': (!modelValue || !isValid) && isSubmitted,
        'border-gray-300': (modelValue && isValid) || !isSubmitted,
      }"
        class="w-full px-4 py-2 pl-10 border rounded-md focus:outline-none focus:ring-2 focus:ring-gray-300 text-gray-700"
    />
    <span class="absolute left-3 top-2 text-gray-500">
      <slot name="icon"></slot>
    </span>
  </div>
</template>

<script>
export default {
  name: 'InputField',
  props: {
    type: {
      type: String,
      default: 'text',
    },
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
  emits: ['update:modelValue'],
};
</script>
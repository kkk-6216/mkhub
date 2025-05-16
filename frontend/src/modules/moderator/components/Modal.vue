<template>
  <div
      class="fixed inset-0 flex backdrop-blur-sm"
      :class="position"
      @mousedown.self="$emit('close')"
  >
    <div class="bg-white bg-opacity-90 p-6 rounded-lg shadow-lg border border-gray-200" :class="width">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "Modal",
  props: {
    width: {
      type: String,
      default: "w-2/5",
      required: false,
    },
    position: {
      type: String,
      default: "items-center justify-center"
    }
  }
};
</script>

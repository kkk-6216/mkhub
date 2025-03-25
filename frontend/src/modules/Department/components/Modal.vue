<template>
  <div
      class="fixed inset-0 flex items-center justify-center backdrop-blur-sm"
      @mousedown.self="$emit('close')"
  >
    <div class="bg-white bg-opacity-90 p-6 rounded-lg shadow-lg border border-gray-200" :class="width">
      <slot></slot> <!-- Bu yerda modal oynaning kontenti joylashadi -->
    </div>
  </div>
</template>

<script>
export default {
  name: "Modal",
  props: {
    width: {
      type: String,
      default: "w-[500px]",
    },
  },
};
</script>


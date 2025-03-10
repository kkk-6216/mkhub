<template>
  <div class="fixed inset-0 flex items-center justify-center backdrop-blur-sm">
    <div class="bg-white bg-opacity-90 p-6 rounded-lg shadow-lg w-2/5 border border-gray-200">
      <div class="fixed inset-0 flex items-center justify-center backdrop-blur-sm">
        <div class="bg-white bg-opacity-90 p-6 rounded-lg shadow-lg w-2/5 border border-gray-200">
          <slot></slot> <!-- Bu yerda modal oynaning kontenti joylashadi -->
        </div>
      </div>
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "Modal",
};
</script>
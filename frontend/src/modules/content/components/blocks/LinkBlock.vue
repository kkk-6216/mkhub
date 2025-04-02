<template>
  <div class="border border-gray-300 rounded p-4 mb-2">
    <a :href="data.url" class="text-blue-500 hover:underline" target="_blank">
      <img v-if="data.image" :src="data.image" alt="Link Preview" class="w-full h-auto mb-2">
      <h3 class="font-bold text-lg">{{ data.title }}</h3>
      <p class="text-gray-700">{{ data.description }}</p>
    </a>
  </div>
</template>

<script>
export default {
  name: "LinkBlock",
  props: {
    data: {
      type: Object,
      required: true
    }
  }
}
</script>

<style scoped>

</style>
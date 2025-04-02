<template>
  <div class="mb-4 p-4 bg-gray-800 text-white rounded-md">
    <pre><code class="language-javascript">{{ data.code }}</code></pre>
    <p v-if="data.caption" class="text-sm text-gray-400">{{ data.caption }}</p>
  </div>
</template>

<script>
export default {
  name: "CodeBlock",
  props: {
    data: {
      type: Object,
      required: true,
    },
  },
};
</script>

<style scoped>

</style>
<template>
  <div class="flex justify-center items-center">
    <div class="min-h-screen w-full max-w-5xl bg-white">
      <ContentEditor :initial-blocks="pageContent" @update-blocks="updateContent" />
    </div>
  </div>
</template>

<script>
import ContentEditor from '@/modules/content/ContentEditor/ContentEditor.vue';

export default {
  components: {
    ContentEditor
  }

 };
</script>
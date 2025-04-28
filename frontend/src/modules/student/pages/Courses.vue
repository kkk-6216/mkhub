<template>
  <div class="flex justify-center items-center">
    
      <ContentEditor :initial-blocks="pageContent" @update-blocks="updateContent" />
    
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
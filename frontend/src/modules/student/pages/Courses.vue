<template>
  <div class="h-screen flex justify-center items-center">
    <div class="min-h-screen w-full max-w-3xl bg-white">
      <ContentEditor :initial-blocks="pageContent" @update-blocks="updateContent" />
    </div>
  </div>
</template>

<script>
import ContentEditor from '@/modules/content/ContentEditor.vue';

export default {
  components: {
    ContentEditor
  }
//   data() {
//     return {
//       pageContent: [
//         { id: 1, type: 'text', content: 'Начальный контент страницы' }
//       ]
//     };
//   },
//   methods: {
//     updateContent(newContent) {
//       this.pageContent = newContent;
//       // Здесь можно сохранить newContent в базе данных или другом месте
//     }
//   }
 };
</script>
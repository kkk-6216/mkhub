<template>
  <div class="max-w-7xl mx-auto p-6 bg-white rounded-lg">
    <div class="flex justify-between items-center mb-6">
      <div class="flex items-center justify-center">
        <h2 class="text-2xl font-medium text-dark ml-10">{{ lessonName }}</h2>
      </div>
      <button class="text-white px-3 py-1 rounded-md hover:opacity-90 flex items-center bg-main hover:bg-main-hover">
        Сохранить
      </button>
    </div>
    <div class="flex justify-center items-center">
      <div class="min-h-screen w-full bg-white">
        <ContentEditor :initial-blocks="pageContent" @update-blocks="updateContent" />
      </div>
    </div>
  </div>
</template>

<script>
import ContentEditor from "@/modules/content/ContentEditor/ContentEditor.vue";

export default {
  name: "LessonContentEdit",
  components: {ContentEditor},
  props: {
    lessonId: {
      type: Number,
      required: true
    },
    lessonName: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      lesson: {}
    };
  },
}
</script>

<style scoped>

</style>
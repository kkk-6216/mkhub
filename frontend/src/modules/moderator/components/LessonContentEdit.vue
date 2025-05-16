<template>
  <div class="max-w-7xl mx-auto p-6 bg-white rounded-lg">
    <div class="flex justify-center items-center">
      <div class="min-h-screen w-full bg-white">
        <ContentEditor
            :initial-blocks="pageContent"
            @update-blocks="updateContent"
            :lesson-name="lessonName"
            :lesson-id="lessonId"
        />
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
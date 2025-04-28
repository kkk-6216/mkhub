<template>
  <div class="max-w-7xl mx-auto p-6 bg-white rounded-lg">
    <div class="flex justify-between items-center mb-6">
      <div class="flex items-center justify-center">
        <h2 class="text-2xl font-medium text-dark">{{ lessonName }}</h2>
      </div>
      <button @click="$emit('openLessonContentEdit', {id: lesson, name: lessonName})" class="text-white px-3 py-1 rounded-md hover:opacity-90 flex items-center bg-main hover:bg-main-hover">
        Редактировать
      </button>
    </div>
    <div class="flex justify-center items-center mt-[40px]" v-if="false">

    </div>
    <div v-else>
      <p class="text-gray-500">Пусто...</p>
    </div>
  </div>
</template>

<script>

export default {
  name: "LessonContent",
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
<template>
  <div class="flex h-screen bg-blue-50 transition-all duration-300" :class="{ 'pr-[400px]': selectedCourse }">
    <!-- Asosiy Kontent -->
    <div class="w-full p-6 space-y-6">
      <div>
        <h1 class="text-2xl font-bold">My courses</h1>
        <div class="flex space-x-4 mt-2">
          <button class="font-semibold border-b-2 border-black">Active</button>
          <button class="text-gray-400">Completed</button>
        </div>
      </div>

      <!-- Kurs Kartalari -->
      <div class="flex space-x-4 overflow-x-auto">
        <div
            v-for="(course, index) in courses"
            :key="index"
            @click="openPanel(course)"
            class="min-w-[200px] cursor-pointer p-4 rounded-xl text-white"
            :class="course.bg"
        >
          <h3 class="text-sm opacity-80">{{ course.category }}</h3>
          <h2 class="text-lg font-semibold">{{ course.title }}</h2>
          <p class="text-sm mt-2 opacity-90">{{ course.progress }}</p>
        </div>
      </div>

      <!-- Trending -->
      <div>
        <h2 class="text-xl font-semibold">Trending</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <div
              v-for="(trend, index) in trending"
              :key="index"
              @click="openPanel(trend)"
              class="bg-white p-4 rounded-xl shadow cursor-pointer hover:bg-gray-50"
          >
            <div class="text-sm text-gray-500">{{ trend.category }}</div>
            <div class="font-semibold">{{ trend.title }}</div>
            <div class="text-xs text-gray-400 mt-2">
              Start {{ trend.start }} | Rate {{ trend.rate }} | Level: {{ trend.level }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- O‘ng Panel (faqat kurs tanlansa chiqadi) -->
    <div
        v-if="selectedCourse"
        class="fixed top-0 right-0 h-full w-[400px] bg-white shadow-xl p-6 transition-transform duration-300 z-50"
    >
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-xl font-semibold">{{ selectedCourse.title }}</h2>
        <button @click="selectedCourse = null" class="text-gray-400 hover:text-black text-xl">&times;</button>
      </div>
      <p class="text-sm text-gray-600 mb-4">{{ selectedCourse.description || 'Detailed course info...' }}</p>
      <div class="flex justify-between text-sm mt-4">
        <div>
          <p class="font-semibold">32h</p>
          <p class="text-gray-500">Theory</p>
        </div>
        <div>
          <p class="font-semibold">58h</p>
          <p class="text-gray-500">Practice</p>
        </div>
      </div>
      <button class="mt-6 bg-blue-600 text-white px-4 py-2 rounded-xl hover:bg-blue-700">View schedule</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// Kurslar
const courses = ref([
  { title: 'User flow', category: 'UX design', progress: '4/8', bg: 'bg-blue-900' },
  { title: 'Strategy', category: 'Marketing', progress: '9/10', bg: 'bg-indigo-700' },
  { title: 'Potion making', category: 'Magic', progress: '5/7', bg: 'bg-blue-800' },
])

// Trending
const trending = ref([
  {
    title: 'How to create patterns',
    category: 'UI design',
    start: '18.02',
    rate: '4.8',
    level: 'hard',
  },
  {
    title: 'UX Research',
    category: 'UX design',
    start: '25.02',
    rate: '4.5',
    level: 'medium',
  },
])

const selectedCourse = ref(null)

function openPanel(course) {
  selectedCourse.value = course
}
</script>

<style scoped>
/* Panel chiqganda background shrink effekt */
</style>

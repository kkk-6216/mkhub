<template>
  <div class="relative h-screen bg-white overflow-y-auto">
    <!-- Основное содержимое -->
    <div class="w-full p-6 space-y-6 max-w-5xl mx-auto">
      <div>
        <h1 class="text-2xl font-bold">Курсы</h1>
      </div>

      <!-- Карточки курсов -->
      <div class="flex space-x-4">
        <div
            v-for="(course, index) in courses"
            :key="index"
            @click="togglePanel(course)"
            class="min-w-[300px] cursor-pointer p-8 rounded-xl text-white"
            :class="course.bg"
        >
          <h3 class="text-sm opacity-80">{{ course.category }}</h3>
          <h2 class="text-lg font-semibold">{{ course.title }}</h2>
          <p class="text-sm mt-2 opacity-90">{{ course.progress }}</p>
        </div>
      </div>

      <!-- Популярное (Trending) -->
      <div>
        <h2 class="text-xl font-semibold mb-2">Популярное</h2>
        <div class="grid gap-4">
          <div
              v-for="(trend, index) in trending"
              :key="index"
              @click="togglePanel(trend)"
              class="w-full bg-white rounded-xl shadow p-4 flex gap-4 items-start cursor-pointer hover:bg-gray-50"
          >
            <!-- Цветной блок -->
            <div class="w-30 h-30 rounded-lg flex-shrink-0" :class="trend.bg"></div>

            <!-- Контент -->
            <div class="flex flex-col flex-grow">
              <div class="text-xs text-gray-500">{{ trend.category }}</div>
              <div class="text-base font-semibold">{{ trend.title }}</div>
              <div class="flex flex-wrap gap-2 mt-2 text-xs text-gray-500">
                <span class="bg-gray-100 px-2 py-1 rounded">Старт {{ trend.start }}</span>
                <span class="bg-gray-100 px-2 py-1 rounded">Оценка {{ trend.rate }}</span>
                <span class="bg-gray-100 px-2 py-1 rounded">Уровень: {{ trend.level }}</span>
              </div>
            </div>

            <!-- Иконка -->
            <div class="text-gray-400 hover:text-black text-lg">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none"
                   viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M5 12h.01M12 12h.01M19 12h.01"/>
              </svg>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Боковая панель -->
    <transition name="slide">
      <div
          v-if="selectedCourse"
          class="fixed top-0 right-0 h-full w-[400px] bg-white shadow-xl p-6 z-[999] transform transition-transform duration-300 flex flex-col justify-between"
      >
        <div>
          <!-- Rasm va narx belgisi -->
          <div class="relative w-full h-[180px] bg-blue-100 rounded-xl mb-6 overflow-hidden">
            <!-- O'zgartirilgan rasm -->
            <img src="https://image.shutterstock.com/image-photo/training-courses-business-concept-stack-260nw-549736798.jpg" alt="Course Image" class="object-cover w-full h-full" />

          </div>

          <!-- Nomi va matn -->
          <h2 class="text-xl font-semibold mb-2">{{ selectedCourse.title }}</h2>
          <p class="text-sm text-gray-600 mb-4">
            {{ selectedCourse.description || '' }}
          </p>

        </div>

        <!-- Pastki qism: Instructors va info -->
        <div>
          <!-- Instructors -->
          <div class="mb-4">
            <p class="text-sm font-semibold text-gray-700 mb-2">Instructors</p>
            <div class="flex -space-x-2">
              <img class="w-8 h-8 rounded-full border-2 border-white" src="https://i.pravatar.cc/32?img=1" />
              <img class="w-8 h-8 rounded-full border-2 border-white" src="https://i.pravatar.cc/32?img=2" />
              <img class="w-8 h-8 rounded-full border-2 border-white" src="https://i.pravatar.cc/32?img=3" />
            </div>
          </div>

          <!-- Info: Теория и Практика -->
          <div class="flex justify-between text-sm text-gray-800 mb-6">
            <div class="text-center">
              <p class="text-lg font-semibold">32h</p>
              <p class="text-gray-500">Theory</p>
            </div>
            <div class="border-l border-gray-300 h-8 mx-4"></div>
            <div class="text-center">
              <p class="text-lg font-semibold">58h</p>
              <p class="text-gray-500">Practice</p>
            </div>
          </div>

          <!-- Tugma -->
          <button class="w-full bg-blue-900 text-white py-2 rounded-xl hover:bg-blue-800 transition">
            Подробнее
          </button>
        </div>
      </div>
    </transition>

  </div>
</template>

<script setup>
import { ref } from 'vue'

const courses = ref([
  { title: 'Пользовательский поток', category: 'UX-дизайн', progress: '7 из 8', bg: 'bg-blue-900' },
  { title: 'Стратегия', category: 'Маркетинг', progress: '9 из 10', bg: 'bg-indigo-700' },
  { title: 'Зельеварение', category: 'Магия', progress: '5 из 7', bg: 'bg-blue-800' },
])

const trending = ref([
  {
    title: 'Как создавать паттерны',
    category: 'UI-дизайн',
    start: '18.02',
    rate: '4.8',
    level: 'Сложный',
    bg: 'bg-green-100'
  },
  {
    title: 'UX Исследование',
    category: 'UX-дизайн',
    start: '25.02',
    rate: '4.5',
    level: 'Средний',
    bg: 'bg-blue-100'
  },
  {
    title: 'Финансовая грамотность',
    category: 'Экономика',
    start: '01.03',
    rate: '4.7',
    level: 'Легкий',
    bg: 'bg-yellow-100'
  },
  {
    title: 'Основы программирования',
    category: 'Информатика',
    start: '10.03',
    rate: '4.9',
    level: 'Средний',
    bg: 'bg-purple-100'
  },
  {
    title: 'Анализ данных',
    category: 'Data Science',
    start: '15.03',
    rate: '4.6',
    level: 'Сложный',
    bg: 'bg-red-100'
  },
  {
    title: 'Эффективная коммуникация',
    category: 'Софт скиллы',
    start: '20.03',
    rate: '4.4',
    level: 'Легкий',
    bg: 'bg-pink-100'
  }
])

const selectedCourse = ref(null)

function togglePanel(item) {
  if (selectedCourse.value === item) {
    selectedCourse.value = null
  } else {
    selectedCourse.value = item
  }
}
</script>

<style scoped>
/* Анимация боковой панели */
.slide-enter-from {
  transform: translateX(100%);
}
.slide-enter-to {
  transform: translateX(0%);
}
.slide-leave-from {
  transform: translateX(0%);
}
.slide-leave-to {
  transform: translateX(100%);
}
.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s ease;
}

/* Скролл скрыт */
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
.no-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
</style>

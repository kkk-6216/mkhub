<template>
  <div class="relative flex min-h-screen">
    <!-- Main Content -->
    <div class="flex-1 p-6 transition-all duration-300" :class="{ 'mr-[400px]': isPanelOpen }">
      <div class="max-w-6xl mx-auto">
        <div class="mb-8">
          <h1 class="text-2xl font-bold text-gray-800 mb-6">My courses</h1>

          <div class="flex border-b border-gray-200 mb-6">
            <button
                class="pb-2 px-4 font-medium text-gray-600 hover:text-indigo-600 focus:outline-none"
                :class="{ 'text-indigo-600 border-b-2 border-indigo-600': activeTab === 'active' }"
                @click="activeTab = 'active'"
            >
              Active
            </button>
            <button
                class="pb-2 px-4 font-medium text-gray-600 hover:text-indigo-600 focus:outline-none"
                :class="{ 'text-indigo-600 border-b-2 border-indigo-600': activeTab === 'completed' }"
                @click="activeTab = 'completed'"
            >
              Completed
            </button>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <div
                v-for="course in filteredCourses"
                :key="course.id"
                class="bg-white rounded-xl shadow-md overflow-hidden cursor-pointer hover:shadow-lg transition-shadow duration-300"
                @click="openRightPanel(course)"
            >
              <div class="p-5">
                <div class="flex justify-between items-start mb-3">
                  <h3 class="text-lg font-semibold text-gray-800">{{ course.title }}</h3>
                  <span class="text-xs bg-gray-100 text-gray-600 py-1 px-2 rounded-full">{{ course.category }}</span>
                </div>

                <p class="text-sm text-gray-600 mb-4">{{ course.descriptionShort }}</p>

                <div class="flex items-center justify-between text-sm text-gray-500 mb-2">
                  <span>{{ course.startDate }}</span>
                  <span class="font-semibold">{{ course.rating }}</span>
                </div>

                <div class="w-full bg-gray-200 rounded-full h-1.5 mb-2">
                  <div
                      class="bg-indigo-600 h-1.5 rounded-full"
                      :style="{ width: `${(course.completed / course.total) * 100}%` }"
                  ></div>
                </div>

                <div class="flex justify-between text-xs text-gray-500">
                  <span>Progress</span>
                  <span>{{ course.completed }}/{{ course.total }} lessons</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Right Panel -->
    <div
        v-if="isPanelOpen"
        class="fixed top-0 right-0 h-full w-[400px] bg-white shadow-xl z-50 border-l border-gray-200 overflow-y-auto"
        @click.self="closeRightPanel"
    >
      <div class="p-6 relative h-full">
        <button
            class="absolute top-4 right-4 text-gray-500 hover:text-gray-700 focus:outline-none"
            @click="closeRightPanel"
        >
          <svg class="h-6 w-6 fill-current" viewBox="0 0 24 24">
            <path
                fill-rule="evenodd"
                d="M15.78 14.36a1 1 0 01-1.42 1.42L12 13.41l-2.36 2.37a1 1 0 01-1.42-1.42L10.59 12l-2.37-2.36a1 1 0 111.42-1.42L12 10.59l2.36-2.37a1 1 0 111.42 1.42L13.41 12l2.37 2.36z"
                clip-rule="evenodd"
            />
          </svg>
        </button>

        <div class="mb-6">
          <h2 class="text-xl font-semibold mb-2">{{ selectedCourse.title }}</h2>
          <p class="text-gray-600">{{ selectedCourse.category }}</p>
        </div>

        <div class="flex items-center justify-between mb-6">
          <div class="flex items-center">
            <svg class="w-5 h-5 text-gray-500 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
            </svg>
            <span class="text-sm text-gray-600">{{ selectedCourse.startDate }}</span>
          </div>
          <div class="flex items-center">
            <svg class="w-5 h-5 text-yellow-500 mr-1" fill="currentColor" viewBox="0 0 20 20">
              <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"></path>
            </svg>
            <span class="text-sm font-medium">{{ selectedCourse.rating }}</span>
          </div>
          <span class="text-sm px-2 py-1 bg-gray-100 text-gray-600 rounded-full">{{ selectedCourse.level }}</span>
        </div>

        <div class="mb-6">
          <h3 class="font-semibold mb-2">Description</h3>
          <p class="text-gray-600 text-sm">{{ selectedCourse.descriptionLong }}</p>
        </div>

        <div class="mb-6">
          <div class="flex justify-between items-center mb-2">
            <h3 class="font-semibold">Progress</h3>
            <span class="text-sm text-gray-600">{{ selectedCourse.completed }}/{{ selectedCourse.total }} lessons</span>
          </div>
          <div class="w-full bg-gray-200 rounded-full h-2">
            <div
                class="bg-indigo-600 h-2 rounded-full"
                :style="{ width: `${(selectedCourse.completed / selectedCourse.total) * 100}%` }"
            ></div>
          </div>
        </div>

        <div class="mb-6" v-if="selectedCourse.details && selectedCourse.details.length > 0">
          <h3 class="font-semibold mb-2">What you'll learn</h3>
          <ul class="space-y-2">
            <li v-for="(detail, index) in selectedCourse.details" :key="index" class="flex items-start">
              <svg class="w-5 h-5 text-green-500 mr-2 mt-0.5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
              </svg>
              <span class="text-gray-600 text-sm">{{ detail }}</span>
            </li>
          </ul>
        </div>

        <div class="mb-6">
          <h3 class="font-semibold mb-3">Instructors</h3>
          <div class="flex space-x-3">
            <div v-for="(instructor, index) in selectedCourse.instructors" :key="index" class="flex items-center">
              <div class="w-10 h-10 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 font-medium mr-2">
                {{ instructor.initials || '?' }}
              </div>
              <div>
                <p class="text-sm font-medium">Instructor {{ index + 1 }}</p>
                <p class="text-xs text-gray-500">Specialist</p>
              </div>
            </div>
          </div>
        </div>

        <div class="flex justify-between mb-6">
          <div class="text-center">
            <p class="text-2xl font-bold text-indigo-600">{{ selectedCourse.theoryHours }}</p>
            <p class="text-sm text-gray-500">Theory</p>
          </div>
          <div class="text-center">
            <p class="text-2xl font-bold text-indigo-600">{{ selectedCourse.practiceHours }}</p>
            <p class="text-sm text-gray-500">Practice</p>
          </div>
        </div>

        <button class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-3 px-4 rounded-lg transition duration-200">
          View schedule
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const activeTab = ref('active');

const courses = ref([
  {
    id: 1,
    title: 'User flow',
    category: 'UX design',
    startDate: '18:02',
    rating: '4.8',
    level: 'Hard',
    completed: 4,
    total: 8,
    descriptionShort: 'Learn the basics of user flow design.',
    descriptionLong: 'This course will teach you how to create effective user flows for web and mobile applications. You will learn to map out user journeys, identify pain points, and optimize the user experience.',
    details: [
      'User flow principles and best practices',
      'Creating comprehensive flowcharts',
      'Testing and optimizing user flows',
      'Tools for user flow design'
    ],
    instructors: [{ initials: 'JD' }, { initials: 'MS' }],
    theoryHours: '32h',
    practiceHours: '58h',
    status: 'active'
  },
  {
    id: 2,
    title: 'Marketing Strategy',
    category: 'Marketing',
    startDate: '23:02',
    rating: '4.5',
    level: 'Medium',
    completed: 9,
    total: 10,
    descriptionShort: 'Develop strong marketing strategies.',
    descriptionLong: 'Master the art of creating effective marketing strategies that drive business growth. Learn to analyze markets, understand customer behavior, and create campaigns that convert.',
    details: [
      'Market analysis techniques',
      'Competitor research methods',
      'Strategic planning frameworks',
      'Campaign measurement'
    ],
    instructors: [{ initials: 'AR' }],
    theoryHours: '25h',
    practiceHours: '40h',
    status: 'active'
  },
  {
    id: 3,
    title: 'UX Research',
    category: 'UX design',
    startDate: '15:02',
    rating: '4.9',
    level: 'Easy',
    completed: 6,
    total: 7,
    descriptionShort: 'Master UX research techniques.',
    descriptionLong: 'Learn various UX research methods and how to apply them in real projects. From user interviews to usability testing, this course covers all essential research techniques.',
    details: [
      'Conducting effective user interviews',
      'Creating and analyzing surveys',
      'Usability testing methods',
      'Data analysis and reporting'
    ],
    instructors: [{ initials: 'TP' }, { initials: 'KL' }],
    theoryHours: '40h',
    practiceHours: '65h',
    status: 'completed'
  },
  {
    id: 4,
    title: 'Action making',
    category: 'Productivity',
    startDate: '10:02',
    rating: '4.7',
    level: 'Medium',
    completed: 5,
    total: 6,
    descriptionShort: 'Improve your decision making skills.',
    descriptionLong: 'This course teaches techniques to make better decisions faster in professional settings. Learn frameworks for quick analysis and risk assessment.',
    details: [
      'Decision making frameworks',
      'Risk assessment techniques',
      'Quick analysis methods',
      'Overcoming decision paralysis'
    ],
    instructors: [{ initials: 'DM' }],
    theoryHours: '20h',
    practiceHours: '30h',
    status: 'active'
  },
  {
    id: 5,
    title: 'Dashboard Design',
    category: 'UI design',
    startDate: '09:02',
    rating: '4.8',
    level: 'Hard',
    completed: 8,
    total: 8,
    descriptionShort: 'Create creative and interactive dashboards.',
    descriptionLong: 'Learn how to design effective dashboards that provide actionable insights. This course covers data visualization principles and interactive elements.',
    details: [
      'Data visualization principles',
      'Dashboard layout best practices',
      'Interactive elements design',
      'User testing for dashboards'
    ],
    instructors: [{ initials: 'SW' }, { initials: 'RJ' }],
    theoryHours: '35h',
    practiceHours: '50h',
    status: 'completed'
  }
]);

const filteredCourses = computed(() => {
  return courses.value.filter(course => course.status === activeTab.value);
});

const isPanelOpen = ref(false);
const selectedCourse = ref({});

const openRightPanel = (courseData) => {
  selectedCourse.value = courseData;
  isPanelOpen.value = true;
};

const closeRightPanel = () => {
  isPanelOpen.value = false;
  selectedCourse.value = {};
};
</script>

<style>
/* Smooth transitions for panel */
.fixed {
  transition: transform 0.3s ease;
}
</style>
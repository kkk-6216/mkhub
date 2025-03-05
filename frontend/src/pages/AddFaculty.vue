<script setup>
import { ref } from 'vue';

const facultyName = ref('');
const errorMessage = ref('');

const addFaculty = () => {
  if (!facultyName.value.trim()) {
    errorMessage.value = 'Введите название факультета!';
    return;
  }

  console.log('Новый факультет:', facultyName.value);
  errorMessage.value = '';
  facultyName.value = '';
};
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-r from-blue-500 to-indigo-600 p-6">
    <div class="bg-white shadow-2xl rounded-3xl p-8 w-full max-w-md">
      <h2 class="text-3xl font-extrabold text-gray-800 mb-6 text-center">Добавить факультет</h2>

      <div class="mb-4">
        <label class="block text-gray-700 font-medium mb-2">Название факультета</label>
        <input
            v-model="facultyName"
            type="text"
            placeholder="Введите название факультета"
            class="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none transition"
        />
        <p v-if="errorMessage" class="text-red-500 text-sm mt-2">{{ errorMessage }}</p>
      </div>

      <button
          @click="addFaculty"
          class="w-full bg-indigo-500 hover:bg-indigo-600 text-white font-semibold py-3 rounded-lg transition text-lg shadow-md"
      >
        Добавить
      </button>
    </div>
  </div>
</template>

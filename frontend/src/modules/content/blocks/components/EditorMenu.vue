<template>
    <div class="header flex justify-between items-center mb-6">
      
      <div class="flex gap-2">
        <button @click="$emit('clear')" class="px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded-lg text-sm flex items-center gap-1 transition-colors">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
          </svg>
          Очистить
        </button>
        <button @click="$emit('show-help')" class="px-4 py-2 bg-gray-500 hover:bg-gray-600 text-white rounded-lg flex items-center gap-1 transition-colors">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.228 9c.549-1.165 2.03-2 3.772-2 2.21 0 4 1.343 4 3 0 1.4-1.278 2.575-3.006 2.907-.542.104-.994.54-.994 1.093m0 3h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          Помощь
        </button>
        <button @click="$emit('finish')" class="px-4 py-2 bg-main hover:bg-main-hover text-white rounded-lg flex items-center gap-1 transition-colors">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
          </svg>
          Готово
        </button>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'EditorMenu',
    emits: ['clear', 'show-help', 'finish']
  }
  </script>
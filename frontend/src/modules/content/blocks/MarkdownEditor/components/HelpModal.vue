<template>
    <div v-if="show" class="fixed inset-0 backdrop-blur-sm bg-opacity-50 flex items-center justify-center z-50 p-4 rounded-lg">
      <div class="bg-white rounded-lg shadow-xl w-full max-w-3xl max-h-[75vh] overflow-auto">
        <div class="p-6">
          <div class="flex justify-between items-center mb-4 border-b pb-2">
            <h2 class="text-xl font-bold text-gray-800">Справочник по Markdown</h2>
            <button @click="close" class="text-gray-500 hover:text-gray-800 text-2xl transition-colors">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          <div class="prose prose-sm max-w-none">
            <div class="bg-red-50 border-l-4 border-red-500 p-4 mb-4">
              <p class="text-red-600 font-semibold">Примечание: Выравнивание текста, цвет и подчеркивание используют HTML-теги и не являются стандартным Markdown.</p>
            </div>
            
            <h3 class="text-lg font-semibold text-gray-800 border-b pb-1 mb-3">Основное форматирование</h3>
            <ul class="space-y-2">
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600"># Заголовок 1</code> (<code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">## H2</code>, <code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">### H3</code> ... <code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">###### H6</code>)</li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">**Жирный текст**</code> или <code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">__Жирный текст__</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">*Курсив*</code> или <code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">_Курсив_</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600"><u>Подчеркнутый текст</u></code> (Использует HTML)</li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">~~Зачеркнутый~~</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">`Встроенный код`</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">[Текст ссылки](https://example.com)</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">> Цитата</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">---</code> или <code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">***</code> для горизонтальной линии</li>
              <li>Размер шрифта: Выделите текст и используйте выпадающий список.</li>  
            </ul>
  
            <h3 class="text-lg font-semibold text-gray-800 border-b pb-1 mb-3 mt-6">Списки</h3>
            <p class="font-medium">Маркированный:</p>
            <ul class="space-y-1">
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">- Пункт 1</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">* Пункт 2</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">+ Пункт 3</code> (Отступ для подпунктов: <code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">  - Подпункт</code>)</li>
            </ul>
            <p class="font-medium mt-3">Нумерованный:</p>
            <ol class="space-y-1">
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">1. Первый пункт</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">2. Второй пункт</code> (Нумерация автоматическая)</li>
            </ol>
  
            <h3 class="text-lg font-semibold text-gray-800 border-b pb-1 mb-3 mt-6">Выравнивание, цвет и подчеркивание (с использованием HTML)</h3>
            <ul class="space-y-2">
              <li>Выравнивание: Выделите текст/строку и используйте кнопки панели инструментов .</li>
              <li>Цвет: Выделите текст и используйте палитру цветов .</li>
              <li>Подчеркивание: Выделите текст и используйте кнопку U.</li>
            </ul>
  
            <h3 class="text-lg font-semibold text-gray-800 border-b pb-1 mb-3 mt-6">Таблицы</h3>
            <pre class="bg-gray-100 p-3 rounded text-sm overflow-x-auto">
              <code>
                    | Заголовок 1 | Заголовок 2 |
                    | ---------- | ---------- |
                    | Ячейка 1   | Ячейка 2   |
                    | Ячейка 3   | Ячейка 4   |
              </code>
            </pre>
            <p class="text-sm mt-2">(Дефисы <code class="bg-gray-100 px-1 py-0.5 rounded text-red-600">---</code> определяют разделитель заголовков. Двоеточия можно использовать для выравнивания: <code class="bg-gray-100 px-1 py-0.5 rounded text-red-600">:---:</code> по центру, <code class="bg-gray-100 px-1 py-0.5 rounded text-red-600">---:</code> по правому краю)</p>
            
            <h3 class="text-lg font-semibold text-gray-800 border-b pb-1 mb-3 mt-6">Горячие клавиши</h3>
            <ul class="space-y-2">
              <li><strong>Жирный:</strong> Ctrl+B</li>
              <li><strong>Курсив:</strong> Ctrl+I</li>
              <li><strong>Таб/Отступ:</strong> Tab (Shift+Tab для уменьшения отступа в списках)</li>
              <li><strong>Продолжить список:</strong> Enter (когда курсор в конце пункта списка)</li>
            </ul>
          </div>
          <div class="mt-6 text-right">
            <DefaultButton label="Закрыть" variant="primary" @click="close" />
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
   import DefaultButton from "@/components/buttons/DefaultButton.vue";

  export default {
    name: 'HelpModal',
    components: {
      DefaultButton,
    },
    
    props: {
      show: {
        type: Boolean,
        default: false
      }
    },
    methods: {
      close() {
        this.$emit('close');
      }
    }
  };
  </script>
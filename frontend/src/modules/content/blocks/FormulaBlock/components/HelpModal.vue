<template>
    <div class="fixed inset-0 backdrop-blur-sm bg-opacity-50 flex items-center justify-center z-50 p-4 rounded-lg">
      <div class="bg-white rounded-xl shadow-lg max-w-2xl w-full p-6 relative">
        <h2 class="text-xl font-semibold mb-4">Помощь по LaTeX</h2>
  
        <p class="text-sm text-gray-700 mb-4">
          Ниже приведены основные примеры синтаксиса LaTeX, которые помогут вам создавать математические формулы.
        </p>
  
        <ul class="text-sm space-y-2">
          <li><strong>Степени:</strong> <code>a^2</code> → \( a^2 \)</li>
          <li><strong>Индексы:</strong> <code>a_1</code> → \( a_1 \)</li>
          <li><strong>Дроби:</strong> <code>\\frac{a}{b}</code> → \( \frac{a}{b} \)</li>
          <li><strong>Корень:</strong> <code>\\sqrt{x}</code> → \( \sqrt{x} \)</li>
          <li><strong>Сумма:</strong> <code>\\sum_{i=1}^{n} i</code> → \( \sum_{i=1}^{n} i \)</li>
          <li><strong>Интеграл:</strong> <code>\\int_a^b x^2\\,dx</code> → \( \int_a^b x^2\,dx \)</li>
          <li><strong>Греческие буквы:</strong> <code>\\alpha, \\beta, \\gamma</code> → \( \alpha, \beta, \gamma \)</li>
          <li><strong>Формулы с условиями:</strong> <code>x = \\begin{cases} a & x &gt; 0 \\\\ b & x \\le 0 \\end{cases}</code></li>
        </ul>
  
        <div class="text-right mt-6">
          <button @click="$emit('close')" class="px-4 py-2 bg-blue-600 text-white text-sm rounded hover:bg-blue-700">
            Закрыть
          </button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'HelpModal',
  };
  </script>
  
  <style scoped>
  code {
    background-color: #f3f4f6;
    padding: 0.2rem 0.4rem;
    border-radius: 4px;
    font-family: monospace;
  }
  </style>
  
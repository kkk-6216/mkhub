<template>
  <div class="fixed inset-0 backdrop-blur-sm bg-opacity-50 flex items-center justify-center z-50 p-4 rounded-lg">
    <div class="bg-white rounded-xl shadow-lg max-w-2xl w-full p-6 relative">
      <h2 class="text-xl font-semibold mb-4">Помощь по LaTeX</h2>

      <p class="text-sm text-gray-700 mb-4">
        Ниже приведены часто используемые символы и примеры синтаксиса LaTeX для создания математических формул.
      </p>

      <ul class="text-sm space-y-2 mb-6">
        <li><strong>Степени:</strong> <code>a^2</code></li>
        <li><strong>Индексы:</strong> <code>a_1</code></li>
        <li><strong>Дроби:</strong> <code>\frac{a}{b}</code></li>
        <li><strong>Корень:</strong> <code>\sqrt{x}</code></li>
        <li><strong>Сумма:</strong> <code>\sum_{i=1}^{n} i</code></li>
        <li><strong>Интеграл:</strong> <code>\int_a^b x^2\,dx</code></li>
        <li><strong>Греческие буквы:</strong> <code>\alpha, \beta, \gamma</code></li>
        <li><strong>Неравенства:</strong> <code>\le, \ge, \neq</code></li>
        <li><strong>Стрелки:</strong> <code>\rightarrow, \leftarrow, \Rightarrow</code></li>
        <li><strong>Логика:</strong> <code>\wedge, \vee, \neg</code></li>
        <li><strong>Множества:</strong> <code>\in, \notin, \subset, \cup, \cap</code></li>
        <li><strong>Формулы с условиями:</strong> <code>\begin{cases} ... \end{cases}</code></li>
      </ul>


      <p class="text-sm text-gray-600">
        <a
          href="https://www.cmor-faculty.rice.edu/~heinken/latex/symbols.pdf"
          target="_blank"
          rel="noopener"
          class="text-main hover:underline"
        >
          Больше информации здесь (PDF)
        </a>
      </p>

      <div class="text-right mt-6">
        <DefaultButton label="Закрыть" variant="primary" @click="$emit('close')" />
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

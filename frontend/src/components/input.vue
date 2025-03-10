<template>
  <div class="mb-4">
    <label class="block text-sm font-bold text-main">{{ label }}</label>
    <input
        :value="modelValue"
        :type="type"
        :placeholder="placeholder"
        :required="required"
        @input="updateValue"
        class="mt-1 block w-full h-10 pl-2 rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
    />
  </div>
</template>

<script>
export default {
  props: {
    label: {
      type: String,
      required: true,
    },
    type: {
      type: String,
      default: 'text',
    },
    placeholder: {
      type: String,
      default: '',
    },
    required: {
      type: Boolean,
      default: false,
    },
    modelValue: {
      type: [String, Number], // Может быть строкой или числом
      default: '',
    },
  },
  emits: ['update:modelValue'], // Объявляем событие для обновления значения
  setup(props, { emit }) {
    const updateValue = (event) => {
      // Эмитим событие с новым значением
      emit('update:modelValue', event.target.value);
    };

    return {
      updateValue,
    };
  },
};
</script>
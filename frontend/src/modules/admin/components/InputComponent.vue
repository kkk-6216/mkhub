<template>
  <div class="relative">
    <label :for="computedId" class="block text-sm/6 font-medium " :class="labelClass">
      {{ label }}
    </label>

    <div class="mt-2">
      <div
          class="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300"
          :class="{
          'focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-main': isFocused && isValid,
          'focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-red-400 outline-red-400 focus:outline-red-400': !isValid
        }"
      >
        <input
            :id="computedId"
            :name="name"
            :type="type"
            :placeholder="placeholder"
            :value="modelValue"
            @input="handleInput"
            @focus="isFocused = true"
            @blur="isFocused = false"
            :class="[
            'block min-w-0 grow py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-400 focus:outline-none sm:text-sm/6'
          ]"
        />
      </div>
      <p v-if="!isValid" class="mt-1 text-sm text-red-500">
        {{ error }}
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'InputComponent',
  props: {
    modelValue: {
      type: String,
      default: '',
    },
    placeholder: {
      type: String,
      default: 'Введите...',
    },
    type: {
      type: String,
      default: 'text',
    },
    labelClass: {
      type: String,
      default: 'text-gray-500',
    },
    id: {
      type: String,
      default: null,
    },
    name: {
      type: String,
      default: 'inputField',
    },
    label: {
      type: String,
      default: '',
    },
    isValid: {
      type: Boolean,
      default: true,
    },
    error: {
      type: String,
      default: '',
    }
  },
  emits: ['update:modelValue'],
  data() {
    return {
      generatedId: this.id || this.generateUniqueId(),
      isFocused: false,
    };
  },
  computed: {
    computedId() {
      return this.id || this.generatedId;
    },
  },
  methods: {
    handleInput(event) {
      const value = event.target.value;
      this.$emit('update:modelValue', value);
    },
    generateUniqueId() {
      return (crypto?.randomUUID?.() || `input-${Math.random().toString(36).substr(2, 9)}`);
    },
  }

};
</script>

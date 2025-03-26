<template>
  <div class="relative">
    <label :for="computedId" class="block text-sm/6 font-medium text-gray-900">
      {{ label }}
    </label>

    <div class="mt-2">
      <div
          class="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300"
          :class="{
          'focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-main': isFocused,
          'focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-red-400 outline-red-400 focus:outline-red-400': !isLocallyValid && wasTouched
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
            @blur="wasTouched = true"
            :class="[
            'block min-w-0 grow py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-400 focus:outline-none sm:text-sm/6',
          ]"
        />
      </div>
      <p v-if="!isLocallyValid && wasTouched" class="mt-1 text-sm text-red-500">
        {{ validationMessage }}
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'InputItem',
  props: {
    modelValue: {
      type: String,
      default: '',
    },
    placeholder: {
      type: String,
      default: '',
    },
    type: {
      type: String,
      default: 'text',
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
    minLength: {
      type: Number,
      default: 1,
    },
    maxLength: {
      type: Number,
      default: 255,
    },
  },
  emits: ['update:modelValue', 'isValidated'],
  data() {
    return {
      wasTouched: false,
      isFocused: false,
      generatedId: this.id || this.generateUniqueId(),
    };
  },
  computed: {
    computedId() {
      return this.id || this.generatedId;
    },
    isLocallyValid() {
      const length = this.modelValue?.length || 0;
      return length >= this.minLength && length <= this.maxLength;
    },
    validationMessage() {
      const length = this.modelValue?.length || 0;
      if (length < this.minLength) {
        return `Минимальное количество символов: ${this.minLength}`;
      }
      if (length > this.maxLength) {
        return `Максимум количество символов: ${this.maxLength}`;
      }
      return '';
    },
  },
  methods: {
    handleInput(event) {
      const value = event.target.value;
      this.$emit('update:modelValue', value);

      const isValid = value.length >= this.minLength && value.length <= this.maxLength;
      this.$emit('isValidated', { isValid, field: this.name });
    },
    generateUniqueId() {
      return (crypto?.randomUUID?.() || `input-${Math.random().toString(36).substr(2, 9)}`);
    },
    markAsTouched() {
      this.wasTouched = true;
      this.handleInput({ target: { value: this.modelValue } });
    }
  },
};
</script>

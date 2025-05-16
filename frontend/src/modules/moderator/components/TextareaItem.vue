<template>
  <div class="relative mb-[20px]">
    <label :for="computedId" class="block text-sm/6 font-bold " :class="labelClass">
      {{ label }}
    </label>
    <div class="mt-2">
      <textarea
          :id="computedId"
          :name="name"
          :rows="rows"
          :placeholder="placeholder"
          v-model.trim="localValue"
          @input="$emit('update:modelValue', localValue)"
          class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-dark outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
          style="resize: none;"
      ></textarea>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TextareaItem',
  props: {
    modelValue: {
      type: String,
      default: '',
    },
    labelClass: {
      type: String,
      default: "text-dark"
    },
    label: {
      type: String,
      default: '',
    },
    placeholder: {
      type: String,
      default: '',
    },
    rows: {
      type: Number,
      default: 3,
    },
    name: {
      type: String,
      default: 'textarea',
    },
    id: {
      type: String,
      default: null,
    },
  },
  emits: ['update:modelValue'],
  data() {
    return {
      localValue: this.modelValue,
      generatedId: this.id || `textarea-${Math.random().toString(36).substr(2, 9)}`,
    };
  },
  computed: {
    computedId() {
      return this.id || this.generatedId;
    },
  },
  watch: {
    modelValue(newVal) {
      this.localValue = newVal;
    }
  }
};
</script>

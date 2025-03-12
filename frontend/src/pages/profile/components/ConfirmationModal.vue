<template>
  <div v-if="show" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full flex items-center justify-center">
    <div class="relative p-5 border w-96 shadow-lg rounded-md bg-white">
      <div class="mt-3 text-center">
        <h3 class="text-lg leading-6 font-medium text-gray-900">{{ title }}</h3>
        <div class="mt-2 pb-2 px-4">
          <p class="text-sm text-gray-500">
            {{ message }}
          </p>
        </div>
      </div>
      <div class="items-center px-4 py-3 flex justify-around">
        <DefaultButton
            label="Отмена"
            @click="onCancel"
        />
        <DefaultButton
            label="Подтвердить"
            variant="danger"
            @click="onConfirm"
        />
      </div>
    </div>
  </div>
</template>

<script>
import DefaultButton from  "@/components/buttons/DefaultButton.vue";

export default {
  name: 'ConfirmationModal',
  components: {
    DefaultButton,
  },
  props: {
    show: {
      type: Boolean,
      required: true,
    },
    title: {
      type: String,
      default: 'Вы уверены?',
    },
    message: {
      type: String,
      required: true,
    },
  },
  emits: ['confirm', 'cancel'],
  methods: {
    onConfirm() {
      this.$emit('confirm');
    },
    onCancel() {
      this.$emit('cancel');
    },
  },
};
</script>

<style scoped>
</style>
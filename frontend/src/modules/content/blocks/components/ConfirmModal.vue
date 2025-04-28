<template>
    <div class="fixed inset-0 z-50 flex items-center justify-center backdrop-blur-sm">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <div class="mb-4">
          <h3 class="text-lg font-medium text-gray-900">{{ title }}</h3>
          <p class="mt-2 text-sm text-gray-500">{{ message }}</p>
        </div>
        <div class="flex justify-end space-x-3">
          <DefaultButton v-bind:label="confirmText" variant="danger" @click="$emit('confirm')" />
          <DefaultButton v-bind:label="cancelText" variant="primary" @click="$emit('cancel')" />
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import DefaultButton from "@/components/buttons/DefaultButton.vue";

  export default {
    name: 'ConfirmModal',
    components: {
      DefaultButton,
    },
    props: {
      title: {
        type: String,
        default: 'Подтверждение действия'
      },
      message: {
        type: String,
        required: true
      },
      confirmText: {
        type: String,
        default: 'Подтвердить'
      },
      cancelText: {
        type: String,
        default: 'Отмена'
      }
    }
  };
  </script>
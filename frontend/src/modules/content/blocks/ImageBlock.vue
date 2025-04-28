<template>
  <div class="mb-2 p-4 rounded-md hover:bg-gray-50 transition-colors duration-200 relative group">
    <!-- Скрытый input для замены изображения -->
    <input
      type="file"
      accept="image/*"
      ref="fileInput"
      class="hidden"
      @change="onFileSelected"
    />

    <!-- Изображение -->
    <div class="relative">
      <img
        :src="data.src"
        :alt="data.caption || 'Image'"
        class="max-w-full max-h-[500px] rounded"
        @click="openImage"
      />
      <p v-if="data.caption" class="text-sm text-gray-500 mt-2">{{ data.caption }}</p>
    </div>

    <!-- Меню опций -->
    <div class="absolute top-2 right-2">
      <OptionsMenu
        @copy="copyImageUrl"
        @download="downloadImage"
        @delete="showDeleteModal = true"
        @edit="triggerImageEdit"
      />
    </div>

    <!-- Модальное окно подтверждения удаления -->
    <ConfirmModal
      v-if="showDeleteModal"
      title="Удаление изображения"
      message="Вы уверены, что хотите удалить это изображение?"
      confirm-text="Удалить"
      cancel-text="Отмена"
      @confirm="confirmDelete"
      @cancel="showDeleteModal = false"
    />
  </div>
</template>

<script>
import { defineComponent } from 'vue';
import OptionsMenu from '@/modules/content/blocks/components/OptionsMenu.vue';
import ConfirmModal from '@/modules/content/blocks/components/ConfirmModal.vue';

export default defineComponent({
  name: "ImageBlock",
  components: {
    OptionsMenu,
    ConfirmModal
  },
  inject: ['showAlert'],
  props: {
    data: {
      type: Object,
      required: true,
      validator: (value) => 'src' in value
    },
    index: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      showDeleteModal: false
    };
  },
  methods: {
    openImage() {
      window.open(this.data.src, '_blank', 'noopener,noreferrer');
    },
    copyImageUrl() {
      navigator.clipboard.writeText(this.data.src)
        .then(() => this.showAlert('success','URL изображения скопирован'))
        .catch(err => {
          console.error('Ошибка копирования URL:', err);
          this.showAlert('error','Ошибка копирования URL');
        });
    },
    downloadImage() {
      try {
        const link = document.createElement('a');
        link.href = this.data.src;
        link.download = this.data.caption
          ? `image-${this.data.caption.substring(0, 20)}.${this.getFileExtension(this.data.src)}`
          : `image.${this.getFileExtension(this.data.src)}`;
        link.rel = 'noopener noreferrer';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      } catch (err) {
        console.error('Ошибка скачивания:', err);
        this.showAlert('error','Ошибка скачивания');
      }
    },
    getFileExtension(url) {
      return url.split('.').pop().split(/#|\?/)[0].toLowerCase();
    },
    triggerImageEdit() {
      this.$refs.fileInput?.click(); // <- открывает выбор нового изображения
    },
    onFileSelected(event) {
      const file = event.target.files[0];
      if (!file) return;

      const reader = new FileReader();
      reader.onload = () => {
        this.$emit('update', {
          index: this.index,
          newData: {
            src: reader.result,
            caption: file.name
          }
        });
      };
      reader.readAsDataURL(file);
      event.target.value = '';
    },
    confirmDelete() {
      this.$emit('delete', this.index);
      this.showDeleteModal = false;
    }
  }
});
</script>

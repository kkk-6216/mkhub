<template>
  <div class="mb-4 p-4 rounded-md hover:bg-gray-100 transition-colors duration-200 relative group">
    <!-- Image Content -->
    <div class="relative">
      <img 
        :src="data.src" 
        :alt="data.caption || 'Image'" 
        class="max-w-full h-auto rounded"
        @click="openImage"
      >
      <p v-if="data.caption" class="text-sm text-gray-500 mt-2">{{ data.caption }}</p>
    </div>

    <!-- Options Menu -->
    <div class="absolute top-2 right-2">
      <OptionsMenu
        @copy="copyImageUrl"
        @download="downloadImage"
        @delete="$emit('delete', index)"
      />
    </div>
  </div>
</template>

<script>
import { defineComponent } from 'vue';
import OptionsMenu from '@/modules/content/components/com/OptionsMenu.vue'; // Путь к компоненту нужно правильно указать

export default defineComponent({
  name: "ImageBlock",
  components: {
    OptionsMenu
  },
  props: {
    data: {
      type: Object,
      required: true,
      validator: (value) => {
        return 'src' in value;
      }
    },
    index: {
      type: Number,
      required: true
    }
  },
  methods: {
    openImage() {
      // Реализация открытия изображения
      window.open(this.data.src, '_blank', 'noopener,noreferrer');
    },
    copyImageUrl() {
      navigator.clipboard.writeText(this.data.src)
        .then(() => {
          this.$toast?.success('URL изображения скопирован') || alert('URL изображения скопирован');
        })
        .catch(err => {
          console.error('Ошибка копирования URL:', err);
          this.$toast?.error('Ошибка копирования URL') || alert('Ошибка копирования URL');
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
        this.$toast?.error('Ошибка скачивания') || alert('Ошибка скачивания');
      }
    },
    getFileExtension(url) {
      return url.split('.').pop().split(/#|\?/)[0].toLowerCase();
    }
  }
});
</script>
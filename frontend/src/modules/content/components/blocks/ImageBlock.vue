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
    <div class="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity duration-200">
      <button 
        @click.stop="toggleOptions" 
        class="text-gray-500 hover:text-gray-700 focus:outline-none p-1"
        aria-label="Image options"
      >
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"></path>
        </svg>
      </button>

      <Transition name="fade">
        <div 
          v-if="showOptions" 
          v-click-outside="closeOptions"
          class="absolute right-0 mt-1 bg-white border border-gray-200 rounded-md shadow-lg z-10 w-48"
        >
          <button 
            @click="handleAction('copy')" 
            class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white"
          >
            <CopyIcon class="w-5 h-5 mr-3"/>
            Копировать URL
          </button>
          <button 
            @click="handleAction('download')" 
            class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white"
          >
            <DownloadIcon class="w-5 h-5 mr-3"/>
            Скачать
          </button>
          <button 
            @click="handleAction('delete')" 
            class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white"
          >
            <DeleteIcon class="w-5 h-5 mr-3"/>
            Удалить
          </button>
        </div>
      </Transition>
    </div>
  </div>
</template>

<script>
import { defineComponent } from 'vue';
import DownloadIcon from '@/modules/content/components/icons/DownloadIcon.vue';
import DeleteIcon from '@/modules/content/components/icons/DeleteIcon.vue';
import CopyIcon from '@/modules/content/components/icons/CopyIcon.vue';

export default defineComponent({
  name: "ImageBlock",
  components: {
    CopyIcon,
    DeleteIcon,
    DownloadIcon
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
  data() {
    return {
      showOptions: false,
    };
  },
  directives: {
    clickOutside: {
      mounted(el, binding) {
        el._clickOutside = event => {
          if (!(el === event.target || el.contains(event.target))) {
            binding.value(event);
          }
        };
        document.addEventListener('click', el._clickOutside);
      },
      unmounted(el) {
        document.removeEventListener('click', el._clickOutside);
      }
    }
  },
  methods: {
    toggleOptions() {
      this.showOptions = !this.showOptions;
    },
    closeOptions() {
      this.showOptions = false;
    },
    handleAction(action) {
      switch(action) {
        case 'delete':
          this.$emit('delete', this.index);
          break;
        case 'copy':
          this.copyImageUrl();
          break;
        case 'download':
          this.downloadImage();
          break;
      }
      this.closeOptions();
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

<style scoped>
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
</style>
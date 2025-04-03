<template>
  <div class="flex items-center p-2 rounded-md hover:bg-gray-100 transition-colors duration-200 relative group">
    <!-- File Icon -->
    <svg class="w-6 h-6 mr-2 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z"></path>
    </svg>
    
    <!-- File Info -->
    <div class="flex items-center flex-grow">
      <a 
        :href="data.fileUrl || '#'" 
        @click.prevent="openFile" 
        class="font-medium text-main hover:underline truncate max-w-xs"
        :title="data.name"
      >
        {{ data.name }}
      </a>
      <span class="text-gray-500 text-xs ml-2 shrink-0">{{ formattedSize }}</span>
    </div>

    <!-- Options Menu -->
    <div class="relative ml-auto">
      <button 
        @click.stop="showOptions = !showOptions" 
        class="text-main hover:text-main-hover focus:outline-none p-1 opacity-0 group-hover:opacity-100 transition-opacity duration-200"
        aria-label="File options"
      >
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"></path>
        </svg>
      </button>

      <Transition name="fade">
        <div 
          v-if="showOptions" 
          v-click-outside="closeOptions"
          class="absolute right-0 top-full mt-1 bg-white border border-gray-200 rounded-md shadow-lg z-10 w-48"
        >
          <button 
              @click="handleAction('copy')" 
              class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white"
            >
            <CopyIcon class="w-5 h-5 mr-3"/>
            Копировать
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
import DownloadIcon from "@/modules/content/components/icons/DownloadIcon.vue";
import DeleteIcon from "@/modules/content/components/icons/DeleteIcon.vue";
import CopyIcon from "@/modules/content/components/icons/CopyIcon.vue";

export default defineComponent({
  name: "FileBlock",
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
        return 'name' in value && 'size' in value;
      }
    },
    index: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      showOptions: false
    };
  },
  computed: {
    formattedSize() {
      return this.formatFileSize(this.data.size);
    },
    hasFileData() {
      return Boolean(this.data.fileUrl || this.data.file);
    }
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
    formatFileSize(bytes) {
      if (!bytes || bytes === 0) return '0 Bytes';
      const k = 1024;
      const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      return `${parseFloat((bytes / Math.pow(k, i)).toFixed(2))} ${sizes[i]}`;
    },
    openFile() {
      if (!this.hasFileData) {
        this.$toast?.error('Файл недоступен') || alert('Файл недоступен');
        return;
      }

      if (this.data.fileUrl) {
        window.open(this.data.fileUrl, '_blank', 'noopener,noreferrer');
      } else if (this.data.file) {
        const url = URL.createObjectURL(this.data.file);
        window.open(url, '_blank', 'noopener,noreferrer');
        URL.revokeObjectURL(url);
      }
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
          this.copyFileUrl();
          break;
        case 'download':
          this.downloadFile();
          break;
      }
      this.closeOptions();
    },
    copyFileUrl() {
      if (!this.data.fileUrl) {
        this.$toast?.error('Нет URL для копирования') || alert('Нет URL для копирования');
        return;
      }
      
      navigator.clipboard.writeText(this.data.fileUrl)
        .then(() => {
          this.$toast?.success('Ссылка скопирована') || alert('Ссылка скопирована');
        })
        .catch(err => {
          console.error('Ошибка копирования:', err);
          this.$toast?.error('Ошибка копирования') || alert('Ошибка копирования');
        });
    },
    downloadFile() {
      if (!this.hasFileData) {
        this.$toast?.error('Файл недоступен для скачивания') || alert('Файл недоступен для скачивания');
        return;
      }

      try {
        const link = document.createElement('a');
        link.href = this.data.fileUrl || URL.createObjectURL(this.data.file);
        link.download = this.data.name;
        link.rel = 'noopener noreferrer';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        
        if (this.data.file) {
          URL.revokeObjectURL(link.href);
        }
      } catch (err) {
        console.error('Ошибка скачивания:', err);
        this.$toast?.error('Ошибка скачивания') || alert('Ошибка скачивания');
      }
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
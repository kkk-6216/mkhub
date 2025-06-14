<template>
  <div class="flex items-center p-3 mb-2 rounded-md hover:bg-gray-50 transition-colors duration-200 relative group">
    <!-- Скрытый input для замены файла -->
    <input
      type="file"
      ref="fileInput"
      class="hidden"
      @change="onFileSelected"
    />

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
    <div class="absolute top-2 right-2">
      <OptionsMenu
        @copy="copyFileUrl"
        @download="downloadFile"
        @edit="triggerFileEdit"
        @delete="showDeleteModal = true"
      />
    </div>

    <!-- Модальное окно подтверждения удаления -->
    <ConfirmModal
      v-if="showDeleteModal"
      title="Удаление файла"
      message="Вы уверены, что хотите удалить этот файл?"
      confirm-text="Удалить"
      cancel-text="Отмена"
      @confirm="confirmDelete"
      @cancel="showDeleteModal = false"
    />
  </div>
</template>

<script>
import OptionsMenu from '@/modules/content/blocks/components/OptionsMenu.vue';
import ConfirmModal from '@/modules/content/blocks/components/ConfirmModal.vue';

export default {
  name: "FileBlock",
  components: {
    OptionsMenu,
    ConfirmModal
  },
  inject: ['showAlert'],
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
      showDeleteModal: false
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
        this.showAlert('error','Файл недоступен');
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
    copyFileUrl() {
      if (!this.data.fileUrl) {
        this.showAlert('error','Нет URL для копирования файла');
        return;
      }
      
      navigator.clipboard.writeText(this.data.fileUrl)
        .then(() => {
          this.showAlert('success','Ссылка  файла скопирована');
        })
        .catch(err => {
          console.error('Ошибка копирования:', err);
          this.showAlert('error','Ошибка копирования');
        });
    },
    downloadFile() {
      if (!this.hasFileData) {
        this.showAlert('error','Файл недоступен для скачивания');
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
        this.showAlert('error','Ошибка скачивания файла');
      }
    },

    // 🆕 Открыть выбор нового файла
    triggerFileEdit() {
      this.$refs.fileInput?.click();
    },

    // 🆕 Обработка нового выбранного файла
    onFileSelected(event) {
      const file = event.target.files[0];
      if (!file) return;

      this.$emit('update', {
        index: this.index,
        newData: {
          name: file.name,
          size: file.size,
          file,
          fileUrl: null
        }
      });

      event.target.value = ''; // сброс input'а
    },
    confirmDelete() {
      this.$emit('delete', this.index);
      this.showDeleteModal = false;
    }
  }
};
</script>

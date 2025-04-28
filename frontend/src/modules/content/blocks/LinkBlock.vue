<template>
  <!-- Main container with hover group -->
  <div class="w-full flex items-center p-2 mb-2 rounded-md bg-white relative group transition-colors duration-150 hover:bg-gray-50">
    <!-- Icon Area -->
    <div class="flex-shrink-0 w-8 h-8 flex items-center justify-center mr-3 mt-0.5">
      <svg v-if="isLoading" class="animate-spin h-6 w-6 text-blue-500" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
      </svg>
      <img v-else-if="meta.favicon" :src="meta.favicon" alt="favicon" class="w-full h-full object-contain rounded">
      <div v-else class="w-full h-full flex items-center justify-center text-gray-400">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
        </svg>
      </div>
    </div>

    <!-- Content Area -->
    <div class="flex-grow min-w-0 relative" ref="contentRef">
      <!-- Display Mode -->
      <div v-if="!isEditing" @click="handleContentClick" class="cursor-text pt-1">
        <h4 class="font-semibold text-sm text-gray-800 truncate"
            :class="{ 'text-gray-500 italic': !meta.title && !isLoading && !error }">
          {{ displayText }}
        </h4>
        <p v-if="error" class="text-xs text-red-500 mt-1">{{ error }}</p>
        <p v-if="!meta.url && !isLoading && !error" class="text-xs text-gray-400 italic mt-1">Нажмите для добавления ссылки</p>
      </div>

      <!-- Edit Mode -->
      <div v-else class="edit-input-container pt-1">
        <input
          ref="urlInputRef"
          type="text"
          v-model="editUrl"
          placeholder="Введите URL и нажмите Enter"
          class="w-full text-sm p-1.5 rounded focus:outline-none placeholder-gray-400"
          @keydown.enter.prevent="saveUrl"
          @blur="handleBlur"
          @keydown.esc="cancelEdit"
        />
        <p v-if="error && isEditing" class="absolute top-full left-0 text-xs text-red-500 mt-1 w-full">{{ error }}</p>
      </div>

      <!-- Hover Card -->
      <div v-if="shouldShowHoverCard"
           :class="[cardPositionClass]"
           class="absolute left-0 w-72 z-20 opacity-0 invisible group-hover:opacity-100 group-hover:visible
                 transition-opacity duration-150 ease-in-out pointer-events-none group-hover:pointer-events-auto">
        <div class="bg-white rounded-lg shadow-xl overflow-hidden border border-gray-200">
          <div class="bg-white p-4 flex items-center justify-center min-h-[80px]">
            <img v-if="meta.favicon" :src="meta.favicon" alt="logo" class="max-h-16 max-w-[80%] object-contain">
            <div v-else class="w-16 h-16 bg-gray-100 rounded flex items-center justify-center text-gray-400">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
              </svg>
            </div>
          </div>
          <div class="p-3 bg-gray-50 border-t border-gray-100">
            <h4 class="font-semibold text-sm text-gray-900 truncate">{{ meta.title || 'Без заголовка' }}</h4>
            <p v-if="meta.description" class="text-xs text-gray-600 mt-1 break-words line-clamp-3">{{ meta.description }}</p>
            <div class="flex items-center space-x-1 mt-2">
              <img v-if="meta.favicon" :src="meta.favicon" alt="" class="w-3.5 h-3.5 object-contain flex-shrink-0">
              <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-3.5 w-3.5 text-gray-400 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
              </svg>
              <a :href="meta.url" target="_blank" rel="noopener noreferrer" class="text-xs text-blue-600 hover:underline truncate min-w-0" @click.stop>
                {{ meta.url?.replace(/^https?:\/\//, '') }}
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Options Menu -->
    <div class="absolute top-1 right-1 z-30">
      <OptionsMenu
        @copy="copyUrl"
        @download="downloadUrl"
        @edit="startEditing"
        @delete="showDeleteModal = true"
        @convert-to-image="convertToImageBlock"
        :disable-copy="!isValidUrl"
        :disable-download="!isValidUrl"
        :disable-convert-to-image="!isValidUrl || !isImageUrl"
      />
    </div>

     <!-- Модальное окно подтверждения удаления -->
     <ConfirmModal
      v-if="showDeleteModal"
      title="Удаление ссылки"
      message="Вы уверены, что хотите удалить эту ссылку?"
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

// API Configuration
const MICROLINK_API_KEY = '';
const API_BASE = MICROLINK_API_KEY ? 'https://pro.microlink.io' : 'https://api.microlink.io';

// URL utility functions
const isValidHttpUrl = (url) => {
  try {
    const parsed = new URL(url);
    return parsed.protocol === "http:" || parsed.protocol === "https:";
  } catch (_) {
    return false;
  }
};

const ensureHttpProtocol = (url) => {
  if (!url) return url;
  return url.match(/^https?:\/\//i) ? url : `https://${url}`;
};

export default {
  name: 'LinkBlock',
  components: { OptionsMenu, ConfirmModal },
  inject: ['showAlert'],
  props: {
    data: {
      type: Object,
      required: true,
      default: () => ({ url: '', title: '', description: '', favicon: '' })
    },
    index: {
      type: Number,
      required: true
    }
  },
  emits: ['update', 'delete', 'convert-to-image'],

  data() {
    return {
      isEditing: !this.data?.url,
      isLoading: false,
      error: '',
      editUrl: this.data?.url || '',
      initialUrl: this.data?.url || '',
      fetchTimeout: null,
      updateInProgress: false,
      showAbove: false,
      resizeObserver: null,
      meta: {
        url: this.data?.url || '',
        title: this.data?.title || '',
        description: this.data?.description || '',
        favicon: this.data?.favicon || ''
      },
      showDeleteModal: false
    };
  },

  computed: {
    isValidUrl() {
      return this.meta.url && isValidHttpUrl(this.meta.url);
    },

    isImageUrl() {
      if (!this.isValidUrl) return false;

      // Check file extensions
      const imageExtensions = ['.jpg', '.jpeg', '.png', '.gif', '.webp', '.svg', '.bmp', '.ico', '.tiff'];
      const url = this.meta.url.toLowerCase();

      const hasImageExtension = imageExtensions.some(ext =>
        url.endsWith(ext) || url.includes(ext + '?'));

      // Check URL patterns
      const hasImagePattern = /images?|\.(?:static|cdn|media)\.|\/img\/|\/photos?\//i.test(url);
      const hasImageQuery = url.includes('image') || url.includes('photo') || url.includes('pic');

      return hasImageExtension || (hasImagePattern && hasImageQuery);
    },

    displayText() {
      if (this.isLoading) return 'Загрузка...';
      if (this.error) return 'Ошибка URL';
      return this.meta.title || (this.meta.url ? this.meta.url : 'Без заголовка');
    },

    shouldShowHoverCard() {
      return !this.isEditing && this.meta.url && !this.isLoading && !this.error;
    },

    cardPositionClass() {
      return this.showAbove ? 'bottom-full mb-1.5' : 'top-full mt-1.5';
    }
  },

  watch: {
    data: {
      handler(newData) {
        if (this.updateInProgress) return;

        const newUrl = newData?.url || '';
        const currentUrl = this.meta.url;

        if (newUrl !== currentUrl ||
            newData?.title !== this.meta.title ||
            newData?.description !== this.meta.description ||
            newData?.favicon !== this.meta.favicon) {

          this.meta = {
            url: newUrl,
            title: newData?.title || '',
            description: newData?.description || '',
            favicon: newData?.favicon || ''
          };

          this.editUrl = newUrl;
          this.initialUrl = newUrl;

          if (this.fetchTimeout) clearTimeout(this.fetchTimeout);

          // Handle URL change
          if (!this.isEditing) {
            if (newUrl && isValidHttpUrl(newUrl)) {
              this.error = '';

              // Fetch metadata if needed
              if (newUrl !== currentUrl && !newData?.title) {
                this.fetchTimeout = setTimeout(() => {
                  if (this.meta.url === newUrl) {
                    this.fetchMetadata(newUrl);
                  }
                }, 300);
              }
            } else if (!newUrl) {
              this.error = '';
              this.startEditing();
            } else {
              this.error = 'Некорректный URL';
              this.meta.title = newUrl;
            }
          }
        }
      },
      deep: true
    }
  },

  mounted() {
    // Setup initial state
    if (this.meta.url && !this.meta.title && isValidHttpUrl(this.meta.url)) {
      this.fetchMetadata(this.meta.url);
    } else if (!this.meta.url && !this.isEditing) {
      this.startEditing();
    } else if (this.meta.url && !isValidHttpUrl(this.meta.url)) {
      this.error = "Некорректный URL";
      this.meta.title = this.meta.url;
    }

    // Setup position observer
    this.setupPositionObserver();

    // Auto-convert image URLs
    if (this.isValidUrl && this.isImageUrl) {
      this.convertToImageBlock();
    }
  },

  beforeUnmount() {
    if (this.fetchTimeout) clearTimeout(this.fetchTimeout);
    this.cleanupObservers();
  },

  methods: {
    // Fetch URL metadata
    async fetchMetadata(url) {
      if (!url || !isValidHttpUrl(url)) {
        this.error = 'Введите корректный URL (http:// или https://)';
        this.isLoading = false;
        return;
      }

      this.isLoading = true;
      this.error = '';

      try {
        const encodedUrl = encodeURIComponent(url);
        const apiUrl = `${API_BASE}/?url=${encodedUrl}&palette=true`;
        const headers = MICROLINK_API_KEY ? { 'x-api-key': MICROLINK_API_KEY } : {};

        const response = await fetch(apiUrl, { headers });

        if (!response.ok) {
          let errorMessage = `Ошибка API: ${response.status}`;
          try {
            const data = await response.json();
            errorMessage = data?.data?.message || data?.message || errorMessage;
          } catch(e) {}
          throw new Error(errorMessage);
        }

        const result = await response.json();

        if (result.status === 'success' && result.data) {
          const data = result.data;
          const title = data.title || url;
          const description = data.description || '';

          let favicon = data.logo?.url || '';
          if (!favicon) {
            try {
              const parsedUrl = new URL(url);
              favicon = `https://www.google.com/s2/favicons?sz=64&domain=${parsedUrl.hostname}`;
            } catch(e) {}
          }

          this.updateMetadata({
            url,
            title,
            description,
            favicon
          });
        } else {
          throw new Error(result.message || 'API вернуло неполные данные');
        }
      } catch (err) {
        console.error("Error fetching metadata:", err);
        this.error = `Не удалось загрузить данные: ${err.message || 'Неизвестная ошибка'}`;
        this.updateMetadata({
          url,
          title: url,
          description: '',
          favicon: ''
        });
      } finally {
        this.isLoading = false;
      }
    },

    // Update metadata and emit changes
    updateMetadata(newData) {
      this.updateInProgress = true;

      // Update local state
      this.meta = { ...this.meta, ...newData };
      this.error = '';

      // Emit update to parent
      this.$emit('update', {
        index: this.index,
        newData: { ...this.data, ...newData }
      });

      setTimeout(() => {
        this.updateInProgress = false;
      }, 50);
    },

    // Handle URL save
    async saveUrl() {
      const rawUrl = this.editUrl.trim();
      const newUrl = ensureHttpProtocol(rawUrl);

      if (newUrl !== rawUrl) {
        this.editUrl = newUrl;
      }

      if (newUrl === this.initialUrl && isValidHttpUrl(newUrl)) {
        this.isEditing = false;
        this.error = '';
        return;
      }

      if (!newUrl) {
        this.error = 'URL не может быть пустым';
        this.$refs.urlInputRef?.focus();
        return;
      }

      if (!isValidHttpUrl(newUrl)) {
        this.error = 'Некорректный URL (нужен http/https)';
        this.$refs.urlInputRef?.focus();
        return;
      }

      this.error = '';
      this.isLoading = true;
      this.isEditing = false;
      this.updateInProgress = true;

      // Initial update with minimal data
      this.updateMetadata({
        url: newUrl,
        title: newUrl
      });

      this.initialUrl = newUrl;

      // Fetch and update with complete metadata
      await this.fetchMetadata(newUrl);

      this.updateInProgress = false;

      // Auto-convert image URLs
      if (this.isImageUrl) {
        this.convertToImageBlock();
      }
    },

    // Start editing mode
    startEditing() {
      if (this.isLoading) return;

      this.initialUrl = this.meta.url;
      this.editUrl = this.meta.url;
      this.isEditing = true;
      this.error = '';

      this.$nextTick(() => {
        this.$refs.urlInputRef?.focus();
        this.$refs.urlInputRef?.select();
      });
    },

    // Cancel editing
    cancelEdit() {
      this.editUrl = this.initialUrl;
      this.isEditing = false;
      this.error = '';

      if (!this.initialUrl && !this.meta.url) {
        setTimeout(() => {
          this.$emit('delete');
        }, 50);
      } else if (this.initialUrl && !isValidHttpUrl(this.initialUrl)) {
        this.error = "Некорректный URL";
        this.meta.url = this.initialUrl;
        this.meta.title = this.initialUrl;
      } else if (this.initialUrl && !this.meta.title) {
        this.meta.title = this.initialUrl;
      }
    },

    // Handle click on content area
    handleContentClick() {
      if (!this.isEditing && this.meta.url && !this.isLoading && !this.error && isValidHttpUrl(this.meta.url)) {
        window.open(this.meta.url, '_blank', 'noopener,noreferrer');
      }
    },

    // Handle input blur
    handleBlur(event) {
      const relatedTarget = event.relatedTarget;

      if (relatedTarget && this.$el.contains(relatedTarget)) {
        const isOptionsMenu = this.$el.querySelector('.options-menu-container')?.contains(relatedTarget);
        const isHoverCard = this.$el.querySelector('.absolute.top-full, .absolute.bottom-full')?.contains(relatedTarget);

        if (isOptionsMenu || isHoverCard) {
          return;
        }
      }

      setTimeout(() => {
        if (this.isEditing) {
          this.saveUrl();
        }
      }, 150);
    },

    // Copy URL to clipboard
    copyUrl() {
      if (!this.isValidUrl) {
        this.showAlert('error','Нет URL для копирования');
        return;
      }

      navigator.clipboard.writeText(this.meta.url)
        .then(() => this.showAlert('success','Ссылка скопирована'))
        .catch(err => {
          console.error(err);
          this.showAlert('error','Ошибка копирования');
        });
    },

    // Trigger download from URL
    downloadUrl() {
      if (!this.isValidUrl) {
        this.showAlert('error','Нет URL для скачивания');
        return;
      }

      try {
        const link = document.createElement('a');
        link.href = this.meta.url;

        // Try to extract filename
        let downloadName = this.meta.title || 'download';
        try {
          const urlPath = new URL(this.meta.url).pathname;
          const filename = urlPath.substring(urlPath.lastIndexOf('/') + 1);
          if (filename) {
            downloadName = filename;
          }
        } catch(e) {}

        link.download = downloadName;
        link.rel = 'noopener noreferrer';
        link.target = '_blank';

        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      } catch (err) {
        console.error('Ошибка скачивания:', err);
        this.showAlert('error','Ошибка при попытке скачивания.');
        window.open(this.meta.url, '_blank', 'noopener,noreferrer');
      }
    },

    // Convert link to image block
    convertToImageBlock() {
      if (!this.isValidUrl) {
        console.error("LinkBlock: Cannot convert to image - invalid URL");
        return;
      }

      this.$emit('convert-to-image', {
        index: this.index,
        imageUrl: this.meta.url,
        caption: this.meta.title || 'Image from URL',
        alt: this.meta.title || 'Image from URL'
      });
    },

    // Setup position observer
    setupPositionObserver() {
      // Initial position check
      this.$nextTick(this.checkPosition);

      // Add event listeners
      window.addEventListener('scroll', this.checkPosition);
      window.addEventListener('resize', this.checkPosition);

      // Setup resize observer
      if (typeof ResizeObserver !== 'undefined') {
        this.resizeObserver = new ResizeObserver(this.checkPosition);
        if (this.$refs.contentRef) {
          this.resizeObserver.observe(this.$refs.contentRef);
        }
      }
    },

    // Clean up observers
    cleanupObservers() {
      window.removeEventListener('scroll', this.checkPosition);
      window.removeEventListener('resize', this.checkPosition);

      if (this.resizeObserver) {
        this.resizeObserver.disconnect();
      }
    },

    // Check and set hover card position
    checkPosition() {
      if (!this.$refs.contentRef) return;

      const rect = this.$refs.contentRef.getBoundingClientRect();
      const hoverCardHeight = 250;

      const spaceBelow = window.innerHeight - rect.bottom;
      const spaceAbove = rect.top;

      this.showAbove = spaceBelow < hoverCardHeight && spaceAbove >= hoverCardHeight;
    },
    confirmDelete() {
      this.$emit('delete', this.index);
      this.showDeleteModal = false;
    }
  }
}
</script>

<style scoped>
.line-clamp-3 {
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
}
</style>
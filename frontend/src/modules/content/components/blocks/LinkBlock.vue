<template>
  <!-- Root container: White background, subtle padding, rounded corners, group for hover -->
  <div class="link-block w-full relative group bg-white rounded mb-2">
    <!-- Link Element: Contains icon and text, provides internal padding -->
    <a :href="localUrl || '#'" target="_blank" rel="noopener noreferrer" class="flex items-start space-x-3 no-underline text-gray-900 p-2">
      <!-- Favicon Area: Fixed size, centered content -->
      <div class="flex-shrink-0 w-10 h-10 flex items-center justify-center">
        <!-- Actual Favicon -->
        <img v-if="faviconUrl" :src="faviconUrl" alt="favicon" class="w-full h-full object-contain rounded">
        <!-- Placeholder: Light gray background, gray icon -->
        <div v-else class="w-full h-full bg-gray-100 rounded flex items-center justify-center text-gray-400">
           <!-- Loading Spinner -->
           <svg v-if="isLoading" class="animate-spin h-5 w-5 text-blue-500" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          <!-- Default Link Icon -->
          <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
          </svg>
        </div>
      </div>

      <!-- Content Area: Takes remaining space -->
      <div class="flex-grow min-w-0">
        <!-- Display Mode: Clickable to edit -->
        <div v-if="!isEditing" @click="enterEditMode" class="cursor-text">
          <!-- Title: Semibold, slightly larger, dark text. Lighter/italic when placeholder/error -->
          <h4 class="font-semibold text-sm truncate" :class="{ 'text-gray-500 italic': !localTitle && !isLoading && !error }">
            {{ isLoading ? 'Загрузка данных...' : (localTitle || (error ? 'Ошибка' : 'Без заголовка')) }}
          </h4>
          <!-- Description: Smaller, slightly lighter text -->
          <p v-if="localDescription && !isLoading && !error" class="text-xs text-gray-600 mt-0.5 break-words line-clamp-2">
            {{ localDescription }}
          </p>
          <!-- URL: Smallest, gray text -->
          <p class="text-xs text-gray-500 truncate mt-0.5">
            {{ localUrl || 'Нет URL' }}
          </p>
          <!-- Error Message: Red text -->
           <p v-if="error && !isLoading" class="text-xs text-red-500 mt-1">{{ error }}</p>
        </div>

        <!-- Edit Mode -->
        <div v-else>
          <!-- Input: Standard input styling for white background -->
          <input
            ref="urlInputRef"
            type="text"
            v-model="editUrl"
            placeholder="Введите URL и нажмите Enter"
            class="w-full text-sm p-1 border border-gray-300 rounded focus:outline-none focus:ring-1 focus:ring-blue-500 bg-white placeholder-gray-400"
            @keydown.enter.prevent="saveUrl"
            @blur="handleBlur"
            @keydown.esc="cancelEdit"
          />
           <p v-if="error" class="text-xs text-red-500 mt-1">{{ error }}</p>
        </div>
      </div>
    </a>

    <!-- Delete Button: Appears on hover, adjusted for white background -->
    <button
      v-if="!isEditing"
      @click.stop="$emit('delete')"
      class="absolute top-1 right-1 p-1 rounded text-gray-500 hover:bg-gray-100 hover:text-red-500 opacity-0 group-hover:opacity-100 transition-opacity"
      aria-label="Удалить блок ссылки"
    >
      <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
      </svg>
    </button>
  </div>
</template>

<script>
// Script content remains the same as your Options API version
// --- КОНФИГУРАЦИЯ API ---
const MICROLINK_API_KEY = ''; // <-- ВАШ API КЛЮЧ (ЕСЛИ ЕСТЬ)
const USE_PRO_ENDPOINT = false; // Установите true, если используете платный ключ

// Утилита для проверки URL
const isValidHttpUrl = (string) => {
  try {
    const url = new URL(string);
    return url.protocol === "http:" || url.protocol === "https:";
  } catch (_) {
    return false;
  }
};

export default {
  name: 'LinkBlock',
  props: {
    data: {
      type: Object,
      required: true,
      default: () => ({ url: '', title: '', description: '', favicon: '' })
    },
    index: { type: Number, required: true }
  },
  emits: ['update', 'delete'],
  data() {
    // Инициализация состояния из props
    const initialUrl = this.data?.url || '';
    return {
      isEditing: !initialUrl, // Начать редактирование, если URL пуст при инициализации
      isLoading: false,
      error: '',
      // Локальное состояние для отображения, инициализируется из props
      localUrl: initialUrl,
      localTitle: this.data?.title || '',
      localDescription: this.data?.description || '',
      faviconUrl: this.data?.favicon || '',
      // Для поля ввода в режиме редактирования
      editUrl: initialUrl,
      initialUrlBeforeEdit: initialUrl // Для отмены редактирования
    };
  },
  watch: {
    // --- Слежение за изменениями props.data извне ---
    data: {
        handler(newData, oldData) {
            if (JSON.stringify(newData) === JSON.stringify(oldData)) {
                 return;
            }
            console.log("LinkBlock: Prop data changed externally", newData);
            const newUrl = newData?.url || '';
            this.localUrl = newUrl;
            this.localTitle = newData?.title || '';
            this.localDescription = newData?.description || '';
            this.faviconUrl = newData?.favicon || '';
            this.editUrl = newUrl;
            this.initialUrlBeforeEdit = newUrl;
            if (newUrl && !newData?.title && !this.isLoading && !this.isEditing && isValidHttpUrl(newUrl)) {
                 console.log("LinkBlock: Fetching metadata due to external prop change with missing title.");
                 this.fetchMetadata(newUrl).then(meta => {
                     if (meta) {
                        if (this.localUrl === newUrl) {
                             this.updateBlockData({ url: newUrl, ...meta });
                        }
                     }
                 });
            } else if (!newUrl && !this.isEditing) {
                 // this.enterEditMode();
                 this.error = '';
            } else if (!isValidHttpUrl(newUrl) && newUrl !== '') {
                this.error = 'Некорректный URL получен извне';
            } else {
                 this.error = '';
            }
        },
        deep: true
    }
  },
  mounted() {
    if (this.localUrl && !this.localTitle && !this.isLoading && isValidHttpUrl(this.localUrl)) {
      console.log("LinkBlock: Fetching metadata on mount because title is missing.");
      this.fetchMetadata(this.localUrl).then(meta => {
         if (meta) {
             if (this.localUrl === this.data.url) {
               this.updateBlockData({ url: this.localUrl, ...meta });
             }
         }
      });
    } else if (!this.localUrl) {
      this.enterEditMode();
    }
  },
  methods: {
    async fetchMetadata(urlToFetch) {
        if (!urlToFetch || !isValidHttpUrl(urlToFetch)) {
            this.error = 'Введите корректный URL (http:// или https://)';
            this.isLoading = false;
            this.localTitle = '';
            this.localDescription = '';
            this.faviconUrl = '';
            return null;
        }
        this.isLoading = true;
        this.error = '';
        console.log(`LinkBlock: Fetching real metadata for ${urlToFetch} using Microlink`);
        const encodedUrl = encodeURIComponent(urlToFetch);
        let apiUrl = `https://api.microlink.io/?url=${encodedUrl}&palette=true`;
        const headers = {};
        if (MICROLINK_API_KEY && USE_PRO_ENDPOINT) {
             apiUrl = `https://pro.microlink.io/?url=${encodedUrl}&palette=true`;
             headers['x-api-key'] = MICROLINK_API_KEY;
             console.log("Using Microlink PRO endpoint with API Key");
        } else if (MICROLINK_API_KEY && !USE_PRO_ENDPOINT){
             console.warn("Microlink API Key provided but USE_PRO_ENDPOINT is false. Using free endpoint.");
        }
        try {
            const response = await fetch(apiUrl, { headers });
            if (!response.ok) {
                let errorMessage = `Ошибка API: ${response.status}`;
                try {
                    const errorData = await response.json();
                    errorMessage = errorData?.data?.message || errorData?.message || errorMessage;
                    console.error("Microlink API Error Response:", errorData);
                } catch (e) { /* Ignore */ }
                throw new Error(errorMessage);
            }
            const result = await response.json();
            console.log("Microlink API Success Response:", result);
            if (result.status === 'success' && result.data) {
                const meta = result.data;
                this.error = '';
                const title = meta.title || urlToFetch;
                const description = meta.description || '';
                let finalFavicon = meta.logo?.url || '';
                if (!finalFavicon) {
                    try {
                        const parsedUrl = new URL(urlToFetch);
                        finalFavicon = `https://www.google.com/s2/favicons?sz=64&domain=${parsedUrl.hostname}`;
                         console.log("Using Google Favicon as fallback:", finalFavicon);
                    } catch(e) {
                         console.warn("Could not parse URL for Google Favicon fallback", e);
                    }
                }
                return { title, description, favicon: finalFavicon };
            } else {
                throw new Error(result.message || 'API вернуло некорректный ответ');
            }
        } catch (err) {
            console.error("LinkBlock: Error fetching metadata via API:", err);
            this.error = `Не удалось загрузить данные: ${err.message || 'Неизвестная ошибка'}`;
            this.localTitle = urlToFetch;
            this.localDescription = '';
            this.faviconUrl = '';
            return null;
        } finally {
            this.isLoading = false;
        }
    },
    updateBlockData(newData) {
        if (newData.url !== undefined) this.localUrl = newData.url;
        if (newData.title !== undefined) this.localTitle = newData.title;
        if (newData.description !== undefined) this.localDescription = newData.description;
        if (newData.favicon !== undefined) this.faviconUrl = newData.favicon;
        this.isLoading = false;
        const updatePayload = {};
        if (newData.url !== undefined) updatePayload.url = newData.url;
        if (newData.title !== undefined) updatePayload.title = newData.title;
        if (newData.description !== undefined) updatePayload.description = newData.description;
        if (newData.favicon !== undefined) updatePayload.favicon = newData.favicon;
        if (Object.keys(updatePayload).length > 0) {
            console.log("LinkBlock: Emitting update event", { index: this.index, newData: updatePayload });
            this.$emit('update', {
                index: this.index,
                newData: updatePayload
            });
        }
    },
    async saveUrl() {
        const newUrl = this.editUrl.trim();
        if (newUrl === this.initialUrlBeforeEdit || !isValidHttpUrl(newUrl)) {
            if (!isValidHttpUrl(newUrl) && newUrl !== '') {
                 this.error = 'Введите корректный URL (http:// или https://)';
            } else if (newUrl === '') {
                 this.error = 'URL не может быть пустым';
            } else {
                this.error = '';
            }
            this.isEditing = false;
            if (!isValidHttpUrl(newUrl)) {
                this.editUrl = this.initialUrlBeforeEdit;
            }
            return;
        }
        this.error = '';
        this.localUrl = newUrl;
        this.initialUrlBeforeEdit = newUrl;
        this.localTitle = newUrl;
        this.localDescription = '';
        this.faviconUrl = '';
        this.isLoading = true;
        this.isEditing = false;
        const meta = await this.fetchMetadata(newUrl);
        if (meta) {
            this.updateBlockData({ url: newUrl, ...meta });
        } else {
            this.updateBlockData({ url: newUrl, title: newUrl, description: '', favicon: '' });
        }
    },
    enterEditMode() {
        if (this.isLoading) return;
        this.initialUrlBeforeEdit = this.localUrl;
        this.editUrl = this.localUrl;
        this.isEditing = true;
        this.error = '';
        this.$nextTick(() => {
            this.$refs.urlInputRef?.focus();
            this.$refs.urlInputRef?.select();
        });
    },
    cancelEdit() {
        this.editUrl = this.initialUrlBeforeEdit;
        this.isEditing = false;
        this.error = '';
        if (!this.initialUrlBeforeEdit) {
            // this.$emit('delete');
        }
    },
    handleBlur(event) {
        if (event.relatedTarget && event.currentTarget.contains(event.relatedTarget)) {
            return;
        }
        setTimeout(() => {
            if (this.isEditing) {
                 this.saveUrl();
            }
        }, 100);
    },
    focusEditor() {
        this.enterEditMode();
    }
  }
}
</script>

<style scoped>
/* line-clamp might require the @tailwindcss/line-clamp plugin */
.line-clamp-2 {
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}

.link-block {
 /* Ensure minimum height even when empty or loading */
  min-height: 56px; /* Adjusted based on p-2 padding */
}
</style>
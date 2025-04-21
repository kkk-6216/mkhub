<template>
  <!-- Main container with optimized classes -->
  <div class="w-full flex items-center p-2 mb-2 rounded-md bg-white relative group transition-colors duration-150 hover:bg-gray-50">
    <!-- A: Favicon/Icon Area (Always Visible) -->
    <div class="flex-shrink-0 w-8 h-8 flex items-center justify-center mr-3 mt-0.5">
      <!-- Conditionally display elements with v-if/else structure -->
      <svg v-if="isLoading" class="animate-spin h-6 w-6 text-blue-500" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
      </svg>
      <img v-else-if="faviconUrl" :src="faviconUrl" alt="favicon" class="w-full h-full object-contain rounded">
      <div v-else class="w-full h-full flex items-center justify-center text-gray-400">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
        </svg>
      </div>
    </div>

    <!-- B: Content Area -->
    <div class="flex-grow min-w-0 relative" ref="contentRef">
      <!-- Display Mode -->
      <div v-if="!isEditing" @click="handleClickOnDisplayArea" class="cursor-text pt-1">
        <h4 class="font-semibold text-sm text-gray-800 truncate"
            :class="{ 'text-gray-500 italic': !localTitle && !isLoading && !error }">
          {{ displayTitle }}
        </h4>
        <p v-if="error && !isLoading" class="text-xs text-red-500 mt-1">{{ error }}</p>
        <p v-if="!localUrl && !isLoading && !error" class="text-xs text-gray-400 italic mt-1">Нажмите для добавления ссылки</p>
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

      <!-- Hover Card Content -->
      <div v-if="shouldShowHoverCard"
           :class="showAbove ? 'bottom-full mb-1.5' : 'top-full mt-1.5'"
           class="absolute left-0 w-72 z-20 opacity-0 invisible group-hover:opacity-100 group-hover:visible
                 transition-opacity duration-150 ease-in-out pointer-events-none group-hover:pointer-events-auto">
        <div class="bg-white rounded-lg shadow-xl overflow-hidden border border-gray-200">
          <!-- Logo Section -->
          <div class="bg-white p-4 flex items-center justify-center min-h-[80px]">
            <img v-if="faviconUrl" :src="faviconUrl" alt="logo" class="max-h-16 max-w-[80%] object-contain">
            <div v-else class="w-16 h-16 bg-gray-100 rounded flex items-center justify-center text-gray-400">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
              </svg>
            </div>
          </div>
          <!-- Text Section -->
          <div class="p-3 bg-gray-50 border-t border-gray-100">
            <h4 class="font-semibold text-sm text-gray-900 truncate">
              {{ localTitle || 'Без заголовка' }}
            </h4>
            <p v-if="localDescription" class="text-xs text-gray-600 mt-1 break-words line-clamp-3">
              {{ localDescription }}
            </p>
            <div class="flex items-center space-x-1 mt-2">
              <img v-if="faviconUrl" :src="faviconUrl" alt="" class="w-3.5 h-3.5 object-contain flex-shrink-0">
              <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-3.5 w-3.5 text-gray-400 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
              </svg>
              <a :href="localUrl" target="_blank" rel="noopener noreferrer" class="text-xs text-blue-600 hover:underline truncate min-w-0" @click.stop="handleLinkClick">
                {{ displayUrlDomain }}
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
        @download="triggerDownload"
        @edit="enterEditMode"
        @delete="triggerDelete"
        @convert-to-image="convertToImageBlock"
        :disable-copy="isUrlInvalid"
        :disable-download="isUrlInvalid"
        :disable-convert-to-image="!isImageUrlValid"
      />
    </div>
  </div>
</template>

<script>
import OptionsMenu from '@/modules/content/blocks/components/OptionsMenu.vue';

// API configuration constants
const MICROLINK_API_KEY = '';
const USE_PRO_ENDPOINT = false;
const HOVER_CARD_HEIGHT = 250; // Approximate height of hover card in pixels

// URL validation utility
const isValidHttpUrl = (string) => {
  try { 
    const url = new URL(string); 
    return url.protocol === "http:" || url.protocol === "https:"; 
  } catch (_) { 
    return false; 
  }
};

// Ensure URL has correct protocol
const ensureHttpProtocol = (url) => {
  if (!url) return url;
  if (!url.match(/^https?:\/\//i)) return `https://${url}`;
  return url;
};

// Image extensions for detection
const IMAGE_EXTENSIONS = ['.jpg', '.jpeg', '.png', '.gif', '.webp', '.svg', '.bmp', '.ico', '.tiff'];
const IMAGE_PATTERNS = [
  /images?/i,
  /\.(static|cdn|media)\./i,
  /\/img\//i,
  /\/photos?\//i
];

export default {
  name: 'LinkBlock',
  components: { OptionsMenu },
  props: {
    data: { 
      type: Object, 
      required: true, 
      default: () => ({ url: '', title: '', description: '', favicon: '' }) 
    },
    index: { type: Number, required: true }
  },
  emits: ['update', 'delete', 'convert-to-image'],
  
  data() {
    const initialUrl = this.data?.url || '';
    return {
      isEditing: !initialUrl,
      isLoading: false,
      error: '',
      localUrl: initialUrl,
      localTitle: this.data?.title || '',
      localDescription: this.data?.description || '',
      faviconUrl: this.data?.favicon || '',
      editUrl: initialUrl,
      initialUrlBeforeEdit: initialUrl,
      fetchTimeout: null,
      updateInProgress: false,
      showAbove: false,
      resizeObserver: null
    };
  },
  
  computed: {
    // Computed property for display title
    displayTitle() {
      if (this.isLoading) return 'Загрузка...';
      if (this.error) return 'Ошибка URL';
      return this.localTitle || (this.localUrl ? this.localUrl : 'Без заголовка');
    },
    
    // Check if URL is invalid for disabling options
    isUrlInvalid() {
      return !this.localUrl || !isValidHttpUrl(this.localUrl);
    },
    
    // Check if URL is valid image for conversion option
    isImageUrlValid() {
      return !this.isUrlInvalid && this.isImageUrl(this.localUrl);
    },
    
    // Display shortened domain instead of full URL
    displayUrlDomain() {
      return this.localUrl.replace(/^https?:\/\//, '');
    },
    
    // Determine if hover card should be shown
    shouldShowHoverCard() {
      return !this.isEditing && this.localUrl && !this.isLoading && !this.error;
    }
  },
  
  watch: {
    data: {
      handler(newData, oldData) {
        // Skip if update is in progress or data hasn't changed
        if (this.updateInProgress || JSON.stringify(newData) === JSON.stringify(oldData)) return;
        
        const newUrl = newData?.url || '';
        const oldLocalUrl = this.localUrl;
        
        // Update local data
        this.localUrl = newUrl;
        this.localTitle = newData?.title || '';
        this.localDescription = newData?.description || '';
        this.faviconUrl = newData?.favicon || '';
        this.editUrl = newUrl;
        this.initialUrlBeforeEdit = newUrl;
        
        // Clear any pending fetch
        if (this.fetchTimeout) clearTimeout(this.fetchTimeout);
        
        if (this.isEditing) {
          this.error = '';
          if (newUrl && !isValidHttpUrl(newUrl)) {
            this.error = 'Некорректный URL';
          }
        } else {
          if (newUrl && isValidHttpUrl(newUrl)) {
            this.error = '';
            const shouldFetch = newUrl !== oldLocalUrl && (!newData?.title || !this.localTitle);
            
            if (shouldFetch && !this.isLoading) {
              // Debounce metadata fetch
              this.fetchTimeout = setTimeout(() => {
                if (this.localUrl === newUrl) {
                  this.fetchMetadata(newUrl).then(meta => {
                    if (meta && this.localUrl === newUrl) {
                      this.updateBlockData({ url: newUrl, ...meta }, true);
                    }
                  });
                }
              }, 300);
            }
          } else if (!newUrl) {
            // Reset data and enter edit mode for empty URL
            this.localTitle = '';
            this.localDescription = '';
            this.faviconUrl = '';
            this.error = '';
            this.enterEditMode();
          } else {
            // Handle invalid URL
            this.error = 'Некорректный URL';
            this.localTitle = newUrl;
            this.localDescription = '';
            this.faviconUrl = '';
          }
        }
      },
      deep: true
    }
  },
  
  mounted() {
    // Initial setup based on URL state
    if (this.localUrl && !this.localTitle && !this.isLoading && isValidHttpUrl(this.localUrl)) {
      this.fetchMetadata(this.localUrl).then(meta => {
        if (meta && this.localUrl === this.data.url) {
          this.updateBlockData({ url: this.localUrl, ...meta });
        }
      });
    } else if (!this.localUrl && !this.isEditing) {
      this.enterEditMode();
    } else if (this.localUrl && !isValidHttpUrl(this.localUrl)) {
      this.error = "Некорректный URL";
      this.localTitle = this.localUrl;
    }
    
    // Set up position checking
    this.setupPositionObservers();
    
    // Auto-convert image URLs
    if (this.isImageUrlValid) {
      console.log("LinkBlock: URL is an image, suggesting conversion");
      this.convertToImageBlock();
    }
  },
  
  beforeUnmount() {
    // Clean up timers and observers
    if (this.fetchTimeout) clearTimeout(this.fetchTimeout);
    this.cleanupObservers();
  },
  
  methods: {
    // Set up position checking observers
    setupPositionObservers() {
      // Initial position check
      this.$nextTick(this.checkPosition);
      
      // Add event listeners
      window.addEventListener('scroll', this.checkPosition);
      window.addEventListener('resize', this.checkPosition);
      
      // Set up resize observer
      if (typeof ResizeObserver !== 'undefined') {
        this.resizeObserver = new ResizeObserver(this.checkPosition);
        if (this.$refs.contentRef) {
          this.resizeObserver.observe(this.$refs.contentRef);
        }
      }
    },
    
    // Clean up position observers
    cleanupObservers() {
      window.removeEventListener('scroll', this.checkPosition);
      window.removeEventListener('resize', this.checkPosition);
      
      if (this.resizeObserver) {
        this.resizeObserver.disconnect();
      }
    },
    
    // Check position for hover card placement
    checkPosition() {
      if (!this.$refs.contentRef) return;
      
      const rect = this.$refs.contentRef.getBoundingClientRect();
      const spaceBelow = window.innerHeight - rect.bottom;
      const spaceAbove = rect.top;
      
      // Show above if there's not enough space below but enough space above
      this.showAbove = spaceBelow < HOVER_CARD_HEIGHT && spaceAbove >= HOVER_CARD_HEIGHT;
    },
    
    // Fetch metadata for URL
    async fetchMetadata(urlToFetch) {
      if (!urlToFetch || !isValidHttpUrl(urlToFetch)) {
        this.error = 'Введите корректный URL (http:// или https://)';
        this.isLoading = false;
        return null;
      }
      
      this.isLoading = true;
      this.error = '';
      
      const encodedUrl = encodeURIComponent(urlToFetch);
      let apiUrl = `https://api.microlink.io/?url=${encodedUrl}&palette=true`;
      const headers = {};
      
      if (MICROLINK_API_KEY && USE_PRO_ENDPOINT) {
        apiUrl = `https://pro.microlink.io/?url=${encodedUrl}&palette=true`;
        headers['x-api-key'] = MICROLINK_API_KEY;
      }
      
      try {
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
          const meta = result.data;
          this.error = '';
          const title = meta.title || urlToFetch;
          const description = meta.description || '';
          
          // Get favicon from metadata or generate from URL
          let finalFavicon = meta.logo?.url || '';
          if (!finalFavicon) {
            try {
              const parsedUrl = new URL(urlToFetch);
              finalFavicon = `https://www.google.com/s2/favicons?sz=64&domain=${parsedUrl.hostname}`;
            } catch(e) {}
          }
          
          return { title, description, favicon: finalFavicon };
        } else {
          throw new Error(result.message || 'API вернуло неполные данные');
        }
      } catch (err) {
        console.error("Error fetching metadata:", err);
        this.error = `Не удалось загрузить данные: ${err.message || 'Неизвестная ошибка'}`;
        this.localTitle = urlToFetch;
        this.localDescription = '';
        this.faviconUrl = '';
        return null;
      } finally {
        this.isLoading = false;
      }
    },
    
    // Update block data and emit change events
    updateBlockData(newData, isExternal = false) {
      this.updateInProgress = true;
      let changed = false;
      const updatePayload = {};
      
      // Check each field for changes
      if (newData.url !== undefined && this.localUrl !== newData.url) {
        this.localUrl = newData.url;
        updatePayload.url = newData.url;
        changed = true;
      }
      
      if (newData.title !== undefined && this.localTitle !== newData.title) {
        this.localTitle = newData.title;
        updatePayload.title = newData.title;
        changed = true;
      }
      
      if (newData.description !== undefined && this.localDescription !== newData.description) {
        this.localDescription = newData.description;
        updatePayload.description = newData.description;
        changed = true;
      }
      
      if (newData.favicon !== undefined && this.faviconUrl !== newData.favicon) {
        this.faviconUrl = newData.favicon;
        updatePayload.favicon = newData.favicon;
        changed = true;
      }
      
      this.isLoading = false;
      
      if (changed) {
        this.error = '';
        this.$emit('update', { 
          index: this.index, 
          newData: { ...this.data, ...updatePayload } 
        });
      }
      
      // Reset updateInProgress flag after a short delay
      setTimeout(() => { this.updateInProgress = false; }, 50);
    },
    
    // Save URL and fetch metadata
    async saveUrl() {
      const rawUrl = this.editUrl.trim();
      const newUrl = ensureHttpProtocol(rawUrl);
      
      if (newUrl !== rawUrl) {
        this.editUrl = newUrl;
      }
      
      // Check if URL is unchanged
      if (newUrl === this.initialUrlBeforeEdit && isValidHttpUrl(newUrl)) {
        this.isEditing = false;
        this.error = '';
        return;
      }
      
      // Validate URL
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
      
      // Update state and exit edit mode
      this.error = '';
      this.isLoading = true;
      this.isEditing = false;
      this.updateInProgress = true;
      
      this.localUrl = newUrl;
      this.localTitle = newUrl;
      this.localDescription = '';
      this.faviconUrl = '';
      this.initialUrlBeforeEdit = newUrl;
      
      // Emit initial update
      this.$emit('update', { 
        index: this.index, 
        newData: { ...this.data, url: newUrl, title: newUrl } 
      });
      
      // Fetch metadata
      const meta = await this.fetchMetadata(newUrl);
      this.isLoading = false;
      
      // Check if URL changed during fetch
      if (this.localUrl !== newUrl) {
        this.updateInProgress = false;
        return;
      }
      
      // Update with metadata if available
      if (meta) {
        this.updateBlockData({ url: newUrl, ...meta });
      } else {
        this.updateBlockData({ url: newUrl, title: newUrl, description: '', favicon: '' });
      }
      
      this.updateInProgress = false;
      
      // Auto-convert if image URL
      if (this.isImageUrl(newUrl)) {
        console.log("LinkBlock: Detected image URL after save:", newUrl);
        this.convertToImageBlock();
      }
    },
    
    // Enter edit mode
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
    
    // Cancel edit mode
    cancelEdit() {
      this.editUrl = this.initialUrlBeforeEdit;
      this.isEditing = false;
      this.error = '';
      
      if (!this.initialUrlBeforeEdit && !this.localUrl) {
        // Delete empty block
        setTimeout(() => { this.triggerDelete(); }, 50);
      } else if (this.initialUrlBeforeEdit && !isValidHttpUrl(this.initialUrlBeforeEdit)) {
        // Handle invalid URL
        this.error = "Некорректный URL";
        this.localUrl = this.initialUrlBeforeEdit;
        this.localTitle = this.initialUrlBeforeEdit;
        this.localDescription = '';
        this.faviconUrl = '';
      } else if (this.initialUrlBeforeEdit && !this.localTitle) {
        // Use URL as title if none available
        this.localTitle = this.initialUrlBeforeEdit;
      }
    },
    
    // Handle blur event
    handleBlur(event) {
      const relatedTarget = event.relatedTarget;
      
      if (relatedTarget && this.$el.contains(relatedTarget)) {
        const optionsMenuEl = this.$el.querySelector('.options-menu-container');
        const hoverCardEl = this.$el.querySelector('.absolute.top-full, .absolute.bottom-full');
        
        if ((optionsMenuEl && optionsMenuEl.contains(relatedTarget)) || 
            (hoverCardEl && hoverCardEl.contains(relatedTarget))) {
          return;
        }
      }
      
      // Save on blur after short delay
      setTimeout(() => {
        if (this.isEditing) {
          this.saveUrl();
        }
      }, 150);
    },
    
    // Handle click on link
    handleLinkClick(event) {
      if (!event.currentTarget.href || event.currentTarget.href.endsWith('#')) {
        event.preventDefault();
      }
    },
    
    // Handle click on display area
    handleClickOnDisplayArea() {
      if (!this.isEditing && this.localUrl && !this.isLoading && !this.error && isValidHttpUrl(this.localUrl)) {
        window.open(this.localUrl, '_blank', 'noopener,noreferrer');
      }
    },
    
    // Copy URL to clipboard
    copyUrl() {
      if (this.isUrlInvalid) {
        alert('Нет URL для копирования');
        return;
      }
      
      navigator.clipboard.writeText(this.localUrl)
        .then(() => alert('Ссылка скопирована'))
        .catch(err => {
          console.error(err);
          alert('Ошибка копирования');
        });
    },
    
    // Trigger delete action
    triggerDelete() {
      this.$emit('delete');
    },
    
    // Trigger download action
    triggerDownload() {
      if (this.isUrlInvalid) {
        alert('Нет URL для скачивания');
        return;
      }
      
      try {
        const link = document.createElement('a');
        link.href = this.localUrl;
        
        // Extract filename from URL or use title
        let downloadName = this.localTitle || 'download';
        try {
          const urlPath = new URL(this.localUrl).pathname;
          const filename = urlPath.substring(urlPath.lastIndexOf('/') + 1);
          if (filename) {
            downloadName = filename;
          }
        } catch(e) {
          // Use default name if parsing fails
        }
        
        link.download = downloadName;
        link.rel = 'noopener noreferrer';
        link.target = '_blank';
        
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      } catch (err) {
        console.error('Ошибка скачивания:', err);
        alert('Ошибка при попытке скачивания файла.');
        // Fallback: Open in new tab
        window.open(this.localUrl, '_blank', 'noopener,noreferrer');
      }
    },
    
    // Check if URL is an image
    isImageUrl(url) {
      if (!url || !isValidHttpUrl(url)) return false;
      
      const lowercaseUrl = url.toLowerCase();
      
      // Check for image extensions
      const hasImageExtension = IMAGE_EXTENSIONS.some(ext => 
        lowercaseUrl.endsWith(ext) || lowercaseUrl.includes(ext + '?'));
      
      // Check for image patterns
      const matchesImagePattern = IMAGE_PATTERNS.some(pattern => pattern.test(url));
      
      // Check for image terms in query
      const hasImageQuery = url.includes('image') || url.includes('photo') || url.includes('pic');
      
      return hasImageExtension || (matchesImagePattern && hasImageQuery);
    },
    
    // Convert link to image block
    convertToImageBlock() {
      if (this.isUrlInvalid) {
        console.error("LinkBlock: Cannot convert to image - invalid URL");
        return;
      }
      
      console.log("LinkBlock: Converting link to image block:", this.localUrl);
      
      this.$emit('convert-to-image', {
        index: this.index,
        imageUrl: this.localUrl,
        caption: this.localTitle || 'Image from URL',
        alt: this.localTitle || 'Image from URL'
      });
    }
  }
};
</script>

<style scoped>
.line-clamp-3 {
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
}
</style>
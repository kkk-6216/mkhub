<template>
  <!-- Main container: Full width, flex layout, padding, NO BORDER, relative group -->
  <div class="w-full flex items-center p-3 mb-2 rounded-md bg-white relative group transition-colors duration-150 hover:bg-gray-50">
    <!-- A: Favicon/Icon Area (Always Visible) -->
    <div class="flex-shrink-0 w-10 h-10 flex items-center justify-center mr-3 mt-0.5">
       <!-- Loading Spinner -->
      <svg v-if="isLoading" class="animate-spin h-6 w-6 text-blue-500" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
      </svg>
      <!-- Actual Favicon -->
      <img v-else-if="faviconUrl" :src="faviconUrl" alt="favicon" class="w-full h-full object-contain rounded">
      <!-- Placeholder Icon -->
      <div v-else class="w-full h-full flex items-center justify-center text-gray-400">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
        </svg>
      </div>
    </div>

    <!-- B: Content Area (Takes remaining space) -->
    <div class="flex-grow min-w-0 relative" ref="contentRef">
      <!-- B1: Display Mode (Clickable to edit, shows ONLY TITLE) -->
      <div v-if="!isEditing" @click="handleClickOnDisplayArea" class="cursor-text pt-1"> <!-- Added pt-1 for better vertical alignment with taller icons -->
        <!-- Title: Always visible, main identifier -->
        <h4 class="font-semibold text-sm text-gray-800 truncate"
            :class="{ 'text-gray-500 italic': !localTitle && !isLoading && !error }">
          {{ isLoading ? 'Загрузка...' : (localTitle || (error ? 'Ошибка URL' : (localUrl ? localUrl : 'Без заголовка'))) }}
        </h4>
        <!-- URL Display Removed from here -->
        <!-- Error Message: Visible when error occurs -->
         <p v-if="error && !isLoading" class="text-xs text-red-500 mt-1">{{ error }}</p>
         <!-- Hint to edit if empty -->
         <p v-if="!localUrl && !isLoading && !error && !isEditing" class="text-xs text-gray-400 italic mt-1">Нажмите для добавления ссылки</p>
      </div>

      <!-- B2: Edit Mode (Replaces display mode) -->
      <div v-else class="edit-input-container pt-1"> <!-- Added pt-1 to align input similarly -->
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

      <!-- C: Hover Card Content (Appears on group hover, only when not editing and has valid data) -->
      <div v-if="!isEditing && localUrl && !isLoading && !error"
           :class="showAbove ? 'bottom-full mb-1.5' : 'top-full mt-1.5'"
           class="absolute left-0 w-72 z-20
                 opacity-0 invisible group-hover:opacity-100 group-hover:visible
                 transition-opacity duration-150 ease-in-out pointer-events-none group-hover:pointer-events-auto">
        <!-- Card Content remains the same -->
        <div class="bg-white rounded-lg shadow-xl overflow-hidden border border-gray-200">
           <!-- Top section with Large Logo -->
           <div class="bg-white p-4 flex items-center justify-center min-h-[80px]">
               <img v-if="faviconUrl" :src="faviconUrl" alt="logo" class="max-h-16 max-w-[80%] object-contain">
               <div v-else class="w-16 h-16 bg-gray-100 rounded flex items-center justify-center text-gray-400">
                 <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" /></svg>
               </div>
           </div>
           <!-- Bottom section with Text -->
           <div class="p-3 bg-gray-50 border-t border-gray-100">
              <h4 class="font-semibold text-sm text-gray-900 truncate">
                  {{ localTitle || 'Без заголовка' }}
              </h4>
              <p v-if="localDescription" class="text-xs text-gray-600 mt-1 break-words line-clamp-3">
                  {{ localDescription }}
              </p>
              <!-- URL Section (still present in hover card) -->
              <div class="flex items-center space-x-1 mt-2">
                  <img v-if="faviconUrl" :src="faviconUrl" alt="" class="w-3.5 h-3.5 object-contain flex-shrink-0">
                  <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-3.5 w-3.5 text-gray-400 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" /></svg>
                 <a :href="localUrl" target="_blank" rel="noopener noreferrer" class="text-xs text-blue-600 hover:underline truncate min-w-0" @click.stop="handleLinkClick">
                   {{ localUrl.replace(/^https?:\/\//, '') }}
                 </a>
              </div>
           </div>
        </div>
      </div>
      <!-- End Hover Card Content -->

    </div> <!-- End Content Area -->

    <!-- D: Options Menu (Appears on group hover when not editing) -->
    <div class="absolute top-1 right-1 z-30"> <!-- Removed v-if and hover opacity classes -->
       <OptionsMenu
        @copy="copyUrl"
        @download="triggerDownload"
        @edit="triggerEdit"
        @delete="triggerDelete"
        :disable-copy="!localUrl || !isValidHttpUrl(localUrl)"
        :disable-download="!localUrl || !isValidHttpUrl(localUrl)"
        />
    </div>

  </div>
</template>

<script>
import OptionsMenu from '@/modules/content/components/com/OptionsMenu.vue'; // Adjust path

// --- API CONFIG & UTILS --- (Keep from LinkBlockHover)
const MICROLINK_API_KEY = '';
const USE_PRO_ENDPOINT = false;

const isValidHttpUrl = (string) => { /* ... no change ... */
  try { const url = new URL(string); return url.protocol === "http:" || url.protocol === "https:"; } catch (_) { return false; }
};
const ensureHttpProtocol = (url) => { /* ... no change ... */
  if (!url) return url; if (!url.match(/^https?:\/\//i)) { return `https://${url}`; } return url;
};

export default {
  name: 'LinkBlock', // New name reflecting the primary display
  components: { OptionsMenu },
  props: { /* ... no change ... */
     data: { type: Object, required: true, default: () => ({ url: '', title: '', description: '', favicon: '' }) },
     index: { type: Number, required: true }
  },
  emits: ['update', 'delete'],
  data() { /* ... no change ... */
     const initialUrl = this.data?.url || '';
     return {
       isEditing: !initialUrl, isLoading: false, error: '',
       localUrl: initialUrl, localTitle: this.data?.title || '', localDescription: this.data?.description || '', faviconUrl: this.data?.favicon || '',
       editUrl: initialUrl, initialUrlBeforeEdit: initialUrl,
       fetchTimeout: null, updateInProgress: false,
       isValidHttpUrl: isValidHttpUrl,
       showAbove: false, // New data property to determine where to show hover card
       resizeObserver: null // Will hold the ResizeObserver instance
     };
   },
  watch: { /* ... no change ... */
    data: {
       handler(newData, oldData) { /* ... identical watch logic ... */
        if (this.updateInProgress || JSON.stringify(newData) === JSON.stringify(oldData)) return;
        const newUrl = newData?.url || ''; const oldLocalUrl = this.localUrl;
        this.localUrl = newUrl; this.localTitle = newData?.title || ''; this.localDescription = newData?.description || ''; this.faviconUrl = newData?.favicon || '';
        this.editUrl = newUrl; this.initialUrlBeforeEdit = newUrl;
        if (this.fetchTimeout) clearTimeout(this.fetchTimeout);
        if (this.isEditing) {
            this.error = ''; if (newUrl && !isValidHttpUrl(newUrl)) { this.error = 'Некорректный URL'; }
        } else {
            if (newUrl && isValidHttpUrl(newUrl)) {
                 this.error = '';
                 const shouldFetch = newUrl !== oldLocalUrl && (!newData?.title || !this.localTitle);
                 if (shouldFetch && !this.isLoading) {
                      this.fetchTimeout = setTimeout(() => {
                           if (this.localUrl === newUrl) {
                               this.fetchMetadata(newUrl).then(meta => {
                                   if (meta && this.localUrl === newUrl) { this.updateBlockData({ url: newUrl, ...meta }, true); }
                               });
                           }
                       }, 300);
                 }
            } else if (!newUrl) {
                 this.localTitle = ''; this.localDescription = ''; this.faviconUrl = ''; this.error = '';
                 this.enterEditMode();
            } else {
                 this.error = 'Некорректный URL'; this.localTitle = newUrl;
                 this.localDescription = ''; this.faviconUrl = '';
            }
        }
       },
       deep: true
     }
  },
  mounted() { 
     if (this.localUrl && !this.localTitle && !this.isLoading && isValidHttpUrl(this.localUrl)) {
       this.fetchMetadata(this.localUrl).then(meta => {
          if (meta && this.localUrl === this.data.url) { this.updateBlockData({ url: this.localUrl, ...meta }); }
       });
     } else if (!this.localUrl && !this.isEditing) {
        this.enterEditMode();
     } else if (this.localUrl && !isValidHttpUrl(this.localUrl)){
         this.error = "Некорректный URL"; this.localTitle = this.localUrl;
     }
     
     // Set up the mutation observer to check position and available space
     this.setupPositionCheck();
     
     // Add event listeners for scroll and resize to recalculate position
     window.addEventListener('scroll', this.checkPosition);
     window.addEventListener('resize', this.checkPosition);
     
     // Setup resize observer to detect changes in the element's size
     if (typeof ResizeObserver !== 'undefined') {
       this.resizeObserver = new ResizeObserver(() => {
         this.checkPosition();
       });
       if (this.$refs.contentRef) {
         this.resizeObserver.observe(this.$refs.contentRef);
       }
     }
  },
  beforeUnmount() { 
     if (this.fetchTimeout) clearTimeout(this.fetchTimeout);
     
     // Clean up all event listeners
     window.removeEventListener('scroll', this.checkPosition);
     window.removeEventListener('resize', this.checkPosition);
     
     // Clean up resize observer
     if (this.resizeObserver) {
       this.resizeObserver.disconnect();
     }
  },
  methods: {
    // --- CORE METHODS (fetchMetadata, updateBlockData, saveUrl, enterEditMode, cancelEdit, handleBlur) ---
    // --- remain unchanged ---
    
    // New method to check position and available space
    checkPosition() {
      if (!this.$refs.contentRef) return;
      
      const rect = this.$refs.contentRef.getBoundingClientRect();
      const hoverCardHeight = 250; // Approximate height of hover card in pixels
      
      const spaceBelow = window.innerHeight - rect.bottom;
      const spaceAbove = rect.top;
      
      // If there's not enough space below but enough space above, show above
      if (spaceBelow < hoverCardHeight && spaceAbove >= hoverCardHeight) {
        this.showAbove = true;
      } 
      // Otherwise default to showing below
      else {
        this.showAbove = false;
      }
    },
    
    // Add setup method
    setupPositionCheck() {
      // Initial position check
      this.$nextTick(() => {
        this.checkPosition();
      });
    },
    
    handleLinkClick(event) { /* ... no change ... */
      if (!event.currentTarget.href || event.currentTarget.href.endsWith('#')) { event.preventDefault(); }
    },
    async fetchMetadata(urlToFetch) { /* ... no change ... */
        if (!urlToFetch || !isValidHttpUrl(urlToFetch)) { this.error = 'Введите корректный URL (http:// или https://)'; this.isLoading = false; return null; }
        this.isLoading = true; this.error = '';
        const encodedUrl = encodeURIComponent(urlToFetch); let apiUrl = `https://api.microlink.io/?url=${encodedUrl}&palette=true`;
        const headers = {}; if (MICROLINK_API_KEY && USE_PRO_ENDPOINT) { apiUrl = `https://pro.microlink.io/?url=${encodedUrl}&palette=true`; headers['x-api-key'] = MICROLINK_API_KEY; }
        try {
            const response = await fetch(apiUrl, { headers });
            if (!response.ok) { let errorMessage = `Ошибка API: ${response.status}`; try { const d=await response.json(); errorMessage = d?.data?.message||d?.message||errorMessage; } catch(e){} throw new Error(errorMessage); }
            const result = await response.json();
            if (result.status === 'success' && result.data) {
                const meta = result.data; this.error = ''; const title = meta.title || urlToFetch; const description = meta.description || '';
                let finalFavicon = meta.logo?.url || '';
                if (!finalFavicon) { try { const pUrl = new URL(urlToFetch); finalFavicon = `https://www.google.com/s2/favicons?sz=64&domain=${pUrl.hostname}`; } catch(e){} }
                return { title, description, favicon: finalFavicon };
            } else { throw new Error(result.message || 'API вернуло неполные данные'); }
        } catch (err) {
            console.error("Error fetching metadata:", err); this.error = `Не удалось загрузить данные: ${err.message || 'Неизвестная ошибка'}`;
            this.localTitle = urlToFetch; this.localDescription = ''; this.faviconUrl = ''; return null;
        } finally { this.isLoading = false; }
    },
    updateBlockData(newData, isExternal = false) { /* ... no change ... */
        this.updateInProgress = true; let changed = false; const updatePayload = {};
        if (newData.url !== undefined && this.localUrl !== newData.url) { this.localUrl = newData.url; updatePayload.url = newData.url; changed = true; }
        if (newData.title !== undefined && this.localTitle !== newData.title) { this.localTitle = newData.title; updatePayload.title = newData.title; changed = true; }
        if (newData.description !== undefined && this.localDescription !== newData.description) { this.localDescription = newData.description; updatePayload.description = newData.description; changed = true; }
        if (newData.favicon !== undefined && this.faviconUrl !== newData.favicon) { this.faviconUrl = newData.favicon; updatePayload.favicon = newData.favicon; changed = true; }
        this.isLoading = false; if (changed) { this.error = ''; }
        if (changed) { this.$emit('update', { index: this.index, newData: { ...this.data, ...updatePayload } }); }
        setTimeout(() => { this.updateInProgress = false; }, 50);
    },
    async saveUrl() { /* ... no change ... */
        const rawUrl = this.editUrl.trim(); const newUrl = ensureHttpProtocol(rawUrl);
        if (newUrl !== rawUrl) { this.editUrl = newUrl; }
        if (newUrl === this.initialUrlBeforeEdit && isValidHttpUrl(newUrl)) { this.isEditing = false; this.error = ''; return; }
        if (!newUrl) { this.error = 'URL не может быть пустым'; this.$refs.urlInputRef?.focus(); return; }
        if (!isValidHttpUrl(newUrl)) { this.error = 'Некорректный URL (нужен http/https)'; this.$refs.urlInputRef?.focus(); return; }
        this.error = ''; this.isLoading = true; this.isEditing = false; this.updateInProgress = true;
        this.localUrl = newUrl; this.localTitle = newUrl; this.localDescription = ''; this.faviconUrl = ''; this.initialUrlBeforeEdit = newUrl;
        this.$emit('update', { index: this.index, newData: { ...this.data, url: newUrl, title: newUrl } });
        const meta = await this.fetchMetadata(newUrl);
        this.isLoading = false; if (this.localUrl !== newUrl) { this.updateInProgress = false; return; }
        if (meta) { this.updateBlockData({ url: newUrl, ...meta }); } else { this.updateBlockData({ url: newUrl, title: newUrl, description: '', favicon: '' }); }
        this.updateInProgress = false;
    },
    enterEditMode() { /* ... no change ... */
        if (this.isLoading) return; this.initialUrlBeforeEdit = this.localUrl; this.editUrl = this.localUrl;
        this.isEditing = true; this.error = '';
        this.$nextTick(() => { this.$refs.urlInputRef?.focus(); this.$refs.urlInputRef?.select(); });
    },
    cancelEdit() { /* ... no change ... */
        this.editUrl = this.initialUrlBeforeEdit; this.isEditing = false; this.error = '';
        if (!this.initialUrlBeforeEdit && !this.localUrl) { setTimeout(() => { this.triggerDelete(); }, 50); }
        else if (this.initialUrlBeforeEdit && !isValidHttpUrl(this.initialUrlBeforeEdit)) {
            this.error = "Некорректный URL"; this.localUrl = this.initialUrlBeforeEdit;
            this.localTitle = this.initialUrlBeforeEdit; this.localDescription = ''; this.faviconUrl = '';
        } else if (this.initialUrlBeforeEdit && !this.localTitle) { this.localTitle = this.initialUrlBeforeEdit; }
    },
    handleBlur(event) { /* ... no change ... */
        const relatedTarget = event.relatedTarget;
        if (relatedTarget && this.$el.contains(relatedTarget)) {
             const optionsMenuEl = this.$el.querySelector('.options-menu-container');
             const hoverCardEl = this.$el.querySelector('.absolute.top-full, .absolute.bottom-full'); // Updated selector
             if ((optionsMenuEl && optionsMenuEl.contains(relatedTarget)) || (hoverCardEl && hoverCardEl.contains(relatedTarget))) {
                 return;
             }
        }
        setTimeout(() => { if (this.isEditing) { this.saveUrl(); } }, 150);
    },

    // --- OptionsMenu Handlers ---
     copyUrl() { /* ... no change ... */
      if (!this.localUrl || !isValidHttpUrl(this.localUrl)) { alert('Нет URL для копирования'); return; }
      navigator.clipboard.writeText(this.localUrl)
        .then(() => alert('Ссылка скопирована'))
        .catch(err => { console.error(err); alert('Ошибка копирования'); });
    },

    handleClickOnDisplayArea() { 
      // Only navigate if not editing, not loading, no error, and URL is valid
      if (!this.isEditing && this.localUrl && !this.isLoading && !this.error && isValidHttpUrl(this.localUrl)) {
          window.open(this.localUrl, '_blank', 'noopener,noreferrer');
      }
      // Do nothing if conditions aren't met (e.g., clicking on loading/error/placeholder)
    },

    triggerEdit() { /* ... no change ... */
      this.enterEditMode();
    },

    triggerDelete() { /* ... no change ... */
      this.$emit('delete');
    },

    // --- NEW: Download Handler ---
    triggerDownload() {
      if (!this.localUrl || !isValidHttpUrl(this.localUrl)) {
        alert('Нет URL для скачивания');
        return;
      }
      try {
        // Attempt to trigger download by creating a link and clicking it
        const link = document.createElement('a');
        link.href = this.localUrl;

        // Try to extract a filename from the URL path or use the title
        let downloadName = this.localTitle || 'download';
        try {
            const urlPath = new URL(this.localUrl).pathname;
            const filename = urlPath.substring(urlPath.lastIndexOf('/') + 1);
            if (filename) {
                downloadName = filename;
            }
        } catch(e) {
            // Use default name if URL parsing fails
        }

        link.download = downloadName; // Suggest a filename
        link.rel = 'noopener noreferrer'; // Security best practice
        link.target = '_blank'; // Optional: Try opening in new tab first, fallback to download

        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);

      } catch (err) {
        console.error('Ошибка скачивания:', err);
        alert('Ошибка при попытке скачивания файла.');
        // Fallback: Open in new tab if direct download fails (might just display the content)
        window.open(this.localUrl, '_blank', 'noopener,noreferrer');
      }
    }
  }
}
</script>

<style scoped>
/* line-clamp might require the @tailwindcss/line-clamp plugin if not using Tailwind v3+ */
.line-clamp-3 {
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
}

/* Ensure minimum height for the block */
.link-block-title-hover { /* If you add this class to the root */
  min-height: 56px; /* Adjust based on p-3 padding and icon size */
}
</style>
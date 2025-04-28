<template>
  <div class="relative w-full p-5 mx-auto">
    <!-- Верхняя панель -->
    <div class="relative flex items-center justify-center p-5">
      <h1 class="text-2xl font-semibold">{{ topic }}</h1>

      <div class="absolute right-0 flex items-center space-x-2 pr-4">
        <button @click="goBack" class="rounded" title="Назад">
          <ArrowUturnLeftIcon class="w-5 h-5" />
        </button>

        <div class="relative" ref="dropdownRef">
          <button 
            @click="toggleDropdown" 
            class="p-1 rounded flex items-center"
            title="Действия"
          >
            <EllipsisVerticalIcon class="w-5 h-5" />
          </button>
          <transition name="dropdown">
            <div 
              v-show="isDropdownOpen" 
              class="absolute right-0 top-full mt-1 bg-white border border-gray-200 rounded-md shadow-lg z-10 w-48 py-1"
            >
              <button 
                @click="showDeleteModal = true" 
                class="flex items-center w-full px-3 py-2 text-sm text-left text-red-600 hover:bg-red-50 hover:text-red-700 transition-colors"
              >
                <TrashIcon class="w-4 h-4 mr-2" />
                Очистить
              </button>
              <button 
                @click="submitContent" 
                class="flex items-center w-full px-3 py-2 text-sm text-left text-gray-700 hover:bg-gray-100 hover:text-gray-900 transition-colors"
              >
                <PaperAirplaneIcon class="w-4 h-4 mr-2 transform rotate-315" />
                Отправить
              </button>
            </div>
          </transition>
        </div>
      </div>  
    </div>     

    <div class="mx-auto max-w-5xl">
      <!-- Hidden file inputs -->
      <input type="file" ref="imageInputRef" @change="handleImageUpload" accept="image/*" class="hidden" />
      <input type="file" ref="fileInputRef" @change="handleFileUpload" class="hidden" />

      <!-- Draggable content blocks -->
      <draggable
        v-model="contentBlocks"
        item-key="id"
        tag="div"
        class="mb-4"
        handle=".drag-handle"
        :animation="200"
        @start="dragStart"
        @end="dragEnd"
        :forceFallback="true"
      >
        <template #item="{ element: block, index }">
          <div
            class="relative group"
            :class="{ 'opacity-80 bg-gray-50 rounded': isDragging }"
            @mouseenter="hoveredBlockIndex = index"
            @mouseleave="hoveredBlockIndex = -1"
          >
            <!-- Drag handle button -->
            <button
              class="drag-handle absolute left-1 top-1 z-10 opacity-0 group-hover:opacity-100 transition-opacity duration-200 cursor-move p-1 rounded"
              :class="{ 'opacity-100': isDragging || hoveredBlockIndex === index }"
              title="Drag block"
            >
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6 text-gray-500 hover:text-gray-700">
                <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 15 12 18.75 15.75 15m-7.5-6L12 5.25 15.75 9" />
              </svg>
            </button>

            <!-- Content block -->
            <div class="pl-9 pt-1">
              <component
                :is="blockComponents[block.component]"
                :ref="el => setBlockRef(el, index)"
                :data="block.data"
                :index="index"
                :isActive="activeBlockIndex === index"
                @update="handleBlockUpdate"
                @delete="deleteBlock(index)"
                @request-new-block-after="insertNewBlockAfter"
                @convert-to-image="handleConvertToImage"
                @focus="handleBlockFocus(index)"
              />
            </div>
          </div>
        </template>
      </draggable>

      <!-- Input area for text and commands -->
      <EditorInput
        ref="editorInputRef"
        @command-typed="handleCommandTyped"
        @direct-input="processPastedOrEnteredContent"
        @text-entered="processPastedOrEnteredContent"
        @clear-requested="clearEditorInput"
      />

      <!-- Модальное окно подтверждения удаления -->
      <ConfirmModal
        v-if="showDeleteModal"
        title="Подтверждение действия."
        message="Вы уверены, что хотите очистить страницу?"
        confirm-text="Очистить"
        cancel-text="Отмена"
        @confirm="confirmDelete"
        @cancel="showDeleteModal = false"
      />
    </div>
  </div>
</template>

<script>
// Component imports 
import MarkdownEditor from '@/modules/content/blocks/MarkdownEditor/MarkdownEditor.vue';
import ImageBlock from '@/modules/content/blocks/ImageBlock.vue';
import FileBlock from '@/modules/content/blocks/FileBlock.vue';
import LinkBlock from '@/modules/content/blocks/LinkBlock.vue';
import CodeBlock from '@/modules/content/blocks/CodeBlock.vue';
import FormulaBlock from '@/modules/content/blocks/FormulaBlock/FormulaBlock.vue';
import EditorInput from '@/modules/content/ContentEditor/components/EditorInput.vue';
import ConfirmModal from '@/modules/content/blocks/components/ConfirmModal.vue';
import { useCommands } from '@/modules/content/ContentEditor/components/useCommands';
import { nextTick } from 'vue';
import draggable from 'vuedraggable';
import { ArrowUturnLeftIcon, TrashIcon, PaperAirplaneIcon, EllipsisVerticalIcon } from '@heroicons/vue/24/outline'

// Utilities
const isValidHttpUrl = (string) => {
  try {
    const url = new URL(string);
    return url.protocol === "http:" || url.protocol === "https:";
  } catch (_) { return false; }
};

const generateId = () => Math.random().toString(36).substring(2, 9);

const isImageUrl = (url) => {
  if (!isValidHttpUrl(url)) return false;
  const imageExtensions = /\.(jpg|jpeg|png|gif|webp|svg|bmp|ico|tiff)(\?.*)?$/i;
  return imageExtensions.test(url);
};

export default {
  name: "ContentEditor",
  components: {
    MarkdownEditor,
    ImageBlock,
    FileBlock,
    LinkBlock,
    CodeBlock,
    FormulaBlock,
    EditorInput,
    ConfirmModal,
    draggable,
    ArrowUturnLeftIcon,
    TrashIcon,
    PaperAirplaneIcon,
    EllipsisVerticalIcon
  },
  inject: ['showAlert'],
  props: {
    initialContent: { type: Array, default: () => [] }
  },
  emits: ['update:content'],
  data() {
    return {
      isDropdownOpen: false,
      showDeleteModal: false,
      blockRefs: {}, 
      blockComponents: {
        'text-block': MarkdownEditor,
        'image-block': ImageBlock,
        'file-block': FileBlock,
        'link-block': LinkBlock,
        'code-block': CodeBlock,
        'formula-block': FormulaBlock
      },
      activeBlockIndex: -1,
      isMounted: false,
      isDragging: false,
      hoveredBlockIndex: -1,
      topic: '',
      contentBlocks: []
    };
  },
  computed: {
    // Command actions
    commandActions() {
      return {
        text: () => this.addEmptyTextBlockAndFocus(this.contentBlocks.length),
        image: () => this.$refs.imageInputRef?.click(),
        file: () => this.$refs.fileInputRef?.click(),
        link: () => this.addLinkBlock(this.contentBlocks.length),
        code: () => this.addCodeBlock(this.contentBlocks.length),
        formula: () => this.addFormulaBlock(this.contentBlocks.length)
      };
    }
  },
  watch: {
    // Watch initialContent changes
    initialContent: {
      handler(newContent) {
        const currentSignificantData = JSON.stringify(this.contentBlocks.map(b => ({ component: b.component, data: b.data })));
        const newSignificantData = JSON.stringify(this.prepareBlocks(newContent || []).map(b => ({ component: b.component, data: b.data })));

        if (newSignificantData !== currentSignificantData) {
          this.contentBlocks = this.prepareBlocks(newContent || []);
          nextTick(() => {
            this.blockRefs = {};
          });
        }
      },
      immediate: true,
    },
    // Watch contentBlocks changes
    contentBlocks: {
      handler(newBlocks, oldBlocks) {
        if (!this.isMounted) return;
        if (JSON.stringify(newBlocks) !== JSON.stringify(oldBlocks)) {
          this.$emit('update:content', JSON.parse(JSON.stringify(newBlocks)));
        }
      },
      deep: true
    }
  },
  created() {
    this.commands = useCommands().commands;
  },
  mounted() {
    this.isMounted = true;
    if (this.contentBlocks.length === 0) {
      this.$refs.editorInputRef?.focus();
    }

    // Имитация получения темы с бэкенда
    setTimeout(() => {
      this.topic = 'Тестовые данные'
    }, 500);

    document.addEventListener('click', this.handleClickOutside);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
  },
  methods: {
    setBlockRef(el, index) {
      if (el) this.blockRefs[index] = el;
    },
    
    toggleDropdown() {
      this.isDropdownOpen = !this.isDropdownOpen;
    },

    handleClickOutside(e) {
      if (!this.$refs.dropdownRef?.contains(e.target)) {
        this.isDropdownOpen = false;
      }
    },

    goBack() {
      this.$router.back();
    },
    
    confirmDelete() {
      this.contentBlocks = [];
      this.showDeleteModal = false;
    },
    
    async submitContent() {
      try {
        const blocksWithOrder = this.contentBlocks.map((block, index) => ({
          order: index,
          componentType: block.component,
          blockId: block.id,
          data: block.data
        }));
        
        const response = await fetch('/api/submit-content', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            topic: this.topic,
            blocks: blocksWithOrder
          })
        });
        
        if (!response.ok) {
          throw new Error(`Ошибка HTTP: ${response.status}`);
        }
        
        const result = await response.json();
        this.showAlert('success', 'Контент успешно отправлен.');
      } catch (error) {
        console.error('Ошибка при отправке:', error);
        this.showAlert('error', 'Произошла ошибка при отправке контента.');
      }
    },

    // --- Block management ---
    dragStart() {
      this.isDragging = true;
    },
    
    dragEnd() {
      this.isDragging = false;
      this.$emit('update:content', JSON.parse(JSON.stringify(this.contentBlocks)));
    },

    /**
     * Prepare blocks from input data
     */
    prepareBlocks(blocks) {
      return Array.isArray(blocks)
        ? blocks
            .filter(block => block && block.component && this.blockComponents[block.component] && typeof block.data === 'object')
            .map(block => ({
              component: block.component,
              data: JSON.parse(JSON.stringify(block.data)),
              id: block.id || generateId()
            }))
        : [];
    },

    /**
     * Add a new block at the specified position
     */
    addBlock(blockData, index) {
      // Deactivate all blocks before adding a new one
      this.deactivateAllBlocks();
      
      const newBlock = {
        ...blockData,
        id: blockData.id || generateId()
      };
      
      const currentBlocks = [...this.contentBlocks];
      currentBlocks.splice(index, 0, newBlock);
      this.contentBlocks = currentBlocks;
      
      // Set the new block as active
      nextTick(() => {
        this.activeBlockIndex = index;
      });
    },

    /**
     * Deactivate all blocks (switch to view mode)
     */
    deactivateAllBlocks() {
      if (this.activeBlockIndex >= 0) {
        // Call exitEditMode on the active block if available
        const activeComponent = this.blockRefs[this.activeBlockIndex];
        if (activeComponent && typeof activeComponent.exitEditMode === 'function') {
          activeComponent.exitEditMode();
        }
        this.activeBlockIndex = -1;
      }
    },

    addFormulaBlock(index) {
      this.addBlock({
        component: 'formula-block',
        data: { latex: '', displayMode: true }
      }, index);
      this.clearEditorInput();
      nextTick(() => {
        this.focusBlock(index);
      });
    },

    addCodeBlock(index) {
      this.addBlock({
        component: 'code-block',
        data: {
          code: '',
          language: 'javascript',
        }
      }, index);
      this.clearEditorInput();
      nextTick(() => {
        this.focusBlock(index);
      });
    },

    addTextBlock(htmlContent, index) {
      this.addBlock({ component: 'text-block', data: { text: htmlContent } }, index);
      nextTick(() => { this.focusBlock(index); });
    },

    addLinkBlock(index) {
      this.addBlock({ component: 'link-block', data: { url: '', title: '', description: '', favicon: '' } }, index);
      this.clearEditorInput();
      nextTick(() => { this.focusBlock(index); });
    },

    addLinkBlockWithUrl(url, index) {
      this.addBlock({ component: 'link-block', data: { url: url, title: '', description: '', favicon: '' } }, index);
      this.clearEditorInput();
      nextTick(() => { this.scrollToInput(); });
    },

    addImageBlockFromUrl(imageUrl, index) {
      this.addBlock({
        component: 'image-block',
        data: { src: imageUrl, caption: 'Image from URL', alt: 'Image from URL' }
      }, index);
      this.clearEditorInput();
      this.scrollToInput();
    },

    addEmptyTextBlockAndFocus(index) {
      this.addBlock({ component: 'text-block', data: { text: '' } }, index);
      this.clearEditorInput();
      nextTick(() => { this.focusBlock(index); });
    },

    /**
     * Insert new empty text block after specified index
     */
    insertNewBlockAfter(currentIndex) {
      this.addEmptyTextBlockAndFocus(currentIndex + 1);
    },

    /**
     * Handle block data update from child component
     */
    handleBlockUpdate({ index, newData }) {
      if (index >= 0 && index < this.contentBlocks.length) {
        const block = this.contentBlocks[index];
        const updatedData = { ...block.data, ...newData };
        
        // Avoid unnecessary updates
        if (JSON.stringify(block.data) !== JSON.stringify(updatedData)) {
          const updatedBlock = { ...block, data: updatedData };
          const newBlocks = [...this.contentBlocks];
          newBlocks[index] = updatedBlock;
          this.contentBlocks = newBlocks;
        }
      }
    },

    /**
     * Delete block at index
     */
    deleteBlock(index) {
      if (index >= 0 && index < this.contentBlocks.length) {
        // Adjust activeBlockIndex if needed
        if (this.activeBlockIndex === index) {
          this.activeBlockIndex = -1;
        } else if (this.activeBlockIndex > index) {
          this.activeBlockIndex--;
        }
        
        this.contentBlocks = this.contentBlocks.filter((_, i) => i !== index);

        nextTick(() => {
          if (this.contentBlocks.length === 0) {
            this.$refs.editorInputRef?.focus();
          } else {
            const focusIndex = Math.max(0, index - 1);
            this.focusBlock(focusIndex);
          }
          this.scrollToInput();
        });
      }
    },

    /**
     * Convert a block to image block
     */
    handleConvertToImage({ index, imageUrl, caption, alt }) {
      if (index >= 0 && index < this.contentBlocks.length) {
        const imageBlock = {
          component: 'image-block',
          id: generateId(),
          data: { src: imageUrl, caption: caption || 'Image from URL', alt: alt || 'Image from URL' }
        };
        const newBlocks = [...this.contentBlocks];
        newBlocks[index] = imageBlock;
        this.contentBlocks = newBlocks;
        this.scrollToInput();
      }
    },

    // --- Input handling from EditorInput ---

    /**
     * Handle command typed (e.g., /code)
     */
    handleCommandTyped(commandType) {
      const action = this.commandActions[commandType];
      if (action) {
        action();
      }
    },

    /**
     * Process pasted or entered content
     */
    processPastedOrEnteredContent(htmlContent) {
      const tempDiv = document.createElement('div');
      tempDiv.innerHTML = htmlContent;
      const textContent = tempDiv.textContent || tempDiv.innerText || "";
      const trimmedText = textContent.trim();

      if (!trimmedText) {
        this.clearEditorInput();
        return;
      }

      if (isValidHttpUrl(trimmedText)) {
        if (isImageUrl(trimmedText)) {
          this.addImageBlockFromUrl(trimmedText, this.contentBlocks.length);
        } else {
          this.addLinkBlockWithUrl(trimmedText, this.contentBlocks.length);
        }
      } else {
        this.addTextBlock(htmlContent, this.contentBlocks.length);
      }
    },

    // --- File uploads ---

    handleImageUpload(event) {
      const file = event.target.files?.[0];
      if (!file || !file.type.startsWith('image/')) return;
      
      // Deactivate all blocks before adding new one
      this.deactivateAllBlocks();
      
      const reader = new FileReader();
      reader.onload = (e) => {
        this.addBlock({
          component: 'image-block',
          data: { src: e.target.result, caption: file.name, alt: file.name }
        }, this.contentBlocks.length);
        this.scrollToInput();
      };
      reader.readAsDataURL(file);
      event.target.value = '';
      this.clearEditorInput();
    },

    handleFileUpload(event) {
      const file = event.target.files?.[0];
      if (!file) return;
      
      // Deactivate all blocks before adding new one
      this.deactivateAllBlocks();
      
      this.addBlock({
        component: 'file-block',
        data: { name: file.name, size: file.size, fileUrl: URL.createObjectURL(file) }
      }, this.contentBlocks.length);
      event.target.value = '';
      this.scrollToInput();
      this.clearEditorInput();
    },

    // --- Focus and scrolling ---

    /**
     * Handle focus event from child block
     */
    handleBlockFocus(index) {
      // Deactivate previous active block if different
      if (this.activeBlockIndex !== -1 && this.activeBlockIndex !== index) {
        const prevComponent = this.blockRefs[this.activeBlockIndex];
        if (prevComponent && typeof prevComponent.exitEditMode === 'function') {
          prevComponent.exitEditMode();
        }
      }
      
      this.activeBlockIndex = index;
    },

    /**
     * Focus on specified block
     */
    focusBlock(index) {
      nextTick(() => {
        const componentRef = this.blockRefs[index];

        if (componentRef) {
          if (typeof componentRef.focusEditor === 'function') {
            componentRef.focusEditor();
            this.activeBlockIndex = index;
          }
          else if (componentRef.$el && typeof componentRef.$el.focus === 'function') {
            componentRef.$el.focus({ preventScroll: false });
            componentRef.$el.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
            this.activeBlockIndex = index;
          }
          else {
            this.scrollToInput();
            this.activeBlockIndex = -1;
          }
        } else {
          this.scrollToInput();
          this.activeBlockIndex = -1;
        }
      });
    },

    /**
     * Scroll to EditorInput component
     */
    scrollToInput() {
      nextTick(() => {
        this.$refs.editorInputRef?.scrollToView();
      });
    },

    /**
     * Clear EditorInput
     */
    clearEditorInput() {
      this.$refs.editorInputRef?.clearInput();
    },
  }
};
</script>

<style scoped>
.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.2s ease;
}
.dropdown-enter-from,
.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>
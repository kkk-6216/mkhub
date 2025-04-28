<template>
  <div class="relative w-full">
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
              :ref="(el) => { if (el) blockRefs[index] = el; }"
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
      @direct-input="handleDirectInput"
      @text-entered="handleTextEntered"
      @clear-requested="clearEditorInput"
    />
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
import { useCommands } from '@/modules/content/ContentEditor/components/useCommands';
import { nextTick } from 'vue';
import draggable from 'vuedraggable';

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
    draggable
  },
  props: {
    initialContent: { type: Array, default: () => [] }
  },
  emits: ['update:content'],
  data() {
    return {
      contentBlocks: [],
      blockRefs: {},
      commands: {},
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
      hoveredBlockIndex: -1
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
          console.log("ContentEditor: Updating contentBlocks from initialContent prop");
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
          console.log("ContentEditor: contentBlocks changed, emitting update:content");
          this.$emit('update:content', JSON.parse(JSON.stringify(newBlocks)));
        }
      },
      deep: true
    }
  },
  created() {
    const { commands } = useCommands();
    this.commands = commands;
    console.log("ContentEditor: Component created");
  },
  mounted() {
    this.isMounted = true;
    console.log("ContentEditor: Component mounted");
    if (this.contentBlocks.length === 0) {
      this.$refs.editorInputRef?.focus();
    }
  },
  beforeUpdate() {
    this.blockRefs = {};
  },
  methods: {
    // --- Block management ---
    dragStart() {
      this.isDragging = true;
      console.log("ContentEditor: Started block dragging");
    },

    dragEnd() {
      this.isDragging = false;
      console.log("ContentEditor: Finished block dragging");
      this.$emit('update:content', JSON.parse(JSON.stringify(this.contentBlocks)));
    },

    /**
     * Prepare blocks from input data
     */
    prepareBlocks(blocks) {
      console.log("ContentEditor: prepareBlocks called");
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
      console.log(`ContentEditor: Adding ${newBlock.component} block at index ${index} with ID ${newBlock.id}`);

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
      console.log(`ContentEditor: Adding formula block at index ${index}`);
      this.addBlock({
        component: 'formula-block',
        data: { latex: '', displayMode: true }
      }, index);
      this.$refs.editorInputRef?.clearInput();
      nextTick(() => {
        this.focusBlock(index);
      });
    },

    addCodeBlock(index) {
      console.log(`ContentEditor: Adding code block at index ${index}`);
      this.addBlock({
        component: 'code-block',
        data: {
          code: '',
          language: 'javascript',
        }
      }, index);
      this.$refs.editorInputRef?.clearInput();
      nextTick(() => {
        this.focusBlock(index);
      });
    },

    addTextBlock(htmlContent, index) {
      console.log(`ContentEditor: Adding text block at index ${index}`);
      this.addBlock({ component: 'text-block', data: { text: htmlContent } }, index);
      nextTick(() => { this.focusBlock(index); });
    },

    addLinkBlock(index) {
      console.log(`ContentEditor: Adding empty link block at index ${index}`);
      this.addBlock({ component: 'link-block', data: { url: '', title: '', description: '', favicon: '' } }, index);
      this.$refs.editorInputRef?.clearInput();
      nextTick(() => { this.focusBlock(index); });
    },

    addLinkBlockWithUrl(url, index) {
      console.log(`ContentEditor: Adding link block with URL ${url} at index ${index}`);
      this.addBlock({ component: 'link-block', data: { url: url, title: '', description: '', favicon: '' } }, index);
      this.$refs.editorInputRef?.clearInput();
      nextTick(() => { this.scrollToInput(); });
    },

    addImageBlockFromUrl(imageUrl, index) {
      console.log(`ContentEditor: Adding image block from URL ${imageUrl} at index ${index}`);
      this.addBlock({
        component: 'image-block',
        data: { src: imageUrl, caption: 'Image from URL', alt: 'Image from URL' }
      }, index);
      this.$refs.editorInputRef?.clearInput();
      this.scrollToInput();
    },

    addEmptyTextBlockAndFocus(index) {
      console.log(`ContentEditor: Adding empty text block at index ${index} and focusing`);
      this.addBlock({ component: 'text-block', data: { text: '' } }, index);
      this.$refs.editorInputRef?.clearInput();
      nextTick(() => { this.focusBlock(index); });
    },

    /**
     * Insert new empty text block after specified index
     */
    insertNewBlockAfter(currentIndex) {
      console.log(`ContentEditor: Inserting new block after index ${currentIndex}`);
      this.addEmptyTextBlockAndFocus(currentIndex + 1);
    },

    /**
     * Handle block data update from child component
     */
    handleBlockUpdate({ index, newData }) {
      if (index >= 0 && index < this.contentBlocks.length) {
        const block = this.contentBlocks[index];
        const currentDataString = JSON.stringify(block.data);
        const potentialNewData = { ...block.data, ...newData };
        const potentialNewDataString = JSON.stringify(potentialNewData);

        if (currentDataString !== potentialNewDataString) {
          console.log(`ContentEditor: Updating ${block.component} block at index ${index} with data:`, newData);
          const updatedBlock = { ...block, data: potentialNewData };
          const newBlocks = [...this.contentBlocks];
          newBlocks[index] = updatedBlock;
          this.contentBlocks = newBlocks;
        }
      } else {
        console.error(`ContentEditor: Invalid index ${index} for block update.`);
      }
    },

    /**
     * Delete block at index
     */
    deleteBlock(index) {
      if (index >= 0 && index < this.contentBlocks.length) {
        const deletedBlockType = this.contentBlocks[index]?.component || 'unknown';
        console.log(`ContentEditor: Deleting ${deletedBlockType} block at index ${index}`);

        // If we're deleting the active block, reset activeBlockIndex
        if (this.activeBlockIndex === index) {
          this.activeBlockIndex = -1;
        } else if (this.activeBlockIndex > index) {
          // Adjust activeBlockIndex if we're deleting a block before it
          this.activeBlockIndex--;
        }

        this.contentBlocks = this.contentBlocks.filter((_, i) => i !== index);

        nextTick(() => {
          if (this.contentBlocks.length === 0) {
            console.log("ContentEditor: No blocks left, focusing on EditorInput.");
            this.$refs.editorInputRef?.focus();
          } else {
            const focusIndex = Math.max(0, index - 1);
            console.log(`ContentEditor: Attempting to focus on block ${focusIndex} after deletion.`);
            this.focusBlock(focusIndex);
          }
          this.scrollToInput();
        });
      } else {
        console.error(`ContentEditor: Invalid index ${index} for block deletion.`);
      }
    },

    /**
     * Convert a block to image block
     */
    handleConvertToImage({ index, imageUrl, caption, alt }) {
      if (index >= 0 && index < this.contentBlocks.length) {
        console.log(`ContentEditor: Converting block ${index} to image, URL: ${imageUrl}`);
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
        console.log(`ContentEditor: Executing command /${commandType}`);
        action();
      } else {
        console.warn(`ContentEditor: Unknown command: /${commandType}`);
      }
    },

    handleDirectInput(htmlContent) {
      console.log("ContentEditor: handleDirectInput called");
      this.processPastedOrEnteredContent(htmlContent);
    },

    handleTextEntered(htmlContent) {
      console.log("ContentEditor: handleTextEntered called");
      this.processPastedOrEnteredContent(htmlContent);
    },

    /**
     * Process pasted or entered content
     */
    processPastedOrEnteredContent(htmlContent) {
      const tempDiv = document.createElement('div');
      tempDiv.innerHTML = htmlContent;
      const textContent = tempDiv.textContent || tempDiv.innerText || "";
      const trimmedText = textContent.trim();

      console.log("ContentEditor: Processing content. Text:", trimmedText);

      if (!trimmedText) {
        console.log("ContentEditor: Processed content is empty, ignoring.");
        this.$refs.editorInputRef?.clearInput();
        return;
      }

      if (isValidHttpUrl(trimmedText)) {
        console.log("ContentEditor: Valid URL detected:", trimmedText);
        if (isImageUrl(trimmedText)) {
          console.log("ContentEditor: Adding image block from URL.");
          this.addImageBlockFromUrl(trimmedText, this.contentBlocks.length);
        } else {
          console.log("ContentEditor: Adding link block from URL.");
          this.addLinkBlockWithUrl(trimmedText, this.contentBlocks.length);
        }
      } else {
        console.log("ContentEditor: Adding text block.");
        this.addTextBlock(htmlContent, this.contentBlocks.length);
      }
    },

    // --- File uploads ---

    handleImageUpload(event) {
      const file = event.target.files?.[0];
      if (!file || !file.type.startsWith('image/')) return;
      console.log("ContentEditor: Processing image upload:", file.name);

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
      this.$refs.editorInputRef?.clearInput();
    },

    handleFileUpload(event) {
      const file = event.target.files?.[0];
      if (!file) return;
      console.log("ContentEditor: Processing file upload:", file.name);

      // Deactivate all blocks before adding new one
      this.deactivateAllBlocks();

      this.addBlock({
        component: 'file-block',
        data: { name: file.name, size: file.size, fileUrl: URL.createObjectURL(file) }
      }, this.contentBlocks.length);
      event.target.value = '';
      this.scrollToInput();
      this.$refs.editorInputRef?.clearInput();
    },

    // --- Focus and scrolling ---

    /**
     * Handle focus event from child block
     */
    handleBlockFocus(index) {
      console.log(`ContentEditor: Block ${index} received focus`);

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
        console.log(`ContentEditor: Attempting to focus on block ${index}. Ref found:`, !!componentRef);

        if (componentRef) {
          if (typeof componentRef.focusEditor === 'function') {
            console.log(`ContentEditor: Calling focusEditor for component ${index}`);
            componentRef.focusEditor();
            this.activeBlockIndex = index;
          }
          else if (componentRef.$el && typeof componentRef.$el.focus === 'function') {
            console.warn(`ContentEditor: Block ${index} (${componentRef.$options.name}) has no focusEditor method. Focusing on root $el.`);
            componentRef.$el.focus({ preventScroll: false });
            componentRef.$el.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
            this.activeBlockIndex = index;
          }
          else {
            console.warn(`ContentEditor: Unable to programmatically focus on block ${index} (no focusEditor or focusable $el).`);
            this.scrollToInput();
            this.activeBlockIndex = -1;
          }
        } else {
          console.warn(`ContentEditor: No component ref found for index ${index}. Cannot focus.`);
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
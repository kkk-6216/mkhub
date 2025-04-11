<template>
  <div class="relative w-full p-5 mt-10">
    <!-- Скрытые input для загрузки файлов -->
    <input type="file" ref="imageInputRef" @change="handleImageUpload" accept="image/*" class="hidden" />
    <input type="file" ref="fileInputRef" @change="handleFileUpload" class="hidden" />

    <!-- Блоки контента -->
    <div class="content-blocks mb-4">
      <component
        v-for="(block, index) in contentBlocks"
        :key="block.id"
        :is="blockComponents[block.component]"
        :ref="(el) => { if (el) blockRefs[index] = el; }"
        :data="block.data"
        @delete="deleteBlock(index)"
        @update="handleBlockUpdate"
        @request-new-block-after="insertNewBlockAfter"
        :index="index"
      />
    </div>

    <!-- Область ввода текста и команд -->
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
// Remove the TextBlock import if it exists
// import TextBlock from '@/modules/content/components/blocks/TextBlock.vue';
import MarkdownEditor from '@/modules/content/components/blocks/MarkdownEditor.vue'; // <--- Import the Markdown Editor
import ImageBlock from '@/modules/content/components/blocks/ImageBlock.vue';
import FileBlock from '@/modules/content/components/blocks/FileBlock.vue';
import EditorInput from '@/modules/content/components/com/EditorInput.vue';
import { useCommands } from '@/modules/content/components/com/useCommands';

export default {
  name: "ContentEditor",
  components: {
    // TextBlock, // Remove or comment out
    MarkdownEditor, // <--- Add MarkdownEditor
    ImageBlock,
    FileBlock,
    EditorInput
  },
  props: {
    initialContent: { type: Array, default: () => [] }
  },
  emits: ['update:content'],
  setup() {
    const { commands } = useCommands();
    return { commands };
  },
  data() {
    return {
      contentBlocks: [],
      blockRefs: {},
      blockComponents: {
        // 'text-block': TextBlock, // Remove or comment out old mapping
        'text-block': MarkdownEditor, // <--- Map 'text-block' to MarkdownEditor
        'image-block': ImageBlock,
        'file-block': FileBlock
        // Add other block types if needed
      }
    };
  },
  computed: {
    commandActions() {
      // Command actions remain the same, they add a 'text-block' component type
      return {
        text: () => this.addEmptyTextBlockAndFocus(this.contentBlocks.length),
        image: () => this.$refs.imageInputRef?.click(),
        file: () => this.$refs.fileInputRef?.click()
        // Add other commands...
      };
    }
  },
  watch: {
    initialContent: {
      handler(newContent) {
        // Ensure initial content doesn't cause infinite loops if emitted back immediately
        const currentContentString = JSON.stringify(this.contentBlocks);
        const newContentString = JSON.stringify(newContent || []);
        if (newContentString !== currentContentString) {
            console.log("ContentEditor: Updating contentBlocks from initialContent prop");
            this.contentBlocks = this.prepareBlocks(newContent || []);
        }
      },
      immediate: true,
      deep: true
    },
    contentBlocks: {
      handler(newBlocks) {
        // console.log("ContentEditor: Emitting update:content");
        this.$emit('update:content', newBlocks);
      },
      deep: true
    }
  },
  beforeUpdate() {
    // Reset refs before each update cycle
    this.blockRefs = {};
  },
  methods: {
    prepareBlocks(blocks) {
      return Array.isArray(blocks)
        ? blocks.map(block => ({ ...block, id: block.id || Math.random().toString(36).substring(2, 9) }))
        : [];
    },

    handleCommandTyped(commandType) {
      const action = this.commandActions[commandType];
      if (action) {
        action();
        // Clear input only if a command was successfully executed
        this.$refs.editorInputRef.clearInput();
      }
      // Optional: handle unknown commands or provide feedback
    },

    handleDirectInput(content) {
      if (content.trim()) {
        // Treat direct input as adding a new markdown block
        this.addTextBlock(content, this.contentBlocks.length);
        this.$refs.editorInputRef.clearInput();
        this.scrollToInput();
      }
    },

    handleTextEntered(htmlContent) {
        // Treat text entered (likely from paste or complex input) as adding a new markdown block
        // We might need to convert HTML back to Markdown here, or just store the HTML
        // For simplicity with MarkdownEditor, let's treat it as raw text for now
        // A proper HTML-to-Markdown converter would be better.
        // Let's assume htmlContent IS the markdown text for now.
      this.addTextBlock(htmlContent, this.contentBlocks.length);
      this.$refs.editorInputRef.clearInput();
      this.scrollToInput();
    },

    // This method now adds a block that will be rendered using MarkdownEditor
    addTextBlock(markdownContent, index) {
      console.log(`ContentEditor: Adding text block at index ${index}`);
      const newBlock = {
        component: 'text-block', // This key now maps to MarkdownEditor
        id: Math.random().toString(36).substring(2, 9),
        data: { text: markdownContent } // MarkdownEditor expects data.text
      };
      this.contentBlocks.splice(index, 0, newBlock);
      // Focus logic might need adjustment depending on async rendering
       this.$nextTick(() => {
          if(index === this.contentBlocks.length - 1) { // If added at the end
              this.focusBlock(index);
          }
       });
    },

    addEmptyTextBlockAndFocus(index) {
      console.log(`ContentEditor: Adding empty text block at index ${index}`);
      this.addTextBlock('', index); // Add block with empty text
        this.$nextTick(() => {
            this.focusBlock(index); // Focus the newly added block
            this.scrollToInput(); // Also ensure input is visible
       });
    },

    insertNewBlockAfter(currentIndex) {
        console.log(`ContentEditor: Inserting new block after index ${currentIndex}`);
      // This is typically requested *from* a block, so we add an empty one after it
      this.addEmptyTextBlockAndFocus(currentIndex + 1);
    },

    handleBlockUpdate({ index, newData }) {
      const block = this.contentBlocks[index];
       if (block && block.component === 'text-block') { // Ensure it's the right type
            console.log(`ContentEditor: Updating block at index ${index} with new text data.`);
             // Create a new object to ensure reactivity
            const updatedBlock = {
              ...block,
              // Merge existing data with new data (important if data has more than just 'text')
              data: { ...block.data, ...newData }
            };
            // Avoid direct mutation
            const newBlocks = [...this.contentBlocks];
            newBlocks[index] = updatedBlock;
            this.contentBlocks = newBlocks;
            // Do NOT emit 'update:content' here, the watcher on contentBlocks handles it.
        } else if (block) {
            // Handle updates for other block types if necessary
             console.log(`ContentEditor: Updating block at index ${index} (type: ${block.component}).`);
             const updatedBlock = { ...block, data: { ...block.data, ...newData } };
             const newBlocks = [...this.contentBlocks];
             newBlocks[index] = updatedBlock;
             this.contentBlocks = newBlocks;
        }
    },

    deleteBlock(index) {
      if (index >= 0 && index < this.contentBlocks.length) {
        console.log(`ContentEditor: Deleting block at index ${index}`);
        // Create a new array instead of mutating
        const newBlocks = this.contentBlocks.filter((_, i) => i !== index);
        this.contentBlocks = newBlocks; // This triggers the watcher to emit update

        // Focus logic after deletion
        if (this.contentBlocks.length === 0) {
          // If no blocks left, focus the main input
          this.$nextTick(() => this.$refs.editorInputRef.focus());
        } else {
          // Focus the previous block, or the first block if index 0 was deleted
          const focusIndex = Math.max(0, index - 1);
          this.$nextTick(() => this.focusBlock(focusIndex));
        }
         this.scrollToInput(); // Ensure input area is visible after potential layout shifts
      }
    },

    handleImageUpload(event) {
      const file = event.target.files?.[0];
      if (!file || !file.type.startsWith('image/')) return;
      console.log("ContentEditor: Handling image upload");
      const reader = new FileReader();
      reader.onload = (e) => {
        // Use spread syntax for adding to array to ensure reactivity
        this.contentBlocks = [...this.contentBlocks, {
          component: 'image-block',
          id: Math.random().toString(36).substring(2, 9),
          data: { src: e.target.result, caption: file.name, alt: file.name }
        }];
        this.scrollToInput();
      };
      reader.readAsDataURL(file);
      event.target.value = ''; // Clear input for subsequent uploads
    },

    handleFileUpload(event) {
      const file = event.target.files?.[0];
      if (!file) return;
      console.log("ContentEditor: Handling file upload");
      // Use spread syntax for adding to array
      this.contentBlocks = [...this.contentBlocks, {
        component: 'file-block',
        id: Math.random().toString(36).substring(2, 9),
        data: {
            name: file.name,
            size: file.size,
            // Generate blob URL. Remember this URL is temporary and tied to the document session.
            // For persistent storage, you'd upload the file and store a permanent URL.
            fileUrl: URL.createObjectURL(file),
            // Store the actual file object temporarily if needed for upload later? (Optional)
            // fileObject: file
        }
      }];
      this.scrollToInput();
      event.target.value = ''; // Clear input
    },

    clearEditorInput() {
      this.$refs.editorInputRef?.clearInput();
    },

    focusBlock(index) {
      this.$nextTick(() => {
        const componentRef = this.blockRefs[index];
        // console.log(`ContentEditor: Attempting to focus block at index ${index}`, componentRef);
        if (componentRef && typeof componentRef.focusEditor === 'function') {
           console.log(`ContentEditor: Calling focusEditor on componentRef for index ${index}`);
          componentRef.focusEditor();
        } else if (componentRef && componentRef.$el && typeof componentRef.$el.focus === 'function') {
           console.log(`ContentEditor: Calling focus on componentRef.$el for index ${index}`);
          componentRef.$el.focus();
        } else {
           console.log(`ContentEditor: No focus method found for block ${index}, focusing EditorInput.`);
          this.$refs.editorInputRef?.focus();
        }
      });
    },

    scrollToInput() {
      this.$nextTick(() => {
        this.$refs.editorInputRef?.scrollToView();
      });
    }
  }
};
</script>
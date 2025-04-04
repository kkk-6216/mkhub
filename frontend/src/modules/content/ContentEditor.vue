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
import TextBlock from '@/modules/content/components/blocks/TextBlock.vue';
import ImageBlock from '@/modules/content/components/blocks/ImageBlock.vue';
import FileBlock from '@/modules/content/components/blocks/FileBlock.vue';
import EditorInput from '@/modules/content/components/com/EditorInput.vue';
import { useCommands } from '@/modules/content/components/com/useCommands';

export default {
  name: "ContentEditor",
  components: {
    TextBlock,
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
        'text-block': TextBlock,
        'image-block': ImageBlock,
        'file-block': FileBlock
      }
    };
  },
  computed: {
    commandActions() {
      return {
        text: () => this.addEmptyTextBlockAndFocus(this.contentBlocks.length),
        image: () => this.$refs.imageInputRef?.click(),
        file: () => this.$refs.fileInputRef?.click()
      };
    }
  },
  watch: {
    initialContent: {
      handler(newContent) {
        if (JSON.stringify(newContent ?? []) !== JSON.stringify(this.contentBlocks)) {
          this.contentBlocks = this.prepareBlocks(newContent || []);
        }
      },
      immediate: true,
      deep: true
    },
    contentBlocks: {
      handler(newBlocks) {
        this.$emit('update:content', newBlocks);
      },
      deep: true
    }
  },
  beforeUpdate() {
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
        this.$refs.editorInputRef.clearInput();
      }
    },
    
    handleDirectInput(content) {
      if (content.trim()) {
        this.addTextBlock(content, this.contentBlocks.length);
        this.$refs.editorInputRef.clearInput();
        this.scrollToInput();
      }
    },
    
    handleTextEntered(htmlContent) {
      this.addTextBlock(htmlContent, this.contentBlocks.length);
      this.$refs.editorInputRef.clearInput();
      this.scrollToInput();
    },

    addTextBlock(htmlContent, index) {
      const newBlock = {
        component: 'text-block',
        id: Math.random().toString(36).substring(2, 9),
        data: { text: htmlContent }
      };
      this.contentBlocks.splice(index, 0, newBlock);
    },

    addEmptyTextBlockAndFocus(index) {
      this.addTextBlock('', index);
      this.scrollToInput();
    },

    insertNewBlockAfter(currentIndex) {
      this.addEmptyTextBlockAndFocus(currentIndex + 1);
    },

    handleBlockUpdate({ index, newData }) {
      const block = this.contentBlocks[index];
      if (block) {
        const updatedBlock = {
          ...block,
          data: { ...block.data, ...newData }
        };
        this.contentBlocks.splice(index, 1, updatedBlock);
      }
    },

    deleteBlock(index) {
      if (index >= 0 && index < this.contentBlocks.length) {
        this.contentBlocks.splice(index, 1);
        const focusIndex = Math.max(0, index - 1);
        if (this.contentBlocks.length > 0) {
             this.$nextTick(() => this.focusBlock(focusIndex));
        } else {
            this.$nextTick(() => this.$refs.editorInputRef.focus());
        }
        this.scrollToInput();
      }
    },

    handleImageUpload(event) {
      const file = event.target.files?.[0];
      if (!file || !file.type.startsWith('image/')) return;
      const reader = new FileReader();
      reader.onload = (e) => {
        this.contentBlocks.push({
          component: 'image-block',
          id: Math.random().toString(36).substring(2, 9),
          data: { src: e.target.result, caption: file.name, alt: file.name }
        });
        this.scrollToInput();
      };
      reader.readAsDataURL(file);
      event.target.value = '';
    },

    handleFileUpload(event) {
      const file = event.target.files?.[0];
      if (!file) return;
      this.contentBlocks.push({
        component: 'file-block',
        id: Math.random().toString(36).substring(2, 9),
        data: {
            name: file.name,
            size: file.size,
            fileUrl: URL.createObjectURL(file)
        }
      });
      this.scrollToInput();
      event.target.value = '';
    },

    clearEditorInput() {
      this.$refs.editorInputRef?.clearInput();
    },

    focusBlock(index) {
      this.$nextTick(() => {
        const componentRef = this.blockRefs[index];
        if (componentRef && typeof componentRef.focusEditor === 'function') {
          componentRef.focusEditor();
        } else if (componentRef && componentRef.$el && typeof componentRef.$el.focus === 'function') {
          componentRef.$el.focus();
        } else {
          this.$refs.editorInputRef?.focus();
        }
      });
    },

    scrollToInput() {
      this.$nextTick(() => {
        this.$refs.editorInputRef.scrollToView();
      });
    }
  }
};
</script>
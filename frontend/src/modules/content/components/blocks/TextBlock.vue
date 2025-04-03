<template>
  <div
    ref="wrapperRef"
    class="relative rounded-md p-4 hover:bg-gray-50 group"
    @focusin="isFocused = true"
    @focusout="handleFocusOut"
  >
    <!-- Панель инструментов (позиционирование относительно курсора) -->
    <div
      v-if="isFocused && editor"
      ref="toolbarRef"
      class="fixed bg-white shadow-md rounded-md flex space-x-2 items-center z-10 p-2"
      :style="toolbarPosition"
      @mousedown.prevent
    >
      <button
        class="p-1 rounded hover:bg-gray-100"
        :class="{ 'bg-gray-200': editor.isActive('bold') }"
        @click="editor.chain().focus().toggleBold().run()"
        :disabled="!editor.can().chain().focus().toggleBold().run()"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5">
          <path d="M6 4h8a4 4 0 0 1 4 4 4 4 0 0 1-4 4H6z"></path>
          <path d="M6 12h9a4 4 0 0 1 4 4 4 4 0 0 1-4 4H6z"></path>
        </svg>
      </button>

      <button
        class="p-1 rounded hover:bg-gray-100"
        :class="{ 'bg-gray-200': editor.isActive('italic') }"
        @click="editor.chain().focus().toggleItalic().run()"
        :disabled="!editor.can().chain().focus().toggleItalic().run()"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5">
          <line x1="19" y1="4" x2="10" y2="4"></line>
          <line x1="14" y1="20" x2="5" y2="20"></line>
          <line x1="15" y1="4" x2="9" y2="20"></line>
        </svg>
      </button>

      <button
        class="p-1 rounded hover:bg-gray-100"
        :class="{ 'bg-gray-200': editor.isActive('underline') }"
        @click="editor.chain().focus().toggleUnderline().run()"
        :disabled="!editor.can().chain().focus().toggleUnderline().run()"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5">
          <path d="M6 3v7a6 6 0 0 0 6 6 6 6 0 0 0 6-6V3"></path>
          <line x1="4" y1="21" x2="20" y2="21"></line>
        </svg>
      </button>

      <div class="border-r border-gray-300 h-6 mx-1"></div>

      <button
        class="p-1 rounded hover:bg-gray-100"
        :class="{ 'bg-gray-200': editor.isActive('heading', { level: 1 }) }"
        @click="editor.chain().focus().toggleHeading({ level: 1 }).run()"
      >
        <span class="font-bold">H1</span>
      </button>

      <button
        class="p-1 rounded hover:bg-gray-100"
        :class="{ 'bg-gray-200': editor.isActive('heading', { level: 2 }) }"
        @click="editor.chain().focus().toggleHeading({ level: 2 }).run()"
      >
        <span class="font-bold">H2</span>
      </button>

      <button
        class="p-1 rounded hover:bg-gray-100"
        :class="{ 'bg-gray-200': editor.isActive('heading', { level: 3 }) }"
        @click="editor.chain().focus().toggleHeading({ level: 3 }).run()"
      >
        <span class="font-bold">H3</span>
      </button>

      <div class="border-r border-gray-300 h-6 mx-1"></div>

      <button
        class="p-1 rounded hover:bg-gray-100"
        :class="{ 'bg-gray-200': editor.isActive({ textAlign: 'left' }) }"
        @click="editor.chain().focus().setTextAlign('left').run()"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5">
          <line x1="17" y1="10" x2="3" y2="10"></line>
          <line x1="21" y1="6" x2="3" y2="6"></line>
          <line x1="21" y1="14" x2="3" y2="14"></line>
          <line x1="17" y1="18" x2="3" y2="18"></line>
        </svg>
      </button>

      <button
        class="p-1 rounded hover:bg-gray-100"
        :class="{ 'bg-gray-200': editor.isActive({ textAlign: 'center' }) }"
        @click="editor.chain().focus().setTextAlign('center').run()"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5">
          <line x1="21" y1="10" x2="3" y2="10"></line>
          <line x1="21" y1="6" x2="3" y2="6"></line>
          <line x1="21" y1="14" x2="3" y2="14"></line>
          <line x1="21" y1="18" x2="3" y2="18"></line>
        </svg>
      </button>

      <button
        class="p-1 rounded hover:bg-gray-100"
        :class="{ 'bg-gray-200': editor.isActive({ textAlign: 'right' }) }"
        @click="editor.chain().focus().setTextAlign('right').run()"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5">
          <line x1="21" y1="10" x2="7" y2="10"></line>
          <line x1="21" y1="6" x2="3" y2="6"></line>
          <line x1="21" y1="14" x2="3" y2="14"></line>
          <line x1="21" y1="18" x2="7" y2="18"></line>
        </svg>
      </button>

      <div class="border-r border-gray-300 h-6 mx-1"></div>

      <div class="relative">
        <button
          class="p-1 rounded hover:bg-gray-100 relative"
          @click.prevent="toggleColorPalette"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5">
            <path d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z"></path>
            <path d="M8 12L16 12"></path>
            <path d="M12 8L12 16"></path>
          </svg>
          <div
            v-if="currentTextColor"
            class="absolute w-3 h-3 rounded-full"
            :style="`background-color: ${currentTextColor}; bottom: 0; right: 0;`"
          ></div>
        </button>

        <!-- Палитра цветов -->
        <div
          v-if="showColorPalette"
          ref="colorPaletteRef"
          class="absolute mt-1 p-2 bg-white shadow-md rounded-md z-20 w-max"
          @mousedown.prevent
        >
          <div class="grid grid-cols-5 gap-2">
            <button
              v-for="color in colorPalette"
              :key="color.value"
              class="w-6 h-6 rounded-full cursor-pointer"
              :style="`background-color: ${color.value};`"
              @click="setTextColor(color.value)"
            ></button>
          </div>
          <button
            class="w-full mt-2 py-1 text-sm text-center rounded-md bg-main text-white"
            @click="resetTextColor"
          >
            Сбросить
          </button>
        </div>
      </div>
    </div>

    <!-- Кнопка опций -->
    <div class="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity duration-200">
    <button
      class="text-gray-500 hover:text-gray-700 focus:outline-none p-1"
      @click.prevent="toggleOptions"
    >
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5">
        <circle cx="12" cy="12" r="1"></circle>
        <circle cx="19" cy="12" r="1"></circle>
        <circle cx="5" cy="12" r="1"></circle>
      </svg>
    </button>

    <!-- Меню опций -->
    <div
      v-if="showOptions"
      ref="optionsMenuRef"
      class="absolute right-0 top-full mt-1 bg-white border border-gray-200 rounded-md shadow-lg z-10 w-48"
      @mousedown.prevent
    >
      <button
        class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white"
        @click="copyContent"
      >
      <CopyIcon class="w-5 h-5 mr-3"/>
        Копировать
      </button>
      <button
        class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white"
        @click="downloadContent"
      >
      <DownloadIcon class="w-5 h-5 mr-3"/>
        Скачать
      </button>
      <button
        class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white"
        @click="deleteBlock"
      >
      <DeleteIcon class="w-5 h-5 mr-3"/>
        Удалить
      </button>
    </div>
    </div>

    <!-- Редактор Tiptap -->
    <editor-content
      v-if="editor"
      :editor="editor"
      class="prose max-w-none focus:outline-none"
    />
  </div>
</template>

<script>
import { Editor, EditorContent } from '@tiptap/vue-3';
import StarterKit from '@tiptap/starter-kit';
import Placeholder from '@tiptap/extension-placeholder';
import TextStyle from '@tiptap/extension-text-style';
import Color from '@tiptap/extension-color';
import TextAlign from '@tiptap/extension-text-align';
import Underline from '@tiptap/extension-underline';

import DownloadIcon from "@/modules/content/components/icons/DownloadIcon.vue";
import DeleteIcon from "@/modules/content/components/icons/DeleteIcon.vue";
import CopyIcon from "@/modules/content/components/icons/CopyIcon.vue";

export default {
  name: 'TextBlock',
  
  components: {
    EditorContent,
    CopyIcon,
    DeleteIcon,
    DownloadIcon
  },
  
  props: {
    data: {
      type: Object,
      required: true,
      default: () => ({ text: '' }),
    },
    index: {
      type: Number,
      required: true,
    },
  },
  
  data() {
    return {
      editor: null,
      isFocused: false,
      showColorPalette: false,
      showOptions: false,
      currentTextColor: null,
      cursorPosition: { x: 0, y: 0 },
      toolbarOffsetY: 10, 
      colorPalette: [
        { name: 'Red', value: '#EF4444' },
        { name: 'Pink', value: '#EC4899' },
        { name: 'Purple', value: '#8B5CF6' },
        { name: 'Blue', value: '#3B82F6' },
        { name: 'Green', value: '#10B981' },
        { name: 'Yellow', value: '#F59E0B' },
        { name: 'Orange', value: '#F97316' },
        { name: 'Brown', value: '#92400E' },
        { name: 'Gray', value: '#6B7280' },
        { name: 'Black', value: '#000000' },
        { name: 'Light Red', value: '#FEE2E2' },
        { name: 'Light Pink', value: '#FCE7F3' },
        { name: 'Light Purple', value: '#EDE9FE' },
        { name: 'Light Blue', value: '#DBEAFE' },
        { name: 'Light Green', value: '#D1FAE5' },
      ]
    };
  },
  
  computed: {
    toolbarPosition() {
    return {
      left: `${this.cursorPosition.x}px`,
      top: `${this.cursorPosition.y - this.toolbarOffsetY - (this.$refs.toolbarRef ? this.$refs.toolbarRef.offsetHeight : 0)}px`,
    };
  }
  },
  
  emits: ['update', 'delete', 'request-new-block-after'],
  
  watch: {
    'data.text': function(newText) {
      if (this.editor && !this.isFocused && this.editor.getHTML() !== newText) {
        this.editor.commands.setContent(newText);
      }
    },
    isFocused(newValue) {
      if (newValue) {
        document.addEventListener('selectionchange', this.updateCursorPosition);
        this.$nextTick(() => {
          this.updateCursorPosition();
        });
      } else {
        document.removeEventListener('selectionchange', this.updateCursorPosition);
      }
    }
  },
  
  mounted() {
    this.initEditor();
    document.addEventListener('click', this.handleClickOutside);
  },
  
  beforeDestroy() {
    if (this.editor) {
      this.editor.destroy();
    }
    document.removeEventListener('click', this.handleClickOutside);
    document.removeEventListener('selectionchange', this.updateCursorPosition);
  },
  
  methods: {
    initEditor() {
      this.editor = new Editor({
        content: this.data.text || '',
        extensions: [
          StarterKit.configure({
            heading: {
              levels: [1, 2, 3],
            },
          }),
          Placeholder.configure({
            placeholder: 'Введите текст...',
          }),
          TextStyle,
          Color,
          TextAlign.configure({
            types: ['heading', 'paragraph'],
            alignments: ['left', 'center', 'right'],
          }),
          Underline,
        ],
        onUpdate: ({ editor }) => {
          this.$emit('update', {
            index: this.index,
            newData: { text: editor.getHTML() },
          });
        },
        onSelectionUpdate: ({ editor }) => {
        
          this.updateCursorPosition();
          
          const marks = editor.view.state.selection.$head.marks();
          for (const mark of marks) {
            if (mark.type.name === 'textStyle' && mark.attrs.color) {
              this.currentTextColor = mark.attrs.color;
              return;
            }
          }
          this.currentTextColor = null;
        },
        onFocus: () => {
          this.isFocused = true;
        },
        onBlur: () => {
         
        },
        editorProps: {
          handleKeyDown: (view, event) => {
          
            if (event.key === 'Enter' && !event.shiftKey) {
              this.$emit('request-new-block-after', this.index);
              return true; 
            }
            return false; 
          },
        },
      });
    },
    
 
    updateCursorPosition() {
  if (!this.editor || !this.isFocused) return;

  const { state, view } = this.editor;
  const { from, to } = state.selection; 

  
  const startCoords = view.coordsAtPos(from);


  let targetLeft = startCoords.left;
  if (from !== to) {
      const endCoords = view.coordsAtPos(to);
      targetLeft = (startCoords.left + endCoords.left) / 2;
  }

  this.$nextTick(() => {
      const toolbarWidth = this.$refs.toolbarRef ? this.$refs.toolbarRef.offsetWidth : 0;

      this.cursorPosition = {
          x: targetLeft - (toolbarWidth / 2),
          y: startCoords.top
      };
      this.adjustToolbarPosition(); 
  });
},

toolbarPosition() {
  const toolbarHeight = this.$refs.toolbarRef ? this.$refs.toolbarRef.offsetHeight : 0;
  const finalTop = this.cursorPosition.y - this.toolbarOffsetY - toolbarHeight;

  return {
    left: `${this.cursorPosition.x}px`,
    top: `${finalTop}px`,
  };
},
    
  
    adjustToolbarPosition() {
    if (!this.$refs.toolbarRef || !this.editor?.view.dom.isConnected) return; 

    const toolbar = this.$refs.toolbarRef;
  
    requestAnimationFrame(() => {
        const toolbarRect = toolbar.getBoundingClientRect();

        let adjustedX = this.cursorPosition.x;
        let adjustedY = this.cursorPosition.y; 
        if (toolbarRect.right > window.innerWidth) {
            const overflow = toolbarRect.right - window.innerWidth;
            adjustedX = this.cursorPosition.x - overflow - 10; 
        }
        if (toolbarRect.left < 0) {
            adjustedX = this.cursorPosition.x - toolbarRect.left + 10; 
        }
        const toolbarHeight = toolbar.offsetHeight;
        let finalTop = adjustedY - this.toolbarOffsetY - toolbarHeight;
        if (finalTop < 0) {
             finalTop = adjustedY + 15;
        }
        toolbar.style.left = `${adjustedX}px`;
        toolbar.style.top = `${finalTop}px`;
   });
},
    
    handleClickOutside(event) {
      if (
        this.$refs.wrapperRef && 
        !this.$refs.wrapperRef.contains(event.target) &&
        (!this.$refs.toolbarRef || !this.$refs.toolbarRef.contains(event.target)) &&
        (!this.$refs.colorPaletteRef || !this.$refs.colorPaletteRef.contains(event.target)) &&
        (!this.$refs.optionsMenuRef || !this.$refs.optionsMenuRef.contains(event.target))
      ) {
        this.showColorPalette = false;
        this.showOptions = false;
      }
    },
    
    handleFocusOut(event) {
      setTimeout(() => {
        const isRelatedTargetInside = this.$refs.wrapperRef?.contains(event.relatedTarget) ||
          this.$refs.toolbarRef?.contains(event.relatedTarget) ||
          this.$refs.colorPaletteRef?.contains(event.relatedTarget) ||
          this.$refs.optionsMenuRef?.contains(event.relatedTarget);
          
        if (!isRelatedTargetInside) {
          this.isFocused = false;
          this.showColorPalette = false;
          this.showOptions = false;
        }
      }, 0);
    },
    
    toggleColorPalette() {
      this.showColorPalette = !this.showColorPalette;
      this.showOptions = false;
    },
    
    setTextColor(color) {
      this.editor.chain().focus().setColor(color).run();
      this.currentTextColor = color;
      this.showColorPalette = false;
    },
    
    resetTextColor() {
      this.editor.chain().focus().unsetColor().run();
      this.currentTextColor = null;
      this.showColorPalette = false;
    },
    
    toggleOptions() {
      this.showOptions = !this.showOptions;
      this.showColorPalette = false;
    },
    
    async copyContent() {
      try {
        await navigator.clipboard.writeText(this.editor.getText());
        this.showOptions = false;
      } catch (err) {
        console.error('Не удалось скопировать текст: ', err);
      }
    },
    
    downloadContent() {
      const text = this.editor.getText();
      const blob = new Blob([text], { type: 'text/plain' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `text-block-${this.index}.txt`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
      this.showOptions = false;
    },
    
    deleteBlock() {
      this.$emit('delete', this.index);
      this.showOptions = false;
    }
  }
};
</script>

<style>
.ProseMirror {
  outline: none;
}

.ProseMirror p {
  margin: 1em 0;
}

.ProseMirror h1 {
  font-size: 2em;
  font-weight: bold;
  margin: 1em 0 0.5em;
}

.ProseMirror h2 {
  font-size: 1.5em;
  font-weight: bold;
  margin: 1em 0 0.5em;
}

.ProseMirror h3 {
  font-size: 1.17em;
  font-weight: bold;
  margin: 1em 0 0.5em;
}

.ProseMirror p.is-editor-empty:first-child::before {
  content: attr(data-placeholder);
  float: left;
  color: #adb5bd;
  pointer-events: none;
  height: 0;
}


.ProseMirror .has-text-align-left {
  text-align: left;
}

.ProseMirror .has-text-align-center {
  text-align: center;
}

.ProseMirror .has-text-align-right {
  text-align: right;
}
</style>
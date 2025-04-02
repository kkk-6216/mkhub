<template>
  <div
    ref="wrapperRef"
    class="relative group mb-2 tiptap-block-wrapper"
    :class="{ 'is-focused': isFocused }"
    tabindex="-1"
    @focusin="handleFocusIn"
    @focusout="handleFocusOut"
  >
    <!-- Toolbar -->
    <div
      v-if="editor && isFocused"
      ref="toolbarRef"
      class="toolbar absolute bottom-full left-0 mb-1 flex flex-wrap items-center space-x-1 p-1 bg-white rounded shadow-md z-20 max-w-full"
      style="min-width: 300px;"
      @mousedown.prevent
    >
      <!-- Bold -->
      <button @click="editor.chain().focus().toggleBold().run()" :disabled="!editorCan('toggleBold')" :class="{ 'is-active': editorIsActive('bold') }" class="p-1 rounded hover:bg-gray-600 hover:text-white" title="Жирный">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-5 h-5"><path d="M0 0h24v24H0V0z" fill="none"/><path d="M15.6 10.79c.97-.67 1.65-1.77 1.65-2.79 0-2.26-1.75-4-4-4H7v14h7.04c2.09 0 3.71-1.7 3.71-3.79 0-1.52-.86-2.82-2.15-3.42zM10 6.5h3c.83 0 1.5.67 1.5 1.5s-.67 1.5-1.5 1.5h-3v-3zm3.5 9H10v-3h3.5c.83 0 1.5.67 1.5 1.5s-.67 1.5-1.5 1.5z"/></svg>
      </button>
      <!-- Italic -->
      <button @click="editor.chain().focus().toggleItalic().run()" :disabled="!editorCan('toggleItalic')" :class="{ 'is-active': editorIsActive('italic') }" class="p-1 rounded hover:bg-gray-600 hover:text-white" title="Курсив">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-5 h-5"><path d="M0 0h24v24H0V0z" fill="none"/><path d="M10 5.5c0 .83.67 1.5 1.5 1.5h.71l-3.42 8H7.5c-.83 0-1.5.67-1.5 1.5S6.67 18 7.5 18h5c.83 0 1.5-.67 1.5-1.5s-.67-1.5-1.5-1.5h-.71l3.42-8H16.5c.83 0 1.5-.67 1.5-1.5S17.33 4 16.5 4h-5c-.83 0-1.5.67-1.5 1.5z"/></svg>
      </button>
      <!-- Underline -->
      <button @click="editor.chain().focus().toggleUnderline().run()" :disabled="!editorCan('toggleUnderline')" :class="{ 'is-active': editorIsActive('underline') }" class="p-1 rounded hover:bg-gray-600ho ver:text-white" title="Подчеркнутый">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-5 h-5"><path d="M0 0h24v24H0V0z" fill="none"/><path d="M12 17c3.31 0 6-2.69 6-6V3h-2.5v8c0 1.93-1.57 3.5-3.5 3.5S8.5 12.93 8.5 11V3H6v8c0 3.31 2.69 6 6 6zm-7 2v2h14v-2H5z"/></svg>
      </button>
      <!-- Headings -->
      <button @click="editor.chain().focus().toggleHeading({ level: 1 }).run()" :class="{ 'is-active': editorIsActive('heading', { level: 1 }) }" class="p-1 rounded hover:bg-gray-600 hover:text-white font-bold text-base" title="H1">H1</button>
      <button @click="editor.chain().focus().toggleHeading({ level: 2 }).run()" :class="{ 'is-active': editorIsActive('heading', { level: 2 }) }" class="p-1 rounded hover:bg-gray-600 hover:text-white font-bold text-sm" title="H2">H2</button>
      <button @click="editor.chain().focus().toggleHeading({ level: 3 }).run()" :class="{ 'is-active': editorIsActive('heading', { level: 3 }) }" class="p-1 rounded hover:bg-gray-600 hover:text-white font-bold text-xs" title="H3">H3</button>
      <!-- Align Left -->
      <button @click="editor.chain().focus().setTextAlign('left').run()" :class="{ 'is-active': editorIsActive({ textAlign: 'left' }) }" class="p-1 rounded hover:bg-gray-600 hover:text-white" title="По левому краю">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-5 h-5"><path d="M0 0h24v24H0V0z" fill="none"/><path d="M15 15H3v2h12v-2zm0-8H3v2h12V7zM3 13h18v-2H3v2zm0 8h18v-2H3v2zM3 3v2h18V3H3z"/></svg>
      </button>
      <!-- Align Center -->
      <button @click="editor.chain().focus().setTextAlign('center').run()" :class="{ 'is-active': editorIsActive({ textAlign: 'center' }) }" class="p-1 rounded hover:bg-gray-600 hover:text-white" title="По центру">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-5 h-5"><path d="M0 0h24v24H0V0z" fill="none"/><path d="M7 15v2h10v-2H7zm-4 6h18v-2H3v2zm0-8h18v-2H3v2zm4-6v2h10V7H7zM3 3v2h18V3H3z"/></svg>
      </button>
      <!-- Align Right -->
      <button @click="editor.chain().focus().setTextAlign('right').run()" :class="{ 'is-active': editorIsActive({ textAlign: 'right' }) }" class="p-1 rounded hover:bg-gray-600" title="По правому краю">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-5 h-5"><path d="M0 0h24v24H0V0z" fill="none"/><path d="M3 21h18v-2H3v2zm6-4h12v-2H9v2zm-6-4h18v-2H3v2zm6-4h12V7H9v2zM3 3v2h18V3H3z"/></svg>
      </button>

      
      <!-- Color Picker Trigger -->
      <div class="relative inline-block flex-shrink-0">
    <button @click.stop="toggleColorPalette" class="p-1 rounded hover:bg-gray-600 flex items-center justify-center w-8 h-8" :class="{ 'is-active': editorIsActive('textStyle') }" title="Цвет текста">
        <!-- Иконка "А" с подчёркиванием (более подходящая для выбора цвета текста) -->
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-5 h-5">
            <path d="M12 3L4 21h2l2.5-6h7L18 21h2L12 3zm0 4.6L14.5 13h-5L12 7.6z"/>
        </svg>
    </button>
    
    <!-- Color Palette Dropdown -->
    <div v-if="showColorPalette" ref="colorPaletteRef" class="absolute left-0 mt-1 p-2 bg-white  rounded shadow-lg z-30 grid grid-cols-4 gap-1 w-max" style="top: 100%; margin-top: 4px;" @mousedown.prevent>
          <button @click="selectColor('#ec4899')" class="p-1 rounded hover:opacity-80 focus:outline-none focus:ring-1 focus:ring-white" title="Розовый"><span class="block w-4 h-4 bg-pink-500 rounded-full border border-gray-400"></span></button>
          <button @click="selectColor('#f97316')" class="p-1 rounded hover:opacity-80 focus:outline-none focus:ring-1 focus:ring-white" title="Оранжевый"><span class="block w-4 h-4 bg-orange-500 rounded-full border border-gray-400"></span></button>
          <button @click="selectColor('#854d0e')" class="p-1 rounded hover:opacity-80 focus:outline-none focus:ring-1 focus:ring-white" title="Коричневый"><span class="block w-4 h-4 rounded-full border border-gray-400" style="background-color: #854d0e;"></span></button>
          <button @click="selectColor('#000000')" :class="{ 'ring-1 ring-white': editorIsActive('textStyle', { color: '#000000' }) || editorIsActive('textStyle', { color: 'rgb(0, 0, 0)' }) }" class="p-1 rounded hover:opacity-80 focus:outline-none focus:ring-1 focus:ring-white" title="Черный"><span class="block w-4 h-4 bg-black rounded-full border border-gray-400"></span></button>
          <button @click="selectColor('#fbcfe8')" class="p-1 rounded hover:opacity-80 focus:outline-none focus:ring-1 focus:ring-white" title="Светло-розовый"><span class="block w-4 h-4 bg-pink-200 rounded-full border border-gray-400"></span></button>
          <button @click="selectColor('#a855f7')" class="p-1 rounded hover:opacity-80 focus:outline-none focus:ring-1 focus:ring-white" title="Фиолетовый"><span class="block w-4 h-4 bg-purple-500 rounded-full border border-gray-400"></span></button>
          <button @click="selectColor('#22c55e')" class="p-1 rounded hover:opacity-80 focus:outline-none focus:ring-1 focus:ring-white" title="Зеленый"><span class="block w-4 h-4 bg-green-500 rounded-full border border-gray-400"></span></button>
          <button @click="selectColor('#3b82f6')" class="p-1 rounded hover:opacity-80 focus:outline-none focus:ring-1 focus:ring-white" title="Синий"><span class="block w-4 h-4 bg-blue-500 rounded-full border border-gray-400"></span></button>
          <button @click="resetColor()" class="p-1 rounded col-span-4 text-xs text-center hover:bg-main hover:text-white focus:outline-none focus:ring-1 focus:ring-white" title="Сбросить цвет">Сбросить</button>
        </div>
</div>

    </div>

    <!-- Editor Content -->
    <editor-content :editor="editor" class="tiptap-editor-content focus:outline-none min-h-[20px]" />

     <!-- Options Button Trigger -->
     <div class="absolute top-1 right-1 opacity-0 group-hover:opacity-100 transition-opacity duration-200 z-10">
      <button @click.stop="toggleOptions" class="text-gray-500 hover:text-gray-700 focus:outline-none">
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"></path>
        </svg>
      </button>
       <!-- Options Menu Dropdown -->
       <div v-if="showOptions" class="absolute right-0 bg-white border border-gray-200 rounded-md shadow-lg w-50">
        <!-- Delete Option -->
        <button @click.prevent="handleDelete" class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white">
          <DeleteIcon class="w-5 h-5 mr-3"/>
          Удалить
        </button>
        <!-- Copy Image URL Option -->
        <button @click.prevent="handleCopy" class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white">
          <CopyIcon class="w-5 h-5 mr-3"/>
          Копировать
        </button>
        <!-- Download Option -->
        <button @click.prevent="handleDownload" class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white">
          <DownloadIcon class="w-5 h-5 mr-3"/>
          Скачать
        </button>
      </div>
     </div>
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

// --- ИМПОРТ ИКОНОК ДЛЯ МЕНЮ ОПЦИЙ ---
// Убедитесь, что пути правильные
import CopyIcon from '@/modules/content/components/icons/CopyIcon.vue';
import DeleteIcon from '@/modules/content/components/icons/DeleteIcon.vue';
import DownloadIcon from '@/modules/content/components/icons/DownloadIcon.vue';
// --- ---

export default {
  name: "TextBlock",
  components: {
    EditorContent,
    // --- Регистрация иконок ---
    CopyIcon,
    DeleteIcon,
    DownloadIcon
    // --- ---
  },
  props: {
    data: { type: Object, required: true, default: () => ({ text: '' }) },
    index: { type: Number, required: true }
  },
  emits: ['update', 'delete', 'request-new-block-after'],
  data() {
    return {
      editor: null,
      showOptions: false,
      showColorPalette: false,
      isFocused: false,
      // Refs будут доступны через this.$refs после монтирования
    };
  },
  watch: {
    'data.text'(newVal) {
      if (this.editor && !this.editor.isDestroyed) {
        const editorHTML = this.editor.getHTML();
        if (newVal !== editorHTML && !this.editor.isFocused) {
          if (this.editor.commands) {
            this.editor.commands.setContent(newVal || '', false);
          }
        }
      }
    }
  },
  mounted() {
    this.editor = new Editor({
      content: this.data.text || '',
      extensions: [
        StarterKit.configure({
           hardBreak: false,
           heading: { levels: [1, 2, 3] },
        }),
        Placeholder.configure({ placeholder: 'Введите текст...' }),
        TextStyle,
        Color,
        TextAlign.configure({
          types: ['heading', 'paragraph'],
          defaultAlignment: 'left',
        }),
        Underline,
      ],
      onUpdate: ({ editor: currentEditor }) => {
        if (currentEditor && !currentEditor.isDestroyed) {
           this.$emit('update', { index: this.index, newData: { text: currentEditor.getHTML() } });
        }
      },
      onFocus: () => {
        this.isFocused = true;
      },
      onBlur: ({ event }) => {
         setTimeout(() => {
              const relatedTarget = event?.relatedTarget;
              const wrapperEl = this.$refs.wrapperRef;
              const toolbarEl = this.$refs.toolbarRef;
              const paletteEl = this.$refs.colorPaletteRef;
              const optionsEl = this.$refs.optionsMenuRef;

              if (
                  wrapperEl && !wrapperEl.contains(relatedTarget) &&
                  (!toolbarEl || !toolbarEl.contains(relatedTarget)) &&
                  (!paletteEl || !paletteEl.contains(relatedTarget)) &&
                  (!optionsEl || !optionsEl.contains(relatedTarget))
              ) {
                   if (this.editor && !this.editor.isDestroyed) {
                       this.isFocused = false;
                   }
                   this.showOptions = false;
                   this.showColorPalette = false;
              }
         }, 150);
      },
      editorProps: {
          handleKeyDown: (view, event) => {
            if (event.key === 'Enter' && !event.shiftKey) {
               event.preventDefault();
               this.$emit('request-new-block-after', this.index);
               return true;
            }
            return false;
          },
      }
    });

    document.addEventListener('click', this.handleClickOutside);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
    if (this.editor && !this.editor.isDestroyed) {
      this.editor.destroy();
      this.editor = null;
    }
  },
  methods: {
    // --- Editor State Checkers ---
    editorIsActive(name, attributes = {}) {
        return this.editor && !this.editor.isDestroyed && this.editor.isActive(name, attributes);
    },
    editorCan(action, attributes = {}) {
        // Simple check, Tiptap 'can' is more complex potentially
        return this.editor && !this.editor.isDestroyed && this.editor.can()?.[action]?.(attributes);
    },

    // --- Focus Handling ---
    handleFocusIn() {
        if (!this.isFocused) {
            this.isFocused = true;
        }
    },
    // Note: handleFocusOut logic is inside onBlur with a timeout

    // --- Menu Toggles & Outside Click ---
    handleClickOutside(event) {
      const target = event.target;
      const wrapperEl = this.$refs.wrapperRef;

      if (!target || !wrapperEl || wrapperEl.contains(target)) {
         return;
      }

      // Close Options Menu
      const optionsEl = this.$refs.optionsMenuRef;
      if (this.showOptions && optionsEl && !optionsEl.contains(target)) {
          const optionsToggleButton = wrapperEl.querySelector('button > svg[viewBox="0 0 24 24"] > path[d^="M12 8c1.1"]');
          if (!optionsToggleButton || !optionsToggleButton.closest('button').contains(target)) {
             this.showOptions = false;
          }
      }

      // Close Color Palette
      const paletteEl = this.$refs.colorPaletteRef;
      if (this.showColorPalette && paletteEl && !paletteEl.contains(target)) {
           const colorToggleButton = wrapperEl.querySelector('button > svg[viewBox="0 0 24 24"] > g > path[d^="M2.25,18.25"]');
           if (!colorToggleButton || !colorToggleButton.closest('button').contains(target)) {
             this.showColorPalette = false;
           }
      }
    },
    toggleOptions() {
        const newState = !this.showOptions;
        this.showColorPalette = false; // Close other menu
        this.showOptions = newState;
        // Optional: Focus first item in menu when opened
        // if (newState) {
        //   this.$nextTick(() => this.$refs.optionsMenuRef?.querySelector('button')?.focus());
        // }
    },
    toggleColorPalette() {
        const newState = !this.showColorPalette;
        this.showOptions = false; // Close other menu
        this.showColorPalette = newState;
    },

    // --- Color Methods ---
    selectColor(color) {
      if (!this.editor || this.editor.isDestroyed) return;
      this.editor.chain().focus().setColor(color).run();
      this.showColorPalette = false;
    },
    resetColor() {
      if (!this.editor || this.editor.isDestroyed) return;
      this.editor.chain().focus().unsetColor().run();
      this.showColorPalette = false;
    },

    // --- Block Action Methods (from options menu) ---
    handleDelete() {
      this.$emit('delete', this.index);
      this.showOptions = false;
    },
    handleCopyText() {
      if (!this.editor || this.editor.isDestroyed) return;
      const textToCopy = this.editor.getText(); // Get plain text
      navigator.clipboard.writeText(textToCopy)
          .then(() => {
            alert('Текст скопирован в буфер обмена!'); // Or use a toast notification
          })
          .catch(err => {
            console.error('Не удалось скопировать текст: ', err);
            alert('Не удалось скопировать текст: ' + err);
          });
      this.showOptions = false;
    },
    handleDownloadText() {
        if (!this.editor || this.editor.isDestroyed) return;
        try {
            const textContent = this.editor.getText(); // Get plain text
            const blob = new Blob([textContent], { type: 'text/plain;charset=utf-8' });
            const link = document.createElement('a');
            link.href = URL.createObjectURL(blob);
            link.download = `text_block_${this.index || 0}.txt`; // Filename
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            URL.revokeObjectURL(link.href); // Clean up blob URL
        } catch(err) {
             console.error('Не удалось скачать файл: ', err);
             alert('Не удалось скачать файл: ' + err);
        }
        this.showOptions = false;
    },

    // --- Focus Control Method (callable from parent via ref) ---
    focusEditor() {
      if (this.editor && !this.editor.isDestroyed && !this.editor.isFocused) {
        this.editor.commands.focus('end');
      }
    },
  }
};
</script>

<style>
/* Стили остаются такими же, как в предыдущем ответе */
.ProseMirror {
  outline: none;
  word-wrap: break-word;
  white-space: pre-wrap;
  padding: 0.5rem;
  min-height: 1.5em;
  line-height: 1.5;
}
.ProseMirror p.is-editor-empty:first-child::before {
  content: attr(data-placeholder);
  float: left;
  color: #a0aec0;
  pointer-events: none;
  height: 0;
}
.toolbar button.is-active {
  background-color: #4b5563;
}
.toolbar button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
.ProseMirror p, .ProseMirror h1, .ProseMirror h2, .ProseMirror h3, .ProseMirror h4, .ProseMirror h5, .ProseMirror h6 {
    margin: 0;
    padding: 0.25em 0;
    line-height: 1.4;
}
.ProseMirror h1 { font-size: 1.875em; font-weight: 700; }
.ProseMirror h2 { font-size: 1.5em; font-weight: 600; }
.ProseMirror h3 { font-size: 1.25em; font-weight: 600; }
.ProseMirror u {
    text-decoration: underline;
}
.ProseMirror [style*="text-align: center"] { text-align: center; }
.ProseMirror [style*="text-align: right"] { text-align: right; }
.ProseMirror [style*="text-align: left"] { text-align: left; }
.toolbar button {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    min-width: 2rem;
    height: 2rem;
}
.toolbar span[class*="bg-"], .toolbar span[style*="background-color"] {
    display: block;
    width: 1rem;
    height: 1rem;
    border-radius: 50%;
    box-shadow: inset 0 0 0 1px rgba(0,0,0,0.1);
}
.tiptap-block-wrapper .toolbar,
.tiptap-block-wrapper .absolute[ref="colorPaletteRef"],
.tiptap-block-wrapper .absolute[ref="optionsMenuRef"] {
    z-index: 20;
}
/* Добавление flex-shrink-0 к иконкам в меню опций, чтобы текст не смещал их */
.absolute[ref="optionsMenuRef"] .flex-shrink-0 {
    flex-shrink: 0;
}
</style>
<template>
  <div
    ref="wrapperRef"
    class="relative rounded-md p-4 hover:bg-gray-50 group"
    @focusin="isFocused = true"
    @focusout="handleFocusOut"
  >
    <!-- Панель инструментов (позиционирование относительно курсора) -->
    <EditorToolbar
      v-if="isFocused && editor"
      ref="toolbarRef"
      :editor="editor"
      :position="toolbarPosition"
      :current-text-color="currentTextColor"
      @update:text-color="currentTextColor = $event"
    />

    <!-- Кнопка опций -->
    <div class="absolute top-2 right-2">
      <OptionsMenu
        @copy="copyContent"
        @download="downloadContent"
        @delete="deleteBlock"
      />
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

import EditorToolbar from '@/modules/content/components/com/EditorToolbar.vue';
import OptionsMenu from '@/modules/content/components/com/OptionsMenu.vue';// Путь к компоненту нужно правильно указать

export default {
  name: 'TextBlock',
  
  components: {
    EditorToolbar,
    EditorContent,
    OptionsMenu
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
      currentTextColor: null,
      cursorPosition: { x: 0, y: 0 },
      toolbarOffsetY: 10
    };
  },
  
  computed: {
    toolbarPosition() {
      return {
        left: `${this.cursorPosition.x}px`,
        top: `${this.cursorPosition.y - this.toolbarOffsetY - (this.$refs.toolbarRef ? this.$refs.toolbarRef.$el.offsetHeight : 0)}px`,
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
  },
  
  beforeDestroy() {
    if (this.editor) {
      this.editor.destroy();
    }
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
        const toolbarWidth = this.$refs.toolbarRef ? this.$refs.toolbarRef.$el.offsetWidth : 0;

        this.cursorPosition = {
          x: targetLeft - (toolbarWidth / 2),
          y: startCoords.top
        };
        this.adjustToolbarPosition(); 
      });
    },
  
    adjustToolbarPosition() {
      if (!this.$refs.toolbarRef || !this.editor?.view.dom.isConnected) return; 

      const toolbar = this.$refs.toolbarRef.$el;
    
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
    
    handleFocusOut(event) {
      setTimeout(() => {
        const isRelatedTargetInside = this.$refs.wrapperRef?.contains(event.relatedTarget) ||
          (this.$refs.toolbarRef && this.$refs.toolbarRef.$el.contains(event.relatedTarget));
          
        if (!isRelatedTargetInside) {
          this.isFocused = false;
        }
      }, 0);
    },
    
    async copyContent() {
      try {
        await navigator.clipboard.writeText(this.editor.getText());
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
    },
    
    deleteBlock() {
      this.$emit('delete', this.index);
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
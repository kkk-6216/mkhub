<template>
  <div class="relative command-input-area mb-4">
    <div
      ref="editorRef"
      class="w-full p-2 focus:outline-none pt-2 text-gray-700"
      contenteditable="true"
      @input="handleInput"
      @keydown="handleKeyDown"
      :placeholder="inputPlaceholder"
      @click="handleClick"
    ></div>

    <CommandMenu
      v-if="showCommandMenu"
      :commands="filteredCommands"
      :active-index="activeCommandIndex"
      :style="commandMenuPosition"
      @command-selected="executeCommand"
      @set-active-index="activeCommandIndex = $event"
    />
  </div>
</template>

<script>
import CommandMenu from '@/modules/content/components/com/CommandMenu.vue';
import { useCommands } from '@/modules/content/components/com/useCommands';

export default {
  name: 'EditorInput',
  components: {
    CommandMenu
  },
  emits: ['command-typed', 'direct-input', 'text-entered', 'clear-requested'],
  setup() {
    const { getFilteredCommands } = useCommands();
    return { getFilteredCommands };
  },
  data() {
    return {
      showCommandMenu: false,
      commandText: '',
      isCommandTyping: false,
      isDirectlyTyping: false,
      commandMenuPosition: { top: '100%', left: '0px' },
      activeCommandIndex: 0,
      inputPlaceholder: "Введите '/' для отображения команд..."
    }
  },
  computed: {
    filteredCommands() {
      if (!this.isCommandTyping || !this.commandText.startsWith('/')) return [];
      if (this.commandText === '/') return this.getFilteredCommands('');
      return this.getFilteredCommands(this.commandText.substring(1).toLowerCase());
    }
  },
  mounted() {
    document.addEventListener('click', this.handleClickOutside);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
  },
  methods: {
    handleInput(event) {
      const text = event.target.innerText;
      
      if (text.startsWith('/')) {
        this.isCommandTyping = true;
        this.isDirectlyTyping = false;
        this.commandText = text;
        this.activeCommandIndex = 0;
        this.showCommandMenu = true;
        this.calculateCommandMenuPosition();
      } 
      else if (text && !this.isCommandTyping) {
        this.isDirectlyTyping = true;
      }
      else if (this.isCommandTyping && !text.startsWith('/')) {
        this.resetCommandState();
        if (text) {
          this.isDirectlyTyping = true;
        }
      }
    },

    handleKeyDown(event) {
      if (this.showCommandMenu) {
        const commandsCount = this.filteredCommands.length;
        if (!commandsCount && !['Escape', 'Backspace', 'Enter', 'Tab'].includes(event.key)) return;

        switch (event.key) {
          case 'ArrowUp':
            event.preventDefault();
            this.activeCommandIndex = (this.activeCommandIndex - 1 + commandsCount) % commandsCount;
            break;
          case 'ArrowDown':
            event.preventDefault();
            this.activeCommandIndex = (this.activeCommandIndex + 1) % commandsCount;
            break;
          case 'Enter':
          case 'Tab':
            event.preventDefault();
            const command = this.filteredCommands[this.activeCommandIndex];
            if (command) {
              this.executeCommand(command);
            } else {
              if (this.$refs.editorRef?.innerText === '/') {
                this.$emit('text-entered', this.$refs.editorRef.innerHTML);
              }
              this.resetCommandState();
            }
            break;
          case 'Escape':
            event.preventDefault();
            this.resetCommandState();
            break;
          case 'Backspace':
            this.$nextTick(() => {
              const currentText = this.$refs.editorRef?.innerText || '';
              if (this.isCommandTyping && !currentText.startsWith('/')) {
                this.resetCommandState();
                if (currentText) {
                  this.isDirectlyTyping = true;
                }
              } else if (this.isCommandTyping && currentText !== this.commandText) {
                this.commandText = currentText;
                this.activeCommandIndex = 0;
                this.calculateCommandMenuPosition();
              }
            });
            break;
        }
      } 
      else if (event.key === 'Enter') {
        event.preventDefault();
        const editor = this.$refs.editorRef;
        if (editor?.innerText.trim()) {
          this.$emit('text-entered', editor.innerHTML);
          this.isDirectlyTyping = false;
        } else {
          this.clearInput();
        }
      }
      else if (this.isDirectlyTyping && event.key === '.') {
        setTimeout(() => {
          const editor = this.$refs.editorRef;
          if (editor?.innerText.trim()) {
            this.$emit('text-entered', editor.innerHTML);
            this.isDirectlyTyping = false;
          }
        }, 0);
      }
    },

    handleClick() {
      if (this.isCommandTyping && this.showCommandMenu) {
        this.calculateCommandMenuPosition();
      }
    },

    executeCommand(command) {
      this.$emit('command-typed', command.type);
      this.resetCommandState();
    },

    resetCommandState() {
      this.showCommandMenu = false;
      this.isCommandTyping = false;
      this.commandText = '';
      this.activeCommandIndex = 0;
    },

    calculateCommandMenuPosition() {
      this.$nextTick(() => {
        const inputEl = this.$refs.editorRef;
        if (!inputEl) return;

        const selection = window.getSelection();
        if (!selection || selection.rangeCount === 0) return;

        const range = selection.getRangeAt(0);
        const rect = range.getBoundingClientRect();
        const inputRect = inputEl.getBoundingClientRect();

        // Оценка места для меню сверху и снизу
        const menuHeight = 200; // Примерная высота меню
        const verticalMargin = 2;
        const spaceBelow = window.innerHeight - rect.bottom;
        const spaceAbove = rect.top;
        
        let top;
        if (spaceBelow < menuHeight && spaceAbove >= menuHeight) {
          top = rect.top - inputRect.top - menuHeight - verticalMargin;
        } else {
          top = rect.bottom - inputRect.top + verticalMargin;
        }

        // Горизонтальное позиционирование
        const menuWidth = 208; // Примерная ширина меню (w-52 = 13rem = 208px)
        let left = rect.left - inputRect.left;
        
        if ((left + menuWidth) > inputRect.width) {
          left = inputRect.width - menuWidth - 5;
        }
        left = Math.max(left, 0);

        this.commandMenuPosition = {
          top: `${top}px`,
          left: `${left}px`,
        };
      });
    },

    handleClickOutside(event) {
      const editor = this.$refs.editorRef;
      if (editor && !editor.contains(event.target)) {
        if (this.showCommandMenu) {
          this.resetCommandState();
        }
        
        if (editor.innerText.trim() && this.isDirectlyTyping) {
          this.$emit('direct-input', editor.innerHTML);
          this.isDirectlyTyping = false;
        }
      }
    },

    clearInput() {
      const editor = this.$refs.editorRef;
      if (editor) editor.innerHTML = '';
      this.$emit('clear-requested');
    },

    focus() {
      this.$refs.editorRef?.focus();
    },

    scrollToView() {
      this.$refs.editorRef?.scrollIntoView({
        behavior: 'smooth',
        block: 'nearest'
      });
    }
  }
}
</script>

<style scoped>
[contenteditable][placeholder]:empty:before {
  content: attr(placeholder);
  color: #a0aec0;
  cursor: text;
  pointer-events: none;
  display: block;
}

.command-input-area [contenteditable] {
  min-height: 1.5em;
  line-height: 1.5;
}
</style>
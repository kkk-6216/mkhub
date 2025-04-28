<template>
  <div class="relative command-input-area mb-4">
    <div
        ref="editorRef"
        class="w-full ml-10 p-2 focus:outline-none pt-2 text-gray-700"
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
import CommandMenu from '@/modules/content/ContentEditor/components/CommandMenu.vue';
import { useCommands } from '@/modules/content/ContentEditor/components/useCommands';

export default {
  name: 'EditorInput',
  components: { CommandMenu },
  emits: ['command-typed', 'direct-input', 'text-entered', 'clear-requested'],

  data() {
    return {
      commands: [],
      showCommandMenu: false,
      commandText: '',
      isCommandTyping: false,
      isDirectlyTyping: false,
      commandMenuPosition: { top: '100%', left: '0px' },
      activeCommandIndex: 0,
      inputPlaceholder: "Введите '/' для отображения команд...",
      getFilteredCommands: null
    };
  },

  computed: {
    filteredCommands() {
      if (!this.isCommandTyping || !this.commandText.startsWith('/')) {
        return [];
      }
      const query = this.commandText === '/' ? '' : this.commandText.slice(1).toLowerCase();
      return this.getFilteredCommands
          ? this.getFilteredCommands(query)
          : this.commands;
    }
  },

  created() {
    const { commands, getFilteredCommands } = useCommands();
    this.commands = commands;
    this.getFilteredCommands = getFilteredCommands;
  },

  mounted() {
    document.addEventListener('click', this.handleClickOutside);
  },

  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
  },

  methods: {
    handleInput(event) {
      const editor = this.$refs.editorRef;
      // если пусто — очистим всё и покажем placeholder
      const text = editor.innerText.trim();
      if (!text) {
        this.clearInput();
        return;
      }
      if (text.startsWith('/')) {
        this.isCommandTyping = true;
        this.isDirectlyTyping = false;
        this.commandText = editor.innerText;
        this.activeCommandIndex = 0;
        this.showCommandMenu = true;
        this.calculateCommandMenuPosition();
      } else if (!this.isCommandTyping) {
        this.isDirectlyTyping = true;
      } else if (this.isCommandTyping && !editor.innerText.startsWith('/')) {
        this.resetCommandState();
        this.isDirectlyTyping = !!editor.innerText.trim();
      }
    },

    handleKeyDown(event) {
      if (this.showCommandMenu) {
        this.onCommandMenuKey(event);
      } else if (event.key === 'Enter') {
        event.preventDefault();
        const editor = this.$refs.editorRef;
        const content = editor.innerText.trim();
        if (content) {
          this.$emit('text-entered', editor.innerHTML);
          this.clearInput();
        } else {
          this.clearInput();
        }
      } else if (this.isDirectlyTyping && event.key === '.') {
        setTimeout(() => {
          const editor = this.$refs.editorRef;
          if (editor.innerText.trim()) {
            this.$emit('text-entered', editor.innerHTML);
            this.clearInput();
          }
        }, 0);
      }
    },

    onCommandMenuKey(event) {
      const count = this.filteredCommands.length;
      if (!count && !['Escape', 'Backspace', 'Enter', 'Tab'].includes(event.key)) return;

      switch (event.key) {
        case 'ArrowUp':
          event.preventDefault();
          this.activeCommandIndex = (this.activeCommandIndex - 1 + count) % count;
          break;
        case 'ArrowDown':
          event.preventDefault();
          this.activeCommandIndex = (this.activeCommandIndex + 1) % count;
          break;
        case 'Enter':
        case 'Tab':
          event.preventDefault();
          const cmd = this.filteredCommands[this.activeCommandIndex];
          if (cmd) {
            this.executeCommand(cmd);
          } else if (this.$refs.editorRef.innerText === '/') {
            this.$emit('text-entered', this.$refs.editorRef.innerHTML);
            this.resetCommandState();
          }
          break;
        case 'Escape':
          event.preventDefault();
          this.resetCommandState();
          break;
        case 'Backspace':
          this.$nextTick(() => {
            const editor = this.$refs.editorRef;
            const current = editor.innerText;
            if (this.isCommandTyping && !current.startsWith('/')) {
              this.resetCommandState();
              this.isDirectlyTyping = !!current.trim();
            } else if (this.isCommandTyping && current !== this.commandText) {
              this.commandText = current;
              this.activeCommandIndex = 0;
              this.calculateCommandMenuPosition();
            }
          });
          break;
      }
    },

    handleClick() {
      if (this.isCommandTyping && this.showCommandMenu) this.calculateCommandMenuPosition();
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
        const editor = this.$refs.editorRef;
        const sel = window.getSelection();
        if (!editor || !sel || !sel.rangeCount) return;

        const range = sel.getRangeAt(0);
        const rect = range.getBoundingClientRect();
        const inputRect = editor.getBoundingClientRect();
        const menuHeight = 200;
        const margin = 2;
        const below = window.innerHeight - rect.bottom;
        const above = rect.top;

        let top;
        if (below < menuHeight && above >= menuHeight) {
          top = rect.top - inputRect.top - menuHeight - margin;
        } else {
          top = rect.bottom - inputRect.top + margin;
        }

        const menuWidthPx = 208;
        let left = rect.left - inputRect.left;
        if (left + menuWidthPx > inputRect.width) {
          left = inputRect.width - menuWidthPx - 5;
        }
        left = Math.max(left, 0);

        this.commandMenuPosition = { top: `${top}px`, left: `${left}px` };
      });
    },

    handleClickOutside(event) {
      const editor = this.$refs.editorRef;
      if (editor && !editor.contains(event.target)) {
        if (this.showCommandMenu) this.resetCommandState();
        if (editor.innerText.trim() && this.isDirectlyTyping) {
          this.$emit('direct-input', editor.innerHTML);
          this.clearInput();
        }
      }
    },

    clearInput() {
      this.resetCommandState();
      const editor = this.$refs.editorRef;
      if (editor) {
        // полностью очистить содержимое и узлы
        editor.innerHTML = '';
        editor.textContent = '';
      }
      this.$emit('clear-requested');
    },

    focus() {
      this.$refs.editorRef.focus();
    },

    scrollToView() {
      this.$refs.editorRef.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }
  }
};
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

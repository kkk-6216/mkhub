<template>
  <div class="relative w-full p-5">
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
    <div class="relative command-input-area">
      <div
        ref="editorInputRef"
        class="w-full p-2 focus:outline-none pt-2 text-gray-700"
        contenteditable="true"
        @input="handleInput"
        @keydown="handleKeyDown"
        :placeholder="inputPlaceholder"
        @click="handleClickOnInput"
      ></div>

      <!-- Выпадающее меню команд -->
      <div
        v-if="showCommandMenu"
        ref="commandMenuRef"
        class="absolute left-0 bg-white rounded-md shadow-lg w-52 z-30"
        :style="commandMenuPosition"
      >
        <div class="p-1 max-h-60 overflow-y-auto">
          <template v-if="filteredCommands.length">
            <button
              v-for="(command, cmdIndex) in filteredCommands"
              :key="command.text"
              :ref="(el) => { if (el) commandRefs[cmdIndex] = el; }"
              :class="{ 'bg-main text-white': cmdIndex === activeCommandIndex }"
              class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white"
              @click="executeCommand(command)"
              @mouseover="activeCommandIndex = cmdIndex"
            >
              <span
                class="flex items-center justify-center w-6 h-6 mr-2 flex-shrink-0"
                :class="{ 'text-white': cmdIndex === activeCommandIndex, 'hover:text-white': cmdIndex !== activeCommandIndex }"
              >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path :d="command.icon" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/>
                </svg>
              </span>
              <span class="font-medium">{{ command.label }}</span>
            </button>
          </template>
          <div v-else class="p-2 text-sm text-red-500">
            Нет подходящих команд.
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import TextBlock from '@/modules/content/components/blocks/TextBlock.vue';
import ImageBlock from '@/modules/content/components/blocks/ImageBlock.vue';
import FileBlock from '@/modules/content/components/blocks/FileBlock.vue';

export default {
  name: "ContentEditor",
  components: {
    TextBlock,
    ImageBlock,
    FileBlock
  },
  props: {
    initialContent: { type: Array, default: () => [] }
  },
  emits: ['update:content'], // Используем update:content для v-model стиля
  data() {
    return {
      contentBlocks: [],
      blockRefs: {},
      commandRefs: [],
      showCommandMenu: false,
      commandText: '',
      isCommandTyping: false,
      commandMenuPosition: { top: '100%', left: '0px' },
      activeCommandIndex: 0,
      commands: [
        { text: '/text', label: 'Текст', icon: 'M3 17h18M3 12h18M3 7h18', action: () => this.addEmptyTextBlockAndFocus(this.contentBlocks.length) },
        { text: '/image', label: 'Изображение', icon: 'M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z', action: () => this.$refs.imageInputRef?.click() },
        { text: '/file', label: 'Файл', icon: 'M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13', action: () => this.$refs.fileInputRef?.click() }
      ],
      blockComponents: {
        'text-block': TextBlock,
        'image-block': ImageBlock,
        'file-block': FileBlock
      }
    };
  },
  computed: {
    filteredCommands() {
      if (!this.isCommandTyping || !this.commandText.startsWith('/')) return [];
      if (this.commandText === '/') return this.commands;
      const query = this.commandText.substring(1).toLowerCase();
      return this.commands.filter(cmd =>
        cmd.text.substring(1).toLowerCase().includes(query) ||
        cmd.label.toLowerCase().includes(query)
      );
    },
    inputPlaceholder() {
      return "Напишите что-нибудь или введите '/' для команд...";
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
    this.commandRefs = [];
  },
  mounted() {
    document.addEventListener('click', this.handleClickOutsideCommandMenu);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutsideCommandMenu);
  },
  methods: {
    prepareBlocks(blocks) {
      return Array.isArray(blocks)
        ? blocks.map(block => ({ ...block, id: block.id || Math.random().toString(36).substring(2, 9) }))
        : [];
    },

    handleInput(event) {
      const text = event.target.innerText;
      this.isCommandTyping = text.startsWith('/');
      if (this.isCommandTyping) {
        this.commandText = text;
        this.activeCommandIndex = 0;
        this.showCommandMenu = true;
        this.calculateCommandMenuPosition(); // <-- Важно вызывать здесь
      } else {
        this.resetCommandState();
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
            this.scrollCommandIntoView();
            break;
          case 'ArrowDown':
            event.preventDefault();
            this.activeCommandIndex = (this.activeCommandIndex + 1) % commandsCount;
            this.scrollCommandIntoView();
            break;
          case 'Enter':
          case 'Tab':
            event.preventDefault();
            const command = this.filteredCommands[this.activeCommandIndex];
            if (command) {
              this.executeCommand(command);
            } else {
                if (this.$refs.editorInputRef?.innerText === '/') {
                    this.addTextBlock(this.$refs.editorInputRef.innerHTML, this.contentBlocks.length);
                    this.clearEditorInput();
                    this.scrollToInput();
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
                const currentText = this.$refs.editorInputRef?.innerText || '';
                if (this.isCommandTyping && !currentText.startsWith('/')) {
                    this.resetCommandState();
                } else if (this.isCommandTyping && currentText !== this.commandText) {
                    this.commandText = currentText;
                    this.activeCommandIndex = 0;
                    this.calculateCommandMenuPosition(); // Обновляем позицию при изменении текста команды
                }
            });
            break;
        }
      } else if (event.key === 'Enter') {
        event.preventDefault();
        const editor = this.$refs.editorInputRef;
        if (editor?.innerText.trim()) {
          this.addTextBlock(editor.innerHTML, this.contentBlocks.length);
          this.clearEditorInput();
          this.scrollToInput();
        } else {
          this.clearEditorInput();
        }
      }
      // Другие обработки клавиш (например, Backspace вне режима команд) можно добавить здесь
    },

    handleClickOnInput() {
      if (this.isCommandTyping && this.showCommandMenu) {
        // Пересчитываем позицию при клике, т.к. каретка могла сместиться
        this.calculateCommandMenuPosition();
      }
    },

    executeCommand(command) {
      command.action();
      this.resetCommandState();
      this.clearEditorInput();
    },

    resetCommandState() {
      this.showCommandMenu = false;
      this.isCommandTyping = false;
      this.commandText = '';
      this.activeCommandIndex = 0;
    },

    // --- ОБНОВЛЕННЫЙ МЕТОД ---
    calculateCommandMenuPosition() {
      this.$nextTick(() => {
        const inputEl = this.$refs.editorInputRef;
        const menuEl = this.$refs.commandMenuRef;

        if (!inputEl || !menuEl) return;

        const selection = window.getSelection();
        if (!selection || selection.rangeCount === 0) return;

        const range = selection.getRangeAt(0);
        const rect = range.getBoundingClientRect(); // Позиция каретки относительно viewport
        const inputRect = inputEl.getBoundingClientRect(); // Позиция поля ввода относительно viewport

        const menuHeight = menuEl.offsetHeight;
        const menuWidth = menuEl.offsetWidth;

        // --- Вертикальное позиционирование ---
        let top;
        const spaceBelow = window.innerHeight - rect.bottom; // Пространство от каретки до низа окна
        const spaceAbove = rect.top; // Пространство от верха окна до каретки
        const verticalMargin = 2; // Отступ между кареткой и меню

        if (spaceBelow < menuHeight && spaceAbove >= menuHeight) {
          // Места снизу НЕ хватает, а сверху ХВАТАЕТ: Позиционируем СВЕРХУ каретки
          top = rect.top - inputRect.top - menuHeight - verticalMargin;
        } else {
          // Иначе (места снизу хватает ИЛИ места не хватает нигде): Позиционируем СНИЗУ (по умолчанию)
          top = rect.bottom - inputRect.top + verticalMargin;
        }

        // --- Горизонтальное позиционирование ---
        let left = rect.left - inputRect.left; // Позиция по левому краю каретки

        // Если вылезает за правую границу ИНПУТА
        if ((left + menuWidth) > inputRect.width) {
           left = inputRect.width - menuWidth - 5; // Сдвигаем влево с отступом 5px
        }
        left = Math.max(left, 0); // Не уезжаем левее инпута

        // Применяем рассчитанные стили
        this.commandMenuPosition = {
          top: `${top}px`,
          left: `${left}px`,
        };
      });
    },
    // --- КОНЕЦ ОБНОВЛЕННОГО МЕТОДА ---

    scrollCommandIntoView() {
      this.$nextTick(() => {
        this.commandRefs[this.activeCommandIndex]?.scrollIntoView({
          block: 'nearest'
        });
      });
    },

    handleClickOutsideCommandMenu(event) {
      const menu = this.$refs.commandMenuRef;
      const input = this.$refs.editorInputRef;
      if (this.showCommandMenu && menu && !menu.contains(event.target) && input && !input.contains(event.target)) {
        this.resetCommandState();
      }
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
      this.scrollToInput(); // Прокручиваем к инпуту
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
            this.$nextTick(() => this.$refs.editorInputRef?.focus());
        }
        this.scrollToInput(); // Всегда прокручиваем к инпуту после удаления
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
        this.scrollToInput(); // Прокручиваем к инпуту
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
      this.scrollToInput(); // Прокручиваем к инпуту
      event.target.value = '';
    },

    clearEditorInput() {
      const editor = this.$refs.editorInputRef;
      if (editor) editor.innerHTML = '';
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
        this.$refs.editorInputRef?.scrollIntoView({
          behavior: 'smooth',
          block: 'nearest'
        });
         // Опционально: фокусируемся после прокрутки
         // this.$refs.editorInputRef?.focus();
      });
    }
  }
};
</script>

<style scoped>
[contenteditable][placeholder]:empty:before {
  content: attr(placeholder);
  color: #a0aec0; /* text-gray-500 */
  cursor: text;
  pointer-events: none;
  display: block;
}

.command-input-area [contenteditable] {
  min-height: 1.5em;
  line-height: 1.5;
}
</style>
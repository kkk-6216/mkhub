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
        :index="index"
        @update="handleBlockUpdate"
        @delete="deleteBlock(index)"
        @request-new-block-after="insertNewBlockAfter"
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
import MarkdownEditor from '@/modules/content/components/blocks/MarkdownEditor.vue';
import ImageBlock from '@/modules/content/components/blocks/ImageBlock.vue';
import FileBlock from '@/modules/content/components/blocks/FileBlock.vue';
import LinkBlock from '@/modules/content/components/blocks/LinkBlock.vue'; // Импортировали
import EditorInput from '@/modules/content/components/com/EditorInput.vue';
import { useCommands } from '@/modules/content/components/com/useCommands';
import { nextTick } from 'vue'; // Import nextTick

// Утилита для проверки URL
const isValidHttpUrl = (string) => {
  try {
    const url = new URL(string);
    return url.protocol === "http:" || url.protocol === "https:";
  } catch (_) {
    return false;
  }
};

// Генерация ID
const generateId = () => Math.random().toString(36).substring(2, 9);

export default {
  name: "ContentEditor",
  components: {
    MarkdownEditor,
    ImageBlock,
    FileBlock,
    LinkBlock, // Добавили
    EditorInput
  },
  props: {
    initialContent: { type: Array, default: () => [] }
  },
  emits: ['update:content'],
  data() {
    return {
      contentBlocks: [],
      blockRefs: {}, // Используем обычный объект для refs
      commands: {}, // Инициализируем команды
      // Переносим blockComponents в data, чтобы он был доступен в template
      blockComponents: {
        'text-block': MarkdownEditor,
        'image-block': ImageBlock,
        'file-block': FileBlock,
        'link-block': LinkBlock
      }
    };
  },
  computed: {
    commandActions() {
      return {
        text: () => this.addEmptyTextBlockAndFocus(this.contentBlocks.length),
        image: () => this.$refs.imageInputRef?.click(),
        file: () => this.$refs.fileInputRef?.click(),
        link: () => this.addLinkBlock(this.contentBlocks.length) // Добавили команду
      };
    }
  },
  watch: {
    initialContent: {
      handler(newContent) {
        const currentContentString = JSON.stringify(this.contentBlocks);
        const newContentString = JSON.stringify(newContent || []);
        // Сравниваем строки, чтобы избежать бесконечного цикла при глубоком сравнении объектов
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
        // Отправляем копию, чтобы избежать мутаций извне
        this.$emit('update:content', JSON.parse(JSON.stringify(newBlocks)));
      },
      deep: true
    }
  },
  created() {
    // Получаем команды из composable при создании компонента
    const { commands } = useCommands();
    this.commands = commands;

    // Начальная инициализация из props (дублируется с immediate watcher, но для ясности)
    // this.contentBlocks = this.prepareBlocks(this.initialContent || []);
  },
  beforeUpdate() {
      // Очищаем refs перед обновлением DOM
      // Важно сбрасывать объект, чтобы функция :ref работала корректно
      this.blockRefs = {};
  },
  methods: {
    // --- Методы ---

    prepareBlocks(blocks) {
      return Array.isArray(blocks)
        ? blocks.map(block => ({ ...block, id: block.id || generateId() }))
        : [];
    },

    handleCommandTyped(commandType) {
      const action = this.commandActions[commandType]; // Доступ через this
      if (action) {
        action();
      }
    },

    handleDirectInput(htmlContent) {
       const tempDiv = document.createElement('div');
       tempDiv.innerHTML = htmlContent;
       const textContent = tempDiv.textContent || tempDiv.innerText || "";
       const trimmedText = textContent.trim();

       if (isValidHttpUrl(trimmedText)) {
           this.addLinkBlockWithUrl(trimmedText, this.contentBlocks.length);
       } else if (trimmedText) {
         this.addTextBlock(htmlContent, this.contentBlocks.length); // Используем оригинальный HTML
       }
       this.$refs.editorInputRef?.clearInput(); // Доступ через this.$refs
       this.scrollToInput();
    },

    handleTextEntered(htmlContent) {
      const tempDiv = document.createElement('div');
      tempDiv.innerHTML = htmlContent;
      const textContent = tempDiv.textContent || tempDiv.innerText || "";
      const trimmedText = textContent.trim();

      if (isValidHttpUrl(trimmedText)) {
        this.addLinkBlockWithUrl(trimmedText, this.contentBlocks.length);
      } else if (trimmedText) {
        this.addTextBlock(htmlContent, this.contentBlocks.length);
      }
      this.$refs.editorInputRef?.clearInput();
      this.scrollToInput();
    },

    addBlock(blockData, index) {
        const newBlock = {
            ...blockData,
            id: blockData.id || generateId()
        };
        // Используем splice для вставки
        // Создаем новый массив для обеспечения реактивности
        const currentBlocks = [...this.contentBlocks];
        currentBlocks.splice(index, 0, newBlock);
        this.contentBlocks = currentBlocks; // Обновляем data свойство
    },

    addTextBlock(htmlContent, index) {
      console.log(`ContentEditor: Adding text block at index ${index}`);
      this.addBlock({ component: 'text-block', data: { text: htmlContent } }, index);
      nextTick(() => { // Используем импортированный nextTick или this.$nextTick
        if (index === this.contentBlocks.length - 1) { // Фокус только если добавили в конец
             this.focusBlock(index);
        } else {
             this.scrollToInput(); // Иначе просто скролл к инпуту
        }
      });
    },

    addLinkBlock(index) {
      console.log(`ContentEditor: Adding empty link block at index ${index}`);
      this.addBlock({ component: 'link-block', data: { url: '', title: '', description: '', favicon: '' } }, index);
      this.$refs.editorInputRef?.clearInput(); // Очищаем инпут после команды
      nextTick(() => {
        this.focusBlock(index);
      });
    },

    addLinkBlockWithUrl(url, index) {
      console.log(`ContentEditor: Adding link block with URL at index ${index}`);
      this.addBlock({ component: 'link-block', data: { url: url, title: url, description: '', favicon: '' } }, index);
      this.$refs.editorInputRef?.clearInput();
      nextTick(() => {
        this.scrollToInput(); // Не фокусируемся, LinkBlock сам начнет загрузку
      });
    },

    addEmptyTextBlockAndFocus(index) {
      console.log(`ContentEditor: Adding empty text block at index ${index}`);
      this.addBlock({ component: 'text-block', data: { text: '' } }, index);
      this.$refs.editorInputRef?.clearInput();
      nextTick(() => {
        this.focusBlock(index);
      });
    },

    insertNewBlockAfter(currentIndex) {
      console.log(`ContentEditor: Inserting new block after index ${currentIndex}`);
      this.addEmptyTextBlockAndFocus(currentIndex + 1);
    },

    handleBlockUpdate({ index, newData }) {
        if (index >= 0 && index < this.contentBlocks.length) {
            const block = this.contentBlocks[index];
            const updatedData = { ...block.data, ...newData };

            if (JSON.stringify(block.data) !== JSON.stringify(updatedData)) {
                console.log(`ContentEditor: Updating block at index ${index} with data:`, newData);
                // Создаем новый объект блока и новый массив для реактивности
                const updatedBlock = { ...block, data: updatedData };
                const newBlocks = [...this.contentBlocks];
                newBlocks[index] = updatedBlock;
                this.contentBlocks = newBlocks; // Присваиваем новый массив
                // Для Vue 2 можно использовать this.$set для гарантированной реактивности:
                // this.$set(this.contentBlocks, index, updatedBlock);
            }
        } else {
            console.error(`ContentEditor: Invalid index ${index} for block update.`);
        }
    },

    deleteBlock(index) {
      if (index >= 0 && index < this.contentBlocks.length) {
        console.log(`ContentEditor: Deleting block at index ${index}`);
        // Фильтруем массив для удаления (создает новый массив)
        this.contentBlocks = this.contentBlocks.filter((_, i) => i !== index);

        nextTick(() => {
           if (this.contentBlocks.length === 0) {
               this.$refs.editorInputRef?.focus(); // Фокус на инпут, если все удалили
           } else {
                // Фокус на предыдущий, или на первый, если удалили первый
                const focusIndex = Math.max(0, index - 1);
                this.focusBlock(focusIndex);
           }
           this.scrollToInput(); // Убедимся что инпут видим
        });
      }
    },

    handleImageUpload(event) {
      const file = event.target.files?.[0];
      if (!file || !file.type.startsWith('image/')) return;
      console.log("ContentEditor: Handling image upload");
      const reader = new FileReader();
      reader.onload = (e) => {
         this.addBlock({
              component: 'image-block',
              data: { src: e.target.result, caption: file.name, alt: file.name }
          }, this.contentBlocks.length);
          this.scrollToInput();
      };
      reader.readAsDataURL(file);
      event.target.value = ''; // Сбросить инпут
    },

    handleFileUpload(event) {
      const file = event.target.files?.[0];
      if (!file) return;
      console.log("ContentEditor: Handling file upload");
       // TODO: Управление Object URL (создание/удаление)
       this.addBlock({
            component: 'file-block',
            data: { name: file.name, size: file.size, fileUrl: URL.createObjectURL(file) }
        }, this.contentBlocks.length);
      this.scrollToInput();
      event.target.value = ''; // Сбросить инпут
    },

    clearEditorInput() {
      this.$refs.editorInputRef?.clearInput();
    },

    focusBlock(index) {
      nextTick(() => {
        // Доступ к динамическим рефам через this.blockRefs
        const componentRef = this.blockRefs[index];
         console.log(`ContentEditor: Attempting to focus block at index ${index}. Ref:`, componentRef);
        if (componentRef && typeof componentRef.focusEditor === 'function') {
          console.log(`ContentEditor: Calling focusEditor on componentRef for index ${index}`);
          componentRef.focusEditor(); // Предполагается, что focusEditor скроллит к себе
        } else if (componentRef?.$el) { // componentRef может быть самим компонентом
           console.warn(`ContentEditor: Block ${index} has no focusEditor method. Focusing $el.`);
           // $el это корневой элемент компонента блока
           componentRef.$el.focus({ preventScroll: false }); // Фокус с прокруткой
           componentRef.$el.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
        } else {
          console.warn(`ContentEditor: Could not focus block ${index}. Focusing EditorInput instead.`);
          this.$refs.editorInputRef?.focus();
        }
      });
    },

    scrollToInput() {
      nextTick(() => {
        this.$refs.editorInputRef?.scrollToView();
      });
    }
  }
};
</script>

<style scoped>
/* Стили для ContentEditor, если нужны */
</style>
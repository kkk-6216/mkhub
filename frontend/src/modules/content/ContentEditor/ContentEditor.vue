<template>
  <div class="relative w-full p-5 mt-10">
    <!-- Скрытые input для загрузки файлов -->
    <input type="file" ref="imageInputRef" @change="handleImageUpload" accept="image/*" class="hidden" />
    <input type="file" ref="fileInputRef" @change="handleFileUpload" class="hidden" />

    <!-- Блоки контента с возможностью перетаскивания -->
    <draggable
      v-model="contentBlocks"
      item-key="id"
      tag="div"
      class="content-blocks mb-4"
      handle=".drag-handle"
      :animation="200"
      @start="dragStart"
      @end="dragEnd"
      :forceFallback="true"
    >
      <template #item="{ element: block, index }">
        <div
          class="relative group"
          :class="{ 'drag-active': isDragging }"
          @mouseenter="hoveredBlockIndex = index"
          @mouseleave="hoveredBlockIndex = -1"
        >
          <!-- Кнопка для перетаскивания -->
          <button
            class="drag-handle absolute left-[5px] top-[5px] z-10  opacity-0 group-hover:opacity-100 transition-opacity duration-200 cursor-move p-1 bg-white bg-opacity-50 rounded /* Optional styling */"
            :class="{ 'opacity-100': isDragging || hoveredBlockIndex === index }"
            title="Перетащить блок"
          >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="black" class="size-6">
            <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 15 12 18.75 15.75 15m-7.5-6L12 5.25 15.75 9" />
          </svg>

          </button>

          <!-- Сам блок контента -->
          <div class="pl-[35px] pt-[5px]">
            <component
              :is="blockComponents[block.component]"
              :ref="(el) => { if (el) blockRefs[index] = el; }"
              :data="block.data"
              :index="index"
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
// Импорты компонентов блоков
import MarkdownEditor from '@/modules/content/blocks/MarkdownEditor/MarkdownEditor.vue';
import ImageBlock from '@/modules/content/blocks/ImageBlock.vue';
import FileBlock from '@/modules/content/blocks/FileBlock.vue';
import LinkBlock from '@/modules/content/blocks/LinkBlock.vue';
import CodeBlock from '@/modules/content/blocks/CodeBlock.vue';
import FormulaBlock from '@/modules/content/blocks/FormulaBlock.vue';
import EditorInput from '@/modules/content/ContentEditor/components/EditorInput.vue';
import { useCommands } from '@/modules/content/ContentEditor/components/useCommands';
import { nextTick } from 'vue';
import draggable from 'vuedraggable';

// Утилиты
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
      blockRefs: {}, // Используем объект для рефов по индексу
      commands: {},
      // Маппинг строк компонентов на импортированные компоненты
      blockComponents: {
        'text-block': MarkdownEditor,
        'image-block': ImageBlock,
        'file-block': FileBlock,
        'link-block': LinkBlock,
        'code-block': CodeBlock,
        'formula-block': FormulaBlock// Маппинг для CodeBlock
      },
      lastFocusedBlockIndex: -1, // Опционально: для отслеживания фокуса
      isMounted: false, // Флаг для предотвращения действий до монтирования
      isDragging: false, // Флаг для отслеживания состояния перетаскивания
      hoveredBlockIndex: -1
    };
  },
  computed: {
    // Действия для команд, вводимых через "/"
    commandActions() {
      return {
        text: () => this.addEmptyTextBlockAndFocus(this.contentBlocks.length),
        image: () => this.$refs.imageInputRef?.click(),
        file: () => this.$refs.fileInputRef?.click(),
        link: () => this.addLinkBlock(this.contentBlocks.length),
        code: () => this.addCodeBlock(this.contentBlocks.length),
        formula: () => this.addFormulaBlock(this.contentBlocks.length) // Команда для CodeBlock
      };
    }
  },
  watch: {
    // Следим за изменениями входных данных
    initialContent: {
      handler(newContent) {
        // Сравниваем только значащие данные, без ID, чтобы избежать лишних обновлений
        const currentSignificantData = JSON.stringify(this.contentBlocks.map(b => ({ component: b.component, data: b.data })));
        const newSignificantData = JSON.stringify(this.prepareBlocks(newContent || []).map(b => ({ component: b.component, data: b.data })));

        if (newSignificantData !== currentSignificantData) {
          console.log("ContentEditor: Обновление contentBlocks из initialContent prop");
          this.contentBlocks = this.prepareBlocks(newContent || []);
          // Сбрасываем рефы после изменения контента
          nextTick(() => {
            this.blockRefs = {};
          });
        } else {
          // console.log("ContentEditor: initialContent не изменился значительно, пропуск обновления.");
        }
      },
      immediate: true,
      // deep: true // Глубокое отслеживание может быть тяжелым. Сравнение выше должно быть достаточно.
    },
    // Следим за изменениями массива блоков (добавление, удаление, ИЗМЕНЕНИЕ ДАННЫХ ВНУТРИ)
    contentBlocks: {
      handler(newBlocks, oldBlocks) {
        if (!this.isMounted) return; // Не отправляем события до монтирования
        // Сравниваем, чтобы убедиться, что изменение было (deep watch иногда срабатывает лишний раз)
        if (JSON.stringify(newBlocks) !== JSON.stringify(oldBlocks)) {
            console.log("ContentEditor: contentBlocks изменены, отправка события update:content");
            // Отправляем глубокую копию, чтобы внешние изменения не влияли на внутреннее состояние
            this.$emit('update:content', JSON.parse(JSON.stringify(newBlocks)));
        }
      },
      deep: true // Глубокое отслеживание здесь НЕОБХОДИМО для отслеживания ИЗМЕНЕНИЙ ВНУТРИ данных блока
    }
  },
  created() {
    const { commands } = useCommands();
    this.commands = commands;
    // Начальный контент устанавливается через watcher с immediate: true
    console.log("ContentEditor: Компонент создан");
  },
  mounted() {
      this.isMounted = true;
      console.log("ContentEditor: Компонент смонтирован");
      // Если при инициализации нет блоков, фокусируемся на инпуте
      if (this.contentBlocks.length === 0) {
          this.$refs.editorInputRef?.focus();
      }
  },
  beforeUpdate() {
    // Очищаем рефы перед каждым циклом обновления DOM
    // Важно, чтобы :ref функция корректно перезаписывала ссылки
    this.blockRefs = {};
  },
  methods: {
    // --- Управление блоками ---
    dragStart() {
      this.isDragging = true;
      console.log("ContentEditor: Начало перетаскивания блока");
    },
    
    dragEnd() {
      this.isDragging = false;
      console.log("ContentEditor: Завершение перетаскивания блока");
      // После перетаскивания обновляем порядок блоков
      this.$emit('update:content', JSON.parse(JSON.stringify(this.contentBlocks)));
    },

    /**
     * Подготавливает блоки из входных данных, добавляя ID и проверяя структуру.
     */
    prepareBlocks(blocks) {
      console.log("ContentEditor: prepareBlocks запущено");
      return Array.isArray(blocks)
        ? blocks
            // Фильтруем некорректные блоки
            .filter(block => block && block.component && this.blockComponents[block.component] && typeof block.data === 'object')
            .map(block => ({
              component: block.component,
              // Глубокое клонирование данных, чтобы избежать общих ссылок
              data: JSON.parse(JSON.stringify(block.data)),
              id: block.id || generateId() // Добавляем ID, если его нет
            }))
        : [];
    },

    /**
     * Добавляет новый блок в указанную позицию.
     */
    addBlock(blockData, index) {
      const newBlock = {
        ...blockData,
        id: blockData.id || generateId()
      };
      console.log(`ContentEditor: Добавление блока ${newBlock.component} по индексу ${index} с ID ${newBlock.id}`);
      // Создаем НОВЫЙ массив для обеспечения реактивности
      const currentBlocks = [...this.contentBlocks];
      currentBlocks.splice(index, 0, newBlock);
      this.contentBlocks = currentBlocks; // Присваиваем новый массив
    },

    // Добавьте метод
    addFormulaBlock(index) {
      console.log(`ContentEditor: Добавление блока формулы по индексу ${index}`);
      this.addBlock({
        component: 'formula-block',
        data: { latex: '', displayMode: true }
      }, index);
      this.$refs.editorInputRef?.clearInput();
      nextTick(() => {
        this.focusBlock(index);
      });
    },

    /**
     * Добавляет блок кода.
     */
     addCodeBlock(index) {
      console.log(`ContentEditor: Добавление блока кода по индексу ${index}`);
      this.addBlock({
        component: 'code-block',
        data: {
          code: '', // Начинаем с пустого кода
          language: 'javascript', // Язык по умолчанию
        }
      }, index);
      this.$refs.editorInputRef?.clearInput();
      nextTick(() => {
        this.focusBlock(index);
      });
    },

    addTextBlock(htmlContent, index) {
      console.log(`ContentEditor: Добавление текстового блока по индексу ${index}`);
      this.addBlock({ component: 'text-block', data: { text: htmlContent } }, index);
      nextTick(() => { this.focusBlock(index); });
    },

    addLinkBlock(index) {
      console.log(`ContentEditor: Добавление пустого блока ссылки по индексу ${index}`);
      this.addBlock({ component: 'link-block', data: { url: '', title: '', description: '', favicon: '' } }, index);
      this.$refs.editorInputRef?.clearInput();
      nextTick(() => { this.focusBlock(index); });
    },

    addLinkBlockWithUrl(url, index) {
      console.log(`ContentEditor: Добавление блока ссылки с URL ${url} по индексу ${index}`);
      this.addBlock({ component: 'link-block', data: { url: url, title: '', description: '', favicon: '' } }, index);
      this.$refs.editorInputRef?.clearInput();
      nextTick(() => { this.scrollToInput(); }); // Не фокусируемся, даем загрузить превью
    },

    addImageBlockFromUrl(imageUrl, index) {
      console.log(`ContentEditor: Добавление блока изображения по URL ${imageUrl} по индексу ${index}`);
      this.addBlock({
        component: 'image-block',
        data: { src: imageUrl, caption: 'Изображение по URL', alt: 'Изображение по URL' }
      }, index);
      this.$refs.editorInputRef?.clearInput();
      this.scrollToInput();
    },

    addEmptyTextBlockAndFocus(index) {
      console.log(`ContentEditor: Добавление пустого текстового блока по индексу ${index} и фокусировка`);
      this.addBlock({ component: 'text-block', data: { text: '' } }, index);
      this.$refs.editorInputRef?.clearInput();
      nextTick(() => { this.focusBlock(index); });
    },

    /**
     * Вставляет новый пустой текстовый блок после указанного индекса.
     */
    insertNewBlockAfter(currentIndex) {
      console.log(`ContentEditor: Вставка нового блока после индекса ${currentIndex}`);
      this.addEmptyTextBlockAndFocus(currentIndex + 1);
    },

    /**
     * Обрабатывает событие обновления данных от дочернего блока.
     */
    handleBlockUpdate({ index, newData }) {
      if (index >= 0 && index < this.contentBlocks.length) {
        const block = this.contentBlocks[index];
        const currentDataString = JSON.stringify(block.data);
        const potentialNewData = { ...block.data, ...newData };
        const potentialNewDataString = JSON.stringify(potentialNewData);

        // Обновляем только если данные действительно изменились
        if (currentDataString !== potentialNewDataString) {
          console.log(`ContentEditor: Обновление блока ${block.component} по индексу ${index} данными:`, newData);
          // Создаем НОВЫЙ объект блока и НОВЫЙ массив для реактивности
          const updatedBlock = { ...block, data: potentialNewData };
          const newBlocks = [...this.contentBlocks];
          newBlocks[index] = updatedBlock;
          this.contentBlocks = newBlocks; // Присваиваем новый массив
        } else {
           // console.log(`ContentEditor: Фактических изменений данных не обнаружено для блока ${index}. Пропуск обновления.`);
        }
      } else {
        console.error(`ContentEditor: Неверный индекс ${index} для обновления блока.`);
      }
    },

    /**
     * Удаляет блок по индексу.
     */
    deleteBlock(index) {
      if (index >= 0 && index < this.contentBlocks.length) {
        const deletedBlockType = this.contentBlocks[index]?.component || 'unknown';
        console.log(`ContentEditor: Удаление блока ${deletedBlockType} по индексу ${index}`);
        // filter создает новый массив, обеспечивая реактивность
        this.contentBlocks = this.contentBlocks.filter((_, i) => i !== index);

        nextTick(() => {
          if (this.contentBlocks.length === 0) {
            // Если блоков не осталось, фокусируемся на поле ввода
            console.log("ContentEditor: Блоков не осталось, фокус на EditorInput.");
            this.$refs.editorInputRef?.focus();
          } else {
            // Пытаемся сфокусироваться на предыдущем блоке (или первом, если удалили первый)
            const focusIndex = Math.max(0, index - 1);
            console.log(`ContentEditor: Попытка фокусировки на блоке ${focusIndex} после удаления.`);
            this.focusBlock(focusIndex);
          }
          this.scrollToInput(); // Убедимся, что поле ввода видно
        });
      } else {
        console.error(`ContentEditor: Неверный индекс ${index} для удаления блока.`);
      }
    },

    /**
     * Конвертирует блок (например, LinkBlock) в ImageBlock.
     */
    handleConvertToImage({ index, imageUrl, caption, alt }) {
      if (index >= 0 && index < this.contentBlocks.length) {
        console.log(`ContentEditor: Конвертация блока ${index} в изображение, URL: ${imageUrl}`);
        const imageBlock = {
          component: 'image-block',
          id: generateId(),
          data: { src: imageUrl, caption: caption || 'Изображение по URL', alt: alt || 'Изображение по URL' }
        };
        const newBlocks = [...this.contentBlocks];
        newBlocks[index] = imageBlock;
        this.contentBlocks = newBlocks;
        this.scrollToInput();
      }
    },

    // --- Обработка ввода из EditorInput ---

    /**
     * Обрабатывает ввод команды (например, /code).
     */
    handleCommandTyped(commandType) {
      const action = this.commandActions[commandType];
      if (action) {
        console.log(`ContentEditor: Выполнение команды /${commandType}`);
        action();
      } else {
        console.warn(`ContentEditor: Неизвестная команда: /${commandType}`);
        // Можно добавить как текст, если команда не распознана
        // this.addTextBlock(`/${commandType} `, this.contentBlocks.length);
      }
    },

    handleDirectInput(htmlContent) {
      console.log("ContentEditor: handleDirectInput вызван");
      this.processPastedOrEnteredContent(htmlContent);
    },

    handleTextEntered(htmlContent) {
      console.log("ContentEditor: handleTextEntered вызван");
      this.processPastedOrEnteredContent(htmlContent);
    },

    /**
     * Обрабатывает вставленный или введенный текст/HTML, определяет тип контента (URL, Image URL, Text) и добавляет соответствующий блок.
     */
    processPastedOrEnteredContent(htmlContent) {
      const tempDiv = document.createElement('div');
      tempDiv.innerHTML = htmlContent; // Используем innerHTML для обработки возможного форматирования
      const textContent = tempDiv.textContent || tempDiv.innerText || "";
      const trimmedText = textContent.trim();

      console.log("ContentEditor: Обработка контента. Текст:", trimmedText);

      if (!trimmedText) {
        console.log("ContentEditor: Обработанный контент пуст, игнорирование.");
        this.$refs.editorInputRef?.clearInput(); // Очищаем инпут в любом случае
        return;
      }

      if (isValidHttpUrl(trimmedText)) {
        console.log("ContentEditor: Обнаружен валидный URL:", trimmedText);
        if (isImageUrl(trimmedText)) {
          console.log("ContentEditor: Добавление блока изображения по URL.");
          this.addImageBlockFromUrl(trimmedText, this.contentBlocks.length);
        } else {
          console.log("ContentEditor: Добавление блока ссылки по URL.");
          this.addLinkBlockWithUrl(trimmedText, this.contentBlocks.length);
        }
      } else {
        console.log("ContentEditor: Добавление текстового блока.");
        // Используем исходный htmlContent, чтобы сохранить базовое форматирование (если было)
        this.addTextBlock(htmlContent, this.contentBlocks.length);
      }

      // Очистка инпута и фокусировка обрабатываются внутри методов add*Block
    },

    // --- Загрузка файлов ---

    handleImageUpload(event) {
      const file = event.target.files?.[0];
      if (!file || !file.type.startsWith('image/')) return;
      console.log("ContentEditor: Обработка загрузки изображения:", file.name);
      const reader = new FileReader();
      reader.onload = (e) => {
        this.addBlock({
          component: 'image-block',
          data: { src: e.target.result, caption: file.name, alt: file.name }
        }, this.contentBlocks.length);
        this.scrollToInput();
      };
      reader.readAsDataURL(file);
      event.target.value = ''; // Сбрасываем значение инпута
      this.$refs.editorInputRef?.clearInput();
    },

    handleFileUpload(event) {
      const file = event.target.files?.[0];
      if (!file) return;
      console.log("ContentEditor: Обработка загрузки файла:", file.name);
      // ВАЖНО: Object URL должен быть освобожден (URL.revokeObjectURL), когда блок или компонент уничтожается.
      // Это лучше делать внутри FileBlock.vue в хуке unmounted/beforeUnmount.
      this.addBlock({
        component: 'file-block',
        data: { name: file.name, size: file.size, fileUrl: URL.createObjectURL(file) /*, fileObject: file // Если нужно хранить сам файл */ }
      }, this.contentBlocks.length);
      event.target.value = ''; // Сбрасываем значение инпута
      this.scrollToInput();
      this.$refs.editorInputRef?.clearInput();
    },

    // --- Фокус и прокрутка ---

    /**
     * Обрабатывает событие фокуса от дочернего блока (опционально).
     */
    handleBlockFocus(index) {
       // console.log(`ContentEditor: Блок ${index} получил фокус`);
       this.lastFocusedBlockIndex = index;
    },

    /**
     * Устанавливает фокус на указанный блок.
     */
    focusBlock(index) {
      // Используем nextTick, чтобы гарантировать, что DOM обновлен после добавления/удаления блока
      nextTick(() => {
        const componentRef = this.blockRefs[index];
        console.log(`ContentEditor: Попытка фокусировки на блоке ${index}. Реф найден:`, !!componentRef);

        if (componentRef) {
          // 1. Предпочтительный способ: вызвать метод focusEditor у компонента блока
          if (typeof componentRef.focusEditor === 'function') {
            console.log(`ContentEditor: Вызов focusEditor для компонента ${index}`);
            componentRef.focusEditor(); // Предполагается, что блок реализует этот метод
          }
          // 2. Запасной способ: сфокусироваться на корневом элементе компонента блока
          else if (componentRef.$el && typeof componentRef.$el.focus === 'function') {
            console.warn(`ContentEditor: Блок ${index} (${componentRef.$options.name}) не имеет метода focusEditor. Фокусировка на корневом $el.`);
            componentRef.$el.focus({ preventScroll: false }); // Фокусируем сам элемент
            // Дополнительно прокручиваем к элементу
            componentRef.$el.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
          }
          // 3. Не удалось сфокусироваться
          else {
            console.warn(`ContentEditor: Не удалось программно сфокусироваться на блоке ${index} (нет focusEditor или фокусируемого $el).`);
            this.scrollToInput(); // Возвращаемся к полю ввода
          }
        } else {
          console.warn(`ContentEditor: Не найден реф компонента для индекса ${index}. Невозможно сфокусироваться.`);
          this.scrollToInput(); // Возвращаемся к полю ввода
        }
      });
    },

    /**
     * Прокручивает к компоненту EditorInput.
     */
    scrollToInput() {
      nextTick(() => {
        this.$refs.editorInputRef?.scrollToView();
      });
    },

    /**
     * Очищает поле ввода EditorInput.
     */
    clearEditorInput() {
      this.$refs.editorInputRef?.clearInput();
    },
  }
};
</script>

<style scoped>
.content-blocks {
  padding-bottom: 1rem;
}

.drag-handle {
  color: #888;
  transition: opacity 0.2s ease;
}

.drag-handle:hover {
  color: #333;
}

.drag-active {
  opacity: 0.8;
}

/* Анимация для перетаскиваемых элементов */
.flip-list-move {
  transition: transform 0.2s;
}

/* Стиль для состояния перетаскивания */
.sortable-chosen {
  opacity: 0.8;
  background-color: rgba(0, 0, 0, 0.03);
  border-radius: 4px;
}

/* Стиль для места, куда можно перетащить элемент */
.sortable-ghost {
  opacity: 0.5;
  background-color: rgba(0, 0, 0, 0.05);
  border-radius: 4px;
}
</style>


<template>
  <div class="relative">
    <!-- Кнопка меню -->
    <button
      @click.stop="toggleMenu"
      class="text-main hover:text-main-hover focus:outline-none p-1 opacity-0 group-hover:opacity-100 transition-opacity duration-200"
      aria-label="Опции"
    >
      <EllipsisHorizontalIcon class="w-5 h-5" />
    </button>

    <!-- Выпадающее меню -->
    <Transition name="fade">
      <div
        v-if="isOpen"
        ref="menuRef"
        class="absolute right-0 top-full mt-1 bg-white border border-gray-200 rounded-md shadow-lg z-10 w-48 py-1"
      >
        <button
          v-if="showCopy"
          @click="handleAction('copy')"
          class="flex items-center w-full px-3 py-2 text-sm text-left text-gray-700 hover:bg-gray-100 hover:text-gray-900 transition-colors"
        >
          <DocumentDuplicateIcon class="w-4 h-4 mr-3 flex-shrink-0" />
          Копировать
        </button>
        <button
          v-if="showDownload"
          @click="handleAction('download')"
          class="flex items-center w-full px-3 py-2 text-sm text-left text-gray-700 hover:bg-gray-100 hover:text-gray-900 transition-colors"
        >
          <ArrowDownTrayIcon class="w-4 h-4 mr-3 flex-shrink-0" />
          Скачать
        </button>
        <!-- Новая кнопка Редактировать -->
        <button
          v-if="showEdit"
          @click="handleAction('edit')"
          class="flex items-center w-full px-3 py-2 text-sm text-left text-gray-700 hover:bg-gray-100 hover:text-gray-900 transition-colors"
        >
          <PencilIcon class="w-4 h-4 mr-3 flex-shrink-0" /> 
          Редактировать
        </button>
        <button
          v-if="showDelete"
          @click="handleAction('delete')"
          class="flex items-center w-full px-3 py-2 text-sm text-left text-red-600 hover:bg-red-50 hover:text-red-700 transition-colors"
        >
          <TrashIcon class="w-4 h-4 mr-3 flex-shrink-0" />
          Удалить
        </button>
        <slot></slot> 
      </div>
    </Transition>
  </div>
</template>

<script>
import {
  EllipsisHorizontalIcon,
  DocumentDuplicateIcon,
  ArrowDownTrayIcon,
  PencilIcon, // <-- Импортируем новую иконку
  TrashIcon,
} from "@heroicons/vue/24/outline"; // Или /20/solid, /24/solid в зависимости от стиля

export default {
  name: "OptionsMenu",
  components: {
    EllipsisHorizontalIcon,
    DocumentDuplicateIcon,
    ArrowDownTrayIcon,
    PencilIcon, // <-- Регистрируем иконку
    TrashIcon,
  },
  props: {
    showCopy: {
      type: Boolean,
      default: true,
    },
    showDownload: {
      type: Boolean,
      default: true,
    },
    // Новый prop для управления видимостью кнопки редактирования
    showEdit: {
      type: Boolean,
      default: true, // По умолчанию показываем кнопку
    },
    showDelete: {
      type: Boolean,
      default: true,
    },
  },
  // Хорошая практика - объявить события, которые компонент может генерировать
  emits: ['copy', 'download', 'edit', 'delete'],
  data() {
    return {
      isOpen: false,
    };
  },
  mounted() {
    document.addEventListener("click", this.handleClickOutside);
  },
  // В Vue 3 лучше использовать beforeUnmount
  beforeUnmount() {
    document.removeEventListener("click", this.handleClickOutside);
  },
  methods: {
    toggleMenu() {
      this.isOpen = !this.isOpen;
    },
    closeMenu() {
      this.isOpen = false;
    },
    handleClickOutside(event) {
      // Проверяем, что клик был вне кнопки-триггера И вне открытого меню
      const triggerButton = this.$el.querySelector('button'); // Получаем кнопку-триггер
      if (
        this.$refs.menuRef &&
        !this.$refs.menuRef.contains(event.target) &&
        !triggerButton.contains(event.target) && // Добавляем проверку на кнопку-триггер
        this.isOpen
      ) {
        this.closeMenu();
      }
    },
    handleAction(action) {
      this.$emit(action); // Генерируем событие с именем действия ('copy', 'download', 'edit', 'delete')
      this.closeMenu(); // Закрываем меню после выбора действия
    },
  },
};
</script>

<style scoped>
/* Стили для анимации остаются прежними */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease; /* Добавляем transform */
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(-5px); /* Небольшой сдвиг вверх при исчезании/появлении */
}

/* Небольшие улучшения стилей кнопок */
button {
  cursor: pointer;
}
/* Убедимся, что иконки не сжимаются, если текст длинный */
button svg {
  flex-shrink: 0;
}
</style>
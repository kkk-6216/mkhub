<template>
    <div class="relative">
      <!-- Кнопка меню -->
      <button 
        @click.stop="toggleMenu" 
        class="text-main hover:text-main-hover focus:outline-none p-1 opacity-0 group-hover:opacity-100 transition-opacity duration-200"
        aria-label="Опции"
      >
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"></path>
        </svg>
      </button>
  
      <!-- Выпадающее меню -->
      <Transition name="fade">
        <div 
          v-if="isOpen" 
          ref="menuRef"
          class="absolute right-0 top-full mt-1 bg-white border border-gray-200 rounded-md shadow-lg z-10 w-48"
        >
          <button 
            v-if="showCopy"
            @click="handleAction('copy')" 
            class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white"
          >
            <CopyIcon class="w-5 h-5 mr-3"/>
            Копировать
          </button>
          <button 
            v-if="showDownload"
            @click="handleAction('download')" 
            class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white"
          >
            <DownloadIcon class="w-5 h-5 mr-3"/>
            Скачать
          </button>
          <button 
            v-if="showDelete"
            @click="handleAction('delete')" 
            class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white"
          >
            <DeleteIcon class="w-5 h-5 mr-3"/>
            Удалить
          </button>
          <slot></slot>
        </div>
      </Transition>
    </div>
  </template>
  
  <script>
  import CopyIcon from "@/modules/content/components/icons/CopyIcon.vue";
  import DeleteIcon from "@/modules/content/components/icons/DeleteIcon.vue";
  import DownloadIcon from "@/modules/content/components/icons/DownloadIcon.vue";
  
  export default {
    name: "OptionsMenu",
    components: {
      CopyIcon,
      DeleteIcon,
      DownloadIcon
    },
    props: {
      showCopy: {
        type: Boolean,
        default: true
      },
      showDownload: {
        type: Boolean,
        default: true
      },
      showDelete: {
        type: Boolean,
        default: true
      }
    },
    data() {
      return {
        isOpen: false
      };
    },
    mounted() {
      document.addEventListener('click', this.handleClickOutside);
    },
    beforeDestroy() {
      document.removeEventListener('click', this.handleClickOutside);
    },
    methods: {
      toggleMenu() {
        this.isOpen = !this.isOpen;
      },
      closeMenu() {
        this.isOpen = false;
      },
      handleClickOutside(event) {
        if (this.$el && !this.$el.contains(event.target) && this.isOpen) {
          this.closeMenu();
        }
      },
      handleAction(action) {
        this.$emit(action);
        this.closeMenu();
      }
    }
  };
  </script>
  
  <style scoped>
  .fade-enter-active, .fade-leave-active {
    transition: opacity 0.2s ease;
  }
  .fade-enter-from, .fade-leave-to {
    opacity: 0;
  }
  </style>
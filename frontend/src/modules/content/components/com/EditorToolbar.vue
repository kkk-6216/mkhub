<!-- EditorToolbar.vue -->
<template>
    <div
      class="fixed bg-white shadow-md rounded-md flex space-x-2 items-center z-10 p-2"
      :style="position"
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
  </template>
  
  <script>
  export default {
    name: 'EditorToolbar',
    
    props: {
      editor: {
        type: Object,
        required: true
      },
      position: {
        type: Object,
        required: true
      },
      currentTextColor: {
        type: String,
        default: null
      }
    },
    
    data() {
      return {
        showColorPalette: false,
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
    
    emits: ['update:text-color'],
    
    mounted() {
      document.addEventListener('click', this.handleClickOutside);
    },
    
    beforeDestroy() {
      document.removeEventListener('click', this.handleClickOutside);
    },
    
    methods: {
      toggleColorPalette() {
        this.showColorPalette = !this.showColorPalette;
      },
      
      setTextColor(color) {
        this.editor.chain().focus().setColor(color).run();
        this.$emit('update:text-color', color);
        this.showColorPalette = false;
      },
      
      resetTextColor() {
        this.editor.chain().focus().unsetColor().run();
        this.$emit('update:text-color', null);
        this.showColorPalette = false;
      },
      
      handleClickOutside(event) {
        if (
          this.$el && 
          !this.$el.contains(event.target) &&
          (!this.$refs.colorPaletteRef || !this.$refs.colorPaletteRef.contains(event.target))
        ) {
          this.showColorPalette = false;
        }
      }
    }
  };
  </script>
<template>
  <div class="toolbar flex flex-wrap gap-2 mb-4 p-3 bg-white rounded-lg shadow-md sticky top-0 z-10 border border-gray-200">
    <!-- Formatting -->
    <div class="formatting-tools flex gap-1">
      <button @click="$emit('format', 'bold')" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Bold (Ctrl+B)">
        <BoldIcon class="h-4 w-4" />
      </button>
      <button @click="$emit('format', 'italic')" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Italic (Ctrl+I)">
        <ItalicIcon class="h-4 w-4" />
      </button>
      <button @click="$emit('format', 'strikethrough')" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Strikethrough">
        <StrikethroughIcon class="h-4 w-4" />
      </button>
      <button @click="$emit('format', 'blockquote')" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Blockquote">
        <ChatBubbleOvalLeftEllipsisIcon class="h-5 w-5" />
      </button>
      <button @click="$emit('format', 'inline-code')" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Inline Code">
        <CodeBracketIcon class="h-5 w-5" />
      </button>
      <button @click="$emit('format', 'underline')" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Underline">
        <UnderlineIcon class="h-5 w-5" />
      </button>
    </div>
    
    <div class="separator h-8 w-px bg-gray-300 mx-1"></div>

    <!-- Font Size -->
    <div class="relative inline-block" ref="fontSizeDropdownContainer">
      <button @click="showFontSizeDropdown = !showFontSizeDropdown" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Font Size">
        <ChevronUpDownIcon class="h-6 w-6" /> <!-- Changed icon to indicate dropdown -->
      </button>
      <div v-if="showFontSizeDropdown" class="absolute left-0 mt-1 w-48 bg-white rounded-md shadow-lg z-20 border border-gray-200">
        <div class="py-1">
          <button v-for="size in fontSizes" :key="size.value"
                  @click="$emit('font-size', size.value); showFontSizeDropdown = false"
                  class="block w-full text-left px-4 py-2 text-sm hover:bg-blue-50 transition-colors">
            {{ size.name }}
          </button>
          <button @click="$emit('font-size-reset'); showFontSizeDropdown = false"
                  class="block w-full text-left px-4 py-2 text-sm text-red-500 hover:bg-red-50 border-t border-gray-200 transition-colors">
            Reset Size
          </button>
        </div>
      </div>
    </div>

    <!-- Headings -->
    <div class="heading-tools flex gap-1">
      <button @click="$emit('heading', 1)" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Heading 1">
        <span class="font-bold text-sm">H1</span>
      </button>
      <button @click="$emit('heading', 2)" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Heading 2">
        <span class="font-bold text-sm">H2</span>
      </button>
      <button @click="$emit('heading', 3)" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Heading 3">
        <span class="font-bold text-sm">H3</span>
      </button>
      <div class="relative inline-block" ref="headingDropdownContainer">
        <button @click="showHeadingDropdown = !showHeadingDropdown" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="More Headings">
          <EllipsisHorizontalIcon class="h-6 w-6" />
        </button>
        <div v-if="showHeadingDropdown" class="absolute left-0 mt-1 w-36 bg-white rounded-md shadow-lg z-20 border border-gray-200">
          <div class="py-1">
            <button @click="$emit('heading', 4); showHeadingDropdown = false" class="block w-full text-left px-4 py-2 text-sm hover:bg-blue-50 transition-colors">Heading 4</button>
            <button @click="$emit('heading', 5); showHeadingDropdown = false" class="block w-full text-left px-4 py-2 text-sm hover:bg-blue-50 transition-colors">Heading 5</button>
            <button @click="$emit('heading', 6); showHeadingDropdown = false" class="block w-full text-left px-4 py-2 text-sm hover:bg-blue-50 transition-colors">Heading 6</button>
          </div>
        </div>
      </div>
    </div>

    <div class="separator h-8 w-px bg-gray-300 mx-1"></div>

    <!-- Text Alignment -->
    <div class="alignment-tools flex gap-1">
      <button @click="$emit('align', 'left')" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Align Left">
        <Bars3BottomLeftIcon class="h-5 w-5" />
      </button>
      <button @click="$emit('align', 'center')" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Align Center">
        <Bars3Icon class="h-5 w-5" /> <!-- Using Bars3Icon for center -->
      </button>
      <button @click="$emit('align', 'right')" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Align Right">
        <Bars3BottomRightIcon class="h-5 w-5" />
      </button>
    </div>

    <div class="separator h-8 w-px bg-gray-300 mx-1"></div>

    <!-- Lists -->
    <div class="list-tools flex gap-1">
      <button @click="$emit('list', 'unordered')" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Unordered List">
        <ListBulletIcon class="h-5 w-5" />
      </button>
      <button @click="$emit('list', 'ordered')" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Ordered List">
        <NumberedListIcon class="h-5 w-5" /> <!-- Better icon for ordered list -->
      </button>
    </div>

    <div class="separator h-8 w-px bg-gray-300 mx-1"></div>

    <!-- Insertions -->
    <div class="insert-tools flex gap-1 items-center">
      <button @click="$emit('insert', 'link')" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Insert Link">
        <LinkIcon class="h-5 w-5" />
      </button>
      <button @click="$emit('insert', 'table')" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Insert Table">
        <TableCellsIcon class="h-5 w-5" />
      </button>
      <button @click="$emit('insert', 'hr')" class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors" title="Horizontal Rule">
        <MinusIcon class="h-5 w-5" />
      </button>

      <!-- Color Picker -->
      <div class="relative" ref="colorPaletteContainer">
        <button
          class="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg flex items-center justify-center transition-colors relative"
          @click.prevent="toggleColorPalette"
          title="Text Color"
        >
          <SwatchIcon class="h-5 w-5" /> <!-- Changed icon for color -->
        </button>
        <!-- Color Palette Dropdown -->
        <div
          v-if="showColorPalette"
          ref="colorPaletteRef"
          class="absolute left-0 mt-1 p-3 bg-white shadow-md rounded-md z-20 w-max border border-gray-200"
          @mousedown.prevent
        >
          <div class="grid grid-cols-5 gap-2">
            <button
              v-for="color in colorPalette"
              :key="color.value || 'reset'"
              class="w-6 h-6 rounded-full cursor-pointer border border-gray-200 hover:border-gray-400 transition-all"
              :style="`background-color: ${color.value || '#ffffff'}; ${color.value ? '' : 'background-image: linear-gradient(45deg, #ccc 25%, transparent 25%, transparent 75%, #ccc 75%, #ccc), linear-gradient(45deg, #ccc 25%, transparent 25%, transparent 75%, #ccc 75%, #ccc); background-size: 10px 10px; background-position: 0 0, 5px 5px;'}`"
              :title="color.name"
              @click="$emit('color', color.value)"
            ></button>
          </div>
          <button
            @click="$emit('color-reset')"
            class="w-full mt-2 px-3 py-1 text-xs text-center rounded bg-gray-100 hover:bg-gray-200 text-gray-700 transition-colors"
          >
            Reset Color
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// Import icons from heroicons
import {
  BoldIcon,
  ItalicIcon,
  StrikethroughIcon,
  ChatBubbleOvalLeftEllipsisIcon,
  CodeBracketIcon,
  UnderlineIcon,
  ChevronUpDownIcon, // Used for Font Size dropdown indication
  EllipsisHorizontalIcon,
  Bars3BottomLeftIcon,
  Bars3Icon, // Using this for Center Align (alternative to specific center align icon)
  Bars3BottomRightIcon,
  ListBulletIcon,
  NumberedListIcon, // Used for Ordered List
  LinkIcon,
  TableCellsIcon,
  MinusIcon,
  SwatchIcon, // Used for Color Picker
} from '@heroicons/vue/24/outline'; // Using 24px outline icons

export default {
  name: 'FormattingToolbar',
  components: {
    // Register imported icons
    BoldIcon,
    ItalicIcon,
    StrikethroughIcon,
    ChatBubbleOvalLeftEllipsisIcon,
    CodeBracketIcon,
    UnderlineIcon,
    ChevronUpDownIcon,
    EllipsisHorizontalIcon,
    Bars3BottomLeftIcon,
    Bars3Icon,
    Bars3BottomRightIcon,
    ListBulletIcon,
    NumberedListIcon,
    LinkIcon,
    TableCellsIcon,
    MinusIcon,
    SwatchIcon,
  },
  data() {
    return {
      showHeadingDropdown: false,
      showColorPalette: false,
      showFontSizeDropdown: false,
      colorPalette: [
        { name: 'Reset', value: null },
        { name: 'Black', value: '#000000' },
        { name: 'Gray', value: '#6B7280' },
        { name: 'Red', value: '#EF4444' },
        { name: 'Orange', value: '#F97316' },
        { name: 'Yellow', value: '#F59E0B' },
        { name: 'Green', value: '#10B981' },
        { name: 'Blue', value: '#3B82F6' },
        { name: 'Purple', value: '#8B5CF6' },
        { name: 'Pink', value: '#EC4899' },
      ],
      fontSizes: [
        { name: 'Tiny (10px)', value: '10px' },
        { name: 'Small (12px)', value: '12px' },
        { name: 'Normal (16px)', value: '16px' },
        { name: 'Medium (18px)', value: '18px' },
        { name: 'Large (24px)', value: '24px' },
        { name: 'X-Large (30px)', value: '30px' },
        { name: 'XX-Large (36px)', value: '36px' },
      ]
    }
  },
  methods: {
    toggleColorPalette() {
      this.showColorPalette = !this.showColorPalette;
      if (this.showColorPalette) {
        this.showHeadingDropdown = false;
      }
    }
  },
  emits: [
    'format',
    'heading',
    'align',
    'list',
    'insert',
    'color',
    'color-reset',
    'font-size',
    'font-size-reset'
  ]
}
</script>

<style>
.toolbar button {
  transition: all 0.15s ease;
}

.toolbar button:hover {
  transform: translateY(-1px);
}

.separator {
  margin: 0 4px;
}
</style>
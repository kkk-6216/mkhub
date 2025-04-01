<template>
  <div class="relative w-full" @mouseover="showPlus" @mouseleave="hidePlus">
    <div
        ref="editor"
        class="w-full p-4 focus:outline-none"
        contenteditable="true"
        @input="handleInput"
        @click="handleClick"
        @keydown="handleKeyDown"
        placeholder="Type '/' for commands..."
    ></div>

    <div
        v-if="showCommandMenu"
        class="absolute left-0 bg-white border border-gray-200 rounded-md shadow-lg w-64 z-50"
        :style="{ top: commandMenuTop }"
    >
      <div class="p-2 space-y-1">
        <div v-if="filteredCommands.length > 0">
          <button
              v-for="command in filteredCommands"
              :key="command.text"
              class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white"
              @click="command.action"
          >
            <span class="flex items-center justify-center w-6 h-6 mr-2 hover:text-white">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path :d="command.icon" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/>
              </svg>
            </span>
            <span>
              <span  class="text-sm font-medium">{{ command.label }}</span>
            </span>
          </button>
        </div>
        <div v-else class="p-2 text-sm text-red-500">
          No matching commands found. Type /image or /file
        </div>
      </div>
    </div>

    <input type="file" ref="imageInput" @change="handleImageUpload" accept="image/*" class="hidden" />
    <input type="file" ref="fileInput" @change="handleFileUpload" class="hidden" />

    <component v-for="(block, index) in contentBlocks" :key="index" :is="block.component" :data="block.data" @delete="deleteBlock(index)" :index="index"/>
  </div>
</template>

<script>
import ImageBlock from './components/ImageBlock.vue';
import FileBlock from './components/FileBlock.vue';

export default {
  name: "ContentEditor",
  components: { ImageBlock, FileBlock },
  data() {
    return {
      showCommandMenu: false,
      commandText: '',
      isCommandTyping: false,
      contentBlocks: [],
      commands: [
        { text: '/image', label: 'Image', icon: 'M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z', action: () => this.$refs.imageInput.click() },
        { text: '/file', label: 'File', icon: 'M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13', action: () => this.$refs.fileInput.click() }
      ],
      showError: false
    };
  },
  computed: {
    filteredCommands() {
      if (this.commandText.length < 1) return this.commands;

      const filtered = this.commands.filter(cmd =>
          cmd.text.toLowerCase().includes(this.commandText.toLowerCase())
      );

      this.showError = filtered.length === 0;
      return filtered;
    }
  },
  methods: {
    handleInput(event) {
      if (this.isCommandTyping) {
        this.commandText = event.target.innerText.trim();
        this.showCommandMenu = this.commandText.startsWith('/');
      }
    },
    handleKeyDown(event) {
      if (event.key === '/' && !this.isCommandTyping) {
        this.isCommandTyping = true;
        this.commandText = '/';
        this.showCommandMenu = true;
        document.execCommand('insertText', false, '/');
        event.preventDefault();
      } else if (event.key === 'Escape') {
        this.resetCommandMenu();
      } else if (event.key === 'Enter' && this.showError) {
        // При нажатии Enter с несуществующей командой
        event.preventDefault();
        this.resetCommandMenu();
      }
    },
    handleImageUpload(event) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.contentBlocks.push({
            component: 'image-block',
            data: { src: e.target.result, caption: 'Uploaded Image' }
          });
        };
        reader.readAsDataURL(file);
      }
      this.resetCommandMenu();
    },
    handleFileUpload(event) {
      const file = event.target.files[0];
      if (file) {
        setTimeout(() => {
          const url = URL.createObjectURL(file);
          this.contentBlocks.push({
            component: 'file-block',
            data: { name: file.name, size: file.size, fileUrl: url }
          });
        }, 1000);
      }
      this.resetCommandMenu();
    },
    resetCommandMenu() {
      this.showCommandMenu = false;
      this.isCommandTyping = false;
      this.commandText = '';
      this.$refs.editor.textContent = '';
      this.showError = false;
    },
    deleteBlock(index) {
      this.contentBlocks.splice(index, 1);
    }
  }
};
</script>

<style scoped>
[contenteditable][placeholder]:empty:before {
  content: attr(placeholder);
  color: #a0aec0;
  cursor: text;
  position: absolute;
  pointer-events: none;
}
</style>
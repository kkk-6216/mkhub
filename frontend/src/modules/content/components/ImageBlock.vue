<template>
  <div class="mb-4 p-4 rounded-md hover:bg-gray-100 relative group">
    <img :src="data.src" alt="Image" class="max-w-full h-auto">
    <p v-if="data.caption" class="text-sm text-gray-500">{{ data.caption }}</p>

    <!-- Options Menu -->
    <div class="absolute top-1 right-1 opacity-0 group-hover:opacity-100 transition-opacity duration-200">
      <button @click.stop="toggleOptions" class="text-gray-500 hover:text-gray-700 focus:outline-none">
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"></path>
        </svg>
      </button>

      <div v-if="showOptions" class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg z-10">
        <a href="#" @click.prevent="handleDelete" class="block py-2 px-4 text-sm text-gray-700 hover:bg-gray-100">Delete</a>
        <a href="#" @click.prevent="handleCopy" class="block py-2 px-4 text-sm text-gray-700 hover:bg-gray-100">Copy Image URL</a>
        <a href="#" @click.prevent="handleDownload" class="block py-2 px-4 text-sm text-gray-700 hover:bg-gray-100">Download</a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ImageBlock",
  props: {
    data: {
      type: Object,
      required: true,
    },
    index: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      showOptions: false,
    };
  },
  methods: {
    toggleOptions() {
      this.showOptions = !this.showOptions;
    },
    handleDelete() {
      this.$emit('delete', this.index);
      this.showOptions = false;
    },
    handleCopy() {
      navigator.clipboard.writeText(this.data.src)
          .then(() => {
            alert('Image URL copied to clipboard!');
          })
          .catch(err => {
            console.error('Failed to copy image URL: ', err);
            alert('Failed to copy image URL: ' + err);
          });
      this.showOptions = false;
    },
    handleDownload() {
      const link = document.createElement('a');
      link.href = this.data.src;
      link.download = 'image.png'; // Or use a more descriptive name, if available
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      this.showOptions = false;
    },
  },
};
</script>


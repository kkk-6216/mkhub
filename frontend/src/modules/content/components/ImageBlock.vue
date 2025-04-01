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


      <div v-if="showOptions" class="absolute right-0 bg-white border border-gray-200 rounded-md shadow-lg w-50">
        <!-- Delete Option -->
        <button @click.prevent="handleDelete" class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white">
          <DeleteIcon class="w-5 h-5 mr-3"/>
          Удалить
        </button>
        <!-- Copy Image URL Option -->
        <button @click.prevent="handleCopy" class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white">
          <CopyIcon class="w-5 h-5 mr-3"/>
          Копировать
        </button>
        <!-- Download Option -->
        <button @click.prevent="handleDownload" class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white">
          <DownloadIcon class="w-5 h-5 mr-3"/>
          Скачать
        </button>
      </div>
    </div>
  </div>
</template>


<script>
import CopyIcon from '@/modules/content/components/icons/CopyIcon.vue';
import DeleteIcon from '@/modules/content/components/icons/DeleteIcon.vue';
import DownloadIcon from '@/modules/content/components/icons/DownloadIcon.vue';


export default {
  name: "ImageBlock",
  components: {
    CopyIcon,
    DeleteIcon,
    DownloadIcon
  },
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


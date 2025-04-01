<template>
  <div class="flex items-center p-2 hover:bg-gray-100 transition-colors duration-200 relative group">
    <svg class="w-6 h-6 mr-2 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z"></path>
    </svg>
    <div class="flex items-center">
      <a :href="data.fileUrl || '#'" @click.prevent="openFile" class="font-medium text-main hover:underline">{{ data.name }}</a>
      <p class="text-gray-500 text-xs ml-2">{{ formatFileSize(data.size) }}</p>
    </div>


    <div class="absolute top-1 right-1 opacity-0 group-hover:opacity-100 transition-opacity duration-200">
      <button @click.stop="toggleOptions" class="text-main hover:text-main-hover focus:outline-none">
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"></path>
        </svg>
      </button>

      <div v-if="showOptions" class="absolute right-0 bg-white border border-gray-200 rounded-md shadow-lg w-50">
        <!-- Delete Option -->
        <button @click.prevent="handleDelete" class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white">
          <DeleteIcon class="w-5 h-5 mr-3"/>
          Delete
        </button>
        <!-- Copy Image URL Option -->
        <button @click.prevent="handleCopy" class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white">
          <CopyIcon class="w-5 h-5 mr-3"/>
          Copy Image URL
        </button>
        <!-- Download Option -->
        <button @click.prevent="handleDownload" class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white">
          <DownloadIcon class="w-5 h-5 mr-3"/>
          Download
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import DownloadIcon from "@/modules/content/components/icons/DownloadIcon.vue";
import DeleteIcon from "@/modules/content/components/icons/DeleteIcon.vue";
import CopyIcon from "@/modules/content/components/icons/CopyIcon.vue";

export default {
  name: "FileBlock",
  components: {
    CopyIcon,
    DeleteIcon,
    DownloadIcon},
  props: {
    data: {
      type: Object,
      required: true
    },
    index: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      showOptions: false
    };
  },
  methods: {
    formatFileSize(bytes) {
      if (bytes === 0) return '0 Bytes';
      const k = 1024;
      const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    },
    openFile() {
      if (this.data.fileUrl) {
        window.open(this.data.fileUrl, '_blank');
      } else if (this.data.file) {
        const url = URL.createObjectURL(this.data.file);
        window.open(url, '_blank');
        URL.revokeObjectURL(url);
      } else {
        alert("File cannot be opened. No file data or URL available.");
      }
    },
    toggleOptions() {
      this.showOptions = !this.showOptions;
    },
    handleDelete() {
      this.$emit('delete', this.index); // Emit the delete event with the index of the component
      this.showOptions = false; // Close the options menu
    },
    handleCopy() {
      if (this.data.fileUrl) {
        navigator.clipboard.writeText(this.data.fileUrl)
            .then(() => {
              alert('Link copied to clipboard!');
            })
            .catch(err => {
              console.error('Failed to copy link: ', err);
              alert('Failed to copy link: ' + err);
            });
      } else {
        alert('No file URL to copy.');
      }
      this.showOptions = false;
    },
    handleDownload() {
      if (this.data.fileUrl) {
        const link = document.createElement('a');
        link.href = this.data.fileUrl;
        link.download = this.data.name; // Set the filename for download
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link); // Clean up
      } else if (this.data.file) {
        // Handle download of a local file
        const url = URL.createObjectURL(this.data.file);
        const link = document.createElement('a');
        link.href = url;
        link.download = this.data.name;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(url); // Clean up
      } else {
        alert('No file available for download.');
      }
      this.showOptions = false;
    }
  }
};
</script>
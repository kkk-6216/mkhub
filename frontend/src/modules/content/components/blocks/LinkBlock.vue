<template>
  <div class="link-block rounded-md border border-gray-200 p-3 my-2 flex items-start hover:bg-gray-50 transition-colors">
    <!-- Icon section -->
    <div class="flex-shrink-0 mr-3">
      <div class="w-8 h-8 rounded-md bg-blue-100 flex items-center justify-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-500" viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd" d="M12.586 4.586a2 2 0 112.828 2.828l-3 3a2 2 0 01-2.828 0 1 1 0 00-1.414 1.414 4 4 0 005.656 0l3-3a4 4 0 00-5.656-5.656l-1.5 1.5a1 1 0 101.414 1.414l1.5-1.5zm-5 5a2 2 0 012.828 0 1 1 0 101.414-1.414 4 4 0 00-5.656 0l-3 3a4 4 0 105.656 5.656l1.5-1.5a1 1 0 10-1.414-1.414l-1.5 1.5a2 2 0 11-2.828-2.828l3-3z" clip-rule="evenodd" />
        </svg>
      </div>
    </div>
    
    <!-- Content section -->
    <div class="flex-grow min-w-0">
      <!-- Title and edit/delete buttons row -->
      <div class="flex items-center justify-between mb-1">
        <div class="flex items-center min-w-0">
          <input 
            v-if="isEditing" 
            ref="titleInput" 
            v-model="editedTitle" 
            class="font-medium text-gray-900 focus:outline-none border-b border-blue-500 w-full" 
            @blur="finishTitleEdit"
            @keydown.enter="finishTitleEdit"
          />
          <h3 
            v-else 
            class="font-medium text-gray-900 truncate cursor-pointer" 
            @click="startTitleEdit"
          >
            {{ data.title || 'Untitled Link' }}
          </h3>
        </div>
        
        <div class="flex-shrink-0 flex space-x-1">
          <button 
            @click="toggleEdit" 
            class="text-gray-400 hover:text-gray-700 p-1 rounded"
            title="Edit link"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
              <path d="M13.586 3.586a2 2 0 112.828 2.828l-7.586 7.586a2 2 0 01-1.414.586H5V12.586a2 2 0 01.586-1.414l7.586-7.586z" />
              <path d="M11 18h3.586a2 2 0 001.414-.586l3.707-3.707a1 1 0 00-1.414-1.414L15 15.586V18h-4v-2.585l3.293-3.293a1 1 0 00-1.414-1.414L11 12.586V18z" />
            </svg>
          </button>
          <button 
            @click="$emit('delete')" 
            class="text-gray-400 hover:text-red-500 p-1 rounded"
            title="Delete link"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clip-rule="evenodd" />
            </svg>
          </button>
        </div>
      </div>
      
      <!-- URL input/display -->
      <div class="mb-1">
        <input 
          v-if="isEditing" 
          ref="urlInput"
          v-model="editedUrl" 
          placeholder="https://example.com" 
          class="text-sm text-blue-600 w-full focus:outline-none border-b border-blue-500" 
          @blur="updateUrl"
          @keydown.enter="updateUrl"
        />
        <a 
          v-else 
          :href="data.url" 
          target="_blank" 
          rel="noopener noreferrer"
          class="text-sm text-blue-600 hover:underline truncate block"
        >
          {{ displayUrl }}
        </a>
      </div>
      
      <!-- Description -->
      <div v-if="isEditing || data.description">
        <textarea 
          v-if="isEditing" 
          v-model="editedDescription" 
          rows="2"
          placeholder="Add a description..."
          class="text-sm text-gray-600 w-full focus:outline-none border-b border-blue-500 resize-none"
          @blur="updateDescription"
          @keydown.enter.prevent="updateDescription"
        ></textarea>
        <p 
          v-else 
          class="text-sm text-gray-600 line-clamp-2"
        >
          {{ data.description }}
        </p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "LinkBlock",
  props: {
    data: {
      type: Object,
      default: () => ({
        title: '',
        url: '',
        description: ''
      })
    },
    index: {
      type: Number,
      required: true
    }
  },
  emits: ['delete', 'update', 'request-new-block-after'],
  data() {
    return {
      isEditing: false,
      editedTitle: '',
      editedUrl: '',
      editedDescription: ''
    };
  },
  computed: {
    displayUrl() {
      if (!this.data.url) return 'No URL';
      
      try {
        const url = new URL(this.data.url);
        // Truncate the URL for display if needed
        return url.hostname + (url.pathname !== '/' ? url.pathname : '');
      } catch (e) {
        return this.data.url;
      }
    }
  },
  methods: {
    toggleEdit() {
      this.isEditing = !this.isEditing;
      if (this.isEditing) {
        this.editedTitle = this.data.title || '';
        this.editedUrl = this.data.url || '';
        this.editedDescription = this.data.description || '';
        this.$nextTick(() => {
          this.$refs.titleInput?.focus();
        });
      }
    },
    
    startTitleEdit() {
      this.isEditing = true;
      this.editedTitle = this.data.title || '';
      this.$nextTick(() => {
        this.$refs.titleInput?.focus();
      });
    },
    
    finishTitleEdit() {
      if (this.editedTitle !== this.data.title) {
        this.$emit('update', {
          index: this.index,
          newData: { title: this.editedTitle }
        });
      }
    },
    
    updateUrl() {
      if (this.editedUrl !== this.data.url) {
        this.$emit('update', {
          index: this.index,
          newData: { url: this.editedUrl }
        });
      }
    },
    
    updateDescription() {
      if (this.editedDescription !== this.data.description) {
        this.$emit('update', {
          index: this.index, 
          newData: { description: this.editedDescription }
        });
      }
    },
    
    focusEditor() {
      if (this.isEditing) {
        this.$refs.titleInput?.focus();
      } else {
        this.startTitleEdit();
      }
    }
  }
};
</script>
<template>
    <div
      ref="menuRef"
      class="absolute left-0 bg-white rounded-md shadow-lg w-52 z-30"
    >
      <div class="p-1 max-h-60 overflow-y-auto">
        <template v-if="commands.length">
          <button
            v-for="(command, index) in commands"
            :key="command.text"
            :ref="(el) => { if (el) commandRefs[index] = el; }"
            :class="{ 'bg-main text-white': index === activeIndex }"
            class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white"
            @click="$emit('command-selected', command)"
            @mouseover="$emit('set-active-index', index)"
          >
            <span
              class="flex items-center justify-center w-6 h-6 mr-2 flex-shrink-0"
              :class="{ 'text-white': index === activeIndex, 'hover:text-white': index !== activeIndex }"
            >
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path :d="command.icon" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/>
              </svg>
            </span>
            <span class="font-medium">{{ command.label }}</span>
          </button>
        </template>
        <div v-else class="p-2 text-sm text-red-500">
          Нет подходящих команд.
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'CommandMenu',
    props: {
      commands: {
        type: Array,
        required: true
      },
      activeIndex: {
        type: Number,
        default: 0
      }
    },
    emits: ['command-selected', 'set-active-index'],
    data() {
      return {
        commandRefs: []
      }
    },
    watch: {
      activeIndex() {
        this.scrollActiveCommandIntoView();
      }
    },
    beforeUpdate() {
      this.commandRefs = [];
    },
    methods: {
      scrollActiveCommandIntoView() {
        this.$nextTick(() => {
          this.commandRefs[this.activeIndex]?.scrollIntoView({
            block: 'nearest'
          });
        });
      }
    }
  }
  </script>
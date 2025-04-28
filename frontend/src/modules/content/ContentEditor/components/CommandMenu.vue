<template>
  <div
      ref="menuRef"
      v-bind="$attrs"
      class="absolute bg-white rounded-md shadow-lg z-30"
      :style="{ width: menuWidth }"
      role="listbox"
  >
    <div
        class="p-1 overflow-y-auto"
        :style="{ maxHeight: menuMaxHeight }"
    >
      <template v-if="commands.length">
        <button
            v-for="(command, index) in commands"
            :key="command.id ?? command.text"
            ref="commandRefs"
            class="flex items-center w-full p-2 text-left rounded hover:bg-main hover:text-white"
            :class="{ 'bg-main text-white': index === activeIndex }"
            @click="$emit('command-selected', command)"
            @mouseover="$emit('set-active-index', index)"
            role="option"
            :aria-selected="index === activeIndex"
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
  inheritAttrs: false,
  props: {
    commands: {
      type: Array,
      required: true
    },
    activeIndex: {
      type: Number,
      default: 0
    },
    menuWidth: {
      type: String,
      default: '13rem'
    },
    menuMaxHeight: {
      type: String,
      default: '15rem'
    }
  },
  emits: ['command-selected', 'set-active-index'],
  watch: {
    activeIndex() {
      this.scrollActiveCommandIntoView();
    }
  },
  mounted() {
    this.scrollActiveCommandIntoView();
  },
  methods: {
    scrollActiveCommandIntoView() {
      this.$nextTick(() => {
        const refs = this.$refs.commandRefs;
        const item = Array.isArray(refs) ? refs[this.activeIndex] : refs;
        item?.scrollIntoView({ block: 'nearest' });
      });
    }
  }
};
</script>

<style scoped>
.command-input-area [contenteditable] {
  min-height: 1.5em;
  line-height: 1.5;
}
</style>

<template>
  <div class="relative inline-block">
    <!-- Trigger button -->
    <button
        @click="toggleDropdown($event)"
        class="p-1 bg-gray-200 rounded-md hover:bg-gray-300"
    >
      <slot name="trigger">
        <!-- default trigger icon -->
        <svg class="w-4 h-4 text-dark" xmlns="http://www.w3.org/2000/svg" fill="none"
             viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round"
                d="M12 6.75a.75.75 0 1 1 0-1.5.75.75 0 0 1 0 1.5ZM12 12.75a.75.75 0 1 1 0-1.5.75.75 0 0 1 0 1.5ZM12 18.75a.75.75 0 1 1 0-1.5.75.75 0 0 1 0 1.5Z"/>
        </svg>
      </slot>
    </button>

    <!-- Dropdown menu -->
    <teleport to="body">
      <ul
          v-if="isOpen"
          v-click-outside="closeDropdown"
          class="absolute bg-white border border-gray-300 shadow-md rounded-md w-36 z-50"
          :style="{ top: position.top + 'px', left: position.left + 'px' }"
      >
        <slot  :close="closeDropdown">
          <li class="px-4 py-2 hover:bg-gray-100 cursor-pointer">Опция 1</li>
          <li class="px-4 py-2 hover:bg-gray-100 cursor-pointer">Опция 2</li>
        </slot>
      </ul>
    </teleport>
  </div>
</template>

<script>
export default {
  name: 'DropdownButton',
  data() {
    return {
      isOpen: false,
      position: { top: 0, left: 0 },
      triggerElement: null
    };
  },
  directives: {
    clickOutside: {
      beforeMount(el, binding) {
        el.__ClickOutsideHandler__ = event => {
          if (!(el === event.target || el.contains(event.target))) {
            binding.value(event);
          }
        };
        document.addEventListener('click', el.__ClickOutsideHandler__);
      },
      unmounted(el) {
        document.removeEventListener('click', el.__ClickOutsideHandler__);
        delete el.__ClickOutsideHandler__;
      }
    }
  },
  methods: {
    toggleDropdown(event) {
      const el = event.target.closest('button');
      if (!el) return;

      event.stopPropagation();

      this.isOpen = !this.isOpen;
      this.triggerElement = el;

      if (this.isOpen) {
        this.setPosition();
        window.addEventListener('scroll', this.setPosition);
        window.addEventListener('resize', this.setPosition);
      } else {
        this.cleanup();
      }
    },
    closeDropdown() {
      this.isOpen = false;
      this.cleanup();
    },
    cleanup() {
      window.removeEventListener('scroll', this.setPosition);
      window.removeEventListener('resize', this.setPosition);
    },
    setPosition() {
      this.$nextTick(() => {
        if (!this.triggerElement) return;
        const rect = this.triggerElement.getBoundingClientRect();
        this.position = {
          top: rect.top + window.scrollY + rect.height + 5,
          left: rect.left + window.scrollX - 100
        };
      });
    }
  },
  beforeUnmount() {
    this.cleanup();
  }
};
</script>

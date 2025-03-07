<template>
  <router-link :to="to" class="block">
    <li
        class="flex items-center p-2 rounded-lg pb-4 transition duration-200 font-light"
        :class="{
        'text-gray-400 hover:text-dark ': !isActive,
        'text-dark font-normal': isActive
      }"
    >

      <i v-if="$slots.icon" class="mdi mr-2" :class="{ [iconClass]: true, 'mr-0': isCollapsed }">
        <slot name="icon"></slot>
      </i>

      <span v-if="!isCollapsed">{{ label }}</span>
    </li>
  </router-link>
</template>

<script>
export default {
  props: {
    to: { type: String, required: true },
    label: { type: String, required: true },
    iconClass: { type: String, default: "", required: true },
    isCollapsed: { type: Boolean, default: false, required: true },
    isActive: { type: Boolean, default: false, required: true }
  }
};
</script>

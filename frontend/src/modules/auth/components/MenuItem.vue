<template>
  <li
    :class="[
      'flex',
      'items-center',
      'p-2',
      'rounded-lg',
      'pb-4',
      'text-gray-500',
      'hover:text-gray-950',
      'transition',
      'duration-200',
      { 'text-gray-950': isActive },
    ]"
  >
    <i class="mdi mr-2" :class="[iconClass, { 'mr-0': isCollapsed }]">
      <slot name="icon"></slot>
    </i>
    <router-link v-if="!isCollapsed" :to="to">{{ label }}</router-link>
  </li>
</template>

<script>
export default {
  props: {
    isCollapsed: Boolean,
    isActive: Boolean,
    iconClass: String,
    to: String,
    label: String,
  },
};
</script>

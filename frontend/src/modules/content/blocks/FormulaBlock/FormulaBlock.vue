<template>
  <div
    class="mb-2 p-4 rounded-md transition-colors duration-200 relative group"
    :class="{
      'bg-gray-100': isEditing,             // Серый фон при редактировании
      'bg-white hover:bg-gray-50': !isEditing // Белый фон при просмотре и серый при наведении
    }"
  >
    <!-- Верхняя панель редактирования -->
    <div v-if="isEditing" class="flex justify-between items-center mb-2">
      <h1 class="text-2xl font-bold text-gray-800 flex items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2 text-main" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" d="M4.745 3A23.933 23.933 0 0 0 3 12c0 3.183.62 6.22 1.745 9M19.5 3c.967 2.78 1.5 5.817 1.5 9s-.533 6.22-1.5 9M8.25 8.885l1.444-.89a.75.75 0 0 1 1.105.402l2.402 7.206a.75.75 0 0 0 1.104.401l1.445-.889m-8.25.75.213.09a1.687 1.687 0 0 0 2.062-.617l4.45-6.676a1.688 1.688 0 0 1 2.062-.618l.213.09" />
        </svg>
        Редактирование формулы
      </h1> 
      <EditorMenu
        @clear="clearEditor"
        @show-help="showHelpModal = true"
        @finish="finishEditing"
      />
    </div>

    <!-- Режим редактирования -->
    <div v-if="isEditing">
      <textarea
        v-model="formula"
        class="w-full p-2 border border-gray-300 rounded-lg bg-white shadow-sm font-mono text-sm"
        rows="3"
        placeholder="Введите LaTeX формулу"
      ></textarea>

      <div class="mt-4 p-2 rounded w-full h-20">
        <p class="text-sm text-gray-600 mb-1">Превью:</p>
        <div id="formula-preview" v-html="renderedFormula"></div>
      </div>
    </div>

    <!-- Режим просмотра -->
    <div v-else>
      <div id="formula-preview" class="text-black text-lg" v-html="renderedFormula"></div>

      <!-- Options Menu -->
      <div class="absolute top-2 right-2">
        <OptionsMenu
          @copy="copyFormula"
          @edit="isEditing = true"
          @download="downloadImage"
          @delete="$emit('delete', index)"
        />
      </div>
    </div>

    <HelpModal 
      v-if="showHelpModal" 
      @close="showHelpModal = false" 
    />
  </div>
</template>


<script>
import katex from 'katex';
import 'katex/dist/katex.min.css';
import html2canvas from 'html2canvas';
import OptionsMenu from '@/modules/content/blocks/components/OptionsMenu.vue';
import EditorMenu from '@/modules/content/blocks/components/EditorMenu.vue'; 
import HelpModal from '@/modules/content/blocks//FormulaBlock/components/HelpModal.vue';

export default {
  name: 'FormulaBlock',
  components: { OptionsMenu, EditorMenu, HelpModal },
  props: {
    data: {
      type: Object,
      required: true,
      validator: value => 'formula' in value
    },
    index: {
      type: Number,
      required: true
    },
    isActive: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      isEditing: true,
      formula: this.data.formula || '',
      showHelpModal: false
    };
  },
  computed: {
    renderedFormula() {
      try {
        return katex.renderToString(this.formula, { throwOnError: false });
      } catch (e) {
        return `<span class="text-red-500 text-sm">Ошибка в формуле</span>`;
      }
    }
  },
  watch: {
    formula(newFormula) {
      this.$emit('update', {
        index: this.index,
        newData: {
          formula: newFormula
        }
      });
    },
  },
  methods: {
    clearEditor() {
      this.formula = '';
    },
    finishEditing() {
      if (!this.formula.trim()) {
        this.$emit('delete', this.index);
      } else {
        this.isEditing = false;
      }
    },
    exitEditMode() {
      if (this.formula.trim()) {
        this.isEditing = false;
      } else {
        this.$emit('delete', this.index);
      }
    },
    copyFormula() {
      navigator.clipboard.writeText(this.formula)
        .then(() => this.$toast?.success('Формула скопирована') || alert('Формула скопирована'))
        .catch(() => this.$toast?.error('Ошибка копирования') || alert('Ошибка копирования'));
    },
    async downloadImage() {
      const el = document.querySelector(`#formula-preview`);
      if (!el) return;

      try {
        const canvas = await html2canvas(el, { backgroundColor: '#ffffff' });
        const link = document.createElement('a');
        link.href = canvas.toDataURL('image/png');
        link.download = 'formula.png';
        link.click();
      } catch (err) {
        console.error('Ошибка при сохранении изображения', err);
        this.$toast?.error('Ошибка при сохранении') || alert('Ошибка при сохранении');
      }
    },
  }
};
</script>

<style scoped>
.katex {
  font-size: 1.25rem;
}
</style>

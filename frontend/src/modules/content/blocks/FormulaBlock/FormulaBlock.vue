<template>
  <div class="mb-2 p-4 rounded-md bg-gray-100 transition-colors duration-200 relative group">
    <!-- Верхняя панель редактирования -->
    <div v-if="isEditing" class="flex justify-between items-center mb-2">
      <h3 class="text-lg font-semibold">Редактирование формулы</h3>
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

      <div class="mt-4 p-2  rounded w-full h-20">
        <p class="text-sm text-gray-600 mb-1">Превью:</p>
        <div id="formula-preview" v-html="renderedFormula" class="text-black text-lg"></div>
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

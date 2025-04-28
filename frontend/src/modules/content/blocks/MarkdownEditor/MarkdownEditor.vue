<template>
  <div :class="['mb-2', !isFinalView ? 'bg-gray-100 min-h-150 p-4 rounded-md' : 'final-view-active']">

    <!-- Editor Mode -->
    <template v-if="!isFinalView">

      <div class="flex justify-between items-center mb-2">
        <h1 class="text-2xl font-bold text-gray-800 flex items-center">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2 text-main" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
          </svg>
          Текстовый редактор
        </h1> 
        <EditorMenu 
          @clear="clearEditor"
          @show-help="showHelpModal = true"
          @finish="finishEditing"
        />
      </div>

      <FormattingToolbar 
        @format="handleFormat"
        @heading="addHeader"
        @align="setTextAlign"
        @list="handleList"
        @insert="handleInsert"
        @color="setTextColor"
        @color-reset="resetTextColor"
        @font-size="setFontSize"
        @font-size-reset="resetFontSize"
      />

      <div class="flex flex-col md:flex-row gap-4">
        <!-- Editor -->
        <div class="w-full md:w-1/2">
          <textarea
            ref="editor"
            v-model="markdownText"
            @input="updatePreview"
            @keydown="handleKeyDown"
            class="w-full h-[60vh] p-4 border border-gray-300 rounded-lg bg-white shadow-sm resize-none font-mono text-sm text-gray-800 leading-relaxed focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
            placeholder="Write your markdown here..."
            spellcheck="false"
          ></textarea>

          <div class="text-xs text-gray-500 mt-2 flex gap-4">
            <span>Lines: {{ lineCount }}</span>
            <span>Words: {{ wordCount }}</span>
            <span>Chars: {{ charCount }}</span>
          </div>
        </div>

        <!-- Preview -->
        <div class="w-full md:w-1/2">
          <div
            class="p-6 border border-gray-300 rounded-lg bg-white overflow-y-auto shadow-sm h-[60vh] prose max-w-none prose-sm"
            v-html="renderedMarkdown">
          </div>
          <div class="text-xs text-gray-500 mt-2 flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
            </svg>
            Live Preview
          </div>
        </div>
      </div>
    </template>

    <!-- Final Preview View -->
    <div v-else class="relative group hover:bg-gray-50 rounded-md">
      <div
        class="p-2 w-full prose max-w-none mx-auto"
        v-html="renderedMarkdown">
      </div>

      <div class="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity duration-200">
        <OptionsMenu
          @copy="copyContent"
          @download="downloadContent"
          @delete="showDeleteModal = true"
          @edit="returnToEditing"
          :showEdit="true"
          :showDelete="true"
        />
      </div>
    </div>

    <!-- Модальное окно подтверждения удаления -->
    <ConfirmModal
      v-if="showDeleteModal"
      title="Удаление изображения"
      message="Вы уверены, что хотите удалить это изображение?"
      confirm-text="Удалить"
      cancel-text="Отмена"
      @confirm="confirmDelete"
      @cancel="showDeleteModal = false"
    />

    <HelpModal 
      :show="showHelpModal"
      @close="showHelpModal = false"
    />
  </div>
</template>

<script>
import EditorMenu from '@/modules/content/blocks/components/EditorMenu.vue';
import FormattingToolbar from '@/modules/content/blocks/MarkdownEditor/components/FormattingToolbar.vue';
import OptionsMenu from '@/modules/content/blocks/components/OptionsMenu.vue';
import ConfirmModal from '@/modules/content/blocks/components/ConfirmModal.vue';
import HelpModal from '@/modules/content/blocks/MarkdownEditor/components/HelpModal.vue';
import MarkdownIt from 'markdown-it';

const COLOR_MAP = {
  red: 'text-red-600',
  blue: 'text-blue-600',
  green: 'text-green-600',
  yellow: 'text-yellow-600',
  purple: 'text-purple-600',
  gray: 'text-gray-600'
};

const SHORTCUTS = {
  'b': 'addBold',
  'i': 'addItalic'
};

const FORMAT_ACTIONS = {
  bold: 'addBold',
  italic: 'addItalic',
  strikethrough: 'addStrikethrough',
  blockquote: 'addBlockquote',
  'inline-code': 'addInlineCode',
  underline: 'addUnderline'
};

const INSERT_ACTIONS = {
  link: 'addLink',
  table: 'addTable',
  hr: 'addHorizontalRule'
};

export default {
  components: {
    EditorMenu,
    FormattingToolbar,
    OptionsMenu,
    ConfirmModal,
    HelpModal 
  },
  inject: ['showAlert'],
  props: {
    data: {
      type: Object,
      required: true,
      default: () => ({ text: '' })
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
  
  emits: ['update', 'delete', 'request-new-block-after'],
  
  data() {
    return {
      markdownText: this.data?.text || '',
      renderedMarkdown: '',
      showDeleteModal: false,
      showHelpModal: false,
      isFinalView: false,
      isInternalChange: false,
      md: null
    };
  },
  
  computed: {
    charCount() {
      return this.markdownText.length;
    },
    wordCount() {
      return this.markdownText.trim().split(/\s+/).filter(Boolean).length;
    },
    lineCount() {
      const lines = this.markdownText.split('\n');
      return lines.length === 1 && !lines[0] ? 0 : lines.length;
    }
  },
  
  watch: {
    'data.text'(newText) {
      if (!this.isInternalChange && newText !== this.markdownText) {
        this.markdownText = newText || '';
        this.updatePreview();
      }
      this.isInternalChange = false;
    },
    markdownText() {
      this.isInternalChange = true;
      this.$emit('update', { index: this.index, newData: { text: this.markdownText } });
      this.updatePreview();
    },
  },
  
  created() {
    this.initMarkdownIt();
  },
  
  mounted() {
    this.updatePreview();
    this.applyTailwindStylesToPreview();
  },
  
  methods: {
  
    initMarkdownIt() {
      this.md = new MarkdownIt({
        html: true,
        xhtmlOut: true,
        breaks: true,
        linkify: true,
        typographer: true
      });
      
      this.configureMarkdownRenderer();
    },
    
    configureMarkdownRenderer() {
      const { renderer } = this.md;
      const rules = renderer.rules;
     
      const defaultLinkRenderer = rules.link_open || 
        ((tokens, idx, options, env, self) => self.renderToken(tokens, idx, options));
      
      rules.link_open = (tokens, idx, options, env, self) => {
        tokens[idx].attrPush(['target', '_blank'], ['rel', 'noopener noreferrer']);
        return defaultLinkRenderer(tokens, idx, options, env, self);
      }
      this.configureListRenderer(rules);
    },
    
    configureListRenderer(rules) {
      const setRenderer = (ruleName, className) => {
        const defaultRenderer = rules[ruleName] || 
          ((tokens, idx, options, env, self) => self.renderToken(tokens, idx, options));
        
        rules[ruleName] = (tokens, idx, options, env, self) => {
          tokens[idx].attrPush(['class', className]);
          return defaultRenderer(tokens, idx, options, env, self);
        };
      };
      
      setRenderer('list_item_open', 'py-1');
      setRenderer('bullet_list_open', 'list-disc pl-5 my-2');
      setRenderer('ordered_list_open', 'list-decimal pl-5 my-2');
    },
    
    updatePreview() {
      try {
        this.renderedMarkdown = this.md.render(this.markdownText);
        this.$nextTick(this.applyTailwindStylesToPreview);
      } catch (error) {
        console.error('Markdown rendering error:', error);
        this.renderedMarkdown = `<div class="text-red-600">Error rendering markdown: ${error.message}</div>`;
      }
    },
    
    applyTailwindStylesToPreview() {
      if (!this.$el) return;
      
      const previewEl = this.$el.querySelector('.prose');
      if (!previewEl) return;
      
      const styleMap = {
        'ul': ['list-disc', 'pl-5', 'my-2', 'space-y-1'],
        'ol': ['list-decimal', 'pl-5', 'my-2', 'space-y-1'],
        'li': ['py-1'],
        'h1': ['text-2xl', 'font-bold', 'my-4'],
        'h2': ['text-xl', 'font-bold', 'my-3'],
        'h3': ['text-lg', 'font-bold', 'my-2'],
        'blockquote': ['pl-4', 'border-l-4', 'border-gray-300', 'italic', 'my-2', 'text-gray-600'],
        'a': ['text-blue-600', 'hover:underline'],
        'pre': ['bg-gray-100', 'p-3', 'rounded', 'my-2', 'overflow-x-auto'],
        'code:not(pre code)': ['bg-gray-100', 'px-1', 'rounded', 'text-red-600', 'font-mono', 'text-sm']
      };
      
      Object.entries(styleMap).forEach(([selector, classes]) => {
        previewEl.querySelectorAll(selector).forEach(el => {
          classes.forEach(cls => {
            if (!el.classList.contains(cls)) {
              el.classList.add(cls);
            }
          });
        });
      });
      
      previewEl.querySelectorAll('table').forEach(table => {
        table.classList.add('border-collapse', 'border', 'border-gray-300', 'my-4', 'w-full');
        
        table.querySelectorAll('th').forEach(th => {
          th.classList.add('border', 'border-gray-300', 'bg-gray-100', 'p-2', 'text-left');
        });
        
        table.querySelectorAll('td').forEach(td => {
          td.classList.add('border', 'border-gray-300', 'p-2');
        });
      });
    },
    
    getSelectedText() {
      const textarea = this.$refs.editor;
      const start = textarea.selectionStart;
      const end = textarea.selectionEnd;
      return {
        text: this.markdownText.substring(start, end),
        start,
        end
      };
    },
    
    insertAtCursor(text, moveCaretAfter = true) {
      const textarea = this.$refs.editor;
      const startPos = textarea.selectionStart;
      const endPos = textarea.selectionEnd;
      
      this.markdownText = 
        this.markdownText.substring(0, startPos) + 
        text + 
        this.markdownText.substring(endPos);
      
      this.$nextTick(() => {
        textarea.focus();
        if (moveCaretAfter) {
          textarea.selectionStart = textarea.selectionEnd = startPos + text.length;
        } else {
          const cursorPos = startPos + text.indexOf('|');
          if (cursorPos > startPos) {
            const newText = text.replace('|', '');
            this.markdownText = 
              this.markdownText.substring(0, startPos) + 
              newText + 
              this.markdownText.substring(endPos);
            textarea.selectionStart = textarea.selectionEnd = cursorPos - 1;
          }
        }
      });
    },
    
    replaceSelection(text) {
      const textarea = this.$refs.editor;
      const startPos = textarea.selectionStart;
      const endPos = textarea.selectionEnd;
      
      this.markdownText = 
        this.markdownText.substring(0, startPos) + 
        text + 
        this.markdownText.substring(endPos);
      
      this.$nextTick(() => {
        textarea.focus();
        textarea.selectionStart = startPos;
        textarea.selectionEnd = startPos + text.length;
      });
    },
    
    handleFormat(type) {
      const method = FORMAT_ACTIONS[type];
      if (method && this[method]) {
        this[method]();
      }
    },
    
    handleList(type) {
      type === 'unordered' ? this.addList() : this.addOrderedList();
    },
    
    handleInsert(type) {
      const method = INSERT_ACTIONS[type];
      if (method && this[method]) {
        this[method]();
      }
    },
    
    wrapSelection(wrapStart, wrapEnd, placeholder = '') {
      const selection = this.getSelectedText();
      if (selection.text) {
        this.replaceSelection(`${wrapStart}${selection.text}${wrapEnd}`);
      } else {
        this.insertAtCursor(`${wrapStart}${placeholder}${wrapEnd}`, false);
      }
    },
    
    addBold() {
      this.wrapSelection('**', '**', 'bold text');
    },
    
    addItalic() {
      this.wrapSelection('*', '*', 'italic text');
    },
    
    addStrikethrough() {
      this.wrapSelection('~~', '~~', 'strikethrough text');
    },
    
    addInlineCode() {
      this.wrapSelection('`', '`', 'code');
    },
    
    addUnderline() {
      this.wrapSelection('<u>', '</u>', 'underlined text');
    },
    
    addBlockquote() {
      const selection = this.getSelectedText();
      if (selection.text) {
        const quotedText = selection.text
          .split('\n')
          .map(line => `> ${line}`)
          .join('\n');
        this.replaceSelection(quotedText);
      } else {
        this.insertAtCursor('> blockquote text\n', false);
      }
    },
    
    addHeader(level) {
      const selection = this.getSelectedText();
      const prefix = '#'.repeat(level) + ' ';
      
      if (selection.text) {
        const newText = selection.text.replace(/^#+\s/, prefix);
        this.replaceSelection(newText.startsWith(prefix) ? newText : `${prefix}${selection.text}`);
      } else {
        const lineStart = this.markdownText.lastIndexOf('\n', selection.start - 1) + 1;
        const lineEnd = this.markdownText.indexOf('\n', selection.start);
        const currentLine = this.markdownText.substring(
          lineStart, 
          lineEnd === -1 ? this.markdownText.length : lineEnd
        );
        
        if (/^#+\s/.test(currentLine)) {
          const newText = currentLine.replace(/^#+\s/, prefix);
          this.markdownText = 
            this.markdownText.substring(0, lineStart) + 
            newText + 
            this.markdownText.substring(lineEnd === -1 ? this.markdownText.length : lineEnd);
        } else {
          this.insertAtCursor(`${prefix}Heading ${level}\n`);
        }
      }
    },
    
    
    addLink() {
      const selection = this.getSelectedText();
      if (selection.text) {
        if (/^(https?:\/\/|www\.)/i.test(selection.text)) {
          this.replaceSelection(`[link text](${selection.text})`);
        } else {
          this.replaceSelection(`[${selection.text}](https://example.com)`);
        }
      } else {
        this.insertAtCursor('[link text](https://example.com)', false);
      }
    },
    
    addList() {
      this.formatLines('- ', 'List item');
    },
    
    addOrderedList() {
      this.formatLines('1. ', 'First item', (lines, index) => 
        `${index + 1}. ${lines[index].replace(/^\d+\.\s*/, '')}`
      );
    },
    
    formatLines(prefix, placeholder, lineFormatter = null) {
      const selection = this.getSelectedText();
      if (selection.text) {
        const lines = selection.text.split('\n');
        const formattedLines = lineFormatter 
          ? lines.map((line, i) => lineFormatter(lines, i))
          : lines.map(line => `${prefix}${line}`);
        this.replaceSelection(formattedLines.join('\n'));
      } else {
        this.insertAtCursor(`${prefix}${placeholder}\n${prefix}Another item\n`);
      }
    },
    
    addTable() {
      const tableTemplate = 
`| Header 1 | Header 2 | Header 3 |
| -------- | -------- | -------- |
| Cell 1   | Cell 2   | Cell 3   |
| Cell 4   | Cell 5   | Cell 6   |
`;
      this.insertAtCursor(`\n${tableTemplate}\n`);
    },
    
    addHorizontalRule() {
      this.insertAtCursor('\n---\n');
    },
    
   
    setTextAlign(align) {
      this.wrapSelection(`<div class="text-${align}">`, '</div>', 'Aligned text');
    },
    
    setTextColor(color) {
      const selection = this.getSelectedText();
      if (selection.text) {
        if (color === null) {
          this.resetTextColor();
        } else {
          this.replaceSelection(`<span style="color: ${color};">${selection.text}</span>`);
        }
      } else if (color !== null) {
        this.insertAtCursor(`<span style="color: ${color};">colored text</span>`, false);
      }
    },
    
    resetTextColor() {
      const selection = this.getSelectedText();
      if (selection.text) {
        const uncoloredText = selection.text.replace(/<span style="color: [^"]+">([^<]+)<\/span>/g, '$1');
        this.replaceSelection(uncoloredText);
      }
    },
    
    setFontSize(size) {
      this.wrapSelection(`<span style="font-size: ${size};">`, '</span>', 'sized text');
    },

    resetFontSize() {
      const selection = this.getSelectedText();
      if (selection.text) {
        const resetText = selection.text.replace(/<span style="font-size: [^"]+;">([^<]+)<\/span>/g, '$1');
        this.replaceSelection(resetText);
      }
    },
   
    handleKeyDown(e) {
      if (e.key === 'Enter') {
        this.handleEnterKey(e);
        return;
      }
      
      if ((e.ctrlKey || e.metaKey) && SHORTCUTS[e.key.toLowerCase()]) {
        e.preventDefault();
        this[SHORTCUTS[e.key.toLowerCase()]]();
      }
    },
    
    handleEnterKey(e) {
      const textarea = e.target;
      const cursorPos = textarea.selectionStart;
      const textBeforeCursor = this.markdownText.substring(0, cursorPos);
      const currentLine = textBeforeCursor.split('\n').pop();
      
      const listMatch = currentLine.match(/^(\s*)([-*+]|\d+\.)\s+(.*)$/);
      if (listMatch) {
        e.preventDefault();
        this.handleListEnter(listMatch, textBeforeCursor, cursorPos);
        return;
      }
      
      if (e.ctrlKey || e.shiftKey) {
        e.preventDefault();
        this.handleNewBlockShortcut();
      }
    },
    
    handleListEnter(listMatch, textBeforeCursor, cursorPos) {
      const [, indent, marker, content] = listMatch;
      
      if (!content.trim()) {
        this.markdownText = 
          textBeforeCursor.substring(0, textBeforeCursor.length - (indent.length + marker.length + 2)) + 
          '\n' + this.markdownText.substring(cursorPos);
        
        this.$nextTick(() => {
          this.$refs.editor.selectionStart = this.$refs.editor.selectionEnd = 
            cursorPos - (indent.length + marker.length + 2) + 1;
        });
        return;
      }
      
      let newMarker = marker;
      if (marker.match(/^\d/)) {
        newMarker = `${parseInt(marker) + 1}.`;
      }
      
      this.markdownText = 
        textBeforeCursor + '\n' + indent + newMarker + ' ' + 
        this.markdownText.substring(cursorPos);
      
      this.$nextTick(() => {
        this.$refs.editor.selectionStart = this.$refs.editor.selectionEnd = 
          cursorPos + indent.length + newMarker.length + 2;
      });
    },
    
    handleNewBlockShortcut() {
      if (this.markdownText.endsWith('\n')) {
        this.markdownText = this.markdownText.replace(/\n+$/, '');
        this.$emit('update', { index: this.index, newData: { text: this.markdownText } });
      }
      this.$emit('request-new-block-after', this.index);
    },
    
    clearEditor() {
        this.markdownText = '';
    },
    
    finishEditing() {
      const isEmpty = !this.markdownText.trim().replace(/<[^>]*>/g, '').length;
      isEmpty ? this.$emit('delete', this.index) : this.isFinalView = true;
    },
    
    returnToEditing() {
      this.isFinalView = false;
      this.$nextTick(() => {
        if (this.$refs.editor) {
          this.$refs.editor.focus();
          const isEmpty = !this.markdownText.trim().replace(/<[^>]*>/g, '').length;
          if (isEmpty) this.$emit('delete', this.index);
        }
      });
    },
    
    async copyContent() {
      try {
        await navigator.clipboard.writeText(this.markdownText);
        this.showAlert('success', 'Текст скопирован.');
      } catch (err) {
        console.error('Copy error:', err);
        this.showAlert('error', 'Произошла ошибка при копировании текста');
      }
    },
    
    downloadContent() {
      try {
        const blob = new Blob([this.markdownText], { type: 'text/markdown' });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = 'content.md';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(url);
      } catch (err) {
        console.error('Download failed:', err);
        this.showAlert('error', 'Ошибка скачивания');
      }
    },
    
    showToast(type, message) {
      this.$toast?.[type](message) || alert(message);
    },
    exitEditMode() {
      if (this.$refs.editor && this.markdownText.trim()) {
        this.isFinalView = true;
      } else if (!this.markdownText.trim()) {
        this.$emit('delete', this.index);
      }
    },
    confirmDelete() {
      this.$emit('delete', this.index);
      this.showDeleteModal = false;
    }
  }
};
</script>
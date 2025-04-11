<template>
  <div 
    :class="{
      'bg-gray-100 min-h-100 mb-4 p-4 rounded-md': !isFinalView, // Фон и высота только при редактировании
      'final-view-active mb-4': isFinalView      // Новый класс для CSS hover-эффекта
    }"
  >
    <!-- Header -->
    <EditorMenu 
      v-if="!isFinalView"
      @clear="clearEditor"
      @show-help="showHelpModal = true"
      @finish="finishEditing"
    />

    <!-- Main Toolbar -->
    <FormattingToolbar 
      v-if="!isFinalView"
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

    <!-- Editor and Preview Area -->
    <div v-if="!isFinalView" class="flex flex-col md:flex-row gap-4">
      <!-- Editor -->
      <div class="editor-wrapper w-full md:w-1/2">
        <textarea
          ref="editor"
          v-model="markdownText"
          @input="updatePreview"
          @keydown="handleKeyDown"
          class="editor w-full h-[60vh] p-4 border border-gray-300 rounded-lg bg-white shadow-sm resize-none font-mono text-sm text-gray-800 leading-relaxed transition-all"
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
      <div class="preview-wrapper w-full md:w-1/2">
        <div
          class="preview p-6 border border-gray-300 rounded-lg bg-white overflow-y-auto shadow-sm h-[60vh] prose max-w-none prose-sm prose-headings:font-semibold prose-a:text-blue-600 hover:prose-a:text-blue-800 prose-code:bg-gray-100 prose-code:px-1 prose-code:py-0.5 prose-code:rounded prose-code:text-red-600 prose-blockquote:border-l-4 prose-blockquote:border-gray-300 prose-blockquote:pl-4 prose-blockquote:italic prose-blockquote:text-gray-700 prose-table:border-collapse prose-table:w-full prose-table:my-4 prose-th:bg-gray-100 prose-th:font-semibold prose-td:border prose-th:border prose-img:rounded-lg"
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

    <!-- Final Preview View section -->
    <div v-if="isFinalView" class="final-preview-container relative group hover:bg-gray-100 rounded-md">
      <div
        class="preview-final p-6 w-full prose max-w-none mx-auto ..." 
        v-html="renderedMarkdown">
      </div>

      <div class="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity duration-200">
        <OptionsMenu
          @copy="copyContent"
          @download="downloadContent"
          @delete="deleteContent"
          :showDelete="true"
        />
      </div>
    </div>

    <!-- Help Modal -->
    <div v-if="showHelpModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-lg shadow-xl w-full max-w-3xl max-h-[90vh] overflow-auto">
        <div class="p-6">
          <div class="flex justify-between items-center mb-4 border-b pb-2">
            <h2 class="text-xl font-bold text-gray-800">Markdown Quick Reference</h2>
            <button @click="showHelpModal = false" class="text-gray-500 hover:text-gray-800 text-2xl transition-colors">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          <div class="prose prose-sm max-w-none">
            <div class="bg-red-50 border-l-4 border-red-500 p-4 mb-4">
              <p class="text-red-600 font-semibold">Note: Text alignment, color, and underline use HTML tags and are not standard Markdown.</p>
            </div>
            
            <h3 class="text-lg font-semibold text-gray-800 border-b pb-1 mb-3">Basic Formatting</h3>
            <ul class="space-y-2">
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600"># Heading 1</code> (<code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">## H2</code>, <code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">### H3</code> ... <code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">###### H6</code>)</li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">**Bold Text**</code> or <code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">__Bold Text__</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">*Italic Text*</code> or <code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">_Italic Text_</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">&lt;u&gt;Underlined Text&lt;/u&gt;</code> (Uses HTML)</li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">~~Strikethrough~~</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">`Inline code`</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">[Link Text](https://example.com)</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">&gt; Blockquote</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">---</code> or <code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">***</code> for Horizontal Rule</li>
              <li>Font Size: Select text and use size dropdown (Inserts <code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">&lt;span style="font-size: size;"&gt;...&lt;/span&gt;</code>).</li>  
            </ul>

            <h3 class="text-lg font-semibold text-gray-800 border-b pb-1 mb-3 mt-6">Lists</h3>
            <p class="font-medium">Unordered:</p>
            <ul class="space-y-1">
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">- Item 1</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">* Item 2</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">+ Item 3</code> (Indent for sub-items: <code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">  - Sub-item</code>)</li>
            </ul>
            <p class="font-medium mt-3">Ordered:</p>
            <ol class="space-y-1">
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">1. First item</code></li>
              <li><code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">2. Second item</code> (Numbering is automatic)</li>
            </ol>

            <h3 class="text-lg font-semibold text-gray-800 border-b pb-1 mb-3 mt-6">Alignment, Color & Underline (Using HTML)</h3>
            <ul class="space-y-2">
              <li>Alignment: Select text/line and use toolbar buttons (Inserts <code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">&lt;div style="text-align: center;"&gt;...&lt;/div&gt;</code>).</li>
              <li>Color: Select text and use color picker (Inserts <code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">&lt;span style="color: #color;"&gt;...&lt;/span&gt;</code>).</li>
              <li>Underline: Select text and use U button (Inserts <code class="bg-gray-100 px-1.5 py-0.5 rounded text-red-600">&lt;u&gt;...&lt;/u&gt;</code>).</li>
            </ul>

            <h3 class="text-lg font-semibold text-gray-800 border-b pb-1 mb-3 mt-6">Tables</h3>
            <pre class="bg-gray-100 p-3 rounded text-sm overflow-x-auto"><code>| Header 1 | Header 2 |
| -------- | -------- |
| Cell 1   | Cell 2   |
| Cell 3   | Cell 4   |</code></pre>
            <p class="text-sm mt-2">(The hyphens <code class="bg-gray-100 px-1 py-0.5 rounded text-red-600">---</code> define the header separator. Colons can be used for alignment: <code class="bg-gray-100 px-1 py-0.5 rounded text-red-600">:---:</code> centered, <code class="bg-gray-100 px-1 py-0.5 rounded text-red-600">---:</code> right-aligned)</p>

            <h3 class="text-lg font-semibold text-gray-800 border-b pb-1 mb-3 mt-6">Keyboard Shortcuts</h3>
            <ul class="space-y-2">
              <li><strong>Bold:</strong> Ctrl+B</li>
              <li><strong>Italic:</strong> Ctrl+I</li>
              <li><strong>Tab/Indent:</strong> Tab (Shift+Tab to unindent in lists)</li>
              <li><strong>Continue List:</strong> Enter (when cursor is at end of list item)</li>
            </ul>
          </div>
          <div class="mt-6 text-right">
            <button @click="showHelpModal = false" class="px-4 py-2 bg-blue-500 hover:bg-blue-600 text-white rounded-lg transition-colors">
              Close
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import EditorMenu from '@/modules/content/components/com/EditorMenu.vue';
import FormattingToolbar from '@/modules/content/components/com/FormattingToolbar.vue';
import OptionsMenu from '@/modules/content/components/com/OptionsMenu.vue';

export default {
  components: {
    EditorMenu,
    FormattingToolbar,
    OptionsMenu
  },
  data() {
    return {
      markdownText: `# Welcome to Markdown Editor`,
      renderedMarkdown: '',
      showHelpModal: false,
      isFinalView: false
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
      if (lines.length === 1 && lines[0] === '') return 0;
      return lines.length;
    }
  },
  mounted() {
    this.updatePreview();
    document.addEventListener('click', this.handleClickOutside);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
  },
  methods: {
    handleFormat(type) {
      switch(type) {
        case 'bold': this.addBold(); break;
        case 'italic': this.addItalic(); break;
        case 'strikethrough': this.addStrikethrough(); break;
        case 'blockquote': this.addBlockquote(); break;
        case 'inline-code': this.addInlineCode(); break;
        case 'underline': this.addUnderline(); break;
      }
    },
    handleList(type) {
      if (type === 'unordered') this.addList();
      else this.addOrderedList();
    },
    handleInsert(type) {
      switch(type) {
        case 'link': this.addLink(); break;
        case 'table': this.addTable(); break;
        case 'hr': this.addHorizontalRule(); break;
      }
    },
    setFontSize(size) {
      const selection = this.getSelectedText();
      if (selection.text) {
        this.replaceSelection(`<span style="font-size: ${size};">${selection.text}</span>`);
      } else {
        this.insertAtCursor(`<span style="font-size: ${size};">sized text</span>`, false);
      }
    },
    resetFontSize() {
      const selection = this.getSelectedText();
      if (selection.text) {
        const resetText = selection.text.replace(/<span style="font-size: [^"]+">([^<]+)<\/span>/g, '$1');
        this.replaceSelection(resetText);
      }
    },
    updatePreview() {
      this.renderedMarkdown = this.convertMarkdownToHtml(this.markdownText);
    },
    convertMarkdownToHtml(markdown) {
      if (!markdown) return '';
      let html = markdown;

      const htmlPlaceholders = [];
      
      const protectHtml = (html) => {
        return html.replace(/<([a-z][a-z0-9]*)\b[^>]*>([\s\S]*?)<\/\1>/gi, (match) => {
          htmlPlaceholders.push(match);
          return `__HTMLPLACEHOLDER_${htmlPlaceholders.length - 1}__`;
        });
      };
      
      const restoreHtml = (processedHtml) => {
        let result = processedHtml;
        for (let i = 0; i < htmlPlaceholders.length; i++) {
          const placeholderRegex = new RegExp(`__HTMLPLACEHOLDER_${i}__`, 'g');
          result = result.replace(placeholderRegex, htmlPlaceholders[i]);
        }
        return result;
      };

      html = protectHtml(html);

      // Tables
      html = html.replace(/^\|(.+)\|\s*\n\|([\-:\s\|]+)\|\s*\n((?:\|.*\|\s*\n?)*)/gm, (match, headerRow, alignmentRow, bodyRows) => {
        const headers = headerRow.split('|').map(cell => cell.trim()).slice(1, -1);
        const alignments = alignmentRow.split('|').map(cell => {
          cell = cell.trim();
          if (!cell) return 'left';
          if (cell.startsWith(':') && cell.endsWith(':')) return 'center';
          if (cell.endsWith(':')) return 'right';
          return 'left';
        }).slice(1, -1);

        let tableHtml = '<table class="markdown-table w-full border-collapse my-4">\n<thead>\n<tr>\n';
        headers.forEach((header, i) => {
          const align = alignments[i] || 'left';
          tableHtml += `<th style="text-align:${align}" class="border px-4 py-2 bg-gray-100 font-semibold">${this.processInlineElements(header)}</th>\n`;
        });
        tableHtml += '</tr>\n</thead>\n<tbody>\n';

        if (bodyRows && bodyRows.trim()) {
          const rows = bodyRows.trim().split('\n');
          rows.forEach(row => {
            if (!row.trim()) return;
            const cells = row.split('|').map(cell => cell.trim()).slice(1, -1);
            const validCells = cells.slice(0, headers.length);

            tableHtml += '<tr>\n';
            validCells.forEach((cell, i) => {
              const align = alignments[i] || 'left';
              const cellContent = cell === '' ? ' ' : this.processInlineElements(cell);
              tableHtml += `<td style="text-align:${align}" class="border px-4 py-2">${cellContent}</td>\n`;
            });
            for (let i = validCells.length; i < headers.length; i++) {
              const align = alignments[i] || 'left';
              tableHtml += `<td style="text-align:${align}" class="border px-4 py-2"> </td>\n`;
            }
            tableHtml += '</tr>\n';
          });
        }
        tableHtml += '</tbody>\n</table>\n';
        return tableHtml;
      });

      // Blockquotes
      html = html.replace(/^> (.*(?:\n> .*)*)/gm, (match, content) => {
        const lines = content.replace(/^> /gm, '');
        return `<blockquote class="border-l-4 border-gray-300 pl-4 italic text-gray-700 my-2">${this.processInlineElements(lines.trim())}</blockquote>\n`;
      });
      html = html.replace(/<\/blockquote>\n?<blockquote class="[^"]*">/g, '<br>');

      // Horizontal Rule
      html = html.replace(/^\s*(?:-{3,}|\*{3,}|_{3,})\s*$/gm, '<hr class="my-4 border-t border-gray-300">\n');

      // Lists - Unordered and Ordered
      const processLists = (type) => {
        const listRegex = type === 'ol'
          ? /^(\s*)(\d+)\.\s+(.*)/gm
          : /^(\s*)([-*+])\s+(.*)/gm;

        let lastIndent = -1;
        let listStack = [];
        let listOpen = false;
        let processedHtml = '';
        let lastIndex = 0;

        html.replace(listRegex, (match, indent, bullet, content, offset) => {
          const precedingText = html.substring(lastIndex, offset);
          processedHtml += precedingText;

          const indentLevel = indent.length;
          let itemHtml = '';
          while (listStack.length > 0 && lastIndent >= indentLevel) {
            if (listOpen) itemHtml += `</${listStack.pop()}>\n`; else listStack.pop();
            lastIndent = listStack.length > 0 ? lastIndent - 2 : -1;
            listOpen = listStack.length > 0;
          }
          if (listStack.length === 0 || lastIndent < indentLevel) {
            itemHtml += `<${type} class="${type === 'ol' ? 'list-decimal' : 'list-disc'} pl-5 my-2">\n`;
            listStack.push(type); lastIndent = indentLevel; listOpen = true;
          } else if (!listOpen) {
            itemHtml += `<${type} class="${type === 'ol' ? 'list-decimal' : 'list-disc'} pl-5 my-2">\n`; listOpen = true;
          }

          itemHtml += `<li class="my-1">${this.processInlineElements(content)}</li>\n`;
          processedHtml += itemHtml;
          lastIndex = offset + match.length;
          return '';
        });
        processedHtml += html.substring(lastIndex);
        while (listStack.length > 0) {
          if (listOpen) processedHtml += `</${listStack.pop()}>\n`; else listStack.pop();
          listOpen = listStack.length > 0;
        }
        html = processedHtml;
      };
      processLists('ol');
      processLists('ul');
      html = html.replace(/<\/ul>\s*<ul class="[^"]*">/g, '');
      html = html.replace(/<\/ol>\s*<ol class="[^"]*">/g, '');
      html = html.replace(/<\/li>\s*<(?=ul|ol)/g, '</li>');

      // Headers
      html = html.replace(/^###### (.*$)/gm, '<h6 class="text-sm font-semibold mt-4 mb-2">$1</h6>\n');
      html = html.replace(/^##### (.*$)/gm, '<h5 class="text-base font-semibold mt-4 mb-2">$1</h5>\n');
      html = html.replace(/^#### (.*$)/gm, '<h4 class="text-lg font-semibold mt-4 mb-2">$1</h4>\n');
      html = html.replace(/^### (.*$)/gm, '<h3 class="text-xl font-semibold mt-5 mb-3">$1</h3>\n');
      html = html.replace(/^## (.*$)/gm, '<h2 class="text-2xl font-bold mt-6 mb-3">$1</h2>\n');
      html = html.replace(/^# (.*$)/gm, '<h1 class="text-3xl font-bold mt-6 mb-4">$1</h1>\n');

      // Restore all HTML placeholders before paragraph processing
      html = restoreHtml(html);

      // Paragraphs
      html = html.split('\n\n').map(paragraph => {
        const trimmed = paragraph.trim();
        if (!trimmed || trimmed.match(/^<(h[1-6]|ul|ol|li|blockquote|pre|hr|table|p|div|span|u)[^>]*>/i)) {
          return paragraph;
        }
        const processedLines = this.processInlineElements(trimmed);
        const linesWithBreaks = processedLines.split('\n').join('<br>\n');
        return `<p class="my-3">${linesWithBreaks}</p>`;
      }).join('\n\n');

      // Final Cleanup
      html = html.replace(/<\/li><br>\n<li/g, '</li><li');
      html = html.replace(/<p class="[^"]*">\s*<\/p>/g, '');
      html = html.replace(/<p>(\s*<(?:ul|ol|blockquote|pre|hr|table)[^>]*>[\s\S]*?<\/(?:ul|ol|blockquote|pre|hr|table)>)\s*<\/p>/g, '$1');

      return html.trim();
    },
    processInlineElements(text) {
      if (!text) return '';
      let processedText = text;
      
      if (/<[a-z][a-z0-9]*\b[^>]*>/i.test(text)) {
        return text;
      }
      
      // Links
      processedText = processedText.replace(/\[([^\]]+)]\(([^)\s]+)\)/g, 
        '<a href="$2" target="_blank" rel="noopener noreferrer" class="text-blue-600 hover:underline">$1</a>');
      
      // Bold
      processedText = processedText.replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>');
      processedText = processedText.replace(/__([^_]+)__/g, '<strong>$1</strong>');
      
      // Italic
      processedText = processedText.replace(/\*([^*]+)\*/g, '<em>$1</em>');
      processedText = processedText.replace(/_([^_]+)_/g, '<em>$1</em>');
      
      // Strikethrough
      processedText = processedText.replace(/~~([^~]+)~~/g, '<del>$1</del>');
      
      // Inline Code
      processedText = processedText.replace(/`([^`]+)`/g, 
        '<code class="bg-gray-100 px-1 py-0.5 rounded text-red-600 font-mono text-sm">$1</code>');

      return processedText;
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
        this.updatePreview();
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
        this.updatePreview();
      });
    },
    addBold() {
      const selection = this.getSelectedText();
      if (selection.text) {
        this.replaceSelection(`**${selection.text}**`);
      } else {
        this.insertAtCursor('**bold text**', false);
      }
    },
    addItalic() {
      const selection = this.getSelectedText();
      if (selection.text) {
        this.replaceSelection(`*${selection.text}*`);
      } else {
        this.insertAtCursor('*italic text*', false);
      }
    },
    addStrikethrough() {
      const selection = this.getSelectedText();
      if (selection.text) {
        this.replaceSelection(`~~${selection.text}~~`);
      } else {
        this.insertAtCursor('~~strikethrough text~~', false);
      }
    },
    addInlineCode() {
      const selection = this.getSelectedText();
      if (selection.text) {
        this.replaceSelection(`\`${selection.text}\``);
      } else {
        this.insertAtCursor('`code`', false);
      }
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
    addUnderline() {
      const selection = this.getSelectedText();
      if (selection.text) {
        this.replaceSelection(`<u>${selection.text}</u>`);
      } else {
        this.insertAtCursor('<u>underlined text</u>', false);
      }
    },
    addHeader(level) {
      const selection = this.getSelectedText();
      const prefix = '#'.repeat(level) + ' ';
      
      if (selection.text) {
        if (/^#+\s/.test(selection.text)) {
          const newText = selection.text.replace(/^#+\s/, prefix);
          this.replaceSelection(newText);
        } else {
          this.replaceSelection(`${prefix}${selection.text}`);
        }
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
      
      this.updatePreview();
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
      const selection = this.getSelectedText();
      if (selection.text) {
        const lines = selection.text.split('\n');
        const bulletedLines = lines.map(line => `- ${line}`).join('\n');
        this.replaceSelection(bulletedLines);
      } else {
        this.insertAtCursor('- List item\n- Another item\n');
      }
    },
    addOrderedList() {
      const selection = this.getSelectedText();
      if (selection.text) {
        const lines = selection.text.split('\n');
        const numberedLines = lines.map((line, index) => `${index + 1}. ${line}`).join('\n');
        this.replaceSelection(numberedLines);
      } else {
        this.insertAtCursor('1. First item\n2. Second item\n');
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
      const selection = this.getSelectedText();
      if (selection.text) {
        this.replaceSelection(`<div style="text-align: ${align};">${selection.text}</div>`);
      } else {
        this.insertAtCursor(`<div style="text-align: ${align};">Aligned text</div>`, false);
      }
    },
    setTextColor(color) {
      const selection = this.getSelectedText();
      if (selection.text) {
        if (color === null) {
          const uncoloredText = selection.text.replace(/<span style="color: [^"]+">([^<]+)<\/span>/g, '$1');
          this.replaceSelection(uncoloredText);
        } else {
          this.replaceSelection(`<span style="color: ${color};">${selection.text}</span>`);
        }
      } else {
        if (color !== null) {
          this.insertAtCursor(`<span style="color: ${color};">colored text</span>`, false);
        }
      }
    },
    resetTextColor() {
      const selection = this.getSelectedText();
      if (selection.text) {
        const uncoloredText = selection.text.replace(/<span style="color: [^"]+">([^<]+)<\/span>/g, '$1');
        this.replaceSelection(uncoloredText);
      }
    },
    handleKeyDown(e) {
      if (e.ctrlKey || e.metaKey) {
        switch (e.key.toLowerCase()) {
          case 'b':
            e.preventDefault();
            this.addBold();
            break;
          case 'i':
            e.preventDefault();
            this.addItalic();
            break;
        }
      }
    },
    handleClickOutside(event) {
      // Handled by individual components now
    },
    clearEditor() {
      if (confirm('Are you sure you want to clear the editor? All content will be lost.')) {
        this.markdownText = '';
        this.updatePreview();
        this.$refs.editor.focus();
      }
    },
    finishEditing() {
      this.isFinalView = true;
      this.updatePreview();
    },
    returnToEditing() {
      this.isFinalView = false;
    },
    copyContent() {
      navigator.clipboard.writeText(this.markdownText)
        .then(() => {
          this.$toast?.success('Content copied to clipboard') || alert('Content copied to clipboard');
        })
        .catch(err => {
          console.error('Failed to copy:', err);
          this.$toast?.error('Failed to copy content') || alert('Failed to copy content');
        });
    },
    deleteContent() {
      this.markdownText = '';
      this.updatePreview();
      this.isFinalView = false;
      this.$emit('delete');
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
        this.$toast?.error('Download failed') || alert('Download failed');
      }
    }
  }
};
</script>

<style>
.final-preview {
  animation: fadeIn 0.3s ease-in-out;
  width: 100%;
  margin: 0;
  padding: 0;
}

.final-preview .preview {
  min-height: calc(100vh - 120px);
  border: none;
  box-shadow: none;
  border-radius: 0;
  padding: 2rem;
}

.editor-container {
  padding: 0;
}

.final-preview .prose {
  max-width: 100%;
}

.toolbar button {
  transition: all 0.15s ease;
}

.toolbar button:hover {
  transform: translateY(-1px);
}

.editor {
  transition: all 0.2s ease;
}

.editor:focus {
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.5);
}

.preview {
  scrollbar-width: thin;
  scrollbar-color: #d1d5db #f3f4f6;
}

.preview::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.preview::-webkit-scrollbar-track {
  background: #f3f4f6;
  border-radius: 4px;
}

.preview::-webkit-scrollbar-thumb {
  background-color: #d1d5db;
  border-radius: 4px;
}

.markdown-table th, .markdown-table td {
  border: 1px solid #e5e7eb;
  padding: 0.5rem 1rem;
}

.markdown-table th {
  background-color: #f9fafb;
  font-weight: 600;
}

.prose :where(pre):not(:where([class~="not-prose"] *)) {
  background-color: #f3f4f6;
  border-radius: 0.375rem;
  padding: 1rem;
  overflow-x: auto;
}

.prose :where(code):not(:where([class~="not-prose"] *))::before,
.prose :where(code):not(:where([class~="not-prose"] *))::after {
  content: none;
}

.final-preview {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

</style>
<template>
  <div
    class="relative group mb-4 rounded-md  bg-white hover:bg-gray-50 p-4 duration-200 resize-y"
  >
    <!-- Языковая метка -->
    <div class="absolute top-2 left-2 text-xs font-semibold text-gray-600 px-2 py-0.5 z-10">
      {{ detectedLanguage }}
    </div>

    <!-- Контейнер для редактора -->
    <div class="code-container relative w-full h-full">
      <div ref="editorRef" class="h-full"></div>
    </div>

    <!-- Кнопка "Готово" (в режиме редактирования) -->
    <button
      v-if="isEditing"
      @click="finishEditing"
      class="absolute top-2 right-2 px-3 py-1 text-sm bg-main text-white rounded hover:bg-main-hover transition-colors z-10"
    >
      Готово
    </button>

    <!-- Контекстное меню (в режиме просмотра) -->
    <div
      v-else
      class="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity duration-200 z-10"
    >
      <OptionsMenu
        @copy="copyCode"
        @download="downloadCode"
        @edit="startEditing"
        @delete="$emit('delete')"
      />
    </div>
  </div>
</template>

<script>
import { EditorState } from '@codemirror/state'
import { EditorView, keymap } from '@codemirror/view'
import { defaultKeymap, indentWithTab } from '@codemirror/commands'
import { syntaxHighlighting, defaultHighlightStyle } from '@codemirror/language'
import hljs from 'highlight.js'
import OptionsMenu from '@/modules/content/blocks/components/OptionsMenu.vue';

// Языковые расширения
import { javascript } from '@codemirror/lang-javascript'
import { python } from '@codemirror/lang-python'
import { html } from '@codemirror/lang-html'
import { css } from '@codemirror/lang-css'
import { json } from '@codemirror/lang-json'
import { cpp } from '@codemirror/lang-cpp'
import { java } from '@codemirror/lang-java'
import { php } from '@codemirror/lang-php'
import { sql } from '@codemirror/lang-sql'
import { markdown } from '@codemirror/lang-markdown'
import { xml } from '@codemirror/lang-xml'

export default {
  name: 'CodeEditor',
  components: { OptionsMenu },
  inject: ['showAlert'],
  props: {
    modelValue: { type: String, default: '' },
    language: { type: String, default: '' },
    placeholder: { type: String, default: 'Введите ваш код здесь...' }
  },
  emits: ['update:modelValue', 'delete'],
  data() {
    return {
      code: this.modelValue,
      editorView: null,
      isEditing: true,
      shouldDelete: false, 
      detectedLanguage: 'text',
      languageWhitelist: [
        'javascript',
        'typescript',
        'python',
        'html',
        'css',
        'json',
        'cpp',
        'java',
        'php',
        'sql',
        'markdown',
        'xml'
      ],
      languageMap: {
        javascript: javascript(),
        typescript: javascript({ typescript: true }),
        jsx: javascript({ jsx: true }),
        python: python(),
        html: html(),
        css: css(),
        json: json(),
        cpp: cpp(),
        java: java(),
        php: php(),
        sql: sql(),
        markdown: markdown(),
        xml: xml(),
        text: null
      }
    }
  },
  watch: {
    modelValue(val) {
      if (val !== this.code && this.editorView) {
        this.code = val
        this.editorView.dispatch({
          changes: { from: 0, to: this.editorView.state.doc.length, insert: val }
        })
        this.editorView.requestMeasure()
      }
    },
    code(newCode) {
      this.detectedLanguage = this.language || this.detectLanguage(newCode)
      this.$emit('update:modelValue', newCode)
    },
    detectedLanguage() {
      this.createEditor()
    },
    isEditing() {
      this.createEditor()
    }
  },
  mounted() {
    this.createEditor()
    this.detectedLanguage = this.language || this.detectLanguage(this.code)
  },
  beforeUnmount() {
    if (this.editorView) {
      this.editorView.destroy()
    }
  },
  methods: {
    detectLanguage(code) {
      if (!code.trim()) return 'text'
      const result = hljs.highlightAuto(code, this.languageWhitelist)
      return result.language || 'text'
    },
    createEditor() {
      if (this.editorView) {
        this.editorView.destroy()
      }

      if (!this.$refs.editorRef) return

      const languageExtension = this.languageMap[this.detectedLanguage.toLowerCase()] || null

      const extensions = [
        keymap.of([...defaultKeymap, indentWithTab]),
        EditorState.tabSize.of(2),
        EditorView.lineWrapping,
        syntaxHighlighting(defaultHighlightStyle),
        EditorView.editable.of(this.isEditing),
        EditorView.theme({
          "&": {
            backgroundColor: "#ffffff",
            color: "#333333",
          },
          "&.cm-focused": {
            outline: "none !important"
          },
          ".cm-gutters": {
            backgroundColor: "#f8f8f8",
            color: "#999",
            borderRight: "1px solid #eee",
          },
          ".cm-activeLineGutter": {
            backgroundColor: "#f0f0f0",
          },
          ".cm-activeLine": {
            backgroundColor: "#f5f5f5",
          },
          ".cm-placeholder": {
            color: "#999",
            fontStyle: "italic",
            paddingLeft: "2px"
          }
        }),
        EditorView.updateListener.of(update => {
          if (update.docChanged) {
            this.code = update.state.doc.toString()
          }
        })
      ]

      if (languageExtension) {
        extensions.push(languageExtension)
      }

      if (!this.code) {
        extensions.push(EditorView.contentAttributes.of({ 'aria-placeholder': this.placeholder }))
        extensions.push(EditorView.theme({
          ".cm-line": {
            paddingLeft: "2px"
          }
        }))
      }

      const startState = EditorState.create({
        doc: this.code,
        extensions
      })

      this.editorView = new EditorView({
        state: startState,
        parent: this.$refs.editorRef
      })

      this.$nextTick(() => {
        if (this.isEditing) {
          this.editorView.focus()
        }
        this.editorView.requestMeasure()
      })
    },
    startEditing() {
      this.isEditing = true
    },
    finishEditing() {
      this.isEditing = false

      if (!this.code.trim()) {
        this.shouldDelete = true
        this.$emit('delete')
      }
    },
    copyCode() {
      navigator.clipboard.writeText(this.code)
        .then(() => {
            this.showAlert('success','Код скопирован');
          })
          .catch(err => {
            console.error('Ошибка копирования:', err);
            this.showAlert('error','Ошибка копирования');
          });
    },
    downloadCode() {
      const blob = new Blob([this.code], { type: 'text/plain;charset=utf-8' })
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = `code.${this.detectedLanguage.toLowerCase()}`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    }
  }
}
</script>

<style scoped>
.code-container {
  position: relative;
  box-sizing: border-box;
  min-height: 50px;
  z-index: 0;
  padding-top: 0.5rem;
}

:deep(.cm-editor) {
  height: 100%;
  border-radius: 4px;
  font-family: 'Fira Code', monospace;
  font-size: 14px;
  line-height: 1.5;
}

:deep(.cm-scroller) {
  overflow: auto;
}

:deep(.cm-content) {
  padding-top: 12px;
  padding-bottom: 12px;
}

.group:hover :deep(.cm-editor) {
  background-color: rgb(249 250 251); /* gray-50 */
  transition: background-color 0.2s ease;
}
</style>
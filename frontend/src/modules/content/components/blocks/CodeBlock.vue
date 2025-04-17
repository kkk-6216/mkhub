<template>
  <div
    class="relative group mb-4 rounded-md border border-gray-200 bg-white p-4 shadow-sm hover:shadow-md transition-shadow duration-200 resize-y min-h-[200px]"
  >
    <!-- Языковая метка -->
    <div class="absolute top-1 left-2 text-xs font-semibold text-gray-600 px-2 py-0.5 z-10">
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
      class="absolute top-2 right-2 px-3 py-1 text-sm bg-blue-500 text-white rounded hover:bg-blue-600 transition-colors z-10"
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

<script setup>
import { ref, watch, onMounted, onUnmounted, computed } from 'vue'
import { EditorState } from '@codemirror/state'
import { EditorView, keymap } from '@codemirror/view'
import { defaultKeymap, indentWithTab } from '@codemirror/commands'
import { syntaxHighlighting, defaultHighlightStyle } from '@codemirror/language'
import hljs from 'highlight.js'
import OptionsMenu from '@/modules/content/components/com/OptionsMenu.vue'

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

const props = defineProps({
  modelValue: { type: String, default: '' },
  language: { type: String, default: '' }, // Пустая строка для автоопределения
  placeholder: { type: String, default: 'Введите ваш код здесь...' }
})

const emit = defineEmits(['update:modelValue', 'delete'])

const code = ref(props.modelValue)
const editorRef = ref(null)
const isEditing = ref(true)
const detectedLanguage = ref('text')
let editorView = null

// Белый список поддерживаемых языков
const languageWhitelist = [
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
]

const languageMap = {
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

// Функция автоопределения языка
function detectLanguage(code) {
  if (!code.trim()) return 'text'
  const result = hljs.highlightAuto(code, languageWhitelist)
  return result.language || 'text'
}

// Обновляем detectedLanguage при изменении кода
watch(code, (newCode) => {
  detectedLanguage.value = props.language || detectLanguage(newCode)
}, { immediate: true })

watch(() => props.modelValue, (val) => {
  if (val !== code.value && editorView) {
    code.value = val
    editorView.dispatch({
      changes: { from: 0, to: editorView.state.doc.length, insert: val }
    })
    editorView.requestMeasure()
  }
})

watch(detectedLanguage, createEditor)
watch(isEditing, createEditor)

function createEditor() {
  if (editorView) {
    editorView.destroy()
  }

  if (!editorRef.value) return

  const languageExtension = languageMap[detectedLanguage.value.toLowerCase()] || null

  const extensions = [
    keymap.of([...defaultKeymap, indentWithTab]),
    EditorState.tabSize.of(2),
    EditorView.lineWrapping,
    syntaxHighlighting(defaultHighlightStyle),
    EditorView.editable.of(isEditing.value),
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
        code.value = update.state.doc.toString()
        emit('update:modelValue', code.value)
      }
    })
  ]

  if (languageExtension) {
    extensions.push(languageExtension)
  }

  if (!code.value) {
    extensions.push(EditorView.contentAttributes.of({ 'aria-placeholder': props.placeholder }))
    extensions.push(EditorView.theme({
      ".cm-line": {
        paddingLeft: "2px"
      }
    }))
  }

  const startState = EditorState.create({
    doc: code.value,
    extensions
  })

  editorView = new EditorView({
    state: startState,
    parent: editorRef.value
  })

  requestAnimationFrame(() => {
    if (isEditing.value) {
      editorView.focus()
    }
    editorView.requestMeasure()
  })
}

function startEditing() {
  isEditing.value = true
}

function finishEditing() {
  isEditing.value = false
}

onMounted(() => {
  createEditor()
})

onUnmounted(() => {
  if (editorView) {
    editorView.destroy()
  }
})

function copyCode() {
  navigator.clipboard.writeText(code.value)
    .then(() => {
      const notification = document.createElement('div')
      notification.textContent = 'Код скопирован'
      notification.style.cssText = 'position:fixed;top:20px;right:20px;background:#4CAF50;color:white;padding:10px;border-radius:4px;z-index:1000;'
      document.body.appendChild(notification)
      setTimeout(() => document.body.removeChild(notification), 2000)
    })
    .catch(() => alert('Ошибка копирования'))
}

function downloadCode() {
  const blob = new Blob([code.value], { type: 'text/plain;charset=utf-8' })
  const link = document.createElement('a')
  link.href = URL.createObjectURL(blob)
  link.download = `code.${detectedLanguage.value.toLowerCase()}`
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}
</script>

<style scoped>
.code-container {
  position: relative;
  box-sizing: border-box;
  min-height: 150px;
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
</style>
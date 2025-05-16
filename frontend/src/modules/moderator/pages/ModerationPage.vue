<template>
  <div class="max-w-7xl mx-auto p-6 bg-white rounded-lg">
    <!-- Заголовок и фильтры -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-medium text-dark">Модерация контента</h2>
      <div class="flex space-x-4">
        <div class="relative">
          <select v-model="currentFilter" class="appearance-none bg-gray-100 border border-gray-300 rounded-md py-2 pl-3 pr-8 text-sm focus:outline-none focus:ring-1 focus:ring-blue-500">
            <option value="all">Все материалы</option>
            <option value="pending">На проверке</option>
            <option value="text">Тексты</option>
            <option value="file">Файлы</option>
            <option value="test">Тесты</option>
          </select>
        </div>
        <input 
          v-model="searchQuery"
          type="text" 
          placeholder="Поиск..." 
          class="bg-gray-100 border border-gray-300 rounded-md py-2 px-3 text-sm focus:outline-none focus:ring-1 focus:ring-blue-500"
        >
      </div>
    </div>

    <!-- Статистика -->
    <div class="mb-3 grid grid-cols-3 gap-3">
      <div class="bg-blue-50 p-4 rounded-lg">
        <p class="text-sm text-gray-500">На проверке</p>
        <p class="text-2xl font-bold">{{ stats.pending }}</p>
      </div>
      <div class="bg-green-50 p-4 rounded-lg">
        <p class="text-sm text-gray-500">Одобрено</p>
        <p class="text-2xl font-bold">{{ stats.approved }}</p>
      </div>
      <div class="bg-red-50 p-4 rounded-lg">
        <p class="text-sm text-gray-500">Отклонено</p>
        <p class="text-2xl font-bold">{{ stats.rejected }}</p>
      </div>
    </div>

    <!-- Таблица контента -->
    <div class="overflow-x-auto">
      <table class="w-full text-left bg-white">
        <thead>
          <tr class="border-b border-gray-300">
            <th class="py-2 text-xs font-medium text-gray-500">#</th>
            <th class="py-2 text-xs font-medium text-gray-500">Заголовок</th>
            <th class="py-2 text-xs font-medium text-gray-500">Автор</th>
            <th class="py-2 text-xs font-medium text-gray-500">Дата</th>
            <th class="py-2 text-xs font-medium text-gray-500">Статус</th>
            <th class="py-2 text-xs font-medium text-gray-500 text-right">Действия</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in filteredContents" :key="item.id" class="border-b border-gray-300 hover:bg-gray-50">
            <td class="py-3 text-sm text-dark">{{ index + 1 }}</td>
            <td class="py-3">
              <button @click="openPreview(item)" class="cursor-pointer text-sm text-left text-dark hover:text-main">
                {{ item.title }}
              </button>
            </td>
            <td class="py-3 text-sm text-dark">{{ item.author }}</td>
            <td class="py-3 text-sm text-gray-500">{{ formatDate(item.date) }}</td>
            <td class="py-3 text-sm">
              <span class="px-2 py-1 rounded-full text-xs" :class="statusClasses[item.status]">
                {{ item.status }}
              </span>
            </td>
            <td class="py-3 text-right space-x-2">
              <template v-if="item.status === 'На проверке' || item.status === 'Жалоба'">
                <button 
                  @click="approve(item)" 
                  class="p-1 bg-green-100 text-green-800 rounded hover:bg-green-200 text-sm px-3 py-1"
                >
                  Одобрить
                </button>
                <button 
                  @click="openRejectModal(item)" 
                  class="p-1 bg-red-100 text-red-800 rounded hover:bg-red-200 text-sm px-3 py-1"
                >
                  Отклонить
                </button>
              </template>
              <span v-else class="text-gray-400 text-sm">Завершено</span>
            </td>
          </tr>
        </tbody>
      </table>

      <p v-if="filteredContents.length === 0" class="text-center text-gray-500 py-4">
        Нет контента для отображения
      </p>
    </div>

    <!-- Модалка предпросмотра -->
    <div v-if="previewItem" class="fixed inset-0 backdrop-blur-sm flex items-center justify-center p-4 z-50">
      <div class="bg-white rounded-lg max-w-4xl w-full max-h-[90vh] overflow-auto">
        <div class="p-6">
          <div class="flex justify-between items-start mb-4">
            <h3 class="text-xl font-bold">{{ previewItem.title }}</h3>
            <button @click="closePreview" class="text-gray-500 hover:text-gray-700">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          <div class="prose max-w-none" v-html="previewItem.content"></div>
        </div>
      </div>
    </div>

    <!-- Модалка отклонения -->
    <div v-if="rejectModal" class="fixed inset-0 backdrop-blur-sm flex items-center justify-center p-4 z-50">
      <div class="bg-white rounded-lg max-w-md w-full">
        <div class="p-6">
          <div class="flex justify-between items-start mb-4">
            <h3 class="text-xl font-bold">Отклонить материал</h3>
            <button @click="closeRejectModal" class="text-gray-500 hover:text-gray-700">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          <div class="mb-4">
            <p class="mb-2">Причина отклонения:</p>
            <select v-model="rejectReason" class="w-full border border-gray-300 rounded-md p-2">
              <option value="Низкое качество">Низкое качество</option>
              <option value="Неточная информация">Неточная информация</option>
              <option value="Нарушение правил">Нарушение правил</option>
              <option value="Дубликат">Дубликат</option>
              <option value="Другое">Другое</option>
            </select>
            <textarea 
              v-if="rejectReason === 'Другое'"
              v-model="customRejectReason"
              placeholder="Введите причину..."
              class="w-full mt-2 border border-gray-300 rounded-md p-2"
              rows="3"
            ></textarea>
          </div>
          <div class="flex justify-end space-x-3">
            <DefaultButton :label="cancelText" variant="primary" @click="closeRejectModal" />
            <DefaultButton :label="confirmText" variant="danger" @click="confirmReject" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getModerationContents, approveContent, rejectContent } from '@/modules/moderator/service/moderationService.js'
import DefaultButton from "@/components/buttons/DefaultButton.vue"

export default {
  name: 'ModerationPage',
  components: {
    DefaultButton
  },
  props: {
      confirmText: {
        type: String,
        default: 'Подтвердить'
      },
      cancelText: {
        type: String,
        default: 'Отмена'
      }
    },
  data() {
    return {
      contents: [],
      currentFilter: 'all',
      searchQuery: '',
      previewItem: null,
      rejectModal: false,
      currentRejectItem: null,
      rejectReason: 'Низкое качество',
      customRejectReason: '',
      statusClasses: {
        'На проверке': 'bg-yellow-100 text-yellow-800',
        'Одобрено': 'bg-green-100 text-green-800',
        'Отклонено': 'bg-red-100 text-red-800',
      },
      cancelText: 'Отмена',
      confirmText: 'Отклонить'
    }
  },
  computed: {
    filteredContents() {
      let result = this.contents

      if (this.currentFilter !== 'all') {
        if (this.currentFilter === 'pending') {
          result = result.filter(item => item.status === 'На проверке')
        } else {
          result = result.filter(item => item.type.toLowerCase().includes(this.currentFilter))
        }
      }

      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase()
        result = result.filter(item =>
          item.title.toLowerCase().includes(query) || 
          item.author.toLowerCase().includes(query)
        )
      }

      return result
    },
    stats() {
      return {
        pending: this.contents.filter(item => item.status === 'На проверке').length,
        approved: this.contents.filter(item => item.status === 'Одобрено').length,
        rejected: this.contents.filter(item => item.status === 'Отклонено').length,
      }
    }
  },
  methods: {
    async fetchContents() {
      try {
        const { data } = await getModerationContents()
        this.contents = data
      } catch (error) {
        console.error('Ошибка загрузки материалов:', error)
      }
    },
    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString('ru-RU', {
        year: 'numeric', month: 'short', day: 'numeric'
      })
    },
    openPreview(item) {
      this.previewItem = item
    },
    closePreview() {
      this.previewItem = null
    },
    async approve(item) {
      try {
        await approveContent(item.id)
        item.status = 'Одобрено'
        this.previewItem = null
      } catch (error) {
        console.error('Ошибка при одобрении:', error)
      }
    },
    openRejectModal(item) {
      this.currentRejectItem = item
      this.rejectModal = true
      this.previewItem = null
    },
    closeRejectModal() {
      this.rejectModal = false
    },
    async confirmReject() {
      if (this.currentRejectItem) {
        try {
          const reason = this.rejectReason === 'Другое' ? this.customRejectReason : this.rejectReason
          await rejectContent(this.currentRejectItem.id, reason)
          this.currentRejectItem.status = 'Отклонено'
        } catch (error) {
          console.error('Ошибка при отклонении:', error)
        }
      }
      this.rejectModal = false
      this.currentRejectItem = null
      this.rejectReason = 'Низкое качество'
      this.customRejectReason = ''
    }
  },
  mounted() {
    this.fetchContents()
  }
}
</script>

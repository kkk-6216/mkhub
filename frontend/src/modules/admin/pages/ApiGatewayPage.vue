<template>
  <div class="p-4">
    <h1 class="text-2xl font-bold mb-4">Мониторинг URL-ов</h1>

    <table class="w-full table-auto border">
      <thead class="bg-gray-100">
      <tr>
        <th class="px-4 py-2 text-left">URL</th>
        <th class="px-4 py-2 text-left">Метод</th>
        <th class="px-4 py-2 text-left">Роль</th>
        <th class="px-4 py-2 text-center">Действия</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="endpoint in endpoints" :key="endpoint.url + endpoint.method" class="border-t">
        <td class="px-4 py-2">{{ endpoint.url }}</td>
        <td class="px-4 py-2">{{ endpoint.method }}</td>
        <td class="px-4 py-2">
            <span :class="getRoleClass(endpoint.role)">
              {{ endpoint.role }}
            </span>
        </td>
        <td class="px-4 py-2 text-center">
          <button @click="openLogs(endpoint)" class="bg-gray-800 text-white px-3 py-1 rounded hover:bg-gray-600">
            Log
          </button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- График -->
    <div class="mt-6">
      <h3 class="text-lg font-semibold mb-2">Активность за 24 часа</h3>
      <canvas ref="chartRef" class="w-full h-64"></canvas>
    </div>

    <!-- Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white w-full max-w-4xl rounded-lg shadow-lg p-6 relative">
        <button class="absolute top-2 right-2 text-gray-500 hover:text-black text-xl" @click="closeModal">×</button>
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-xl font-bold">Логи для {{ selectedEndpoint?.url }}</h2>
          <button @click="downloadLogs" class="bg-green-600 hover:bg-green-700 text-white px-3 py-1 rounded">
            Скачать CSV
          </button>
        </div>

        <!-- Фильтры -->
        <div class="flex flex-wrap gap-4 mb-4">
          <input v-model="filters.user" placeholder="Пользователь" class="border px-2 py-1 rounded w-40" />
          <input v-model="filters.role" placeholder="Роль" class="border px-2 py-1 rounded w-40" />
          <input v-model="filters.method" placeholder="Метод" class="border px-2 py-1 rounded w-40" />
        </div>

        <div class="max-h-96 overflow-y-auto border rounded p-2 bg-gray-50 text-sm">
          <div v-if="logs.length === 0" class="text-gray-400">Нет логов для отображения.</div>
          <div v-for="(log, index) in logs" :key="index" class="mb-2">
            <div>
              <strong>{{ log.timestamp }}</strong> —
              <span class="text-blue-600">{{ log.user }}</span>
              (<span>{{ log.role }}</span>) →
              <span class="font-mono">{{ log.method }}</span>
              <span class="text-green-700">{{ log.url }}</span>
              за {{ log.duration }} мс
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import apiClient from "@/api/axiosInstance.js";
import { Chart, registerables } from 'chart.js'
import { useAuthStore } from "@/store/auth.js"
import debounce from 'lodash/debounce'

// 🔒 Проверка роли
const auth = useAuthStore()
const currentRole = auth.user.role

const endpoints = ref([])
const logs = ref([])
const showModal = ref(false)
const selectedEndpoint = ref(null)
const intervalId = ref(null)
const chartRef = ref(null)
let chartInstance = null

const filters = ref({
  user: '',
  role: '',
  method: '',
})

// 📊 График активности
Chart.register(...registerables)

function drawChart(stats) {
  const labels = Object.keys(stats)
  const values = Object.values(stats)

  if (chartInstance) chartInstance.destroy()

  chartInstance = new Chart(chartRef.value, {
    type: 'line',
    data: {
      labels,
      datasets: [{
        label: 'Кол-во запросов',
        data: values,
        fill: false,
        borderWidth: 2,
        tension: 0.3,
      }]
    },
    options: {
      responsive: true,
      scales: {
        x: { title: { display: true, text: 'Часы' } },
        y: { title: { display: true, text: 'Количество' }, beginAtZero: true }
      }
    }
  })
}

function fetchChartData() {
  if (!selectedEndpoint.value) return
  apiClient.get('/admin/stats', { params: { url: selectedEndpoint.value.url } })
      .then(res => drawChart(res.data))
      .catch(console.error)
}

// 🔁 Основные действия
function fetchEndpoints() {
  apiClient.get('/admin/endpoints')
      .then(res => endpoints.value = res.data)
      .catch(err => console.error('Ошибка загрузки endpoint-ов:', err))
}

function fetchLogs() {
  if (!selectedEndpoint.value) return

  const params = {
    url: selectedEndpoint.value.url,
    user: filters.value.user || undefined,
    role: filters.value.role || undefined,
    method: filters.value.method || undefined,
  }

  apiClient.get('/admin/logs', { params })
      .then(res => logs.value = res.data)
      .catch(err => console.error('Ошибка загрузки логов:', err))
}

function openLogs(endpoint) {
  selectedEndpoint.value = endpoint
  showModal.value = true
  clearInterval(intervalId.value)
  fetchLogs()
  fetchChartData()
  intervalId.value = setInterval(() => {
    fetchLogs()
    fetchChartData()
  }, 15000)
}

function closeModal() {
  showModal.value = false
  selectedEndpoint.value = null
  logs.value = []
  clearInterval(intervalId.value)
}

// 📁 Скачать CSV
function downloadLogs() {
  if (!logs.value.length) return

  const csv = [
    ['timestamp', 'user', 'role', 'method', 'url', 'duration'],
    ...logs.value.map(log => [
      log.timestamp,
      log.user,
      log.role,
      log.method,
      log.url,
      log.duration,
    ])
  ]
      .map(row => row.map(cell => `"${String(cell).replace(/"/g, '""')}"`).join(','))
      .join('\n')

  const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = `logs-${selectedEndpoint.value.url.replace(/\W+/g, '_')}.csv`
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

// 🎨 Цвет по роли
function getRoleClass(role) {
  const pureRole = role.replace('ROLE_', '')
  switch (pureRole) {
    case 'ADMIN': return 'bg-red-500 text-white px-2 py-1 rounded'
    case 'MODERATOR': return 'bg-blue-500 text-white px-2 py-1 rounded'
    case 'STUDENT': return 'bg-green-500 text-white px-2 py-1 rounded'
    default: return 'bg-gray-400 text-white px-2 py-1 rounded'
  }
}

// 🔁 Динамическое обновление при изменении фильтров
watch(filters, debounce(fetchLogs, 500))

// 🔐 Роль + init
onMounted(() => {
  if (currentRole !== 'ROLE_ADMIN') {
    alert('Доступ только для администратора!')
    window.location.href = '/'
  } else {
    fetchEndpoints()
  }
})

onBeforeUnmount(() => clearInterval(intervalId.value))
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
}
</style>

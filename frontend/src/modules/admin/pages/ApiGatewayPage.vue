<template>
  <div class="max-w-7xl mx-auto p-6 bg-white rounded-lg">
    <div class="flex justify-between items-center mb-6">
      <div class="flex items-center justify-center">
        <h2 class="text-2xl font-medium text-dark">Мониторинг [Endpoint]-ов</h2>
        <svg v-if="loading" aria-hidden="true" class="inline w-5 h-5 text-gray-200 ml-3 animate-spin dark:text-gray-500 fill-dark" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"/>
          <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"/>
        </svg>
      </div>
    </div>

    <div class="overflow-x-auto">
      <table class="w-full text-left">
        <thead>
        <tr class="border-b border-gray-300">
          <th class="py-2 text-xs font-medium text-gray-500">#</th>
          <th class="py-2 text-xs font-medium text-gray-500">URL</th>
          <th class="py-2 text-xs font-medium text-gray-500">Метод</th>
          <th class="py-2 text-xs font-medium text-gray-500">Роли</th>
          <th class="py-2 text-xs font-medium text-gray-500 text-center">Логи</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(endpoint, index) in endpoints" :key="endpoint.url + endpoint.method" class="border-b border-gray-300 hover:bg-gray-50">
          <td class="py-3 text-sm text-dark">{{ index+1 }}</td>
          <td class="py-3 text-sm text-dark">{{ endpoint.url }}</td>
          <td class="py-3 text-sm text-dark">{{ endpoint.method }}</td>
          <td class="py-3 space-x-1">
              <span v-for="role in endpoint.roles" :key="role" :class="getRoleClass(role)">
                {{ role }}
              </span>
          </td>
          <td class="py-3 text-center">
            <button @click="openLogs(endpoint)" class="p-1 rounded-md hover:bg-gray-300">
              <svg width="20" height="20" viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg" fill="currentColor">
                <g transform="translate(85.572501, 42.666667)" fill="currentColor">
                  <path d="M236.35,0H1.68V234.67H44.35V192V169.6V42.67H218.64L300.35,124.37V169.6V192V234.67H343.02V106.67L236.35,0ZM0,405.33V277.36H28.81V382.76H83.81V405.33H0ZM153.17,275.1C173.28,275.1 188.69,281.48 199.41,294.25C209.71,306.47 214.85,322.19 214.85,341.39C214.85,362.4 208.77,379.11 196.61,391.52C186.13,402.23 171.65,407.59 153.17,407.59C133.06,407.59 117.65,401.21 106.93,388.44C96.63,376.22 91.49,360.27 91.49,340.58C91.49,319.99 97.57,303.49 109.73,291.09C120.27,280.43 134.75,275.1 153.17,275.1ZM153.08,297.68C142.66,297.68 134.63,302.02 128.97,310.69C123.85,318.51 121.29,328.57 121.29,340.85C121.29,355.06 124.33,366.08 130.41,373.9C136.13,381.31 143.72,385.01 153.17,385.01C163.53,385.01 171.59,380.65 177.37,371.92C182.49,364.21 185.05,354.04 185.05,341.39C185.05,327.48 182.01,316.62 175.93,308.79C170.21,301.38 162.59,297.68 153.08,297.68ZM343.91,333.72V399.01C336.56,401.48 331.39,403.11 328.38,403.89C319.04,406.36 309.02,407.59 298.3,407.59C277.23,407.59 261.18,402.29 250.17,391.7C237.94,380.02 231.83,363.73 231.83,342.84C231.83,318.81 239.42,300.69 254.59,288.47C265.61,279.56 280.48,275.1 299.21,275.1C315.22,275.1 330.12,278.02 343.91,283.86L334.07,306.35C327.56,303.1 321.87,300.83 316.99,299.53C312.12,298.24 306.76,297.59 300.92,297.59C286.95,297.59 276.66,302.14 270.03,311.23C264.44,318.93 261.64,329.08 261.64,341.66C261.64,356.78 265.85,368.15 274.28,375.8C281.02,381.94 289.39,385.01 299.39,385.01C305.23,385.01 310.77,384.02 316,382.03V356.29H293.97V333.72H343.91Z"/>
                </g>
              </svg>
            </button>
          </td>
        </tr>
        </tbody>
      </table>

      <p v-if="endpoints.length === 0" class="text-center text-gray-500 py-4">Нет доступных endpoint-ов.</p>
    </div>

    <div class="mt-8">
      <h3 class="text-base font-medium text-dark mb-3">Активность за 24 часа</h3>
      <canvas ref="chartRef" class="w-full h-64"></canvas>
    </div>

    <Modal v-if="showModal" @close="closeModal" width="w-[1000px]" position="fixed p-4 pt-5 z-50 flex justify-center items-start">
      <div class="flex justify-between items-center">
        <h2 class="text-xl font-medium text-dark mb-6">Логи для {{ selectedEndpoint?.url }}</h2>
        <!-- Кнопка CSV -->
        <div class="flex justify-end mb-4">
          <button @click="downloadLogs" class="px-4 py-2  text-main rounded hover:bg-gray-200">
            Скачать CSV
          </button>
        </div>
      </div>

      <!-- Фильтры -->
      <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-4">
        <InputComponent v-model.trim="filters.user" name="username" placeholder="Имя пользователя" type=""/>
        <InputComponent v-model.trim="filters.role" name="role" placeholder="Роль"/>
        <InputComponent v-model.trim="filters.status" name="status" placeholder="Статус"/>
      </div>

      <div class="max-h-[500px] overflow-y-auto text-sm bg-gray-50 rounded border px-4 py-3">
        <div v-if="logs.length === 0" class="text-gray-500">Нет логов для отображения.</div>
        <div v-for="(log, index) in filteredLogs" :key="index" class="mb-4 border-b pb-3">
          <div><strong>{{ log.timestamp }}</strong> — <span class="text-blue-600">{{ log.user }}</span> ({{ log.role }})</div>
          <div><strong>Method:</strong> {{ log.method }} | <strong>URI:</strong> {{ log.url }} | <strong>Status:</strong> {{ log.status }} | <strong>Time:</strong> {{ log.duration }} ms</div>
          <div v-if="log.ipAddress"><strong>IP:</strong> {{ log.ipAddress }}</div>
          <div v-if="log.authorization"><strong>Authorization:</strong> <code>{{ log.authorization }}</code></div>
          <div v-if="log.contentType"><strong>Content-Type:</strong> {{ log.contentType }}</div>
          <div v-if="log.userAgent"><strong>User-Agent:</strong> {{ log.userAgent }}</div>
          <div v-if="log.requestBody">
            <strong>Request Body:</strong>
            <pre class="whitespace-pre-wrap bg-white border rounded p-2 text-gray-800 overflow-auto">{{ log.requestBody }}</pre>
          </div>
          <div v-if="log.responseBody">
            <strong>Response Body:</strong>
            <pre class="whitespace-pre-wrap bg-white border rounded p-2 text-gray-800 overflow-auto">{{ log.responseBody }}</pre>
          </div>
        </div>
      </div>
    </Modal>

  </div>
</template>

<script>
import Modal from "@/modules/moderator/components/Modal.vue";
import { Chart, registerables } from "chart.js";
import apiClient from "@/api/axiosInstance";
import debounce from "lodash/debounce";
import InputComponent from "@/modules/admin/components/InputComponent.vue";

Chart.register(...registerables);

export default {
  name: "UrlMonitor",
  components: {InputComponent, Modal },
  data() {
    return {
      endpoints: [],
      logs: [],
      loading: false,
      showModal: false,
      selectedEndpoint: null,
      intervalId: null,
      chartRef: null,
      chartInstance: null,
      filters: {
        user: '',
        role: '',
        status: '',
      },
      filteredLogs: [],
    };
  },
  mounted() {
    this.chartRef = this.$refs.chartRef;
    this.fetchEndpoints();
  },
  watch: {
    filters: {
      handler: debounce(function () {
        this.fetchLogs();
      }, 500),
      deep: true,
    }
  },
  beforeUnmount() {
    clearInterval(this.intervalId);
  },
  methods: {
    async fetchEndpoints() {
      this.loading = true;
      try {
        const response = await apiClient.get('/admin/endpoints');
        this.endpoints = response.data;
      } catch (err) {
        console.error("Ошибка загрузки endpoint-ов:", err);
      } finally {
        this.loading = false;
      }
    },
    async fetchLogs() {
      if (!this.selectedEndpoint) return;

      const params = {
        url: this.selectedEndpoint.url
      };

      try {
        const res = await apiClient.get('/admin/logs', { params });
        this.logs = res.data;
        this.filterLogs();
      } catch (err) {
        console.error("Ошибка загрузки логов:", err);
      }
    },
    async fetchChartData() {
      if (!this.selectedEndpoint) return;

      try {
        const res = await apiClient.get('/admin/stats', {
          params: { url: this.selectedEndpoint.url },
        });
        this.drawChart(res.data);
      } catch (err) {
        console.error(err);
      }
    },
    drawChart(stats) {
      const labels = Object.keys(stats);
      const values = Object.values(stats);

      if (this.chartInstance) this.chartInstance.destroy();

      this.chartInstance = new Chart(this.chartRef, {
        type: 'line',
        data: {
          labels,
          datasets: [{
            label: 'Кол-во запросов',
            data: values,
            fill: false,
            borderWidth: 2,
            tension: 0.3,
          }],
        },
        options: {
          responsive: true,
          scales: {
            x: { title: { display: true, text: 'Часы' } },
            y: { title: { display: true, text: 'Количество' }, beginAtZero: true }
          },
        },
      });
    },
    openLogs(endpoint) {
      this.selectedEndpoint = endpoint;
      this.showModal = true;
      clearInterval(this.intervalId);
      this.fetchLogs();
      this.fetchChartData();
      this.intervalId = setInterval(() => {
        this.fetchLogs();
        this.fetchChartData();
      }, 15000);
    },
    closeModal() {
      this.showModal = false;
      this.selectedEndpoint = null;
      this.logs = [];
      clearInterval(this.intervalId);
    },
    downloadLogs() {
      if (!this.logs.length) return;

      const csv = [
        ['timestamp', 'user', 'role', 'method', 'url', 'duration'],
        ...this.logs.map(log => [
          log.timestamp, log.user, log.role, log.method, log.url, log.duration,
        ])
      ]
          .map(row => row.map(cell => `"${String(cell).replace(/"/g, '""')}"`).join(','))
          .join('\n');

      const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `logs-${this.selectedEndpoint.url.replace(/\W+/g, '_')}.csv`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },
    filterLogs() {
      const { user, role, status } = this.filters;
      this.filteredLogs = this.logs.filter(log => {

        const userMatch = !user || (
            log.user &&
            log.user.toLowerCase().includes(user.toLowerCase())
        );

        // ROLE фильтр — ROLE_ префикс и приведение к upper
        const normalizedFilterRole = role
            ? role.toUpperCase().startsWith('ROLE_') ? role.toUpperCase() : 'ROLE_' + role.toUpperCase()
            : '';

        const roleMatch = !role || log.role.toUpperCase() === normalizedFilterRole;

        // STATUS фильтр — точное сравнение
        const statusMatch = !status || log.status === status;

        return userMatch && roleMatch && statusMatch;
      });
    },

    getRoleClass(role) {
      const pure = role.replace('ROLE_', '');
      const base = 'px-2 inline-flex text-xs leading-5 font-semibold rounded-full';
      switch (pure) {
        case 'ADMIN': return `${base} text-red-800`;
        case 'MODERATOR': return `${base} text-blue-800`;
        case 'STUDENT': return `${base} text-green-800`;
        case 'AUTHENTICATED': return `${base} text-yellow-800`;
        case 'PUBLIC': return `${base} text-green-800`;
        default: return `${base} text-gray-800`;
      }
    },
  }
};
</script>

<style scoped>
table {
  border-collapse: collapse;
}
th, td {
  padding: 8px 12px;
}
</style>

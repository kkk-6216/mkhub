<template>
  <div class="max-w-7xl mx-auto p-6 bg-white rounded-lg">
    <div class="flex items-baseline justify-between border-b border-gray-200 pb-3 mb-3">
      <div class="flex items-center justify-center">
        <h2 class="text-2xl font-medium text-dark">Мониторинг микросервисов</h2>
        <svg v-if="loading" aria-hidden="true" class="inline w-5 h-5 text-gray-200 ml-3 animate-spin dark:text-gray-500 fill-dark" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"/>
          <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"/>
        </svg>
      </div>
    </div>

    <div class="flex items-baseline justify-between border-b border-gray-200 pb-3 mb-3">
      <table class="flex items-center text-sm text-dark mb-2">
        <tr>
          <td class="font-medium pr-10">Apps Hashcode:</td>
          <td class="bg-white px-2 py-1 text-red-500 font-mono">UP_2_</td>
        </tr>
        <tr>
          <td class="font-medium pr-10">Apps Hashcode:</td>
          <td class="bg-white px-2 py-1 text-red-500 font-mono">1</td>
        </tr>
      </table>
    </div>

    <div v-if="!loading" class="overflow-x-auto">
      <table class="w-3xl text-left">
        <thead>
        <tr class="border-b border-gray-300">
          <th class="py-2 text-xs font-medium text-gray-500">#</th>
          <th class="py-2 text-xs font-medium text-gray-500">Название</th>
          <th class="py-2 text-xs font-medium text-gray-500">Экземпляры</th>
        </tr>
        </thead>
        <tbody>
        <template v-for="(application, index) in applications.application" :key="index">
          <tr
              @click="toggleDetails(index)"
              class="cursor-pointer border-b border-gray-300 hover:bg-gray-50"
          >
            <td class="py-3 text-sm text-dark">{{ index + 1 }}</td>
            <td class="py-3 text-sm text-left text-dark hover:text-main">
              {{ application.name }}
            </td>
            <td class="py-3 text-sm text-dark">{{ application.instance.length }}</td>
          </tr>

          <tr v-if="expandedRowIndex === index">
            <td colspan="3" class="p-0">
              <transition name="slide-fade">
                <div class="overflow-hidden bg-gray-50 p-4">
                  <table class="w-full text-sm">
                    <thead>
                    <tr class="text-gray-500 text-xs border-b border-gray-300">
                      <th class="py-3 text-left text-gray-500 ">Экземпляр ID</th>
                      <th class="py-3 text-left text-gray-500 ">IP</th>
                      <th class="py-3 text-left text-gray-500 ">Статус</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr
                        v-for="(instance, idx) in application.instance"
                        :key="idx"
                        class="border-b border-gray-300 hover:bg-gray-100 cursor-pointer"
                        @click="openInstanceDetails(idx)"
                    >
                      <td class="py-3">{{ instance.instanceId }}</td>
                      <td class="py-3">{{ instance.ipAddr }}</td>
                      <td class="py-3">
                        <span
                            :class="{
                            'text-green-600': instance.status === 'UP',
                            'text-red-600': instance.status !== 'UP'
                          }"
                        >
                          {{ instance.status }}
                        </span>
                      </td>
                    </tr>
                    </tbody>
                  </table>
                </div>
              </transition>
            </td>
          </tr>
        </template>
        </tbody>
      </table>

      <p v-if="applications?.application.length === 0" class="text-center text-gray-500 py-4">
        Пока пусто
      </p>
    </div>


    <Modal v-if="isInstanceDetailsOpen" @close="closeInstanceDetails" width="w-[700px]">
    <h2 class="text-xl font-medium text-dark mb-6 text-left ml-4">Подробная информация</h2>

    <div v-if="instance" class="space-y-4 text-sm">
      <div class="grid grid-cols-2 gap-4 p-4 rounded-lg">
        <div class="font-medium text-gray-500">Instance ID:</div>
        <div>{{ instance.instanceId }}</div>

        <div class="font-medium text-gray-500">IP Address:</div>
        <div>{{ instance.ipAddr }}</div>

        <div class="font-medium text-gray-500">Home Page:</div>
        <div>
          <a :href="instance.homePageUrl" class="text-blue-600 hover:underline" target="_blank">
            {{ instance.homePageUrl }}
          </a>
        </div>

        <div class="font-medium text-gray-700">✅ Health Check:</div>
        <div>
          <a :href="instance.healthCheckUrl" class="text-green-600 hover:underline" target="_blank">
            {{ instance.healthCheckUrl }}
          </a>
        </div>

        <div class="font-medium text-gray-700">ℹ️ Status Page:</div>
        <div>
          <a :href="instance.statusPageUrl" class="text-purple-600 hover:underline" target="_blank">
            {{ instance.statusPageUrl }}
          </a>
        </div>

        <div class="font-medium text-gray-700">⚙️ Status:</div>
        <div>
        <span
            :class="{
            'text-green-600': instance.status === 'UP',
            'text-red-600': instance.status !== 'UP'
          }"
        >
          {{ instance.status }}
        </span>
        </div>

        <div class="font-medium text-gray-700">🛰️ App:</div>
        <div>{{ instance.app }}</div>

        <div class="font-medium text-gray-700">🔢 Port:</div>
        <div>{{ instance.port['$'] }}</div>

        <div class="font-medium text-gray-700">🔐 Secure Port:</div>
        <div>{{ instance.securePort['$'] }} (enabled: {{ instance.securePort['@enabled'] }})</div>

        <div class="font-medium text-gray-700">🌐 Host Name:</div>
        <div>{{ instance.hostName }}</div>

        <div class="font-medium text-gray-700">📡 VIP Address:</div>
        <div>{{ instance.vipAddress }}</div>

        <div class="font-medium text-gray-700">🔁 Last Renewal:</div>
        <div>{{ formatTimestamp(instance.leaseInfo.lastRenewalTimestamp) }}</div>
      </div>
    </div>
  </Modal>


  </div>
</template>

<script>

import AdminService from "@/modules/admin/service/admin-service.js";
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import Modal from "@/modules/moderator/components/Modal.vue";

export default {
  name: 'EurekaOverview',
  components: {Modal, DefaultButton},
  data() {
    return {
      applications: {},
      loading: true,
      expandedRowIndex: null,

      isInstanceDetailsOpen: false,
      selectedIndex: null,
      instance: {},

    }
  },
  mounted() {
    this.fetchOverview()
  },
  methods: {
    toggleDetails(index) {
      this.expandedRowIndex = this.expandedRowIndex === index ? null : index;
    },
    async fetchOverview() {
      try {
        const response = await AdminService.getEurekaOverview()
        this.applications = response.data?.applications;
      } catch (error) {
        this.showAlert('error', `Ошибка при получении данных от Eureka: ${error}`);
      } finally {
        this.loading = false
      }
    },
    formatTimestamp(ts) {
      const date = new Date(Number(ts));
      return date.toLocaleString();
    },

    closeInstanceDetails() {
      this.isInstanceDetailsOpen = false;
    },

    openInstanceDetails(index) {
      this.selectedIndex = index;
      this.instance = {...this.applications.application[this.expandedRowIndex].instance[index]};
      console.log(this.instance);
      this.isInstanceDetailsOpen = true;
    },
  },
  inject: ['showAlert'],
}
</script>

<style scoped>
/* Стили по желанию */
table {
  border-collapse: collapse;
}
th, td {
  text-align: left;
}
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.3s ease;
}
.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateY(-10px);
  opacity: 0;
}
</style>

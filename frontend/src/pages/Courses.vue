<template>
  <div class="max-w-7xl mx-auto p-6 bg-white rounded-lg">
    <!-- Sarlavha va Fakultet qo'shish tugmasi -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-semibold text-dark">Список факультетов</h2>
      <button @click="openAddPanel"
              class="text-white px-3 py-1 rounded-md hover:opacity-90 flex items-center bg-main hover:bg-main-hover">
        Создать
        <svg class="w-4 h-4 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
        </svg>
      </button>
    </div>

    <!-- Jadval -->
    <div class="overflow-x-auto">
      <table class="w-full text-left bg-white">
        <thead>
        <tr class="border-b border-gray-300">
          <th class="py-2 text-xs font-medium text-gray-500">#</th>
          <th class="py-2 text-xs font-medium text-gray-500">Название</th>
          <th class="py-2 text-xs font-medium text-gray-500 text-right">Действие</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(faculty, index) in faculties" :key="index" class="border-b border-gray-300 hover:bg-gray-50">
          <td class="py-3 text-xs text-dark">{{ index + 1 }}</td>
          <td @click="openFacultyDetails(index)" class="py-3 cursor-pointer text-xs text-left text-dark hover:text-main">
              {{ faculty.name }}
          </td>
          <td class="py-3 text-right">
            <button @click="openEditPanel(index)" class="p-1 bg-gray-200 rounded-md hover:bg-gray-300 ">
              <svg class="w-4 h-4 text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"></path>
              </svg>
            </button>
            <button @click="openDeleteConfirmation(index)" class="p-1 bg-gray-200 rounded-md hover:bg-gray-300 ml-2 ">
              <svg class="w-4 h-4 text-red-700" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
              </svg>
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal oyna: Fakultetni o'chirishni tasdiqlash -->
    <div v-if="isDeleteConfirmationOpen" class="fixed inset-5 flex items-center justify-center backdrop-blur-sm">
      <div class="bg-white bg-opacity-90 p-6 rounded-lg shadow-lg w-1/3 border border-gray-200">
        <h3 class="text-xl font-bold mb-4">Подтверждение удаления</h3>
        <p class="text-gray-700 mb-4">Вы уверены, что хотите удалить этот факультет?</p>
        <div class="flex justify-end space-x-2">
          <button @click="confirmDelete" class="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600">Удалить</button>
          <button @click="closeDeleteConfirmation" class="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600">Отмена</button>
        </div>
      </div>
    </div>

    <!-- Modal oyna: Fakultet qo'shish -->
    <div v-if="isAddPanelOpen" class="fixed inset-0 flex items-center justify-center backdrop-blur-sm">
      <div class="bg-white bg-opacity-90 p-6 rounded-lg shadow-lg w-2/5 border border-gray-200">
        <h3 class="text-xl font-bold mb-4">Создать факультет</h3>
        <h1>Название факультета</h1>
        <input v-model="newFaculty.name" type="text" class="w-full p-2 border rounded-lg mb-4" />
        <h1>Описание факультета</h1>
        <textarea v-model="newFaculty.description" class="w-full p-4 border rounded-lg mb-4"></textarea>
        <div class="flex justify-end space-x-2">
          <button @click="closeAddPanel" class="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600">Отмена</button>
          <button @click="addFaculty" class="px-4 py-2 bg-dark text-white rounded-lg hover:bg-dark">Сохранить</button>
        </div>
      </div>
    </div>

    <!-- Modal oyna: Fakultetni tahrirlash -->
    <div v-if="isEditPanelOpen" class="fixed inset-0 flex items-center justify-center backdrop-blur-sm">
      <div class="bg-white bg-opacity-90 p-6 rounded-lg shadow-lg w-1/3 border border-gray-200">
        <h3 class="text-xl font-bold mb-4">Редактировать факультет</h3>
        <input v-model="editedFaculty.name" type="text" class="w-full p-2 border rounded-lg mb-4" />
        <textarea v-model="editedFaculty.description" class="w-full p-2 border rounded-lg mb-4"></textarea>
        <div class="flex justify-end space-x-2">
          <button @click="saveFaculty" class="px-4 py-2 bg-dark text-white rounded-lg hover:bg-dark">Сохранить</button>
          <button @click="closeEditPanel" class="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600">Отмена</button>
        </div>
      </div>
    </div>

    <!-- Modal oyna: Fakultet haqida ma'lumot -->
    <div v-if="isFacultyDetailsOpen" class="fixed inset-0 flex items-center justify-center backdrop-blur-sm">
      <div class="bg-white bg-opacity-90 p-6 rounded-lg shadow-lg w-1/3 border border-gray-200">
        <h3 class="text-xl font-bold mb-4">{{ selectedFaculty.name }}</h3>
        <p class="text-gray-700 mb-4">{{ selectedFaculty.description }}</p>
        <div class="flex justify-end w-full">
          <button @click="closeFacultyDetails" class="w-1/4 bg-blue-400 text-white py-1 rounded-md hover:bg-blue-600">
            OK
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      faculties: [
        { name: "Факультет информационных технологий", description: "Подготовка специалистов в области IT и программирования." },
        { name: "Факультет математики и механики", description: "Исследования в области математики и механики." },
        { name: "Факультет медицины", description: "Подготовка квалифицированных врачей и медицинских специалистов." },
      ],
      newFaculty: { name: "", description: "" },
      isAddPanelOpen: false,
      isEditPanelOpen: false,
      isFacultyDetailsOpen: false,
      isDeleteConfirmationOpen: false,
      editedFaculty: { name: "", description: "" },
      editIndex: null,
      selectedFaculty: { name: "", description: "" },
      deleteIndex: null,
    };
  },
  methods: {
    // Открыть панель добавления
    openAddPanel() {
      this.isAddPanelOpen = true;
      this.isEditPanelOpen = false;
    },
    // Закрыть панель добавления
    closeAddPanel() {
      this.isAddPanelOpen = false;
      this.newFaculty = { name: "", description: "" };
    },
    // Открыть панель редактирования
    openEditPanel(index) {
      this.editIndex = index;
      this.editedFaculty = { ...this.faculties[index] };
      this.isEditPanelOpen = true;
    },
    // Закрыть панель редактирования
    closeEditPanel() {
      this.isEditPanelOpen = false;
      this.editedFaculty = { name: "", description: "" };
      this.editIndex = null;
    },
    // Открыть детали факультета
    openFacultyDetails(index) {
      this.selectedFaculty = { ...this.faculties[index] };
      this.isFacultyDetailsOpen = true;
    },
    // Закрыть детали факультета
    closeFacultyDetails() {
      this.isFacultyDetailsOpen = false;
      this.selectedFaculty = { name: "", description: "" };
    },
    // Добавить факультет
    addFaculty() {
      if (this.newFaculty.name.trim() && this.newFaculty.description.trim()) {
        this.faculties.push({ ...this.newFaculty });
        this.closeAddPanel();
      }
    },
    // Сохранить изменения факультета
    saveFaculty() {
      if (this.editedFaculty.name.trim() && this.editedFaculty.description.trim() && this.editIndex !== null) {
        this.faculties[this.editIndex] = { ...this.editedFaculty };
        this.closeEditPanel();
      }
    },
    // Открыть подтверждение удаления
    openDeleteConfirmation(index) {
      this.deleteIndex = index;
      this.isDeleteConfirmationOpen = true;
    },
    // Закрыть подтверждение удаления
    closeDeleteConfirmation() {
      this.isDeleteConfirmationOpen = false;
      this.deleteIndex = null;
    },
    // Подтвердить удаление
    confirmDelete() {
      if (this.deleteIndex !== null) {
        this.faculties.splice(this.deleteIndex, 1);
        this.closeDeleteConfirmation();
      }
    },
  },
};
</script>

<style scoped>
/* Стили для таблицы */
table {
  width: 100%;
  border-collapse: collapse;
}
th, td {
  padding: 8px 12px;
}
th {
  color: #6b7280;
  font-weight: 500;
  text-transform: uppercase;
  font-size: 0.75rem;
}
td {
  font-size: 0.75rem;
}
tr:hover {
  background-color: #f9fafb;
}
</style>
<template>
  <div class="max-w-7xl mx-auto p-6 bg-white rounded-lg">
    <!-- Sarlavha va Bo'lim qo'shish tugmasi -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-medium text-dark">Список кафедр</h2>
      <button @click="openAddPanel"
              class="text-white px-3 py-1 rounded-md hover:opacity-90 flex items-center bg-main hover:bg-main-hover">
        Создать
        <svg class="w-4 h-4 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
        </svg>
      </button>
    </div>

    <!-- Modal oyna: Bo'lim qo'shish -->
    <Modal v-if="isAddPanelOpen" @close="closeAddPanel">
      <form action="" class="space-y-12">
        <div class="pb-1">
          <h2 class="text-base/7 font-medium text-gray-900">Создать кафедру</h2>
          <div class="mt-5 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
            <!-- Bo'lim nomi -->
            <div class="sm:col-span-4">
              <label for="departmentName" class="block text-sm/6 font-medium text-gray-900">Название</label>
              <div class="mt-2">
                <div class="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-main">
                  <input v-model="newDepartment.name" type="text" id="departmentName" class="block min-w-0 grow py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-400 focus:outline-none sm:text-sm/6">
                </div>
              </div>
            </div>

            <!-- Bo'lim tavsifi -->
            <div class="col-span-full">
              <label for="departmentDescription" class="block text-sm/6 font-medium text-gray-900">Описание</label>
              <div class="mt-2">
                <textarea v-model="newDepartment.description" id="departmentDescription" rows="3" class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"></textarea>
              </div>
            </div>

            <!-- Fakultetni tanlash -->
            <div class="col-span-full">
              <label for="facultySearch" class="block text-sm/6 font-medium text-gray-900">Выберите факультет</label>
              <div class="mt-2 relative">
                <input v-model="facultySearchQuery" type="text" id="facultySearch" placeholder="Поиск факультета" class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6">
                <ul v-if="filteredFaculties.length > 0" class="absolute z-10 mt-1 w-full bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-y-auto">
                  <li v-for="faculty in filteredFaculties" :key="faculty.id" @click="selectFaculty(faculty)" class="px-4 py-2 cursor-pointer hover:bg-gray-100">
                    {{ faculty.name }}
                  </li>
                </ul>
              </div>
            </div>

            <!-- Tanlangan fakultet -->
            <div v-if="selectedFaculty" class="col-span-full">
              <p class="text-sm text-gray-700">Выбранный факультет: <span class="font-medium">{{ selectedFaculty.name }}</span></p>
            </div>

            <!-- Tugmalar -->
            <div class="col-span-full">
              <div class="flex justify-end space-x-2">
                <DefaultButton label="Отмена" type="button" variant="secondary" @click="closeAddPanel"></DefaultButton>
                <DefaultButton label="Сохранить" type="button" variant="primary" @click="addDepartment"></DefaultButton>
              </div>
            </div>
          </div>
        </div>
      </form>
    </Modal>

    <!-- Таблица -->
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
        <tr v-for="(department, index) in departments" :key="index" class="border-b border-gray-300 hover:bg-gray-50">
          <td class="py-3 text-sm text-dark">{{ index + 1 }}</td>
          <td @click="openDepartmentDetails(index)" class="py-3 cursor-pointer text-sm text-left text-dark hover:text-main">
            {{ department.name }}
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

    <!-- Modal oyna: Bo'limni o'chirishni tasdiqlash -->
    <Modal v-if="isDeleteConfirmationOpen" @close="closeDeleteConfirmation" width="w-[430px]">
      <h3 class="text-xl font-medium mb-5 text-dark">Подтверждение удаления</h3>
      <p class="text-gray-700  mb-5">Вы уверены, что хотите удалить этот кафедру?</p>
      <div class="flex justify-end  space-x-2">
        <DefaultButton label="Удалить" size="sm" type="button" variant="danger" @click="confirmDelete"></DefaultButton>
        <DefaultButton label="Отмена" size="sm" type="button" variant="secondary" @click="closeDeleteConfirmation"></DefaultButton>
      </div>
    </Modal>

    <!-- Modal oyna: Bo'limni tahrirlash -->
    <Modal v-if="isEditPanelOpen" @close="closeEditPanel">
      <form action="" class="space-y-12">
        <div class="pb-1">
          <h2 class="text-base/7 font-medium text-gray-900">Редактировать кафедру</h2>
          <div class="mt-5 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
            <div class="sm:col-span-4">
              <label for="username" class="block text-sm/6 font-medium text-gray-900">Название</label>
              <div class="mt-2">
                <div class="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-main">
                  <input v-model="editedDepartment.name" type="text" name="username" id="username" class="block min-w-0 grow py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-400 focus:outline-none sm:text-sm/6">
                </div>
              </div>
            </div>
            <div class="col-span-full">
              <label for="about" class="block text-sm/6 font-bold text-gray-900">Описание</label>
              <div class="mt-2">
                <textarea v-model="editedDepartment.description" name="about" id="about" rows="3" class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"></textarea>
              </div>
            </div>
            <div class="col-span-full">
              <div class="flex justify-end space-x-2">
                <DefaultButton label="Отмена" type="button" variant="secondary" @click="closeEditPanel"></DefaultButton>
                <DefaultButton label="Сохранить" type="button" variant="primary" @click="saveDepartment"></DefaultButton>
              </div>
            </div>
          </div>
        </div>
      </form>
    </Modal>

    <!-- Modal oyna: Bo'lim haqida ma'lumot -->
    <Modal v-if="isDepartmentDetailsOpen" @close="closeDepartmentDetails">
      <h2 class="text-xl font-medium text-dark mb-6 text-left">Информация о кафедре</h2>

      <!-- Название кафедры -->
      <div class="mb-4">
        <label class="block text-base/6 font-medium text-gray-600">Название</label>
        <p class="py-5 text-sm text-gray-900">
          {{ selectedDepartment.name }}
        </p>
      </div>

      <!-- Описание кафедры -->
      <div>
        <label class="block text-base/6 font-medium text-gray-600">Описание</label>
        <p class="py-5 text-sm text-gray-900">
          {{ selectedDepartment.description }}
        </p>
      </div>

      <!-- Кнопка -->
      <div class="flex justify-end mt-6">
        <DefaultButton label="OK" type="button" variant="primary" @click="closeDepartmentDetails"></DefaultButton>
      </div>
    </Modal>
  </div>
</template>

<script>
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import Modal from "@/modules/faculty/components/Modal.vue";

export default {
  name: "Department",
  components: {DefaultButton, Modal},
  data() {
    return {
      faculties: [
        { id: 1, name: "Факультет информационных технологий" },
        { id: 2, name: "Факультет математики и механики" },
        { id: 3, name: "Факультет медицины" },
      ],
      departments: [
        {
          name: "Кафедра программирования",
          description: "Кафедра, занимающаяся подготовкой специалистов в области программирования.",
          facultyId: 1,
        },
        {
          name: "Кафедра кибербезопасности",
          description: "Кафедра, занимающаяся подготовкой специалистов в области кибербезопасности.",
          facultyId: 1,
        },
        {
          name: "Кафедра искусственного интеллекта",
          description: "Кафедра, занимающаяся подготовкой специалистов в области искусственного интеллекта.",
          facultyId: 1,
        },
      ],
      newDepartment: { name: "", description: "", facultyId: null },
      isAddPanelOpen: false,
      isEditPanelOpen: false,
      isDepartmentDetailsOpen: false,
      isDeleteConfirmationOpen: false,
      editedDepartment: { name: "", description: "", facultyId: null },
      editIndex: null,
      selectedDepartment: { name: "", description: "", facultyId: null },
      deleteIndex: null,
      facultySearchQuery: "",
      selectedFaculty: null,
    };
  },
  computed: {
    // Qidiruv natijasiga ko'ra fakultetlarni filtrlash
    filteredFaculties() {
      if (this.facultySearchQuery) {
        return this.faculties.filter(faculty =>
            faculty.name.toLowerCase().includes(this.facultySearchQuery.toLowerCase())
        );
      }
      return this.faculties;
    },
  },
  methods: {
    openAddPanel() {
      this.isAddPanelOpen = true;
      this.facultySearchQuery = "";
      this.selectedFaculty = null;
    },
    closeAddPanel() {
      this.isAddPanelOpen = false;
      this.newDepartment = { name: "", description: "", facultyId: null };
    },
    // Fakultetni tanlash
    selectFaculty(faculty) {
      this.selectedFaculty = faculty;
      this.newDepartment.facultyId = faculty.id;
      this.facultySearchQuery = ""; // Qidiruv maydonini tozalash
    },
    // Bo'lim qo'shish
    addDepartment() {
      if (this.newDepartment.name.trim() && this.newDepartment.description.trim() && this.newDepartment.facultyId) {
        this.departments.push({ ...this.newDepartment });
        this.closeAddPanel();
      } else {
        alert("Iltimos, barcha maydonlarni to'ldiring va fakultetni tanlang.");
      }
    },
    openEditPanel(index) {
      this.editIndex = index;
      this.editedDepartment = { ...this.departments[index] };
      this.isEditPanelOpen = true;
    },
    closeEditPanel() {
      this.isEditPanelOpen = false;
      this.editedDepartment = { name: "", description: "", facultyId: null };
      this.editIndex = null;
    },
    saveDepartment() {
      if (this.editedDepartment.name.trim() && this.editedDepartment.description.trim() && this.editIndex !== null) {
        this.departments[this.editIndex] = { ...this.editedDepartment };
        this.closeEditPanel();
      }
    },
    openDepartmentDetails(index) {
      this.selectedDepartment = { ...this.departments[index] };
      this.isDepartmentDetailsOpen = true;
    },
    closeDepartmentDetails() {
      this.isDepartmentDetailsOpen = false;
      this.selectedDepartment = { name: "", description: "", facultyId: null };
    },
    openDeleteConfirmation(index) {
      this.deleteIndex = index;
      this.isDeleteConfirmationOpen = true;
    },
    closeDeleteConfirmation() {
      this.isDeleteConfirmationOpen = false;
      this.deleteIndex = null;
    },
    confirmDelete() {
      if (this.deleteIndex !== null) {
        this.departments.splice(this.deleteIndex, 1);
        this.closeDeleteConfirmation();
      }
    },
  },
};
</script>

<style scoped>
/* Qo'shimcha stil */
ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}
</style>
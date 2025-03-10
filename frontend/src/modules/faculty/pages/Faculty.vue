<template>
  <div class="max-w-7xl mx-auto p-6 bg-white rounded-lg">
    <!-- Sarlavha va Fakultet qo'shish tugmasi -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-medium text-dark">Список факультетов</h2>
      <button @click="openAddPanel"
              class="text-white px-3 py-1 rounded-md hover:opacity-90 flex items-center bg-main hover:bg-main-hover">
        Создать
        <svg class="w-4 h-4 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
        </svg>
      </button>
    </div>

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
        <tr v-for="(faculty, index) in faculties" :key="index" class="border-b border-gray-300 hover:bg-gray-50">
          <td class="py-3 text-sm text-dark">{{ index + 1 }}</td>
          <td @click="openFacultyDetails(index)" class="py-3 cursor-pointer text-sm text-left text-dark hover:text-main">
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
    <Modal v-if="isDeleteConfirmationOpen" @close="closeDeleteConfirmation">
      <h3 class="text-xl font-medium mb-5 text-dark">Подтверждение удаления</h3>
      <p class="text-gray-700 mb-5">Вы уверены, что хотите удалить этот факультет?</p>
      <div class="flex justify-end space-x-2">
        <DefaultButton label="Удалить" size="sm" type="button" variant="danger" @click="confirmDelete"></DefaultButton>
        <DefaultButton label="Отмена" size="sm" type="button" variant="secondary" @click="closeDeleteConfirmation"></DefaultButton>
      </div>
    </Modal>

    <!-- Modal oyna: Fakultet qo'shish -->
    <Modal v-if="isAddPanelOpen" @close="closeAddPanel">
      <form action="" class="space-y-12">
        <div class="pb-1">
          <h2 class="text-base/7 font-medium text-gray-900">Создать факультет</h2>
          <div class="mt-5 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
            <div class="sm:col-span-4">
              <label for="username" class="block text-sm/6 font-medium text-gray-900">Название</label>
              <div class="mt-2">
                <div class="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-main">
                  <input v-model="newFaculty.name" type="text" name="username" id="username" class="block min-w-0 grow py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-400 focus:outline-none sm:text-sm/6">
                </div>
              </div>
            </div>
            <div class="col-span-full">
              <label for="about" class="block text-sm/6 font-medium text-gray-900">Описание</label>
              <div class="mt-2">
                <textarea v-model="newFaculty.description" name="about" id="about" rows="3" class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"></textarea>
              </div>
            </div>
            <div class="col-span-full">
              <div class="flex justify-end space-x-2">
                <DefaultButton label="Отмена" type="button" variant="secondary" @click="closeAddPanel"></DefaultButton>
                <DefaultButton label="Сохранить" type="button" variant="primary" @click="addFaculty"></DefaultButton>
              </div>
            </div>
          </div>
        </div>
      </form>
    </Modal>

    <!-- Modal oyna: Fakultetni tahrirlash -->
    <Modal v-if="isEditPanelOpen" @close="closeEditPanel">
      <form action="" class="space-y-12">
        <div class="pb-1">
          <h2 class="text-base/7 font-medium text-gray-900">Редактировать факультет</h2>
          <div class="mt-5 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
            <div class="sm:col-span-4">
              <label for="username" class="block text-sm/6 font-medium text-gray-900">Название</label>
              <div class="mt-2">
                <div class="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-main">
                  <input v-model="editedFaculty.name" type="text" name="username" id="username" class="block min-w-0 grow py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-400 focus:outline-none sm:text-sm/6">
                </div>
              </div>
            </div>
            <div class="col-span-full">
              <label for="about" class="block text-sm/6 font-bold text-gray-900">Описание</label>
              <div class="mt-2">
                <textarea v-model="editedFaculty.description" name="about" id="about" rows="3" class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"></textarea>
              </div>
            </div>
            <div class="col-span-full">
              <div class="flex justify-end space-x-2">
                <DefaultButton label="Отмена" type="button" variant="secondary" @click="closeEditPanel"></DefaultButton>
                <DefaultButton label="Сохранить" type="button" variant="primary" @click="saveFaculty"></DefaultButton>
              </div>
            </div>
          </div>
        </div>
      </form>
    </Modal>

    <!-- Modal oyna: Fakultet haqida ma'lumot -->
<!--    <Modal v-if="isFacultyDetailsOpen" @close="closeFacultyDetails">-->
<!--     <h2 class="text-base/7 font-medium mb-4">{{ selectedFaculty.name }}</h2>-->
<!--      <p class="text-gray-700 mb-4">{{ selectedFaculty.description }}</p>-->
<!--      <div class="flex justify-end w-full">-->
<!--        <button @click="closeFacultyDetails" class="w-1/4 bg-dark text-white py-1 rounded-md hover:bg-dark">-->
<!--          OK-->
<!--        </button>-->
<!--      </div>-->
<!--    </Modal>-->

    <Modal v-if="isFacultyDetailsOpen" @close="closeFacultyDetails">
      <h2 class="text-xl font-medium text-dark mb-6 text-left">Информация о факультете</h2>

      <!-- Название факультета -->
      <div class="mb-4">
        <label class="block text-base/6 font-medium text-gray-600">Название</label>
        <p class="py-5 text-sm text-gray-900">
          {{ selectedFaculty.name }}
        </p>
      </div>

      <!-- Описание факультета -->
      <div>
        <label class="block text-base/6 font-medium text-gray-600">Описание</label>
        <p class="py-5 text-sm text-gray-900">
          {{ selectedFaculty.description }}
        </p>
      </div>

      <!-- Информация о кафедрах -->
      <div class="mt-6 border-t pt-4">
        <div class="flex items-center cursor-pointer" @click="toggleDepartments">
          <svg class="w-5 h-5 text-black mr-2" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"
               xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" d="M19 9l-7 7-7-7"></path>
          </svg>
          <span class="text-base font-medium text-gray-600">Информация о кафедрах</span>
        </div>

        <ul v-if="isDepartmentsOpen" class="mt-3 text-sm text-gray-900">
          <li v-for="department in selectedFaculty.departments" :key="department.id" class="py-1">
            {{ department.name }}
          </li>
        </ul>
      </div>

      <!-- Кнопка -->
      <div class="flex justify-end mt-6">
        <DefaultButton label="OK" type="button" variant="primary" @click="closeFacultyDetails"></DefaultButton>
      </div>
    </Modal>


  </div>

</template>

<script>
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import Modal from "@/modules/faculty/components/Modal.vue";

export default {
  name: "Faculty",
  components: {DefaultButton, Modal},
  data() {
    return {
      faculties: [
        {
          name: "Факультет информационных технологий",
          description: "Подготовка специалистов в области IT и программирования.",
          departments: [
            {id: 1, name: "Кафедра программирования"},
            {id: 2, name: "Кафедра кибербезопасности"},
            {id: 3, name: "Кафедра искусственного интеллекта"},
            {id: 4, name: "Кафедра веб-разработки"},
            {id: 5, name: "Кафедра мобильной разработки"},
          ],
        },
        {
          name: "Факультет математики и механики",
          description: "Исследования в области математики и механики.",
          departments: [
            {id: 1, name: "Кафедра алгебры"},
            {id: 2, name: "Кафедра механики"},
            {id: 3, name: "Кафедра теории чисел"},
            {id: 4, name: "Кафедра статистики"},
            {id: 5, name: "Кафедра математического моделирования"},
          ],
        },
        {
          name: "Факультет медицины",
          description: "Подготовка квалифицированных врачей и медицинских специалистов.",
          departments: [
            {id: 1, name: "Кафедра хирургии"},
            {id: 2, name: "Кафедра кардиологии"},
            {id: 3, name: "Кафедра неврологии"},
            {id: 4, name: "Кафедра педиатрии"},
            {id: 5, name: "Кафедра реабилитации"},
          ],
        },
      ],
      newFaculty: {name: "", description: "", departments: []},
      isAddPanelOpen: false,
      isEditPanelOpen: false,
      isFacultyDetailsOpen: false,
      isDeleteConfirmationOpen: false,
      editedFaculty: {name: "", description: "", departments: []},
      editIndex: null,
      selectedFaculty: {name: "", description: "", departments: []},
      deleteIndex: null,
      isDepartmentsOpen: false, // Bo‘limlar ro‘yxati ochiq/yopiq holati
    };
  },
  methods: {
    openAddPanel() {
      this.isAddPanelOpen = true;
      this.isEditPanelOpen = false;
    },
    closeAddPanel() {
      this.isAddPanelOpen = false;
      this.newFaculty = {name: "", description: "", departments: []};
    },
    openEditPanel(index) {
      this.editIndex = index;
      this.editedFaculty = {...this.faculties[index]};
      this.isEditPanelOpen = true;
    },
    closeEditPanel() {
      this.isEditPanelOpen = false;
      this.editedFaculty = {name: "", description: "", departments: []};
      this.editIndex = null;
    },
    openFacultyDetails(index) {
      this.selectedFaculty = {...this.faculties[index]};
      this.isFacultyDetailsOpen = true;
    },
    closeFacultyDetails() {
      this.isFacultyDetailsOpen = false;
      this.selectedFaculty = {name: "", description: "", departments: []};
    },
    addFaculty() {
      if (this.newFaculty.name.trim() && this.newFaculty.description.trim()) {
        this.faculties.push({...this.newFaculty});
        this.closeAddPanel();
      }
    },
    saveFaculty() {
      if (this.editedFaculty.name.trim() && this.editedFaculty.description.trim() && this.editIndex !== null) {
        this.faculties[this.editIndex] = {...this.editedFaculty};
        this.closeEditPanel();
      }
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
        this.faculties.splice(this.deleteIndex, 1);
        this.closeDeleteConfirmation();
      }
    },
    // Kafedralarni asta-sekin chiqarish yoki yashirish
    toggleDepartments() {
      this.isDepartmentsOpen = !this.isDepartmentsOpen;
    }
  }
}
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
  font-weight: 500;
  text-transform: uppercase;
}
tr:hover {
  background-color: #f9fafb;
}
</style>
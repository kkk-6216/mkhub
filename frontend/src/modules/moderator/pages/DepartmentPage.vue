<template>
  <div class="max-w-7xl mx-auto p-6 bg-white rounded-lg">

    <div class="flex justify-between items-center mb-6">
      <div class="flex items-center justify-center">
        <h2 class="text-2xl font-medium text-dark">Список кафедр</h2>
        <svg v-if="loading" aria-hidden="true" class="inline w-5 h-5 text-gray-200 ml-3 animate-spin dark:text-gray-500 fill-dark" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"/>
          <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"/>
        </svg>
      </div>
      <button @click="openAddPanel"
              class="text-white px-3 py-1 rounded-md hover:opacity-90 flex items-center bg-main hover:bg-main-hover">
        Создать
        <svg class="w-4 h-4 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
        </svg>
      </button>
    </div>

    <Modal v-if="isAddPanelOpen" @close="closeAddPanel">
      <form class="space-y-12">
        <div class="pb-1">
          <h2 class="text-base/7 font-medium text-dark">Создать кафедру</h2>
          <div class="mt-5 grid grid-cols-1 gap-x-6 gap-y-6 sm:grid-cols-6">
            <div class="sm:col-span-4">
              <InputItem
                  v-model.trim="newDepartment.name"
                  :minLength="3"
                  :maxLength="50"
                  @isValidated="isValidated"
                  label="Название"
                  name="newDepartmentNameInput"
                  ref="newDepartmentNameInput"
              />
            </div>
            <div class="col-span-full">
              <TextareaItem
                  v-model.trim="newDepartment.description"
                  label="Описание"
                  placeholder="(опционально)"
                  name="about-1"
              />
            </div>

            <div class="col-span-full relative">
              <label for="facultySearch" class="block text-sm/6 font-medium text-dark">
                Выберите факультет
              </label>
              <div class="mt-2 relative">
                <input
                    v-model="facultySearchQuery"
                    type="text"
                    id="facultySearch"
                    placeholder="Поиск факультета"
                    class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-dark outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                    @focus="isFacultyListOpen = true"
                    @blur="closeFacultyList"
                    @keydown.enter.prevent="selectFirstFaculty"
                />

                <ul
                    v-if="isFacultyListOpen && filteredFaculties.length > 0"
                    class="absolute z-10 mt-1 w-full bg-white border border-gray-300 rounded-md shadow-lg max-h-32 overflow-y-auto"
                >
                  <li
                      v-for="faculty in filteredFaculties"
                      :key="faculty.id"
                      @click="selectFaculty(faculty)"
                      class="px-4 py-2 cursor-pointer hover:bg-gray-100"
                  >
                    {{ faculty.name }}
                  </li>
                </ul>
              </div>
              <p v-if="!isSelectValid" class="mt-1 text-sm text-red-500">
                {{ validationSelectMessage }}
              </p>
            </div>

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

    <div v-if="!loading" class="overflow-x-auto">
      <table class="w-full text-left bg-white">
        <thead>
          <tr class="border-b border-gray-300">
            <th class="py-2 text-xs font-medium text-gray-500">#</th>
            <th class="py-2 text-xs font-medium text-gray-500">Название</th>
            <th class="py-2 text-xs font-medium text-gray-500">Факультет</th>
            <th class="py-2 text-xs font-medium text-gray-500 text-right">Действие</th>
          </tr>
        </thead>
        <tbody>
        <tr v-for="(department, index) in departments" :key="index" class="border-b border-gray-300 hover:bg-gray-50">
          <td class="py-3 text-sm text-dark">{{ index + 1 }}</td>
          <td @click="openDepartmentDetails(index)" class="py-3 cursor-pointer text-sm text-left text-dark hover:text-main">
            {{ department.name }}
          </td>
          <td class="py-3 text-sm text-dark">{{ department.faculty.name }}</td>
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
      <p v-if="departments.length === 0" class="text-center text-gray-500 py-4">
        Пока пусто. Добавьте первую кафедру!
      </p>
    </div>

    <Modal v-if="isDeleteConfirmationOpen" @close="closeDeleteConfirmation" width="w-[430px]">
      <h3 class="text-xl font-medium mb-5 text-dark">Подтверждение удаления</h3>
      <p class="text-gray-700  mb-5">Вы уверены, что хотите удалить этот кафедру?</p>
      <div class="flex justify-end  space-x-2">
        <DefaultButton label="Удалить" size="sm" type="button" variant="danger" @click="confirmDelete"></DefaultButton>
        <DefaultButton label="Отмена" size="sm" type="button" variant="secondary" @click="closeDeleteConfirmation"></DefaultButton>
      </div>
    </Modal>

    <Modal v-if="isEditPanelOpen" @close="closeEditPanel">
      <form class="space-y-12">
        <div class="pb-1">
          <h2 class="text-base/7 font-medium text-dark">Редактировать кафедру</h2>
          <div class="mt-5 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
            <div class="sm:col-span-4">
              <InputItem
                  v-model.trim="editedDepartment.name"
                  :minLength="3"
                  :maxLength="50"
                  @isValidated="isValidated"
                  label="Название"
                  name="updatedDepartmentNameInput"
                  ref="updatedDepartmentNameInput"
              />
            </div>
            <div class="col-span-full">
              <TextareaItem
                  v-model.trim="editedDepartment.description"
                  label="Описание"
                  name="about-2"
                  placeholder="(опционально)"
              />
            </div>
            <div class="col-span-full">
              <div class="flex justify-end space-x-2">
                <DefaultButton label="Отмена" type="button" variant="secondary" @click="closeEditPanel"></DefaultButton>
                <DefaultButton label="Сохранить" type="button" variant="primary" @click="updateDepartment"></DefaultButton>
              </div>
            </div>
          </div>
        </div>
      </form>
    </Modal>

    <Modal v-if="isDepartmentDetailsOpen" @close="closeDepartmentDetails">
      <h2 class="text-xl font-medium text-dark mb-6 text-left">Информация о кафедре</h2>

      <div class="mb-4">
        <label class="block text-base/6 font-medium text-gray-500">Название</label>
        <p class="py-5 text-sm text-dark">
          {{ selectedDepartment.name }}
        </p>
      </div>

      <div>
        <label class="block text-base/6 font-medium text-gray-500">Описание</label>
        <p class="py-5 text-sm text-dark">
          {{ displayDepartmentDescription }}
        </p>
      </div>

      <div>
        <label class="block text-base/6 font-medium text-gray-500">Факультет</label>
        <p class="py-5 text-sm text-dark">
          {{ selectedDepartment.faculty.name }}
        </p>
      </div>

      <div class="flex justify-end mt-6">
        <DefaultButton label="OK" type="button" variant="primary" @click="closeDepartmentDetails"></DefaultButton>
      </div>
    </Modal>

  </div>
</template>
<script>
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import Modal from "@/modules/moderator/components/Modal.vue";
import InputItem from "@/modules/moderator/components/InputItem.vue";
import TextareaItem from "@/modules/moderator/components/TextareaItem.vue";
import { getFaculties } from "@/modules/moderator/service/facultyService.js";
import { getDepartments, createDepartment, updateDepartment, deleteDepartment } from '@/modules/moderator/service/departmentService.js'

export default {
  name: "DepartmentPage",
  components: {TextareaItem, Modal, DefaultButton, InputItem },
  data() {
    return {

      faculties: [],
      departments: [],

      loading: false,

      isAddPanelOpen: false,
      isEditPanelOpen: false,
      isDepartmentDetailsOpen: false,
      isDeleteConfirmationOpen: false,
      isFacultyListOpen: false,

      newDepartment: { name: "", description: "", facultyId: null },
      editedDepartment: {},
      selectedDepartment: {},
      facultySearchQuery: "",

      deleteIndex: null,
      editIndex: null,

      fieldValidationStatus: {},
      isSelectValid: true,
      validationSelectMessage: '',


    };
  },
  computed: {
    filteredFaculties() {
      if (this.facultySearchQuery) {
        return this.faculties.filter((faculty) =>
            faculty.name.toLowerCase().includes(this.facultySearchQuery.toLowerCase())
        );
      }
      return this.faculties;
    },
    selectedFaculty() {
      return this.faculties.find(f => f.id === this.newDepartment.facultyId) || null;
    },
    displayDepartmentDescription() {
      const description = this.selectedDepartment?.description?.trim();
      return description ? description : '(Пусто)';
    }
  },
  methods: {

    selectFirstFaculty() {
      if (this.filteredFaculties.length > 0) {
        this.selectFaculty(this.filteredFaculties[0]);
      } else {
        this.isSelectValid = false;
        this.validationSelectMessage = 'Факультет не найден';
      }
    },

    isValidated({isValid, field}) {
      this.fieldValidationStatus[field] = isValid;
    },

    openAddPanel() {
      this.isAddPanelOpen = true;
      this.facultySearchQuery = "";
      this.newDepartment = { name: "", description: "", facultyId: null };

      if (this.faculties.length === 0) {
        this.isSelectValid = false;
        this.validationSelectMessage = 'Список факультетов пуст!';
      }
    },
    closeAddPanel() {
      this.isAddPanelOpen = false;
      this.newDepartment = { name: "", description: "", facultyId: null };
    },
    selectFaculty(faculty) {
      this.newDepartment.facultyId = faculty.id;
      this.facultySearchQuery = faculty.name;
      this.isFacultyListOpen = false;
    },
    closeFacultyList() {
      setTimeout(() => {
        this.isFacultyListOpen = false;
      }, 200);
    },

    async addDepartment() {
      this.$refs.newDepartmentNameInput?.markAsTouched();
      await this.$nextTick();
      if (!this.fieldValidationStatus?.newDepartmentNameInput) return;

      if (!this.newDepartment.facultyId) {
        this.isSelectValid = false;
        this.validationSelectMessage = 'Выберите факультет!';
        return;
      }

      this.isSelectValid = true;
      this.validationSelectMessage = '';

      try {
        const response = await createDepartment({
          name: this.newDepartment.name,
          description: this.newDepartment.description,
          facultyId: this.newDepartment.facultyId,
        });
        const createdDepartment = response.data;

        this.departments.push(createdDepartment);
        this.closeAddPanel();
        this.showAlert('success', `Кафедра с ID: ${createdDepartment.id} успешно создана.`);
      } catch (error) {
        this.showAlert('error', `Ошибка при создании кафедры: ${error}`);
      }
    },


    openEditPanel(index) {
      this.editIndex = index;
      this.editedDepartment = { ...this.departments[index] };
      this.isEditPanelOpen = true;
    },
    closeEditPanel() {
      this.isEditPanelOpen = false;
      this.editedDepartment = null;
      this.editIndex = null;
    },

    async updateDepartment() {
      const isEditing = this.editIndex !== null;
      if (!isEditing) return;

      this.$refs.updatedDepartmentNameInput?.markAsTouched();
      await this.$nextTick();

      if (!this.fieldValidationStatus?.updatedDepartmentNameInput) return;

      const { id, name, description } = this.editedDepartment;

      if (!name.trim() || !description.trim()) return;

      const original = this.departments[this.editIndex];
      const isUnchanged =
          name.trim() === original.name.trim() &&
          (description?.trim() || '') === (original.description?.trim() || '');

      if (isUnchanged) {
        this.closeEditPanel();
        return;
      }

      try {
        const response = await updateDepartment(id, { id, name, description });
        this.departments[this.editIndex] = response.data;

        this.closeEditPanel();
        this.showAlert('success', `Кафедра "${response.data.name}" успешно обновлена.`);
      } catch (error) {
        const message = error.response?.data?.message || error.message || 'Ошибка';
        this.showAlert('error', `Ошибка при обновлении кафедры: ${message}`);
      }
    },

    openDepartmentDetails(index) {
      this.selectedDepartment = { ...this.departments[index] };
      this.isDepartmentDetailsOpen = true;
    },
    closeDepartmentDetails() {
      this.isDepartmentDetailsOpen = false;
      this.selectedDepartment = null;
    },

    openDeleteConfirmation(index) {
      this.deleteIndex = index;
      this.isDeleteConfirmationOpen = true;
    },
    closeDeleteConfirmation() {
      this.isDeleteConfirmationOpen = false;
      this.deleteIndex = null;
    },

    async confirmDelete() {
      if (this.deleteIndex === null) return;

      const department = this.departments[this.deleteIndex];

      try {
        await deleteDepartment(department.id);

        await this.fetchDepartments();

        this.showAlert('success', `Кафедра "${department.name}" успешно удалена.`);
      } catch (error) {
        const message = error.response?.data?.message || error.message || 'Ошибка при удалении';
        this.showAlert('error', `Не удалось удалить кафедру: ${message}`);
      } finally {
        this.closeDeleteConfirmation();
      }
    },

    async facultyList() {
      let error = null;
      try {
        const response = await getFaculties(false);
        this.faculties = response.data;
      } catch (err) {
        error = err.response?.data?.message || err.message || 'Произошла ошибка при загрузке';
        this.showAlert('error', `Ошибка: ${error}`);
      }
    },

    async fetchDepartments() {
      this.loading = true;
      let error = null;
      try {
        const response = await getDepartments(true);
        this.departments = response.data;
      } catch (err) {
        error = err.response?.data?.message || err.message || 'Произошла ошибка при загрузке';
        this.showAlert('error', `Ошибка: ${error}`);
      } finally {
        this.loading = false;
      }
    },

  },
  inject: ['showAlert'],
  mounted() {
    this.facultyList();
    this.fetchDepartments();
  },

};
</script>

<style scoped>
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
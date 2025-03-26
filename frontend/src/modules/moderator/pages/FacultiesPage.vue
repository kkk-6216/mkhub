<template>
  <div class="max-w-7xl mx-auto p-6 bg-white rounded-lg">

    <div class="flex justify-between items-center mb-6">
      <div class="flex items-center justify-center">
        <h2 class="text-2xl font-medium text-dark">Список факультетов</h2>
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
          <h2 class="text-base/7 font-medium text-dark">Создать факультет</h2>
          <div class="mt-5 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
            <div class="sm:col-span-4">
              <InputItem
                  v-model.trim="newFaculty.name"
                  label="Название"
                  @isValidated="isValidated"
                  :minLength="3"
                  :maxLength="50"
                  name="newFacultyNameInput"
                  ref="newFacultyNameInput"
              />
            </div>
            <div class="col-span-full">
              <TextareaItem
                  v-model.trim="newFaculty.description"
                  label="Описание"
                  placeholder="(опционально)"
                  name="about-1"
              />
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

    <div v-if="!loading" class="overflow-x-auto">
      <table class="w-full text-left">
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
      <p v-if="faculties.length === 0" class="text-center text-gray-500 py-4">
        Пока пусто. Добавьте первый факультет!
      </p>
    </div>

    <Modal v-if="isDeleteConfirmationOpen" @close="closeDeleteConfirmation" width="w-[430px]">
      <h3 class="text-xl font-medium mb-5 text-dark">Подтверждение удаления</h3>
      <p class="text-gray-700  mb-5">Вы уверены, что хотите удалить этот факультет?</p>
      <div class="flex justify-end  space-x-2">
        <DefaultButton label="Удалить" size="sm" type="button" variant="danger" @click="confirmDelete"></DefaultButton>
        <DefaultButton label="Отмена" size="sm" type="button" variant="secondary" @click="closeDeleteConfirmation"></DefaultButton>
      </div>

    </Modal>

    <Modal v-if="isEditPanelOpen" @close="closeEditPanel">
      <form class="space-y-12">
        <div class="pb-1">
          <h2 class="text-base/7 font-medium text-dark">Редактировать факультет</h2>
          <div class="mt-5 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
            <div class="sm:col-span-4">
              <InputItem
                  v-model.trim="editedFaculty.name"
                  @isValidated="isValidated"
                  label="Название"
                  :minLength="3"
                  :maxLength="50"
                  name="updatedFacultyNameInput"
                  ref="updatedFacultyNameInput"
              />
            </div>
            <div class="col-span-full">
              <TextareaItem
                  v-model.trim="editedFaculty.description"
                  label="Описание"
                  name="about-2"
                  placeholder="(опционально)"
              />
            </div>
            <div class="col-span-full">
              <div class="flex justify-end space-x-2">
                <DefaultButton label="Отмена" type="button" variant="secondary" @click="closeEditPanel"></DefaultButton>
                <DefaultButton label="Сохранить" type="button" variant="primary" @click="updateFaculty"></DefaultButton>
              </div>
            </div>
          </div>
        </div>
      </form>
    </Modal>

    <Modal v-if="isFacultyDetailsOpen" @close="closeFacultyDetails">
      <h2 class="text-xl font-medium text-dark mb-6 text-left">Информация о факультете</h2>

      <div class="mb-4">
        <label class="block text-base/6 font-medium text-gray-500">Название</label>
        <p class="py-5 text-sm text-dark">
          {{ selectedFaculty.name }}
        </p>
      </div>

      <div>
        <label class="block text-base/6 font-medium text-gray-500">Описание</label>
        <p class="py-5 text-sm text-dark">
          {{ selectedFaculty.description }}
        </p>
      </div>

      <div class="mt-6 border-t border-main pt-4">
        <div class="flex items-center cursor-pointer" @click="toggleDepartments">
          <svg class="w-5 h-5 text-gray-500 mr-2" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"
               xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" d="M19 9l-7 7-7-7"></path>
          </svg>
          <span class="text-base font-medium text-gray-500">Список кафедр</span>

          <svg
              v-if="isLoadingDepartments"
              width="20px"
              height="20px"
              viewBox="0 0 20 20"
              xmlns="http://www.w3.org/2000/svg"
              class="ml-4 animate-spin text-gray-700"
          >
            <g fill="currentColor" fill-rule="nonzero">
              <path
                  d="M10,3.5 C6.41015,3.5 3.5,6.41015 3.5,10 C3.5,10.4142 3.16421,10.75 2.75,10.75 C2.33579,10.75 2,10.4142 2,10 C2,5.58172 5.58172,2 10,2 C14.4183,2 18,5.58172 18,10 C18,14.4183 14.4183,18 10,18 C9.58579,18 9.25,17.6642 9.25,17.25 C9.25,16.8358 9.58579,16.5 10,16.5 C13.5899,16.5 16.5,13.5899 16.5,10 C16.5,6.41015 13.5899,3.5 10,3.5 Z"
              />
            </g>
          </svg>
        </div>

        <ul v-if="isDepartmentsOpen && selectedFaculty.departments" class="mt-3 ml-7 text-sm text-dark">
          <li v-for="department in selectedFaculty.departments" :key="department.id" class="py-1">
            {{ department.name }}
          </li>
        </ul>
      </div>

      <div class="flex justify-end mt-6">
        <DefaultButton label="OK" type="button" variant="primary" @click="closeFacultyDetails"></DefaultButton>
      </div>
    </Modal>

  </div>
</template>

<script>
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import Modal from "@/modules/moderator/components/Modal.vue";
import InputItem from "@/modules/moderator/components/InputItem.vue";
import TextareaItem from "@/modules/moderator/components/TextareaItem.vue";
import { createFaculty, getFaculties, updateFaculty, deleteFaculty } from '@/modules/moderator/service/facultyService.js'
import { getDepartmentsByFacultyId } from '@/modules/moderator/service/departmentService.js';

export default {
  name: "FacultiesPage",
  components: {InputItem, DefaultButton, Modal, TextareaItem},
  data() {
    return {

      faculties: [],

      isAddPanelOpen: false,
      isEditPanelOpen: false,
      isFacultyDetailsOpen: false,
      isDeleteConfirmationOpen: false,
      isDepartmentsOpen: false,

      isLoadingDepartments: false,
      loading: false,

      editedFaculty: {},
      newFaculty: {name: "", description: ""},
      selectedFaculty: {},

      deleteIndex: null,
      editIndex: null,
      selectedIndex: null,

      fieldValidationStatus: {},

    };
  },
  methods: {

    isValidated({isValid, field}) {
      this.fieldValidationStatus[field] = isValid;
    },

    async fetchFaculties() {
      this.loading = true;
      let error = null;
      try {
        const response = await getFaculties(true);
        this.faculties = response.data;
      } catch (err) {
        error = err.response?.data?.message || err.message || 'Произошла ошибка при загрузке';
        this.showAlert('error', `Ошибка: ${error}`);
      } finally {
        this.loading = false;
      }
    },

    openAddPanel() {
      this.isAddPanelOpen = true;
    },
    closeAddPanel() {
      this.isAddPanelOpen = false;
      this.newFaculty = {name: "", description: ""};
    },

    async addFaculty() {
      this.$refs.newFacultyNameInput?.markAsTouched();
      await this.$nextTick();

      if (!this.fieldValidationStatus?.newFacultyNameInput) return;

      try {
        const response = await createFaculty(this.newFaculty);
        const createdFaculty = response.data;

        this.faculties.push(createdFaculty);
        this.closeAddPanel();
        this.showAlert('success', `Факультет с ID: ${createdFaculty.id} успешно создан.`);
      } catch (error) {
        this.showAlert('error', `Ошибка при создании факультета: ${error}`);
      }
    },

    openEditPanel(index) {
      this.editIndex = index;
      this.editedFaculty = {...this.faculties[index]};
      this.isEditPanelOpen = true;
    },
    closeEditPanel() {
      this.isEditPanelOpen = false;
      this.editedFaculty = {};
      this.editIndex = null;
    },

    async updateFaculty() {
      const isEditing = this.editIndex !== null;
      if (!isEditing) return;

      this.$refs.updatedFacultyNameInput?.markAsTouched();
      await this.$nextTick();

      if (!this.fieldValidationStatus?.updatedFacultyNameInput) return;

      const { name, description, id } = this.editedFaculty;
      const isValid = name.trim() && description.trim();
      if (!isValid) return;

      const originalFaculty = this.faculties[this.editIndex];
      const isUnchanged =
          name.trim() === originalFaculty.name.trim() &&
          description.trim() === (originalFaculty.description || '').trim();

      if (isUnchanged) {
        this.closeEditPanel();
        return;
      }

      try {
        const response = await updateFaculty(id, { id, name, description });

        this.faculties[this.editIndex] = response.data;

        this.closeEditPanel();
        this.showAlert('success', `Факультет с ID: ${response.data.id} успешно обновлён.`);
      } catch (error) {
        const message = error.response?.data?.message || error.message || 'Ошибка';
        this.showAlert('error', `Ошибка при обновлении факультета: ${message}`);
      }
    },

    openFacultyDetails(index) {
      this.selectedIndex = index;
      this.selectedFaculty = {...this.faculties[index]};
      this.isFacultyDetailsOpen = true;
    },
    closeFacultyDetails() {
      this.isFacultyDetailsOpen = false;
      this.isDepartmentsOpen = false;
      this.isLoadingDepartments = false;
      this.selectedFaculty = {};
    },
    async toggleDepartments() {
      if (this.isDepartmentsOpen) {
        this.isDepartmentsOpen = false;
        return;
      }

      this.isDepartmentsOpen = true;

      if (!this.selectedFaculty.departments) {
        this.isLoadingDepartments = true;
        try {
          const { data } = await getDepartmentsByFacultyId(this.selectedFaculty.id);

          this.selectedFaculty.departments = data;

        } catch (error) {
          this.showAlert('error', 'Ошибка загрузки кафедр!');
        } finally {
          this.isLoadingDepartments = false;
        }
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

    async confirmDelete() {
      if (this.deleteIndex === null) return;

      const faculty = this.faculties[this.deleteIndex];

      try {
        await deleteFaculty(faculty.id);

        await this.fetchFaculties();

        this.showAlert('success', `Факультет "${faculty.name}" успешно удалён.`);
      } catch (error) {
        const message = error.response?.data?.message || error.message || 'Ошибка при удалении факультета';
        this.showAlert('error', `Не удалось удалить факультет: ${message}`);
      } finally {
        this.closeDeleteConfirmation();
      }
    }
  },
  inject: ['showAlert'],
  mounted() {
    this.fetchFaculties();
  },
}
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
<template>
  <div class="max-w-7xl mx-auto p-6 bg-white rounded-lg">

    <div class="flex justify-between items-center mb-6">
      <div class="flex items-center justify-center">
        <h2 class="text-2xl font-medium text-dark">Список Пользователей</h2>
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
      <form class="space-y-8 px-3">
        <div class="space-y-4">
          <h2 class="text-base font-medium text-dark">Создать пользователя</h2>

          <InputComponent
              v-model.trim="newUser.username"
              name="username"
              label="Имя пользователя"
              placeholder="Введите..."
              :is-valid="newUserValid.username.isValid"
              :error="newUserValid.username.message"
          />

          <InputComponent
              v-model.trim="newUser.email"
              name="email"
              type="email"
              label="Email"
              placeholder="Введите..."
              :is-valid="newUserValid.email.isValid"
              :error="newUserValid.email.message"
          />

          <InputComponent
              v-model.trim="newUser.password"
              name="password"
              type="password"
              label="Пароль"
              placeholder="Введите..."
              :is-valid="newUserValid.password.isValid"
              :error="newUserValid.password.message"
          />

          <InputComponent
              v-model.trim="newUser.confirmPassword"
              name="confirmPassword"
              type="password"
              label="Подтвердить пароль"
              placeholder="Введите..."
              :is-valid="newUserValid.confirmPassword.isValid"
              :error="newUserValid.confirmPassword.message"
          />

          <div>
            <label class="block text-sm font-medium text-gray-500 mb-2">Роль</label>
            <div class="mt-2 grid grid-cols-1">
              <select v-model="newUser.role" class="col-start-1 row-start-1 w-full appearance-none rounded-md bg-white py-1.5 pr-8 pl-3 text-base text-dark outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6">
                <option value="ROLE_STUDENT">Студент</option>
                <option value="ROLE_MODERATOR">Модератор</option>
                <option value="ROLE_ADMIN">Администратор</option>
              </select>
              <svg class="pointer-events-none col-start-1 row-start-1 mr-2 size-5 self-center justify-self-end text-gray-500 sm:size-4" viewBox="0 0 16 16" fill="currentColor" aria-hidden="true" data-slot="icon">
                <path fill-rule="evenodd" d="M4.22 6.22a.75.75 0 0 1 1.06 0L8 8.94l2.72-2.72a.75.75 0 1 1 1.06 1.06l-3.25 3.25a.75.75 0 0 1-1.06 0L4.22 7.28a.75.75 0 0 1 0-1.06Z" clip-rule="evenodd" />
              </svg>
            </div>
          </div>

          <div class="flex justify-end space-x-2 pt-2">
            <DefaultButton label="Отмена" type="button" variant="secondary" @click="closeAddPanel" />
            <DefaultButton label="Сохранить" type="button" variant="primary" @click="addUser" />
          </div>
        </div>
      </form>
    </Modal>

    <div v-if="!loading" class="overflow-x-auto">
      <table class="w-full text-left">
        <thead>
        <tr class="border-b border-gray-300">
          <th class="py-2 text-xs font-medium text-gray-500">#</th>
          <th class="py-2 text-xs font-medium text-gray-500">Имя пользователя</th>
          <th class="py-2 text-xs font-medium text-gray-500">Почта</th>
          <th class="py-2 text-xs font-medium text-gray-500">Роль</th>
          <th class="py-2 text-xs font-medium text-gray-500">Статус</th>
          <th class="py-2 text-xs font-medium text-gray-500 text-right">Действие</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(user, index) in users" :key="index" class="border-b border-gray-300 hover:bg-gray-50">
          <td class="py-3 text-sm text-dark">{{ index + 1 }}</td>
          <td @click="openUserDetails(user.userId)" class="py-3 cursor-pointer text-sm text-left text-dark hover:text-main">
            {{ user.username }}
          </td>
          <td class="py-3 cursor-pointer text-sm text-left text-dark hover:text-main">
            {{ user.email }}
          </td>
          <td class="py-3 cursor-pointer text-sm text-left text-dark hover:text-main">
            {{ user.role }}
          </td>
          <td class="px-6 py-4 whitespace-nowrap">
            <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">{{ user.enabled }}</span>
          </td>
          <td class="py-3 text-right">
            <button @click="openEditPanel(user.userId)" class="p-1 bg-gray-200 rounded-md hover:bg-gray-300 ">
              <svg class="w-4 h-4 text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"></path>
              </svg>
            </button>
            <button @click="openDeleteConfirmation(user.userId, user.username)" class="p-1 bg-gray-200 rounded-md hover:bg-gray-300 ml-2 ">
              <svg class="w-4 h-4 text-red-700" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
              </svg>
            </button>
          </td>
        </tr>
        </tbody>
      </table>
      <p v-if="users.length === 0" class="text-center text-gray-500 py-4">
        Пока пусто. Добавьте первого пользователя!
      </p>
    </div>

    <Modal v-if="isDeleteConfirmationOpen" @close="closeDeleteConfirmation" width="w-[430px]">
      <h3 class="text-xl font-medium mb-5 text-dark">Подтверждение удаления</h3>
      <p class="text-gray-700 mb-5 text-base/7">Вы уверены, что хотите удалить пользователя:
        <span class="px-2 inline-flex leading-5 font-semibold text-red-600">{{ deleteUsername }}</span>?
      </p>
      <div class="flex justify-end  space-x-2">
        <DefaultButton label="Удалить" size="sm" type="button" variant="danger" @click="confirmDelete"></DefaultButton>
        <DefaultButton label="Отмена" size="sm" type="button" variant="secondary" @click="closeDeleteConfirmation"></DefaultButton>
      </div>

    </Modal>

    <Modal v-if="isEditPanelOpen" @close="closeEditPanel" position="fixed p-4 pt-16 z-50 flex justify-center items-start" width="w-[900px]">
      <div class="p-3 space-y-6">

        <!-- Заголовок -->
        <div class="flex justify-between items-center">
          <h2 class="text-xl font-bold text-dark">Редактирование пользователя</h2>
          <button @click="closeEditPanel" class="text-main hover:text-main-hover transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 20 20">
              <path d="M2.146 2.854a.5.5 0 1 1 .708-.708L8 7.293l5.146-5.147a.5.5 0 0 1 .708.708L8.707 8l5.147 5.146a.5.5 0 0 1-.708.708L8 8.707l-5.146 5.147a.5.5 0 0 1-.708-.708L7.293 8z"/>
            </svg>
          </button>
        </div>

        <!-- Табы -->
        <div class="border-b border-gray-200">
          <nav class="flex flex-wrap gap-3">
            <button
                v-for="tabItem in tabsEdit"
                :key="tabItem.key"
                :class="tabEdit === tabItem.key ? 'border-b-2 border-main text-main' : 'text-gray-500 hover:text-gray-700'"
                class="px-3 py-2 font-medium text-sm"
                @click="tabEdit = tabItem.key"
            >
              {{ tabItem.label }}
            </button>
          </nav>
        </div>

        <!-- Контент табов -->
        <div class="mt-4 space-y-6">

          <!-- 1. Смена username -->
          <div v-if="tabEdit === 'username'">
            <InputComponent
                v-model="editUser.username"
                name="username"
                label="Имя пользователя"
                placeholder="Введите имя пользователя"
                :is-valid="editUserValid.username.nameValid"
                :error="editUserValid.username.message"
            />
          </div>

          <!-- 2. О себе -->
          <div v-if="tabEdit === 'about'">
            <TextareaItem
                v-model.trim="editUser.about"
                label="О себе"
                label-class="text-gray-500"
                placeholder="Напишите что-нибудь о себе (опционально)"
                name="about-1"
                :rows="10"
            />
          </div>

          <!-- 3. Данные -->
          <div v-if="tabEdit === 'data'" class="space-y-4">
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <InputComponent v-model="editUser.firstName" name="firstName" label="Имя" placeholder="Введите имя" />
              <InputComponent v-model="editUser.lastName" name="lastName" label="Фамилия" placeholder="Введите фамилию" />
              <InputComponent :is-valid="editUserValid.data[0].emailValid" :error="editUserValid.data[0].message" v-model="editUser.email" name="email" type="email" label="Email" placeholder="example@mail.com" />
              <InputComponent :is-valid="editUserValid.data[1].phoneValid" :error="editUserValid.data[1].message" v-model="editUser.phone" name="phone" type="phone" label="Phone" placeholder="Введите номер телефона" />
              <InputComponent v-model="editUser.instagram" name="instagram" label="Instagram" placeholder="https://instagram.com/..." />
              <InputComponent v-model="editUser.github" name="github" label="GitHub" placeholder="https://github.com/..." />
            </div>
          </div>

          <!-- 4. Смена роли -->
          <div v-if="tabEdit === 'role'">
            <label class="block text-sm font-medium text-gray-500 mb-2">Роль</label>
            <div class="mt-2 grid grid-cols-1">
              <select v-model="editUser.role" class="col-start-1 row-start-1 w-full appearance-none rounded-md bg-white py-1.5 pr-8 pl-3 text-base text-dark outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6">
                <option value="ROLE_ADMIN">Администратор</option>
                <option value="ROLE_MODERATOR">Модератор</option>
                <option value="ROLE_STUDENT">Студент</option>
              </select>
              <svg class="pointer-events-none col-start-1 row-start-1 mr-2 size-5 self-center justify-self-end text-gray-500 sm:size-4" viewBox="0 0 16 16" fill="currentColor" aria-hidden="true" data-slot="icon">
                <path fill-rule="evenodd" d="M4.22 6.22a.75.75 0 0 1 1.06 0L8 8.94l2.72-2.72a.75.75 0 1 1 1.06 1.06l-3.25 3.25a.75.75 0 0 1-1.06 0L4.22 7.28a.75.75 0 0 1 0-1.06Z" clip-rule="evenodd" />
              </svg>
            </div>
          </div>

          <!-- 5. Безопасность -->
          <div v-if="tabEdit === 'security'">
            <h3 class="text-sm font-medium text-gray-500 mb-2">Параметры безопасности</h3>
            <div class="grid grid-cols-2 sm:grid-cols-4 gap-4 text-sm">

              <div class="flex gap-3">
                <div class="flex h-6 shrink-0 items-center">
                  <div class="group grid size-4 grid-cols-1">
                    <input v-model="editUser.enabled" id="enabled" aria-describedby="enabled-description" name="enabled" type="checkbox" checked class="col-start-1 row-start-1 appearance-none rounded-sm border border-gray-300 bg-white checked:border-main checked:bg-main indeterminate:border-main indeterminate:bg-main focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-main disabled:border-gray-300 disabled:bg-gray-100 disabled:checked:bg-gray-100 forced-colors:appearance-auto">
                    <svg class="pointer-events-none col-start-1 row-start-1 size-3.5 self-center justify-self-center stroke-white group-has-disabled:stroke-gray-950/25" viewBox="0 0 14 14" fill="none">
                      <path class="opacity-0 group-has-checked:opacity-100" d="M3 8L6 11L11 3.5" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                      <path class="opacity-0 group-has-indeterminate:opacity-100" d="M3 7H11" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                  </div>
                </div>
                <div class="text-sm/6">
                  <label for="comments" class="font-medium text-dark">Активен</label>
                  </div>
              </div>
              <div class="flex gap-3">
                <div class="flex h-6 shrink-0 items-center">
                  <div class="group grid size-4 grid-cols-1">
                    <input v-model="editUser.accountNonExpired" id="accountNonExpired" aria-describedby="accountNonExpired-description" name="accountNonExpired" type="checkbox" checked class="col-start-1 row-start-1 appearance-none rounded-sm border border-gray-300 bg-white checked:border-main checked:bg-main indeterminate:border-main indeterminate:bg-main focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-main disabled:border-gray-300 disabled:bg-gray-100 disabled:checked:bg-gray-100 forced-colors:appearance-auto">
                    <svg class="pointer-events-none col-start-1 row-start-1 size-3.5 self-center justify-self-center stroke-white group-has-disabled:stroke-gray-950/25" viewBox="0 0 14 14" fill="none">
                      <path class="opacity-0 group-has-checked:opacity-100" d="M3 8L6 11L11 3.5" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                      <path class="opacity-0 group-has-indeterminate:opacity-100" d="M3 7H11" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                  </div>
                </div>
                <div class="text-sm/6">
                  <label for="comments" class="font-medium text-dark">Учётка не истекла</label>
                </div>
              </div>
              <div class="flex gap-3">
                <div class="flex h-6 shrink-0 items-center">
                  <div class="group grid size-4 grid-cols-1">
                    <input v-model="editUser.accountNonLocked" id="accountNonLocked" aria-describedby="accountNonLocked-description" name="accountNonLocked" type="checkbox" checked class="col-start-1 row-start-1 appearance-none rounded-sm border border-gray-300 bg-white checked:border-main checked:bg-main indeterminate:border-main indeterminate:bg-main focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-main disabled:border-gray-300 disabled:bg-gray-100 disabled:checked:bg-gray-100 forced-colors:appearance-auto">
                    <svg class="pointer-events-none col-start-1 row-start-1 size-3.5 self-center justify-self-center stroke-white group-has-disabled:stroke-gray-950/25" viewBox="0 0 14 14" fill="none">
                      <path class="opacity-0 group-has-checked:opacity-100" d="M3 8L6 11L11 3.5" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                      <path class="opacity-0 group-has-indeterminate:opacity-100" d="M3 7H11" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                  </div>
                </div>
                <div class="text-sm/6">
                  <label for="comments" class="font-medium text-dark">Не заблокирован</label>
                </div>
              </div>
              <div class="flex gap-3">
                <div class="flex h-6 shrink-0 items-center">
                  <div class="group grid size-4 grid-cols-1">
                    <input v-model="editUser.credentialsNonExpired" id="credentialsNonExpired" aria-describedby="credentialsNonExpired-description" name="credentialsNonExpired" type="checkbox" checked class="col-start-1 row-start-1 appearance-none rounded-sm border border-gray-300 bg-white checked:border-main checked:bg-main indeterminate:border-main indeterminate:bg-main focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-main disabled:border-gray-300 disabled:bg-gray-100 disabled:checked:bg-gray-100 forced-colors:appearance-auto">
                    <svg class="pointer-events-none col-start-1 row-start-1 size-3.5 self-center justify-self-center stroke-white group-has-disabled:stroke-gray-950/25" viewBox="0 0 14 14" fill="none">
                      <path class="opacity-0 group-has-checked:opacity-100" d="M3 8L6 11L11 3.5" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                      <path class="opacity-0 group-has-indeterminate:opacity-100" d="M3 7H11" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                  </div>
                </div>
                <div class="text-sm/6">
                  <label for="comments" class="font-medium text-dark">Пароль не истёк</label>
                </div>
              </div>

            </div>
          </div>

          <!-- Кнопки -->
          <div class="flex justify-end space-x-4 pt-4">
            <button @click="closeEditPanel" class="px-4 py-2 bg-gray-200 hover:bg-gray-300 text-gray-800 rounded-md">
              Отмена
            </button>
            <button @click="updateUser(tabEdit)" class="px-4 py-2 bg-main hover:bg-main-hover text-white rounded-md">
              Сохранить
            </button>
          </div>
        </div>
      </div>
    </Modal>

    <Modal position="fixed p-4 pt-16 z-50 flex justify-center items-start" v-if="isUserDetailsOpen" @close="closeUserDetails" width="w-[900px]">
      <div class="p-3 space-y-3">

        <!-- Заголовок -->
        <div class="flex justify-end items-center">
          <button @click="closeUserDetails" class="text-main hover:text-main-hover transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-x-lg" viewBox="0 0 20 20">
              <path d="M2.146 2.854a.5.5 0 1 1 .708-.708L8 7.293l5.146-5.147a.5.5 0 0 1 .708.708L8.707 8l5.147 5.146a.5.5 0 0 1-.708.708L8 8.707l-5.146 5.147a.5.5 0 0 1-.708-.708L7.293 8z"/>
            </svg>
          </button>
        </div>

        <!-- 1. Аватар и имя -->
        <div class="flex flex-col items-center space-y-3">
          <div class="w-32 h-32 border-2 border-gray-300 rounded-full overflow-hidden">
            <img
                :src="selectedUser.imageUrl || defaultAvatar"
                alt="Аватар"
                class="w-full h-full object-cover"
                @error="onAvatarError"
            />
          </div>

          <div class="text-center">
            <h3 class="text-xl font-semibold text-dark">
              {{ selectedUser.firstName }} {{ selectedUser.lastName }}
            </h3>
            <p class=" text-gray-500 mt-3">
              Роль: <span :class="roleColor">{{ roleLabel }}</span>
            </p>
          </div>
        </div>

        <!-- 2. Табы -->
        <div>
          <div class="border-b border-gray-200 mt-6">
            <nav class="flex space-x-4">
              <button
                  :class="tab === 'data' ? 'border-b-2 border-main text-main' : 'text-gray-500 hover:text-gray-700'"
                  class="px-3 py-2 font-medium text-sm"
                  @click="tab = 'data'"
              >
                Данные
              </button>
              <button
                  :class="tab === 'about' ? 'border-b-2 border-main text-main' : 'text-gray-500 hover:text-gray-700'"
                  class="px-3 py-2 font-medium text-sm"
                  @click="tab = 'about'"
              >
                О себе
              </button>
              <button
                  :class="tab === 'security' ? 'border-b-2 border-main text-main' : 'text-gray-500 hover:text-gray-700'"
                  class="px-3 py-2 font-medium text-sm"
                  @click="tab = 'security'"
              >
                Безопасность
              </button>
            </nav>
          </div>

          <!-- Тело табов -->
          <div class="mt-6 space-y-6">

            <!-- ТАБ: ДАННЫЕ -->
            <div v-if="tab === 'data'" class="space-y-6">
              <!-- Основные данные -->
              <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <p class="text-gray-600 text-sm">ID</p>
                  <p class="font-medium text-gray-800">{{ selectedUser.id }}</p>
                </div>
                <div>
                  <p class="text-gray-600 text-sm">Имя пользователя</p>
                  <p class="font-medium text-gray-800">{{ selectedUser.username }}</p>
                </div>
                <div>
                  <p class="text-gray-600 text-sm">Телефон номер</p>
                  <p class="font-medium text-gray-800">{{ selectedUser.phone || '—' }}</p>
                </div>
                <div>
                  <p class="text-gray-600 text-sm">Email</p>
                  <p class="font-medium text-gray-800">{{ selectedUser.email }}</p>
                </div>
                <div>
                  <p class="text-gray-600 text-sm">Создан</p>
                  <p class="font-medium text-gray-800">{{ formatDate(selectedUser.createdAt) }}</p>
                </div>
                <div>
                  <p class="text-gray-600 text-sm">Обновлён</p>
                  <p class="font-medium text-gray-800">{{ formatDate(selectedUser.updatedAt) }}</p>
                </div>
              </div>

              <!-- Соцсети -->
              <div class="grid grid-cols-2 gap-4">
                <div class="flex items-center space-x-2">
                  <p class="text-gray-600 text-sm w-24">Instagram:</p>
                  <div v-if="selectedUser.instagram">
                    <a :href="selectedUser.instagram" target="_blank" rel="noopener noreferrer" class="text-pink-500 hover:text-pink-600">
                      <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M7.75 2A5.75 5.75 0 0 0 2 7.75v8.5A5.75 5.75 0 0 0 7.75 22h8.5A5.75 5.75 0 0 0 22 16.25v-8.5A5.75 5.75 0 0 0 16.25 2h-8.5Zm0 1.5h8.5A4.25 4.25 0 0 1 20.5 7.75v8.5a4.25 4.25 0 0 1-4.25 4.25h-8.5A4.25 4.25 0 0 1 3.5 16.25v-8.5A4.25 4.25 0 0 1 7.75 3.5ZM12 7a5 5 0 1 0 0 10 5 5 0 0 0 0-10Zm0 1.5a3.5 3.5 0 1 1 0 7 3.5 3.5 0 0 1 0-7Zm4.75-.25a1 1 0 1 1 0 2 1 1 0 0 1 0-2Z"/>
                      </svg>
                    </a>
                  </div>
                  <span v-else>—</span>
                </div>
                <div class="flex items-center space-x-2">
                  <p class="text-gray-600 text-sm w-24">GitHub:</p>
                  <div v-if="selectedUser.github">
                    <a :href="selectedUser.github" target="_blank" rel="noopener noreferrer" class="text-gray-800 hover:text-black">
                      <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 24 24">
                        <path
                            fill-rule="evenodd"
                            d="M12 2C6.477 2 2 6.484 2 12.012c0 4.422 2.865 8.167 6.839 9.504.5.092.682-.217.682-.483
                      0-.237-.009-.868-.014-1.703-2.782.605-3.369-1.342-3.369-1.342-.454-1.153-1.11-1.461-1.11-1.461-.908-.62.069-.608.069-.608
                      1.003.07 1.531 1.03 1.531 1.03.892 1.53 2.341 1.088 2.91.833.091-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951
                      0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.27.098-2.646 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0 1 12 6.844c.85.004
                      1.705.115 2.504.337 1.909-1.296 2.748-1.026 2.748-1.026.546 1.376.202 2.393.1 2.646.64.7 1.028 1.595
                      1.028 2.688 0 3.847-2.339 4.695-4.566 4.944.359.309.678.918.678 1.852
                      0 1.337-.012 2.419-.012 2.748 0 .268.18.58.688.481A10.019 10.019 0 0 0 22 12.012C22 6.484 17.523 2 12 2Z"
                            clip-rule="evenodd"
                        />
                      </svg>
                    </a>
                  </div>
                  <span v-else>—</span>
                </div>
              </div>
            </div>

            <div v-if="tab === 'about'" class="space-y-6">
              <!-- О себе -->
              <div>
                <p class="whitespace-pre-line text-gray-800">{{ selectedUser.about || '—' }}</p>
              </div>
            </div>

            <!-- ТАБ: БЕЗОПАСНОСТЬ -->
            <div v-if="tab === 'security'">
              <table class="min-w-full text-sm text-left border border-gray-200 rounded overflow-hidden">
                <thead class="bg-gray-50">
                  <tr>
                    <th class="px-4 py-2 font-semibold text-gray-500">Параметр</th>
                    <th class="px-4 py-2 font-semibold text-gray-500">Значение</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(value, label) in securityFields" :key="label">
                    <td class="px-4 py-2">{{ label }}</td>
                    <td class="px-4 py-2">
                      <span :class="value ? 'text-green-600 font-semibold' : 'text-red-600 font-semibold'">
                        {{ value ? 'Да' : 'Нет' }}
                      </span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </Modal>

  </div>
</template>

<script>
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import Modal from "@/modules/moderator/components/Modal.vue";
import TextareaItem from "@/modules/moderator/components/TextareaItem.vue";

import {
  createUser,
  deleteUser,
  getUser, getUsers,
  updateAbout, updateData, updateRole, updateSecurityStatus, updateUsername
} from "@/modules/moderator/service/user-service.js";

import InputComponent from "@/modules/admin/components/InputComponent.vue";

export default {
  name: "FacultiesPage",
  components: {InputComponent, DefaultButton, Modal, TextareaItem},
  data() {
    return {

      users: [],

      isAddPanelOpen: false,
      isEditPanelOpen: false,
      isUserDetailsOpen: false,
      isDeleteConfirmationOpen: false,

      loading: false,

      editUser: {},
      originalUser: {},
      newUser: {username: "", email: "", password: "", role: "ROLE_STUDENT", confirmPassword: ""},
      selectedUser: {},

      deleteIndex: null,
      editIndex: null,

      deleteUsername: null,
      tab: 'data',
      tabEdit: 'username',
      tabsEdit: [
        { key: 'username', label: 'Смена имени' },
        { key: 'about', label: 'Обо мне' },
        { key: 'data', label: 'Данные' },
        { key: 'role', label: 'Смена роли' },
        { key: 'security', label: 'Безопасность' },
      ],

      defaultAvatar: 'https://www.svgrepo.com/show/452030/avatar-default.svg',

      editUserValid: {
        username: {
          nameValid: true, message: ''
        },
        data: [
          {emailValid: true, message: ''},
          {phoneValid: true, message: ''},
        ]
      },

      newUserValid: {
        username: {isValid: true, message: ''},
        password: {isValid: true, message: ''},
        confirmPassword: {isValid: true, message: ''},
        email: {isValid: true, message: ''},
      }
    };
  },
  methods: {

    openAddPanel() {
      this.isAddPanelOpen = true;
    },
    closeAddPanel() {
      this.isAddPanelOpen = false;
      this.newUser = {username: "", email: "", password: "", role: "ROLE_STUDENT", confirmPassword: ""};
      this.newUserValid = {
        username: {isValid: true, message: ''},
        password: {isValid: true, message: ''},
        confirmPassword: {isValid: true, message: ''},
        email: {isValid: true, message: ''},
      };
    },

    async addUser() {

      if (!this.validateNewUser()) {
        return;
      }

      try {
        await createUser(this.newUser);
        await this.fetchUsers();

        this.showAlert(
            'success',
            `Пользователь с именем пользователя "${this.newUser.username}" успешно создан.`
        );

        this.closeAddPanel();
      } catch (error) {
        if (error.response && error.response.status === 400) {
          console.log(error.response)
          this.showAlert('error', `Ошибка: ${error.response.data}`);
        } else {
          this.showAlert('error', `Произошла ошибка при создании пользователя: ${error.message || error}`);
        }
      }

    },

    // ----------------------------------------------------------

    validateNewUser() {
      let isValid = true;

      if (!this.newUser.username || this.newUser.username.length < 3) {
        this.newUserValid.username = {
          isValid: false,
          message: 'Имя пользователя должно содержать минимум 3 символа'
        };
        isValid = false;
      } else {
        this.newUserValid.username = { isValid: true, message: '' };
      }

      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!this.newUser.email || !emailRegex.test(this.newUser.email)) {
        this.newUserValid.email = {
          isValid: false,
          message: 'Некорректный формат email'
        };
        isValid = false;
      } else {
        this.newUserValid.email = { isValid: true, message: '' };
      }

      if (!this.newUser.password || this.newUser.password.length < 6) {
        this.newUserValid.password = {
          isValid: false,
          message: 'Пароль должен содержать минимум 6 символов'
        };
        isValid = false;
      } else {
        this.newUserValid.password = { isValid: true, message: '' };
      }

      if (this.newUser.confirmPassword !== this.newUser.password) {
        this.newUserValid.confirmPassword = {
          isValid: false,
          message: 'Пароли не совпадают'
        };
        isValid = false;
      } else {
        this.newUserValid.confirmPassword = { isValid: true, message: '' };
      }

      return isValid;
    },

    async updateUser(tab) {
      if (!this.validateEditUser(tab)) {
        return;
      }

      if (!this.hasChanges(tab)) {
        this.showAlert('info', `Ничего не изменилось`);
        this.closeEditPanel();
        return;
      }

      try {
        switch (tab) {
          case 'username':
            await updateUsername(this.editUser.id, { username: this.editUser.username });
            this.showAlert('success', 'Имя пользователя успешно обновлено');
            break;
          case 'about':
            await updateAbout(this.editUser.id, { about: this.editUser.about });
            this.showAlert('success', 'Данные о себе успешно обновлено');
            break;
          case 'data':
            await updateData(this.editUser.id,
                {
                  firstName: this.editUser.firstName,
                  lastName: this.editUser.lastName,
                  email: this.editUser.email,
                  phone: this.editUser.phone,
                  github: this.editUser.github,
                  instagram: this.editUser.instagram,
                }
            );
            this.showAlert('success', 'Данные пользователя успешно обновлено');
            break;
          case 'role':
            await updateRole(this.editUser.id, { role: this.editUser.role });
            this.showAlert('success', 'Роль пользователя успешно обновлено');
            break;
          case 'security':
            await updateSecurityStatus(this.editUser.id, {
              enabled: this.editUser.enabled,
              accountNonExpired: this.editUser.accountNonExpired,
              accountNonLocked: this.editUser.accountNonLocked,
              credentialsNonExpired: this.editUser.credentialsNonExpired,
            });
            this.showAlert('success', 'Статусы безопасности успешно обновлено');
            await this.fetchUsers();
            break;
        }
        await this.fetchUsers();
        this.closeEditPanel();
      } catch (error) {
        this.showAlert('error', `Ошибка: ${error}`);
      }
    },
    hasChanges(tab) {
      switch (tab) {
        case 'username':
          return this.editUser.username !== this.originalUser.username;

        case 'about':
          return this.editUser.about !== this.originalUser.about;

        case 'data':
          return (
              this.editUser.firstName !== this.originalUser.firstName ||
              this.editUser.lastName !== this.originalUser.lastName ||
              this.editUser.email !== this.originalUser.email ||
              this.editUser.instagram !== this.originalUser.instagram ||
              this.editUser.github !== this.originalUser.github
          );

        case 'role':
          return this.editUser.role !== this.originalUser.role;

        case 'security':
          return (
              this.editUser.enabled !== this.originalUser.enabled ||
              this.editUser.accountNonExpired !== this.originalUser.accountNonExpired ||
              this.editUser.accountNonLocked !== this.originalUser.accountNonLocked ||
              this.editUser.credentialsNonExpired !== this.originalUser.credentialsNonExpired
          );

        default:
          return false;
      }
    },

    validateEditUser(tab) {
      let isValid = true;

      if (tab === 'username') {
        const name = this.editUser.username;
        if (!name || name.length < 3) {
          this.editUserValid.username = {
            nameValid: false,
            message: 'Имя пользователя должно содержать минимум 3 символа'
          };
          isValid = false;
        } else {
          this.editUserValid.username = {
            nameValid: true,
            message: ''
          };
        }
      }

      if (tab === 'data') {
        const fields = ['email', 'phone'];

        fields.forEach((field, index) => {
          const value = this.editUser[field] || '';
          let valid = true;
          let message = '';

          if (value.trim() !== '') {
            if (field === 'email' && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
              valid = false;
              message = 'Некорректный формат email';
            } else if (field === 'phone' && !/^\+?[0-9]{10,15}$/.test(value)) {
              valid = false;
              message = 'Некорректный формат телефонного номера';
            }
          }

          const validityKey = `${field}Valid`;

          this.editUserValid.data[index] = {
            [validityKey]: valid,
            message
          };

          if (!valid) isValid = false;
        });
      }

      return isValid;
    },

    async openEditPanel(userId) {
      try {
        const response = await getUser(userId);
        this.editIndex = userId;
        this.editUser = response.data;
        this.originalUser = { ...response.data };
        this.isEditPanelOpen = true;
      } catch (error) {
        this.showAlert('error', 'Ошибка при получении данных пользователя');
      }
    },
    closeEditPanel() {
      this.isEditPanelOpen = false;
      this.editUser = {};
      this.originalUser = {};
      this.editIndex = null;
      this.tabEdit = 'username';
      this.editUserValid = {
        username: {
          nameValid: true, message: ''
        },
        data: [
          {firstNameValid: true, message: ''},
          {lastNameValid: true, message: ''},
          {emailValid: true, message: ''},
          {instagramValid: true, message: ''},
          {githubValid: true, message: ''},
        ]
      }
    },

    async fetchUsers() {
      this.loading = true;
      let error = null;
      try {
        const response = await getUsers();
        this.users = response.data;
      } catch (err) {
        error = err.response?.data?.message || err.message || 'Произошла ошибка при загрузке';
        this.showAlert('error', `Ошибка: ${error}`);
      } finally {
        this.loading = false;
      }
    },

    onAvatarError(event) {
      event.target.src = this.defaultAvatar;
    },
    formatDate(dateStr) {
      if (!dateStr) return '—';
      return new Date(dateStr).toLocaleString();
    },

    async openUserDetails(userId) {
      try {
        const response = await getUser(userId);
        this.selectedUser = response.data;
        this.isUserDetailsOpen = true;
      } catch (error) {
        this.showAlert('error', 'Ошибка при получении данных пользователя');
      }
    },
    closeUserDetails() {
      this.isUserDetailsOpen = false;
      this.selectedUser = {};
    },

    openDeleteConfirmation(index, username) {
      this.deleteUsername = username;
      this.deleteIndex = index;
      this.isDeleteConfirmationOpen = true;
    },
    closeDeleteConfirmation() {
      this.isDeleteConfirmationOpen = false;
      this.deleteIndex = null;
      this.deleteUsername = null;
    },

    async confirmDelete() {
      if (this.deleteIndex === null) return;

      try {
        await deleteUser(this.deleteIndex);

        this.showAlert('success', `Пользователь с именем "${this.deleteUsername}" успешно удалён.`);
      } catch (error) {
        const message = error.response?.data?.message || error.message || 'Ошибка при удалении пользователя';
        this.showAlert('error', `Не удалось удалить пользователь: ${message}`);
      } finally {
        await this.fetchUsers();
        this.closeDeleteConfirmation();
      }
    },

  },
  computed: {
    roleLabel() {
      const role = this.selectedUser.role;
      switch (role) {
        case 'ROLE_ADMIN':
          return 'Администратор';
        case 'ROLE_STUDENT':
          return 'Студент';
        case 'ROLE_MODERATOR':
          return 'Модератор';
        default:
          return 'Неизвестно';
      }
    },
    roleColor() {
      const role = this.selectedUser.role;
      return {
        'ROLE_ADMIN': 'text-red-600 font-semibold',
        'ROLE_STUDENT': 'text-green-600 font-semibold',
        'ROLE_MODERATOR': 'text-yellow-600 font-semibold'
      }[role] || 'text-gray-500';
    },
    securityFields() {
      return {
        'Активен': this.selectedUser.enabled,
        'Учётка не истекла': this.selectedUser.accountNonExpired,
        'Не заблокирован': this.selectedUser.accountNonLocked,
        'Пароль не истёк': this.selectedUser.credentialsNonExpired
      };
    },

  },
  inject: ['showAlert'],
  mounted() {
    this.fetchUsers();
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
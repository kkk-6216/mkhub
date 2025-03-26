<template>
  <div class="relative w-full max-w-xl mx-auto">
    <!-- Основной контейнер -->
    <div
        :class="{
      'bg-white rounded-full shadow-sm border border-gray-500 overflow-hidden': !isFocused,
      'bg-white rounded-xl shadow-sm border border-gray-500 overflow-hidden': isFocused
    }"
    >
      <!-- Поле поиска -->
      <div class="relative px-4 py-3 border-b border-gray-100">
        <div class="absolute inset-y-0 left-4 flex items-center pointer-events-none">
          <svg class="w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
          </svg>
        </div>
        <input
            type="text"
            v-model="searchTerm"
            @focus="handleFocus"
            @blur="handleBlur"
            @keydown.down.prevent="moveHighlight(1)"
            @keydown.up.prevent="moveHighlight(-1)"
            @keydown.enter="selectHighlighted"
            class="w-full pl-9 pr-4 py-1 bg-transparent focus:outline-none text-gray-700 placeholder-gray-400 text-sm"
            placeholder="Поиск..."
        />
      </div>

      <!-- Результаты поиска -->
      <div v-if="showResults" class="divide-y divide-gray-100">
        <div class="max-h-96 overflow-y-auto">

          <!-- Projects -->
          <div v-if="searchTerm !== '' && filteredProjects.length > 0">
            <div class="px-4 pt-3 pb-1 text-xs font-semibold text-gray-500 uppercase tracking-wider text-left">Темы</div>
            <div class="divide-y divide-gray-100">
              <div
                  v-for="(project, index) in filteredProjects"
                  :key="project.name"
                  @click="selectItem(project, 'project')"
                  @mouseover="highlightedIndex = index; searchType = 'projects'"
                  class="px-4 py-2.5 text-sm cursor-pointer flex items-center"
                  :class="{
                  'bg-main text-white': highlightedIndex === index && searchType === 'projects',
                  'hover:bg-gray-50': highlightedIndex !== index || searchType !== 'projects'
                }"
              >
                <svg class="w-5 h-5 mr-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" :class="{'stroke-white': highlightedIndex === index && searchType === 'projects'}">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2z" />
                </svg>
                <span :class="{'text-white': highlightedIndex === index && searchType === 'projects'}">{{ project.name }}</span>
              </div>
            </div>
          </div>

          <!-- Пользователи -->
          <div v-if="searchTerm !== '' && filteredUsers.length > 0">
            <div class="px-4 pt-3 pb-1 text-xs font-semibold text-gray-500 uppercase tracking-wider text-left">Пользователи</div>
            <div class="divide-y divide-gray-100">
              <div
                  v-for="(user, index) in filteredUsers"
                  :key="user.name"
                  @click="selectItem(user, 'user')"
                  @mouseover="highlightedIndex = index; searchType = 'users'"
                  class="px-4 py-2.5 text-sm cursor-pointer flex items-center"
                  :class="{
                  'bg-main text-white': highlightedIndex === index && searchType === 'users',
                  'hover:bg-gray-50': highlightedIndex !== index || searchType !== 'users'
                }"
              >
                <img :src="user.avatar" class="w-6 h-6 rounded-full mr-3" :class="{'ring-2 ring-white': highlightedIndex === index && searchType === 'users'}" alt="Изображение пользователя">
                <span :class="{'text-white': highlightedIndex === index && searchType === 'users'}">{{ user.name }}</span>
              </div>
            </div>
          </div>

          <!-- Кафедры -->
          <div v-if="searchTerm !== '' && filteredDepartments.length > 0">
            <div class="px-4 pt-3 pb-1 text-xs font-semibold text-gray-500 uppercase tracking-wider text-left">Кафедры</div>
            <div class="divide-y divide-gray-100">
              <div
                  v-for="(department, index) in filteredDepartments"
                  :key="department.name"
                  @click="selectItem(department, 'department')"
                  @mouseover="highlightedIndex = index; searchType = 'departments'"
                  class="px-4 py-2.5 text-sm cursor-pointer flex items-center"
                  :class="{
                  'bg-main text-white': highlightedIndex === index && searchType === 'departments',
                  'hover:bg-gray-50': highlightedIndex !== index || searchType !== 'departments'
                }"
              >
                <svg class="w-5 h-5 mr-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" :class="{'stroke-white': highlightedIndex === index && searchType === 'departments'}">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
                </svg>
                <span :class="{'text-white': highlightedIndex === index && searchType === 'departments'}">{{ department.name }}</span>
              </div>
            </div>
          </div>

          <!-- Нет результатов -->
          <div v-else-if="!hasResults && searchTerm !== '' && searchTerm !== '?'" class="px-4 py-5 text-center">
            <div class="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-gray-100 mb-3">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <h3 class="text-sm font-medium text-gray-900 mb-1">Ничего не найдено</h3>
            <p class="text-xs text-gray-500">Попробуйте изменить поисковой запрос</p>
          </div>

          <!-- Помощь -->
          <div v-if="searchTerm === '?'" class="px-4 py-5 text-center">
            <div class="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-indigo-100 mb-3">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-main" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.228 9c.549-1.165 2.03-2 3.772-2 2.21 0 4 1.343 4 3 0 1.4-1.278 2.575-3.006 2.907-.542.104-.994.54-.994 1.093m0 3h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <h3 class="text-lg font-medium text-gray-900 mb-1">Помощь в поиске</h3>
            <p class="text-sm text-black">Используйте: # для поиска тем, > для поиска пользователей, ! для отделений поиска, ? для получения информации</p>
          </div>
        </div>

        <!-- Подсказка внизу -->
        <div class="bg-gray-50 px-4 py-2 text-xs text-gray-800 border-t border-gray-100 flex items-center justify-start">
          <span class="pr-2">Нажмите </span>
          <kbd class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-white border border-gray-200 ">?</kbd>
          <span class="pl-2"> для справки.</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchTerm: '',
      isFocused: false,
      showResults: false,
      highlightedIndex: -1,
      searchType: '',  // Тип поиска: '', 'projects', 'users'
      projects: [
        { name: 'Workflow Inc. / Редизайн Веб-сайта' },
        { name: 'Workflow Inc. / Открытое Графическое Изображение' },
        { name: 'Workflow Inc. / Дизайн Логотипа' },
        { name: 'Workflow Inc. / Рекламная Кампания' },
        { name: 'Conglomerate Inc. / ТВ Рекламная Кампания' },
        { name: 'Conglomerate Inc. / Мобильное Приложение' },
        { name: 'Conglomerate Inc. / Дизайн Продукта' },
        { name: 'Другой Пример Проекта С Очень Длинным Именем для Проверки Прокрутки' },
        { name: 'Еще Один Пример Проекта С Очень Длинным Именем' },
        { name: 'Финальный Проект С Очень Длинным Названием' }
      ],
      users: [
        { name: 'Лесли Александер', avatar: 'https://randomuser.me/api/portraits/women/44.jpg' },
        { name: 'Майкл Фостер', avatar: 'https://randomuser.me/api/portraits/men/32.jpg' },
        { name: 'Дрис Винсент', avatar: 'https://randomuser.me/api/portraits/men/47.jpg' },
        { name: 'Лесли Александер', avatar: 'https://randomuser.me/api/portraits/women/44.jpg' },
        { name: 'Майкл Фостер', avatar: 'https://randomuser.me/api/portraits/men/32.jpg' },
        { name: 'Дрис Винсент', avatar: 'https://randomuser.me/api/portraits/men/47.jpg' },
        { name: 'Лесли Александер', avatar: 'https://randomuser.me/api/portraits/women/44.jpg' },
        { name: 'Майкл Фостер', avatar: 'https://randomuser.me/api/portraits/men/32.jpg' },
        { name: 'Дрис Винсент', avatar: 'https://randomuser.me/api/portraits/men/47.jpg' },
        { name: 'Другой Пользователь с Длинным Именем' },
        { name: 'Какой-то Другой Пример Пользователя' }
      ],
      departments: [
        { name: 'Кафедра Информационных Технологий' },
        { name: 'Кафедра Математики и Моделирования' },
        { name: 'Кафедра Экономики и Управления' },
        { name: 'Кафедра Информационных Технологий' },
        { name: 'Кафедра Математики и Моделирования' },
        { name: 'Кафедра Экономики и Управления' },
        { name: 'Кафедра Информационных Технологий' },
        { name: 'Кафедра Математики и Моделирования' },
        { name: 'Кафедра Экономики и Управления' },
        { name: 'Кафедра Информационных Технологий' },
        { name: 'Кафедра Математики и Моделирования' },
        { name: 'Кафедра Экономики и Управления' },
        { name: 'Кафедра Информационных Технологий' },
        { name: 'Кафедра Математики и Моделирования' },
        { name: 'Кафедра Экономики и Управления' }
      ]
    };
  },
  computed: {
    filteredProjects() {
      const term = this.searchTerm.toLowerCase();
      if (this.searchTerm.startsWith('#')) {
        const trimmedTerm = term.slice(1);
        return this.projects.filter((project) =>
            project.name.toLowerCase().includes(trimmedTerm)
        );
      } else if (
          !this.searchTerm.startsWith('#') &&
          !this.searchTerm.startsWith('>') &&
          !this.searchTerm.startsWith('!') && // Добавлено исключение для '!'
          this.searchTerm !== '?'
      ) {
        return this.projects.filter((project) =>
            project.name.toLowerCase().includes(term)
        );
      }
      return []; // Возвращаем пустой массив, если тип поиска не соответствует
    },
    filteredUsers() {
      const term = this.searchTerm.toLowerCase();
      if (this.searchTerm.startsWith('>')) {
        const trimmedTerm = term.slice(1);
        return this.users.filter((user) =>
            user.name.toLowerCase().includes(trimmedTerm)
        );
      } else if (
          !this.searchTerm.startsWith('#') &&
          !this.searchTerm.startsWith('>') &&
          !this.searchTerm.startsWith('!') && // Добавлено исключение для '!'
          this.searchTerm !== '?'
      ) {
        return this.users.filter((user) =>
            user.name.toLowerCase().includes(term)
        );
      }
      return []; // Возвращаем пустой массив, если тип поиска не соответствует
    },
    filteredDepartments() {
      const term = this.searchTerm.toLowerCase();
      if (this.searchTerm.startsWith('!')) {
        const trimmedTerm = term.slice(1);
        return this.departments.filter(department =>
            department.name.toLowerCase().includes(trimmedTerm)
        );
      } else if (
          !this.searchTerm.startsWith('#') &&
          !this.searchTerm.startsWith('>') &&
          !this.searchTerm.startsWith('!') && // Добавлено исключение для '!'
          this.searchTerm !== '?'
      ) {
        return this.departments.filter(department =>
            department.name.toLowerCase().includes(term)
        );
      }
      return []; // Возвращаем пустой массив, если тип поиска не соответствует
    },
   hasResults() {
      return this.filteredProjects.length > 0 || this.filteredUsers.length > 0;
    },
  },
  watch: {
    searchTerm(newValue) {
      if (newValue.startsWith('#')) {
        this.searchType = 'projects';
      } else if (newValue.startsWith('>')) {
        this.searchType = 'users';
      } else {
        this.searchType = '';
      }
      this.highlightedIndex = -1; // Сброс индекса при изменении поискового запроса
    },
  },
  methods: {
    handleFocus() {
      this.isFocused = true;
      this.showResults = true;  // Добавляем эту строку
    },
    handleBlur() {
      this.isFocused = false;
      // Задержка перед скрытием результатов, чтобы успеть кликнуть на элемент
      setTimeout(() => {
        this.showResults = false;
        this.searchTerm = '';
      }, 100);
    },
    moveHighlight(direction) {
      let maxIndex = 0;
      if (this.searchType === 'projects') {
        maxIndex = this.filteredProjects.length - 1;
      } else if (this.searchType === 'users') {
        maxIndex = this.filteredUsers.length - 1;
      }
      if (maxIndex === -1) {
        return;
      }

      this.highlightedIndex =
          this.highlightedIndex + direction < 0
              ? maxIndex
              : this.highlightedIndex + direction > maxIndex
                  ? 0
                  : this.highlightedIndex + direction;
    },
    selectHighlighted() {
      if (this.highlightedIndex !== -1) {
        if (this.searchType === 'projects') {
          this.selectItem(this.filteredProjects[this.highlightedIndex], 'project');
        } else if (this.searchType === 'users') {
          this.selectItem(this.filteredUsers[this.highlightedIndex], 'user');
        }
      }
    },
    selectItem(item, type) {
      alert(`Выбрано: ${item.name} (Тип: ${type})`);
      this.searchTerm = '';
      this.showResults = false;
      this.highlightedIndex = -1;
      this.searchType = '';
    },
  },
};
</script>



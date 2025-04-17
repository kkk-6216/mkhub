<template>
  <div class="flex min-h-screen bg-[#e7f4fd] text-gray-800 p-6 space-x-6">
    <!-- Sidebar already exists -->

    <!-- Main content -->
    <div class="flex-1 space-y-6">
      <!-- My Courses Section -->
      <div>
        <h1 class="text-2xl font-semibold">My courses</h1>
        <div class="flex space-x-6 mt-4">
          <button class="font-medium border-b-2 border-black pb-1">Active</button>
          <button class="text-gray-500">Completed</button>
        </div>

        <!-- Top course cards -->
        <div class="flex space-x-4 mt-6">
          <div class="bg-[#041c4d] rounded-xl p-4 text-white w-48">
            <p class="text-sm">UX design</p>
            <h3 class="text-lg font-semibold mt-2">User flow</h3>
            <p class="text-xs mt-6 text-gray-300">4/8</p>
          </div>
          <div class="bg-[#0a1d3d] rounded-xl p-4 text-white w-48">
            <p class="text-sm">Marketing</p>
            <h3 class="text-lg font-semibold mt-2">Strategy</h3>
            <p class="text-xs mt-6 text-gray-300">9/10</p>
          </div>
          <div class="bg-[#021936] rounded-xl p-4 text-white w-48">
            <p class="text-sm">Magic</p>
            <h3 class="text-lg font-semibold mt-2">Potion making</h3>
            <p class="text-xs mt-6 text-gray-300">5/7</p>
          </div>
        </div>
      </div>

      <!-- Trending Section -->
      <div>
        <h2 class="text-xl font-semibold">Trending</h2>
        <div class="flex space-x-4 mt-2 text-sm text-gray-600">
          <button class="font-medium text-black">Design</button>
          <button>Marketing</button>
          <button>Sales</button>
          <button>Branding</button>
          <button>Advertising</button>
        </div>

        <div class="space-y-4 mt-4">
          <!-- Course Card 1 -->
          <div class="bg-white rounded-xl p-4 flex items-center shadow-sm">
            <div class="w-16 h-16 bg-green-100 rounded-lg mr-4"></div>
            <div class="flex-1">
              <h3 class="font-medium">How to create patterns</h3>
              <p class="text-xs text-gray-500">Start 18.02 • Rate 4.8 • Level - hard</p>
            </div>
            <button class="text-gray-400 hover:text-black">⋮</button>
          </div>

          <!-- Course Card 2 -->
          <div class="bg-white rounded-xl p-4 flex items-center shadow-sm">
            <div class="w-16 h-16 bg-blue-100 rounded-lg mr-4"></div>
            <div class="flex-1">
              <h3 class="font-medium">UX Research</h3>
              <p class="text-xs text-gray-500">Start 25.02 • Rate 4.5 • Level - medium</p>
            </div>
            <button class="text-gray-400 hover:text-black">⋮</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Right panel (Course details) -->
    <div class="w-64 bg-white rounded-xl p-4 shadow-md relative">
      <div class="w-full h-24 bg-blue-100 rounded-md mb-4"></div>
      <div class="absolute top-2 right-2 bg-blue-700 text-white px-2 py-1 rounded-full text-sm">$176</div>
      <h3 class="font-semibold text-lg mb-2">UX Research</h3>
      <p class="text-sm text-gray-600 mb-2">
        Want to learn more about CJM creation and informational sketches?
      </p>
      <ul class="list-disc pl-4 text-xs text-gray-500 mb-4">
        <li>UX process methodologies</li>
        <li>Customer Journey Map creation</li>
        <li>User flow & App flow</li>
      </ul>
      <div class="text-sm mb-4">
        <div><strong>32h</strong> Theory</div>
        <div><strong>58h</strong> Practice</div>
      </div>
      <button class="bg-blue-700 text-white w-full py-2 rounded-md font-medium">View schedule</button>
    </div>
  </div>
</template>

<script setup>
// Hozircha hech qanday JS logika kiritilmadi.
// Interaktivlik (tabs, ochiluvchi panel) kerak bo‘lsa alohida qo‘shamiz.
</script>

<style scoped>
/* Qo‘shimcha custom uslublar kerak bo‘lsa shu yerga yoziladi */
</style>

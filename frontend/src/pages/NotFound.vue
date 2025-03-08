<script>
import DefaultButton from "@/components/buttons/DefaultButton.vue";

export default {
  name: "NotFound",
  components: {DefaultButton},
  methods: {
    goBack() {
      if (window.history.length > 1) {
        this.$router.go(-1);
      } else {
        this.$router.push("/");
      }
    },
  },
}
</script>

<template>
  <main class="grid min-h-full place-items-center bg-white px-6 py-24 sm:py-32 lg:px-8">
    <div class="text-center">
      <p class="text-base font-semibold text-main">404</p>
      <h1 class="mt-4 text-5xl font-semibold tracking-tight text-balance text-dark sm:text-7xl">Страница не найдена</h1>
      <p class="mt-6 text-lg font-medium text-pretty text-gray-500 sm:text-xl/8">Извините, мы не смогли найти страницу, которую вы ищете.</p>
      <div class="mt-10 flex items-center justify-center gap-x-6">
<!--        <a href="#" class="rounded-md bg-main px-3.5 py-2.5 text-sm font-semibold text-white shadow-xs hover:bg-main-hover focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-main-hover">Go back home</a>-->
        <DefaultButton label="Вернуться на главную" @click="goBack"></DefaultButton>
      </div>
    </div>
  </main>
</template>

<style scoped>

</style>
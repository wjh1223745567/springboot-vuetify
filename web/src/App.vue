<template>
  <v-app>
    <v-fade-transition mode="out-in">
      <router-view />
    </v-fade-transition>

    <prop-message />
  </v-app>
</template>

<script>
  // Styles
  import '@/styles/overrides.sass'
  import PropMessage from './components/PropMessage.vue'
  export default {
    name: 'App',
    components: { PropMessage },
    metaInfo: {
      title: 'App',
      titleTemplate: '%s | Material Dashboard Free',
      htmlAttrs: { lang: 'en' },
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      ],
    },
  }
</script>

<style>
html{
  overflow-y: auto !important;
}
</style>

<template>
  <material-snackbar
    v-model="show"
    :type="type"
    :timeout="5000"
    v-bind="{
      top: true,
      center: true
    }"
  >
    <span>{{ text }}</span>
  </material-snackbar>
</template>

<script>
  import { sync } from 'vuex-pathify'
  export default {
    name: 'PropMessage',
    data () {
      return {
      }
    },
    computed: {
      ...sync('message', ['show', 'type', 'text']),
    },
    created () {
    },
  }
</script>

<style scoped>

</style>

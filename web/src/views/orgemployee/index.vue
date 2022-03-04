<template>
  <div class="main-page">
    <div class="left-tree">
      <orgtree />
    </div>
    <v-card
      class="employ-info"
      elevation="1"
    >
      <emporginfo />
      <employeeinfoVue />
    </v-card>
  </div>
</template>

<script>
  import employeeinfoVue from './common/employeeinfo.vue'
  import Emporginfo from './common/emporginfo.vue'
  import Orgtree from './common/orgtree.vue'
  export default {
    components: {
      Orgtree,
      employeeinfoVue,
      Emporginfo,
    },
    data: () => ({
    }),
    methods: {
      reloadtree () {

      },
    },
  }
</script>

<style lang="scss" scoped>
  .main-page{
    display: flex;
    width: 100%;
    margin: 0;
    padding: 0;

    .left-tree{
      width: 300px;
      padding-right: 10px;
    }

    .employ-info{
      width: calc(100% - 300px)
    }
  }
</style>

<template>
  <v-card
    class="mx-auto"
  >
    <v-sheet class="pa-4 primary lighten-2">
      <v-text-field
        v-model="search"
        label="搜索"
        dark
        flat
        solo-inverted
        hide-details
        clearable
        clear-icon="mdi-close-circle-outline"
      />
    </v-sheet>
    <div style="height: 2px;">
      <v-progress-linear
        :active="loading"
        :indeterminate="loading"
        color="primary darken-2"
      />
    </div>
    <v-card-text>
      <v-treeview
        v-if="showtreeview"
        ref="treeview"
        :active.sync="treevalue"
        item-key="id"
        activatable
        :search="search"
        class="org-tree"
        hoverable
        dense
        return-object
        :items="treedata"
        :open.sync="opentree"
      >
        <template #prepend="{ item }">
          <v-icon
            v-text="`mdi-${item.children && item.children.length ? 'home-variant' : 'folder-network'}`"
          />
        </template>
      </v-treeview>
    </v-card-text>
  </v-card>
</template>

<script>
  import { orgTree } from '@/api/orgmember'
  import { sync } from 'vuex-pathify'
  import { findPoint } from '@/util/pubmethods'
  export default {
    data: () => ({
      treevalue: [],
      search: '',
      opentree: [],
      loading: false,
      showtreeview: true,
    }),
    computed: {
      ...sync('org', ['treedata', 'reload', 'chooseorg', 'choosefirst']),
    },
    watch: {
      search: function (val) {
        if (val) {
          this.$refs.treeview.updateAll(true)
        } else {
          this.$refs.treeview.updateAll(false)
        }
      },
      treevalue: function (val) {
        const choose = val[0]
        if (choose) {
          this.opentree.push(choose)
          this.chooseorg = choose
        }
      },
      reload: function (val) {
        this.loadtree()
      },
    },
    created () {
      this.loadtree()
    },
    methods: {
      loadtree () {
        this.loading = true
        orgTree(this.search).then(resp => {
          const { code, data, msg } = resp
          if (code === '0') {
            this.showtreeview = false
            this.treedata = data
            this.initchoose()
            this.$nextTick(() => {
              this.showtreeview = true
            })
          } else {
            this.$message.error(msg)
          }
        }).finally(() => {
          this.loading = false
        })
      },
      initchoose () {
        if (this.choosefirst) {
          if (this.treedata && this.treedata.length) {
            this.chooseorg = this.treedata[0]
          } else {
            this.chooseorg = {}
          }
        } else if (this.chooseorg) {
          const chooseorg = findPoint(this.treedata, this.chooseorg.id)
          this.treevalue = [chooseorg]
        }
        this.choosefirst = false
      },
    },
  }
</script>

<style lang="scss" scoped>
  .org-tree{
    min-height: 77.5vh;
  }
</style>

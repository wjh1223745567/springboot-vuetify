<template>
  <v-menu
    offset-y
  >
    <template #activator="{ on, attrs }">
      <v-text-field
        v-model="orgname"
        label="所属部门"
        v-bind="attrs"
        clearable
        :rules=" required ? [
          v => !!v || '部门不能为空'
        ] : [] "
        v-on="on"
        @click:clear="clearchoose"
      />
    </template>

    <div class="treeviewback">
      <v-treeview
        dense
        return-object
        hoverable
        item-disabled="locked"
        activatable
        :active.sync="choose"
        :items="treedata"
      />
    </div>
  </v-menu>
</template>

<script>
  import { sync } from 'vuex-pathify'
  import { findPoint } from '@/util/pubmethods'
  export default {
    name: 'Orgtreeselect',
    props: {
      value: {
        type: String,
        default: '',
      },
      required: {
        type: Boolean,
        default: false,
      },
      disabledid: {
        type: String,
        default: '',
      },
    },
    data () {
      return {
        orgname: '',
        dates: [],
        menu: true,
        choose: [],
      }
    },
    computed: {
      ...sync('org', ['treedata', 'chooseorg']),
    },
    watch: {
      choose: function (val) {
        if (val[0]) {
          const firstval = val[0]
          this.orgname = firstval.name
          this.$emit('update:value', firstval.id)
        } else {
          this.orgname = null
          this.$emit('update:value', null)
        }
      },
      value: {
        immediate: true,
        handler: function (val) {
          if (val) {
            this.defaultvalue()
          } else {
            this.choose = []
          }
        },
      },
      disabledid: {
        immediate: true,
        handler: function (val) {
          if (val) {
            this.disabledData(this.treedata, val)
          } else {
            this.clearDisabled(this.treedata)
          }
        },
      },
    },
    methods: {
      defaultvalue () {
        const nowvalue = findPoint(this.treedata, this.value)
        if (nowvalue) {
          this.choose = [nowvalue]
        }
      },

      clearchoose () {
        this.$emit('update:value', null)
      },
      clearDisabled (data) {
        if (data && data.length) {
          for (const val of data) {
            this.$set(val, 'locked', false)
            this.clearDisabled(val.children)
          }
        }
      },

      disabledData (data, id) {
        if (data && data.length && id) {
          for (const val of data) {
            if (val.id === id) {
              this.$set(val, 'locked', true)
            } else {
              this.$set(val, 'locked', false)
              this.disabledData(val.children, id)
            }
          }
        }
      },
    },
  }
</script>

<style lang="scss">
  .treeviewback{
    background-color: #fff;
  }
</style>

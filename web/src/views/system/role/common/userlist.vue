<template>
  <v-sheet class="d-flex justify-space-between align-center">
    <v-card
      width="270"
      elevation="2"
    >
      <v-card-title>
        <div style="width: 100%;">
          <p class="text-h5">
            未授权
          </p>
          <v-text-field
            v-model="condition.noauthsearch"
            dense
            hide-details
            placeholder="搜索"
            @keyup.enter.native="getRoleUserFun"
            @blur="getRoleUserFun"
          />
          <div style="height: 2px;">
            <v-progress-linear
              :active="loading"
              :indeterminate="loading"
              color="primary darken-2"
            />
          </div>
        </div>
      </v-card-title>
      <v-card-text class="checkboxgroup">
        <v-checkbox
          v-for="(item) in leftcheck"
          :key="item.id"
          v-model="leftselected"
          hide-details
          dense
          :label="item.name"
          :value="item.id"
          color="primary"
        />
      </v-card-text>
    </v-card>

    <v-btn
      small
      color="primary"
      @click="movetoleft"
    >
      <v-icon
        size="25"
      >
        mdi-arrow-left-bold
      </v-icon>
    </v-btn>

    <v-btn
      small
      color="primary"
      @click="movetoright"
    >
      <v-icon
        size="25"
      >
        mdi-arrow-right-bold
      </v-icon>
    </v-btn>

    <v-card
      width="270"
      elevation="2"
    >
      <v-card-title>
        <div style="width: 100%;">
          <p class="text-h5">
            已授权
          </p>
          <v-text-field
            v-model="condition.authsearch"
            dense
            hide-details
            placeholder="搜索"
            @keyup.enter.native="getRoleUserFun"
            @blur="getRoleUserFun"
          />
          <div style="height: 2px;">
            <v-progress-linear
              :active="loading"
              :indeterminate="loading"
              color="primary darken-1"
            />
          </div>
        </div>
      </v-card-title>
      <v-card-text class="checkboxgroup">
        <v-checkbox
          v-for="(item) in rightcheck"
          :key="item.id"
          v-model="rightselected"
          hide-details
          dense
          :label="item.name"
          :value="item.id"
          color="primary"
        />
      </v-card-text>
    </v-card>
  </v-sheet>
</template>

<script>
  import { removeRoleUser, addRoleUser, getRoleUser } from '@/api/role'
  export default {
    props: {
      roleId: {
        type: String,
      },
    },
    data () {
      return {
        leftselected: [],
        leftcheck: [],
        rightselected: [],
        rightcheck: [],

        condition: {
          noauthsearch: '',
          authsearch: '',
        },

        loading: false,
      }
    },
    watch: {
      roleId: {
        immediate: true,
        handler: function (val) {
          if (val) {
            this.getRoleUserFun()
          }
        },
      },
    },
    methods: {
      clearall () {
        this.leftcheck = []
        this.leftselected = []
        this.rightcheck = []
        this.rightselected = []
      },
      movetoleft () {
        if (this.rightselected.length > 0) {
          this.removeRoleUserFun(this.rightselected.concat())
        }
        this.rightselected = []
      },
      movetoright () {
        if (this.leftselected.length > 0) {
          this.addRoleUserFun(this.leftselected.concat())
        }
        this.leftselected = []
      },

      // 获取角色用户
      getRoleUserFun () {
        const param = Object.assign({}, this.condition)
        param.roleId = this.roleId
        this.loading = true
        getRoleUser(param).then(resp => {
          const { code, data, msg } = resp
          if (code === '0') {
            const { noAuthUser, authUser } = data
            this.leftcheck = noAuthUser
            this.rightcheck = authUser
          } else {
            this.$message.error(msg)
          }
        }).finally(() => {
          this.loading = false
        })
      },

      addRoleUserFun (memberIds) {
        this.loading = false
        addRoleUser({ roleId: this.roleId, memberIds }).then(resp => {
          const { code, msg } = resp
          if (code === '0') {
            this.$message.success('人员授权成功')
            this.getRoleUserFun()
          } else {
            this.$message.error(msg)
          }
        }).finally(() => {
          this.loading = true
        })
      },

      removeRoleUserFun (memberIds) {
        this.loading = false
        removeRoleUser({ roleId: this.roleId, memberIds }).then(resp => {
          const { code, msg } = resp
          if (code === '0') {
            this.$message.success('移除授权成功')
            this.getRoleUserFun()
          } else {
            this.$message.error(msg)
          }
        }).finally(() => {
          this.loading = true
        })
      },
    },
  }
</script>

<style scoped>
  .checkboxgroup{
    height: 600px;
    overflow-y: auto;
    overflow-x: hidden;
  }
</style>

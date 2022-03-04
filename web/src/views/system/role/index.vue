<template>
  <div>
    <material-table
      title="角色信息"
      :tableicon="$icon.mdiAccountSync"
      :headers="headers"
      :desserts="desserts"
      :loading="loading"
      :server-items-length="serverItemsLength"
      @save="savefun"
      @deleted="deletedfun"
      @reload="reloaddata"
    >
      <template #formfield="{ editedItem }">
        <v-row>
          <v-col
            cols="12"
            sm="6"
            md="4"
          >
            <v-text-field
              v-model="editedItem.name"
              label="角色名称"
              counter="20"
              :rules="[
                v => !!v || '角色名称不能为空',
                v => !v || v.length <=20 || '长度不能超过20个字符']"
              required
            />
          </v-col>
          <v-col
            cols="12"
            sm="6"
            md="4"
          >
            <v-text-field
              v-model="editedItem.remark"
              label="角色描述"
              counter="200"
              :rules="[
                v => !v || v.length <=200 || '长度不能超过200个字符'
              ]"
            />
          </v-col>
        </v-row>
      </template>
      <template #othactions="{ item }">
        <span>
          <v-icon
            small
            class="mr-2"
            color="primary"
            @click="chooseUser(item)"
            v-text="'mdi-account-switch'"
          />

          <v-icon
            small
            class="mr-2"
            color="primary"
            @click="choosePermission(item)"
            v-text="'mdi-account-key'"
          />
        </span>
      </template>
    </material-table>

    <v-dialog
      v-model="permissiondialog"
      max-width="500px"
    >
      <v-card>
        <v-card-title>
          <span class="text-h5">权限</span>
        </v-card-title>

        <v-card-text>
          <v-treeview
            v-model="permissselection"
            selectable
            :items="permisstreeitem"
            item-key="value"
            selected-color="primary"
            selection-type="independent"
          />
        </v-card-text>

        <v-card-actions>
          <v-spacer />
          <v-btn
            text
            color="secondary darken-1"
            @click="closepermission"
          >
            取消
          </v-btn>
          <v-btn
            text
            color="primary darken-1"
            :loading="savepermissionloading"
            @click="savepermission"
          >
            保存
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog
      v-model="userdialog"
      max-width="800px"
    >
      <v-card>
        <v-card-title class="text-h5">
          角色授权
        </v-card-title>
        <v-card-text>
          <userlist
            ref="userlist"
            :role-id="editRow.id"
          />
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn
            text
            color="primary darken-1"
            @click="userdialog = false"
          >
            关闭
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
  import { list, save, deleted, updatePermission } from '@/api/role'
  import { permission } from '@/util/permission'
  import userlist from './common/userlist.vue'
  export default {
    components: { userlist },
    data: () => ({
      headers: [
        {
          text: '角色名称',
          align: 'start',
          value: 'name',
        },
        { text: '角色描述', value: 'remark' },
      ],
      desserts: [],
      loading: false,
      serverItemsLength: 0,

      permissiondialog: false,
      savepermissionloading: false,

      editRow: {},
      permisstreeitem: permission,
      permissselection: [],

      searchcondition: {},

      userdialog: false,
    }),
    watch: {
      userdialog: function (val) {
        val || this.closeuserdialog()
      },
    },
    methods: {
      // 保存
      savefun (val) {
        save(val).then(resp => {
          const { code, data, msg } = resp
          if (code === '0') {
            if (!val.id) {
              this.serverItemsLength++
            }
            val.id = data
            this.$message.success('保存成功')
          } else {
            this.$message.error(msg)
          }
        })
      },
      // 删除
      deletedfun (val) {
        deleted([val.id]).then(resp => {
          const { code, msg } = resp
          if (code === '0') {
            this.$message.success('删除成功')
            this.serverItemsLength--
          } else {
            this.$message.error(msg)
          }
        })
      },
      reloaddata (val) {
        this.searchcondition = val
        this.initialize(val)
      },
      initialize (condition) {
        this.loading = true
        list(condition).then(resp => {
          const { code, data, msg } = resp
          if (code === '0') {
            const { records, total } = data
            this.desserts = records
            this.serverItemsLength = total
          } else {
            this.$message.error(msg)
          }
        }).finally(() => {
          this.loading = false
        })
      },
      // 修改人员
      chooseUser (val) {
        this.editRow = Object.assign({}, val)
        this.userdialog = true
      },

      closeuserdialog () {
        this.editRow = {}
        this.$refs.userlist.clearall()
        this.userdialog = false
      },

      // 修改资源权限
      choosePermission (val) {
        this.editRow = Object.assign({}, val)
        this.permissselection = this.editRow && this.editRow.permissions ? this.editRow.permissions : []
        this.permissiondialog = true
      },

      closepermission () {
        this.permissselection = []
        this.editRow = {}
        this.permissiondialog = false
      },

      savepermission () {
        this.savepermissionloading = true
        updatePermission({ id: this.editRow.id, permissions: this.permissselection }).then(resp => {
          const { code, msg } = resp
          if (code === '0') {
            this.$message.success('保存成功')
            this.closepermission()
            this.reloaddata(this.searchcondition)
          } else {
            this.$message.error(msg)
          }
        }).finally(() => {
          this.savepermissionloading = false
        })
      },
    },
  }
</script>

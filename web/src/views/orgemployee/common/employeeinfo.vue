<template>
  <v-data-table
    v-model="selected"
    :headers="headers"
    :items="desserts"
    class="elevation-1"
    item-key="id"
    show-select
    :options.sync="options"
    :footer-props="footerProps"
    height="70vh"
    fixed-header
    :loading="tableloading"
    :server-items-length="serverItemsLength"
    loading-text="加载中..."
  >
    <template #top>
      <v-toolbar
        flat
      >
        <v-toolbar-title class="d-flex justify-start align-center">
          <v-sheet>
            <v-icon size="30">
              mdi-account
            </v-icon>
          </v-sheet>
        </v-toolbar-title>
        <span class="font-weight-medium text-h4">部门人员</span>
        <v-spacer />

        <div
          style="width: 360px;"
          class="mr-2"
        >
          <v-text-field
            v-model="condition.search"
            hide-details="auto"
            label="搜索"
            solo
            dense
            @blur="initialize"
            @keyup.enter.native="initialize"
          />
        </div>
        <v-btn
          elevation="1"
          color="primary"
          height="38"
          @click="initialize"
        >
          搜索
        </v-btn>
        <v-spacer />
        <v-dialog
          v-model="dialog"
          max-width="1000px"
        >
          <template #activator="{ on, attrs }">
            <v-btn
              color="primary"
              dark
              class="mb-2"
              v-bind="attrs"
              v-on="on"
            >
              新增
            </v-btn>
          </template>
          <v-card>
            <v-card-title>
              <span class="text-h5">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-form ref="saveform">
                  <v-row>
                    <v-col
                      cols="12"
                      sm="6"
                      md="4"
                      class="d-flex justify-center"
                    >
                      <v-avatar
                        color="primary"
                        size="80"
                      >
                        <v-img :src="editedItem.showavatar" />
                      </v-avatar>
                    </v-col>

                    <v-col
                      cols="12"
                      sm="6"
                      md="4"
                    >
                      <v-text-field
                        v-model="editedItem.name"
                        label="姓名"
                        counter="30"
                        maxlength="30"
                        :rules="[
                          v => !!v || '姓名不能为空',
                        ]"
                      />
                    </v-col>

                    <v-col
                      cols="12"
                      sm="6"
                      md="4"
                    >
                      <v-radio-group
                        v-model="editedItem.gender"
                        row
                      >
                        <v-radio
                          label="男"
                          :value="1"
                        />
                        <v-radio
                          label="女"
                          :value="0"
                        />
                      </v-radio-group>
                    </v-col>

                    <v-col
                      cols="12"
                      sm="6"
                      md="4"
                    >
                      <v-file-input
                        v-model="editedItem.avatar"
                        :rules="[
                          v => !v || v.size < 4000000 || '头像大小不能超过4MB'
                        ]"
                        accept="image/png, image/jpeg, image/bmp"
                        placeholder="上传头像"
                        prepend-icon="mdi-camera"
                        @click:clear="editedItem.showavatar = null"
                      />
                    </v-col>

                    <v-col
                      cols="12"
                      sm="6"
                      md="4"
                    >
                      <v-text-field
                        v-model="editedItem.workNum"
                        label="工号"
                        counter="30"
                        maxlength="30"
                      />
                    </v-col>

                    <v-col
                      cols="12"
                      sm="6"
                      md="4"
                    >
                      <orgtreeselect
                        :value.sync="editedItem.orgId"
                        required
                      />
                    </v-col>

                    <v-col
                      cols="12"
                      sm="6"
                      md="4"
                    >
                      <v-text-field
                        v-model="editedItem.mobile"
                        label="手机号"
                        counter="15"
                        maxlength="15"
                      />
                    </v-col>
                  </v-row>
                </v-form>
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer />
              <v-btn
                color="secondary darken-1"
                text
                @click="close"
              >
                取消
              </v-btn>
              <v-btn
                color="primary darken-1"
                text
                :loading="saveloading"
                @click="save"
              >
                保存
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <v-dialog
          v-model="dialogDelete"
          max-width="500px"
        >
          <v-card>
            <v-card-title class="text-h5">
              确认要删除这行数据吗?
            </v-card-title>
            <v-card-actions>
              <v-spacer />
              <v-btn
                color="secondary darken-1"
                text
                @click="closeDelete"
              >
                取消
              </v-btn>
              <v-btn
                color="primary darken-1"
                text
                :loading="deletedloading"
                @click="deleteItemConfirm"
              >
                确认
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>
    <template #item.actions="{ item }">
      <v-icon
        small
        class="mr-2"
        color="primary"
        @click="editItem(item)"
      >
        mdi-pencil
      </v-icon>
      <v-icon
        small
        color="error"
        @click="deleteItem(item)"
      >
        mdi-delete
      </v-icon>
    </template>
    <template #no-data>
      暂无数据
    </template>
  </v-data-table>
</template>
<script>
  import { memberPage, memberSave, memberDeleted } from '@/api/orgmember'
  import toServerPagePram from '@/util/pageparam'
  import { get } from 'vuex-pathify'
  import Orgtreeselect from './orgtreeselect.vue'
  export default {
    components: {
      Orgtreeselect,
    },
    data: () => ({
      selected: [],
      dialog: false,
      dialogDelete: false,
      footerProps: {
        itemsPerPageOptions: [10, 20, 50],
        showFirstLastPage: true,
      },
      options: {},

      tableloading: false,
      serverItemsLength: 0,
      condition: {
        search: '',
      },

      saveloading: false,
      deletedloading: false,

      headers: [
        { text: '姓名', value: 'name' },
        { text: '工号', value: 'workNum' },
        { text: '部门', value: 'orgName', sortable: false },
        { text: '性别', value: 'genderName', sortable: false },
        { text: '电话', value: 'mobile' },
        { text: '操作', value: 'actions', sortable: false },
      ],
      desserts: [],
      editedIndex: -1,
      editedItem: {
        gender: 1,
        orgId: '',
      },
      defaultItem: {
        avatar: null,
        name: '',
        gender: 1,
        workNum: '',
        mobile: '',
        orgId: '',
      },
    }),

    computed: {
      formTitle () {
        return this.editedIndex === -1 ? '新增' : '编辑'
      },
      ...get('org', ['chooseorg']),
    },

    watch: {
      dialog (val) {
        val || this.close()
      },
      dialogDelete (val) {
        val || this.closeDelete()
      },
      options: {
        handler () {
          this.initialize()
        },
        deep: true,
      },
      chooseorg: function (val) {
        this.defaultItem.orgId = val.id
        this.editedItem.orgId = val.id
        this.initialize()
      },

      'editedItem.avatar': function (val) {
        if (val && typeof val === 'object') {
          this.editedItem.showavatar = window.URL.createObjectURL(val)
        }
      },
    },

    methods: {
      initialize () {
        const cond = Object.assign({}, this.condition)
        cond.orgId = this.chooseorg.id
        if (!cond.orgId) {
          return
        }
        this.tableloading = true
        memberPage(toServerPagePram(this.options, cond)).then(resp => {
          const { code, data, msg } = resp
          if (code === '0') {
            const { total, records } = data
            this.desserts = records
            this.serverItemsLength = total
          } else {
            this.$message.error(msg)
          }
        }).finally(() => {
          this.tableloading = false
        })
      },

      editItem (item) {
        this.editedIndex = this.desserts.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.editedItem.showavatar = this.editedItem.avatar ? '/' + this.editedItem.avatar : ''
        this.editedItem.avatar = null
        this.dialog = true
      },

      deleteItem (item) {
        this.editedIndex = this.desserts.indexOf(item)
        this.dialogDelete = true
      },

      deleteItemConfirm () {
        this.deletedloading = true
        memberDeleted([this.desserts[this.editedIndex].id]).then(resp => {
          const { code, msg } = resp
          if (code === '0') {
            this.$message.success('删除成功')
            this.closeDelete()
            this.initialize()
          } else {
            this.$message.error(msg)
          }
        }).finally(() => {
          this.deletedloading = false
        })
      },

      close () {
        this.dialog = false
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.$refs.saveform.resetValidation()
          this.editedIndex = -1
        })
      },

      closeDelete () {
        this.dialogDelete = false
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        })
      },

      save () {
        const valid = this.$refs.saveform.validate()
        if (!valid) {
          return
        }

        this.saveloading = true
        const param = Object.assign({}, this.editedItem)
        if (typeof param.avatar === 'string') {
          param.avatar = null
        }
        memberSave(param).then(resp => {
          const { code, msg } = resp
          if (code === '0') {
            this.$message.success('保存成功')
            this.initialize()
            this.close()
          } else {
            this.$message.error(msg)
          }
        }).finally(() => {
          this.saveloading = false
        })
      },
    },
  }
</script>

<style lang="scss" scoped>

</style>

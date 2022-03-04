<template>
  <v-sheet class="d-flex justify-start align-center pa-3">
    <div class="text-h3 font-weight-medium">
      {{ chooseorg.name }}
    </div>

    <v-tooltip bottom>
      <template
        #activator="{on, attrs}"
      >
        <v-icon
          color="primary"
          class="pointer ml-1"
          v-bind="attrs"
          v-on="on"
          @click="saveorgdialog(chooseorg)"
        >
          {{ $icon.mdiPencil }}
        </v-icon>
      </template>
      <span>编辑部门</span>
    </v-tooltip>

    <v-tooltip bottom>
      <template #activator="{on, attrs}">
        <v-icon
          class="pointer ml-1"
          color="error"
          v-bind="attrs"
          v-on="on"
          @click="deletedorg"
        >
          {{ $icon.mdiDelete }}
        </v-icon>
      </template>
      <span>删除部门</span>
    </v-tooltip>

    <v-spacer />

    <v-tooltip bottom>
      <template #activator="{on, attrs}">
        <v-icon
          size="35"
          color="primary"
          class="float-right pointer"
          v-bind="attrs"
          v-on="on"
          @click="saveorgdialog()"
        >
          {{ $icon.mdiPlusBox }}
        </v-icon>
      </template>
      <span>新增下级部门</span>
    </v-tooltip>

    <v-dialog
      v-model="saveDialog"
      max-width="1000px"
    >
      <v-card>
        <v-card-title>
          <span class="text-h5">{{ savetitle }}</span>
        </v-card-title>

        <v-card-text>
          <v-form ref="saveform">
            <v-row>
              <v-col
                cols="12"
                sm="6"
                md="4"
              >
                <v-text-field
                  v-model="formitem.name"
                  label="部门名称"
                  counter="20"
                  :rules="[
                    v => !!v || '部门名称不能为空',
                    v => !v || v.length <= 20 || '长度不能超过20个字符'
                  ]"
                />
              </v-col>

              <v-col
                cols="12"
                sm="6"
                md="4"
              >
                <orgtreeselect
                  :value.sync="formitem.pid"
                  :disabledid="formitem.id"
                />
              </v-col>

              <v-col
                cols="12"
                sm="6"
                md="4"
              >
                <v-text-field
                  v-model="formitem.remark"
                  label="备注"
                  counter="200"
                  :rules="[
                    v => !v || v.length <=200 || '长度不能超过200个字符'
                  ]"
                />
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>

        <v-card-actions>
          <v-spacer />
          <v-btn
            text
            color="secondary darken-1"
            @click="closeSaveDialog"
          >
            取消
          </v-btn>
          <v-btn
            text
            color="primary darken-1"
            :loading="saveloading"
            @click="savedata"
          >
            保存
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog
      v-model="deletedDialog"
      max-width="500px"
    >
      <v-card>
        <v-card-title class="text-h5">
          确定要删除当前部门吗？
        </v-card-title>
        <v-card-actions>
          <v-spacer />
          <v-btn
            text
            color="secondary darken-1"
            @click="deletedDialog = false"
          >
            取消
          </v-btn>
          <v-btn
            text
            color="primary darken-1"
            :loading="deletedloading"
            @click="deletedConfirm"
          >
            确认
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-sheet>
</template>

<script>
  import { orgDeleted, orgSave } from '@/api/orgmember'
  import { sync } from 'vuex-pathify'
  import orgtreeselect from './orgtreeselect.vue'
  export default {
    components: {
      orgtreeselect,
    },
    data () {
      return {
        deletedDialog: false,
        deletedloading: false,

        saveDialog: false,
        saveloading: false,

        formitem: {},
        savetitle: '新增',
      }
    },
    computed: {
      ...sync('org', ['reload', 'chooseorg', 'choosefirst']),
    },
    watch: {
      saveDialog: function (val) {
        val || this.closeSaveDialog()
      },
    },
    methods: {
      deletedorg () {
        this.deletedDialog = true
      },
      deletedConfirm () {
        this.deletedloading = true
        orgDeleted([this.chooseorg.id]).then(resp => {
          const { code, msg } = resp
          if (code === '0') {
            this.$message.success('删除成功')
            this.reloadtree()
            this.choosefirst = true
            this.deletedDialog = false
          } else {
            this.$message.error(msg)
          }
        }).finally(() => {
          this.deletedloading = false
        })
      },
      saveorgdialog (val) {
        this.savetitle = val ? '编辑' : '新增'
        if (val) {
          this.formitem = Object.assign({}, val)
        } else {
          this.formitem = {
            pid: this.chooseorg ? this.chooseorg.id : null,
          }
        }
        this.saveDialog = true
      },
      closeSaveDialog () {
        this.savetitle = '新增'
        this.formitem = {
          pid: this.chooseorg ? this.chooseorg.id : null,
        }
        this.$refs.saveform.resetValidation()
        this.saveDialog = false
      },
      savedata () {
        const valid = this.$refs.saveform.validate()
        if (valid) {
          this.saveloading = true
          orgSave(this.formitem).then(resp => {
            const { code, msg } = resp
            if (code === '0') {
              this.$message.success('保存成功')
              this.reloadtree()
              this.saveDialog = false
            } else {
              this.$message.error(msg)
            }
          }).finally(() => {
            this.saveloading = false
          })
        }
      },
      reloadtree () {
        this.reload = !this.reload
      },
    },
  }
</script>

<style lang="scss" scoped>
</style>

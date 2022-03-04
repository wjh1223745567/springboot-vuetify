<template>
  <v-container
    id="regular-tables-view"
    fluid
    tag="section"
  >
    <material-card
      :icon="tableicon"
      icon-small
      :title="title"
      color="primary"
    >
      <v-data-table
        :headers="headers"
        :items="desserts"
        class="elevation-1"
        :options.sync="options"
        height="63vh"
        fixed-header
        loading-text="加载中..."
        :loading="loading"
        :footer-props="footerOptions"
        :server-items-length="serverItemsLength"
      >
        <template #top>
          <v-toolbar
            flat
          >
            <slot
              name="searchcond"
              :condition="condition"
            />
            <div style="width: 360px;">
              <v-text-field
                v-model="condition.search"
                class="pa-2"
                hide-details="auto"
                label="搜索"
                solo
                dense
                @blur="reloaddata"
                @keydown.enter.native="reloaddata"
              />
            </div>
            <v-btn
              color="primary"
              height="38"
              elevation="2"
              @click="reloaddata"
            >
              搜索
            </v-btn>
            <v-spacer class="hidden-sm-and-down" />
            <v-dialog
              v-if="!hiddenbtn"
              v-model="dialog"
              max-width="600px"
            >
              <template #activator="{ on, attrs }">
                <v-btn
                  color="primary"
                  dark
                  class="mb-2"
                  height="38"
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
                  <v-form ref="vform">
                    <v-container>
                      <slot
                        name="formfield"
                        :editedItem="editedItem"
                      />
                    </v-container>
                  </v-form>
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
                    @click="save"
                  >
                    保存
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
            <v-dialog
              v-if="!hiddenbtn"
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
                    text
                    color="primary darken-1"
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
          <slot
            name="othactions"
            :item="item"
          />
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
          <div>暂无数据</div>
        </template>

        <template
          v-for="name in slotname"
          #[pareseSlotItem(name)]="{ item}"
        >
          <slot
            :name="name"
            :item="item"
          />
        </template>
      </v-data-table>
    </material-card>
  </v-container>
</template>

<script>
  export default {
    name: 'MaterialTable',
    props: {
      title: {
        type: String,
        default: '',
      },
      tableicon: {
        type: String,

      },
      desserts: {
        type: Array,
        default: () => [],
      },
      headers: {
        type: Array,
        default: () => [],
      },
      loading: {
        type: Boolean,
        default: false,
      },
      serverItemsLength: {
        type: Number,
        default: 0,
      },
      hiddenbtn: {
        type: Boolean,
        default: false,
      },
      slotname: {
        type: Array,
        default: () => [],
      },
    },
    data: () => ({
      condition: {
        search: '',
      },
      options: {},
      dialog: false,
      dialogDelete: false,
      footerOptions: {
        itemsPerPageOptions: [10, 20, 50],
        showFirstLastPage: true,
      },
      editedIndex: -1,
      editedItem: {},
      defaultItem: {},
    }),

    computed: {
      formTitle () {
        return this.editedIndex === -1 ? '新增' : '编辑'
      },
    },

    watch: {
      dialog (val) {
        val || this.close()
        !val || !this.$refs.vform || this.$refs.vform.resetValidation()
      },
      dialogDelete (val) {
        val || this.closeDelete()
      },
      options: {
        handler () {
          this.reloaddata()
        },
        deep: true,
      },
    },

    created () {
      // 添加默认操作列
      if (this.headers && !this.hiddenbtn) {
        this.headers.push({ text: '操作', value: 'actions', sortable: false, align: 'center' })
      }
    },

    methods: {
      pareseSlotItem (val) {
        return 'item.' + val
      },
      editItem (item) {
        this.editedIndex = this.desserts.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialog = true
      },

      deleteItem (item) {
        this.editedIndex = this.desserts.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialogDelete = true
      },

      deleteItemConfirm () {
        this.$emit('deleted', this.editedItem)
        this.desserts.splice(this.editedIndex, 1)
        this.closeDelete()
      },

      close () {
        this.dialog = false
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
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
        const valid = this.$refs.vform.validate()
        if (valid) {
          if (this.editedIndex > -1) {
            Object.assign(this.desserts[this.editedIndex], this.editedItem)
          } else {
            this.desserts.push(this.editedItem)
          }
          this.$emit('save', this.editedItem)
          this.close()
        }
      },

      reloaddata () {
        const params = Object.assign({}, this.options)
        const condition = Object.assign({}, this.condition)
        this.$emit('reload', this.$pageParam(params, condition))
      },
    },
  }
</script>

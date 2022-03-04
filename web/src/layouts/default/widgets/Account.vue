<template>
  <div>
    <v-menu
      bottom
      left
      min-width="200"
      offset-y
      origin="top right"
      transition="scale-transition"
    >
      <template v-slot:activator="{ attrs, on }">
        <v-btn
          class="ml-2"
          min-width="0"
          text
          v-bind="attrs"
          v-on="on"
        >
          <v-icon>mdi-account</v-icon>
        </v-btn>
      </template>

      <v-list
        :tile="false"
        flat
        nav
      >
        <template v-for="(p, i) in profile">
          <v-divider
            v-if="p.divider"
            :key="`divider-${i}`"
            class="mb-2 mt-2"
          />

          <app-bar-item
            v-else
            :key="`item-${i}`"
            :to="p.path"
          >
            <v-list-item
              @click="logoutfun(p.path)"
              v-text="p.title"
            />
          </app-bar-item>
        </template>
      </v-list>
    </v-menu>

    <v-dialog
      v-model="showupdatepwd"
      max-width="600px"
    >
      <v-card>
        <v-card-title>
          <span class="text-h5">修改密码</span>
        </v-card-title>
        <v-card-text>
          <v-form ref="pwdform">
            <v-row>
              <v-col
                cols="12"
                sm="6"
                md="4"
              >
                <v-text-field
                  v-model="password.oldpwd"
                  label="旧密码"
                  :rules="[
                    v => !!v || '旧密码不能为空'
                  ]"
                  :append-icon="showpwd ? 'mdi-eye' : 'mdi-eye-off'"
                  :type="showpwd ? 'text' : 'password'"
                  @click:append="showpwd = !showpwd"
                />
              </v-col>
              <v-col
                cols="12"
                sm="6"
                md="4"
              >
                <v-text-field
                  v-model="password.newpwd"
                  :rules="[
                    v => !!v || '新密码不能为空',
                    v => !v || v.length > 6 || '密码长度不能少于6位',
                    v => !v || !password.confirmpwd || v == password.confirmpwd || '新密码与确认密码不相同'
                  ]"
                  :append-icon="shownewpwd ? 'mdi-eye' : 'mdi-eye-off'"
                  :type="shownewpwd ? 'text' : 'password'"
                  label="新密码"
                  @click:append="shownewpwd = !shownewpwd"
                />
              </v-col>

              <v-col
                cols="12"
                sm="6"
                md="4"
              >
                <v-text-field
                  v-model="password.confirmpwd"
                  :rules="[
                    v => !!v || '确认密码不能为空',
                    v => !v || !password.newpwd || password.newpwd == v || '确认密码与新密码不相同'
                  ]"
                  label="确认密码"
                  :append-icon="showconfirmpwd ? 'mdi-eye' : 'mdi-eye-off'"
                  :type="showconfirmpwd ? 'text' : 'password'"
                  @click:append="showconfirmpwd = !showconfirmpwd"
                  @keyup.enter.native="savepwd"
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
            @click="closeupdatepwd"
          >
            取消
          </v-btn>
          <v-btn
            text
            color="primary darken-1"
            :loading="loading"
            @click="savepwd"
          >
            保存
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
  import { removeToken } from '@/util/auth'
  import { logout, updatePwd } from '@/api/auth'
  import { encrypt } from '@/util/aescrypt'
  export default {
    name: 'DefaultAccount',
    data: () => ({
      profile: [
        { title: '修改密码' },
        { divider: true },
        { title: '登出', path: '/login' },
      ],
      showupdatepwd: false,
      password: {},
      loading: false,

      showpwd: false,
      shownewpwd: false,
      showconfirmpwd: false,
    }),

    methods: {
      logoutfun (path) {
        if (path === '/login') {
          logout().then(resp => {
            removeToken()
            this.$store.set('user/userInfo', {})
          })
        }
        if (!path) {
          this.showupdatepwd = true
        }
      },
      closeupdatepwd () {
        this.password = {}
        this.$refs.pwdform.resetValidation()
        this.showupdatepwd = false
      },
      savepwd () {
        this.loading = true
        const { oldpwd, newpwd } = this.password
        updatePwd({ oldpwd: encrypt(oldpwd), newpwd: encrypt(newpwd) }).then(resp => {
          const { code, msg } = resp
          if (code === '0') {
            this.$message.success('密码修改成功')
            this.closeupdatepwd()
          } else {
            this.$message.error(msg)
          }
        }).finally(() => {
          this.loading = false
        })
      },
    },
  }
</script>

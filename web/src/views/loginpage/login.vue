<template>
  <div class="d-flex justify-center align-center outdiv">
    <v-card style="width: 400px;">
      <v-card-title>
        <v-divider />
        <span class="text-h3 ml-3 mr-3 font-weight-regular">登录</span>
        <v-divider />
      </v-card-title>
      <v-card-text>
        <v-form ref="form">
          <v-text-field
            v-model.trim="formdata.username"
            :rules="[
              v => !!v || '用户名不能为空',
            ]"
            label="用户名"
            required
            autofocus
          />

          <v-text-field
            v-model.trim="formdata.password"
            :rules="[
              v => !!v || '密码不能为空',
            ]"
            :append-icon="showpwd ? 'mdi-eye' : 'mdi-eye-off'"
            :type="showpwd ? 'text' : 'password'"
            label="密码"
            required
            @click:append="showpwd = !showpwd"
          />

          <v-text-field
            v-model="formdata.validCode"
            label="验证码"
            :rules="[
              v => !!v || '验证码不能为空'
            ]"
            required
            @keyup.enter.native="submit"
          >
            <template #append>
              <v-img
                v-if="image"
                class="validcode"
                :src="image"
                width="120"
                contain
                @click="changevalidimg"
              />
            </template>
          </v-text-field>

          <v-checkbox
            v-model="rememberpwd"
            label="记住密码"
          />

          <div class="d-flex justify-center">
            <v-btn
              color="primary"
              :loading="loading"
              block
              @click="submit"
            >
              登录
            </v-btn>
          </div>
        </v-form>
      </v-card-text>
    </v-card>
  </div>
</template>
<script>
  import { validImg, login } from '@/api/auth'
  import { encrypt } from '@/util/aescrypt'
  import { setToken, rememberPwd, getPwd } from '@/util/auth'
  export default {
    data () {
      return {
        showpwd: false,
        formdata: {
          username: '',
          password: '',
          validCode: '',
          valueKey: '',
        },
        image: '',
        loading: false,
        rememberpwd: false,
      }
    },
    mounted () {
      this.changevalidimg()
      const { username, password } = getPwd()
      this.formdata.username = username
      this.formdata.password = password
    },
    methods: {
      submit () {
        const valid = this.$refs.form.validate()
        if (valid) {
          this.loading = true
          const param = Object.assign({}, this.formdata)
          param.username = encrypt(param.username)
          param.password = encrypt(param.password)
          login(param).then(resp => {
            const { code, data, msg } = resp
            if (code === '0') {
              if (this.rememberpwd) {
                const { username, password } = this.formdata
                rememberPwd({ username, password })
              }
              this.$store.set('user/userInfo', data)
              localStorage.setItem('user/userInfo', JSON.stringify(data))
              setToken(data.token)
              this.$router.replace('/')
            } else {
              this.changevalidimg()
              this.$message.error(msg)
            }
          }).finally(() => {
            this.loading = false
          })
        }
      },
      changevalidimg () {
        validImg().then(resp => {
          const { code, data, msg } = resp
          if (code === '0') {
            this.image = `data:image/jpeg;base64,${data.base64}`
            this.formdata.valueKey = data.valueKey
          } else {
            this.$message.error(msg)
          }
        })
      },
    },
  }
</script>

<style lang="scss" scoped>
  .validcode {
    cursor: pointer;
  }

  .outdiv{
    width: 100%;
    height: 60%;
  }
</style>

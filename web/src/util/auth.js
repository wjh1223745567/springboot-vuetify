import Cookies from 'js-cookie'

const TokenKey = 'Vuetify-Token-front'

export function getToken () {
  return Cookies.get(TokenKey)
}

export function setToken (token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken () {
  localStorage.clear()
  return Cookies.remove(TokenKey)
}

/**
 * 记住密码
 */
export function rememberPwd (form) {
  return Cookies.set(TokenKey + '-pwd', JSON.stringify(form))
}

/**
 * 获取密码
 * @returns
 */
export function getPwd () {
  return Cookies.get(TokenKey + '-pwd') ? JSON.parse(Cookies.get(TokenKey + '-pwd')) : { username: '', password: '' }
}

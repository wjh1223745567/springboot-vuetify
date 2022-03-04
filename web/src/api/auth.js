import { get, post } from '@/util/request'

// 获取验证码
export function validImg () {
  return get('user/valid_img')
}

export function login (data) {
  return post('user/login', data)
}

export function logout () {
  return get('user/logout')
}

export function updatePwd (data) {
  return post('user/update_pwd', data)
}

import { get, post, del } from '@/util/request'

export function list (condition) {
  return get('sys_role', condition)
}

export function save (data) {
  return post('sys_role', data)
}

export function deleted (ids) {
  return del('sys_role', { ids: ids })
}

// 更新角色权限
export function updatePermission (data) {
  return post('sys_role/update_permissions', data)
}

/**
 * 获取角色用户信息
 * @param {*} data
 * @returns
 */
export function getRoleUser (data) {
  return get('sys_role/get_role_user', data)
}

/**
 * 添加角色
 * @param {*} data
 */
export function addRoleUser (data) {
  return post('sys_role/add_role_user', data)
}

/**
 * 移除角色用户
 * @param {*} data
 * @returns
 */
export function removeRoleUser (data) {
  return post('sys_role/remove_role_user', data)
}

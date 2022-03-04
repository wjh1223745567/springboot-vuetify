import { get, post, del } from '@/util/request'

// 部门接口
export function orgSave (data) {
  return post('org', data)
}

export function orgDeleted (ids) {
  return del('org', { ids: ids })
}

export function orgTree (name) {
  return get('org/find_tree', { name: name })
}

// 人员接口
export function memberPage (data) {
  return get('org_member', data)
}

export function memberSave (data) {
  return post('org_member', data)
}

export function memberDeleted (ids) {
  return del('org_member', { ids: ids })
}

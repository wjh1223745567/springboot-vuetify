
import { snakeCase } from 'lodash'
// 转化为服务器分页参数
export function toServerPagePram (pageParam, condition) {
  const { page, itemsPerPage, sortBy, sortDesc } = pageParam
  const serverParam = {
    current: page,
    size: itemsPerPage,
    orders: [],
    ...condition,
  }

  sortBy.forEach((item, index) => {
    const order = {
      column: snakeCase(item),
      asc: !sortDesc[index],
    }
    serverParam.orders.push(order)
  })

  if (sortBy.length === 0) {
    serverParam.orders = [{ column: snakeCase('createTime'), asc: false }]
  }

  return serverParam
}

export default toServerPagePram

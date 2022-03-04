export function validPer (val) {
  const { authorizes } = JSON.parse(localStorage.getItem('user/userInfo'))
  if (!authorizes) {
    return false
  }
  if (authorizes.includes('ADMIN')) {
    return true
  }

  if (!val) {
    return true
  }

  const authres = authorizes.some(item => val.includes(item))
  console.log(authres, authorizes, val, '权限信息')

  return authres
}

export function validItems (val) {
  if (val && val.items) {
    for (const iterator of val.items) {
      const res = validItems(iterator)
      if (res !== undefined) {
        return res
      }
    }
  } else {
    return validPer(val.auth)
  }
}

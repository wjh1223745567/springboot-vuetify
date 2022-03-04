export function findPoint (data, id) {
  if (data && data.length && id) {
    for (const val of data) {
      if (val.id === id) {
        return val
      } else {
        const res = findPoint(val.children, id)
        if (res) return res
      }
    }
  }
}

export function treeDataTranslate(data, id = 'id', pid = 'parentId') {
  const map = {}
  const result = []
  data.forEach(item => {
    map[item[id]] = { ...item, children: [] }
  })
  data.forEach(item => {
    const parent = map[item[pid]]
    if (parent && item[pid] !== 0) {
      parent.children.push(map[item[id]])
    } else {
      result.push(map[item[id]])
    }
  })
  return result
}

export function filterAsyncRoutes(routes, menuIds) {
  return routes.filter(route => menuIds.includes(route.id))
}

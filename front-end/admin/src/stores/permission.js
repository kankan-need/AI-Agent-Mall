import { defineStore } from 'pinia'
import { menuList, listMenuIds } from '@/api/menu'
import { constantRoutes } from '@/router'
import { filterAsyncRoutes, treeDataTranslate } from '@/utils'

const modules = import.meta.glob('../views/**/*.vue')

function loadView(component) {
  if (component === 'Layout') {
    return () => import('@/layout/index.vue')
  }
  const path = `../views/${component}.vue`
  return modules[path]
}

export const usePermissionStore = defineStore('permission', {
  state: () => ({
    routes: [],
    addRoutes: []
  }),
  actions: {
    async generateRoutes(isAdmin) {
      let menus = await menuList()
      if (!isAdmin) {
        const menuIds = await listMenuIds()
        menus = filterAsyncRoutes(menus, menuIds)
      }
      const asyncRoutes = menus.map(item => ({
        ...item,
        component: loadView(item.component)
      }))
      const treeRoutes = treeDataTranslate(asyncRoutes)
      this.addRoutes = treeRoutes
      this.routes = constantRoutes.concat(treeRoutes)
      return treeRoutes
    }
  }
})

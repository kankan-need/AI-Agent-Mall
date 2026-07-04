import { defineStore } from 'pinia'
import { menuList, listMenuIds } from '@/api/menu'
import { constantRoutes } from '@/router'
import { filterAsyncRoutes, treeDataTranslate } from '@/utils'

const modules = import.meta.glob('../views/**/*.vue')

function loadView(component, parentId) {
  if (component === 'Layout') {
    if (!parentId || parentId === 0) {
      return () => import('@/layout/index.vue')
    }
    return () => import('@/layout/ParentView.vue')
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
        component: loadView(item.component, item.parentId)
      }))
      // 修正商品管理 redirect：数据库里是 /product/spu/index，实际路由是 /product/spu
      const productMenu = asyncRoutes.find(m => m.redirect === '/product/spu/index')
      if (productMenu) {
        productMenu.redirect = '/product/spu'
      }
      const treeRoutes = treeDataTranslate(asyncRoutes)
      this.addRoutes = treeRoutes
      this.routes = constantRoutes.concat(treeRoutes)
      return treeRoutes
    }
  }
})

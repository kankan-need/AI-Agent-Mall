import router from './router'
import { getToken } from '@/utils/auth'
import { useUserStore } from '@/stores/user'
import { usePermissionStore } from '@/stores/permission'

const whiteList = ['/login']

router.beforeEach(async (to, from, next) => {
  const token = getToken()
  const userStore = useUserStore()
  const permissionStore = usePermissionStore()

  if (token) {
    if (to.path === '/login') {
      next({ path: '/' })
      return
    }
    if (userStore.roles && userStore.roles.length > 0) {
      next()
      return
    }
    try {
      const userInfo = await userStore.getUserInfo()
      const accessRoutes = await permissionStore.generateRoutes(userInfo.isAdmin === 1)
      accessRoutes.forEach(route => router.addRoute(route))
      next({ ...to, replace: true })
    } catch (e) {
      await userStore.resetState()
      next(`/login?redirect=${to.fullPath}`)
    }
    return
  }

  if (whiteList.includes(to.path)) {
    next()
  } else {
    next(`/login?redirect=${to.fullPath}`)
  }
})

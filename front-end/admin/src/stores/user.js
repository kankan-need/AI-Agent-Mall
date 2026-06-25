import { defineStore } from 'pinia'
import { login as loginApi, logout as logoutApi } from '@/api/auth'
import { getUserInfo } from '@/api/user'
import { setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    name: '',
    avatar: '',
    isAdmin: 0,
    roles: []
  }),
  actions: {
    async login(loginForm) {
      const data = await loginApi({
        principal: loginForm.username,
        credentials: loginForm.password,
        sysType: 2
      })
      this.token = data.accessToken
      setToken(data.accessToken)
    },
    async getUserInfo() {
      const data = await getUserInfo()
      this.name = data.name
      this.avatar = data.avatar
      this.isAdmin = data.isAdmin
      this.roles = data.isAdmin === 1 ? ['admin'] : []
      return data
    },
    async logout() {
      await logoutApi()
      this.resetState()
    },
    resetState() {
      this.token = ''
      this.name = ''
      this.avatar = ''
      this.isAdmin = 0
      this.roles = []
      removeToken()
    }
  }
})

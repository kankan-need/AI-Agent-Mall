import axios from 'axios'
import { getToken, removeToken } from './auth'

const service = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  timeout: 15000
})

service.interceptors.request.use(config => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = token
  }
  return config
})

service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === '00000') {
      return res.data
    }
    if (res.code === 'A00004') {
      removeToken()
      return Promise.reject(new Error('请先登录'))
    }
    return Promise.reject(new Error(res.msg || '请求失败'))
  },
  error => Promise.reject(error)
)

export default service

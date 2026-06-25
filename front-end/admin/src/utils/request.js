import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
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
      ElMessageBox.confirm('登录已失效，请重新登录', '提示', {
        confirmButtonText: '重新登录',
        showCancelButton: false,
        type: 'warning'
      }).then(() => {
        removeToken()
        location.reload()
      })
      return Promise.reject(new Error(res.msg || 'Unauthorized'))
    }
    ElMessage.error(res.msg || '请求失败')
    return Promise.reject(new Error(res.msg || 'Error'))
  },
  error => {
    ElMessage.error(error.message || '网络异常')
    return Promise.reject(error)
  }
)

export default service

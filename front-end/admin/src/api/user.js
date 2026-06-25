import request from '@/utils/request'

export function getUserInfo() {
  return request({
    url: '/learn-rbac/admin/user/info',
    method: 'get'
  })
}

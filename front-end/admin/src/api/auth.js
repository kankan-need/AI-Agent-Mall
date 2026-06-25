import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/learn-auth/ua/login',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/learn-auth/login_out',
    method: 'post'
  })
}

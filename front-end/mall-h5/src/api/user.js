import request from '@/utils/request'

export function register(data) {
  return request({
    url: '/learn-user/ua/user/register',
    method: 'post',
    data
  })
}

export function getSimpleInfo() {
  return request({
    url: '/learn-user/a/user/simple_info',
    method: 'get'
  })
}

export function getUserDetail() {
  return request({
    url: '/learn-user/a/user/ma/user_detail_info',
    method: 'get'
  })
}

export function updateUser(data) {
  return request({
    url: '/learn-user/a/user/ma/update_user',
    method: 'post',
    data
  })
}

export function listAddress() {
  return request({
    url: '/learn-user/a/user_addr/list',
    method: 'get'
  })
}

export function getAddress(addrId) {
  return request({
    url: '/learn-user/a/user_addr',
    method: 'get',
    params: { addrId }
  })
}

export function saveAddress(data) {
  return request({
    url: '/learn-user/a/user_addr',
    method: 'post',
    data
  })
}

export function updateAddress(data) {
  return request({
    url: '/learn-user/a/user_addr',
    method: 'put',
    data
  })
}

export function deleteAddress(addrId) {
  return request({
    url: '/learn-user/a/user_addr',
    method: 'delete',
    params: { addrId }
  })
}

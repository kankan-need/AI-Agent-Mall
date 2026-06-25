import request from '@/utils/request'

export function menuList() {
  return request({
    url: '/learn-rbac/menu/route',
    method: 'get'
  })
}

export function listMenuIds() {
  return request({
    url: '/learn-rbac/menu/list_menu_ids',
    method: 'get'
  })
}

import request from '@/utils/request'

export function pageOrder(params) {
  return request({
    url: '/learn-order/admin/order/page',
    method: 'get',
    params
  })
}

export function getOrder(orderId) {
  return request({
    url: `/learn-order/admin/order/${orderId}`,
    method: 'get'
  })
}

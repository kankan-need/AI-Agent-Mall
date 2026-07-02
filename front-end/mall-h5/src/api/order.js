import request from '@/utils/request'

export function submitOrder(data) {
  return request({
    url: '/learn-order/a/order/submit',
    method: 'post',
    data
  })
}

export function listOrders(params) {
  return request({
    url: '/learn-order/a/order/list',
    method: 'get',
    params
  })
}

export function getOrderDetail(orderId) {
  return request({
    url: '/learn-order/a/order/detail',
    method: 'get',
    params: { orderId }
  })
}

export function getPayInfo(orderId) {
  return request({
    url: '/learn-order/a/order/pay_info',
    method: 'get',
    params: { orderId }
  })
}

export function payOrder(orderId) {
  return request({
    url: '/learn-order/a/order/pay',
    method: 'post',
    params: { orderId }
  })
}

export function cancelOrder(orderId) {
  return request({
    url: `/learn-order/a/order/cancel/${orderId}`,
    method: 'put'
  })
}

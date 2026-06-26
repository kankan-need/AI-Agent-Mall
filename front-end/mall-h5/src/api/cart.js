import request from '@/utils/request'

export function getCartInfo() {
  return request({
    url: '/learn-product/a/shop_cart/info',
    method: 'get'
  })
}

export function getCartCount() {
  return request({
    url: '/learn-product/a/shop_cart/prod_count',
    method: 'get'
  })
}

export function changeCartItem(data) {
  return request({
    url: '/learn-product/a/shop_cart/change_item',
    method: 'post',
    data
  })
}

export function deleteCartItems(ids) {
  return request({
    url: '/learn-product/a/shop_cart/delete_item',
    method: 'delete',
    data: ids
  })
}

export function checkCartItems(data) {
  return request({
    url: '/learn-product/a/shop_cart/check_items',
    method: 'post',
    data
  })
}

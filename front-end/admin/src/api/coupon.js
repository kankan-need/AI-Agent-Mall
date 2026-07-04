import request from '@/utils/request'

export function listCoupons() {
  return request({
    url: '/learn-product/admin/coupon/list',
    method: 'get'
  })
}

export function getCoupon(couponId) {
  return request({
    url: '/learn-product/admin/coupon',
    method: 'get',
    params: { couponId }
  })
}

export function saveCoupon(data) {
  return request({
    url: '/learn-product/admin/coupon',
    method: 'post',
    data
  })
}

export function updateCoupon(data) {
  return request({
    url: '/learn-product/admin/coupon',
    method: 'put',
    data
  })
}

export function deleteCoupon(couponId) {
  return request({
    url: '/learn-product/admin/coupon',
    method: 'delete',
    params: { couponId }
  })
}

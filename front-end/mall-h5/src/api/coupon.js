import request from '@/utils/request'

export function fetchCoupons() {
  return request({
    url: '/learn-product/ua/coupon/list',
    method: 'get'
  })
}

export function claimCouponApi(couponId) {
  return request({
    url: '/learn-product/a/coupon/claim',
    method: 'post',
    params: { couponId }
  })
}

export function fetchMyCoupons() {
  return request({
    url: '/learn-product/a/coupon/my',
    method: 'get'
  })
}

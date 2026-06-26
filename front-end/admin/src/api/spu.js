import request from '@/utils/request'

export function pageSpu(params) {
  return request({
    url: '/learn-product/admin/spu/page',
    method: 'get',
    params
  })
}

export function getSpu(spuId) {
  return request({
    url: '/learn-product/admin/spu',
    method: 'get',
    params: { spuId }
  })
}

export function saveSpu(data) {
  return request({
    url: '/learn-product/admin/spu',
    method: 'post',
    data
  })
}

export function updateSpu(data) {
  return request({
    url: '/learn-product/admin/spu',
    method: 'put',
    data
  })
}

export function updateSpuStatus(spuId, status) {
  return request({
    url: '/learn-product/admin/spu/prod_status',
    method: 'put',
    params: { spuId, status }
  })
}

export function deleteSpu(spuId) {
  return request({
    url: '/learn-product/admin/spu',
    method: 'delete',
    params: { spuId }
  })
}

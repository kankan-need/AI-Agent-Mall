import request from '@/utils/request'

export function listCategories(parentId = 0) {
  return request({
    url: '/learn-product/ua/category/category_list',
    method: 'get',
    params: { parentId, shopId: 1 }
  })
}

export function pageSpu(params) {
  return request({
    url: '/learn-product/ua/spu/page',
    method: 'get',
    params
  })
}

export function getSpuDetail(spuId) {
  return request({
    url: '/learn-product/ua/spu/prod_info',
    method: 'get',
    params: { spuId }
  })
}

import request from '@/utils/request'

export function listCategories() {
  return request({
    url: '/learn-product/admin/category/shop_categories',
    method: 'get'
  })
}

export function listCategoryByParent(parentId) {
  return request({
    url: '/learn-product/admin/category/get_list_by_parent_id',
    method: 'get',
    params: { parentId }
  })
}

export function saveCategory(data) {
  return request({
    url: '/learn-product/admin/category',
    method: 'post',
    data
  })
}

export function updateCategory(data) {
  return request({
    url: '/learn-product/admin/category',
    method: 'put',
    data
  })
}

export function deleteCategory(categoryId) {
  return request({
    url: '/learn-product/admin/category',
    method: 'delete',
    params: { categoryId }
  })
}

import request from '@/utils/request-to-server'

export function getArticlePagedList(params) {
  return request({
    url: `/posts/queryPageable?_=${Math.random()}`,
    method: 'get',
    params
  })
}

export function getArticleById(params) {
  return request({
    url: `/posts/getById?_=${Math.random()}`,
    method: 'get',
    params
  })
}

export function deleteArticle(params) {
  return request({
    url: `/posts/delete?_=${Math.random()}`,
    method: 'get',
    params
  })
}

export function createArticle(data) {
  return request({
    url: '/posts/insert',
    method: 'post',
    data
  })
}

export function updateArticle(data) {
  return request({
    url: '/posts/update',
    method: 'post',
    data
  })
}

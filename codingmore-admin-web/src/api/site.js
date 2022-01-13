import request from '@/utils/request-to-server'

export function getSiteList(params) {
  // console.log('调用getSiteList方法请求后台数据，请求携带数据：', data)
  return request({
    url: `/site/queryPageable?_=${Math.random()}`,
    method: 'get',
    params
  })
}

export function getOneSite(id) {
  return request({
    url: `/site/getById`,
    method: 'get',
    params: {
      id
    }
  })
}

export function updateSite(data) {
  return request({
    url: '/site/update',
    method: 'post',
    data
  })
}

export function addSite(data) {
  return request({
    url: '/site/insert',
    method: 'post',
    data
  })
}

export function deleteSite(id) {
  return request({
    url: `/site/delete?_=${Math.random()}`,
    method: 'get',
    params: {
      siteId: id
    }
  })
}


import request from '../utils/request'

// 获得站点配置信息
export function getSiteConfigInfo() {
  return request({
    url: `/site/getById?_=${Math.random()}`,
    method: 'get'
  })
}

// 更新站点配置信息
export function updateSiteConfig(data) {
  return request({
    url: `/site/update`,
    method: 'post',
    data
  })
}

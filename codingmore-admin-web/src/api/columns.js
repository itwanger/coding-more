import request from '../utils/request'

export function getAllColumns(params) {
  return request({
    url: `/termTaxonomy/getPyParentId`,
    method: 'get',
    params
  })
}

export function getOneColumn(params) {
  return request({
    url: `/termTaxonomy/getById`,
    method: 'get',
    params
  })
}

export function updateColumn(data) {
  return request({
    url: '/termTaxonomy/update',
    method: 'post',
    data
  })
}

export function addColumn(data) {
  return request({
    url: '/termTaxonomy/insert',
    method: 'post',
    data
  })
}

export function deleteColumn(id) {
  return request({
    url: `/termTaxonomy/delete?_=${Math.random()}`,
    method: 'get',
    params: {
      termTaxonomyId: id
    }
  })
}

import request from '../utils/request'

// 获取当前用户登录信息
export function GetLoginUserInfo() {
  return request({
    url: '/users/info',
    method: 'get'
  })
}

// 用户退出登陆方法
export function UserLogout() {
  return request({
    url: '/users/logout',
    method: 'post'
  })
}

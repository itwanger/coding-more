import request from '../utils/request'

// 用户提交登陆请求
export function UserLogin(data) {
  return request({
    url: '/users/login',
    method: 'post',
    data
  })
}

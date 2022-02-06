import Cookies from 'js-cookie'
import { createUuid } from './common'

// 随机存储cookie的token key
const TokenKey = createUuid()

export function getToken () {
  return Cookies.get(TokenKey)
}

export function setToken (token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken () {
  return Cookies.remove(TokenKey)
}

import axios from 'axios'
import { Loading, MessageBox, Message } from 'element-ui'
import { getToken, removeToken } from '@/utils/auth'

// #region 处理ajax效果
// 标记页面加载对象是否存在
let loadingService = null
// 当前请求数量
let ajaxCount = 0
// 检查当前是否所有ajax都结束方法
let checkAllAjaxDone = () => {
  if (ajaxCount === 0) {
    // console.log('所有ajax结束。。。。', loadingService);
    if (loadingService) {
      loadingService.close()
      loadingService = null
    }
  }
}
// #endregion

// 创建请求对象
const httpRequest = axios.create({
  // 统一的ajax请求前缀
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // 请求超时时间
})

// 请求封装
httpRequest.interceptors.request.use(
  config => {
    // do something before request is sent
    // console.log('request config data:', config.data)
    if (getToken()) {
      // 设置请求头token
      config.headers['Authorization'] = getToken()
    }
    // #region 处理ajax效果
    // 如果loading对象不存在，当前又要发起请求，则创建loading对象，从而对所有ajax请求添加统一的请求等待效果
    if (!loadingService) {
      loadingService = Loading.service({
        fullscreen: true,
        lock: true,
        text: '加载中，请稍后...'
      })
    }
    // ajax数量+1
    ajaxCount++
    // #endregion
    return config
  },
  error => {
    // do something with request error
    console.log(`发送请求失败${error}`) // for debug
    return Promise.reject(error)
  }
)

// 响应处理封装
httpRequest.interceptors.response.use(
  response => {
    console.log('服务器返回最外层响应：', response)
    const {
      code,
      data,
      message
    } = response.data
    if ( // 请求正常的情况
      (code === 0 && response.status === 200) ||
      (response.data instanceof Blob && response.status === 200)
    ) {
      // #region 处理ajax效果
      ajaxCount--
      checkAllAjaxDone()
      // #endregion
      if (response.data instanceof Blob) {
        return response.data
      } else {
        return data
      }
    } else {
      Message({
        message: message,
        type: 'error',
        duration: 3 * 1000,
        showClose: true
      })
      if (code === 401) { // 无效 token
        removeToken() // 删除本地缓存
        // 跳转到登录页面
        window.location.href = '/login'
      }
      // #region 处理ajax效果
      ajaxCount--
      checkAllAjaxDone()
      // #endregion
      return Promise.reject(message)
    }
  },
  error => {
    // #region 处理ajax效果
    ajaxCount--
    checkAllAjaxDone()
    // #endregion
    console.log('服务器返回异常信息：', error, error.response)
    if (error.response && error.response.data) {
      if (error.response.data.message) {
        MessageBox({ type: 'error', message: error.response.data.message, title: '系统提示' })
      }
    }
    if (error.response.status === 401) {
      removeToken()
      window.location.href = '/login'
    }
    return Promise.reject(error.response)
  }
)

export default httpRequest

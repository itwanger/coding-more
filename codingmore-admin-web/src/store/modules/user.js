import {
  login,
  logout,
  getInfo
} from '@/api/user'
import {
  getToken,
  setToken,
  removeToken
} from '@/utils/auth'
import router, {
  resetRouter
} from '@/router'
import qs from 'qs'
import {
  getSiteList
} from '@/api/site'

const state = {
  token: getToken(),
  name: '',
  loginName: '',
  avatar: '',
  introduction: '',
  roles: [],
  siteManagement: {
    currentSiteId: -1,
    currentSite: null,
    siteList: []
  },
  userInfo: {}
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_LOGIN_NAME: (state, loginName) => {
    state.loginName = loginName
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_SITE_MANAGEMENT: (state, managementObj) => {
    state.siteManagement = managementObj
  },
  SET_CURRENT_SITE: (state, siteId) => {
    state.siteManagement.currentSiteId = siteId
    const theSite = state.siteManagement.siteList.filter(x => x.siteId === siteId)
    state.siteManagement.currentSite = theSite
  },
  SET_USER: (state, userModel) => {
    state.userInfo = userModel
  }
}

const actions = {
  // 从views/login/index.vue的handleLogin方法调用过来
  login({
    commit
  }, userInfo) {
    const {
      username,
      password
    } = userInfo // 知识点：ES6解构赋值，可参考网址：https://www.runoob.com/w3cnote/deconstruction-assignment.html
    return new Promise((resolve, reject) => {
      let requestData = {
        userLogin: username.trim(),
        userPass: password
      }
      requestData = qs.stringify(requestData)
      login(requestData).then(response => {
        const data = response.result
        const resToken = `${data.tokenHead}${data.token}`
        commit('SET_LOGIN_NAME', requestData.userLogin)
        commit('SET_TOKEN', resToken) // 设置vuex存储token
        setToken(resToken) // 设置cookie存储
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({
    commit,
    state
  }) {
    return new Promise((resolve, reject) => {
      const requestData = {
        name: state.loginName
      }
      getInfo(requestData).then(response => {
        const data = {}

        if (!data) {
          reject('Verification failed, please Login again.')
        }

        // TODO: 目前接口只返回了用户名，所以采用假数据，之后接口完善之后返回其他信息
        data.name = response.result.username
        data.roles = ['admin']
        data.avatar = 'https://img2.baidu.com/it/u=504609824,3604971623&fm=26&fmt=auto'
        data.introduction = 'introduction'

        const {
          roles,
          name,
          avatar,
          introduction
        } = data

        // roles must be a non-empty array
        if (!roles || roles.length <= 0) {
          reject('getInfo: 登陆账号不具备任何角色，所以自动退出!')
        }

        commit('SET_ROLES', roles)
        commit('SET_NAME', name)
        commit('SET_AVATAR', avatar)
        commit('SET_INTRODUCTION', introduction)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // TODO: 应该在查询当前登陆用户信息返回的，等接口完善之后再去掉这个调用
  getUserChargingSites({ commit }) {
    return new Promise((resolve, reject) => {
      getSiteList({
        page: 1,
        pageSize: 1000
      }).then(response => {
        if (!response.result) {
          reject(new Error('初始化可管理站点失败，退出登陆'))
        }
        if (response.result.items === 0) {
          reject(new Error('该用户没有任何站点管理权限，退出登陆'))
        }

        // eslint-disable-next-line prefer-const
        let siteManagmentValue = {
          currentSiteId: response.result.items[0].siteId,
          currentSite: response.result.items[0],
          siteList: response.result.items
        }
        commit('SET_SITE_MANAGEMENT', siteManagmentValue)
        resolve()
      })
    })
  },

  // user logout
  logout({
    commit,
    state,
    dispatch
  }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        removeToken()
        resetRouter()

        // reset visited views and cached views
        // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        dispatch('tagsView/delAllViews', null, {
          root: true
        })

        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({
    commit
  }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // dynamically modify permissions 根据角色，动态改变权限方法
  async changeRoles({
    commit,
    dispatch
  }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    setToken(token)

    const {
      roles
    } = await dispatch('getInfo')

    resetRouter()

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, {
      root: true
    })
    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, {
      root: true
    })
  },

  // 初始化用户可以管理的站点信息和当前站点信息
  initManagementInfo({
    commit
  }, managementInfo) {
    commit('SET_SITE_MANAGEMENT', managementInfo)
  },

  // 切换站点方法
  changeSite({
    commit
  }, siteId) {
    commit('SET_CURRENT_SITE', siteId)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

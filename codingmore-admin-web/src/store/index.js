import Vue from 'vue'
import Vuex from 'vuex'
import {
  GetLoginUserInfo
} from '../api/users'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // 定义存储当前登陆用户信息的变量
    userInfo: null
  },
  mutations: {
    // 设置用户信息的方法
    SET_USER_INFO(state, data) {
      state.userInfo = data
    }
  },
  actions: {
    // 调用后端服务接口，获取当前用户信息并存入vuex
    refleshUserInfo({commit}) {
      GetLoginUserInfo().then(res => {
        console.log('获取登录用户信息成功', res)
        commit('SET_USER_INFO', res)
      })
    },

    // 用户退出登陆时候，移除用户信息方法
    removeUserInfo({commit}) {
      commit('SET_USER_INFO', null)
    }
  }
})

import Vue from 'vue'
import Router from 'vue-router'
import mainFrame from '@/layout'
import articles from '../views/content-management/article-management'
import columns from '../views/content-management/column-management'
import users from '../views/system-management/users-management'
import power from '../views/system-management/power-management'
import log from '../views/system-management/log-management'
import page404 from '../views/error-pages/404'
import page500 from '../views/error-pages/500'
import pageLogin from '../views/login'

Vue.use(Router)

// 定义路由数组，暂时前端写死，之后加入权限管理之后，读取权限之后要进行过滤
const pageRouters = [
  {
    path: '/content',
    name: 'content-management',
    component: mainFrame,
    children: [
      {
        path: 'articles',
        name: 'article-management',
        component: articles
      },
      {
        path: 'columns',
        name: 'columns-management',
        component: columns
      }
    ]
  },
  {
    path: '/system',
    name: 'system-management',
    component: mainFrame,
    children: [
      {
        path: 'users',
        name: 'users-management',
        component: users
      },
      {
        path: 'power',
        name: 'power-management',
        component: power
      },
      {
        path: 'log',
        name: 'log-management',
        component: log
      }
    ]
  },
  {
    path: '/404',
    name: 'error-page404',
    component: page404
  },
  {
    path: '/500',
    name: 'error-page500',
    component: page500
  },
  {
    path: '/login',
    name: 'login',
    component: pageLogin
  }
]

export default new Router({
  routes: pageRouters
})

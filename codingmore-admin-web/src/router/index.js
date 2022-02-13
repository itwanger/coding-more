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
import articleEdit from '../views/content-management/article-editing'

Vue.use(Router)

// 定义路由数组，暂时前端写死，之后加入权限管理之后，读取权限之后要进行过滤

// 要展示在导航栏上的菜单数组
export const pageRouters = [
  {
    path: '/content',
    name: 'content-management',
    component: mainFrame,
    icon: 'el-icon-edit-outline',
    meta: {
      title: '内容管理'
    },
    children: [
      {
        path: 'articles',
        name: 'article-management',
        component: articles,
        meta: {
          title: '文章管理'
        }
      },
      {
        path: 'columns',
        name: 'columns-management',
        component: columns,
        meta: {
          title: '栏目管理'
        }
      }
    ]
  },
  {
    path: '/system',
    name: 'system-management',
    component: mainFrame,
    icon: 'el-icon-s-operation',
    meta: {
      title: '系统管理'
    },
    children: [
      {
        path: 'users',
        name: 'users-management',
        component: users,
        meta: {
          title: '用户管理'
        }
      },
      {
        path: 'power',
        name: 'power-management',
        component: power,
        meta: {
          title: '权限管理'
        }
      },
      {
        path: 'log',
        name: 'log-management',
        component: log,
        meta: {
          title: '日志管理'
        }
      }
    ]
  }
]

// 不展示在菜单栏上，但是系统要使用到的数组
const systemRouters = [
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
  },
  {
    path: '/content/article-editing',
    name: 'article-editing',
    component: articleEdit
  },
  {
    path: '/',
    name: 'root',
    redirect: '/content/articles'
  },
  {
    path: '*',
    name: 'root',
    redirect: '/'
  }
]

export default new Router({
  routes: [...pageRouters, ...systemRouters]
})

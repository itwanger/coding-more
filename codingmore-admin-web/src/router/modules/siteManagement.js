import Layout from '@/layout'

const siteManagementRouters = [
  {
    path: '/site/columns',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/columns/index'),
        name: 'columns',
        meta: { title: '栏目管理', icon: 'guide', noCache: true }
      }
    ]
  },
  {
    path: '/site/articles',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/articles/index'),
        name: 'articles',
        meta: { title: '文章管理', icon: 'guide', noCache: true }
      }
    ]
  }
]

export default siteManagementRouters

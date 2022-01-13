/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const systemSettingsRouter = {
  path: '/system',
  component: Layout,
  redirect: '/system/sites-management',
  name: 'System',
  meta: {
    title: '系统管理',
    icon: 'guide'
  },
  children: [
    {
      path: 'sites-management',
      component: () => import('@/views/system/sites.vue'),
      name: 'SitesManagement',
      meta: { title: '站点管理' }
    },
    {
      path: 'users-management',
      component: () => import('@/views/system/users.vue'),
      name: 'SitesManagement',
      meta: { title: '用户管理' }
    }
  ]
}
export default systemSettingsRouter

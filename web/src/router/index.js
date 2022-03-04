// Imports
import Vue from 'vue'
import Router from 'vue-router'
import { trailingSlash } from '@/util/helpers'
import { getToken } from '@/util/auth'
import {
  layout,
  route,
  nolayout,
} from '@/util/routes'

import { whitelist } from '@/util/pagewhitelist'

Vue.use(Router)
const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  scrollBehavior: (to, from, savedPosition) => {
    if (to.hash) return { selector: to.hash }
    if (savedPosition) return savedPosition

    return { x: 0, y: 0 }
  },
  routes: [
    layout('Default', [
      route('Dashboard'),

      // Pages
      route('UserProfile', null, 'components/profile'),

      // Components
      route('Notifications', null, 'components/notifications'),
      route('Icons', null, 'components/icons'),
      route('Typography', null, 'components/typography'),

      // Tables
      route('Regular Tables', null, 'tables/regular'),

      // Maps
      route('Google Maps', null, 'maps/google'),

      route('orgemployee', null, 'orgemployee', '人员管理'),

      route('system/role', null, 'system/role', '角色管理'),
    ]),
    // 登录页面无layout
    nolayout('loginpage/login', '/login'),
  ],
})

router.beforeEach((to, from, next) => {
  if (getToken() || whitelist.includes(to.path)) {
    return to.path.endsWith('/') ? next() : next(trailingSlash(to.path))
  } else {
    next(trailingSlash('/login'))
  }
})

export default router

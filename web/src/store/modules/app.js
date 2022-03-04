// Pathify
import { make } from 'vuex-pathify'

// Data
const state = {
  drawer: null,
  drawerImage: true,
  mini: false,
  items: [
    {
      title: '首页',
      icon: 'mdi-view-dashboard',
      to: '/',
    },
    {
      title: '人员管理',
      icon: 'mdi-account',
      to: '/orgemployee/',
      auth: ['ORG_MEMBER_ALL'],
    },
    {
      title: '系统设置',
      icon: 'mdi-apps',
      items: [
        {
          title: '角色管理',
          icon: ' ',
          to: '/system/role/',
          auth: ['SYS_ROLE_ALL'],
        },
      ],
    },

    {
      title: 'Regular Tables',
      icon: 'mdi-clipboard-outline',
      to: '/tables/regular/',
    },
    {
      title: 'Typography',
      icon: 'mdi-format-font',
      to: '/components/typography/',
    },
    {
      title: 'Icons',
      icon: 'mdi-chart-bubble',
      to: '/components/icons/',
    },
    {
      title: 'Google Maps',
      icon: 'mdi-map-marker',
      to: '/maps/google/',
    },
    {
      title: 'Notifications',
      icon: 'mdi-bell',
      to: '/components/notifications/',
    },
  ],
}

const mutations = make.mutations(state)

const actions = {
  ...make.actions(state),
  init: async ({ dispatch }) => {
    //
  },
}

const getters = {}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters,
}

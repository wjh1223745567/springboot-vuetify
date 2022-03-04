// Pathify
import { make } from 'vuex-pathify'

const state = {
  show: false,
  type: 'info',
  text: '',
}

const mutations = make.mutations(state)

const actions = {}

const getters = {}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters,
}

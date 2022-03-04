// Pathify
import { make } from 'vuex-pathify'

const state = {
  reload: false,
  treedata: [],
  chooseorg: {},
  choosefirst: true,
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

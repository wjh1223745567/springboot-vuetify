import Vue from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import './plugins'
import store from './store'
import { sync } from 'vuex-router-sync'
import * as icons from '@mdi/js'
import '@mdi/font/css/materialdesignicons.css'
import './util/message'
import toServerPagePram from './util/pageparam'

Vue.config.productionTip = false
Vue.prototype.$icon = icons
Vue.prototype.$pageParam = toServerPagePram

sync(store, router)

Vue.use(vuetify)

new Vue({
  router,
  vuetify,
  store,
  render: h => h(App),
}).$mount('#app')

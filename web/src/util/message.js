import vue from 'vue'
import store from '../store'

function basetext (text) {
  store.set('message/text', text)
  store.set('message/show', true)
}

function warning (text) {
  store.set('message/type', 'warning')
  basetext(text)
}

function error (text) {
  store.set('message/type', 'error')
  basetext(text)
}

function success (text) {
  store.set('message/type', 'success')
  basetext(text)
}

const message = {
  warning, error, success,
}

vue.prototype.$message = message

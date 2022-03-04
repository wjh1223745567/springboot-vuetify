import router from '@/router'
import { getToken, removeToken } from '@/util/auth'
import axios from 'axios'
import { decrypt } from '@/util/aescrypt'
import qs from 'qs'
// 请求前缀
const baseUrl = '/vuetify-api'

// create an axios instance
const service = axios.create({
  baseURL: baseUrl, // url = base url + request url
  withCredentials: true, // send cookies when cross-domain requests
  timeout: 30000, // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent

    if (getToken()) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      // config.headers['X-Token'] = getToken()
      config.headers['X-Authorization'] = getToken()
      // config.headers.Authorization = getToken();
    } else {
      config.headers.Authorization = 'Basic dnVlOnZ1ZQ==' // 增加客户端认证
    }

    // config.data = encrypt(config.data)

    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(JSON.parse(decrypt(error)))
  },
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    if (typeof response.data !== 'string') {
      return response.data
    } else {
      return JSON.parse(decrypt(response.data))
    }
  },
  error => {
    let errorData
    try {
      errorData = JSON.parse(decrypt(error))
    } catch (er) {
      errorData = error
    }
    const { response } = errorData
    if (response.status === 401) {
      removeToken()
      router.replace({
        path: '/login',
      })
    } else if (response.status === 403) {
      console.log('无权限访问')
    } else {
      return Promise.reject(error)
    }
  },
)

export default service

// get请求
export function get (url, params = {}) {
  return service({
    url: url,
    method: 'get',
    params,
    paramsSerializer: function (params) {
      return qs.stringify(params, { arrayFormat: 'indices', allowDots: true })
    },
  })
}

export function getFile (url, params = {}) {
  return service.get(url, { params, responseType: 'blob' })
}

// post请求， 第一个data是body数据， params是queryString
export function post (url, data = {}) {
  const formData = changeDataToFormData(data)
  return service.post(url, formData, { headers: { 'Content-type': 'multipart/form-data;charset=UTF-8' } })
}

// 同上
export function put (url, data = {}) {
  const formData = changeDataToFormData(data)
  return service.put(url, formData, { headers: { 'Content-type': 'multipart/form-data;charset=UTF-8' } })
}

// 同上
export function del (url, params = {}) {
  return service.delete(url, { params })
}

// 证件模板
export function cardPost (url, data = {}) {
  const isUndefined = isType('Undefined')
  const isNull = isType('Null')
  const formData = new FormData()
  for (const param in data) {
    if (param === 'elementDtoList') {
      let n = 0
      for (let i = 0; i < data[param].length; i++) {
        const element = data[param][i]
        if (element.elementType === 1 && ((isUndefined(element.file) || isNull(element.file) || element.file === '') && (isUndefined(element.filePath) || isNull(element.filePath) || element.filePath === ''))) {
          continue
        }
        for (const eleParam in element) {
          if (isUndefined(element[eleParam]) || isNull(element[eleParam]) || element[eleParam] === '') {
            continue
          }

          formData.append('elementDtoList[' + n + '].' + eleParam, element[eleParam])
        }
        n++
      }
    } else {
      formData.append(param, data[param])
    }
  }
  return service.post(url, formData, { headers: { 'Content-type': 'multipart/form-data;charset=UTF-8', responseType: 'blob' } })
}

// 格式 formData
export function jsonpost (url, data = {}) {
  return service.post(url, data, { headers: { 'Content-type': 'application/json' } })
}

const isType = function (type) {
  return function (obj) {
    return Object.prototype.toString.call(obj) === `[object ${type}]`
  }
}

/**
 * 修改格式为fromData
 * @param {*} data
 */
export function changeDataToFormData (data) {
  const isArray = isType('Array')
  const isUndefined = isType('Undefined')
  const isNull = isType('Null')
  const formData = new FormData()

  for (const param in data) {
    if (isArray(data[param])) {
      data[param].forEach((item, index) => {
        formData.append(param, item)
      })
    } else if (isUndefined(data[param]) || isNull(data[param])) {
      continue
    } else {
      formData.append(param, data[param])
    }
  }
  return formData
}

export function requests (params) {
  return service.request(params)
}

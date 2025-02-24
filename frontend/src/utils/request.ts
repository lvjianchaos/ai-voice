// src/utils/request
import axios, { type AxiosRequestHeaders } from 'axios'
import { useTokenStore } from '@/stores/mytoken'
import { refreshToken } from '@/api/users'
import router from '@/router/index'
const request = axios.create({
  // baseURL: import.meta.env.VITE_API_URL,
  timeout: 50000,
})
// 请求拦截器
request.interceptors.request.use((config) => {
  if (!config.headers) {
    config.headers = {} as AxiosRequestHeaders
  }

  const store = useTokenStore()
  config.headers.Authorization = store.token?.access_token

  return config
})

// 响应拦截器
request.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response.status === 401) {
      // 刷新token
      const { data } = await refreshToken()
      if (data.success) {
        useTokenStore().saveToken(data.content)

        request(error.config)
      } else {
        ElMessage.error('token过期，请重新登录!')
        router.push({ name: 'Login', query: { redirect: router.currentRoute.value.fullPath } })
        return
      }
    }
    return Promise.reject(error)
  },
)
export default request

import request from '@/utils/request'
import { useTokenStore } from '@/stores/mytoken'
// 用户登录-参数类型
type LoginInfo = {
  name: string
  code?: string
  password: string
}

// 公共类型
type CommonReturn<T = string> = {
  success: boolean
  state: number
  message: string
  content: T
}
// 用户登录-返回数据类型
type LoginResult = CommonReturn

// 用户登录请求 {name: 'Chaos', password: '123456'}
/*
  要求请求类型 application/json
*/
export const login = (loginInfo: LoginInfo) => {
  return request<LoginResult>({
    method: 'POST',
    url: '/front/user/login',
    data: loginInfo,
  })
}

// 获取用户信息
type UserInfo = CommonReturn<{
  isUpdatedPassword: boolean
  portrait: string
  userName: string
}>
export const getInfo = () => {
  return request<UserInfo>({
    method: 'GET',
    url: '/front/user/getInfo',
  })
}

// 用户退出
export const logout = () => {
  return request({
    method: 'POST',
    url: '/front/user/logout',
  })
}

// 刷新token
type RToken = CommonReturn
let promiseRT: Promise<any>
let isRefreshing = false
export const refreshToken = () => {
  if (isRefreshing) {
    return promiseRT
  }
  isRefreshing = true
  promiseRT = request<RToken>({
    method: 'POST',
    url: '/front/user/refresh_token',
    params: {
      refreshtoken: useTokenStore().token?.refresh_token,
    },
  }).finally(() => {
    isRefreshing = false
  })
  return promiseRT
}

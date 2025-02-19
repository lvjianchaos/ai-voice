import { defineStore } from 'pinia'

interface Token {
  access_token: string
  token_type: string
  refresh_token: string
  expires_in: number
  user_id: string
}

export const useTokenStore = defineStore('mytoken', () => {
  const tokenJson = ref('')
  const token = computed<Token>(() => {
    try {
      return JSON.parse(tokenJson.value || window.localStorage.getItem('TokenInfo') || '{}')
    } catch (err) {
      ElMessage.error('json格式不对,token解析失败...')
      window.localStorage.setItem('TokenInfo', '')
      throw err
    }
  })

  function saveToken(data: string) {
    tokenJson.value = data
    window.localStorage.setItem('TokenInfo', data)
  }

  return { token, saveToken }
})

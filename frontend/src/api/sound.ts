// src/api/sound.ts

import request from '@/utils/request'

// 定义类型
type Common<T> = {
  success: boolean
  message: string
  time: string
  data: T
}

export type SoundItem = {
  id: number
  name: string
  discription: string
}
// 获取所有声音样本
export const getAll = () => {
  return request<Common<SoundItem[]>>({
    url: '/front/sound/getAllSound',
    method: 'GET',
  })
}

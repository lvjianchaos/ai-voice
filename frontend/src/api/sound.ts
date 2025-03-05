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
  gender: string
  language: string
  discription: string
}
// 获取所有声音样本
export const getAll = () => {
  return request<Common<SoundItem[]>>({
    url: '/front/sound/getAllSound',
    method: 'GET',
  })
}

type SoundParams = {
  id?: number
  name: string
  gender: string
  language: string
  discription: string
}

// 新建或更新样本
export const saveOrUpdate = (SoundInfo: SoundParams) => {
  return request<Common<boolean>>({
    url: '/front/sound/saveOrUpdate',
    method: 'POST',
    data: SoundInfo,
  })
}

// 删除声音样本
export const deleteSample = (id: number) => {
  return request<Common<boolean>>({
    url: `/front/sound/${id}`,
    method: 'DELETE',
    data: { id },
  })
}

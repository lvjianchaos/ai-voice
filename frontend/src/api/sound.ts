// src/api/sound.ts

import request from '@/utils/request'
import { ElMessage } from 'element-plus' // 引入 ElMessage

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
  audioUrl: string // 后端返回的音频 URL
}

export type SoundParams = {
  id?: number
  name: string
  gender: string
  language: string
  discription: string
  audioFile: File | null
}

// 获取所有声音样本
export const getAll = async () => {
  try {
    const response = await request<Common<SoundItem[]>>({
      url: '/front/sound/getAllSound',
      method: 'GET',
    })
    return response // 返回 response，以便在组件中处理结果
  } catch (error: any) {
    ElMessage.error(`获取样本失败: ${error.message}`) // 显示错误消息
    throw error // 抛出错误，以便在组件中处理
  }
}

// 新建或更新样本
export const saveOrUpdate = async (SoundInfo: SoundParams) => {
  const formData = new FormData()

  // 添加文本数据
  formData.append('name', SoundInfo.name)
  formData.append('gender', SoundInfo.gender)
  formData.append('language', SoundInfo.language)
  formData.append('discription', SoundInfo.discription)

  // 添加音频文件 (如果存在)
  if (SoundInfo.audioFile) {
    formData.append('audioFile', SoundInfo.audioFile)
  }

  // 添加 id (如果存在)
  if (SoundInfo.id) {
    formData.append('id', String(SoundInfo.id)) // 转换为字符串
  }

  try {
    const response = await request<Common<boolean>>({
      url: '/front/sound/saveOrUpdate',
      method: 'POST',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data', // 显式设置 Content-Type
      },
    })

    return response // 返回 response，以便在组件中处理结果
  } catch (error: any) {
    ElMessage.error(`保存失败: ${error.message}`) // 显示错误消息
    throw error // 抛出错误，以便在组件中处理
  }
}

// 删除声音样本
export const deleteSample = async (id: number) => {
  try {
    const response = await request<Common<boolean>>({
      url: `/front/sound/${id}`,
      method: 'DELETE',
    })
    return response // 返回 response，以便在组件中处理结果
  } catch (error: any) {
    ElMessage.error(`删除样本失败: ${error.message}`) // 显示错误消息
    throw error // 抛出错误，以便在组件中处理
  }
}

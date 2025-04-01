import axios from 'axios'

export interface VoiceItem {
  id: number
  name: string
  gender: string
  language: string
  discription: string
}

export interface VoiceGenerateParams {
  content: string
  voiceId: number | string
  speed: number
  tone: number
  rhythm: number
}

// 获取所有可用语音
export const getVoices = () => {
  return axios.get('/front/sound/all')
}

// 生成语音
export const generateVoice = (params: VoiceGenerateParams) => {
  return axios.post('/front/sound/voiceguide/generate-voice', params)
}

<script lang="ts" setup>
import { getAll } from '@/api/sound'
import type { SoundItem } from '@/api/sound'
// 存储返回数据
const allSounds = ref([] as SoundItem[])
// 获取所有声音
const getAllSounds = async() => {
  const { data } = await getAll()
  // console.log(data.data)
  if(data.success == true) {
    allSounds.value = data.data
  } else {
    ElMessage.error('获取声音样本库失败...')
    throw new Error('获取声音样本库失败...')
  }
}
getAllSounds()
</script>

<template>
  <h2>声音样本库</h2>
</template>

<style lang="scss" scoped>
</style>

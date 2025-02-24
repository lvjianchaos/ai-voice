<script lang="ts" setup>
import { getAll } from '@/api/sound'
import type { SoundItem } from '@/api/sound'
import { useRouter } from 'vue-router'
const router = useRouter()
// 存储返回数据
const allSounds = ref([ {id:1,name:"Chaos",discription:"用户Chaos的声音"},
                        {id:1,name:"Chaos",discription:"用户Chaos的声音"},
                        {id:1,name:"Chaos",discription:"用户Chaos的声音"},
                        {id:1,name:"Chaos",discription:"用户Chaos的声音"},
                        {id:1,name:"Chaos",discription:"用户Chaos的声音"},
                        {id:1,name:"Chaos",discription:"用户Chaos的声音"},
                        {id:1,name:"Chaos",discription:"用户Chaos的声音"},
                        {id:1,name:"Chaos",discription:"用户Chaos的声音"},
                        {id:1,name:"Chaos",discription:"用户Chaos的声音"},
                        {id:1,name:"Chaos",discription:"用户Chaos的声音"},
                        {id:1,name:"Chaos",discription:"用户Chaos的声音"},
                        {id:1,name:"Chaos",discription:"用户Chaos的声音"},
                        {id:1,name:"Chaos",discription:"用户Chaos的声音"},
                        {id:1,name:"Chaos",discription:"用户Chaos的声音"}
                      ] as SoundItem[])
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
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <el-button type="primary" @click="router.push({ name: 'sample-create' })">添加样本</el-button>
      </div>
    </template>
    <el-table :data="allSounds" border style="width: 100%">
      <el-table-column type="index" label="序号" align="center" width="100"/>
      <el-table-column prop="name" label="样本名称" align="center"/>
      <el-table-column prop="discription" label="样本描述" align="center"/>
      <el-table-column label="操作" align="center">
        <el-button type="primary" @click="router.push({ name: 'sample-edit' })">编辑</el-button>
        <el-button type="danger">删除</el-button>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<style lang="scss" scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  /*width: auto;*/
}
</style>

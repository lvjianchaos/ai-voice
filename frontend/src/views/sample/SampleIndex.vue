<script lang="ts" setup>
import DlgSampleCreateOrEdit from './DlgSampleCreateOrEdit.vue'
import { allSound,getAllSound,handleDelete } from '../../composables/useSounds'
getAllSound()

const dlgCreateOrEdit = ref<InstanceType<typeof DlgSampleCreateOrEdit>>()


</script>

<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <el-button
        type="primary"
        @click="dlgCreateOrEdit?.initAndShow()">新建样本</el-button>
      </div>
    </template>
    <el-table :data="allSound" border style="width: 100%">
      <el-table-column type="index" label="序号" align="center" width="100"/>
      <el-table-column prop="name" label="样本名称" align="center" width="200"/>
      <el-table-column prop="gender" label="性别" align="center" width="100"/>
      <el-table-column prop="language" label="语言" align="center" width="150"/>
      <el-table-column prop="discription" label="样本描述" align="center"/>
      <el-table-column label="操作" align="center" v-slot="{row}">
        <el-button type="primary" @click="dlgCreateOrEdit?.initAndShow(row.id)">编辑</el-button>
        <el-button type="danger" @click="handleDelete(row.id)">删除</el-button>
      </el-table-column>
    </el-table>
    <DlgSampleCreateOrEdit ref="dlgCreateOrEdit"/>
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

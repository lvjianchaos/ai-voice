<script lang="ts" setup>
import { allSound,dialogFormVisible,onSubmit,isCreate,msgText,form } from '@/composables/useSounds'
import type { FormInstance } from 'element-plus'
const formLabelWidth = '140px'



const initAndShow = (id = 0) => {
  fmSound.value?.resetFields()
  dialogFormVisible.value = true
  if(id) {
    isCreate.value = false
    msgText.value = '编辑'
    const sound = allSound.value.find(item => item.id === id)
    Object.assign(form, sound)
  } else {
    isCreate.value = true
    msgText.value = '创建'
  }
}

const fmSound = ref<FormInstance>()


defineExpose({
  initAndShow
})
</script>

<template>
  <el-dialog v-model="dialogFormVisible" :title="msgText+'样本'" width="500">
    <el-form :model="form" ref="fmSound">
      <el-form-item label="样本名称" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="声音性别" :label-width="formLabelWidth" prop="gender">
        <el-input v-model="form.gender" autocomplete="off" />
      </el-form-item>
      <el-form-item label="语言" :label-width="formLabelWidth" prop="language">
        <el-input v-model="form.language" autocomplete="off" />
      </el-form-item>
      <el-form-item label="样本描述" :label-width="formLabelWidth" prop="discription">
        <el-input v-model="form.discription" type="textarea" :autosize="{ minRows: 2, maxRows: 4 }" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false"> 取 消 </el-button>
        <el-button type="primary" @click="onSubmit">
          确 认
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style type="scss" scoped>
</style>

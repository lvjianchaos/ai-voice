<script lang="ts" setup>
import { useSounds } from "@/composables/useSounds"
const { form, onSubmit } = useSounds()


</script>

<template>
  <div class="sound-sample-form">
    <h2 class="form-title">
      <i class="el-icon-music"></i>
      编辑声音样本
    </h2>
    <el-form :model="form" label-position="top" :hide-required-asterisk="true">
      <el-form-item label="样本名称">
        <el-input
          v-model="form.name"
        />
      </el-form-item>

      <el-form-item label="样本描述">
        <el-input
          v-model="form.discription"
          type="textarea"
          :rows="4"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">立即创建</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style type="scss" scoped>
.sound-sample-form {
  max-width: 600px;
  margin: 0 auto;
  padding: 2rem;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);

  .form-title {
    display: flex;
    align-items: center;
    font-size: 1.5rem;
    color: #409EFF;
    margin-bottom: 1.5rem;

    i {
      margin-right: 0.5rem;
      font-size: 1.8rem;
    }
  }

  :deep(.el-form-item__label) {
    font-weight: 600;
  }

  :deep(.el-input__wrapper),
  :deep(.el-textarea__inner) {
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  }

  :deep(.el-button) {
    padding: 12px 20px;
    font-weight: 600;
  }

  :deep(.el-button--primary) {
    background-color: #409EFF;

    &:hover {
      background-color: #66b1ff;
    }
  }
}
</style>

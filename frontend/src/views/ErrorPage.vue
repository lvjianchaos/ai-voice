<!-- src/views/ErrorPage.vue -->
<script lang="ts" setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const goHome = () => {
  router.push('/');
};
</script>

<template>
  <div class="error-page">
    <el-result
      status="error"
      title="出错了"
      sub-title="遇到了一些问题或页面不存在，请稍后再试"
    >
      <template #icon>
        <i class="el-icon-error" style="font-size: 50px; color: #f56c6c;"></i>
      </template>
      <template #extra>
        <el-button type="primary" @click="goHome">返回首页</el-button>
      </template>
    </el-result>
  </div>
</template>

<style lang="scss" scoped>
.error-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f7f7f7;

  .el-result {
    width: 400px;
    background-color: #fff;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }

  .el-button {
    margin-top: 20px;
  }
}
</style>

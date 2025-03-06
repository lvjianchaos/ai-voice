<!-- src/components/layout/AppLayout.vue -->
<script lang="ts" setup>
import AppHeader from './AppHeader.vue';
</script>

<template>
  <div class="app-layout">
    <el-container class="layout-container">
      <AppHeader />
      <el-main class="layout-main">
        <RouterView />
      </el-main>
    </el-container>
  </div>
</template>

<style lang="scss" scoped>
.app-layout {
  background: linear-gradient(135deg, #e0e5ec 0%, #ffffff 100%); /* 渐变背景 */
  min-height: 100vh; /* 确保背景覆盖整个视口 */
  overflow: hidden; /* 防止滚动条 */
}

.layout-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.layout-main {
  padding: 20px;
  overflow: auto; /* 内容超出时显示滚动条 */
}
</style>

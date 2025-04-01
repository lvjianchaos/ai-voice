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
      <el-footer class="layout-footer">
        <div class="footer-content">
          <p>© {{ new Date().getFullYear() }} AI-Voice. All rights reserved.</p>
        </div>
      </el-footer>
    </el-container>
  </div>
</template>

<style lang="scss" scoped>
.app-layout {
  background: linear-gradient(135deg, var(--neutral-100) 0%, var(--neutral-50) 100%);
  min-height: 100vh;
  overflow: hidden;
}

.layout-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.layout-main {
  padding: var(--spacing-6);
  overflow: auto;
  flex: 1;

  @media (min-width: 768px) {
    padding: var(--spacing-8);
  }

  @media (min-width: 1200px) {
    padding: var(--spacing-10);
  }
}

.layout-footer {
  background-color: white;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
  padding: var(--spacing-4) var(--spacing-6);

  .footer-content {
    max-width: 1400px;
    margin: 0 auto;
    text-align: center;
    color: var(--neutral-600);
    font-size: 14px;
  }
}
</style>

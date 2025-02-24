<!-- src/components/layout/AppHeader.vue -->
<script lang="ts" setup>
import AppHeader from './AppHeader.vue';
</script>

<template>
  <div class="common-layout">
    <el-container class = "header-and-main">
      <AppHeader />
      <el-main>
        <RouterView />
      </el-main>
    </el-container>
  </div>
</template>

<style lang="scss" scoped>
.common-layout {
  background-color: rgb(232.8, 233.4, 234.6);
}
.header-and-main {
  height: 100vh;
  flex-direction: column;
}

</style>

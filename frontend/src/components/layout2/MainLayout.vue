<!-- src/components/layout2/AppHeader.vue -->
<script lang="ts" setup>
import MainAside from './MainAside.vue';
import MainHeader from './MainHeader.vue';
</script>

<template>
  <div class="common-layout">
    <el-container>
      <MainAside />
      <el-container class = "header-and-main">
        <MainHeader />
        <el-main>
          <el-scrollbar>
            <RouterView />
          </el-scrollbar>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style lang="scss" scoped>
.header-and-main {
  height: 100vh;
  flex-direction: column;
}
.el-main {
  // padding-right: 0;
  background-color: rgb(243.9, 244.2, 244.8);
}
</style>

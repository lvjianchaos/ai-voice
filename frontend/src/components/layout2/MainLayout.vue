<script lang="ts" setup>
import MainAside from './MainAside.vue';
import MainHead from './MainHead.vue';
</script>

<template>
  <div class="common-layout">
    <el-container>
      <MainAside />
      <el-container class = "header-and-main">
        <MainHead />
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
  padding-right: 0;
  background-color: rgb(239.8, 248.9, 235.3)
}
</style>

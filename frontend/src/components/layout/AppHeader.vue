<script lang="ts" setup>

</script>

<template>
  <el-header>
    <a href="/" class="logo">
      <img src="@/assets/logo.svg" />
      <h1>AI-Voice</h1>
    </a>
  </el-header>

</template>

<style lang="scss" scoped>

.el-header {
  background-color: #c4f1e9; /* 导航栏背景色 */
}

.logo {
  display: flex;
  justify-content: left;
  align-items: center;
  text-decoration: none;
  color: black;
  height: 60px;
  width:150px;
  img {
    width: 32px;
    height: 32px;
  }
}
</style>

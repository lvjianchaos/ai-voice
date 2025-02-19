<script lang="ts" setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const navigateToAbout = () => {
  router.push('/about');
};

const navigateToLogin = () => {
  router.push('/login');
};

const navigateToRegister = () => {
  router.push('/register');
};

const handleSelect = (key: string) => {
  console.log(`导航项被选中：${key}`);
};
</script>

<template>
  <el-header class="header">
    <a href="/" class="logo">
      <img src="@/assets/logo.svg" alt="AI-Voice Logo" />
      <h1>AI-Voice</h1>
    </a>
    <el-menu mode="horizontal" class="nav-menu" active-text-color="#409EFF" @select="handleSelect">
      <el-menu-item index="1" @click="navigateToAbout">关于</el-menu-item>
      <el-menu-item index="2" @click="navigateToLogin">登录</el-menu-item>
      <el-menu-item index="3" @click="navigateToRegister">注册</el-menu-item>
    </el-menu>
  </el-header>
</template>

<style lang="scss" scoped>
.el-header {
  background-color: rgb(221.7, 222.6, 224.4);
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .logo {
    display: flex;
    align-items: center;
    text-decoration: none;
    color: black;

    img {
      width: 40px;
      height: 40px;
      margin-right: 10px;
    }

    h1 {
      font-size: 24px;
    }
  }

  .nav-menu {
    background-color: transparent;
    border: none;
  }

  .el-menu-item {
    color: #666;
    font-size: 16px;
  }

  .el-menu-item:hover {
    color: #409EFF;
  }
}
</style>

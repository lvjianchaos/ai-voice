<!-- src/components/layout/AppHeader.vue -->
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
  <el-header class="app-header">
    <a href="/" class="logo">
      <img src="@/assets/favicon.ico" alt="AI-Voice Logo" />
      <h1>AI - Voice</h1>
    </a>
    <div class="header-actions">
      <el-button @click="navigateToAbout" class="about-button">关于</el-button>
      <el-button @click="navigateToLogin" class="login-button">登录</el-button>
      <el-button type="primary" @click="navigateToRegister" class="register-button">注册</el-button>
    </div>
  </el-header>
</template>

<style lang="scss" scoped>
.app-header {
  background-color: #fff;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  border-bottom: 1px solid #eee;
  z-index: 10;

  .logo {
    display: flex;
    align-items: center;
    text-decoration: none;
    color: #333;

    img {
      width: 40px;
      height: 40px;
      margin-right: 10px;
    }

    h1 {
      font-size: 28px;
      font-weight: 600;
      font-family: 'Lobster', cursive; /* 使用 Lobster 字体 */    }
  }

  .header-actions {
    display: flex;
    align-items: center;

    .el-button {
      margin-left: 15px;
      font-size: 16px;
      font-weight: 500;
      transition: all 0.3s ease; /* 添加过渡效果 */

      &:hover {
        transform: translateY(-2px); /* 悬停时向上移动 */
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 悬停时增加阴影 */
      }
    }

    .register-button {
      background-color: #409EFF;
      color: #fff;

      &:hover {
        background-color: #66b1ff;
      }
    }
  }
}
</style>

<!-- src/views/AppIndexView.vue -->
<script lang="ts" setup>
import router from '@/router';

const goToLogin = () => {
  // 跳转到登录页面
  router.push('/login');
};

const goToSignUp = () => {
  // 跳转到注册页面
  router.push('/register');
};
</script>

<template>
  <!-- 主体内容区域 -->
  <div class="main-content">
    <h1>欢迎来到 AI-Voice</h1>
    <p>基于AI语音合成的教学声音处理软件，提升您的教学效果。</p>
      <div class="button-group">
        <el-button type="primary" size="large" @click="goToLogin">登录</el-button>
        <el-button size="large" @click="goToSignUp">注册</el-button>
      </div>
  </div>
</template>

<style lang="scss" scoped>



  .main-content {
    height: 62vh;
    text-align: center;
    padding: 40px 20px;
    background-color: #ffffff;
    margin-top: 20px;
    border-radius: 25px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);

    h1 {
      margin-top: 80px;
      font-size: 32px;
      color: #333;
      margin-bottom: 30px;
    }

    p {
      font-size: 18px;
      color: #666;
      margin: 20px 0;
    }

    .button-group {
      margin-top: 80px;
      display: flex;
      justify-content: center;
      gap: 20px;
    }
  }

</style>

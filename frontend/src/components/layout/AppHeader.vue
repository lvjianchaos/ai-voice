<!-- src/components/layout/AppHeader.vue -->
<script lang="ts" setup>
import { useRouter } from 'vue-router';
import { ArrowRight } from '@element-plus/icons-vue';

const router = useRouter();

const navigateToLogin = () => {
  router.push('/login');
};

const navigateToRegister = () => {
  router.push('/register');
};
</script>

<template>
  <el-header class="app-header">
    <a href="/" class="logo">
      <img src="@/assets/favicon.ico" alt="AI-Voice Logo" />
      <h1>AI-Voice</h1>
    </a>

    <div class="nav-links">
      <a href="/#features" class="nav-link">特色功能</a>
      <a href="/#how-it-works" class="nav-link">使用方法</a>
      <a href="/about" class="nav-link">关于我们</a>
    </div>

    <div class="header-actions">
      <el-button @click="navigateToLogin" text class="login-button">登录</el-button>
      <el-button type="primary" @click="navigateToRegister" class="register-button">
        免费注册
        <el-icon class="el-icon--right"><ArrowRight /></el-icon>
      </el-button>
    </div>
  </el-header>
</template>

<style lang="scss" scoped>
.app-header {
  background-color: white;
  padding: 0 var(--spacing-6);
  height: 70px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: var(--shadow-sm);
  border-bottom: 1px solid var(--neutral-200);
  z-index: 10;

  @media (min-width: 768px) {
    padding: 0 var(--spacing-8);
  }

  .logo {
    display: flex;
    align-items: center;
    text-decoration: none;
    color: var(--neutral-900);

    img {
      width: 36px;
      height: 36px;
      margin-right: var(--spacing-3);
    }

    h1 {
      font-size: 24px;
      font-weight: 600;
      font-family: 'Poppins', sans-serif;
      background: linear-gradient(90deg, var(--primary-color), var(--secondary-color));
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      letter-spacing: -0.5px;
    }
  }

  .nav-links {
    display: none;

    @media (min-width: 768px) {
      display: flex;
      gap: var(--spacing-6);
    }

    .nav-link {
      color: var(--neutral-700);
      font-weight: 500;
      text-decoration: none;
      transition: color var(--transition-fast);
      font-size: 15px;
      position: relative;

      &:hover {
        color: var(--primary-color);
      }

      &::after {
        content: '';
        position: absolute;
        bottom: -4px;
        left: 0;
        width: 0;
        height: 2px;
        background-color: var(--primary-color);
        transition: width var(--transition-fast);
      }

      &:hover::after {
        width: 100%;
      }
    }
  }

  .header-actions {
    display: flex;
    align-items: center;
    gap: var(--spacing-3);

    .login-button {
      font-weight: 500;
      color: var(--primary-color);

      &:hover {
        color: var(--primary-dark);
      }
    }

    .register-button {
      border-radius: var(--border-radius-md);
      padding: 10px 16px;
      font-weight: 500;
      transition: all var(--transition-fast);
      box-shadow: 0 2px 6px rgba(25, 118, 210, 0.2);

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(25, 118, 210, 0.3);
      }
    }
  }
}

@media (max-width: 767px) {
  .app-header {
    padding: 0 var(--spacing-4);

    .logo h1 {
      font-size: 20px;
    }

    .login-button span {
      display: none;
    }
  }
}
</style>

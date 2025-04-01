<script lang="ts" setup>
import router from '@/router';
import { useTokenStore } from '@/stores/mytoken';
import { computed, onMounted } from 'vue';

const tokenStore = useTokenStore();
const userInfo = computed(() => tokenStore.getUser);

// 当前活动路由
const activeRoute = computed(() => {
  return router.currentRoute.value.path;
});

// 跳转函数
const navigateTo = (path: string) => {
  // 添加点击动画
  const menuItems = document.querySelectorAll('.menu-item');
  menuItems.forEach(item => {
    if ((item as HTMLElement).dataset.path === path) {
      item.classList.add('menu-item-active');

      // 创建波纹效果
      const ripple = document.createElement('span');
      ripple.classList.add('menu-ripple');
      item.appendChild(ripple);

      // 移除波纹效果
      setTimeout(() => {
        ripple.remove();
      }, 800);
    }
  });

  // 延迟导航，给动画时间
  setTimeout(() => {
    router.push(path);
  }, 180);
};

onMounted(() => {
  // 初始化侧边栏动画
  const aside = document.querySelector('.main-aside') as HTMLElement;
  if (aside) {
    // 淡入动画
    aside.style.opacity = '0';
    aside.style.transform = 'translateX(-20px)';

    setTimeout(() => {
      aside.style.transition = 'all 0.5s ease-out';
      aside.style.opacity = '1';
      aside.style.transform = 'translateX(0)';
    }, 300);
  }

  // 菜单项逐个淡入
  const menuItems = document.querySelectorAll('.menu-item');
  menuItems.forEach((item, index) => {
    const el = item as HTMLElement;
    el.style.opacity = '0';
    el.style.transform = 'translateX(-10px)';
    el.style.transition = 'all 0.3s ease-out';

    setTimeout(() => {
      el.style.opacity = '1';
      el.style.transform = 'translateX(0)';
    }, 400 + index * 100);
  });

  // 添加菜单项悬浮动画
  menuItems.forEach(item => {
    item.addEventListener('mouseenter', () => {
      // 不是活动项才加悬浮效果
      if (!item.classList.contains('is-active')) {
        const icon = item.querySelector('.el-icon') as HTMLElement;
        if (icon) {
          icon.style.transform = 'scale(1.2) translateX(3px)';
          icon.style.transition = 'transform 0.3s cubic-bezier(0.34, 1.56, 0.64, 1)';
        }
      }
    });

    item.addEventListener('mouseleave', () => {
      const icon = item.querySelector('.el-icon') as HTMLElement;
      if (icon) {
        icon.style.transform = 'scale(1) translateX(0)';
      }
    });
  });

  // 添加用户头像动画
  const avatar = document.querySelector('.user-avatar') as HTMLElement;
  if (avatar) {
    avatar.addEventListener('mouseenter', () => {
      avatar.style.transform = 'scale(1.1) rotate(5deg)';
      avatar.style.transition = 'all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1)';
      avatar.style.boxShadow = '0 6px 15px rgba(0, 0, 0, 0.2)';
    });

    avatar.addEventListener('mouseleave', () => {
      avatar.style.transform = 'scale(1) rotate(0)';
      avatar.style.boxShadow = '0 4px 10px rgba(0, 0, 0, 0.1)';
    });
  }
});
</script>

<template>
  <aside class="main-aside">
    <div class="aside-header">
      <div class="logo-container">
        <img src="@/assets/favicon.ico" alt="Logo" class="logo-img animate-pulse" />
        <h1 class="logo-text">AI-Voice</h1>
      </div>
    </div>

    <div class="aside-content">
      <el-menu
        default-active="/main"
        class="aside-menu"
        :router="false"
        @select="navigateTo"
      >
        <el-menu-item index="/main" data-path="/main" class="menu-item">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>

        <el-menu-item index="/main/sample" data-path="/main/sample" class="menu-item">
          <el-icon><Microphone /></el-icon>
          <span>样本管理</span>
        </el-menu-item>

        <el-menu-item index="/main/composition" data-path="/main/composition" class="menu-item">
          <el-icon><Edit /></el-icon>
          <span>合成管理</span>
        </el-menu-item>

        <el-menu-item index="/main/audio" data-path="/main/audio" class="menu-item">
          <el-icon><Headset /></el-icon>
          <span>音频管理</span>
        </el-menu-item>

        <el-menu-item index="/main/courseware" data-path="/main/courseware" class="menu-item">
          <el-icon><Document /></el-icon>
          <span>课件工具</span>
        </el-menu-item>

        <el-menu-item index="/about" data-path="/about" class="menu-item">
          <el-icon><InfoFilled /></el-icon>
          <span>关于我们</span>
        </el-menu-item>
      </el-menu>
    </div>

    <div class="aside-footer">
      <div class="user-container">
        <img
          :src="userInfo.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'"
          alt="Avatar"
          class="user-avatar"
        />
        <div class="user-info">
          <p class="user-name">{{ userInfo.name }}</p>
          <p class="user-role">普通用户</p>
        </div>
      </div>
    </div>
  </aside>
</template>

<style lang="scss" scoped>
.main-aside {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  background-color: #fff;
  border-right: 1px solid var(--neutral-200);
  box-shadow: var(--shadow-md);
  position: relative;
  overflow: hidden;
  will-change: transform, opacity;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, rgba(var(--primary-color-rgb), 0.03) 0%, transparent 100%);
    z-index: 0;
  }
}

.aside-header {
  padding: var(--spacing-4);
  border-bottom: 1px solid var(--neutral-200);
  background-color: rgba(var(--primary-color-rgb), 0.05);
  position: relative;
  z-index: 1;

  .logo-container {
    display: flex;
    align-items: center;
    gap: var(--spacing-3);

    .logo-img {
      width: 36px;
      height: 36px;
      transition: all 0.5s ease;

      &:hover {
        transform: rotate(360deg);
      }
    }

    .logo-text {
      font-size: 20px;
      font-weight: 600;
      color: var(--primary-color);
      background: linear-gradient(90deg, var(--primary-color), var(--primary-light));
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      letter-spacing: 0.5px;
    }
  }
}

.aside-content {
  flex: 1;
  padding: var(--spacing-2) 0;
  position: relative;
  z-index: 1;

  .aside-menu {
    border-right: none;

    .menu-item {
      position: relative;
      height: 50px;
      line-height: 50px;
      margin: var(--spacing-1) var(--spacing-2);
      border-radius: var(--border-radius-md);
      transition: all 0.3s ease;
      overflow: hidden;

      .el-icon {
        margin-right: var(--spacing-3);
        font-size: 18px;
        will-change: transform;
      }

      span {
        font-size: 14px;
      }

      &.is-active {
        background-color: rgba(var(--primary-color-rgb), 0.1);
        color: var(--primary-color);

        &::after {
          content: '';
          position: absolute;
          left: 0;
          top: 0;
          height: 100%;
          width: 4px;
          background: linear-gradient(to bottom, var(--primary-color), var(--primary-light));
          border-radius: 0 2px 2px 0;
        }
      }

      &:not(.is-active):hover {
        background-color: rgba(var(--primary-color-rgb), 0.05);
        color: var(--primary-color);
      }

      &.menu-item-active {
        animation: click-animation 0.4s forwards;
      }

      @keyframes click-animation {
        0% {
          transform: scale(1);
        }
        50% {
          transform: scale(0.97);
        }
        100% {
          transform: scale(1);
        }
      }

      .menu-ripple {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 5px;
        height: 5px;
        background-color: rgba(var(--primary-color-rgb), 0.3);
        border-radius: 50%;
        animation: ripple-animation 0.8s ease-out;
        z-index: 0;
      }

      @keyframes ripple-animation {
        0% {
          width: 0;
          height: 0;
          opacity: 0.5;
        }
        100% {
          width: 500px;
          height: 500px;
          opacity: 0;
        }
      }
    }
  }
}

.aside-footer {
  padding: var(--spacing-4);
  border-top: 1px solid var(--neutral-200);
  background-color: rgba(var(--primary-color-rgb), 0.03);
  position: relative;
  z-index: 1;

  .user-container {
    display: flex;
    align-items: center;
    gap: var(--spacing-3);

    .user-avatar {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      object-fit: cover;
      border: 2px solid rgba(var(--primary-color-rgb), 0.2);
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
      transition: all 0.3s ease;
      will-change: transform, box-shadow;
    }

    .user-info {
      flex: 1;

      .user-name {
        font-size: 14px;
        font-weight: 500;
        color: var(--neutral-900);
        margin: 0 0 2px 0;
      }

      .user-role {
        font-size: 12px;
        color: var(--neutral-600);
        margin: 0;
      }
    }
  }
}

/* 暗色模式适配 */
@media (prefers-color-scheme: dark) {
  .main-aside {
    background-color: #1e1e1e;
    border-right: 1px solid var(--neutral-800);

    &::before {
      background: linear-gradient(135deg, rgba(var(--primary-color-rgb), 0.1) 0%, transparent 100%);
    }
  }

  .aside-header {
    border-bottom: 1px solid var(--neutral-800);
    background-color: rgba(var(--primary-color-rgb), 0.1);
  }

  .aside-content {
    .aside-menu {
      background-color: transparent;

      .menu-item {
        &:not(.is-active) {
          color: var(--neutral-300);
        }

        &.is-active {
          background-color: rgba(var(--primary-color-rgb), 0.2);
        }

        &:not(.is-active):hover {
          background-color: rgba(var(--primary-color-rgb), 0.1);
        }
      }
    }
  }

  .aside-footer {
    border-top: 1px solid var(--neutral-800);
    background-color: rgba(var(--primary-color-rgb), 0.1);

    .user-container {
      .user-info {
        .user-name {
          color: var(--neutral-300);
        }

        .user-role {
          color: var(--neutral-500);
        }
      }
    }
  }
}
</style>

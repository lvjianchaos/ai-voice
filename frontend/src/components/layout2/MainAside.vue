<!-- src/components/layout2/MainAside.vue -->
<script lang="ts" setup>
import { isCollapse } from './isCollapse';
</script>

<template>
  <el-aside class="main-aside" :width="isCollapse ? '64px' : '220px'">
    <el-scrollbar class="scrollbar-thin">
      <div class="logo-container">
        <router-link to="/main" class="logo">
          <img src="@/assets/favicon.ico" alt="logo" />
          <h1 v-show="!isCollapse">AI-Voice</h1>
        </router-link>
      </div>

      <el-menu
        router
        unique-opened
        :collapse="isCollapse"
        class="main-menu"
        background-color="transparent"
        text-color="var(--neutral-800)"
        active-text-color="var(--primary-color)"
      >
        <el-sub-menu index="1" popper-class="menu-popper">
          <template #title>
            <el-icon><IEpTools /></el-icon>
            <span>声音管理</span>
          </template>
          <el-menu-item index="/sample-library">
            <el-icon><IEpShop /></el-icon>
            <span>声音样本库</span>
          </el-menu-item>
          <el-menu-item index="voice-guide">
            <el-icon><IEpPhoneFilled /></el-icon>
            <span>个性语音讲解</span>
          </el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/courseware">
          <el-icon><IEpList /></el-icon>
          <span>课件制作</span>
        </el-menu-item>

        <el-sub-menu index="2" popper-class="menu-popper">
          <template #title>
            <el-icon><IEpVideoCameraFilled /></el-icon>
            <span>音视频处理</span>
          </template>
          <el-menu-item index="/audio-subtitles">
            <el-icon><IEpPlatform /></el-icon>
            <span>声音置换+字幕</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-scrollbar>
  </el-aside>
</template>

<style lang="scss" scoped>
.main-aside {
  height: 100vh;
  transition: width var(--transition-normal);
  border-right: 1px solid var(--neutral-200);
  background-color: white;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .scrollbar-thin {
    height: 100%;
  }

  .logo-container {
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0 var(--spacing-4);
    border-bottom: 1px solid var(--neutral-200);

    .logo {
      display: flex;
      align-items: center;
      text-decoration: none;
      color: var(--neutral-900);
      width: 100%;

      img {
        width: 32px;
        height: 32px;
      }

      h1 {
        font-size: 18px;
        font-weight: 600;
        margin-left: var(--spacing-3);
        white-space: nowrap;
        background: linear-gradient(90deg, var(--primary-color), var(--secondary-color));
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        font-family: 'Poppins', sans-serif;
      }
    }
  }

  .main-menu {
    border-right: none;
    transition: all var(--transition-normal);

    :deep(.el-menu-item) {
      height: 50px;
      padding: 0 var(--spacing-4);
      margin: var(--spacing-1) var(--spacing-2);
      border-radius: var(--border-radius-md);

      &.is-active {
        background-color: rgba(25, 118, 210, 0.1);
        color: var(--primary-color);
        font-weight: 500;
      }

      &:hover {
        background-color: var(--neutral-100);
      }

      .el-icon {
        margin-right: var(--spacing-3);
      }
    }

    :deep(.el-sub-menu) {
      .el-sub-menu__title {
        height: 50px;
        padding: 0 var(--spacing-4);
        margin: var(--spacing-1) var(--spacing-2);
        border-radius: var(--border-radius-md);

        &:hover {
          background-color: var(--neutral-100);
        }

        .el-icon {
          margin-right: var(--spacing-3);
        }
      }
    }
  }
}

:global(.menu-popper) {
  .el-menu--popup {
    padding: var(--spacing-2) 0;
    border-radius: var(--border-radius-md);

    .el-menu-item {
      height: 44px;
      margin: var(--spacing-1) var(--spacing-2);
      border-radius: var(--border-radius-md);

      &.is-active {
        background-color: rgba(25, 118, 210, 0.1);
        color: var(--primary-color);
      }

      &:hover {
        background-color: var(--neutral-100);
      }
    }
  }
}
</style>

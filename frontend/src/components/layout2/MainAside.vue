<!-- src/components/layout2/MainAside.vue -->
<script lang="ts" setup>
import { isCollapse } from './isCollapse';
</script>

<template>
    <el-aside>
      <el-scrollbar>
        <el-menu router unique-opened :collapse="isCollapse">
          <a href="/main" class="logo">
            <img src="@/assets/logo.svg" alt="logo" />
            <h1>AI-Voice</h1>
          </a>
          <el-sub-menu index="1">
            <template #title>
              <el-icon> <IEpTools /> </el-icon> <span> 声音管理 </span>
            </template>
            <el-menu-item index="/sample-library">
              <el-icon> <IEpShop /> </el-icon> <span> 声音样本库 </span>
            </el-menu-item>
            <el-menu-item index="voice-guide">
              <el-icon> <IEpPhoneFilled /> </el-icon> <span> 个性语音讲解 </span>
            </el-menu-item>
          </el-sub-menu>

          <el-menu-item index="/courseware">
            <el-icon> <IEpList /> </el-icon> <span> 课件制作 </span>
          </el-menu-item>

          <el-sub-menu index="2">
            <template #title>
              <el-icon> <IEpVideoCameraFilled /> </el-icon> <span> 音视频处理 </span>
            </template>
            <el-menu-item index="/audio-subtitles">
              <el-icon> <IEpPlatform /> </el-icon> <span> 声音置换+字幕 </span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-scrollbar>
    </el-aside>
</template>

<style lang="scss" scoped>
  /* logo样式 */
  .logo {
    display: flex;
    justify-content: center;
    align-items: center;
    text-decoration: none;
    color: #303133;
    height: 60px;
    img {
      width: 32px;
      height: 32px;
    }
  }

  /* 侧边栏样式 */
  .el-aside {
    color: #303133;
    background-color: rgb(232.8, 233.4, 234.6);
    border-radius: 10px;
    height: 100vh;
    width:auto;
    text-align: center;
    line-height: 200px;
  }
  /* 菜单样式 */
  .el-menu {
    background-color: rgb(232.8, 233.4, 234.6);
    border-right: none;
    /* 菜单折叠 */
    &.el-menu--collapse {
        width: 64px;
        & h1 {
          display: none;
        }
    }
  }
</style>

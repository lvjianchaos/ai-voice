<script lang="ts" setup>
import { isCollapse } from './isCollapse';
</script>

<template>
    <el-aside>
      <el-scrollbar>
        <el-menu router unique-opened :collapse="isCollapse">
          <a href="/" class="logo">
            <img src="@/assets/logo.svg" alt="logo" />
            <h1>AI-Voice</h1>
          </a>
          <el-sub-menu index="1">
            <template #title>
              <el-icon> <IEpTools /> </el-icon> <span> 声音管理 </span>
            </template>
            <el-menu-item index="/sample-library">
              <el-icon> <IEpTools /> </el-icon> <span> 声音样本库 </span>
            </el-menu-item>
            <el-menu-item index="voice-guide">
              <el-icon> <IEpTools /> </el-icon> <span> 个性化语音讲解 </span>
            </el-menu-item>
            <el-menu-item index="/standard-output">
              <el-icon> <IEpTools /> </el-icon> <span> 标准语言输出 </span>
            </el-menu-item>
          </el-sub-menu>

          <el-menu-item index="/courseware">
            <el-icon> <IEpTools /> </el-icon> <span> 课件制作 </span>
          </el-menu-item>

          <el-sub-menu index="2">
            <template #title>
              <el-icon> <IEpTools /> </el-icon> <span> 音视频处理 </span>
            </template>
            <el-menu-item index="/audio-subtitles">
              <el-icon> <IEpTools /> </el-icon> <span> 声音置换及加字幕 </span>
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
    background-color: rgb(224.6, 242.8, 215.6);
    height: 100vh;
    width:auto;
    text-align: center;
    line-height: 200px;
  }
  /* 菜单样式 */
  .el-menu {
    background-color: rgb(224.6, 242.8, 215.6);
    width: 200px;
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

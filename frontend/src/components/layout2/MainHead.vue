<script lang="ts" setup>
import { isCollapse } from './isCollapse';

</script>

<template>
  <el-header>
    <!--icon-->
    <el-icon @click="isCollapse = !isCollapse">
      <IEpExpand v-show="isCollapse"/>
      <IEpFold v-show="!isCollapse"/>
    </el-icon>

    <!--面包屑-->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/main' }">homepage</el-breadcrumb-item>
      <el-breadcrumb-item>
        <a href="/main">promotion management</a>
      </el-breadcrumb-item>
      <el-breadcrumb-item>promotion list</el-breadcrumb-item>
      <el-breadcrumb-item>promotion detail</el-breadcrumb-item>
    </el-breadcrumb>

    <!--下拉菜单-->
    <el-dropdown>
    <span class="el-dropdown-link">
      <el-avatar :size="35" :src="'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
      <el-icon class="el-icon--right">
        <IEparrow-down />
      </el-icon>
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item>用户姓名</el-dropdown-item>
        <el-dropdown-item divided>Lay out</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>

  </el-header>

</template>

<style lang="scss" scoped>
.el-header {
  display: flex;
  align-items: center;
  background-color: rgb(209.4, 236.7, 195.9);
  .el-icon {
    font-size: 20px;
    margin-right:17px;
  }
}
.el-dropdown {
  margin-left: auto;
  .el-dropdown-link {
    justify-content: center;
    display: flex;
    align-items: center;
  }
}
</style>

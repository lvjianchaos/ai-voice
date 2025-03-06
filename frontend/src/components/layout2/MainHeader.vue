<!-- src/components/layout2/AppHeader.vue -->
<script lang="ts" setup>
import { isCollapse } from './isCollapse';
import { getInfo , logout } from '@/api/users';
import { useTokenStore } from '@/stores/mytoken';

const router = useRouter()

const userInfo = ref({ portrait: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png', userName: "Chaos"})
getInfo().then((res) => {
  userInfo.value = res.data.data
})

// 退出事件处理
const handleLogout = async () => {
  // 1. 讯问用户确认
  await ElMessageBox.confirm('确认要退出吗？', '推出询问', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
  }).catch(() => {
    ElMessage.info('已取消退出');
    return new Promise(()=>{})
  })
  // 2. 退出
  await logout().catch(()=>{})
  ElMessage.success('退出成功！');
  // 3. 清空token，跳转到默认页面
  useTokenStore().saveToken("")
  router.push({name:'login'})
}
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
      <el-breadcrumb-item v-for="(item,index) in $route.matched" :key="index" :to="{ path: item.path }">{{
        item.meta.title}}
      </el-breadcrumb-item>
    </el-breadcrumb>

    <!--下拉菜单-->
    <el-dropdown>
    <span class="el-dropdown-link">
      <el-avatar
        :size="34"
        :src="userInfo.portrait"
      />
      <el-icon class="el-icon--right">
        <IEparrow-down />
      </el-icon>
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item>{{ userInfo.userName }}</el-dropdown-item>
        <el-dropdown-item divided @click="handleLogout">Log Out</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>

  </el-header>

</template>

<style lang="scss" scoped>
.el-header {
  border-radius: 5px;
  display: flex;

  align-items: center;
  background-color: rgb(221.7, 222.6, 224.4);
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

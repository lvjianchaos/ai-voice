<!-- src/components/layout2/MainHeader.vue -->
<script lang="ts" setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox, ElMessage } from 'element-plus';
import { isCollapse } from './isCollapse';
import { getInfo, logout } from '@/api/users';
import { useTokenStore } from '@/stores/mytoken';
import { Expand, Fold, ArrowDown, User, Setting, SwitchButton } from '@element-plus/icons-vue';

const router = useRouter();

const userInfo = ref({ portrait: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png', userName: "Chaos"});

// Get user info
getInfo().then((res) => {
  userInfo.value = res.data.data;
});

// Handle logout
const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确认要退出吗？', '退出确认', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    });

    await logout().catch(() => {});
    ElMessage.success('退出成功！');

    // Clear token and redirect to login
    useTokenStore().saveToken("");
    router.push({name: 'login'});
  } catch {
    ElMessage.info('已取消退出');
  }
};
</script>

<template>
  <el-header class="main-header">
    <!-- Menu toggle -->
    <div class="left-section">
      <el-button
        class="menu-toggle"
        :icon="isCollapse ? Expand : Fold"
        text
        @click="isCollapse = !isCollapse"
      />

      <!-- Breadcrumb -->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item
          v-for="(item, index) in $route.matched"
          :key="index"
          :to="{ path: item.path }"
        >
          {{ item.meta.title }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- Right Section -->
    <div class="right-section">
      <el-dropdown trigger="click">
        <div class="user-dropdown">
          <el-avatar
            :size="36"
            :src="userInfo.portrait"
          />
          <span class="username">{{ userInfo.userName }}</span>
          <el-icon><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>
              <el-icon><User /></el-icon>
              个人资料
            </el-dropdown-item>
            <el-dropdown-item>
              <el-icon><Setting /></el-icon>
              系统设置
            </el-dropdown-item>
            <el-dropdown-item divided @click="handleLogout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </el-header>
</template>

<style lang="scss" scoped>
.main-header {
  background-color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
  padding: 0 var(--spacing-5);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  border-bottom: 1px solid var(--neutral-200);

  .left-section {
    display: flex;
    align-items: center;
    gap: var(--spacing-3);

    .menu-toggle {
      font-size: 20px;
      color: var(--neutral-700);

      &:hover {
        color: var(--primary-color);
      }
    }

    .el-breadcrumb {
      font-size: 14px;

      :deep(.el-breadcrumb__item) {
        .el-breadcrumb__inner {
          color: var(--neutral-700);
          transition: color var(--transition-fast);

          &.is-link:hover {
            color: var(--primary-color);
          }
        }

        &:last-child {
          .el-breadcrumb__inner {
            color: var(--primary-color);
            font-weight: 600;
          }
        }
      }
    }
  }

  .right-section {
    display: flex;
    align-items: center;
    gap: var(--spacing-6);

    .user-dropdown {
      display: flex;
      align-items: center;
      gap: var(--spacing-2);
      cursor: pointer;
      padding: var(--spacing-1) var(--spacing-2);
      border-radius: var(--border-radius-md);
      transition: background-color var(--transition-fast);

      &:hover {
        background-color: var(--neutral-100);
      }

      .username {
        font-size: 14px;
        color: var(--neutral-800);
        font-weight: 500;
        margin-left: var(--spacing-2);

        @media (max-width: 768px) {
          display: none;
        }
      }

      .el-icon {
        font-size: 12px;
        color: var(--neutral-600);
        margin-left: var(--spacing-1);
      }
    }
  }
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);

  .el-icon {
    font-size: 16px;
  }
}
</style>

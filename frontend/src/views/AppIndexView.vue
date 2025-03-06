<script lang="ts" setup>
import { User, Opportunity } from '@element-plus/icons-vue'
import router from '@/router'

const goToLogin = () => router.push('/login')
const goToSignUp = () => router.push('/register')
</script>

<template>
  <div class="enhanced-main">
    <!-- 核心操作区 -->
    <div class="action-section">
      <h1 class="main-title">AI 赋能教学创新</h1>
      <p class="sub-title">智能语音解决方案助力教育数字化转型</p>

      <div class="action-buttons">
        <el-button
          type="primary"
          size="large"
          class="action-btn login-btn"
          @click="goToLogin"
        >
          <el-icon><User /></el-icon>
          立即登录
        </el-button>
        <el-button
          type="success"
          size="large"
          class="action-btn signup-btn"
          @click="goToSignUp"
        >
          <el-icon><Opportunity /></el-icon>
          免费注册
        </el-button>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.enhanced-main {
  padding: 40px 20px;
  background-color: rgba(255, 255, 255, 0.95);
  border-radius: 24px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  margin: 20px auto;
  max-width: 1200px;
  height: 75%;
  .feature-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 24px;
    padding: 24px 0;

    .feature-card {
      border-radius: 16px;
      transition: transform 0.3s ease;

      &:hover {
        transform: translateY(-5px);
      }

      :deep(.el-card__header) {
        border-bottom: none;
        padding: 24px;
        background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);

        .feature-icon {
          font-size: 36px;
          color: #409EFF;
          margin-bottom: 16px;
        }

        h3 {
          font-size: 20px;
          color: #303133;
          margin: 0;
        }
      }

      .feature-desc {
        color: #606266;
        font-size: 14px;
        line-height: 1.6;
        margin: 0;
      }
    }
  }

  .action-section {
    text-align: center;
    padding: 110px 0;

    .main-title {
      font-size: 36px;
      color: #303133;
      margin-bottom: 16px;
      letter-spacing: 1px;
    }

    .sub-title {
      font-size: 18px;
      color: #606266;
      margin-bottom: 40px;
    }

    .action-buttons {
      display: flex;
      justify-content: center;
      gap: 32px;
      flex-wrap: wrap;

      .action-btn {
        padding: 18px 36px;
        font-size: 16px;
        border-radius: 12px;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

        &.login-btn {
          background: linear-gradient(135deg, #409EFF, #6c9cff);
          border: none;
          box-shadow: 0 4px 14px rgba(64, 158, 255, 0.3);

          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(64, 158, 255, 0.4);
          }
        }

        &.signup-btn {
          background: linear-gradient(135deg, #67C23A, #85ce61);
          border: none;
          box-shadow: 0 4px 14px rgba(103, 194, 58, 0.3);

          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(103, 194, 58, 0.4);
          }
        }

        .el-icon {
          margin-right: 8px;
          font-size: 20px;
          vertical-align: middle;
        }
      }
    }
  }
}
</style>

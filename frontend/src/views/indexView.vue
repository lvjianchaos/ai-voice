<script lang="ts" setup>
import { ref, onMounted } from 'vue';

// 欢迎语
const welcomeMessage = ref('');

onMounted(() => {
  // 获取当前时间
  const now = new Date();
  const hour = now.getHours();

  // 根据时间设置欢迎语
  if (hour < 12) {
    welcomeMessage.value = '早上好！';
  } else if (hour < 18) {
    welcomeMessage.value = '下午好！';
  } else {
    welcomeMessage.value = '晚上好！';
  }
});
</script>

<template>
  <div class="main-container">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span>{{ welcomeMessage }} 欢迎使用 AI-Voice 系统！</span>
        </div>
      </template>
      <div class="main-content">
        <p>
          AI-Voice 是一款基于人工智能语音合成和声音克隆技术的系统，旨在提高教学效果、增强学习体验。
        </p>
        <p>
          您可以在这里体验个性化语音讲解、课件制作、声音置换和字幕等功能。
        </p>
        <el-button type="primary" size="large" @click="$router.push('/courseware')">
          开始制作课件
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<style lang="scss" scoped>
.main-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 85vh;
  background: linear-gradient(to bottom, #f0f2f5, #e0e5ec);
  border-radius: 30px;

  .main-card {
    width: 600px;
    border-radius: 15px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease;

    &:hover {
      transform: translateY(-5px);
    }

    .card-header {
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 15px;

      span {
        font-size: 24px;
        font-weight: 600;
        color: #333;
      }
    }

    .main-content {
      padding: 20px;
      text-align: center;

      p {
        font-size: 16px;
        color: #555;
        margin-bottom: 20px;
      }

      .el-button {
        border-radius: 8px;
      }
    }
  }
}
</style>

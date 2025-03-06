<!-- src/views/AboutView.vue -->
<template>
  <div class="about-container">
    <section class="about-carousel">
      <el-carousel height="400px" indicator-position="outside">
        <el-carousel-item v-for="item in carouselItems" :key="item.id">
          <div class="carousel-item-content" :style="{ backgroundColor: item.backgroundColor }">
            <el-icon :size="80" color="#fff"><component :is="item.icon" /></el-icon>
            <h3>{{ item.title }}</h3>
            <p>{{ item.description }}</p>
          </div>
        </el-carousel-item>
      </el-carousel>
    </section>

    <section class="about-hero">
      <div class="hero-content">
        <h1>关于 AI-Voice</h1>
        <p>
          AI-Voice 是一款基于人工智能语音合成和声音克隆技术的教学声音处理软件。
          我们致力于通过个性化的语音讲解、标准语言输出、有声课件制作等功能，
          提高教学效果、增强学习体验、促进教育公平。
        </p>
      </div>
    </section>

    <!-- <section class="about-team">
      <h2>我们的团队</h2>
      <div class="team-list">
        <div class="team-member">
          <img src="@/assets/team-member-1.jpg" alt="Team Member 1" />
          <h3>John Doe</h3>
          <p>创始人 & CEO</p>
        </div>
        <div class="team-member">
          <img src="@/assets/team-member-2.jpg" alt="Team Member 2" />
          <h3>Jane Smith</h3>
          <p>技术总监</p>
        </div>
      </div>
    </section> -->
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { Edit, Mic, Document, VideoCamera } from '@element-plus/icons-vue';

const carouselItems = ref([
  {
    id: 1,
    title: '个性化语音讲解',
    description: '上传或输入文本，选择声音样本进行语音讲解。',
    icon: Edit,
    backgroundColor: '#409EFF',
  },
  {
    id: 2,
    title: '标准语言输出',
    description: '输出普通话、英文等标准发音，可控制语速、语气和节奏。',
    icon: Mic,
    backgroundColor: '#67C23A',
  },
  {
    id: 3,
    title: '课件制作下载',
    description: '上传课件，选择声音样本，完成有声课件的制作和下载。',
    icon: Document,
    backgroundColor: '#E6A23C',
  },
  {
    id: 4,
    title: '声音置换及加字幕',
    description: '导入音视频，通过更换声音实现变声，同步展示字幕。',
    icon: VideoCamera,
    backgroundColor: '#F56C6C',
  },
]);
</script>

<style lang="scss" scoped>
.about-container {
  padding: 20px;
}

.about-carousel {
  h2 {
    font-size: 28px; /* 调整标题字体大小 */
    font-weight: 600;
    margin-bottom: 20px; /* 调整间距 */
    text-align: center;
  }

  .el-carousel {
    border-radius: 15px;
    overflow: hidden;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    margin-bottom: 30px; /* 调整间距 */

    .el-carousel__item {
      background-color: #f2f2f2;
      color: #333;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
    }

    .carousel-item-content {
      padding: 20px;
      text-align: center;
      color: #fff;
      border-radius: 15px;

      h3 {
        font-size: 20px; /* 调整标题字体大小 */
        font-weight: 500;
        margin-bottom: 10px;
      }

      p {
        font-size: 14px; /* 调整描述字体大小 */
        line-height: 1.4;
        margin-bottom: 15px;
      }
    }
  }
}

.about-hero {
  background: linear-gradient(to right, #f0f2f5, #e0e5ec);
  padding: 60px 0;
  text-align: center;
  border-radius: 15px; /* 添加圆角 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加阴影 */
  margin-bottom: 30px; /* 增加底部间距 */

  .hero-content {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px; /* 增加内边距 */

    h1 {
      font-size: 36px;
      font-weight: 700;
      margin-bottom: 15px;
      color: #333; /* 修改标题颜色 */
    }

    p {
      font-size: 16px;
      line-height: 1.6;
      margin-bottom: 0; /* 移除底部间距 */
      color: #666; /* 修改描述颜色 */
    }
  }
}

.about-team {
  h2 {
    font-size: 28px; /* 调整标题字体大小 */
    font-weight: 600;
    margin-bottom: 20px; /* 调整间距 */
    text-align: center;
  }

  .team-list {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;

    .team-member {
      width: 250px;
      margin: 20px;
      text-align: center;

      img {
        width: 120px; /* 调整图片大小 */
        height: 120px; /* 调整图片大小 */
        border-radius: 50%;
        margin-bottom: 10px;
      }

      h3 {
        font-size: 18px; /* 调整标题字体大小 */
        font-weight: 500;
        margin-bottom: 8px;
      }

      p {
        font-size: 14px; /* 调整描述字体大小 */
        line-height: 1.4;
      }
    }
  }
}
</style>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { Microphone, VideoPlay, Document, Edit, Headset } from '@element-plus/icons-vue';

const router = useRouter();
const greeting = ref('');

// 根据时间设置问候语
const setGreeting = () => {
  const hour = new Date().getHours();
  if (hour < 12) {
    greeting.value = '早上好';
  } else if (hour < 18) {
    greeting.value = '下午好';
  } else {
    greeting.value = '晚上好';
  }
};

// 导航到指定路由
const navigateTo = (path: string) => {
  router.push(path);
};

// 动画效果
const animateElements = () => {
  const elements = document.querySelectorAll('.animate-on-enter');
  elements.forEach((el, index) => {
    setTimeout(() => {
      el.classList.add('animated');
    }, index * 300);
  });
};

onMounted(() => {
  setGreeting();
  animateElements();
});
</script>

<template>
  <div class="home-page">
    <div class="hero-section animate-on-enter">
      <div class="greeting-container">
        <h1 class="greeting">{{ greeting }}</h1>
        <div class="typing-container">
          <h2 class="typing-text">欢迎使用 AI-Voice</h2>
        </div>
      </div>

      <div class="hero-image">
        <div class="image-container">
          <div class="floating-icons">
            <div class="floating-icon icon-1"><el-icon><Microphone /></el-icon></div>
            <div class="floating-icon icon-2"><el-icon><VideoPlay /></el-icon></div>
            <div class="floating-icon icon-3"><el-icon><Document /></el-icon></div>
            <div class="floating-icon icon-4"><el-icon><Edit /></el-icon></div>
          </div>
        </div>
      </div>
    </div>

    <div class="features-section animate-on-enter">
      <h2 class="section-title">AI语音解决方案</h2>
      <p class="section-description">为您提供全方位的AI语音合成与处理服务</p>

      <div class="features-grid">
        <el-card class="feature-card" shadow="hover" @click="navigateTo('/voice-guide')">
          <div class="feature-icon voice-icon">
            <el-icon><Microphone /></el-icon>
          </div>
          <h3 class="feature-title">个性化语音指南</h3>
          <p class="feature-description">将文本内容转换为自然流畅的语音解说，支持多种音色、语速和音调调节</p>
        </el-card>

        <el-card class="feature-card" shadow="hover" @click="navigateTo('/courseware')">
          <div class="feature-icon courseware-icon">
            <el-icon><VideoPlay /></el-icon>
          </div>
          <h3 class="feature-title">音频课件制作</h3>
          <p class="feature-description">上传PPT文件，AI自动生成带解说的视频课件，提升教学效果</p>
        </el-card>

        <el-card class="feature-card" shadow="hover" @click="navigateTo('/audio-subtitles')">
          <div class="feature-icon audio-icon">
            <el-icon><Document /></el-icon>
          </div>
          <h3 class="feature-title">音频字幕生成</h3>
          <p class="feature-description">上传音频或视频文件，AI自动识别语音内容并生成精准字幕</p>
        </el-card>

        <el-card class="feature-card" shadow="hover" @click="navigateTo('/sample-library')">
          <div class="feature-icon sample-icon">
            <el-icon><Headset /></el-icon>
          </div>
          <h3 class="feature-title">声音样本库</h3>
          <p class="feature-description">管理您的声音样本，用于生成个性化的语音内容</p>
        </el-card>
      </div>
    </div>

    <div class="cta-section animate-on-enter">
      <div class="cta-content">
        <h2 class="cta-title">开始您的AI语音之旅</h2>
        <p class="cta-description">立即探索AI-Voice的功能，体验智能语音合成的魅力</p>
        <el-button type="primary" size="large" class="cta-button" @click="navigateTo('/voice-guide')">
          立即开始
        </el-button>
      </div>

      <div class="wave-animation">
        <div class="wave"></div>
        <div class="wave"></div>
        <div class="wave"></div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.home-page {
  --animation-curve: cubic-bezier(0.34, 1.56, 0.64, 1);

  .animate-on-enter {
    opacity: 0;
    transform: translateY(30px);
    transition: all 1s var(--animation-curve);

    &.animated {
      opacity: 1;
      transform: translateY(0);
    }
  }

  .hero-section {
    display: flex;
    align-items: center;
    min-height: 500px;
    margin-bottom: var(--spacing-12);
    position: relative;

    @media (max-width: 992px) {
      flex-direction: column;
      text-align: center;
    }

    .greeting-container {
      flex: 1;
      padding-right: var(--spacing-8);

      @media (max-width: 992px) {
        padding-right: 0;
        margin-bottom: var(--spacing-8);
      }

      .greeting {
        font-size: 48px;
        font-weight: 800;
        color: var(--neutral-900);
        margin-bottom: var(--spacing-4);
        background: linear-gradient(45deg, var(--primary-color), var(--primary-light));
        -webkit-background-clip: text;
        background-clip: text;
        -webkit-text-fill-color: transparent;
        position: relative;
        display: inline-block;

        &::after {
          content: '';
          position: absolute;
          left: 0;
          bottom: -8px;
          width: 80px;
          height: 4px;
          background: linear-gradient(90deg, var(--primary-color), var(--primary-light));
          border-radius: 2px;

          @media (max-width: 992px) {
            left: 50%;
            transform: translateX(-50%);
          }
        }
      }

      .typing-container {
        height: 40px;
        position: relative;
        overflow: hidden;

        .typing-text {
          font-size: 24px;
          font-weight: 600;
          color: var(--neutral-700);
          position: relative;
          display: inline-block;

          &::after {
            content: '|';
            position: absolute;
            right: -8px;
            animation: blink 1s infinite;
          }
        }
      }
    }

    .hero-image {
      flex: 1;
      display: flex;
      justify-content: flex-end;

      @media (max-width: 992px) {
        justify-content: center;
      }

      .image-container {
        width: 400px;
        height: 400px;
        background: radial-gradient(circle, rgba(var(--primary-color-rgb), 0.1) 0%, rgba(var(--primary-color-rgb), 0) 70%);
        border-radius: 50%;
        position: relative;
        display: flex;
        align-items: center;
        justify-content: center;

        &::before {
          content: '';
          position: absolute;
          width: 200px;
          height: 200px;
          border-radius: 50%;
          border: 2px dashed rgba(var(--primary-color-rgb), 0.3);
          animation: rotate 20s linear infinite;
        }

        .floating-icons {
          position: absolute;
          width: 100%;
          height: 100%;

          .floating-icon {
            position: absolute;
            width: 60px;
            height: 60px;
            background-color: white;
            border-radius: 50%;
            box-shadow: var(--shadow-md);
            display: flex;
            align-items: center;
            justify-content: center;
            animation: float 6s infinite ease-in-out;

            .el-icon {
              font-size: 30px;
              color: var(--primary-color);
            }

            &.icon-1 {
              top: 20%;
              left: 20%;
              animation-delay: 0s;
            }

            &.icon-2 {
              top: 70%;
              left: 30%;
              animation-delay: 1.5s;
            }

            &.icon-3 {
              top: 30%;
              right: 20%;
              animation-delay: 3s;
            }

            &.icon-4 {
              top: 60%;
              right: 25%;
              animation-delay: 4.5s;
            }
          }
        }
      }
    }
  }

  .features-section {
    margin-bottom: var(--spacing-12);
    text-align: center;

    .section-title {
      font-size: 32px;
      font-weight: 700;
      color: var(--neutral-900);
      margin-bottom: var(--spacing-2);
    }

    .section-description {
      font-size: 18px;
      color: var(--neutral-600);
      max-width: 600px;
      margin: 0 auto var(--spacing-8);
    }

    .features-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: var(--spacing-6);

      .feature-card {
        cursor: pointer;
        transition: all 0.3s ease;
        height: 100%;
        border-radius: var(--border-radius-lg);
        overflow: hidden;

        &:hover {
          transform: translateY(-8px);
          box-shadow: var(--shadow-lg);

          .feature-icon {
            transform: scale(1.1);
          }
        }

        .feature-icon {
          width: 70px;
          height: 70px;
          margin: 0 auto var(--spacing-4);
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          transition: all 0.3s ease;

          .el-icon {
            font-size: 32px;
            color: white;
          }

          &.voice-icon {
            background: linear-gradient(45deg, #4a6cf7, #2079ff);
          }

          &.courseware-icon {
            background: linear-gradient(45deg, #f56c6c, #f78989);
          }

          &.audio-icon {
            background: linear-gradient(45deg, #67c23a, #95d475);
          }

          &.sample-icon {
            background: linear-gradient(45deg, #e6a23c, #f0c78a);
          }
        }

        .feature-title {
          font-size: 18px;
          font-weight: 600;
          color: var(--neutral-800);
          margin-bottom: var(--spacing-2);
        }

        .feature-description {
          font-size: 14px;
          color: var(--neutral-600);
          line-height: 1.6;
        }
      }
    }
  }

  .cta-section {
    position: relative;
    background: linear-gradient(135deg, var(--primary-color), var(--primary-dark));
    border-radius: var(--border-radius-lg);
    padding: var(--spacing-10) var(--spacing-6);
    color: white;
    overflow: hidden;
    text-align: center;

    .cta-content {
      position: relative;
      z-index: 2;
      max-width: 600px;
      margin: 0 auto;

      .cta-title {
        font-size: 32px;
        font-weight: 700;
        margin-bottom: var(--spacing-4);
      }

      .cta-description {
        font-size: 18px;
        opacity: 0.9;
        margin-bottom: var(--spacing-6);
      }

      .cta-button {
        padding: var(--spacing-3) var(--spacing-6);
        font-size: 16px;
        background-color: white;
        color: var(--primary-color);
        border: none;
        font-weight: 600;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-3px);
          box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
        }
      }
    }

    .wave-animation {
      position: absolute;
      bottom: 0;
      left: 0;
      width: 100%;
      height: 80px;
      overflow: hidden;

      .wave {
        position: absolute;
        bottom: 0;
        left: 0;
        width: 200%;
        height: 100%;
        background: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320"><path fill="white" fill-opacity="0.3" d="M0,192L48,186.7C96,181,192,171,288,154.7C384,139,480,117,576,138.7C672,160,768,224,864,234.7C960,245,1056,203,1152,170.7C1248,139,1344,117,1392,106.7L1440,96L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"></path></svg>');
        background-repeat: repeat-x;
        background-size: 1440px 80px;
        animation: wave 20s linear infinite;
        opacity: 0.3;

        &:nth-child(2) {
          bottom: -5px;
          animation: wave 18s linear reverse infinite;
          opacity: 0.5;
        }

        &:nth-child(3) {
          bottom: -10px;
          animation: wave 15s linear infinite;
          opacity: 0.2;
        }
      }
    }
  }
}

@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0; }
}

@keyframes rotate {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-15px);
  }
}

@keyframes wave {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(-50%);
  }
}
</style>

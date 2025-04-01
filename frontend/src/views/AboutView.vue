<!-- src/views/AboutView.vue -->
<template>
  <div class="about-container">
    <!-- 顶部动态背景 -->
    <div class="animated-background">
      <div class="wave wave1"></div>
      <div class="wave wave2"></div>
      <div class="wave wave3"></div>
      <div class="wave wave4"></div>
    </div>

    <!-- Interactive particles -->
    <div class="interactive-particles">
      <div
        v-for="(particle, index) in particles"
        :key="index"
        class="particle"
        :style="{
          left: `${particle.x}%`,
          top: `${particle.y}%`,
          width: `${particle.size}px`,
          height: `${particle.size}px`,
          backgroundColor: particle.color,
          opacity: particle.opacity,
        }"
      ></div>
    </div>

    <!-- Mouse follower light effect -->
    <div
      class="mouse-light"
      :style="{
        left: `${mouseX * 100}%`,
        top: `${mouseY * 100}%`,
        opacity: scrollY > 300 ? 0.15 : 0.25
      }"
    ></div>

    <section class="about-hero">
      <div
        class="hero-content parallax-element"
        v-motion-fade-visible
        :style="{
          transform: `translateY(${scrollY * -0.1}px)`
        }"
      >
        <h1 class="gradient-text">关于 AI-Voice</h1>
        <p>
          AI-Voice 是一款基于人工智能语音合成和声音克隆技术的教学声音处理软件。
          我们致力于通过个性化的语音讲解、标准语言输出、有声课件制作等功能，
          提高教学效果、增强学习体验、促进教育公平。
        </p>
      </div>

      <div class="decorative-circles">
        <div class="circle circle1"></div>
        <div class="circle circle2"></div>
        <div class="circle circle3"></div>
      </div>
    </section>

    <section class="about-vision">
      <div class="vision-container">
        <div
          class="vision-card parallax-element"
          v-motion-slide-visible-left
          :style="{
            transform: `translateX(${(mouseX - 0.5) * -20}px) translateY(${(mouseY - 0.5) * -10}px)`,
            transition: 'transform 0.3s ease-out'
          }"
        >
          <div class="vision-icon">
            <el-icon><Aim /></el-icon>
          </div>
          <h2>我们的愿景</h2>
          <p>打造世界一流的AI语音解决方案，让教育更加普惠，让学习更加高效。</p>
        </div>
        <div
          class="vision-card parallax-element"
          v-motion-slide-visible-bottom
          :style="{
            transform: `translateY(${(mouseY - 0.5) * -15}px)`,
            transition: 'transform 0.3s ease-out'
          }"
        >
          <div class="vision-icon">
            <el-icon><Medal /></el-icon>
          </div>
          <h2>我们的使命</h2>
          <p>通过AI技术赋能教育，让每个学生都能获得个性化的学习体验和高质量的教学资源。</p>
        </div>
        <div
          class="vision-card parallax-element"
          v-motion-slide-visible-right
          :style="{
            transform: `translateX(${(mouseX - 0.5) * 20}px) translateY(${(mouseY - 0.5) * -10}px)`,
            transition: 'transform 0.3s ease-out'
          }"
        >
          <div class="vision-icon">
            <el-icon><Star /></el-icon>
          </div>
          <h2>我们的价值观</h2>
          <p>创新、品质、用户至上，持续不断地探索AI技术在教育领域的应用与突破。</p>
        </div>
      </div>

      <div class="decorative-shapes">
        <div class="shape shape1"></div>
        <div class="shape shape2"></div>
        <div class="shape shape3"></div>
      </div>
    </section>

    <section class="about-carousel">
      <h2 class="section-title" v-motion-fade-visible>我们的核心功能</h2>
      <el-carousel
        height="400px"
        indicator-position="outside"
        class="feature-carousel"
        :interval="5000"
        :autoplay="true"
        arrow="always"
        type="card">
        <el-carousel-item v-for="item in carouselItems" :key="item.id" class="carousel-item">
          <div
            class="carousel-item-content"
            :style="{ backgroundColor: item.backgroundColor }"
            v-motion-pop-visible>
            <div class="icon-container">
            <el-icon :size="80" color="#fff"><component :is="item.icon" /></el-icon>
            </div>
            <h3>{{ item.title }}</h3>
            <p>{{ item.description }}</p>
            <el-button class="learn-more-btn" type="text" @click="gotoLogin">了解更多</el-button>
          </div>
        </el-carousel-item>
      </el-carousel>
    </section>

    <section class="about-timeline">
      <h2 class="section-title" v-motion-fade-visible>我们的发展历程</h2>
      <div class="timeline-container">
        <div
          v-for="(_, index) in 4"
          :key="index"
          class="timeline-item"
          v-motion-slide-visible-bottom
          :delay="index * 100 + 100"
        >
          <div class="timeline-dot"></div>
          <div class="timeline-content" :class="{ 'animate-pulse': index === 0 }">
            <div class="timeline-date">
              <template v-if="index === 0">2025年1月19日</template>
              <template v-if="index === 1">2025年3月2日</template>
              <template v-if="index === 2">2025年3月29日</template>
              <template v-if="index === 3">2026年</template>
            </div>
            <h3>
              <template v-if="index === 0">项目启动</template>
              <template v-if="index === 1">技术突破</template>
              <template v-if="index === 2">产品发布</template>
              <template v-if="index === 3">未来展望</template>
            </h3>
            <p>
              <template v-if="index === 0">AI-Voice项目团队成立，开始研发基础语音合成技术</template>
              <template v-if="index === 1">完成多语言语音合成引擎的开发，支持多种语言</template>
              <template v-if="index === 2">AI-Voice 1.0版本正式发布</template>
              <template v-if="index === 3">AI-Voice 2.0版本即将发布，引入更多创新功能和体验优化</template>
            </p>
          </div>
        </div>
      </div>
    </section>

    <section class="about-contact">
      <div class="contact-card" v-motion-slide-visible-bottom>
        <div class="animated-circles">
          <div class="circle" v-for="i in 5" :key="i"></div>
        </div>

        <h2>联系我们</h2>
        <p>我们期待与您合作，共同探索AI语音技术在教育领域的无限可能</p>
        <div class="contact-details">
          <div class="contact-item">
            <el-icon><Location /></el-icon>
            <span>地址：武汉市洪山区武汉理工大学</span>
          </div>
          <div class="contact-item">
            <el-icon><Phone /></el-icon>
            <span>电话：520-1314-520</span>
          </div>
          <div class="contact-item">
            <el-icon><Message /></el-icon>
            <span>邮箱：contact@ai-voice.com</span>
          </div>
        </div>
        <el-button
          type="primary"
          size="large"
          class="contact-btn"
          :style="{
            transform: `translateY(${(mouseY - 0.5) * -5}px)`
          }"
        >
          立即咨询
        </el-button>
      </div>
    </section>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { Edit, Mic, Document, VideoCamera, Aim, Medal, Star, Location, Phone, Message } from '@element-plus/icons-vue';
import { useMotions } from '@vueuse/motion';
import { useRouter } from 'vue-router';

const {
  vMotionFadeVisible,
  vMotionSlideVisibleLeft,
  vMotionSlideVisibleRight,
  vMotionSlideVisibleBottom,
  vMotionPopVisible
} = useMotions();

const router = useRouter();
const gotoLogin = () => router.push('/login');

// Mouse position for interactive elements
const mouseX = ref(0);
const mouseY = ref(0);
const windowWidth = ref(window.innerWidth);
const windowHeight = ref(window.innerHeight);

// Parallax effect settings
const scrollY = ref(0);
const parallaxItems = ref<HTMLElement[]>([]);

// Particle interface
interface Particle {
  x: number;
  y: number;
  size: number;
  speedX: number;
  speedY: number;
  opacity: number;
  color: string;
}

// Create particles
const particlesCount = 20;
const particles = ref<Particle[]>([]);

for (let i = 0; i < particlesCount; i++) {
  particles.value.push({
    x: Math.random() * 100,
    y: Math.random() * 100,
    size: Math.random() * 10 + 5,
    speedX: (Math.random() - 0.5) * 0.3,
    speedY: (Math.random() - 0.5) * 0.3,
    opacity: Math.random() * 0.5 + 0.1,
    color: `hsl(${Math.random() * 30 + 200}, 70%, 60%)`,
  });
}

// Handle mouse movement for interactive effects
const handleMouseMove = (event: MouseEvent) => {
  mouseX.value = event.clientX / windowWidth.value;
  mouseY.value = event.clientY / windowHeight.value;
};

// Handle scroll for parallax effects
const handleScroll = () => {
  scrollY.value = window.scrollY;
};

// Handle window resize
const handleResize = () => {
  windowWidth.value = window.innerWidth;
  windowHeight.value = window.innerHeight;
};

// Animation frame for particles
let animationFrameId: number | null = null;
const animateParticles = () => {
  particles.value.forEach(p => {
    p.x += p.speedX;
    p.y += p.speedY;

    // Bounce off edges
    if (p.x < 0 || p.x > 100) p.speedX *= -1;
    if (p.y < 0 || p.y > 100) p.speedY *= -1;
  });

  animationFrameId = requestAnimationFrame(animateParticles);
};

const carouselItems = ref([
  {
    id: 1,
    title: '个性化语音讲解',
    description: '上传或输入文本，选择声音样本进行语音讲解，支持情感与语调调整。',
    icon: Edit,
    backgroundColor: 'var(--primary-color)',
  },
  {
    id: 2,
    title: '标准语言输出',
    description: '输出普通话、英文等标准发音，可控制语速、语气和节奏，适合语言学习。',
    icon: Mic,
    backgroundColor: 'var(--success-color)',
  },
  {
    id: 3,
    title: '课件制作下载',
    description: '上传课件，选择声音样本，完成有声课件的制作和下载，提升学习效果。',
    icon: Document,
    backgroundColor: 'var(--warning-color)',
  },
  {
    id: 4,
    title: '声音置换及加字幕',
    description: '导入音视频，通过更换声音实现变声，同步展示字幕，实现多语种转换。',
    icon: VideoCamera,
    backgroundColor: 'var(--danger-color)',
  },
]);

onMounted(() => {
  window.addEventListener('mousemove', handleMouseMove);
  window.addEventListener('scroll', handleScroll);
  window.addEventListener('resize', handleResize);

  // Start particle animation
  animateParticles();

  // Setup intersection observer for scroll animations
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('in-view');
      }
    });
  }, {
    threshold: 0.1
  });

  // Observe sections
  document.querySelectorAll('section').forEach(section => {
    observer.observe(section);
  });

  // Collect parallax elements
  document.querySelectorAll('.parallax-element').forEach(el => {
    parallaxItems.value.push(el as HTMLElement);
  });
});

onBeforeUnmount(() => {
  window.removeEventListener('mousemove', handleMouseMove);
  window.removeEventListener('scroll', handleScroll);
  window.removeEventListener('resize', handleResize);

  // Stop animation
  if (animationFrameId) {
    cancelAnimationFrame(animationFrameId);
  }
});
</script>

<style lang="scss" scoped>
.about-container {
  position: relative;
  padding: 0;
  overflow: hidden;
}

/* Interactive elements */
.interactive-particles {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 2;
  overflow: hidden;

  .particle {
    position: absolute;
    border-radius: 50%;
    background-color: rgba(var(--primary-color-rgb), 0.5);
    will-change: transform;
    pointer-events: none;
    filter: blur(1px);
  }
}

.mouse-light {
  position: fixed;
  width: 400px;
  height: 400px;
  margin-left: -200px;
  margin-top: -200px;
  background: radial-gradient(circle, rgba(var(--primary-color-rgb), 0.15) 0%, rgba(var(--primary-color-rgb), 0.05) 40%, transparent 70%);
  border-radius: 50%;
  pointer-events: none;
  z-index: 1;
  will-change: transform;
}

/* Decorative elements */
.decorative-circles {
  position: absolute;
  top: 0;
  right: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  pointer-events: none;
  z-index: -1;

  .circle {
    position: absolute;
    border-radius: 50%;
    opacity: 0.1;

    &.circle1 {
      width: 300px;
      height: 300px;
      top: -50px;
      right: -50px;
      background: linear-gradient(135deg, var(--primary-light), var(--primary-color));
      animation: float-rotate 20s infinite ease-in-out;
    }

    &.circle2 {
      width: 200px;
      height: 200px;
      bottom: 100px;
      left: 10%;
      background: var(--primary-color);
      animation: float-rotate 15s infinite ease-in-out reverse;
    }

    &.circle3 {
      width: 150px;
      height: 150px;
      top: 40%;
      right: 15%;
      background: var(--primary-light);
      animation: float-rotate 12s infinite ease-in-out;
    }
  }
}

.decorative-shapes {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: -1;

  .shape {
    position: absolute;
    opacity: 0.07;

    &.shape1 {
      width: 150px;
      height: 150px;
      border-radius: 30px;
      background: var(--primary-color);
      top: 10%;
      left: 5%;
      transform: rotate(30deg);
      animation: float-shape 15s infinite ease-in-out;
    }

    &.shape2 {
      width: 80px;
      height: 80px;
      background: var(--primary-dark);
      top: 70%;
      right: 10%;
      transform: rotate(45deg);
      animation: float-shape 12s infinite ease-in-out 2s;
    }

    &.shape3 {
      width: 120px;
      height: 120px;
      border-radius: 24px;
      background: var(--primary-light);
      bottom: 15%;
      left: 30%;
      transform: rotate(60deg);
      animation: float-shape 18s infinite ease-in-out 1s;
    }
  }
}

/* 动态背景波浪 */
.animated-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 500px;
  overflow: hidden;
  z-index: -1;

  .wave {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 200%;
    height: 100px;
    background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1440 320'%3E%3Cpath fill='%230099ff' fill-opacity='0.1' d='M0,192L48,197.3C96,203,192,213,288,229.3C384,245,480,267,576,250.7C672,235,768,181,864,181.3C960,181,1056,235,1152,234.7C1248,235,1344,181,1392,154.7L1440,128L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z'%3E%3C/path%3E%3C/svg%3E") repeat-x;
    background-size: 100% 100px;
    animation: wave 15s linear infinite;
  }

  .wave1 {
    animation: wave 10s linear infinite;
    z-index: 1;
    opacity: 0.3;
    animation-delay: 0s;
    bottom: 0;
  }

  .wave2 {
    animation: wave 8s linear infinite;
    z-index: 2;
    opacity: 0.5;
    animation-delay: -5s;
    bottom: 10px;
  }

  .wave3 {
    animation: wave 5s linear infinite;
    z-index: 3;
    opacity: 0.2;
    animation-delay: -2s;
    bottom: 15px;
  }

  .wave4 {
    animation: wave 7s linear infinite;
    z-index: 4;
    opacity: 0.7;
    animation-delay: -5s;
    bottom: 20px;
  }

  @keyframes wave {
    0% {
      transform: translateX(0);
    }
    100% {
      transform: translateX(-50%);
    }
  }
}

/* Animation keyframes */
@keyframes float-rotate {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  25% {
    transform: translateY(-15px) rotate(5deg);
  }
  50% {
    transform: translateY(10px) rotate(10deg);
  }
  75% {
    transform: translateY(-5px) rotate(5deg);
  }
}

@keyframes float-shape {
  0%, 100% {
    transform: translateY(0) rotate(30deg);
  }
  50% {
    transform: translateY(-20px) rotate(45deg);
  }
}

@keyframes pulse-glow {
  0%, 100% {
    box-shadow: 0 0 20px rgba(var(--primary-color-rgb), 0.2);
  }
  50% {
    box-shadow: 0 0 30px rgba(var(--primary-color-rgb), 0.4);
  }
}

@keyframes orbit {
  0% {
    transform: rotate(0deg) translateX(80px) rotate(0deg);
  }
  100% {
    transform: rotate(360deg) translateX(80px) rotate(-360deg);
  }
}

.animate-pulse {
  animation: pulse-glow 3s infinite ease-in-out;
}

/* Section transition effect for scrolling */
section {
  transition: transform 0.5s ease, opacity 0.5s ease;
  transform: translateY(30px);
  opacity: 0;

  &.in-view {
    transform: translateY(0);
    opacity: 1;
  }
}

.parallax-element {
  transition: transform 0.3s ease-out;
  will-change: transform;
}

.section-title {
  text-align: center;
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 3rem;
  background: linear-gradient(90deg, var(--primary-color), var(--primary-light));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  position: relative;
  display: inline-block;
  left: 50%;
  transform: translateX(-50%);

  &::after {
    content: '';
    position: absolute;
    bottom: -10px;
    left: 50%;
    transform: translateX(-50%);
    width: 80px;
    height: 4px;
    background: linear-gradient(90deg, var(--primary-color), var(--primary-light));
    border-radius: 2px;
  }
}

.about-hero {
  padding: 120px 0 80px;
  text-align: center;
  position: relative;

  .hero-content {
    max-width: 800px;
    margin: 0 auto;
    padding: 40px;
    background: rgba(255, 255, 255, 0.8);
    backdrop-filter: blur(10px);
    border-radius: var(--border-radius-lg);
    box-shadow: var(--shadow-lg);
    transition: transform 0.5s ease, box-shadow 0.5s ease;

    &:hover {
      transform: translateY(-5px) !important;
      box-shadow: var(--shadow-xl);
    }

    h1 {
      font-size: 3.5rem;
      font-weight: 700;
      margin-bottom: 1.5rem;
      line-height: 1.2;
    }

    .gradient-text {
      background: linear-gradient(90deg, var(--primary-color), var(--primary-light));
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }

    p {
      font-size: 1.25rem;
      line-height: 1.8;
      color: var(--neutral-700);
    }
  }
}

.about-vision {
  padding: 80px 20px;
  background-color: var(--neutral-50);
  position: relative;

  .vision-container {
    display: flex;
    gap: 30px;
    max-width: 1200px;
    margin: 0 auto;
    position: relative;
    z-index: 1;

    @media (max-width: 992px) {
      flex-direction: column;
    }

    .vision-card {
      flex: 1;
      background: white;
      border-radius: var(--border-radius-lg);
      padding: 40px 30px;
      text-align: center;
      box-shadow: var(--shadow-md);
      transition: all 0.4s ease;
      overflow: hidden;
      position: relative;

      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: linear-gradient(135deg, rgba(var(--primary-color-rgb), 0.05) 0%, transparent 100%);
        opacity: 0;
        transition: opacity 0.4s ease;
      }

      &:hover {
        box-shadow: var(--shadow-lg);

        &::before {
          opacity: 1;
        }

        .vision-icon {
          transform: rotateY(360deg);
        }
      }

      .vision-icon {
        width: 80px;
        height: 80px;
        background: linear-gradient(135deg, var(--primary-light) 0%, var(--primary-color) 100%);
        border-radius: 50%;
        display: flex;
        justify-content: center;
        align-items: center;
        margin: 0 auto 20px;
        transition: transform 0.8s ease;

        .el-icon {
          font-size: 40px;
          color: white;
        }
      }

      h2 {
        font-size: 1.75rem;
        font-weight: 600;
        margin-bottom: 15px;
        color: var(--neutral-900);
      }

      p {
        font-size: 1rem;
        line-height: 1.6;
        color: var(--neutral-600);
      }
    }
  }
}

.about-carousel {
  padding: 80px 20px 100px;

  .feature-carousel {
    max-width: 1200px;
    margin: 0 auto;

    .carousel-item {
      transition: transform 0.5s ease;

      &:hover {
        transform: translateY(-10px);
      }
    }

    .carousel-item-content {
      height: 100%;
      padding: 40px 30px;
      text-align: center;
      color: white;
      border-radius: var(--border-radius-lg);
      box-shadow: var(--shadow-lg);
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      transition: transform 0.3s ease;
      position: relative;
      overflow: hidden;

      &::before {
        content: '';
        position: absolute;
        top: -50%;
        left: -50%;
        width: 200%;
        height: 200%;
        background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 60%);
        opacity: 0;
        transition: all 0.4s ease;
      }

      &:hover::before {
        opacity: 1;
      }

      .icon-container {
        margin-bottom: 25px;
        animation: float 3s ease-in-out infinite;
      }

      @keyframes float {
        0% { transform: translateY(0px); }
        50% { transform: translateY(-10px); }
        100% { transform: translateY(0px); }
      }

      h3 {
        font-size: 1.75rem;
        font-weight: 600;
        margin-bottom: 20px;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
      }

      p {
        font-size: 1.1rem;
        line-height: 1.6;
        margin-bottom: 30px;
        max-width: 360px;
      }

      .learn-more-btn {
        color: white;
        font-weight: 500;
        font-size: 1rem;
        border: 2px solid rgba(255, 255, 255, 0.5);
        border-radius: var(--border-radius-md);
        padding: 8px 24px;
        transition: all 0.3s ease;
        position: relative;
        overflow: hidden;
        z-index: 1;

        &::before {
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          width: 0;
          height: 100%;
          background-color: rgba(255, 255, 255, 0.2);
          transition: width 0.3s ease;
          z-index: -1;
        }

        &:hover {
          border-color: white;
          transform: scale(1.05);

          &::before {
            width: 100%;
          }
        }
      }
    }
  }
}

.about-timeline {
  padding: 80px 20px;
  background-color: white;

  .timeline-container {
    position: relative;
    max-width: 800px;
    margin: 0 auto;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 50%;
      transform: translateX(-50%);
      width: 4px;
      height: 100%;
      background: linear-gradient(to bottom, var(--primary-color), var(--primary-light));
      border-radius: 2px;

      @media (max-width: 768px) {
        left: 30px;
      }
    }

    .timeline-item {
      position: relative;
      margin-bottom: 60px;

      &:last-child {
        margin-bottom: 0;
      }

      .timeline-dot {
        position: absolute;
        top: 0;
        left: 50%;
        transform: translateX(-50%);
        width: 24px;
        height: 24px;
        background-color: white;
        border: 4px solid var(--primary-color);
        border-radius: 50%;
        z-index: 1;
        transition: all 0.3s ease;

        @media (max-width: 768px) {
          left: 30px;
        }
      }

      &:hover .timeline-dot {
        transform: translateX(-50%) scale(1.2);
        box-shadow: 0 0 15px rgba(var(--primary-color-rgb), 0.5);
      }

      .timeline-content {
        position: relative;
        width: calc(50% - 50px);
        padding: 25px;
        background-color: white;
        border-radius: var(--border-radius-lg);
        box-shadow: var(--shadow-md);
        transition: transform 0.3s ease, box-shadow 0.3s ease;

        &:hover {
          transform: scale(1.03);
          box-shadow: var(--shadow-lg);
        }

        @media (max-width: 768px) {
          width: calc(100% - 80px);
          margin-left: 80px;
        }

        .timeline-date {
          display: inline-block;
          padding: 5px 15px;
          background: linear-gradient(90deg, var(--primary-color), var(--primary-light));
          color: white;
          font-weight: 600;
          border-radius: var(--border-radius-md);
          margin-bottom: 15px;
        }

        h3 {
          font-size: 1.3rem;
          font-weight: 600;
          margin-bottom: 10px;
          color: var(--neutral-900);
        }

        p {
          font-size: 1rem;
      line-height: 1.6;
          color: var(--neutral-600);
        }
      }

      &:nth-child(odd) {
        .timeline-content {
          margin-left: auto;

          @media (max-width: 768px) {
            margin-left: 80px;
          }

          &::before {
            content: '';
            position: absolute;
            top: 10px;
            left: -15px;
            width: 0;
            height: 0;
            border-top: 15px solid transparent;
            border-bottom: 15px solid transparent;
            border-right: 15px solid white;

            @media (max-width: 768px) {
              left: -15px;
              border-right: 15px solid white;
              border-left: none;
            }
          }
        }
      }

      &:nth-child(even) {
        .timeline-content {
          &::before {
            content: '';
            position: absolute;
            top: 10px;
            right: -15px;
            width: 0;
            height: 0;
            border-top: 15px solid transparent;
            border-bottom: 15px solid transparent;
            border-left: 15px solid white;

            @media (max-width: 768px) {
              left: -15px;
              border-right: 15px solid white;
              border-left: none;
              right: auto;
            }
          }
        }
      }
    }
  }
}

.about-contact {
  padding: 80px 20px 100px;

  .contact-card {
    max-width: 800px;
    margin: 0 auto;
    background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);
    border-radius: var(--border-radius-lg);
    padding: 60px 40px;
    text-align: center;
    color: white;
    box-shadow: var(--shadow-lg);
    position: relative;
    overflow: hidden;

    .animated-circles {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;

      .circle {
        position: absolute;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.1);

        &:nth-child(1) {
          width: 100px;
          height: 100px;
          top: 10%;
          left: 10%;
          animation: orbit 20s linear infinite;
        }

        &:nth-child(2) {
          width: 50px;
          height: 50px;
          top: 20%;
          right: 15%;
          animation: orbit 15s linear infinite reverse;
        }

        &:nth-child(3) {
          width: 70px;
          height: 70px;
          bottom: 15%;
          right: 10%;
          animation: orbit 25s linear infinite;
        }

        &:nth-child(4) {
          width: 40px;
          height: 40px;
          bottom: 20%;
          left: 15%;
          animation: orbit 18s linear infinite reverse;
        }

        &:nth-child(5) {
          width: 60px;
          height: 60px;
          top: 50%;
          left: 50%;
          animation: orbit 30s linear infinite;
        }
      }
    }

    &::before {
      content: '';
      position: absolute;
      top: -50%;
      left: -50%;
      width: 200%;
      height: 200%;
      background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 60%);
      animation: rotate 30s linear infinite;
      z-index: 0;
    }

    @keyframes rotate {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }

    h2 {
      font-size: 2.5rem;
      font-weight: 700;
      margin-bottom: 20px;
      position: relative;
      z-index: 1;
    }

    p {
      font-size: 1.2rem;
      line-height: 1.6;
      margin-bottom: 40px;
      max-width: 600px;
      margin-left: auto;
      margin-right: auto;
      opacity: 0.9;
      position: relative;
      z-index: 1;
    }

    .contact-details {
      display: flex;
      flex-direction: column;
      gap: 20px;
      margin-bottom: 40px;
      position: relative;
      z-index: 1;

      .contact-item {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
        transition: transform 0.3s ease;

        &:hover {
          transform: translateX(5px);
        }

        .el-icon {
          font-size: 20px;
        }

        span {
          font-size: 1.1rem;
        }
      }
    }

    .contact-btn {
      position: relative;
      z-index: 1;
      min-width: 200px;
      height: 50px;
      font-size: 1.1rem;
        font-weight: 500;
      background-color: white;
      color: var(--primary-color);
      border: none;
      transition: all 0.3s ease;
      overflow: hidden;

      &::after {
        content: '';
        position: absolute;
        top: -50%;
        left: -50%;
        width: 200%;
        height: 200%;
        background: radial-gradient(circle, rgba(var(--primary-color-rgb), 0.1) 0%, transparent 70%);
        opacity: 0;
        transform: scale(0.5);
        transition: all 0.4s ease;
        z-index: -1;
      }

      &:hover {
        transform: translateY(-5px) !important;
        box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);

        &::after {
          opacity: 1;
          transform: scale(1);
        }
      }
    }
  }
}
</style>

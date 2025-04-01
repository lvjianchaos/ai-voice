<script lang="ts" setup>
import { User, Opportunity, Microphone, Tools, Document, Trophy } from '@element-plus/icons-vue'
import { ref, onMounted, onBeforeUnmount } from 'vue'
import router from '@/router'

const goToLogin = () => router.push('/login')
const goToSignUp = () => router.push('/register')
const goToAbout = () => router.push('/about')

// Refs for intersection observer
const heroSection = ref(null)
const featuresSection = ref(null)
const howItWorksSection = ref(null)
const ctaSection = ref(null)

// Refs for feature cards
const featureCards = ref([])

// Refs for steps
const stepItems = ref([])

// Scroll position for parallax effect
const scrollY = ref(0)
const mousePosX = ref(0)
const mousePosY = ref(0)

// Handles mouse movement for interactive effects
const handleMouseMove = (event) => {
  mousePosX.value = event.clientX / window.innerWidth
  mousePosY.value = event.clientY / window.innerHeight
}

// Handles scroll for parallax effects
const handleScroll = () => {
  scrollY.value = window.scrollY
}

// Initialize intersection observers for scroll animations
const initObservers = () => {
  const options = {
    root: null,
    rootMargin: '0px',
    threshold: 0.1,
  }

  const sectionObserver = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add('visible')
      }
    })
  }, options)

  // Observe sections
  if (heroSection.value) sectionObserver.observe(heroSection.value)
  if (featuresSection.value) sectionObserver.observe(featuresSection.value)
  if (howItWorksSection.value) sectionObserver.observe(howItWorksSection.value)
  if (ctaSection.value) sectionObserver.observe(ctaSection.value)

  // Observe feature cards with delay
  if (featureCards.value.length) {
    featureCards.value.forEach((card, index) => {
      setTimeout(() => {
        sectionObserver.observe(card)
      }, index * 150)
    })
  }

  // Observe step items with delay
  if (stepItems.value.length) {
    stepItems.value.forEach((step, index) => {
      setTimeout(() => {
        sectionObserver.observe(step)
      }, index * 200)
    })
  }
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  window.addEventListener('mousemove', handleMouseMove)

  // Allow time for DOM to render before initializing observers
  setTimeout(() => {
    initObservers()
  }, 100)
})

onBeforeUnmount(() => {
  window.removeEventListener('scroll', handleScroll)
  window.removeEventListener('mousemove', handleMouseMove)
})
</script>

<template>
  <div class="home-page">
    <!-- Hero Section -->
    <section class="hero-section" ref="heroSection">
      <div class="parallax-bg" :style="{ transform: `translateY(${scrollY * 0.4}px)` }"></div>
      <div class="floating-particles">
        <div class="particle" v-for="i in 10" :key="i"></div>
      </div>
      <div class="hero-content animate-element">
        <h1 class="hero-title">AI 赋能教育创新</h1>
        <p class="hero-description">
          智能语音解决方案助力教育数字化转型，打造个性化学习体验
        </p>
        <div class="hero-actions">
        <el-button
          type="primary"
          size="large"
            class="hero-button login-btn"
          @click="goToLogin"
        >
          <el-icon><User /></el-icon>
          立即登录
        </el-button>
        <el-button
            type="primary"
          size="large"
            class="hero-button signup-btn"
          @click="goToSignUp"
        >
          <el-icon><Opportunity /></el-icon>
          免费注册
        </el-button>
      </div>
        <div class="hero-stats">
          <div class="stat-item">
            <div class="stat-value">10,000+</div>
            <div class="stat-label">活跃用户</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">50+</div>
            <div class="stat-label">支持语言</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">99.9%</div>
            <div class="stat-label">服务可靠性</div>
          </div>
        </div>
      </div>
      <div class="hero-image animate-element">
        <img src="@/assets/hero.png" alt="AI Voice"
          :style="{ transform: `translate(${(mousePosX - 0.5) * 20}px, ${(mousePosY - 0.5) * 20}px)` }" />
      </div>
      <div class="scroll-indicator">
        <div class="mouse">
          <div class="wheel"></div>
        </div>
        <div class="scroll-text">向下滚动探索更多</div>
      </div>
    </section>

    <!-- Features Section -->
    <section class="features-section" id="features" ref="featuresSection">
      <div class="section-header animate-element">
        <h2 class="section-title">特色功能</h2>
        <p class="section-description">
          AI-Voice 提供全面的语音解决方案，满足教育行业的多样化需求
        </p>
      </div>

      <div class="features-grid">
        <div v-for="(feature, index) in 4" :key="index"
          class="feature-card animate-element"
          ref="el => featureCards[index] = el">
          <div class="feature-icon">
            <el-icon v-if="index === 0"><Microphone /></el-icon>
            <el-icon v-if="index === 1"><Tools /></el-icon>
            <el-icon v-if="index === 2"><Document /></el-icon>
            <el-icon v-if="index === 3"><Trophy /></el-icon>
          </div>
          <h3 class="feature-title">
            <template v-if="index === 0">多样化声音样本</template>
            <template v-if="index === 1">个性化语音合成</template>
            <template v-if="index === 2">课件制作工具</template>
            <template v-if="index === 3">音视频处理</template>
          </h3>
          <p class="feature-description">
            <template v-if="index === 0">提供丰富的声音样本库，满足不同场景和角色的需求，支持多种语言和口音</template>
            <template v-if="index === 1">基于AI深度学习，生成自然流畅的语音，支持情感表达和语调调整</template>
            <template v-if="index === 2">一站式课件制作工具，轻松将文本转换为语音内容，支持多种格式导出</template>
            <template v-if="index === 3">智能声音置换和自动字幕生成，提高内容制作效率，增强学习体验</template>
          </p>
        </div>
      </div>
    </section>

    <!-- How It Works Section -->
    <section class="how-it-works-section" id="how-it-works" ref="howItWorksSection">
      <div class="section-header animate-element">
        <h2 class="section-title">使用方法</h2>
        <p class="section-description">
          简单几步，轻松创建高质量的教学语音内容
        </p>
      </div>

      <div class="steps-container">
        <div v-for="(step, index) in 4" :key="index"
          class="step-item animate-element"
          ref="el => stepItems[index] = el">
          <div class="step-number">0{{ index + 1 }}</div>
          <h3 class="step-title">
            <template v-if="index === 0">上传样本</template>
            <template v-if="index === 1">编辑内容</template>
            <template v-if="index === 2">生成语音</template>
            <template v-if="index === 3">应用场景</template>
          </h3>
          <p class="step-description">
            <template v-if="index === 0">上传您的声音样本或从样本库中选择合适的声音</template>
            <template v-if="index === 1">输入教学文本内容，调整语速、语调和情感参数</template>
            <template v-if="index === 2">系统自动合成高质量语音，可预览、编辑和导出</template>
            <template v-if="index === 3">将生成的语音应用于课件、视频、网站等多种场景</template>
          </p>
          <div v-if="index < 3" class="step-divider"></div>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="cta-section" ref="ctaSection">
      <div class="moving-waves">
        <div class="wave wave1"></div>
        <div class="wave wave2"></div>
        <div class="wave wave3"></div>
      </div>
      <div class="cta-content animate-element">
        <h2 class="cta-title">准备好开始了吗？</h2>
        <p class="cta-description">
          立即注册，体验AI语音技术为教育带来的革命性变化
        </p>
        <div class="cta-actions">
          <el-button type="primary" size="large" class="cta-button" @click="goToSignUp">
            免费注册
          </el-button>
          <el-button size="large" class="cta-button-secondary" @click="goToAbout">
            了解更多
        </el-button>
      </div>
    </div>
    </section>
  </div>
</template>

<style lang="scss" scoped>
.home-page {
  overflow-x: hidden;
}

/* Animations for scrolling */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(40px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(-40px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeInLeft {
  from {
    opacity: 0;
    transform: translateX(40px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

@keyframes wave {
  0% {
    transform: translateX(0) translateZ(0) scaleY(1);
  }
  50% {
    transform: translateX(-25%) translateZ(0) scaleY(0.8);
  }
  100% {
    transform: translateX(-50%) translateZ(0) scaleY(1);
  }
}

@keyframes float-particle {
  0% {
    transform: translateY(0) translateX(0) rotate(0);
  }
  33% {
    transform: translateY(-20px) translateX(10px) rotate(120deg);
  }
  66% {
    transform: translateY(-10px) translateX(-10px) rotate(240deg);
  }
  100% {
    transform: translateY(0) translateX(0) rotate(360deg);
  }
}

@keyframes scroll-wheel {
  0% {
    opacity: 1;
    transform: translateY(0);
  }
  100% {
    opacity: 0;
    transform: translateY(10px);
  }
}

/* Animation classes */
.animate-element {
  opacity: 0;
  transform: translateY(40px);
  transition: all 0.8s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

section.visible .animate-element,
.animate-element.visible {
  opacity: 1;
  transform: translateY(0);
}

/* Hero Section */
.hero-section {
  display: flex;
  align-items: center;
  padding: var(--spacing-8) var(--spacing-6);
  min-height: 90vh;
  background: linear-gradient(135deg, var(--neutral-50) 0%, white 100%);
  position: relative;
  overflow: hidden;
  gap: var(--spacing-10);
  transition: all 0.5s ease;

  @media (max-width: 1200px) {
    flex-direction: column;
    text-align: center;
    gap: var(--spacing-16);
    padding-bottom: var(--spacing-16);
  }

  .parallax-bg {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 130%;
    background: radial-gradient(ellipse at top left, rgba(var(--primary-color-rgb), 0.05) 0%, transparent 70%),
                radial-gradient(ellipse at bottom right, rgba(var(--primary-color-rgb), 0.05) 0%, transparent 70%);
    z-index: 0;
    transform: translateY(0);
    will-change: transform;
  }

  .floating-particles {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    overflow: hidden;
    z-index: 1;

    .particle {
      position: absolute;
      width: 10px;
      height: 10px;
      background: linear-gradient(135deg, var(--primary-light), var(--primary-color));
      border-radius: 50%;
      opacity: 0.3;
      animation: float-particle 15s infinite ease-in-out;

      &:nth-child(1) { top: 10%; left: 10%; animation-delay: 0s; }
      &:nth-child(2) { top: 20%; left: 80%; animation-delay: 1s; }
      &:nth-child(3) { top: 80%; left: 30%; animation-delay: 2s; }
      &:nth-child(4) { top: 40%; left: 60%; animation-delay: 3s; }
      &:nth-child(5) { top: 60%; left: 90%; animation-delay: 4s; }
      &:nth-child(6) { top: 30%; left: 40%; animation-delay: 5s; }
      &:nth-child(7) { top: 70%; left: 70%; animation-delay: 6s; }
      &:nth-child(8) { top: 80%; left: 20%; animation-delay: 7s; }
      &:nth-child(9) { top: 10%; left: 50%; animation-delay: 8s; }
      &:nth-child(10) { top: 50%; left: 5%; animation-delay: 9s; }
    }
  }

  .scroll-indicator {
    position: absolute;
    bottom: 30px;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    flex-direction: column;
    align-items: center;
    color: var(--neutral-600);
    opacity: 0.8;
    z-index: 2;

    .mouse {
      width: 26px;
      height: 40px;
      border: 2px solid var(--neutral-400);
      border-radius: 20px;
      margin-bottom: 8px;
      position: relative;

      .wheel {
        position: absolute;
        width: 4px;
        height: 8px;
        background: var(--primary-color);
        border-radius: 2px;
        left: 50%;
        top: 8px;
        transform: translateX(-50%);
        animation: scroll-wheel 1.5s infinite;
      }
    }

    .scroll-text {
      font-size: 0.8rem;
      opacity: 0.7;
    }
  }

  .hero-content {
    flex: 1;
    z-index: 2;
    padding: 0 var(--spacing-6);

    @media (max-width: 1200px) {
      padding: 0;
    }
  }

  .hero-title {
    font-size: 3.5rem;
    font-weight: 700;
    margin-bottom: var(--spacing-6);
    color: var(--neutral-900);
    line-height: 1.2;
    letter-spacing: -0.5px;
    position: relative;
    overflow: hidden;

    @media (max-width: 768px) {
      font-size: 2.5rem;
    }

    &::after {
      content: '';
      position: absolute;
      left: 0;
      bottom: -6px;
      width: 80px;
      height: 4px;
      background: linear-gradient(to right, var(--primary-color), var(--primary-light));
      border-radius: 2px;

      @media (max-width: 1200px) {
        left: 50%;
        transform: translateX(-50%);
      }
    }
  }

  .hero-description {
    font-size: 1.25rem;
    color: var(--neutral-700);
    margin-bottom: var(--spacing-8);
    max-width: 600px;
    line-height: 1.6;

    @media (max-width: 1200px) {
      margin-left: auto;
      margin-right: auto;
    }
  }

  .hero-actions {
    display: flex;
    gap: var(--spacing-4);
    margin-bottom: var(--spacing-10);

    @media (max-width: 768px) {
      flex-direction: column;
      align-items: center;
    }

    .hero-button {
      min-width: 160px;
      height: 52px;
      border-radius: var(--border-radius-md);
      font-weight: 500;
      font-size: 1rem;
      letter-spacing: 0.5px;
      position: relative;
      overflow: hidden;

      .el-icon {
        margin-right: var(--spacing-2);
      }

      &::after {
        content: '';
        position: absolute;
        top: -50%;
        left: -50%;
        width: 200%;
        height: 200%;
        background: radial-gradient(circle, rgba(255,255,255,0.2) 0%, transparent 80%);
        opacity: 0;
        transform: scale(0.5);
        transition: transform 0.4s ease, opacity 0.3s ease;
      }

      &:hover::after {
        opacity: 1;
        transform: scale(1);
      }

      &.login-btn {
        background: linear-gradient(90deg, var(--primary-color), var(--primary-light));
        border: none;
        box-shadow: 0 4px 14px rgba(var(--primary-color-rgb), 0.4);
        transition: all var(--transition-normal);

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 6px 20px rgba(var(--primary-color-rgb), 0.5);
        }
      }

      &.signup-btn {
        background: white;
        border: 2px solid var(--primary-color);
        color: var(--primary-color);

        &:hover {
          background-color: rgba(var(--primary-color-rgb), 0.05);
        }
      }
    }
  }

  .hero-stats {
    display: flex;
    gap: var(--spacing-10);

    @media (max-width: 1200px) {
      justify-content: center;
    }

    @media (max-width: 768px) {
      flex-direction: column;
      gap: var(--spacing-6);
    }

    .stat-item {
      .stat-value {
        font-size: 2rem;
        font-weight: 700;
        color: var(--primary-color);
        margin-bottom: var(--spacing-1);
        transition: transform 0.3s ease;

        &:hover {
          transform: scale(1.1);
        }
      }

      .stat-label {
        font-size: 0.875rem;
        color: var(--neutral-600);
      }
    }
  }

  .hero-image {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 2;
    perspective: 1000px;

    img {
      max-width: 100%;
      height: auto;
      border-radius: var(--border-radius-lg);
      box-shadow: var(--shadow-lg);
      transition: transform 0.3s ease-out;
      will-change: transform;
    }
  }
}

/* Section Styles */
.section-header {
  text-align: center;
  margin-bottom: var(--spacing-12);

  .section-title {
    font-size: 2.5rem;
    font-weight: 700;
    color: var(--neutral-900);
    margin-bottom: var(--spacing-4);
    position: relative;
    display: inline-block;

    @media (max-width: 768px) {
      font-size: 2rem;
    }

    &::after {
      content: '';
      position: absolute;
      left: 50%;
      bottom: -10px;
      width: 60px;
      height: 4px;
      background: linear-gradient(to right, var(--primary-color), var(--primary-light));
      border-radius: 2px;
      transform: translateX(-50%);
    }
  }

  .section-description {
    font-size: 1.125rem;
    color: var(--neutral-600);
    max-width: 700px;
    margin: 0 auto;
  }
}

/* Features Section */
.features-section {
  padding: var(--spacing-20) var(--spacing-6);
  background-color: white;
  position: relative;

  &.visible {
    .feature-card {
      &:nth-child(1) { animation-delay: 0.1s; }
      &:nth-child(2) { animation-delay: 0.3s; }
      &:nth-child(3) { animation-delay: 0.5s; }
      &:nth-child(4) { animation-delay: 0.7s; }
    }
  }

  .features-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: var(--spacing-6);
    max-width: 1200px;
    margin: 0 auto;

    .feature-card {
      background-color: white;
      border-radius: var(--border-radius-lg);
      padding: var(--spacing-6);
      box-shadow: var(--shadow-md);
      transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
      position: relative;
      z-index: 1;
      overflow: hidden;

      &:hover {
        transform: translateY(-8px);
        box-shadow: var(--shadow-lg);
      }

      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: linear-gradient(135deg, rgba(var(--primary-color-rgb), 0.05) 0%, transparent 100%);
        z-index: -1;
        opacity: 0;
        transition: opacity 0.4s ease;
      }

      &:hover::before {
        opacity: 1;
      }

        .feature-icon {
        width: 64px;
        height: 64px;
        background: linear-gradient(135deg, var(--primary-light) 0%, var(--primary-color) 100%);
        border-radius: var(--border-radius-md);
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: var(--spacing-4);
        transition: transform 0.3s ease;

        .el-icon {
          font-size: 32px;
          color: white;
        }

        &:hover {
          transform: rotate(10deg);
        }
      }

      .feature-title {
        font-size: 1.25rem;
        font-weight: 600;
        color: var(--neutral-900);
        margin-bottom: var(--spacing-3);
      }

      .feature-description {
        font-size: 0.875rem;
        color: var(--neutral-600);
        line-height: 1.6;
      }
    }
  }
}

/* How It Works Section */
.how-it-works-section {
  padding: var(--spacing-20) var(--spacing-6);
  background-color: var(--neutral-50);
  position: relative;

  &.visible {
    .step-item {
      &:nth-child(1) { animation-delay: 0.1s; }
      &:nth-child(2) { animation-delay: 0.3s; }
      &:nth-child(3) { animation-delay: 0.5s; }
      &:nth-child(4) { animation-delay: 0.7s; }
    }
  }

  .steps-container {
    display: flex;
    align-items: flex-start;
    max-width: 1200px;
    margin: 0 auto;

    @media (max-width: 992px) {
      flex-direction: column;
      align-items: center;
    }

    .step-item {
      flex: 1;
      text-align: center;
      padding: var(--spacing-6);
      position: relative;

      .step-number {
        width: 60px;
        height: 60px;
        background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-light) 100%);
        border-radius: 50%;
      display: flex;
        align-items: center;
      justify-content: center;
        margin: 0 auto var(--spacing-4);
        font-size: 1.25rem;
        font-weight: 700;
        color: white;
        position: relative;
        z-index: 1;
        transition: transform 0.3s ease;

          &:hover {
          transform: scale(1.1);
        }

        &::before {
          content: '';
          position: absolute;
          top: -5px;
          left: -5px;
          right: -5px;
          bottom: -5px;
          background: rgba(var(--primary-color-rgb), 0.3);
          border-radius: 50%;
          z-index: -1;
          opacity: 0;
          transform: scale(0.8);
          transition: all 0.3s ease;
        }

        &:hover::before {
          opacity: 1;
          transform: scale(1.2);
        }
      }

      .step-title {
        font-size: 1.25rem;
        font-weight: 600;
        color: var(--neutral-900);
        margin-bottom: var(--spacing-3);
      }

      .step-description {
        font-size: 0.875rem;
        color: var(--neutral-600);
        line-height: 1.6;
      }

      .step-divider {
        position: absolute;
        top: 90px;
        right: -20px;
        width: 40px;
        height: 2px;
        background: linear-gradient(to right, var(--primary-color), transparent);
        z-index: 0;

        @media (max-width: 992px) {
          top: auto;
          right: auto;
          bottom: -20px;
          left: 50%;
          transform: translateX(-50%) rotate(90deg);
          width: 40px;
        }
      }
    }
  }
}

/* CTA Section */
.cta-section {
  padding: var(--spacing-16) var(--spacing-6);
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);
  color: white;
  text-align: center;
  position: relative;
  overflow: hidden;

  .moving-waves {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 100px;
    overflow: hidden;

    .wave {
      position: absolute;
      bottom: 0;
      left: 0;
      width: 200%;
      height: 100%;
      background-repeat: repeat-x;
      background-position: 0 bottom;
      transform-origin: center bottom;
    }

    .wave1 {
      background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320"><path fill="white" fill-opacity="0.3" d="M0,224L48,213.3C96,203,192,181,288,181.3C384,181,480,203,576,202.7C672,203,768,181,864,170.7C960,160,1056,160,1152,181.3C1248,203,1344,245,1392,266.7L1440,288L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"></path></svg>');
      animation: wave 20s linear infinite;
      z-index: 1;
      opacity: 0.3;
    }

    .wave2 {
      background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320"><path fill="white" fill-opacity="0.5" d="M0,160L48,149.3C96,139,192,117,288,122.7C384,128,480,160,576,165.3C672,171,768,149,864,149.3C960,149,1056,171,1152,176C1248,181,1344,171,1392,165.3L1440,160L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"></path></svg>');
      animation: wave 17s linear infinite;
      z-index: 2;
      opacity: 0.5;
    }

    .wave3 {
      background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320"><path fill="white" fill-opacity="0.2" d="M0,288L48,272C96,256,192,224,288,218.7C384,213,480,235,576,245.3C672,256,768,256,864,256C960,256,1056,256,1152,240C1248,224,1344,192,1392,176L1440,160L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"></path></svg>');
      animation: wave 15s linear infinite reverse;
      z-index: 3;
      opacity: 0.2;
    }
  }

  .cta-content {
    max-width: 700px;
    margin: 0 auto;
    position: relative;
    z-index: 10;
  }

  .cta-title {
    font-size: 2.5rem;
    font-weight: 700;
    margin-bottom: var(--spacing-4);

    @media (max-width: 768px) {
      font-size: 2rem;
    }
  }

  .cta-description {
    font-size: 1.125rem;
    margin-bottom: var(--spacing-8);
    opacity: 0.9;
  }

  .cta-actions {
    display: flex;
    justify-content: center;
    gap: var(--spacing-4);

    @media (max-width: 768px) {
      flex-direction: column;
      align-items: center;
    }

    .cta-button {
      min-width: 160px;
      height: 48px;
      border-radius: var(--border-radius-md);
      font-weight: 500;
      background-color: white;
      color: var(--primary-color);
      border: none;
      position: relative;
      overflow: hidden;
      transition: all 0.3s ease;

      &::after {
        content: '';
        position: absolute;
        top: -50%;
        left: -50%;
        width: 200%;
        height: 200%;
        background: radial-gradient(circle, rgba(var(--primary-color-rgb), 0.1) 0%, transparent 80%);
        opacity: 0;
        transform: scale(0.5);
        transition: all 0.4s ease;
      }

      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);

        &::after {
          opacity: 1;
          transform: scale(1);
        }
      }
    }

    .cta-button-secondary {
      min-width: 160px;
      height: 48px;
      border-radius: var(--border-radius-md);
      font-weight: 500;
      background-color: transparent;
      color: white;
      border: 2px solid white;
      transition: all 0.3s ease;

      &:hover {
        background-color: rgba(255, 255, 255, 0.1);
        transform: translateY(-3px);
      }
    }
  }
}
</style>

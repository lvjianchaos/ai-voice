<!-- src/views/ErrorPage.vue -->
<script lang="ts" setup>
import { useRouter } from 'vue-router';
import { ref, onMounted } from 'vue';
import { Back } from '@element-plus/icons-vue';

interface Particle {
  id: number;
  x: number;
  y: number;
  size: number;
  speed: number;
  opacity: number;
}

interface Digit {
  value: string;
  animationDelay: string;
}

const router = useRouter();
const isLoaded = ref(false);
const particles = ref<Particle[]>([]);
const errorCodeDigits = ref<Digit[]>([]);

// 生成随机数字
const generateRandomDigits = () => {
  const errorCode = '404';
  return errorCode.split('').map(digit => ({
    value: digit,
    animationDelay: `${Math.random() * 0.5}s`
  }));
};

const goHome = () => {
  // 添加按钮点击过渡效果
  isLoaded.value = false;
  setTimeout(() => {
  router.push('/main');
  }, 600);
};

const goBack = () => {
  router.back();
};

// 生成粒子
const generateParticles = () => {
  const newParticles: Particle[] = [];
  for (let i = 0; i < 40; i++) {
    newParticles.push({
      id: i,
      x: Math.random() * 100,
      y: Math.random() * 100,
      size: Math.random() * 6 + 1,
      speed: Math.random() * 1 + 0.5,
      opacity: Math.random() * 0.5 + 0.3
    });
  }
  particles.value = newParticles;
};

onMounted(() => {
  // 生成元素并加载动画
  generateParticles();
  errorCodeDigits.value = generateRandomDigits();
  setTimeout(() => {
    isLoaded.value = true;
  }, 100);
});
</script>

<template>
  <div class="error-page">
    <div class="particles-container">
      <div
        v-for="particle in particles"
        :key="particle.id"
        class="particle"
        :style="{
          left: `${particle.x}%`,
          top: `${particle.y}%`,
          width: `${particle.size}px`,
          height: `${particle.size}px`,
          opacity: particle.opacity,
          animationDuration: `${particle.speed + 3}s`
        }"
      ></div>
    </div>

    <div :class="['error-content', { 'loaded': isLoaded }]">
      <div class="error-code">
        <span
          v-for="(digit, index) in errorCodeDigits"
          :key="index"
          class="digit"
          :style="{ animationDelay: digit.animationDelay }"
        >
          {{ digit.value }}
        </span>
      </div>

      <div class="error-title">哎呀！页面丢失了</div>

      <div class="error-subtitle">
        <span class="glitch" data-text="看起来您要找的页面不存在或已被移动">
          看起来您要找的页面不存在或已被移动
        </span>
      </div>

      <div class="error-illustration">
        <div class="astronaut">
          <div class="astronaut-body"></div>
          <div class="astronaut-head"></div>
          <div class="astronaut-arm left"></div>
          <div class="astronaut-arm right"></div>
          <div class="astronaut-leg left"></div>
          <div class="astronaut-leg right"></div>
        </div>
        <div class="planet"></div>
      </div>

      <div class="action-buttons">
        <el-button
          type="primary"
          @click="goHome"
          class="btn-home hover-button"
          size="large"
          round
        >
          返回首页
        </el-button>

        <el-button
          @click="goBack"
          class="btn-back hover-button"
          size="large"
          round
        >
          <el-icon><Back /></el-icon>
          返回上一页
        </el-button>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.error-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  overflow: hidden;
  position: relative;
  background: linear-gradient(135deg, #1a2035 0%, #31425c 100%);
  color: white;
  font-family: var(--font-family-base);

  &::before {
    content: '';
    position: absolute;
    width: 100%;
    height: 100%;
    background:
      radial-gradient(circle at 10% 10%, rgba(255, 255, 255, 0.05) 0%, transparent 80%),
      radial-gradient(circle at 90% 90%, rgba(255, 255, 255, 0.05) 0%, transparent 80%);
  }
}

.particles-container {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 1;
}

.particle {
  position: absolute;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 50%;
  animation: float 8s infinite linear;

  &:nth-child(even) {
    background-color: rgba(var(--primary-color-rgb), 0.8);
  }
}

.error-content {
  z-index: 10;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  max-width: 600px;
  text-align: center;
  opacity: 0;
  transform: translateY(20px);
  transition: all 0.8s cubic-bezier(0.68, -0.55, 0.27, 1.55);

  &.loaded {
    opacity: 1;
    transform: translateY(0);
  }
}

.error-code {
  font-size: 8rem;
  font-weight: 700;
  margin-bottom: 1rem;
  display: flex;
  line-height: 1;

  .digit {
    display: inline-block;
    animation: bounceIn 1s cubic-bezier(0.68, -0.55, 0.27, 1.55) forwards;
    opacity: 0;
    color: var(--primary-color);
    text-shadow:
      0 0 10px rgba(var(--primary-color-rgb), 0.5),
      0 0 20px rgba(var(--primary-color-rgb), 0.3),
      0 0 30px rgba(var(--primary-color-rgb), 0.1);

    &:nth-child(2) {
      margin: 0 0.5rem;
    }
  }
}

.error-title {
  font-size: 2.5rem;
  font-weight: 600;
  margin-bottom: 1rem;
  animation: fadeInUp 0.8s 0.4s forwards;
  opacity: 0;
  background: linear-gradient(90deg, #e0e0e0, #ffffff, #e0e0e0);
  background-size: 200% auto;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  animation: shimmer 3s infinite linear, fadeInUp 0.8s 0.4s forwards;
}

.error-subtitle {
  font-size: 1.2rem;
  margin-bottom: 2.5rem;
  color: rgba(255, 255, 255, 0.8);
  animation: fadeInUp 0.8s 0.6s forwards;
  opacity: 0;
}

.error-illustration {
  position: relative;
  width: 200px;
  height: 200px;
  margin-bottom: 2rem;
  animation: fadeInUp 0.8s 0.8s forwards;
  opacity: 0;
}

.astronaut {
  position: absolute;
  width: 60px;
  height: 80px;
  left: 70px;
  top: 50px;
  animation: float 6s ease-in-out infinite;

  &-body {
    position: absolute;
    width: 40px;
    height: 45px;
    background: white;
    border-radius: 20px;
    left: 10px;
    top: 25px;
  }

  &-head {
    position: absolute;
    width: 30px;
    height: 30px;
    background: white;
    border-radius: 50%;
    left: 15px;
    top: 0;

    &::after {
      content: '';
      position: absolute;
      width: 20px;
      height: 15px;
      background: rgba(0, 0, 0, 0.2);
    border-radius: 10px;
      left: 5px;
      top: 7px;
    }
  }

  &-arm {
    position: absolute;
    width: 10px;
    height: 30px;
    background: white;
    border-radius: 5px;

    &.left {
      left: 5px;
      top: 35px;
      transform-origin: top center;
      animation: wave 2s ease-in-out infinite;
    }

    &.right {
      left: 45px;
      top: 35px;
      transform-origin: top center;
      animation: wave 2s ease-in-out infinite 0.5s;
    }
  }

  &-leg {
    position: absolute;
    width: 10px;
    height: 20px;
    background: white;
    border-radius: 5px;

    &.left {
      left: 15px;
      top: 65px;
    }

    &.right {
      left: 35px;
      top: 65px;
    }
  }
}

.planet {
  position: absolute;
  width: 140px;
  height: 140px;
  background: linear-gradient(135deg, var(--primary-dark) 0%, var(--primary-color) 100%);
  border-radius: 50%;
  left: 30px;
  top: 30px;
  box-shadow:
    inset -20px -20px 40px rgba(0, 0, 0, 0.3),
    0 0 20px rgba(var(--primary-color-rgb), 0.5);

  &::before {
    content: '';
    position: absolute;
    width: 30px;
    height: 30px;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    left: 20px;
    top: 30px;
  }

  &::after {
    content: '';
    position: absolute;
    width: 15px;
    height: 15px;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    left: 90px;
    top: 40px;
  }
}

.action-buttons {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
  animation: fadeInUp 0.8s 1s forwards;
  opacity: 0;

  .btn-home, .btn-back {
    min-width: 140px;
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.3);
  }

  .btn-home {
    background: linear-gradient(90deg, var(--primary-dark), var(--primary-color));
    border: none;

    &:hover {
      background: linear-gradient(90deg, var(--primary-color), var(--primary-dark));
    }
  }

  .btn-back {
    color: white;
    background: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.2);

    &:hover {
      background: rgba(255, 255, 255, 0.2);
    }
  }
}

.glitch {
  position: relative;
  animation: glitch 3s infinite;

  &::before, &::after {
    content: attr(data-text);
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
  }

  &::before {
    left: 2px;
    text-shadow: -2px 0 var(--primary-color);
    clip: rect(44px, 450px, 56px, 0);
    animation: glitch-anim 5s infinite linear alternate-reverse;
  }

  &::after {
    left: -2px;
    text-shadow: -2px 0 var(--danger-color);
    clip: rect(44px, 450px, 56px, 0);
    animation: glitch-anim2 5s infinite linear alternate-reverse;
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

@keyframes wave {
  0%, 100% {
    transform: rotate(0deg);
  }
  50% {
    transform: rotate(20deg);
  }
}

@keyframes bounceIn {
  0% {
    opacity: 0;
    transform: scale(0.3);
  }
  50% {
    opacity: 1;
    transform: scale(1.1);
  }
  70% {
    transform: scale(0.9);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}

@keyframes fadeInUp {
  0% {
    opacity: 0;
    transform: translateY(20px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes shimmer {
  0% {
    background-position: 200% center;
  }
  100% {
    background-position: -200% center;
  }
}

@keyframes glitch {
  0%, 100% {
    transform: translate(0);
  }
  20% {
    transform: translate(-5px, 5px);
  }
  40% {
    transform: translate(-5px, -5px);
  }
  60% {
    transform: translate(5px, 5px);
  }
  80% {
    transform: translate(5px, -5px);
  }
}

@keyframes glitch-anim {
  0% {
    clip: rect(14px, 9999px, 63px, 0);
  }
  5% {
    clip: rect(37px, 9999px, 3px, 0);
  }
  10% {
    clip: rect(89px, 9999px, 15px, 0);
  }
  15% {
    clip: rect(15px, 9999px, 90px, 0);
  }
  20% {
    clip: rect(39px, 9999px, 21px, 0);
  }
  25% {
    clip: rect(2px, 9999px, 59px, 0);
  }
  30% {
    clip: rect(81px, 9999px, 6px, 0);
  }
  35% {
    clip: rect(4px, 9999px, 21px, 0);
  }
  40% {
    clip: rect(90px, 9999px, 12px, 0);
  }
  45% {
    clip: rect(16px, 9999px, 83px, 0);
  }
  50% {
    clip: rect(89px, 9999px, 99px, 0);
  }
  55% {
    clip: rect(57px, 9999px, 73px, 0);
  }
  60% {
    clip: rect(10px, 9999px, 7px, 0);
  }
  65% {
    clip: rect(31px, 9999px, 69px, 0);
  }
  70% {
    clip: rect(14px, 9999px, 56px, 0);
  }
  75% {
    clip: rect(52px, 9999px, 44px, 0);
  }
  80% {
    clip: rect(82px, 9999px, 92px, 0);
  }
  85% {
    clip: rect(99px, 9999px, 100px, 0);
  }
  90% {
    clip: rect(27px, 9999px, 53px, 0);
  }
  95% {
    clip: rect(24px, 9999px, 37px, 0);
  }
  100% {
    clip: rect(66px, 9999px, 95px, 0);
  }
}

@keyframes glitch-anim2 {
  0% {
    clip: rect(73px, 9999px, 33px, 0);
  }
  5% {
    clip: rect(48px, 9999px, 65px, 0);
  }
  10% {
    clip: rect(75px, 9999px, 4px, 0);
  }
  15% {
    clip: rect(11px, 9999px, 24px, 0);
  }
  20% {
    clip: rect(31px, 9999px, 25px, 0);
  }
  25% {
    clip: rect(45px, 9999px, 61px, 0);
  }
  30% {
    clip: rect(86px, 9999px, 2px, 0);
  }
  35% {
    clip: rect(38px, 9999px, 73px, 0);
  }
  40% {
    clip: rect(26px, 9999px, 57px, 0);
  }
  45% {
    clip: rect(45px, 9999px, 16px, 0);
  }
  50% {
    clip: rect(5px, 9999px, 36px, 0);
  }
  55% {
    clip: rect(82px, 9999px, 31px, 0);
  }
  60% {
    clip: rect(88px, 9999px, 40px, 0);
  }
  65% {
    clip: rect(41px, 9999px, 51px, 0);
  }
  70% {
    clip: rect(59px, 9999px, 78px, 0);
  }
  75% {
    clip: rect(41px, 9999px, 70px, 0);
  }
  80% {
    clip: rect(86px, 9999px, 5px, 0);
  }
  85% {
    clip: rect(46px, 9999px, 14px, 0);
  }
  90% {
    clip: rect(52px, 9999px, 22px, 0);
  }
  95% {
    clip: rect(9px, 9999px, 86px, 0);
  }
  100% {
    clip: rect(94px, 9999px, 43px, 0);
  }
}
</style>

<!-- src/App.vue -->
<script lang="ts" setup>
import { onMounted, onBeforeUnmount, provide, ref } from 'vue';
import { useRouter } from 'vue-router';
import request from './utils/request';

const router = useRouter();
const pageLoaded = ref(false);
const cursorPosition = ref({ x: 0, y: 0 });

// 提供全局动画状态
provide('animationsEnabled', true);

// 页面过渡动画状态
provide('pageTransition', {
  enterClass: 'page-enter',
  leaveClass: 'page-leave',
});

// 监听路由变化添加过渡动画
const handleRouteChange = (to, from) => {
  // 如果是首次加载，不添加动画
  if (!from.name) return;

  const app = document.querySelector('#app') as HTMLElement;
  if (!app) return;

  // 添加离开动画
  app.classList.add('page-transition');
  app.classList.add('page-leave');

  // 添加粒子爆炸效果
  createRouteChangeParticles();

  setTimeout(() => {
    // 移除离开动画
    app.classList.remove('page-leave');

    // 添加进入动画
    app.classList.add('page-enter');

    // 完成后清理
    setTimeout(() => {
      app.classList.remove('page-enter');
      app.classList.remove('page-transition');
    }, 800);
  }, 400);
};

// 创建路由变化粒子效果
const createRouteChangeParticles = () => {
  const container = document.createElement('div');
  container.className = 'route-change-particles';
  document.body.appendChild(container);

  // 创建30个粒子
  for (let i = 0; i < 30; i++) {
    const particle = document.createElement('div');
    particle.className = 'route-particle';

    // 随机颜色
    const colors = ['#409EFF', '#79bbff', '#67C23A', '#E6A23C', '#F56C6C', '#ffffff'];
    const color = colors[Math.floor(Math.random() * colors.length)];

    // 随机大小
    const size = Math.random() * 10 + 5;

    // 随机位置和动画
    const xPos = 50 + (Math.random() - 0.5) * 30;
    const yPos = 50 + (Math.random() - 0.5) * 30;
    const duration = Math.random() * 1.5 + 0.5;
    const delay = Math.random() * 0.3;

    particle.style.backgroundColor = color;
    particle.style.width = `${size}px`;
    particle.style.height = `${size}px`;
    particle.style.left = `${xPos}%`;
    particle.style.top = `${yPos}%`;
    particle.style.animationDuration = `${duration}s`;
    particle.style.animationDelay = `${delay}s`;

    container.appendChild(particle);
  }

  // 移除粒子容器
  setTimeout(() => {
    if (container.parentNode) {
      container.parentNode.removeChild(container);
    }
  }, 2000);
};

// 创建鼠标跟随效果
const createMouseFollower = () => {
  // 创建鼠标跟随元素
  const follower = document.createElement('div');
  follower.classList.add('mouse-follower');
  document.body.appendChild(follower);

  // 创建内圈
  const followerInner = document.createElement('div');
  followerInner.classList.add('mouse-follower-inner');
  follower.appendChild(followerInner);

  // 添加光晕效果
  const followerGlow = document.createElement('div');
  followerGlow.classList.add('mouse-follower-glow');
  follower.appendChild(followerGlow);

  // 鼠标移动事件
  const handleMouseMove = (e) => {
    // 更新全局鼠标位置
    cursorPosition.value = { x: e.clientX, y: e.clientY };

    // 添加平滑跟随效果，使用 GSAP 的思路
    requestAnimationFrame(() => {
      const x = e.clientX;
      const y = e.clientY;

      follower.style.transform = `translate(${x}px, ${y}px)`;

      // 内圈以稍微延迟跟随，创造更流畅的效果
      setTimeout(() => {
        followerInner.style.transform = `translate(-50%, -50%) scale(1)`;
      }, 50);
    });
  };

  // 鼠标点击效果
  const handleMouseClick = () => {
    // 添加点击扩散动画
    const clickRipple = document.createElement('div');
    clickRipple.classList.add('click-ripple');
    clickRipple.style.left = `${cursorPosition.value.x}px`;
    clickRipple.style.top = `${cursorPosition.value.y}px`;
    document.body.appendChild(clickRipple);

    // 动画结束后移除
    setTimeout(() => {
      if (clickRipple.parentNode) {
        clickRipple.parentNode.removeChild(clickRipple);
      }
    }, 1000);

    // 鼠标跟随器缩放效果
    followerInner.style.transform = 'translate(-50%, -50%) scale(0.5)';
    setTimeout(() => {
      followerInner.style.transform = 'translate(-50%, -50%) scale(1)';
    }, 300);
  };

  // 互动元素的特殊效果
  const addInteractiveEffect = () => {
    const interactiveElements = document.querySelectorAll(
      'a, button, .el-button, [role="button"], .interactive, input, .el-input, .el-select, .el-checkbox, .el-radio, .card, .hover-card'
    );

    interactiveElements.forEach((el) => {
      el.addEventListener('mouseenter', () => {
        follower.classList.add('follower-active');

        // 互动元素类型特殊效果
        if (el.classList.contains('el-button') || el.tagName.toLowerCase() === 'button') {
          follower.classList.add('follower-button');
        } else if (el.tagName.toLowerCase() === 'a') {
          follower.classList.add('follower-link');
        } else if (el.classList.contains('card') || el.classList.contains('hover-card')) {
          follower.classList.add('follower-card');
        }
      });

      el.addEventListener('mouseleave', () => {
        follower.classList.remove('follower-active');
        follower.classList.remove('follower-button');
        follower.classList.remove('follower-link');
        follower.classList.remove('follower-card');
      });
    });
  };

  // 添加事件监听
  document.addEventListener('mousemove', handleMouseMove);
  document.addEventListener('click', handleMouseClick);

  // 添加互动效果
  window.addEventListener('load', () => {
    addInteractiveEffect();

    // 监控DOM变化，为新添加的元素添加互动效果
    const observer = new MutationObserver(() => {
      addInteractiveEffect();
    });

    observer.observe(document.body, {
      childList: true,
      subtree: true
    });
  });

  // 返回清理函数
  return () => {
    document.removeEventListener('mousemove', handleMouseMove);
    document.removeEventListener('click', handleMouseClick);
    if (follower.parentNode) {
      follower.parentNode.removeChild(follower);
    }
  };
};

// 添加视差滚动效果
const addParallaxEffect = () => {
  const parallaxElements = document.querySelectorAll('.parallax');

  const handleScroll = () => {
    const scrollTop = window.pageYOffset;

    parallaxElements.forEach((el) => {
      const speed = el.getAttribute('data-parallax-speed') || 0.5;
      const offset = scrollTop * speed;
      (el as HTMLElement).style.transform = `translateY(${offset}px)`;
    });
  };

  window.addEventListener('scroll', handleScroll);

  return () => {
    window.removeEventListener('scroll', handleScroll);
  };
};

// 添加页面进入动画
const addPageEntranceAnimation = () => {
  setTimeout(() => {
    pageLoaded.value = true;

    // 触发动画
    const elements = document.querySelectorAll('.animate-on-enter');
    elements.forEach((el, index) => {
      (el as HTMLElement).style.animationDelay = `${index * 0.1}s`;
      el.classList.add('animated');
    });
  }, 300);
};

let cleanupMouseFollower = null;
let cleanupParallax = null;

onMounted(() => {
  // 添加路由监听
  router.beforeEach(handleRouteChange);

  // 添加鼠标跟随效果
  cleanupMouseFollower = createMouseFollower();

  // 添加视差滚动效果
  cleanupParallax = addParallaxEffect();

  // 添加顺滑滚动
  document.documentElement.style.scrollBehavior = 'smooth';

  // 添加页面进入动画
  addPageEntranceAnimation();
});

onBeforeUnmount(() => {
  // 清理鼠标跟随效果
  if (cleanupMouseFollower) {
    cleanupMouseFollower();
  }

  // 清理视差效果
  if (cleanupParallax) {
    cleanupParallax();
  }

  // 移除顺滑滚动
  document.documentElement.style.scrollBehavior = '';
});

request({
  method: "GET",
  url: "/api/v1/user/info",
}).then((res) => {
  console.log(res);
});
</script>

<template>
  <div :class="['app-wrapper', { 'page-loaded': pageLoaded }]">
    <router-view v-slot="{ Component }">
      <transition
        name="route-transition"
        mode="out-in"
        appear
      >
        <component :is="Component" />
      </transition>
    </router-view>
  </div>
</template>

<style lang="scss">
// 引入全局CSS变量和基础样式
:root {
  // 主色
  --primary-color: #409EFF;
  --primary-light: #79bbff;
  --primary-dark: #337ecc;
  --primary-color-rgb: 64, 158, 255;

  // 次要色
  --secondary-color: #67C23A;
  --secondary-light: #85ce61;
  --secondary-dark: #529b2e;
  --secondary-color-rgb: 103, 194, 58;

  // 状态色
  --success-color: #67C23A;
  --warning-color: #E6A23C;
  --danger-color: #F56C6C;
  --danger-color-rgb: 245, 108, 108;
  --info-color: #909399;

  // 中性色
  --neutral-50: #F9FAFB;
  --neutral-100: #F3F4F6;
  --neutral-200: #E5E7EB;
  --neutral-300: #D1D5DB;
  --neutral-400: #9CA3AF;
  --neutral-500: #6B7280;
  --neutral-600: #4B5563;
  --neutral-700: #374151;
  --neutral-800: #1F2937;
  --neutral-900: #111827;

  // 间距
  --spacing-1: 4px;
  --spacing-2: 8px;
  --spacing-3: 12px;
  --spacing-4: 16px;
  --spacing-5: 20px;
  --spacing-6: 24px;
  --spacing-8: 32px;
  --spacing-10: 40px;
  --spacing-12: 48px;
  --spacing-16: 64px;
  --spacing-20: 80px;

  // 圆角
  --border-radius-sm: 4px;
  --border-radius-md: 8px;
  --border-radius-lg: 16px;
  --border-radius-xl: 24px;
  --border-radius-full: 9999px;

  // 阴影
  --shadow-sm: 0 1px 2px rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -2px rgba(0, 0, 0, 0.1);
  --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -4px rgba(0, 0, 0, 0.1);
  --shadow-xl: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.1);

  // 字体
  --font-family-base: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  --font-family-heading: var(--font-family-base);

  // 过渡时间
  --transition-fast: 0.15s;
  --transition-normal: 0.3s;
  --transition-slow: 0.5s;
}

// 全局基础样式
html, body {
  width: 100%;
  height: 100%;
  overflow-x: hidden;
}

body {
  font-family: var(--font-family-base);
  color: var(--neutral-900);
  background-color: var(--neutral-50);
  margin: 0;
  padding: 0;
  line-height: 1.5;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  cursor: none; /* 隐藏默认鼠标指针，使用自定义光标 */
}

/* 对于移动设备保留默认鼠标 */
@media (max-width: 768px) {
  body {
    cursor: auto;
  }

  .mouse-follower, .mouse-follower-inner, .mouse-follower-glow {
    display: none !important;
  }
}

// App wrapper
.app-wrapper {
  position: relative;
  min-height: 100vh;
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.8s ease, transform 0.8s ease;

  &.page-loaded {
    opacity: 1;
    transform: translateY(0);
  }
}

// 鼠标跟随器
.mouse-follower {
  position: fixed;
  top: 0;
  left: 0;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  pointer-events: none;
  z-index: 9999;
  transform: translate(-50%, -50%);
  transition: width 0.3s ease, height 0.3s ease, transform 0.1s ease;

  &-inner {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 8px;
    height: 8px;
    background-color: var(--primary-color);
    border-radius: 50%;
    transition: transform 0.3s ease, width 0.3s ease, height 0.3s ease, background-color 0.3s ease;
  }

  &-glow {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 40px;
    height: 40px;
    background: radial-gradient(circle, rgba(var(--primary-color-rgb), 0.3) 0%, rgba(var(--primary-color-rgb), 0) 70%);
    border-radius: 50%;
    opacity: 0.6;
    transition: width 0.3s ease, height 0.3s ease, opacity 0.3s ease;
  }

  &.follower-active {
    .mouse-follower-inner {
      width: 12px;
      height: 12px;
      background-color: var(--primary-color);
    }

    .mouse-follower-glow {
      width: 60px;
      height: 60px;
      opacity: 0.8;
    }
  }

  &.follower-button {
    .mouse-follower-inner {
      background-color: white;
    }

    .mouse-follower-glow {
      background: radial-gradient(circle, rgba(255, 255, 255, 0.3) 0%, rgba(255, 255, 255, 0) 70%);
    }
  }

  &.follower-link {
    .mouse-follower-inner {
      background-color: var(--primary-light);
      transform: translate(-50%, -50%) scale(1.5);
    }
  }

  &.follower-card {
    .mouse-follower-inner {
      background-color: var(--success-color);
    }

    .mouse-follower-glow {
      background: radial-gradient(circle, rgba(var(--secondary-color-rgb), 0.3) 0%, rgba(var(--secondary-color-rgb), 0) 70%);
      width: 80px;
      height: 80px;
    }
  }
}

// 点击波纹效果
.click-ripple {
  position: fixed;
  border-radius: 50%;
  pointer-events: none;
  transform: translate(-50%, -50%);
  background-color: rgba(var(--primary-color-rgb), 0.3);
  width: 10px;
  height: 10px;
  animation: click-ripple 1s ease-out forwards;
}

@keyframes click-ripple {
  0% {
    width: 0;
    height: 0;
    opacity: 0.5;
  }
  100% {
    width: 100px;
    height: 100px;
    opacity: 0;
  }
}

// 路由变化粒子
.route-change-particles {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 9998;
}

.route-particle {
  position: absolute;
  width: 10px;
  height: 10px;
  background-color: var(--primary-color);
  border-radius: 50%;
  opacity: 0;
  animation: particle-explosion 1.5s cubic-bezier(0.165, 0.84, 0.44, 1) forwards;
}

@keyframes particle-explosion {
  0% {
    transform: translate(-50%, -50%) scale(0);
    opacity: 1;
  }
  100% {
    transform: translate(
      calc(-50% + (random(100) - 50) * 3px),
      calc(-50% + (random(100) - 50) * 3px)
    ) scale(1);
    opacity: 0;
  }
}

// 页面过渡动画
.route-transition-enter-active,
.route-transition-leave-active {
  transition: opacity 0.5s ease, transform 0.5s cubic-bezier(0.33, 1, 0.68, 1);
}

.route-transition-enter-from {
  opacity: 0;
  transform: translateY(30px) scale(0.95);
}

.route-transition-leave-to {
  opacity: 0;
  transform: translateY(-30px) scale(0.95);
}

// 单独页面过渡动画
.page-transition {
  transition: opacity 0.5s ease, transform 0.5s cubic-bezier(0.33, 1, 0.68, 1);
}

.page-leave {
  opacity: 0;
  transform: translateY(-30px) scale(0.95);
}

.page-enter {
  opacity: 0;
  transform: translateY(30px) scale(0.95);
  animation: fade-in-up 0.8s cubic-bezier(0.33, 1, 0.68, 1) forwards;
}

@keyframes fade-in-up {
  from {
    opacity: 0;
    transform: translateY(30px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

// 元素进入动画
.animate-on-enter {
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.6s ease, transform 0.6s ease;

  &.animated {
    opacity: 1;
    transform: translateY(0);
  }
}

// 全局按钮悬停动画
button, .el-button {
  transition: transform 0.3s cubic-bezier(0.34, 1.56, 0.64, 1), box-shadow 0.3s cubic-bezier(0.34, 1.56, 0.64, 1) !important;

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 7px 14px rgba(50, 50, 93, 0.1), 0 3px 6px rgba(0, 0, 0, 0.08);
  }

  &:active {
    transform: translateY(-1px);
  }
}

// 全局卡片悬停动画
.el-card, .card, .hover-card {
  transition: transform 0.3s cubic-bezier(0.34, 1.56, 0.64, 1), box-shadow 0.3s ease !important;

  &:hover {
    transform: translateY(-5px);
    box-shadow: var(--shadow-lg);
  }
}

// 输入框聚焦动画
.el-input__inner, .el-textarea__inner {
  transition: border 0.3s ease, box-shadow 0.3s ease, transform 0.2s ease !important;

  &:focus {
    transform: scale(1.01);
    box-shadow: 0 0 0 3px rgba(var(--primary-color-rgb), 0.1);
  }
}

// 使表格行有悬停效果
.el-table__row {
  transition: background-color 0.3s ease, transform 0.2s ease !important;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
    z-index: 1;
    position: relative;
  }
}

// 使按钮点击有波纹效果
.el-button {
  position: relative;
  overflow: hidden;

  &::after {
    content: '';
    display: block;
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    pointer-events: none;
    background-image: radial-gradient(circle, rgba(255, 255, 255, 0.3) 10%, transparent 10.01%);
    background-repeat: no-repeat;
    background-position: 50%;
    transform: scale(10, 10);
    opacity: 0;
    transition: transform 0.6s, opacity 0.6s;
  }

  &:active::after {
    transform: scale(0, 0);
    opacity: 0.3;
    transition: 0s;
  }
}

// 全局顺滑过渡
* {
  transition-timing-function: cubic-bezier(0.34, 1.56, 0.64, 1);
}

// 动画可访问性
@media (prefers-reduced-motion: reduce) {
  *,
  ::before,
  ::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
    scroll-behavior: auto !important;
  }

  .mouse-follower, .click-ripple, .route-particle {
    display: none !important;
  }

  body {
    cursor: auto !important;
  }
}
</style>

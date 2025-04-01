<!-- src/components/layout2/AppHeader.vue -->
<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import MainAside from './MainAside.vue';
import MainHeader from './MainHeader.vue';

const contentLoaded = ref(false);

onMounted(() => {
  // 添加加载动画效果
  setTimeout(() => {
    contentLoaded.value = true;
  }, 100);
});
</script>

<template>
  <div class="app-admin-layout">
    <div class="background-gradient"></div>
    <div class="background-particles">
      <div v-for="i in 20" :key="i" class="particle"
           :style="{
             '--size': `${Math.random() * 10 + 2}px`,
             '--x': `${Math.random() * 100}%`,
             '--y': `${Math.random() * 100}%`,
             '--duration': `${Math.random() * 40 + 20}s`,
             '--delay': `${Math.random() * 5}s`
           }">
      </div>
    </div>

    <el-container class="admin-container">
      <MainAside />
      <el-container :class="['header-and-main', { 'content-loaded': contentLoaded }]">
        <MainHeader />
        <el-main class="admin-main">
          <el-scrollbar class="scrollbar-thin">
            <div class="content-container">
              <RouterView v-slot="{ Component }">
                <transition name="fade-slide" mode="out-in" appear>
                  <component :is="Component" />
                </transition>
              </RouterView>
            </div>
          </el-scrollbar>
        </el-main>
        <el-footer class="admin-footer">
          <div class="footer-content">
            <p>© {{ new Date().getFullYear() }} AI-Voice. 教育创新技术提供商</p>
          </div>
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<style lang="scss" scoped>
.app-admin-layout {
  position: relative;
  background-color: var(--neutral-100);
  min-height: 100vh;
  overflow: hidden;
}

.background-gradient {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.05) 0%, rgba(103, 194, 58, 0.05) 100%);
  z-index: 0;
  pointer-events: none;
}

.background-particles {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  pointer-events: none;
  overflow: hidden;

  .particle {
    position: absolute;
    width: var(--size);
    height: var(--size);
    background-color: rgba(var(--primary-color-rgb), 0.2);
    border-radius: 50%;
    top: var(--y);
    left: var(--x);
    animation: float-particle var(--duration) ease-in-out infinite;
    animation-delay: var(--delay);

    &:nth-child(even) {
      background-color: rgba(var(--secondary-color-rgb), 0.15);
    }

    &:nth-child(3n) {
      background-color: rgba(255, 255, 255, 0.3);
      filter: blur(1px);
    }
  }
}

@keyframes float-particle {
  0%, 100% {
    transform: translate(0, 0) rotate(0deg);
  }
  25% {
    transform: translate(50px, 25px) rotate(90deg);
  }
  50% {
    transform: translate(0, 50px) rotate(180deg);
  }
  75% {
    transform: translate(-50px, 25px) rotate(270deg);
  }
}

.admin-container {
  position: relative;
  z-index: 2;
  min-height: 100vh;
}

.header-and-main {
  height: 100vh;
  flex-direction: column;
  overflow: hidden;
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 0.8s ease, transform 0.8s cubic-bezier(0.16, 1, 0.3, 1);

  &.content-loaded {
    opacity: 1;
    transform: translateY(0);
  }
}

.admin-main {
  padding: var(--spacing-5);
  background-color: var(--neutral-100);
  flex: 1;
  overflow: hidden;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 100px;
    background: linear-gradient(to bottom,
      rgba(var(--primary-color-rgb), 0.03) 0%,
      rgba(var(--primary-color-rgb), 0) 100%);
    pointer-events: none;
    z-index: 1;
  }
}

.scrollbar-thin {
  height: 100%;

  :deep(.el-scrollbar__bar) {
    opacity: 0.2;
    transition: opacity 0.3s;

    &:hover {
      opacity: 0.8;
    }
  }
}

.content-container {
  max-width: 1400px;
  margin: 0 auto;
  position: relative;
  z-index: 2;
}

.admin-footer {
  background-color: white;
  padding: var(--spacing-3) var(--spacing-5);
  border-top: 1px solid var(--neutral-200);
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 2px;
    background: linear-gradient(to right,
      var(--primary-color),
      var(--secondary-color),
      var(--primary-color));
    background-size: 200% 100%;
    animation: gradient-shift 8s linear infinite;
  }

  .footer-content {
    text-align: center;
    color: var(--neutral-600);
    font-size: 14px;

    p {
      margin: 0;
      position: relative;
      display: inline-block;

      &::after {
        content: '';
        position: absolute;
        bottom: -3px;
        left: 50%;
        width: 0;
        height: 1px;
        background-color: var(--primary-color);
        transition: width 0.3s ease, left 0.3s ease;
      }

      &:hover::after {
        width: 100%;
        left: 0;
      }
    }
  }
}

@keyframes gradient-shift {
  0% {
    background-position: 0% 0;
  }
  100% {
    background-position: 200% 0;
  }
}

.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: opacity 0.5s ease, transform 0.5s cubic-bezier(0.16, 1, 0.3, 1);
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}
</style>

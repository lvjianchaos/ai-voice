<!-- src/views/login/LoginView.vue -->
<script lang="ts" setup>
import type { FormInstance, FormRules } from 'element-plus';
import { ElMessage } from 'element-plus';
import { login } from "@/api/users";
import { useTokenStore } from "@/stores/mytoken";
import { useRouter, useRoute } from 'vue-router';
import { ref, reactive, onMounted, computed } from 'vue';
import { useAnimations } from '@/composables/useAnimations';

const store = useTokenStore();
const router = useRouter();
const route = useRoute();
const { applyTypingEffect } = useAnimations();

// 表单响应式数据
const form = reactive({
  name: '',
  password: ''
});

// 记住我状态
const rememberMe = ref(false);

// 鼠标位置跟踪（用于交互效果）
const mousePosition = reactive({
  x: 0,
  y: 0
});

// 监听鼠标移动
const handleMouseMove = (event: MouseEvent) => {
  mousePosition.x = event.clientX;
  mousePosition.y = event.clientY;

  // 更新光晕效果位置
  updateGlowPosition(event);
};

// 光晕效果
const updateGlowPosition = (event: MouseEvent) => {
  const glow = document.querySelector('.mouse-glow') as HTMLElement;
  if (glow) {
    const x = event.clientX;
    const y = event.clientY;
    glow.style.left = `${x}px`;
    glow.style.top = `${y}px`;
  }
};

// 登录事件处理
const onSubmit = async () => {
  // 如果已经在加载中，不再重复提交
  if (isLoading.value) return;

  // 添加提交按钮动画
  const loginButton = document.querySelector('.login-button') as HTMLElement;
  if (loginButton) {
    loginButton.classList.add('animate-pulse');
  }

  isLoading.value = true;
  // 表单校验
  await formRef.value?.validate().catch((err) => {
    ElMessage.error("表单校验失败...");
    isLoading.value = false;
    if (loginButton) {
      loginButton.classList.remove('animate-pulse');
    }
    throw err;
  });

  // 登录请求
  try {
    const data = await login(form);
    if (!data.data.success) {
      ElMessage.error('登录失败！');
      isLoading.value = false;

      // 添加错误抖动动画
      const loginCard = document.querySelector('.login-card') as HTMLElement;
      if (loginCard) {
        loginCard.classList.add('animate-shake');
        setTimeout(() => {
          loginCard.classList.remove('animate-shake');
        }, 800);
      }

      if (loginButton) {
        loginButton.classList.remove('animate-pulse');
      }
      throw new Error("登录信息有误");
    }
    // 保存token信息
    store.saveToken(data.data.data);

    isLoading.value = false;

    ElMessage.success('登录成功！');

    // 登录成功动画
    if (loginButton) {
      loginButton.classList.remove('animate-pulse');
      loginButton.classList.add('login-success');
    }

    // 淡出动画后跳转
    const loginContainer = document.querySelector('.login-container') as HTMLElement;
    if (loginContainer) {
      loginContainer.classList.add('login-fade-out');

      // 添加成功登录的粒子爆炸效果
      createSuccessParticles();

      setTimeout(() => {
        router.push(route.query.redirect as string || '/main');
      }, 800);
    } else {
    router.push(route.query.redirect as string || '/main');
    }
  } catch (error) {
    console.error("登录出错:", error);
    isLoading.value = false;
    if (loginButton) {
      loginButton.classList.remove('animate-pulse');
    }
  }
};

// 登录成功粒子爆炸效果
const createSuccessParticles = () => {
  const container = document.querySelector('.login-page') as HTMLElement;
  if (!container) return;

  const particlesContainer = document.createElement('div');
  particlesContainer.classList.add('success-particles-container');

  // 创建粒子
  for (let i = 0; i < 100; i++) {
    const particle = document.createElement('div');
    particle.classList.add('success-particle');

    // 随机位置、大小和颜色
    const size = Math.random() * 8 + 4;
    const xPos = 50 + (Math.random() - 0.5) * 40;
    const yPos = 50 + (Math.random() - 0.5) * 40;
    const speed = Math.random() * 3 + 1;
    const delay = Math.random() * 0.5;
    const rotation = Math.random() * 360;
    const color = [
      '#4CAF50', '#2196F3', '#FFC107', '#E91E63', '#9C27B0'
    ][Math.floor(Math.random() * 5)];

    particle.style.width = `${size}px`;
    particle.style.height = `${size}px`;
    particle.style.left = `${xPos}%`;
    particle.style.top = `${yPos}%`;
    particle.style.backgroundColor = color;
    particle.style.animationDuration = `${speed}s`;
    particle.style.animationDelay = `${delay}s`;
    particle.style.transform = `rotate(${rotation}deg)`;

    particlesContainer.appendChild(particle);
  }

  container.appendChild(particlesContainer);
};

// 定义表单校验规则
const rules = reactive<FormRules>({
  name: [
    {
      required: true,
      message: "用户名不能为空",
      trigger: ['change']
    },
    {
      min: 3,
      max: 10,
      message: "长度需为3-10位",
      trigger: ['blur']
    },
    {
      pattern: /^[a-zA-Z0-9_]+$/,
      message: "只允许字母、数字和下划线",
      trigger: ['blur']
    },
  ],
  password: [
    {
      required: true,
      message: "密码不能为空",
      trigger: "change"
    },
    {
      min: 6,
      max: 18,
      message: "长度需为6-18位",
      trigger: ['blur']
    },
  ]
});

// 定义是否登录加载中
const isLoading = ref(false);
// 表单实例
const formRef = ref<FormInstance>();
// 密码可见性
const passwordVisible = ref(false);
const togglePasswordVisibility = () => {
  passwordVisible.value = !passwordVisible.value;

  // 添加图标动画
  const icon = document.querySelector('.password-toggle .el-icon') as HTMLElement;
  if (icon) {
    icon.classList.add('animate-rotate-in');
    setTimeout(() => {
      icon.classList.remove('animate-rotate-in');
    }, 800);
  }
};

// 注册跳转
const goToRegister = () => {
  // 添加页面离开动画
  const loginContainer = document.querySelector('.login-container') as HTMLElement;
  if (loginContainer) {
    loginContainer.classList.add('login-slide-out');
    setTimeout(() => {
      router.push('/register');
    }, 400);
  } else {
    router.push('/register');
  }
}

// 创建波浪背景和动画元素
const createAnimatedBackground = () => {
  // 创建波浪容器
  const waveContainer = document.createElement('div');
  waveContainer.classList.add('login-wave-container');

  // 创建多层波浪
  for (let i = 1; i <= 4; i++) {
    const wave = document.createElement('div');
    wave.classList.add('login-wave', `wave-${i}`);
    waveContainer.appendChild(wave);
  }

  // 添加到登录页面
  const loginPage = document.querySelector('.login-page');
  if (loginPage) {
    loginPage.insertBefore(waveContainer, loginPage.firstChild);
  }

  // 添加浮动粒子
  createFloatingParticles();

  // 添加鼠标跟随光晕
  createMouseGlow();

  // 添加装饰性圆圈
  createDecorativeElements();
};

// 创建浮动粒子
const createFloatingParticles = () => {
  const particlesContainer = document.createElement('div');
  particlesContainer.classList.add('particles-container');

  // 创建多个粒子
  for (let i = 0; i < 25; i++) {
    const particle = document.createElement('div');
    particle.classList.add('particle');

    // 随机位置和大小
    const size = Math.random() * 10 + 5;
    particle.style.width = `${size}px`;
    particle.style.height = `${size}px`;
    particle.style.left = `${Math.random() * 100}%`;
    particle.style.top = `${Math.random() * 100}%`;

    // 随机动画延迟和持续时间
    particle.style.animationDelay = `${Math.random() * 5}s`;
    particle.style.animationDuration = `${Math.random() * 10 + 10}s`;

    // 随机透明度
    particle.style.opacity = `${Math.random() * 0.5 + 0.1}`;

    particlesContainer.appendChild(particle);
  }

  // 添加到登录页面
  const loginPage = document.querySelector('.login-page');
  if (loginPage) {
    loginPage.insertBefore(particlesContainer, loginPage.firstChild);
  }
};

// 创建鼠标跟随光晕
const createMouseGlow = () => {
  const glow = document.createElement('div');
  glow.classList.add('mouse-glow');

  // 添加到登录页面
  const loginPage = document.querySelector('.login-page');
  if (loginPage) {
    loginPage.appendChild(glow);
  }
};

// 创建装饰性元素
const createDecorativeElements = () => {
  const decorContainer = document.createElement('div');
  decorContainer.classList.add('decorative-elements');

  // 添加多个装饰性圆圈
  for (let i = 0; i < 6; i++) {
    const circle = document.createElement('div');
    circle.classList.add('decorative-circle', `circle-${i + 1}`);
    decorContainer.appendChild(circle);
  }

  // 添加到登录页面
  const loginPage = document.querySelector('.login-page');
  if (loginPage) {
    loginPage.insertBefore(decorContainer, loginPage.firstChild);
  }
};

// 页面加载动画
onMounted(() => {
  // 注册鼠标移动事件
  window.addEventListener('mousemove', handleMouseMove);

  // 标题打字机效果
  setTimeout(() => {
    applyTypingEffect('.login-subtitle', '智能语音解决方案助力教育数字化转型', 50);
  }, 500);

  // 登录表单淡入动画
  const loginCard = document.querySelector('.login-card') as HTMLElement;
  if (loginCard) {
    loginCard.classList.add('animate-fade-in-up');
  }

  // 表单项逐个淡入
  const formItems = document.querySelectorAll('.login-form .el-form-item');
  formItems.forEach((item, index) => {
    const el = item as HTMLElement;
    el.style.opacity = '0';
    el.style.transform = 'translateY(20px)';
    el.style.transition = 'all 0.4s ease-out';

    setTimeout(() => {
      el.style.opacity = '1';
      el.style.transform = 'translateY(0)';
    }, 200 + index * 150);
  });

  // 添加动画背景
  createAnimatedBackground();

  // 输入框聚焦特效
  const inputs = document.querySelectorAll('.el-input__wrapper');
  inputs.forEach(input => {
    input.addEventListener('focus', (e) => {
      const target = e.currentTarget as HTMLElement;
      target.style.transform = 'scale(1.02)';
      target.style.boxShadow = '0 4px 20px rgba(var(--primary-color-rgb), 0.25)';
      target.style.transition = 'all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1)';
    });

    input.addEventListener('blur', (e) => {
      const target = e.currentTarget as HTMLElement;
      target.style.transform = 'scale(1)';
      target.style.boxShadow = '0 1px 2px rgba(0, 0, 0, 0.05)';
    });
  });
});
</script>

<template>
  <div class="login-page" @mousemove="handleMouseMove">
    <div class="login-container animate-fade-in">
      <div class="login-header">
        <img src="@/assets/favicon.ico" alt="AI-Voice Logo" class="login-logo animate-float" />
        <h1 class="login-title animate-fade-in-up">欢迎使用 AI-Voice</h1>
        <p class="login-subtitle"></p>
      </div>

    <el-card class="login-card">
        <h2 class="form-title">账号登录</h2>

        <el-form
          :model="form"
          :rules="rules"
          ref="formRef"
          label-position="top"
          size="large"
          class="login-form"
        >
        <el-form-item label="用户名" prop="name">
            <el-input
              v-model="form.name"
              placeholder="请输入用户名"
              clearable
              :prefix-icon="'User'"
              class="input-with-effects"
            />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="form.password"
            :type="passwordVisible ? 'text' : 'password'"
            placeholder="请输入密码"
            clearable
              :prefix-icon="'Lock'"
            @keyup.enter="onSubmit"
              class="input-with-effects"
          >
              <template #suffix>
                <el-icon @click="togglePasswordVisibility" class="password-toggle" :size="22">
                <IEpView v-if="passwordVisible"/>
                <IEpHide v-else />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

          <div class="form-options">
            <el-checkbox v-model="rememberMe" label="记住我" size="small" class="remember-checkbox" />
            <el-link type="primary" :underline="false" href="#" class="forgot-password hover-bounce">忘记密码?</el-link>
          </div>

          <el-form-item class="login-button-wrapper">
            <el-button
              type="primary"
              @click="onSubmit"
              :loading="isLoading"
              size="large"
              class="login-button animate-ripple"
            >
              <span class="login-button-text">登录</span>
              <span class="button-effect"></span>
          </el-button>
        </el-form-item>

          <div class="register-link">
            还没有账号? <el-link type="primary" @click="goToRegister" :underline="false" class="hover-scale">立即注册</el-link>
          </div>

          <div class="safe-login-info">
            <i class="secure-icon">🔒</i> 安全登录，保障您的账户安全
          </div>
      </el-form>
    </el-card>
    </div>

    <!-- 登录卡片倒影效果 -->
    <div class="card-reflection"></div>
  </div>
</template>

<style scoped lang="scss">
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--primary-light) 0%, var(--primary-dark) 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: var(--spacing-6);
  position: relative;
  overflow: hidden;
}

/* 鼠标跟随光晕 */
.mouse-glow {
  position: fixed;
    width: 400px;
  height: 400px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.15) 0%, rgba(255, 255, 255, 0.05) 40%, transparent 70%);
  transform: translate(-50%, -50%);
  pointer-events: none;
  z-index: 1;
  transition: opacity 0.3s ease;
  will-change: transform, left, top;
}

/* 登录成功粒子效果 */
.success-particles-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 100;
}

.success-particle {
  position: absolute;
  border-radius: 50%;
  animation: particle-explosion 2s ease-out forwards;
  opacity: 0;
}

@keyframes particle-explosion {
  0% {
    transform: translate(-50%, -50%) scale(0);
    opacity: 1;
  }
  50% {
    opacity: 1;
  }
  100% {
    transform: translate(
      calc(-50% + (var(--random-x, 0) * 1000px)),
      calc(-50% + (var(--random-y, 0) * 1000px))
    ) scale(1);
    opacity: 0;
  }
}

/* 装饰性元素 */
.decorative-elements {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.decorative-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.15;
  background: white;
  filter: blur(6px);

  &.circle-1 {
    top: 15%;
    right: 10%;
    width: 150px;
    height: 150px;
    animation: float-circle 8s infinite ease-in-out;
  }

  &.circle-2 {
    bottom: 15%;
    left: 10%;
    width: 100px;
    height: 100px;
    animation: float-circle 12s infinite ease-in-out reverse;
  }

  &.circle-3 {
    top: 50%;
    left: 15%;
    width: 80px;
    height: 80px;
    animation: float-circle 10s infinite ease-in-out 2s;
  }

  &.circle-4 {
    bottom: 30%;
    right: 15%;
    width: 60px;
    height: 60px;
    animation: float-circle 14s infinite ease-in-out 1s;
  }

  &.circle-5 {
    top: 25%;
    left: 25%;
    width: 40px;
    height: 40px;
    animation: float-circle 16s infinite ease-in-out 4s;
  }

  &.circle-6 {
    top: 70%;
    right: 25%;
    width: 70px;
    height: 70px;
    animation: float-circle 18s infinite ease-in-out 3s;
  }
}

@keyframes float-circle {
  0%, 100% {
    transform: translate(0, 0);
  }
  25% {
    transform: translate(-10px, 15px);
  }
  50% {
    transform: translate(5px, -15px);
  }
  75% {
    transform: translate(10px, 5px);
  }
}

/* 背景波浪动画 */
.login-wave-container {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 0;
}

.login-wave {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 200%;
  height: 100px;
  background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1440 320'%3E%3Cpath fill='%23ffffff' fill-opacity='0.2' d='M0,192L48,197.3C96,203,192,213,288,229.3C384,245,480,267,576,250.7C672,235,768,181,864,181.3C960,181,1056,235,1152,234.7C1248,235,1344,181,1392,154.7L1440,128L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z'%3E%3C/path%3E%3C/svg%3E") repeat-x;
  background-size: 100% 100px;
  will-change: transform;
}

.wave-1 {
  animation: wave 15s linear infinite;
  z-index: 1;
  opacity: 0.3;
  bottom: 0;
}

.wave-2 {
  animation: wave 10s linear infinite;
  z-index: 2;
  opacity: 0.5;
  animation-delay: -5s;
  bottom: 10px;
}

.wave-3 {
  animation: wave 8s linear infinite;
  z-index: 3;
  opacity: 0.2;
  animation-delay: -2s;
  bottom: 15px;
}

.wave-4 {
  animation: wave 12s linear infinite;
  z-index: 4;
  opacity: 0.7;
  animation-delay: -5s;
  bottom: 20px;
}

@keyframes wave {
  0% {
    transform: translateX(0);
  }
  50% {
    transform: translateX(-25%);
  }
  100% {
    transform: translateX(-50%);
  }
}

/* 粒子特效 */
.particles-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 0;
}

.particle {
  position: absolute;
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 50%;
  animation: float-particle 15s ease-in-out infinite;
  z-index: 0;
  filter: blur(1px);
  box-shadow: 0 0 10px rgba(255,255,255,0.5);
}

@keyframes float-particle {
  0% {
    transform: translateY(0) translateX(0) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: var(--particle-opacity, 0.5);
  }
  90% {
    opacity: var(--particle-opacity, 0.5);
  }
  100% {
    transform: translateY(-100vh) translateX(100px) rotate(360deg);
    opacity: 0;
  }
}

/* 倒影效果 */
.card-reflection {
  position: absolute;
  bottom: -20px;
  left: 50%;
  transform: translateX(-50%) rotateX(180deg) perspective(300px);
  width: 460px;
  height: 460px;
  background: linear-gradient(to bottom, rgba(255,255,255,0.1), transparent);
  z-index: -1;
  filter: blur(20px);
  opacity: 0.3;
  border-radius: var(--border-radius-lg);
  pointer-events: none;
}

.login-container {
  width: 100%;
  max-width: 460px;
  position: relative;
  z-index: 2;
  will-change: transform, opacity;

  /* 退出动画 */
  &.login-fade-out {
    animation: fade-out 0.6s forwards;
  }

  &.login-slide-out {
    animation: slide-out 0.4s forwards;
  }

  @keyframes fade-out {
    to {
      opacity: 0;
      transform: scale(0.9);
    }
  }

  @keyframes slide-out {
    to {
      opacity: 0;
      transform: translateX(-100px);
    }
  }
}

/* 晃动错误动画 */
@keyframes shake {
  0%, 100% { transform: translateX(0); }
  10%, 30%, 50%, 70%, 90% { transform: translateX(-10px); }
  20%, 40%, 60%, 80% { transform: translateX(10px); }
}

.animate-shake {
  animation: shake 0.8s cubic-bezier(.36,.07,.19,.97) both;
}

/* 旋转动画 */
@keyframes rotate-in {
  from { transform: rotate(-180deg); opacity: 0; }
  to { transform: rotate(0); opacity: 1; }
}

.animate-rotate-in {
  animation: rotate-in 0.8s cubic-bezier(0.34, 1.56, 0.64, 1);
}

/* 浮动动画 */
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.animate-float {
  animation: float 3s ease-in-out infinite;
}

/* 弹跳动画 */
@keyframes bounce {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

.hover-bounce:hover {
  animation: bounce 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.login-header {
  text-align: center;
  margin-bottom: var(--spacing-8);

  .login-logo {
    width: 64px;
    height: 64px;
    margin-bottom: var(--spacing-4);
    filter: drop-shadow(0 0 10px rgba(255, 255, 255, 0.5));
    transition: filter 0.3s ease;

    &:hover {
      filter: drop-shadow(0 0 15px rgba(255, 255, 255, 0.8));
    }
  }

  .login-title {
    color: white;
    font-size: 28px;
    font-weight: 600;
    margin-bottom: var(--spacing-2);
    font-family: var(--font-family-heading);
    text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
    background: linear-gradient(90deg, #ffffff, #d0e6ff);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }

  .login-subtitle {
    color: rgba(255, 255, 255, 0.9);
    font-size: 16px;
    height: 24px; /* 防止高度塌陷 */
    overflow: hidden;
    white-space: nowrap;
    border-right: 2px solid white;
    padding-right: 5px;
    text-shadow: 0 0 5px rgba(255,255,255,0.3);
  }
}

.login-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25),
              0 0 40px rgba(var(--primary-color-rgb), 0.2);
  padding: var(--spacing-6);
  border: none;
  backdrop-filter: blur(10px);
  will-change: transform;
  transition: all 0.3s ease;
  overflow: hidden;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: linear-gradient(to right, transparent, rgba(255,255,255,0.1), transparent);
    transform: rotate(45deg);
    animation: shimmer 6s linear infinite;
    z-index: 0;
  }

  @keyframes shimmer {
    0% { transform: translateX(-100%) rotate(45deg); }
    100% { transform: translateX(100%) rotate(45deg); }
  }

  &:hover {
    box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25),
                0 0 50px rgba(var(--primary-color-rgb), 0.3);
      transform: translateY(-5px);
    }

  .form-title {
    text-align: center;
    font-size: 22px;
        font-weight: 600;
    color: var(--neutral-900);
    margin-bottom: var(--spacing-6);
    position: relative;
    z-index: 1;

    &::after {
      content: '';
      position: absolute;
      bottom: -10px;
      left: 50%;
      transform: translateX(-50%);
      width: 40px;
      height: 3px;
      background: var(--primary-color);
      border-radius: 3px;
      transition: width 0.3s ease;
    }

    &:hover::after {
      width: 60px;
    }
  }
}

/* 淡入上移动画 */
@keyframes fade-in-up {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-in-up {
  animation: fade-in-up 0.8s cubic-bezier(0.34, 1.56, 0.64, 1);
}

/* 淡入动画 */
@keyframes fade-in {
  from { opacity: 0; }
  to { opacity: 1; }
}

.animate-fade-in {
  animation: fade-in 1s ease-out;
}

.login-form {
  position: relative;
  z-index: 1;

  .el-form-item {
    margin-bottom: var(--spacing-6);
    will-change: transform, opacity;
  }

  .input-with-effects {
    &:deep(.el-input__wrapper) {
      border-radius: var(--border-radius-md);
      padding: var(--spacing-2) var(--spacing-3);
      transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
      background-color: var(--neutral-50);
      border: 1px solid var(--neutral-200);
      box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
      will-change: transform, box-shadow;
      overflow: hidden;
      position: relative;

      &::before {
        content: '';
        position: absolute;
        left: -150%;
        width: 100%;
        height: 100%;
        background: linear-gradient(90deg, transparent, rgba(255,255,255,0.8), transparent);
        transform: skewX(-25deg);
        transition: all 0.3s ease;
        opacity: 0;
      }

      &.is-focus {
        border-color: var(--primary-color);

        &::before {
          left: 150%;
          opacity: 1;
          transition: all 0.5s ease;
        }
      }
    }

    &:deep(.el-input__prefix) {
      font-size: 18px;
      color: var(--neutral-500);
    }
  }

  .password-toggle {
    cursor: pointer;
    padding: 4px;
    border-radius: 50%;
    transition: all 0.3s ease;
    font-size: 18px; /* 调整图标大小 */

    &:hover {
      background-color: var(--neutral-100);
      transform: scale(1.1);
    }

          .el-icon {
      will-change: transform;
    }
  }

  .form-options {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: var(--spacing-6);

    .remember-checkbox {
      &:deep(.el-checkbox__inner) {
        transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);

        &:hover {
          transform: scale(1.1);
          box-shadow: 0 0 8px rgba(var(--primary-color-rgb), 0.4);
        }
      }
    }

    .forgot-password {
      font-size: 14px;
      transition: all 0.3s ease;
      position: relative;

      &::after {
        content: '';
        position: absolute;
        bottom: -2px;
        left: 0;
        width: 0;
        height: 1px;
        background-color: var(--primary-color);
        transition: width 0.3s ease;
      }

      &:hover::after {
        width: 100%;
      }
    }
  }

  .login-button-wrapper {
    margin-bottom: var(--spacing-4);
  }

  .login-button {
    width: 100%;
    height: 48px;
    font-size: 16px;
    font-weight: 500;
    letter-spacing: 0.5px;
    background: linear-gradient(90deg, var(--primary-color), var(--primary-light));
    border: none;
    border-radius: var(--border-radius-md);
    box-shadow: 0 4px 10px rgba(var(--primary-color-rgb), 0.5);
    transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
    position: relative;
    overflow: hidden;

    .login-button-text {
      position: relative;
      z-index: 2;
      transition: all 0.3s ease;
    }

    .button-effect {
      position: absolute;
      top: 50%;
      left: 50%;
      width: 0;
      height: 0;
      background: rgba(255, 255, 255, 0.3);
      border-radius: 50%;
      transform: translate(-50%, -50%);
      opacity: 0;
      transition: width 0.5s ease, height 0.5s ease, opacity 0.5s ease;
    }

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 15px rgba(var(--primary-color-rgb), 0.6);

      .button-effect {
        width: 300px;
        height: 300px;
        opacity: 0.3;
      }
    }

    &:active {
      transform: translateY(1px);
      box-shadow: 0 2px 5px rgba(var(--primary-color-rgb), 0.4);
    }

    /* 脉冲动画 */
    &.animate-pulse {
      animation: pulse 1.2s infinite;
    }

    @keyframes pulse {
      0% {
        box-shadow: 0 0 0 0 rgba(var(--primary-color-rgb), 0.7);
      }
      70% {
        box-shadow: 0 0 0 10px rgba(var(--primary-color-rgb), 0);
      }
      100% {
        box-shadow: 0 0 0 0 rgba(var(--primary-color-rgb), 0);
      }
    }

    /* 登录成功动画 */
    &.login-success {
      background: linear-gradient(90deg, var(--success-color), #85ce61);
      box-shadow: 0 4px 10px rgba(103, 194, 58, 0.5);
      animation: success-animation 0.6s forwards;
    }

    @keyframes success-animation {
      0% {
        transform: scale(1);
      }
      50% {
        transform: scale(1.05);
      }
      100% {
        transform: scale(1);
      }
    }
  }

  .register-link {
    text-align: center;
    font-size: 14px;
    color: var(--neutral-600);
    margin-bottom: var(--spacing-4);

    .el-link {
      font-weight: 500;
      margin-left: 4px;
      transition: all 0.3s ease;

      &:hover {
        transform: scale(1.05);
        text-shadow: 0 0 5px rgba(var(--primary-color-rgb), 0.3);
      }
    }
  }

  .safe-login-info {
    text-align: center;
    font-size: 12px;
    color: var(--neutral-500);
    padding-top: var(--spacing-2);
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 5px;

    .secure-icon {
      animation: float 2s ease-in-out infinite;
    }
  }
}

/* 适配暗色模式 */
@media (prefers-color-scheme: dark) {
  .login-card {
    background-color: rgba(30, 30, 30, 0.9);

    .form-title {
      color: var(--neutral-100);
    }
  }

  .login-form {
    .input-with-effects {
      &:deep(.el-input__wrapper) {
        background-color: rgba(40, 40, 40, 0.8);
        border-color: var(--neutral-700);
      }
    }

    .safe-login-info {
      color: var(--neutral-400);
    }

    .register-link {
      color: var(--neutral-400);
    }
  }
}
</style>

<!-- src/views/register/RegisterView.vue -->
<script setup lang="ts">
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElNotification } from 'element-plus'
import { View, Hide } from '@element-plus/icons-vue'
import type { FormInstance } from 'element-plus'

const router = useRouter()
const formRef = ref<FormInstance>()
const isLoading = ref(false)
const passwordVisible = ref(false)
const agreedToTerms = ref(false)
const mouseGlowRef = ref<HTMLElement | null>(null)

// 添加打字机效果函数
function applyTypingEffect(selector: string, text: string, speed = 100) {
  const element = document.querySelector(selector) as HTMLElement
  if (!element) return

  element.innerText = ''

  let i = 0
  const typing = setInterval(() => {
    if (i < text.length) {
      element.innerText += text.charAt(i)
      i++
    } else {
      clearInterval(typing)
    }
  }, speed)
}

const form = reactive({
  name: '',
  password: '',
  passwordConfirm: ''
})

const rules = {
  name: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 个字符', trigger: 'blur' }
  ],
  passwordConfirm: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: Function) => {
        if (value !== form.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 鼠标跟踪功能
const mousePosition = reactive({ x: 0, y: 0 })

function handleMouseMove(e: MouseEvent) {
  mousePosition.x = e.clientX
  mousePosition.y = e.clientY

  if (mouseGlowRef.value) {
    mouseGlowRef.value.style.left = `${mousePosition.x}px`
    mouseGlowRef.value.style.top = `${mousePosition.y}px`
  }
}

// 表单提交处理
async function onSubmit() {
  if (!formRef.value) return

  try {
    await formRef.value.validate()

    if (!agreedToTerms.value) {
      ElMessage.warning('请先同意用户协议和隐私政策')
      return
    }

    isLoading.value = true

    // 模拟API调用
    setTimeout(() => {
      // 注册成功
      isLoading.value = false

      // 成功动画和通知
      const registerContainer = document.querySelector('.register-container')
      registerContainer?.classList.add('register-fade-out')

      // 创建粒子爆炸效果
      createSuccessParticles()

      ElNotification({
        title: '注册成功',
        message: '欢迎加入 AI-Voice，即将跳转到登录页面',
        type: 'success',
        duration: 3000
      })

      // 延迟跳转到登录页
      setTimeout(() => {
        router.push('/login')
      }, 2000)
    }, 1500)
  } catch (error) {
    console.error('表单验证失败:', error)

    // 错误动画
    const registerCard = document.querySelector('.register-card')
    registerCard?.classList.add('animate-shake')

    setTimeout(() => {
      registerCard?.classList.remove('animate-shake')
    }, 800)
  }
}

// 密码强度检测
const passwordStrength = computed(() => {
  const password = form.password
  if (!password) return 0

  let strength = 0

  // 长度检查
  if (password.length >= 6) strength += 1
  if (password.length >= 10) strength += 1

  // 复杂度检查
  if (/[A-Z]/.test(password)) strength += 1
  if (/[0-9]/.test(password)) strength += 1
  if (/[^A-Za-z0-9]/.test(password)) strength += 1

  return Math.min(5, strength)
})

const passwordStrengthText = computed(() => {
  const strength = passwordStrength.value
  const texts = ['极弱', '弱', '一般', '强', '很强', '非常强']
  return texts[strength]
})

const passwordStrengthColor = computed(() => {
  const strength = passwordStrength.value
  const colors = [
    '#F56C6C', // 极弱 - 红色
    '#E6A23C', // 弱 - 橙色
    '#E6A23C', // 一般 - 橙色
    '#67C23A', // 强 - 绿色
    '#67C23A', // 很强 - 绿色
    '#67C23A'  // 非常强 - 绿色
  ]
  return colors[strength]
})

// 创建成功动画粒子
function createSuccessParticles() {
  const container = document.createElement('div')
  container.className = 'success-particles-container'
  document.body.appendChild(container)

  const centerX = window.innerWidth / 2
  const centerY = window.innerHeight / 2

  // 创建100个粒子
  for (let i = 0; i < 100; i++) {
    const particle = document.createElement('div')
    particle.className = 'success-particle'

    // 随机大小 (3-8px)
    const size = Math.random() * 5 + 3
    particle.style.width = `${size}px`
    particle.style.height = `${size}px`

    // 随机颜色
    const hue = Math.random() * 60 + 120 // 绿色范围
    particle.style.backgroundColor = `hsl(${hue}, 80%, 60%)`

    // 随机方向
    const randomX = Math.random() * 2 - 1 // -1 到 1
    const randomY = Math.random() * 2 - 1 // -1 到 1

    particle.style.setProperty('--random-x', randomX.toString())
    particle.style.setProperty('--random-y', randomY.toString())

    // 设置初始位置在中心
    particle.style.left = `${centerX}px`
    particle.style.top = `${centerY}px`

    container.appendChild(particle)
  }

  // 2秒后移除粒子容器
  setTimeout(() => {
    document.body.removeChild(container)
  }, 2000)
}

// 创建交互式背景元素
function createBackgroundElements() {
  // 鼠标跟随光晕
  const mouseGlow = document.createElement('div')
  mouseGlow.className = 'mouse-glow'
  mouseGlowRef.value = mouseGlow
  document.body.appendChild(mouseGlow)

  // 创建装饰性浮动圆
  const elementsContainer = document.createElement('div')
  elementsContainer.className = 'decorative-elements'

  for (let i = 1; i <= 6; i++) {
    const circle = document.createElement('div')
    circle.className = `decorative-circle circle-${i}`
    elementsContainer.appendChild(circle)
  }

  document.querySelector('.register-page')?.appendChild(elementsContainer)

  // 添加波浪装饰
  const wavesContainer = document.createElement('div')
  wavesContainer.className = 'decorative-waves'

  for (let i = 1; i <= 3; i++) {
    const wave = document.createElement('div')
    wave.className = `decorative-wave wave-${i}`
    wavesContainer.appendChild(wave)
  }

  document.querySelector('.register-page')?.appendChild(wavesContainer)
}

// 切换密码可见性
function togglePasswordVisibility() {
  passwordVisible.value = !passwordVisible.value
}

// 前往登录页
function goToLogin() {
  router.push('/login')
}

onMounted(() => {
  // 初始化背景动画元素
  createBackgroundElements()

  // 添加窗口大小改变事件监听
  window.addEventListener('resize', handleResize)

  // 添加打字机效果
  setTimeout(() => {
    applyTypingEffect('.register-subtitle', '创建您的账号，开启智能语音之旅', 50)
  }, 500)
})

onUnmounted(() => {
  // 移除事件监听器
  window.removeEventListener('resize', handleResize)

  // 清理创建的DOM元素
  if (mouseGlowRef.value && mouseGlowRef.value.parentNode) {
    mouseGlowRef.value.parentNode.removeChild(mouseGlowRef.value)
  }
})

// 处理窗口大小改变
function handleResize() {
  // 重新适应窗口大小变化
  // 这里可以添加任何需要根据窗口大小调整的逻辑
}
</script>

<template>
  <div class="register-page" @mousemove="handleMouseMove">
    <div class="register-container animate-fade-in">
      <div class="register-header">
        <img src="@/assets/favicon.ico" alt="AI-Voice Logo" class="register-logo animate-float" />
        <h1 class="register-title animate-fade-in-up">加入 AI-Voice</h1>
        <p class="register-subtitle"></p>
      </div>

      <el-card class="register-card">
        <h2 class="form-title">账号注册</h2>

        <el-form
          :model="form"
          :rules="rules"
          ref="formRef"
          label-position="top"
          size="large"
          class="register-form"
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
              class="input-with-effects"
            >
              <template #suffix>
                <el-icon @click="togglePasswordVisibility" class="password-toggle" :size="22">
                  <View v-if="passwordVisible"/>
                  <Hide v-else />
                </el-icon>
              </template>
            </el-input>

            <!-- 密码强度提示 -->
            <div v-if="form.password" class="password-strength">
              <div class="strength-text">密码强度: <span :style="{ color: passwordStrengthColor }">{{ passwordStrengthText }}</span></div>
              <div class="strength-bar-container">
                <div class="strength-bar" :style="{ width: `${passwordStrength * 20}%`, backgroundColor: passwordStrengthColor }"></div>
              </div>
            </div>
          </el-form-item>

          <el-form-item label="确认密码" prop="passwordConfirm">
            <el-input
              v-model="form.passwordConfirm"
              :type="passwordVisible ? 'text' : 'password'"
              placeholder="请确认密码"
              clearable
              :prefix-icon="'Lock'"
              class="input-with-effects"
            >
              <template #suffix>
                <el-icon @click="togglePasswordVisibility" class="password-toggle" :size="22">
                  <View v-if="passwordVisible"/>
                  <Hide v-else />
                </el-icon>
              </template>
            </el-input>
          </el-form-item>

          <div class="form-agreement">
            <el-checkbox v-model="agreedToTerms" label="我已阅读并同意" size="small" class="agreement-checkbox" />
            <el-link type="primary" :underline="false" href="#" class="hover-bounce">服务条款</el-link>
            和
            <el-link type="primary" :underline="false" href="#" class="hover-bounce">隐私政策</el-link>
          </div>

          <el-form-item class="register-button-wrapper">
            <el-button
              type="primary"
              @click="onSubmit"
              :loading="isLoading"
              :disabled="!agreedToTerms"
              size="large"
              class="register-button animate-ripple"
            >
              <span class="register-button-text">立即注册</span>
              <span class="button-effect"></span>
            </el-button>
          </el-form-item>

          <div class="login-link">
            已有账号? <el-link type="primary" @click="goToLogin" :underline="false" class="hover-scale">返回登录</el-link>
          </div>

          <div class="safe-register-info">
            <i class="secure-icon">🔒</i> 安全注册，保障您的账户安全
          </div>
        </el-form>
      </el-card>
    </div>

    <!-- 注册卡片倒影效果 -->
    <div class="card-reflection"></div>
  </div>
</template>

<style scoped lang="scss">
.register-page {
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

/* 注册成功粒子效果 */
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
.decorative-waves {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100px;
  overflow: hidden;
  z-index: 0;
}

.decorative-wave {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 200%;
  height: 100%;
  background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1440 320'%3E%3Cpath fill='%23ffffff' fill-opacity='0.2' d='M0,192L48,197.3C96,203,192,213,288,229.3C384,245,480,267,576,250.7C672,235,768,181,864,181.3C960,181,1056,235,1152,234.7C1248,235,1344,181,1392,154.7L1440,128L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z'%3E%3C/path%3E%3C/svg%3E") repeat-x;
  background-size: 100% 100px;
  will-change: transform;

  &.wave-1 {
    animation: wave 15s linear infinite;
    z-index: 1;
    opacity: 0.3;
    bottom: 0;
  }

  &.wave-2 {
    animation: wave 10s linear infinite;
    z-index: 2;
    opacity: 0.5;
    animation-delay: -5s;
    bottom: 10px;
  }

  &.wave-3 {
    animation: wave 8s linear infinite;
    z-index: 3;
    opacity: 0.2;
    animation-delay: -2s;
    bottom: 15px;
  }
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

.register-container {
  width: 100%;
  max-width: 460px;
  position: relative;
  z-index: 2;
  will-change: transform, opacity;

  /* 退出动画 */
  &.register-fade-out {
    animation: fade-out 0.6s forwards;
  }

  &.register-slide-out {
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
      transform: translateX(100px);
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

.register-header {
  text-align: center;
  margin-bottom: var(--spacing-8);

  .register-logo {
    width: 64px;
    height: 64px;
    margin-bottom: var(--spacing-4);
    filter: drop-shadow(0 0 10px rgba(255, 255, 255, 0.5));
    transition: filter 0.3s ease;

    &:hover {
      filter: drop-shadow(0 0 15px rgba(255, 255, 255, 0.8));
    }
  }

  .register-title {
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

  .register-subtitle {
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

.register-card {
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

.register-form {
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

  /* 密码强度提示 */
  .password-strength {
    margin-top: var(--spacing-2);
    padding: var(--spacing-1) var(--spacing-2);

    .strength-text {
      font-size: 14px;
      color: var(--neutral-600);
      margin-bottom: 6px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .strength-bar-container {
      height: 4px;
      background-color: var(--neutral-200);
      border-radius: 2px;
      overflow: hidden;
    }

    .strength-bar {
      height: 100%;
      border-radius: 2px;
      transition: width 0.3s ease, background-color 0.3s ease;
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

  .form-agreement {
    display: flex;
    align-items: center;
    margin-bottom: var(--spacing-6);
    flex-wrap: wrap;
    gap: 4px;

    .agreement-checkbox {
      &:deep(.el-checkbox__inner) {
        transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);

        &:hover {
          transform: scale(1.1);
          box-shadow: 0 0 8px rgba(var(--primary-color-rgb), 0.4);
        }
      }
    }

    .el-link {
      font-size: 14px;
      transition: all 0.3s ease;
      margin: 0 4px;
    }
  }

  .register-button-wrapper {
    margin-bottom: var(--spacing-4);
  }

  .register-button {
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

    .register-button-text {
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

    /* 注册成功动画 */
    &.register-success {
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

  .login-link {
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

  .safe-register-info {
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
  .register-card {
    background-color: rgba(30, 30, 30, 0.9);

    .form-title {
      color: var(--neutral-100);
    }
  }

  .register-form {
    .input-with-effects {
      &:deep(.el-input__wrapper) {
        background-color: rgba(40, 40, 40, 0.8);
        border-color: var(--neutral-700);
      }
    }

    .password-strength {
      .strength-text {
        color: var(--neutral-400);
      }

      .strength-bar-container {
        background-color: var(--neutral-700);
      }
    }

    .safe-register-info {
      color: var(--neutral-400);
    }

    .login-link {
      color: var(--neutral-400);
    }
  }
}
</style>

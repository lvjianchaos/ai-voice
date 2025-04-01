// src/main.ts
import { createApp } from 'vue'
import { createPinia } from 'pinia'

import 'st-common-ui-vue3/es/st-common-ui-vue3.css'

import { StPopoverRegister } from 'st-common-ui-vue3'

import App from './App.vue'
import router from './router'

// Element Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// Motion动画库 (暂时注释，解决类型声明问题)
// import { MotionPlugin } from '@vueuse/motion'

// 导入自定义样式
import './assets/styles/main.scss'
import './assets/styles/animations.scss'
import './app.scss' // 全局动画效果

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus)
// app.use(MotionPlugin)

app.use(StPopoverRegister)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.mount('#app')

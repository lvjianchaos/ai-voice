// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import AppLayout from '@/components/layout/AppLayout.vue'
import MainLayout from '@/components/layout2/MainLayout.vue'
import indexView from '@/views/indexView.vue'
import AppIndexView from '@/views/AppIndexView.vue'
import { useTokenStore } from '@/stores/mytoken'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: AppLayout,
      children: [
        {
          path: '',
          name: 'appindex',
          component: AppIndexView,
        },
        {
          path: '/login',
          name: 'login',
          component: () => import('../views/login/LoginView.vue'),
        },
        {
          path: '/register',
          name: 'register',
          component: () => import('../views/register/RegisterView.vue'),
        },
        {
          path: '/about',
          name: 'about',
          // route level code-splitting
          // this generates a separate chunk (About.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () => import('../views/AboutView.vue'),
        },
      ],
    },
    {
      path: '/:xxx(.*)*',
      name: 'ErrorPage',
      component: () => import('../views/ErrorPage.vue'),
    },
    {
      path: '/main',
      name: 'main',
      component: MainLayout,
      // meta: { requireAuth: true, title: '主页' },
      meta: { title: '主页' },
      children: [
        {
          path: '',
          name: 'index',
          component: indexView,
          meta: { title: '默认页' },
        },
        {
          path: '/sample-library',
          name: 'sample-library',
          component: () => import('../views/sample/SampleIndex.vue'),
          meta: { title: '声音样本库' },
        },
        {
          path: '/voice-guide',
          name: 'voice-guide',
          component: () => import('../views/voice/VoiceGuideIndex.vue'),
          meta: { title: '个性化语音讲解' },
        },
        {
          path: '/courseware',
          name: 'courseware',
          component: () => import('../views/courseware/CoursewareIndex.vue'),
          meta: { title: '课件制作' },
        },
        {
          path: '/audio-subtitles',
          name: 'audio-subtitles',
          component: () => import('../views/audio/AudioSubtitlesIndex.vue'),
          meta: { title: '声音置换+字幕' },
        },
      ],
    },
    {
      path: '/test',
      name: 'test',
      component: () => import('../views/TestPage.vue'),
    },
  ],
})

router.beforeEach((to, from, next) => {
  if (to.matched.some((r) => r.meta?.requireAuth)) {
    const store = useTokenStore()
    if (!store.token?.access_token) {
      next({ name: 'login', query: { redirect: to.fullPath } })
      return
    }
  }

  next()
})

export default router

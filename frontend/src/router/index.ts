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
      // meta: { requireAuth: true },
      children: [
        {
          path: '',
          name: 'index',
          component: indexView,
        },
        {
          path: '/sample-library',
          name: 'sample-library',
          component: () => import('../views/sample/SampleIndex.vue'),
        },
        {
          path: '/sample-library/edit',
          name: 'sample-edit',
          component: () => import('../views/sample/SampleEdit.vue'),
        },
        {
          path: '/sample-library/create',
          name: 'sample-create',
          component: () => import('../views/sample/SampleCreate.vue'),
        },
      ],
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

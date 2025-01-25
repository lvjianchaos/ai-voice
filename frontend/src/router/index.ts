import { createRouter, createWebHistory } from 'vue-router'
import AppLayout from '@/components/layout/AppLayout.vue'
import MainLayout from '@/components/layout2/MainLayout.vue'
import indexView from '@/views/indexView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: AppLayout,
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
      children: [
        {
          path: '',
          name: 'index',
          component: indexView,
        },
      ],
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
})

export default router

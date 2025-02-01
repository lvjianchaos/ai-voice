import { createApp } from 'vue'
import { createPinia } from 'pinia'

import 'st-common-ui-vue3/es/st-common-ui-vue3.css'

import { StPopoverRegister } from 'st-common-ui-vue3'

import App from './App.vue'
import router from './router'

import '@/styles/index.scss'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.use(StPopoverRegister)

app.mount('#app')

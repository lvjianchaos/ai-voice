<script setup lang="ts">
import {StLoginPage1} from 'st-common-ui-vue3' // StLoginPage1 登录页面组件
import LoginFormByPwd from "./login-form-by-pwd.vue" // 用户名密码登录表单
import LoginFormByEmail from "./login-form-by-email.vue" // 邮箱登录表单
import LoginFormToggle from "./login-form-toggle.vue" // 登录表单切换组件
import {ref} from 'vue'

// 是否切换表单
const isToggleForm = ref(false)
/**
 * 切换表单
 */
const toggleForm = () => isToggleForm.value = !isToggleForm.value
</script>

<template>
  <div class="login-page">
    <!-- 登录页面1组件 -->
    <st-login-page1
      v-model="isToggleForm"
      circle-mask-color="#18A058"
      :panel-img-src="['','']"
    >
      <!-- 面板1顶部 -->
      <template #panel-1-top>
        <LoginFormToggle
          title="前往注册"
          sub-title="如果您没有账号，那么请您前往注册！"
          toggle-btn-text="前往用户注册"
          :toggle-handler="toggleForm"
        />
      </template>
      <!-- 表单1 -->
      <template #form-1>
        <LoginFormByPwd/>
      </template>
      <!-- 面板2顶部 -->
      <template #panel-2-top>
        <LoginFormToggle
          title="前往登录"
          sub-title="如果您已有账号，请您前往登录！"
          toggle-btn-text="前往登录"
          :toggle-handler="toggleForm"
        />
      </template>
      <!-- 表单2 -->
      <template #form-2>
        <LoginFormByEmail/>
      </template>
    </st-login-page1>
  </div>
</template>

<style scoped>

</style>

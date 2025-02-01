<script setup lang="ts">
/**
 * 组件参数
 */
withDefaults(
  defineProps<{
    // 标题
    title?: string;
    // 副标题
    subTitle?: string;
    // 切换按钮文字
    toggleBtnText: string;
    // 切换按钮点击事件处理函数
    toggleHandler?: () => void;
  }>(),
  {
    title: '',
    subTitle: '',
    toggleBtnText: '',
    toggleHandler: () => {}
  }
)
</script>

<template>
  <div class="login-form-toggle">
    <div class="login-form-toggle__title">{{ title }}</div>
    <div class="login-form-toggle__sub-title">{{ subTitle }}</div>
    <div class="login-form-toggle__toggle-btn">
      <n-button
        class="btn"
        color="transparent"
        type="primary"
        size="large"
        round
        @click="toggleHandler"
      >{{ toggleBtnText }}</n-button>
    </div>
  </div>
</template>

<style scoped lang="scss">
.login-form-toggle {
  color: #fff;

  .login-form-toggle__title {
    margin-bottom: 0.25rem;
    font-size: 1.5rem;
    font-weight: bold;
  }

  .login-form-toggle__sub-title {
    max-width: 240px;
    margin-bottom: 0.75rem;
    font-size: 0.875rem;
  }

  .login-form-toggle__toggle-btn {

    .btn {
      border: 2px solid #fff;
    }
  }
}
</style>

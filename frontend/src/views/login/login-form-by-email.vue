<script setup lang="ts">
import LoginForm from "./login-form.vue"
</script>

<template>
  <div class="login-form-by-email">
    <LoginForm title="注册">
      <el-form-item label="用户名">
        <el-input placeholder="请输入用户名" clearable/>
      </el-form-item>
      <el-form-item label="密码">
        <el-input placeholder="请输入密码" clearable/>
      </el-form-item>
      <el-form-item label="重新输入密码">
        <el-input placeholder="请重新输入密码" clearable/>
      </el-form-item>
    </LoginForm>
  </div>
</template>

<style scoped>

</style>

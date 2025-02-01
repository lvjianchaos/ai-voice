<script setup lang="ts">
import LoginForm from "./login-form.vue"
</script>

<template>
  <div class="login-form-by-pwd">
    <LoginForm>
      <el-form-item label="用户名">
        <el-input placeholder="请输入用户名" clearable/>
      </el-form-item>
      <el-form-item label="密码">
        <el-input placeholder="请输入密码" type="password" clearable show-password-on="click"/>
      </el-form-item>
    </LoginForm>
  </div>
</template>

<style scoped>

</style>

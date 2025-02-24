<!-- src/view/register/Register.vue -->
<script lang="ts" setup>
import type { FormInstance, FormRules } from 'element-plus';
import { register } from "@/api/users.ts";
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();
// 表单响应式数据
const form = reactive({
  name: "Chaos",
  password: "123456",
  passwordConfirm: "123456",
});
// 注册事件处理
const onSubmit = async () => {
  isLoading.value = true;
  // 表单校验
  await formRef.value?.validate().catch((err)=>{
    ElMessage.error("表单校验失败...");
    isLoading.value = false;
    throw err;
  });

  // 注册请求
  const data = await register(form).then((res) => {
    if(!res.data.success) {
      ElMessage.error('注册失败！');
      isLoading.value = false;
      throw new Error("注册信息有误");
    }
    return res.data;
  });
  console.log(data);

  isLoading.value = false;

  ElMessage.success('注册成功！');
  router.push(route.query.redirect as string || '/login');
};
// 定义表单校验规则
const rules = reactive<FormRules>({
  name: [
    {
      required: true,
      message: "用户名不能为空",
      trigger: ['change']
    },
    {
      min: 3,
      max: 10,
      message: "长度需为3-10位",
      trigger: ['blur']
    },
    {
      pattern: /^[a-zA-Z0-9_]+$/,
      message: "只允许字母、数字和下划线",
      trigger: ['blur']
    },
  ],
  password: [
    {
      required: true,
      message: "密码不能为空",
      trigger: "change" },
    {
      min: 6,
      max: 18,
      message: "长度需为6-18位",
      trigger: ['blur']
    },
  ],
  passwordConfirm: [
  {
      required: true,
      message: "请确认密码",
      trigger: "change",
    },
    {
      validator: (rule, value, callback) => {
        if (value !== form.password) {
          callback(new Error('确认密码与密码不一致'));
        } else {
          callback();
        }
      },
      trigger: ['blur'],
    }
  ]
});
// 定义是否注册加载中
const isLoading = ref(false);
// 表单实例
const formRef = ref<FormInstance>();
</script>

<template>
  <div class="register">
    <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" label-position="top" size="large">
      <h2>注册</h2>

      <el-form-item label="用户名" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>

      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" type="password"/>
      </el-form-item>

      <el-form-item label="确认密码" prop="passwordConfirm">
        <el-input v-model="form.passwordConfirm" type="password" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit" :loading="isLoading">注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped lang="scss">
.register {
  background-color: #ffffff;
  border-radius: 20px;
  height: 85vh;
  display: flex;
  justify-content: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  align-items: center;

  .el-form {
    width:300px;
    background-color: rgb(243.9, 244.2, 244.8);
    padding: 30px;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);

    .el-form-item {
      margin-top: 20px;
    }

    .el-button {
      width:100%;
      margin-top:30px;
    }
  }
}
</style>


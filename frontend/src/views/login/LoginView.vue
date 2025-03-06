<!-- src/views/login/LoginView.vue -->
<script lang="ts" setup>
import type { FormInstance, FormRules } from 'element-plus';
import { login } from "@/api/users.ts";
import { useTokenStore } from "@/stores/mytoken.ts";
import { useRouter, useRoute } from 'vue-router';
import { ref, reactive } from 'vue';

const store = useTokenStore();
const router = useRouter();
const route = useRoute();

// 表单响应式数据
const form = reactive({
  name: "Chaos",
  password: "123456",
});

// 登录事件处理
const onSubmit = async () => {
  isLoading.value = true;
  // 表单校验
  await formRef.value?.validate().catch((err) => {
    ElMessage.error("表单校验失败...");
    isLoading.value = false;
    throw err;
  });

  // 登录请求
  try {
    const data = await login(form);
    if (!data.data.success) {
      ElMessage.error('登录失败！');
      isLoading.value = false;
      throw new Error("登录信息有误");
    }

    // 保存token信息
    store.saveToken(data.data.data.token);

    isLoading.value = false;

    ElMessage.success('登录成功！');
    router.push(route.query.redirect as string || '/main');
  } catch (error) {
    console.error("登录出错:", error);
    isLoading.value = false;
  }
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
      trigger: "change"
    },
    {
      min: 6,
      max: 18,
      message: "长度需为6-18位",
      trigger: ['blur']
    },
  ]
});

// 定义是否登录加载中
const isLoading = ref(false);
// 表单实例
const formRef = ref<FormInstance>();
// 密码可见性
const passwordVisible = ref(false);
const togglePasswordVisibility = () => {
  passwordVisible.value = !passwordVisible.value;
};
</script>

<template>
  <div class="login">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>登录</h2>
        </div>
      </template>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" label-position="top" size="large">
        <el-form-item label="用户名" prop="name">
          <el-input v-model="form.name" placeholder="请输入用户名" clearable />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="form.password"
            :type="passwordVisible ? 'text' : 'password'"
            placeholder="请输入密码"
            clearable
            @keyup.enter="onSubmit"
          >

            <template #append>
              <el-icon @click="togglePasswordVisibility" style="cursor: pointer">
                <IEpView v-if="passwordVisible"/>
                <IEpHide v-else />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit" :loading="isLoading" size="large" block>
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.login {
  background: linear-gradient(to bottom, #f0f2f5, #e0e5ec);
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;

  .login-card {
    width: 400px;
    border-radius: 15px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease;

    &:hover {
      transform: translateY(-5px);
    }

    .card-header {
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 15px;

      h2 {
        font-size: 24px;
        font-weight: 600;
        color: #333;
      }
    }

    .el-form {
      padding: 20px;

      .el-form-item {
        margin-bottom: 25px;

        .el-input {
          border-radius: 8px;
          .el-icon {
            font-size: 20px;
          }
        }
      }

      .el-button {
        border-radius: 8px;
        width: 100%;
      }
    }
  }
}
</style>

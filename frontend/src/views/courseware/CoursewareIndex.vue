<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>有声课件制作</span>
        <el-button type="primary" class="generate-button" @click="generateCourseware">生成有声课件</el-button>
      </div>
    </template>

    <el-form label-width="100px">
      <!-- 课件上传 -->
      <el-form-item label="课件上传">
        <el-upload
          class="upload-demo"
          action="#"
          :http-request="uploadCourseware"
          :limit="1"
          accept=".ppt,.pptx,.pdf"
          :auto-upload="false"
          :on-remove="handleRemove"
          :file-list="fileList"
          :before-upload="beforeUpload"
        >
          <el-button type="primary">选择课件</el-button>
          <template #tip>
            <div class="el-upload__tip">只能上传PPT/PDF文件，且大小在3MB~20MB之间</div>
          </template>
        </el-upload>
      </el-form-item>

      <!-- 声音选择 -->
      <el-form-item label="声音选择">
        <el-select v-model="selectedVoice" placeholder="请选择声音">
          <el-option v-for="item in voiceOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <!-- 语速、语气、节奏调节 -->
      <el-form-item label="语速">
        <el-slider v-model="speed" :min="0.5" :max="1.5" :step="0.1" show-tooltip />
      </el-form-item>
      <el-form-item label="语气">
        <el-slider v-model="tone" :min="0" :max="1" :step="0.1" show-tooltip />
      </el-form-item>
      <el-form-item label="节奏">
        <el-slider v-model="rhythm" :min="0.5" :max="1.5" :step="0.1" show-tooltip />
      </el-form-item>
    </el-form>

    <!-- 有声课件生成结果展示 -->
    <div v-if="videoUrl">
      <el-divider />
      <h3>有声课件生成结果</h3>
      <video controls :src="videoUrl" width="640" height="360"></video>
      <el-button type="primary" @click="downloadVideo">下载视频</el-button>
    </div>
  </el-card>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import axios from 'axios';

// 响应式变量
const selectedVoice = ref('');
const speed = ref(1);
const tone = ref(0.5);
const rhythm = ref(1);
const videoUrl = ref('');
const fileList = ref([]);
const coursewareFile = ref<File | null>(null); // 用于存储上传的课件文件

// 声音样本选项 (需要从后端获取)
const voiceOptions = ref([
  { value: 'voice1', label: '男声1' },
  { value: 'voice2', label: '女声1' },
  { value: 'voice3', label: '男声2' },
]);

// 课件上传
const beforeUpload = (file: File) => {
  const isPPT = file.name.endsWith('.ppt') || file.name.endsWith('.pptx');
  const isLt20M = file.size / 1024 / 1024 > 3 && file.size / 1024 / 1024 < 20;

  if (!isPPT) {
    ElMessage.error('仅支持PPT文件!');
    return false;
  }
  if (!isLt20M) {
    ElMessage.error('文件大小必须在3MB~20MB之间!');
    return false;
  }
  return true;
};

const uploadCourseware = async (uploadOption: any) => {
  coursewareFile.value = uploadOption.file; // 保存上传的文件
};

const handleRemove = (file: any, fileList: any) => {
  console.log(file, fileList);
  coursewareFile.value = null; // 移除文件时清空
};

// 生成有声课件
const generateCourseware = async () => {
  if (!coursewareFile.value) {
    ElMessage.warning('请上传课件');
    return;
  }
  if (!selectedVoice.value) {
    ElMessage.warning('请选择声音');
    return;
  }

  const formData = new FormData();
  formData.append('courseware', coursewareFile.value);
  formData.append('voice', selectedVoice.value);
  formData.append('speed', speed.value.toString());
  formData.append('tone', tone.value.toString());
  formData.append('rhythm', rhythm.value.toString());

  try {
    const response = await axios.post(
      '/api/generate-courseware',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        responseType: 'blob', // 期望响应类型为 blob
      }
    );

    // 创建一个指向 blob 数据的 URL
    videoUrl.value = URL.createObjectURL(response.data);
    ElMessage.success('有声课件生成成功！');
  } catch (error) {
    console.error('有声课件生成失败:', error);
    ElMessage.error('有声课件生成失败，请稍后再试');
  }
};

// 下载视频
const downloadVideo = () => {
  if (videoUrl.value) {
    const link = document.createElement('a');
    link.href = videoUrl.value;
    link.download = 'courseware.mp4'; // 可以自定义文件名
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
};
</script>

<style lang="scss" scoped>
.box-card {
  width: 800px; /* 缩小卡片宽度 */
  margin: 0 auto; /* 居中显示 */
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.upload-demo {
  margin-bottom: 15px;
}

video {
  width: 100%;
  margin-bottom: 15px;
}

/* 调整 slider 的宽度 */
.el-slider {
  width: 300px; /* 根据需要调整宽度 */
}

/* 醒目的生成语音按钮样式 */
.generate-button {
  font-weight: bold;
  padding: 10px 20px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.2s ease-in-out;

  &:hover {
    transform: scale(1.05);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  }
}
</style>

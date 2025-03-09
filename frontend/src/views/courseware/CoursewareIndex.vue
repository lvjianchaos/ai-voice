<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>有声课件制作</span>
        <el-button
          type="primary"
          class="generate-button"
          @click="generateCourseware"
          :disabled="isGenerating"
        >
          {{ isGenerating ? '生成中...' : '生成有声课件' }}
        </el-button>
      </div>
    </template>

    <el-form label-width="100px">
      <!-- 课件上传 -->
      <el-form-item label="课件上传">
        <el-upload
          class="upload-demo"
          :limit="1"
          accept=".ppt,.pptx"
          :auto-upload="false"
          :on-remove="handleRemove"
          :before-upload="beforeUpload"
          :on-change="handleFileChange"
          :file-list="fileList"
        >
        <div class="upload-wrapper"> <!-- 包裹容器 -->
          <el-button type="primary">选择课件</el-button>
          <div class="el-upload__tip" style="text-align: left; margin-top: 8px; margin-left: 0">
            只能上传PPT文件，且大小在3MB~20MB之间
          </div>
      </div>
        </el-upload>
      </el-form-item>

      <!-- 声音选择 -->
      <el-form-item label="声音选择">
        <el-select
          v-model="selectedVoiceId"
          placeholder="请选择声音"
          :disabled="isLoadingVoices"
          clearable
        >
          <el-option
            v-for="item in voiceOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
        <div v-if="isLoadingVoices" class="loading-voices">加载声音样本中...</div>
      </el-form-item>

      <!-- 语速、语气、节奏调节 -->
      <el-form-item label="语速">
        <el-slider
          v-model="speed"
          :min="0.5"
          :max="1.5"
          :step="0.1"
          :format-tooltip="formatSpeed"
          show-tooltip
        />
      </el-form-item>
      <el-form-item label="语气">
        <el-slider
          v-model="tone"
          :min="0"
          :max="1"
          :step="0.1"
          :format-tooltip="formatTone"
          show-tooltip
        />
      </el-form-item>
      <el-form-item label="节奏">
        <el-slider
          v-model="rhythm"
          :min="0.5"
          :max="1.5"
          :step="0.1"
          :format-tooltip="formatRhythm"
          show-tooltip
        />
      </el-form-item>
    </el-form>

    <!-- 有声课件生成结果展示 -->
    <div v-if="videoUrl" class="result-section">
      <el-divider />
      <h3>有声课件生成结果</h3>

      <el-button
        type="success"
        @click="downloadVideo"
        :disabled="!videoUrl"
        class="download-btn"
      >
        下载视频
      </el-button>
    </div>
  </el-card>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { ElMessage, type UploadFile } from 'element-plus';
import axios from 'axios';
import { getAll } from '@/api/sound';

// 响应式变量
const selectedVoiceId = ref<string | null>(null); // Store the voice ID
const speed = ref(1);
const tone = ref(0.5);
const rhythm = ref(1);
const videoUrl = ref('');
const coursewareFile = ref<File | null>(null);
const fileList = ref<UploadFile[]>([]);
const isGenerating = ref(false);
const isLoadingVoices = ref(false);
const voiceOptions = ref<{ id: string; name: string }[]>([]);

// 文件处理逻辑
const handleFileChange = (file: UploadFile) => {
  coursewareFile.value = file.raw as File;
};

const handleRemove = () => {
  coursewareFile.value = null;
  fileList.value = [];
};

const beforeUpload = (file: File) => {
  const isPPT = ['.ppt', '.pptx'].some(ext => file.name.toLowerCase().endsWith(ext));
  const fileSizeMB = file.size / 1024 / 1024;
  const isSizeValid = fileSizeMB >= 3 && fileSizeMB <= 20;

  if (!isPPT) {
    ElMessage.error('仅支持PPT文件!');
    return false;
  }
  if (!isSizeValid) {
    ElMessage.error('文件大小必须在3MB~20MB之间!');
    return false;
  }
  return true;
};

// 声音样本加载
const getVoiceOptions = async () => {
  isLoadingVoices.value = true;
  try {
    const response = await getAll();
    if (response.data.success) {
      voiceOptions.value = response.data.data;
    } else {
      ElMessage.error('获取声音样本失败');
    }
  } catch (error) {
    console.error('获取声音样本失败:', error);
    ElMessage.error('获取声音样本失败，请稍后再试');
  } finally {
    isLoadingVoices.value = false;
  }
};

// 生成课件逻辑
const generateCourseware = async () => {
  if (!validateForm()) return;
  console.log(coursewareFile.value)
  isGenerating.value = true;
  try {
    const formData = createFormData();
    const response = await submitFormData(formData);
    console.log(videoUrl.value)
    console.log(response)
    videoUrl.value = response.data.data;

    ElMessage.success('有声课件生成成功！');
  } catch (error) {
    handleGenerationError(error);
  } finally {
    isGenerating.value = false;
  }
};

const validateForm = (): boolean => {
  if (!coursewareFile.value) {
    ElMessage.warning('请上传课件');
    return false;
  }
  if (!selectedVoiceId.value) {
    ElMessage.warning('请选择声音');
    return false;
  }
  return true;
};

const createFormData = (): FormData => {
  const formData = new FormData();
  formData.append('courseware', coursewareFile.value!);
  formData.append('voice', selectedVoiceId.value!); // Send the ID
  formData.append('speed', speed.value.toString());
  formData.append('tone', tone.value.toString());
  formData.append('rhythm', rhythm.value.toString());
  return formData;
};

const submitFormData = async (formData: FormData) => {
  return await axios.post('/front/courseware/generate-courseware', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};

const handleGenerationError = (error: unknown) => {
  console.error('生成失败:', error);
  ElMessage.error('生成失败，请检查参数后重试');
};

// 下载处理
const downloadVideo = () => {
  if (videoUrl.value) {
    const link = document.createElement('a');
    link.href = videoUrl.value;
    link.download = `courseware-${Date.now()}.mp4`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
};

// 辅助函数
const formatSpeed = (val: number) => `${val}x 语速`;
const formatTone = (val: number) => `${Math.round(val * 100)}% 情感强度`;
const formatRhythm = (val: number) => `${val}x 节奏`;

onMounted(() => {
  getVoiceOptions();
});
</script>

<style lang="scss" scoped>
.box-card {
  max-width: 800px;
  margin: 20px auto;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background: #f8f9fa;
  border-bottom: 1px solid #ebeef5;

  span {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }
}

.upload-demo {

  :deep(.el-upload) {
    width: 100%;
    text-align: left;
  }

  /* Reduce space between button and file list */
  :deep(.el-upload-dragger) {
    margin-bottom: 0;
  }

  :deep(.el-upload-list) {
    margin-top: 8px; /* Adjust as needed */
  }
}

.el-form-item {
  margin-bottom: 22px;

  :deep(.el-form-item__label) {
    color: #606266;
    font-weight: 500;
  }
}

.el-slider {
  width: 360px;
}

.loading-voices {
  color: #909399;
  font-size: 12px;
  margin-top: 4px;
}

.result-section {
  margin-top: 24px;
  text-align: center;

  h3 {
    color: #303133;
    margin-bottom: 16px;
  }

  video {
    border-radius: 6px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
}

.download-btn {
  margin-top: 16px;
  width: 200px;
}

.generate-button {
  transition: transform 0.2s, box-shadow 0.2s;

  &:active {
    transform: scale(0.98);
  }
}
.upload-wrapper {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
</style>

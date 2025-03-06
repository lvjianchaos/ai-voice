<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>声音置换 + 字幕</span>
        <el-button
          type="primary"
          class="generate-button"
          @click="processAudioVideo"
          :disabled="isProcessing"
        >
          {{ isProcessing ? '处理中...' : '开始处理' }}
        </el-button>
      </div>
    </template>

    <el-form label-width="120px">
      <!-- 音视频文件上传 -->
      <el-form-item label="音视频文件">
        <el-upload
          class="upload-demo"
          action="#"
          :http-request="uploadAudioVideo"
          :limit="1"
          accept=".mp4,.avi,.mov,.mp3,.wav"
          :auto-upload="false"
          :on-remove="handleRemove"
          :file-list="fileList"
          :before-upload="beforeUpload"
        >
          <el-button type="primary">选择文件</el-button>
          <template #tip>
            <div class="el-upload__tip">
              只能上传MP4/AVI/MOV/MP3/WAV文件，建议视频文件小于50MB，音频文件小于20MB
            </div>
          </template>
        </el-upload>
      </el-form-item>

      <!-- 声音选择 -->
      <el-form-item label="声音选择">
        <el-select v-model="selectedVoice" placeholder="请选择声音" :disabled="isLoadingVoices">
          <el-option v-for="item in voiceOptions" :key="item.id" :label="item.name" :value="item.name" />
        </el-select>
        <div v-if="isLoadingVoices">加载声音样本中...</div>
      </el-form-item>
    </el-form>

    <!-- 结果展示区 -->
    <div v-if="resultUrl">
      <el-divider />
      <h3>处理结果</h3>
      <template v-if="isVideo">
        <video controls :src="resultUrl" width="640" height="360"></video>
      </template>
      <template v-else>
        <audio controls :src="resultUrl"></audio>
      </template>
      <el-button type="primary" @click="downloadResult" :disabled="!resultUrl">下载结果</el-button>
    </div>
  </el-card>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import axios from 'axios';
import { getAll } from '@/api/sound'; // 导入获取声音样本的 API

// 响应式变量
const selectedVoice = ref('');
const resultUrl = ref('');
const fileList = ref([]);
const audioVideoFile = ref<File | null>(null);
const isVideo = ref(false); // 用于判断结果是视频还是音频
const isProcessing = ref(false); // 是否正在处理
const isLoadingVoices = ref(false); // 是否正在加载声音样本

// 声音样本选项 (需要从后端获取)
const voiceOptions = ref([]);

// 获取声音样本
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

// 文件上传
const beforeUpload = (file: File) => {
  const isVideoFile = file.name.endsWith('.mp4') || file.name.endsWith('.avi') || file.name.endsWith('.mov');
  const isAudioFile = file.name.endsWith('.mp3') || file.name.endsWith('.wav');
  const isFileSizeValid =
    (isVideoFile && file.size / 1024 / 1024 <= 50) || (isAudioFile && file.size / 1024 / 1024 <= 20);

  if (!isVideoFile && !isAudioFile) {
    ElMessage.error('只能上传MP4/AVI/MOV/MP3/WAV文件!');
    return false;
  }

  if (!isFileSizeValid) {
    ElMessage.error('视频文件建议小于50MB，音频文件建议小于20MB!');
    return false;
  }

  return true;
};

const uploadAudioVideo = async (uploadOption: any) => {
  audioVideoFile.value = uploadOption.file;
};

const handleRemove = (file: any, fileList: any) => {
  audioVideoFile.value = null;
  resultUrl.value = ''; // 清空结果
};

// 处理音视频
const processAudioVideo = async () => {
  if (!audioVideoFile.value) {
    ElMessage.warning('请上传音视频文件');
    return;
  }

  if (!selectedVoice.value) {
    ElMessage.warning('请选择声音');
    return;
  }

  isProcessing.value = true; // 设置为正在处理
  try {
    const formData = new FormData();
    formData.append('file', audioVideoFile.value);
    formData.append('voice', selectedVoice.value);

    const response = await axios.post(
      '/api/process-audio-video', // 替换为你的后端 API 地址
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        responseType: 'blob', // 期望响应类型为 blob
      }
    );

    resultUrl.value = URL.createObjectURL(response.data);

    // 判断返回的是视频还是音频
    const contentType = response.headers['content-type'];
    isVideo.value = contentType.startsWith('video/');

    ElMessage.success('处理成功！');
  } catch (error) {
    console.error('处理失败:', error);
    ElMessage.error('处理失败，请稍后再试');
  } finally {
    isProcessing.value = false; // 恢复为未处理状态
  }
};

// 下载结果
const downloadResult = () => {
  if (resultUrl.value) {
    const link = document.createElement('a');
    link.href = resultUrl.value;
    link.download = isVideo.value ? 'processed_video.mp4' : 'processed_audio.mp3'; // 根据类型设置下载文件名
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
};

onMounted(() => {
  getVoiceOptions(); // 在组件加载时获取声音样本
});
</script>

<style lang="scss" scoped>
.box-card {
  width: 800px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.upload-demo {
  margin-bottom: 15px;
}

video,
audio {
  width: 100%;
  margin-bottom: 15px;
}

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

<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>个性化语音讲解</span>
        <el-button type="primary" class="generate-button" @click="generateVoice" :disabled="isGenerating">
          {{ isGenerating ? '生成中...' : '生成语音' }}
        </el-button>
      </div>
    </template>

    <!-- 文本输入 -->
    <el-form label-width="80px">
      <el-form-item label="文本内容">
        <el-upload
          class="upload-demo"
          action="#"
          :http-request="uploadFile"
          :limit="1"
          accept=".txt,.docx"
          :auto-upload="false"
          :on-remove="handleRemove"
          :file-list="fileList"
        >
          <el-button type="primary">选择文件</el-button>
          <template #tip>
            <div class="el-upload__tip">只能上传txt/docx文件，且不超过2MB</div>
          </template>
        </el-upload>
        <el-input v-model="textContent" type="textarea" :rows="10" placeholder="或在此输入文本内容, 长度在800~2000字" />
      </el-form-item>

      <!-- 声音选择 -->
      <el-form-item label="声音选择">
        <el-select v-model="selectedVoice" placeholder="请选择声音" :disabled="isLoadingVoices">
          <el-option v-for="item in voiceOptions" :key="item.id" :label="item.name" :value="item.name" />
        </el-select>
        <div v-if="isLoadingVoices">加载声音样本中...</div>
      </el-form-item>

      <!-- 语速、语气、节奏调节 -->
      <el-form-item label="语速">
        <el-slider v-model="speed" :min="0.5" :max="2" :step="0.05" show-tooltip />
      </el-form-item>
      <el-form-item label="语气">
        <el-slider v-model="tone" :min="0" :max="1" :step="0.1" show-tooltip />
      </el-form-item>
      <el-form-item label="节奏">
        <el-slider v-model="rhythm" :min="0.5" :max="1.5" :step="0.1" show-tooltip />
      </el-form-item>
    </el-form>

    <!-- 语音生成结果展示 -->
    <div v-if="audioUrl">
      <el-divider />
      <h3>语音生成结果</h3>
      <audio controls :src="audioUrl"></audio>
      <el-button type="primary" @click="downloadAudio" :disabled="!audioUrl">下载音频</el-button>
    </div>
  </el-card>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import axios from 'axios';
import { getAll } from '@/api/sound'; // 导入获取声音样本的 API

// 响应式变量
const textContent = ref('');
const selectedVoice = ref('');
const speed = ref(1.25);
const tone = ref(0.5);
const rhythm = ref(1);
const audioUrl = ref('');
const fileList = ref([]);
const isGenerating = ref(false); // 是否正在生成语音
const isLoadingVoices = ref(false); // 是否正在加载声音样本

// 声音样本选项 (从后端获取)
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
const uploadFile = async (uploadOption: any) => {
  const file = uploadOption.file;
  if (file) {
    const reader = new FileReader();
    reader.onload = (e: any) => {
      textContent.value = e.target.result;
    };

    if (file.name.endsWith('.txt')) {
      reader.readAsText(file);
    } else if (file.name.endsWith('.docx')) {
      // 需要使用mammoth.js等库来解析docx文件
      // 这里只是一个占位符
      ElMessage.warning('暂不支持解析docx文件，请手动输入文本');
    } else {
      ElMessage.error('不支持的文件类型');
    }
  }
};

const handleRemove = (file: any, fileList: any) => {
  console.log(file, fileList);
  textContent.value = '';
};

// 生成语音
const generateVoice = async () => {
  // 修改判断逻辑：只要文本内容不为空，或者有上传文件，就可以生成语音
  if (!textContent.value && fileList.value.length === 0) {
    ElMessage.warning('请输入文本内容或上传文件');
    return;
  }
  if (!selectedVoice.value) {
    ElMessage.warning('请选择声音');
    return;
  }

  isGenerating.value = true; // 设置为正在生成
  try {
    const response = await axios.post(
      '/api/generate-voice', // 替换为后端 API 地址
      {
        text: textContent.value,
        voice: selectedVoice.value,
        speed: speed.value,
        tone: tone.value,
        rhythm: rhythm.value,
      },
      { responseType: 'blob' } // 告诉 axios 期望的响应类型是 blob (二进制数据)
    );

    // 创建一个指向 blob 数据的 URL
    audioUrl.value = URL.createObjectURL(response.data);
    ElMessage.success('语音生成成功！');
  } catch (error) {
    console.error('语音生成失败:', error);
    ElMessage.error('语音生成失败，请稍后再试');
  } finally {
    isGenerating.value = false; // 恢复为未生成状态
  }
};

// 下载音频
const downloadAudio = () => {
  if (audioUrl.value) {
    const link = document.createElement('a');
    link.href = audioUrl.value;
    link.download = 'voice.mp3'; // 可以自定义文件名
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

audio {
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

audio {
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

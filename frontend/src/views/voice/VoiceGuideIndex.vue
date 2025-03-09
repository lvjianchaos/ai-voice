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
        <el-input v-model="textContent" style="padding-top: 9px;" type="textarea" :rows="14" placeholder="在此输入文本内容, 长度在800~2000字" />
      </el-form-item>

      <!-- 声音选择 -->
      <el-form-item label="声音选择">
        <el-select v-model="selectedVoiceId" placeholder="请选择声音" :disabled="isLoadingVoices">
          <el-option v-for="item in voiceOptions" :key="item.id" :label="item.name" :value="item.id" />
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
const selectedVoiceId = ref<number | null>(null); // 使用 ID 作为选择的声音
const speed = ref(1);
const tone = ref(0.5);
const rhythm = ref(1);
const audioUrl = ref('');
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

// 生成语音
const generateVoice = async () => {
  if (!textContent.value) {
    ElMessage.warning('请输入文本内容');
    return;
  }
  if (!selectedVoiceId.value) {
    ElMessage.warning('请选择声音');
    return;
  }

  isGenerating.value = true; // 设置为正在生成
  try {
    const response = await axios.post(
      '/front/sound/voiceguide/generate-voice',
      {
        text: textContent.value,
        voiceId: selectedVoiceId.value, // 发送声音 ID
        speed: speed.value,
        tone: tone.value,
        rhythm: rhythm.value,
      },
    );


    audioUrl.value = response.data.data;
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

<template>
  <el-dialog v-model="dialogFormVisible" :title="msgText + '样本'" width="500">
    <el-form :model="form" ref="fmSound">
      <el-form-item label="样本名称" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="声音性别" :label-width="formLabelWidth" prop="gender">
        <el-input v-model="form.gender" autocomplete="off" />
      </el-form-item>
      <el-form-item label="语言" :label-width="formLabelWidth" prop="language">
        <el-input v-model="form.language" autocomplete="off" />
      </el-form-item>
      <el-form-item label="样本描述" :label-width="formLabelWidth" prop="discription">
        <el-input v-model="form.discription" type="textarea" :autosize="{ minRows: 2, maxRows: 4 }" autocomplete="off" />
      </el-form-item>

      <el-form-item label="音频文件" :label-width="formLabelWidth">
        <input type="file" accept="audio/*" @change="handleFileChange" style="margin-bottom: 10px;" :disabled="hasAudioFile" />
        <el-button type="primary" @click="startRecording" :disabled="isRecording || hasAudioFile">
          开始录音
        </el-button>
        <el-button type="danger" @click="stopRecording" :disabled="!isRecording">
          停止录音
        </el-button>
        <el-button v-if="hasAudioFile" @click="handleDeleteAudio">
          删除音频
        </el-button>
        <div v-if="hasAudioFile">文件名: {{ audioFileName }}</div>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false"> 取 消 </el-button>
        <el-button type="primary" @click="submitForm">
          确 认
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import { allSound, dialogFormVisible, onSubmit, isCreate, msgText, form, getAllSound } from '@/composables/useSounds';
import type { FormInstance } from 'element-plus';
import { ElMessage } from 'element-plus';

const formLabelWidth = '140px';

// 音频文件
const audioFile = ref<File | null>(null);

// 音频文件名
const audioFileName = ref<string>('');

// 录音状态
const isRecording = ref(false);

// 录音对象
const mediaRecorder = ref<MediaRecorder | null>(null);

// 录音数据
const recordedChunks = ref<Blob[]>([]);

// 录音按钮文字
const recordButtonText = ref('开始录音');

// 是否已选择音频文件
const hasAudioFile = computed(() => audioFile.value !== null);

// 初始化并显示对话框
const initAndShow = async (id = 0) => {
  fmSound.value?.resetFields();
  dialogFormVisible.value = true;
  audioFile.value = null; // 重置音频文件
  audioFileName.value = ''; // 重置音频文件名

  // 重置 form 对象
  form.name = '';
  form.gender = '';
  form.language = '';
  form.discription = '';
  form.audioFile = null;

  if (id) {
    isCreate.value = false;
    msgText.value = '编辑';
    // 查找对应的 sound 对象
    const sound = allSound.value.find(item => item.id === id);
    if (sound) {
      // 将 sound 对象的值赋给 form 对象
      form.name = sound.name;
      form.gender = sound.gender;
      form.language = sound.language;
      form.discription = sound.discription;
      // 注意：这里不能直接赋值 audioFile，因为 audioFile 是 File 对象，需要单独处理
    }
  } else {
    isCreate.value = true;
    msgText.value = '创建';
  }
};

// 选择文件
const handleFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files.length > 0) {
    audioFile.value = target.files[0];
    audioFileName.value = target.files[0].name; // 保存文件名
    form.audioFile = target.files[0]; // 将音频文件保存到 form 对象中
  }
};

// 开始录音
const startRecording = async () => {
  try {
    const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
    mediaRecorder.value = new MediaRecorder(stream);
    recordedChunks.value = [];

    mediaRecorder.value.ondataavailable = (event: BlobEvent) => {
      recordedChunks.value.push(event.data);
    };

    mediaRecorder.value.onstop = () => {
      const blob = new Blob(recordedChunks.value, { type: 'audio/webm' });
      audioFile.value = new File([blob], 'recorded-audio.webm');
      audioFileName.value = 'recorded-audio.webm'; // 设置录音文件名
      recordButtonText.value = '开始录音';
      ElMessage.success('录音完成！');
      form.audioFile = new File([blob], 'recorded-audio.webm'); // 将录音文件保存到 form 对象中
    };

    mediaRecorder.value.start();
    isRecording.value = true;
    recordButtonText.value = '停止录音';
    ElMessage.info('开始录音...');
  } catch (error) {
    console.error('录音失败:', error);
    ElMessage.error('录音失败，请检查麦克风权限');
  }
};

// 停止录音
const stopRecording = () => {
  if (mediaRecorder.value && mediaRecorder.value.state === 'recording') {
    mediaRecorder.value.stop();
    isRecording.value = false;
  }
};

// 删除音频文件
const handleDeleteAudio = () => {
  audioFile.value = null;
  audioFileName.value = '';
  form.audioFile = null;
};

// 提交表单
const submitForm = async () => {
  if (!audioFile.value) {
    ElMessage.error('请选择音频文件或录音');
    return;
  }

  // 调用 onSubmit 函数
  await onSubmit(form);

  // 关闭对话框
  dialogFormVisible.value = false;
};

const fmSound = ref<FormInstance>();

defineExpose({
  initAndShow
});

onMounted(() => {
  getAllSound();
});
</script>

<style type="scss" scoped>
</style>

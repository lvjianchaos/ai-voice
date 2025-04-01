<template>
  <el-dialog
    v-model="dialogFormVisible"
    :title="msgText + '样本'"
    width="560px"
    destroy-on-close
    class="sample-dialog"
  >
    <el-form
      :model="form"
      ref="fmSound"
      label-position="top"
      :rules="rules"
    >
      <el-form-item label="样本名称" prop="name">
        <el-input
          v-model="form.name"
          placeholder="请输入样本名称"
          clearable
        />
      </el-form-item>

      <div class="form-row">
        <el-form-item label="声音性别" prop="gender" class="half-width">
          <el-select v-model="form.gender" placeholder="请选择性别" class="full-width">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>

        <el-form-item label="语言" prop="language" class="half-width">
          <el-select v-model="form.language" placeholder="请选择语言" class="full-width">
            <el-option label="中文" value="中文" />
            <el-option label="英语" value="英语" />
            <el-option label="日语" value="日语" />
            <el-option label="法语" value="法语" />
            <el-option label="德语" value="德语" />
          </el-select>
        </el-form-item>
      </div>

      <el-form-item label="样本描述" prop="discription">
        <el-input
          v-model="form.discription"
          type="textarea"
          :autosize="{ minRows: 3, maxRows: 5 }"
          placeholder="请输入样本描述"
        />
      </el-form-item>

      <!-- 音频文件上传/录制区域，仅在创建模式下显示 -->
      <el-form-item v-if="isCreate" label="音频文件" class="audio-form-item">
        <div class="audio-upload-section">
          <div class="upload-container" v-if="!hasAudioFile">
            <el-upload
              action="#"
              :auto-upload="false"
              :on-change="handleFileUpload"
              :show-file-list="false"
              accept="audio/*"
              :limit="1"
              class="audio-uploader"
            >
              <div class="upload-trigger">
                <el-icon><UploadFilled /></el-icon>
                <div class="upload-text">点击上传音频文件</div>
                <div class="upload-tip">支持 mp3、wav、ogg 等格式</div>
              </div>
            </el-upload>

            <div class="divider">或</div>

            <div class="record-controls">
              <el-button
                type="primary"
                @click="startRecording"
                :class="{ 'is-recording': isRecording }"
              >
                <el-icon v-if="!isRecording"><Microphone /></el-icon>
                <el-icon v-else><VideoPause /></el-icon>
                {{ recordButtonText }}
              </el-button>
              <div class="record-tip" v-if="isRecording">录音中，点击按钮结束录音...</div>
            </div>
          </div>

          <div class="audio-preview" v-if="hasAudioFile">
            <div class="audio-info">
              <el-icon class="file-icon"><Document /></el-icon>
              <div class="file-name">{{ audioFileName }}</div>
              <el-button
                type="danger"
                @click="handleDeleteAudio"
                size="small"
                text
                class="delete-button"
              >
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
            <audio controls class="audio-player" :src="audioPreviewUrl"></audio>
          </div>
        </div>
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="isSubmitting">
          {{ isCreate ? '创建' : '保存' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import { allSound, dialogFormVisible, onSubmit, isCreate, msgText, form, getAllSound } from '@/composables/useSounds';
import type { FormInstance, UploadFile, FormRules } from 'element-plus';
import { ElMessage } from 'element-plus';
import { Document, Delete, UploadFilled, Microphone, VideoPause } from '@element-plus/icons-vue';

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入样本名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度应为2到20个字符', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  language: [
    { required: true, message: '请选择语言', trigger: 'change' }
  ],
  discription: [
    { required: true, message: '请输入样本描述', trigger: 'blur' },
    { min: 5, max: 200, message: '长度应为5到200个字符', trigger: 'blur' }
  ]
};

// 音频文件
const audioFile = ref<File | null>(null);

// 音频文件名
const audioFileName = ref<string>('');

// 录音状态
const isRecording = ref(false);

// 提交状态
const isSubmitting = ref(false);

// 录音对象
const mediaRecorder = ref<MediaRecorder | null>(null);

// 录音数据
const recordedChunks = ref<Blob[]>([]);

// 录音按钮文字
const recordButtonText = ref('开始录音');

// 是否已选择音频文件
const hasAudioFile = computed(() => audioFile.value !== null);

// 音频预览URL
const audioPreviewUrl = computed(() => {
  if (audioFile.value) {
    return URL.createObjectURL(audioFile.value);
  }
  return '';
});

// 初始化并显示对话框
const initAndShow = async (id = 0) => {
  fmSound.value?.resetFields();
  dialogFormVisible.value = true;
  audioFile.value = null; // 重置音频文件
  audioFileName.value = ''; // 重置音频文件名
  isSubmitting.value = false;

  // 重置 form 对象
  form.id = '';
  form.name = '';
  form.gender = '';
  form.language = '';
  form.discription = '';
  form.audioFile = null;

  if (id) {
    isCreate.value = false;
    msgText.value = '编辑';
    form.id = id;
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

// 文件上传
const handleFileUpload = (uploadFile: UploadFile) => {
  if (uploadFile.raw) {
    audioFile.value = uploadFile.raw;
    audioFileName.value = uploadFile.name;
    form.audioFile = uploadFile.raw;
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
    if (isRecording.value) {
      stopRecording();
      return;
    }

    const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
    mediaRecorder.value = new MediaRecorder(stream);
    recordedChunks.value = [];

    mediaRecorder.value.ondataavailable = (event: BlobEvent) => {
      if (event.data.size > 0) {
        recordedChunks.value.push(event.data);
      }
    };

    mediaRecorder.value.onstop = () => {
      if (recordedChunks.value.length > 0) {
        const blob = new Blob(recordedChunks.value, { type: 'audio/webm' });
        audioFile.value = new File([blob], 'recorded-audio.webm');
        audioFileName.value = 'recorded-audio.webm'; // 设置录音文件名
        recordButtonText.value = '开始录音';
        ElMessage.success('录音完成！');
        form.audioFile = audioFile.value; // 将录音文件保存到 form 对象中

        // 关闭媒体流
        if (stream) {
          stream.getTracks().forEach(track => track.stop());
        }
      }
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
  if (!mediaRecorder.value) return;

  if (mediaRecorder.value.state === 'recording') {
    mediaRecorder.value.stop();
    isRecording.value = false;

    // 关闭媒体流
    if (mediaRecorder.value.stream) {
      mediaRecorder.value.stream.getTracks().forEach(track => track.stop());
    }
  }
};

// 删除音频文件
const handleDeleteAudio = () => {
  if (audioFile.value) {
    // 如果存在URL对象，撤销它以释放内存
    if (audioPreviewUrl.value) {
      URL.revokeObjectURL(audioPreviewUrl.value);
    }

    audioFile.value = null;
    audioFileName.value = '';
    form.audioFile = null;

    ElMessage.success('音频文件已删除');
  }
};

// 提交表单
const submitForm = async () => {
  // 表单验证
  try {
    await fmSound.value?.validate();
  } catch (error) {
    return;
  }

  if (!audioFile.value && isCreate.value) {
    ElMessage.error('请选择音频文件或录音');
    return;
  }

  // 设置提交状态
  isSubmitting.value = true;

  try {
    // 调用 onSubmit 函数
    await onSubmit(form);

    // 发送更新事件
    emit('sample-updated');

    // 关闭对话框
    dialogFormVisible.value = false;
  } catch (error) {
    console.error('提交失败:', error);
  } finally {
    isSubmitting.value = false;
  }
};

const fmSound = ref<FormInstance>();
const emit = defineEmits(['sample-updated']);

defineExpose({
  initAndShow
});

onMounted(() => {
  getAllSound();
});
</script>

<style lang="scss" scoped>
.sample-dialog {
  :deep(.el-dialog__header) {
    padding: var(--spacing-6);
    border-bottom: 1px solid var(--neutral-200);
    margin-right: 0;

    .el-dialog__title {
      font-size: 18px;
      font-weight: 600;
      color: var(--neutral-900);
    }
  }

  :deep(.el-dialog__body) {
    padding: var(--spacing-6);
  }

  :deep(.el-dialog__footer) {
    padding: var(--spacing-4) var(--spacing-6);
    border-top: 1px solid var(--neutral-200);
  }
}

.form-row {
  display: flex;
  gap: var(--spacing-5);
  margin-bottom: var(--spacing-2);

  .half-width {
    width: 50%;
  }
}

.full-width {
  width: 100%;
}

.audio-form-item {
  margin-bottom: 0;

  .audio-upload-section {
    border: 1px dashed var(--neutral-300);
    border-radius: var(--border-radius-md);
    padding: var(--spacing-5);
    background-color: var(--neutral-50);

    .upload-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: var(--spacing-5);

      .audio-uploader {
        width: 100%;

        .upload-trigger {
          height: 120px;
          border: 1px dashed var(--neutral-400);
          border-radius: var(--border-radius-md);
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          cursor: pointer;
          transition: all var(--transition-fast);

          &:hover {
            border-color: var(--primary-color);
            background-color: rgba(25, 118, 210, 0.04);
          }

          .el-icon {
            font-size: 32px;
            color: var(--primary-color);
            margin-bottom: var(--spacing-2);
          }

          .upload-text {
            font-size: 16px;
            font-weight: 500;
            color: var(--neutral-700);
            margin-bottom: var(--spacing-1);
          }

          .upload-tip {
            font-size: 12px;
            color: var(--neutral-500);
          }
        }
      }

      .divider {
        width: 100%;
        text-align: center;
        font-size: 14px;
        color: var(--neutral-500);
        position: relative;

        &::before, &::after {
          content: '';
          position: absolute;
          top: 50%;
          width: 40%;
          height: 1px;
          background-color: var(--neutral-300);
        }

        &::before {
          left: 0;
        }

        &::after {
          right: 0;
        }
      }

      .record-controls {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: var(--spacing-3);

        .record-button {
          width: 160px;
          height: 44px;
          border-radius: var(--border-radius-full);

          &.is-recording {
            background-color: var(--danger-color);
            animation: pulse 1.5s infinite;
          }
        }

        .record-tip {
          font-size: 14px;
          color: var(--error-color);
        }
      }
    }

    .audio-preview {
      display: flex;
      flex-direction: column;
      gap: var(--spacing-3);

      .audio-info {
        display: flex;
        align-items: center;
        gap: var(--spacing-2);
        background-color: white;
        padding: var(--spacing-3);
        border-radius: var(--border-radius-md);
        box-shadow: var(--shadow-sm);

        .file-icon {
          font-size: 24px;
          color: var(--primary-color);
        }

        .file-name {
          flex: 1;
          font-size: 14px;
          color: var(--neutral-700);
        }
      }

      .audio-player {
        width: 100%;
        height: 40px;
        border-radius: var(--border-radius-md);
      }
    }
  }
}

.record-button {
  &.is-recording {
    background-color: var(--danger-color);
    animation: pulse 1.5s infinite;
  }
}

.delete-button {
  color: var(--danger-color);

  &:hover {
    background-color: rgba(var(--danger-color-rgb), 0.1);
  }
}

@keyframes pulse {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
  100% {
    opacity: 1;
  }
}
</style>

<template>
  <div class="audio-subtitles-page">
    <div class="page-header animate-on-enter">
      <h1 class="page-title">音频字幕生成</h1>
      <p class="page-description">上传音频/视频文件，使用AI技术自动生成字幕内容</p>
      <div class="particles">
        <span class="particle" v-for="i in 5" :key="i"></span>
      </div>
    </div>

    <el-card class="subtitles-card animate-on-enter">
      <div class="wave-background">
        <div class="wave"></div>
        <div class="wave"></div>
        <div class="wave"></div>
      </div>

      <div class="card-body">
        <!-- 上传区域 -->
        <div class="upload-section">
          <h3 class="section-title">
            <span class="icon-wrapper">
              <el-icon class="section-icon"><Upload /></el-icon>
            </span>
            <span>上传音频/视频文件</span>
          </h3>

        <el-upload
            class="file-uploader"
            drag
            action=""
            :auto-upload="false"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
            :file-list="fileList"
          :limit="1"
            :multiple="false"
          accept=".mp4,.avi,.mov,.mp3,.wav"
          >
            <div class="upload-content">
              <el-icon class="upload-icon"><UploadFilled /></el-icon>
              <div class="upload-text">
                <span class="main-text">拖拽文件到此处或 <em>点击上传</em></span>
                <span class="sub-text">支持 MP4, AVI, MOV, MP3, WAV 格式文件</span>
              </div>
            </div>
            <template #tip>
              <div class="upload-tip">
                <el-icon><InfoFilled /></el-icon>
                <span>视频文件不超过50MB，音频文件不超过20MB</span>
            </div>
          </template>
        </el-upload>
        </div>

        <!-- 文件信息和设置区域 -->
        <div v-if="fileList.length > 0" class="file-settings animate-fade-in">
          <div class="file-info">
            <div class="file-card">
              <div class="file-icon">
                <el-icon v-if="isVideoFile"><VideoCamera /></el-icon>
                <el-icon v-else><Headset /></el-icon>
              </div>
              <div class="file-details">
                <div class="file-name">{{ fileList[0].name }}</div>
                <div class="file-size">{{ formatFileSize(fileList[0].size || 0) }}</div>
              </div>
              <el-button
                type="danger"
                size="small"
                circle
                @click="handleFileRemove()"
                class="remove-button"
              >
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
          </div>

          <div class="voice-settings">
            <h4 class="settings-title">字幕设置</h4>

            <div class="voice-selection">
              <span class="label">选择语音（可选，用于生成双语字幕）</span>
              <el-select
                v-model="selectedVoice"
                placeholder="请选择语音"
                class="voice-select"
                :loading="voiceLoading"
                clearable
              >
                <el-option
                  v-for="item in voices"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                  <div class="voice-option">
                    <span class="voice-name">{{ item.name }}</span>
                    <el-tag size="small" :type="item.gender === '男' ? 'info' : 'primary'" class="voice-tag">
                      {{ item.gender }}
                    </el-tag>
                    <el-tag size="small" type="success" class="voice-tag">
                      {{ item.language }}
                    </el-tag>
                  </div>
                </el-option>
        </el-select>
            </div>
          </div>

          <div class="action-buttons">
            <el-button
              type="primary"
              @click="processFile"
              :loading="processing"
              :disabled="!canProcess"
              class="process-button pulse-effect"
            >
              <el-icon><VideoPlay /></el-icon>
              <span>生成字幕</span>
            </el-button>

            <el-button
              v-if="resultUrl"
              type="success"
              @click="downloadResult"
              class="download-button"
            >
              <el-icon><Download /></el-icon>
              <span>下载结果</span>
            </el-button>
          </div>
        </div>

        <!-- 结果区域将在后续编辑中添加 -->
    </div>
  </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import {
  Upload,
  UploadFilled,
  InfoFilled,
  VideoCamera,
  Headset,
  Delete,
  VideoPlay,
  Download
} from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import type { UploadFile, UploadUserFile } from 'element-plus';
import { getVoices, type VoiceItem } from '@/api/voice';

// 文件上传相关变量
const fileList = ref<UploadUserFile[]>([]);

// 语音相关变量
const selectedVoice = ref<number | string>('');
const voices = ref<VoiceItem[]>([]);
const voiceLoading = ref(false);

// 处理相关变量
const processing = ref(false);
const resultUrl = ref('');

// 计算属性
const isVideoFile = computed(() => {
  if (fileList.value.length === 0) return false;
  const fileName = fileList.value[0].name.toLowerCase();
  return fileName.endsWith('.mp4') || fileName.endsWith('.avi') || fileName.endsWith('.mov');
});

const canProcess = computed(() => {
  return fileList.value.length > 0;
});

// 处理文件变化
const handleFileChange = (uploadFile: UploadFile) => {
  if (!validateFile(uploadFile)) {
    fileList.value = [];
    return;
  }

  // 限制只有一个文件，设置后会替换之前的文件
  fileList.value = [uploadFile];
};

// 处理文件移除
const handleFileRemove = () => {
  fileList.value = [];
  resultUrl.value = '';
};

// 文件验证
const validateFile = (file: UploadFile): boolean => {
  // 验证文件类型
  const fileName = file.name.toLowerCase();
  const isVideoFile = fileName.endsWith('.mp4') || fileName.endsWith('.avi') || fileName.endsWith('.mov');
  const isAudioFile = fileName.endsWith('.mp3') || fileName.endsWith('.wav');

  if (!isVideoFile && !isAudioFile) {
    ElMessage.error('请上传支持的文件格式！');
    return false;
  }

  // 验证文件大小
  const fileSizeMB = (file.size || 0) / 1024 / 1024;
  const maxSize = isVideoFile ? 50 : 20; // 视频50MB，音频20MB

  if (fileSizeMB > maxSize) {
    ElMessage.error(`文件大小不能超过${maxSize}MB!`);
    return false;
  }

  return true;
};

// 格式化文件大小
const formatFileSize = (size: number): string => {
  if (size < 1024) {
    return size + ' B';
  } else if (size < 1024 * 1024) {
    return (size / 1024).toFixed(2) + ' KB';
  } else {
    return (size / (1024 * 1024)).toFixed(2) + ' MB';
  }
};

// 获取所有可用的语音
const fetchVoices = async () => {
  try {
    voiceLoading.value = true;
    const response = await getVoices();
    if (response.data.success) {
      voices.value = response.data.data;
    } else {
      ElMessage.error('获取语音列表失败');
    }
  } catch (error) {
    console.error('获取语音列表错误', error);
    ElMessage.error('获取语音列表错误');
  } finally {
    voiceLoading.value = false;
  }
};

// 处理文件生成字幕
const processFile = async () => {
  if (!canProcess.value) {
    ElMessage.warning('请上传文件');
    return;
  }

  try {
    processing.value = true;
    resultUrl.value = ''; // 清空之前的结果

    // 在实际项目中，这里应该提交文件到后端进行处理
    // 模拟API调用延迟
    setTimeout(() => {
      // 模拟返回结果URL
      if (isVideoFile.value) {
        resultUrl.value = 'https://example.com/sample-subtitled-video.mp4';
      } else {
        resultUrl.value = 'https://example.com/sample-subtitles.srt';
      }
      ElMessage.success('字幕生成成功');
      processing.value = false;
    }, 3000);

  } catch (error) {
    console.error('处理文件错误', error);
    ElMessage.error('处理文件错误');
    processing.value = false;
  }
};

// 下载结果
const downloadResult = () => {
  if (!resultUrl.value) {
    ElMessage.warning('没有可下载的结果');
    return;
  }

    const link = document.createElement('a');
    link.href = resultUrl.value;
  link.download = isVideoFile.value
    ? `带字幕视频_${new Date().getTime()}.mp4`
    : `字幕文件_${new Date().getTime()}.srt`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  ElMessage.success('下载中...');
};

// 页面动画
const animateElements = () => {
  const elements = document.querySelectorAll('.animate-on-enter');
  elements.forEach((el, index) => {
    setTimeout(() => {
      el.classList.add('animated');
    }, index * 200);
  });
};

onMounted(() => {
  fetchVoices();
  animateElements();
});
</script>

<style lang="scss" scoped>
.audio-subtitles-page {
  padding-bottom: var(--spacing-8);
  position: relative;
  overflow: hidden;

  .page-header {
    margin-bottom: var(--spacing-6);
    position: relative;
    opacity: 0;
    transform: translateY(20px);
    transition: all 0.8s cubic-bezier(0.34, 1.56, 0.64, 1);

    &.animated {
      opacity: 1;
      transform: translateY(0);
    }

    .page-title {
      font-size: 32px;
      font-weight: 700;
      color: var(--neutral-900);
      margin-bottom: var(--spacing-2);
      background: linear-gradient(45deg, var(--primary-color), var(--primary-light));
      -webkit-background-clip: text;
      background-clip: text;
      -webkit-text-fill-color: transparent;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      position: relative;
      display: inline-block;
    }

    .page-description {
      font-size: 16px;
      color: var(--neutral-600);
      max-width: 600px;
    }

    .particles {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      pointer-events: none;
      overflow: hidden;
      z-index: -1;

      .particle {
        position: absolute;
        width: 6px;
        height: 6px;
        background-color: var(--primary-color);
        border-radius: 50%;
        opacity: 0.5;
        animation: float 8s infinite linear;

        &:nth-child(1) {
          top: 10%;
          left: 20%;
          animation-delay: 0s;
        }

        &:nth-child(2) {
          top: 30%;
          left: 40%;
          animation-delay: 1s;
          background-color: var(--success-color);
        }

        &:nth-child(3) {
          top: 60%;
          left: 80%;
          animation-delay: 2s;
          background-color: var(--primary-light);
        }

        &:nth-child(4) {
          top: 15%;
          left: 70%;
          animation-delay: 3s;
          background-color: var(--warning-color);
        }

        &:nth-child(5) {
          top: 50%;
          left: 10%;
          animation-delay: 4s;
          background-color: var(--danger-color);
        }
      }
    }
  }

  .subtitles-card {
    border-radius: var(--border-radius-lg);
    box-shadow: var(--shadow-md);
    position: relative;
    overflow: hidden;
    opacity: 0;
    transform: translateY(20px);
    transition: all 0.8s cubic-bezier(0.34, 1.56, 0.64, 1);
    transition-delay: 0.2s;

    &.animated {
      opacity: 1;
      transform: translateY(0);
    }

    .wave-background {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      overflow: hidden;
      z-index: 0;
      opacity: 0.1;

      .wave {
        position: absolute;
        bottom: 0;
        left: 0;
        width: 200%;
        height: 100px;
        background: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320"><path fill="%234d88ff" fill-opacity="1" d="M0,128L48,122.7C96,117,192,107,288,128C384,149,480,203,576,202.7C672,203,768,149,864,122.7C960,96,1056,96,1152,106.7C1248,117,1344,139,1392,149.3L1440,160L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"></path></svg>');
        background-repeat: repeat-x;
        background-size: 1440px 100px;
        animation: wave 20s linear infinite;

        &:nth-child(2) {
          bottom: 10px;
          animation: wave 18s linear reverse infinite;
          opacity: 0.6;
        }

        &:nth-child(3) {
          bottom: 20px;
          animation: wave 16s linear infinite;
          opacity: 0.4;
        }
      }
    }

    .card-body {
      position: relative;
      z-index: 1;

      .section-title {
        display: flex;
        align-items: center;
        gap: var(--spacing-2);
        margin-bottom: var(--spacing-4);
        font-size: 18px;
        font-weight: 600;
        color: var(--neutral-800);

        .icon-wrapper {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 28px;
          height: 28px;
          background-color: rgba(var(--primary-color-rgb), 0.1);
          border-radius: var(--border-radius-full);

          .section-icon {
            font-size: 16px;
            color: var(--primary-color);
          }
        }
      }

      .upload-section {
        margin-bottom: var(--spacing-6);

        .file-uploader {
          width: 100%;

          :deep(.el-upload) {
            width: 100%;
          }

          :deep(.el-upload-dragger) {
            width: 100%;
            height: 180px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            border: 2px dashed rgba(var(--primary-color-rgb), 0.3);
            background-color: rgba(var(--primary-color-rgb), 0.02);
            border-radius: var(--border-radius-lg);
            transition: all 0.3s ease;

            &:hover {
              border-color: var(--primary-color);
              background-color: rgba(var(--primary-color-rgb), 0.05);
              transform: translateY(-2px);
            }

            .upload-content {
              display: flex;
              flex-direction: column;
              align-items: center;
              justify-content: center;

              .upload-icon {
                font-size: 48px;
                color: var(--primary-color);
                margin-bottom: var(--spacing-3);
              }

              .upload-text {
                display: flex;
                flex-direction: column;
                align-items: center;

                .main-text {
                  font-size: 16px;
                  color: var(--neutral-700);
                  margin-bottom: var(--spacing-1);

                  em {
                    font-style: normal;
                    color: var(--primary-color);
                    font-weight: 600;
                  }
                }

                .sub-text {
                  font-size: 14px;
                  color: var(--neutral-500);
                }
              }
            }
          }

          :deep(.el-upload__tip) {
            width: 100%;

            .upload-tip {
              display: flex;
              align-items: center;
              gap: var(--spacing-2);
              background-color: rgba(var(--neutral-50-rgb), 0.8);
              padding: var(--spacing-2) var(--spacing-3);
              border-radius: var(--border-radius-md);
              margin-top: var(--spacing-3);

              .el-icon {
                color: var(--warning-color);
                font-size: 16px;
              }

              span {
                font-size: 13px;
                color: var(--neutral-600);
                line-height: 1.4;
              }
            }
          }
        }
      }

      .file-settings {
        .file-info {
          margin-bottom: var(--spacing-6);

          .file-card {
            display: flex;
            align-items: center;
            gap: var(--spacing-3);
            background-color: rgba(var(--neutral-50-rgb), 0.7);
            padding: var(--spacing-3) var(--spacing-4);
            border-radius: var(--border-radius-md);
            box-shadow: var(--shadow-sm);
            position: relative;

            .file-icon {
              width: 40px;
              height: 40px;
              background-color: var(--primary-color);
              border-radius: var(--border-radius-md);
  display: flex;
  align-items: center;
              justify-content: center;

              .el-icon {
                font-size: 24px;
                color: white;
              }
            }

            .file-details {
              flex: 1;

              .file-name {
                font-size: 15px;
                font-weight: 600;
                color: var(--neutral-800);
                margin-bottom: var(--spacing-1);
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
                max-width: 300px;
              }

              .file-size {
                font-size: 13px;
                color: var(--neutral-500);
              }
            }

            .remove-button {
              margin-left: auto;
              opacity: 0.8;
              transition: all 0.3s ease;

              &:hover {
                opacity: 1;
                transform: scale(1.1);
              }
            }
          }
        }

        .voice-settings {
          margin-bottom: var(--spacing-6);

          .settings-title {
            font-size: 16px;
            font-weight: 600;
            color: var(--neutral-800);
            margin-bottom: var(--spacing-4);
          }

          .voice-selection {
            .label {
              display: block;
              margin-bottom: var(--spacing-2);
              font-size: 14px;
              color: var(--neutral-600);
            }

            .voice-select {
  width: 100%;

              :deep(.el-input__wrapper) {
                border-radius: var(--border-radius-md);
                transition: all 0.3s ease;

                &:focus-within {
                  transform: translateY(-2px);
                }
              }
            }

            .voice-option {
              display: flex;
              align-items: center;
              gap: var(--spacing-2);

              .voice-name {
                flex: 1;
              }

              .voice-tag {
                font-size: 12px;
                transform: scale(0.9);
                margin-left: auto;
              }
            }
          }
        }

        .action-buttons {
          display: flex;
          gap: var(--spacing-3);

          .process-button, .download-button {
            display: flex;
            align-items: center;
            gap: var(--spacing-2);
            padding: var(--spacing-2) var(--spacing-4);
            border-radius: var(--border-radius-md);
            transition: all 0.3s ease;

            .el-icon {
              font-size: 16px;
            }

  &:hover {
              transform: translateY(-2px);
            }
          }

          .process-button {
            position: relative;
            overflow: hidden;

            &::before {
              content: '';
              position: absolute;
              top: 0;
              left: 0;
              width: 100%;
              height: 100%;
              background: linear-gradient(45deg, rgba(255,255,255,0), rgba(255,255,255,0.2), rgba(255,255,255,0));
              transform: translateX(-100%);
              animation: shimmer 3s infinite;
            }
          }
        }
      }
    }
  }
}

// 动画定义
@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

@keyframes wave {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(-50%);
  }
}

@keyframes shimmer {
  0% {
    transform: translateX(-100%);
  }
  100% {
    transform: translateX(100%);
  }
}

.pulse-effect {
  animation: pulse-button 2s infinite;
}

@keyframes pulse-button {
  0% {
    box-shadow: 0 0 0 0 rgba(var(--primary-color-rgb), 0.7);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(var(--primary-color-rgb), 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(var(--primary-color-rgb), 0);
  }
}

.animate-fade-in {
  animation: fadeIn 0.8s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>

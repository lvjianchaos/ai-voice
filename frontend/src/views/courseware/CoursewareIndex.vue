<template>
  <div class="courseware-page">
    <div class="page-header animate-on-enter">
      <h1 class="page-title">音频课件制作</h1>
      <p class="page-description">上传PPT文件，使用AI技术自动生成带解说的视频课件</p>
      <div class="particles">
        <span class="particle" v-for="i in 5" :key="i"></span>
      </div>
    </div>

    <el-card class="courseware-card animate-on-enter">
      <div class="wave-background">
        <div class="wave"></div>
        <div class="wave"></div>
        <div class="wave"></div>
      </div>

      <div class="card-body">
        <div class="input-section">
          <div class="upload-container">
            <h3 class="section-title">
              <span class="icon-wrapper">
                <el-icon class="section-icon"><Upload /></el-icon>
              </span>
              <span>上传PPT文件</span>
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
              accept=".ppt,.pptx"
            >
              <div class="upload-content">
                <el-icon class="upload-icon"><UploadFilled /></el-icon>
                <div class="upload-text">
                  <span class="main-text">拖拽文件到此处或 <em>点击上传</em></span>
                  <span class="sub-text">仅支持 PPT / PPTX 格式文件</span>
          </div>
      </div>
              <template #tip>
                <div class="upload-tip">
                  <el-icon><InfoFilled /></el-icon>
                  <span>文件大小不超过50MB，将自动提取PPT内容生成解说词</span>
                </div>
              </template>
        </el-upload>

            <div v-if="fileList.length > 0" class="file-info">
              <div class="file-card">
                <div class="file-icon">
                  <el-icon><Document /></el-icon>
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
          </div>

          <div class="voice-settings-container">
            <h3 class="section-title">
              <span class="icon-wrapper">
                <el-icon class="section-icon"><Setting /></el-icon>
              </span>
              <span>语音设置</span>
            </h3>

            <div class="voice-selection">
              <span class="label">选择语音</span>
        <el-select
                v-model="selectedVoice"
                placeholder="请选择语音"
                class="voice-select"
                :loading="voiceLoading"
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

            <div class="voice-adjustments">
              <div class="slider-group">
                <div class="slider-item">
                  <div class="slider-label">
                    <span class="label">语速</span>
                    <span class="value">{{ speed }}</span>
                  </div>
        <el-slider
          v-model="speed"
          :min="0.5"
                    :max="2"
          :step="0.1"
                    show-stops
                    class="custom-slider"
                  >
                    <template #marks>
                      <template v-for="(value, key) in speedMarks" :key="key">
                        <div :style="{ left: `${(key - 0.5) * 100 / 1.5}%` }" class="slider-mark">
                          {{ value }}
                        </div>
                      </template>
                    </template>
                  </el-slider>
                </div>

                <div class="slider-item">
                  <div class="slider-label">
                    <span class="label">音调</span>
                    <span class="value">{{ tone }}</span>
                  </div>
        <el-slider
          v-model="tone"
                    :min="0.5"
                    :max="2"
          :step="0.1"
                    show-stops
                    class="custom-slider"
                  >
                    <template #marks>
                      <template v-for="(value, key) in toneMarks" :key="key">
                        <div :style="{ left: `${(key - 0.5) * 100 / 1.5}%` }" class="slider-mark">
                          {{ value }}
                        </div>
                      </template>
                    </template>
                  </el-slider>
                </div>

                <div class="slider-item">
                  <div class="slider-label">
                    <span class="label">节奏</span>
                    <span class="value">{{ rhythm }}</span>
                  </div>
        <el-slider
          v-model="rhythm"
          :min="0.5"
                    :max="2"
          :step="0.1"
                    show-stops
                    class="custom-slider"
                  >
                    <template #marks>
                      <template v-for="(value, key) in rhythmMarks" :key="key">
                        <div :style="{ left: `${(key - 0.5) * 100 / 1.5}%` }" class="slider-mark">
                          {{ value }}
                        </div>
                      </template>
                    </template>
                  </el-slider>
                </div>
              </div>
            </div>

            <div class="action-buttons">
              <el-button
                type="primary"
                @click="generateCourseware"
                :loading="generating"
                :disabled="!canGenerate"
                class="generate-button pulse-effect"
              >
                <el-icon><VideoPlay /></el-icon>
                <span>生成课件</span>
              </el-button>

      <el-button
                v-if="resultUrl"
        type="success"
        @click="downloadVideo"
                class="download-button"
      >
                <el-icon><Download /></el-icon>
                <span>下载视频</span>
      </el-button>
            </div>
          </div>
        </div>

        <div v-if="resultUrl" class="result-section animate-fade-in">
          <h3 class="result-title">课件结果</h3>
          <div class="loading-indicator" v-if="generating">
            <div class="loading-spinner"></div>
            <div class="loading-text">正在生成课件，请稍候...</div>
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: `${generatingProgress}%` }"></div>
            </div>
          </div>
          <div class="video-container" v-else>
            <video
              :src="resultUrl"
              controls
              class="video-player"
              ref="videoPlayer"
              @play="isPlaying = true"
              @pause="isPlaying = false"
              @ended="isPlaying = false"
            ></video>
          </div>
        </div>
    </div>
  </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed } from 'vue';
import { Upload, UploadFilled, InfoFilled, Document, Delete, Setting, VideoPlay, Download } from '@element-plus/icons-vue';
import { ElMessage, type UploadFile, type UploadUserFile } from 'element-plus';
import axios from 'axios';
import { getAll } from '@/api/sound';
import { getVoices, type VoiceItem } from '@/api/voice';

// 响应式变量
const selectedVoice = ref<number | string>('');
const speed = ref(1);
const tone = ref(1);
const rhythm = ref(1);
const videoUrl = ref('');
const coursewareFile = ref<File | null>(null);
const fileList = ref<UploadUserFile[]>([]);
const isGenerating = ref(false);
const isLoadingVoices = ref(false);
const voices = ref<VoiceItem[]>([]);
const voiceLoading = ref(false);
const generating = ref(false);
const resultUrl = ref('');
const videoPlayer = ref();
const isPlaying = ref(false);
const generatingProgress = ref(0);

// 速度、音调、节奏的标记点
const speedMarks = {
  0.5: '慢',
  1: '正常',
  1.5: '快',
  2: '极快',
};

const toneMarks = {
  0.5: '低',
  1: '正常',
  1.5: '高',
  2: '极高',
};

const rhythmMarks = {
  0.5: '缓慢',
  1: '正常',
  1.5: '紧凑',
  2: '极紧凑',
};

// 是否可以生成课件
const canGenerate = computed(() => {
  return fileList.value.length > 0 && selectedVoice.value !== '';
});

// 文件处理逻辑
const handleFileChange = (uploadFile: UploadFile) => {
  if (!validateFile(uploadFile)) {
    fileList.value = [];
    return;
  }

  // 限制只有一个文件，设置后会替换之前的文件
  fileList.value = [uploadFile];
};

const handleFileRemove = () => {
  fileList.value = [];
};

const validateFile = (file: UploadFile): boolean => {
  // 验证文件类型
  const isValidType = [
    'application/vnd.ms-powerpoint',
    'application/vnd.openxmlformats-officedocument.presentationml.presentation',
  ].includes(file.raw?.type || '');

  if (!isValidType) {
    ElMessage.error('只能上传PPT/PPTX格式文件!');
    return false;
  }

  // 验证文件大小
  const isLt50M = (file.size || 0) / 1024 / 1024 < 50;
  if (!isLt50M) {
    ElMessage.error('文件大小不能超过50MB!');
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

// 声音样本加载
const getVoiceOptions = async () => {
  isLoadingVoices.value = true;
  try {
    const response = await getVoices();
    if (response.data.success) {
      voices.value = response.data.data;
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
  if (!canGenerate.value) {
    ElMessage.warning('请上传课件并选择语音');
    return;
  }

  try {
    generating.value = true;
    generatingProgress.value = 0;
    resultUrl.value = ''; // 清空之前的结果

    // 模拟生成进度
    const progressInterval = setInterval(() => {
      if (generatingProgress.value < 95) {
        generatingProgress.value += Math.random() * 10;
      }
    }, 1000);

    // 在实际项目中，这里应该提交文件到后端进行处理
    // 模拟API调用延迟
    setTimeout(() => {
      clearInterval(progressInterval);
      generatingProgress.value = 100;

      // 模拟返回结果URL
      resultUrl.value = 'https://example.com/sample-courseware.mp4';
      ElMessage.success('有声课件生成成功');
      generating.value = false;
    }, 8000);

  } catch (error) {
    console.error('生成失败:', error);
    ElMessage.error('生成失败，请检查参数后重试');
    generating.value = false;
  }
};

// 下载处理
const downloadVideo = () => {
  if (resultUrl.value) {
    const link = document.createElement('a');
    link.href = resultUrl.value;
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
  getVoiceOptions();
  animateElements();
});
</script>

<style lang="scss" scoped>
.courseware-page {
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

  .courseware-card {
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
    }

    .input-section {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: var(--spacing-6);
      margin-bottom: var(--spacing-6);

      @media (max-width: 992px) {
        grid-template-columns: 1fr;
      }

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

      .upload-container {
        display: flex;
        flex-direction: column;

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

        .file-info {
          margin-top: var(--spacing-4);

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
                max-width: 200px;
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

.voice-settings-container {
  display: flex;
  flex-direction: column;

  .voice-selection {
    margin-bottom: var(--spacing-5);

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

  .voice-adjustments {
    margin-bottom: var(--spacing-5);

    .slider-group {
      display: flex;
      flex-direction: column;
      gap: var(--spacing-5);

      .slider-item {
        .slider-label {
          display: flex;
          justify-content: space-between;
          margin-bottom: var(--spacing-2);

          .label {
            font-size: 14px;
            color: var(--neutral-600);
          }

          .value {
            font-size: 14px;
            font-weight: 600;
            color: var(--primary-color);
            background-color: rgba(var(--primary-color-rgb), 0.1);
            padding: 0 var(--spacing-2);
            border-radius: var(--border-radius-sm);
          }
        }

        .custom-slider {
          :deep(.el-slider__bar) {
            background-color: var(--primary-color);
          }

          :deep(.el-slider__button) {
            border-color: var(--primary-color);
            width: 16px;
            height: 16px;
            transition: all 0.3s ease;

            &:hover, &:active {
              transform: scale(1.2);
            }
          }

          :deep(.el-slider__runway) {
            height: 8px;
            border-radius: 4px;
            background-color: rgba(var(--neutral-300-rgb), 0.3);
          }

          :deep(.el-slider__marks) {
            position: absolute;
            top: 20px;
            width: 100%;

            .slider-mark {
              position: absolute;
              font-size: 12px;
              color: var(--neutral-500);
              transform: translateX(-50%);
            }
          }
        }
      }
    }
  }

  .action-buttons {
    display: flex;
    gap: var(--spacing-3);
    margin-top: auto;

    .generate-button, .download-button {
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

.generate-button {
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

.result-section {
  background-color: rgba(var(--neutral-50-rgb), 0.7);
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-5);
  box-shadow: var(--shadow-inner);
  position: relative;

  .result-title {
    font-size: 18px;
    font-weight: 600;
    color: var(--neutral-800);
    margin-bottom: var(--spacing-4);
    text-align: center;
  }

  .loading-indicator {
  display: flex;
  flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: var(--spacing-6);

    .loading-spinner {
      width: 48px;
      height: 48px;
      border: 4px solid rgba(var(--primary-color-rgb), 0.2);
      border-left-color: var(--primary-color);
      border-radius: 50%;
      animation: spin 1s linear infinite;
      margin-bottom: var(--spacing-4);
    }

    .loading-text {
      font-size: 16px;
      color: var(--neutral-700);
      margin-bottom: var(--spacing-4);
    }

    .progress-bar {
      width: 100%;
      max-width: 400px;
      height: 8px;
      background-color: rgba(var(--neutral-300-rgb), 0.3);
      border-radius: 4px;
      overflow: hidden;

      .progress-fill {
        height: 100%;
        background-color: var(--primary-color);
        border-radius: 4px;
        transition: width 0.3s ease;
      }
    }
  }

  .video-container {
    width: 100%;

    .video-player {
      width: 100%;
      border-radius: var(--border-radius-md);
      box-shadow: var(--shadow-md);
    }
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

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
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
</style>

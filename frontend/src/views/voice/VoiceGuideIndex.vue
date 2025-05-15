<template>
  <div class="voice-guide-page">
    <div class="page-header animate-on-enter">
      <h1 class="page-title">个性化语音指南</h1>
      <p class="page-description">通过AI技术将您的文本转换为自然、流畅的语音解说</p>
      <div class="particles">
        <span class="particle" v-for="i in 5" :key="i"></span>
      </div>
    </div>

    <el-card class="voice-card animate-on-enter">
      <div class="wave-background">
        <div class="wave"></div>
        <div class="wave"></div>
        <div class="wave"></div>
      </div>

      <div class="card-body">
        <div class="input-section">
          <div class="content-input-container">
            <h3 class="section-title">
              <span class="icon-wrapper">
                <el-icon class="section-icon"><Document /></el-icon>
              </span>
              <span>语音内容</span>
            </h3>
            <el-input
              v-model="content"
              type="textarea"
              :rows="8"
              placeholder="请输入需要转换为语音的文本内容..."
              class="content-input"
              :maxlength="2000"
              show-word-limit
              resize="none"
            />
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
                @change="handleVoiceChange"
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
                @click="generateVoice"
                :loading="generating"
                :disabled="!canGenerate"
                class="generate-button pulse-effect"
              >
                <el-icon><VideoPlay /></el-icon>
                <span>生成语音</span>
              </el-button>

              <el-button
                v-if="resultUrl"
                type="success"
                @click="downloadAudio"
                class="download-button"
              >
                <el-icon><Download /></el-icon>
                <span>下载音频</span>
              </el-button>
            </div>
          </div>
        </div>

        <div v-if="resultUrl" class="result-section animate-fade-in">
          <h3 class="result-title">语音结果</h3>
          <div class="audio-visualizer" v-if="isPlaying">
            <div class="bar" v-for="i in 20" :key="i"></div>
          </div>
          <audio
            :src="resultUrl"
            controls
            class="audio-player"
            ref="audioPlayer"
            @play="isPlaying = true"
            @pause="isPlaying = false"
            @ended="isPlaying = false"
          ></audio>
        </div>
    </div>
  </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted, nextTick } from 'vue';
import { Document, Setting, VideoPlay, Download } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { getVoices, generateVoice as apiGenerateVoice, type VoiceItem } from '@/api/voice';

// 响应式变量
const content = ref('');
const selectedVoice = ref<number | string>('');
const voices = ref<VoiceItem[]>([]);
const voiceLoading = ref(false);
const speed = ref(1);
const tone = ref(1);
const rhythm = ref(1);
const generating = ref(false);
const resultUrl = ref('');
const audioPlayer = ref();
const isPlaying = ref(false);

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

// 是否可以生成语音
const canGenerate = computed(() => {
  return content.value.trim() !== '' && selectedVoice.value !== '';
});

// 获取所有可用的语音
const fetchVoices = async () => {
  try {
    voiceLoading.value = true;
    const response = await getVoices();
    if (response.data.success) {
      voices.value = response.data.data;
      // 如果有数据，默认选择第一个
      if (voices.value.length > 0) {
        selectedVoice.value = voices.value[0].id;
      }
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

// 处理语音选择变化
const handleVoiceChange = (value: number | string) => {
  console.log('选择的语音ID：', value);
};

// 生成语音
const generateVoice = async () => {
  if (!canGenerate.value) {
    ElMessage.warning('请填写内容并选择语音');
    return;
  }

  try {
    generating.value = true;
    const params = {
      content: content.value,
      voiceId: selectedVoice.value,
        speed: speed.value,
        tone: tone.value,
        rhythm: rhythm.value,
    };

    const response = await apiGenerateVoice(params);
    if (response.data.success) {
      resultUrl.value = response.data.data;
      ElMessage.success('语音生成成功');

      // 添加一小段延迟，确保音频URL已经加载完毕
      setTimeout(() => {
        nextTick(() => {
          if (audioPlayer.value) {
            animateElements();
          }
        });
      }, 500);
    } else {
      ElMessage.error('语音生成失败');
    }
  } catch (error) {
    console.error('生成语音错误', error);
    ElMessage.error('生成语音错误');
  } finally {
    generating.value = false;
  }
};

// 下载音频
const downloadAudio = () => {
  if (!resultUrl.value) {
    ElMessage.warning('没有可下载的音频');
    return;
  }

    const link = document.createElement('a');
  link.href = resultUrl.value;
  link.download = `语音指南_${new Date().getTime()}.mp3`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  ElMessage.success('音频下载中...');
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
.voice-guide-page {
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

  .voice-card {
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

      .content-input-container {
        display: flex;
        flex-direction: column;

        .content-input {
          :deep(.el-textarea__inner) {
            border-radius: var(--border-radius-md);
            resize: none;
            transition: all 0.3s ease;
            font-size: 15px;
            line-height: 1.6;
            padding: var(--spacing-3);
            height: 100%;
            min-height: 200px;

            &:focus {
              box-shadow: 0 0 0 2px rgba(var(--primary-color-rgb), 0.2);
              transform: translateY(-2px);
            }
          }

          :deep(.el-input__count) {
            background: transparent;
            font-size: 12px;
            color: var(--neutral-500);
          }
        }
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

      .audio-player {
        width: 100%;
        height: 48px;
        border-radius: var(--border-radius-md);
        outline: none;
      }

      .audio-visualizer {
        position: absolute;
        top: -15px;
        left: 0;
  width: 100%;
        height: 15px;
        display: flex;
        align-items: flex-end;
        justify-content: center;
        gap: 3px;
        pointer-events: none;

        .bar {
          width: 3px;
          background-color: var(--primary-color);
          border-radius: 1px;
          height: 5px;
          animation: equalizer 0.8s infinite alternate;

          @for $i from 1 through 20 {
            &:nth-child(#{$i}) {
              animation-delay: 0.05s * $i;
            }
          }
        }
      }
    }
  }
}

// 动画
@keyframes shimmer {
  0% {
    transform: translateX(-100%);
  }
  100% {
    transform: translateX(100%);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 0.6;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.1);
  }
}

@keyframes equalizer {
  0% {
    height: 3px;
  }
  100% {
    height: 15px;
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

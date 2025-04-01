<template>
  <div class="samples-page">
    <div class="page-header animate-on-enter">
      <h1 class="page-title">声音样本库</h1>
      <p class="page-description">管理您的声音样本，用于生成个性化的语音内容</p>
      <div class="particles">
        <span class="particle" v-for="i in 5" :key="i"></span>
      </div>
    </div>

    <el-card class="samples-card hover-card animate-on-enter">
      <div class="card-header">
        <div class="left-actions">
          <el-button
            type="primary"
            @click="dlgCreateOrEdit?.initAndShow()"
            class="create-button pulse-effect"
          >
            <el-icon><Plus /></el-icon>
            <span>新建样本</span>
          </el-button>
        </div>

        <div class="search-container">
          <el-input
            v-model="searchName"
            placeholder="搜索样本名称"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
            class="search-input"
          >
            <template #prefix>
              <el-icon class="search-icon"><Search /></el-icon>
            </template>
          </el-input>
          <el-button type="primary" @click="handleSearch" class="search-button hover-button">
            查询
          </el-button>
        </div>
      </div>

      <transition-group
        name="list"
        tag="div"
        class="samples-list-container"
      >
        <el-table
          :data="tableData"
          border
          stripe
          highlight-current-row
          class="samples-table"
        >
          <el-table-column type="index" label="序号" align="center" width="80" />
          <el-table-column prop="name" label="样本名称" align="center" width="180" />
          <el-table-column prop="gender" label="性别" align="center" width="80">
            <template #default="{ row }">
              <el-tag
                :type="row.gender === '男' ? 'info' : 'primary'"
                size="small"
                effect="light"
                class="tag-animate"
              >
                {{ row.gender }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="language" label="语言" align="center" width="120">
            <template #default="{ row }">
              <el-tag type="success" size="small" effect="light" class="tag-animate">
                {{ row.language }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="discription" label="样本描述" align="center" />
          <el-table-column label="操作" align="center" width="320">
            <template #default="{ row }">
              <div class="table-actions">
                <el-button
                  type="primary"
                  size="small"
                  plain
                  @click="dlgCreateOrEdit?.initAndShow(row.id)"
                  class="action-button edit-button"
                >
                  <el-icon><Edit /></el-icon>
                  <span>编辑</span>
                </el-button>
                <el-button
                  type="danger"
                  size="small"
                  plain
                  @click="handleDelete(row.id)"
                  class="action-button delete-button"
                >
                  <el-icon><Delete /></el-icon>
                  <span>删除</span>
                </el-button>
              </div>
              <div class="audio-container">
                <div class="audio-visualizer" v-if="playingAudio === row.id">
                  <div class="bar" v-for="i in 10" :key="i"></div>
                </div>
                <audio
                  :src="row.audioUrl"
                  controls
                  class="audio-player"
                  @play="playingAudio = row.id"
                  @pause="playingAudio = null"
                  @ended="playingAudio = null"
                ></audio>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </transition-group>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 30, 40]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
        />
      </div>

      <div class="empty-state" v-if="tableData.length === 0">
        <div class="empty-icon">
          <el-icon><Document /></el-icon>
        </div>
        <p class="empty-text">暂无样本数据</p>
        <el-button type="primary" @click="dlgCreateOrEdit?.initAndShow()" class="hover-button">
          创建第一个样本
        </el-button>
      </div>
    </el-card>

    <DlgSampleCreateOrEdit ref="dlgCreateOrEdit" @sample-updated="fetchData" />
  </div>
</template>

<script lang="ts" setup>
import DlgSampleCreateOrEdit from './DlgSampleCreateOrEdit.vue';
import { ref, computed, onMounted, nextTick } from 'vue';
import { allSound, getAllSound, handleDelete as deleteSound } from '@/composables/useSounds';
import { Plus, Edit, Delete, Document, Search } from '@element-plus/icons-vue';
import { ElMessageBox } from 'element-plus';

const dlgCreateOrEdit = ref();
const searchName = ref(''); // 搜索名称
const currentPage = ref(1); // 当前页码
const pageSize = ref(10); // 每页显示条数
const total = ref(0); // 总条数
const playingAudio = ref<number | null>(null); // 当前正在播放的音频ID

// 计算属性，用于根据搜索名称和分页信息过滤数据
const tableData = computed(() => {
  const filteredData = allSound.value.filter((item) => {
    if (searchName.value) {
      return item.name.includes(searchName.value);
    }
    return true;
  });

  // 分页
  const startIndex = (currentPage.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  return filteredData.slice(startIndex, endIndex);
});

// 单独更新总条数的函数
const updateTotal = () => {
  const filteredData = allSound.value.filter((item) => {
    if (searchName.value) {
      return item.name.includes(searchName.value);
    }
    return true;
  });
  total.value = filteredData.length;
};

// 获取所有样本数据
const fetchData = async () => {
  await getAllSound();
  updateTotal(); // 更新总条数
  nextTick(() => {
    animateItems();
  });
};

// 进入动画
const animateItems = () => {
  const elements = document.querySelectorAll('.animate-on-enter');
  elements.forEach((el, index) => {
    setTimeout(() => {
      el.classList.add('animated');
    }, index * 200);
  });
};

// 删除确认
const confirmDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('此操作将永久删除该样本，是否继续？', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      customClass: 'custom-message-box',
      closeOnClickModal: false,
    });
    return true;
  } catch {
    return false;
  }
};

// 删除操作
const handleDelete = async (id: number) => {
  const confirmed = await confirmDelete(id);
  if (confirmed) {
    await deleteSound(id);
    fetchData(); // 重新获取数据
  }
};

// 搜索
const handleSearch = () => {
  currentPage.value = 1; // 搜索时重置为第一页
  updateTotal(); // 更新总条数
};

// 每页显示条数改变
const handleSizeChange = (size: number) => {
  pageSize.value = size;
  currentPage.value = 1; // 改变每页显示条数时重置为第一页
};

// 当前页码改变
const handleCurrentChange = (page: number) => {
  currentPage.value = page;
};

onMounted(() => {
  fetchData(); // 在组件加载时获取所有样本数据
  animateItems(); // 添加进入动画
});
</script>

<style lang="scss" scoped>
.samples-page {
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

  .samples-card {
    border-radius: var(--border-radius-lg);
    box-shadow: var(--shadow-md);
    opacity: 0;
    transform: translateY(20px);
    transition: all 0.8s cubic-bezier(0.34, 1.56, 0.64, 1);
    transition-delay: 0.2s;

    &.animated {
      opacity: 1;
      transform: translateY(0);
    }

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: var(--spacing-6);
      flex-wrap: wrap;
      gap: var(--spacing-4);

      .left-actions {
        display: flex;
        gap: var(--spacing-3);

        .create-button {
          display: flex;
          align-items: center;
          gap: var(--spacing-2);
          border-radius: var(--border-radius-md);
          padding: var(--spacing-2) var(--spacing-4);
          font-weight: 500;
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

          .el-icon {
            font-size: 16px;
          }
        }
      }

      .search-container {
        display: flex;
        align-items: center;
        gap: var(--spacing-3);
        flex: 1;
        max-width: 500px;
        margin-left: auto;

        .search-input {
          :deep(.el-input__wrapper) {
            border-radius: var(--border-radius-md);
            transition: all 0.3s ease;

            &:focus-within {
              transform: translateY(-2px);
              box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }
          }

          .search-icon {
            color: var(--neutral-500);
            transition: color 0.3s ease;
          }

          &:focus-within {
            .search-icon {
              color: var(--primary-color);
            }
          }
        }

        .search-button {
          border-radius: var(--border-radius-md);
          padding: var(--spacing-2) var(--spacing-6);
        }
      }
    }

    .samples-list-container {
      margin-bottom: var(--spacing-6);
    }

    .samples-table {
      :deep(.el-table__body) {
        width: 100% !important; // 解决某些情况下表格宽度问题
      }

      :deep(.el-table__row) {
        transition: all 0.3s ease;

        td {
          transition: all 0.3s ease;
        }

        &:hover {
          td {
            background-color: rgba(var(--primary-color-rgb), 0.05) !important;
          }
        }

        .tag-animate {
          position: relative;
          overflow: hidden;

          &::after {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: linear-gradient(45deg, rgba(255,255,255,0), rgba(255,255,255,0.3), rgba(255,255,255,0));
            transform: translateX(-100%);
            animation: shimmer 3s infinite;
          }
        }
      }
    }

    .table-actions {
      display: flex;
      justify-content: center;
      gap: var(--spacing-2);
      margin-bottom: var(--spacing-2);

      .action-button {
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
        gap: var(--spacing-1);
        border-radius: var(--border-radius-sm);

        &:hover {
          transform: translateY(-2px);
        }

        &.edit-button:hover {
          background-color: rgba(var(--primary-color-rgb), 0.1);
          color: var(--primary-color);
        }

        &.delete-button:hover {
          background-color: rgba(var(--danger-color-rgb), 0.1);
          color: var(--danger-color);
        }
      }
    }

    .audio-container {
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;

      .audio-player {
        width: 100%;
        height: 30px;
        border-radius: var(--border-radius-sm);
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

        .bar {
          width: 3px;
          background-color: var(--primary-color);
          border-radius: 1px;
          height: 5px;
          animation: equalizer 0.8s infinite alternate;

          &:nth-child(1) { animation-delay: 0.1s; }
          &:nth-child(2) { animation-delay: 0.2s; }
          &:nth-child(3) { animation-delay: 0.3s; }
          &:nth-child(4) { animation-delay: 0.4s; }
          &:nth-child(5) { animation-delay: 0.5s; }
          &:nth-child(6) { animation-delay: 0.4s; }
          &:nth-child(7) { animation-delay: 0.3s; }
          &:nth-child(8) { animation-delay: 0.2s; }
          &:nth-child(9) { animation-delay: 0.1s; }
          &:nth-child(10) { animation-delay: 0.0s; }
        }
      }
    }

    .pagination-container {
      display: flex;
      justify-content: center;
      margin-top: var(--spacing-6);

      :deep(.el-pagination) {
        .el-pagination__sizes {
          margin-right: var(--spacing-4);
        }

        .btn-prev, .btn-next, .el-pager li {
          transition: all 0.3s ease;

          &:hover, &.is-active {
            transform: translateY(-2px);
          }
        }
      }
    }

    .empty-state {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: var(--spacing-12);
      color: var(--neutral-500);

      .empty-icon {
        font-size: 48px;
        margin-bottom: var(--spacing-4);
        color: var(--neutral-300);
        animation: pulse 2s infinite ease-in-out;
      }

      .empty-text {
        font-size: 16px;
        margin-bottom: var(--spacing-6);
      }
    }
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

// 列表动画
.list-enter-active,
.list-leave-active {
  transition: all 0.5s ease;
}
.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateY(30px);
}

// 自定Message Box
:deep(.custom-message-box) {
  border-radius: var(--border-radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-lg);
}
</style>

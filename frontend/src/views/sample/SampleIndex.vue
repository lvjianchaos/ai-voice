<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <el-button type="primary" @click="dlgCreateOrEdit?.initAndShow()">新建样本</el-button>
        <div style="display: flex; align-items: center;">
          <el-input v-model="searchName" placeholder="请输入样本名称" clearable @clear="handleSearch" @keyup.enter="handleSearch" style="width: 200px; margin-right: 10px;" />
          <el-button type="primary" @click="handleSearch">查询</el-button>
        </div>
      </div>
    </template>
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column type="index" label="序号" align="center" width="80" />
      <el-table-column prop="name" label="样本名称" align="center" width="180" />
      <el-table-column prop="gender" label="性别" align="center" width="80" />
      <el-table-column prop="language" label="语言" align="center" width="120" />
      <el-table-column prop="discription" label="样本描述" align="center" />
      <el-table-column label="操作" align="center">
        <template #default="{ row }">
          <el-button type="primary" size="default" @click="dlgCreateOrEdit?.initAndShow(row.id)">编辑</el-button>
          <el-button type="danger" size="default" @click="handleDelete(row.id)">删除</el-button>
          <div class="audio-container">
            <audio :src="row.audioUrl" controls class="audio-player"></audio>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 30, 40]"
      :small="false"
      :disabled="false"
      :background="true"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      style="margin-top: 20px; display: flex; justify-content: center"
    />
    <DlgSampleCreateOrEdit ref="dlgCreateOrEdit" />
  </el-card>
</template>

<script lang="ts" setup>
import DlgSampleCreateOrEdit from './DlgSampleCreateOrEdit.vue';
import { ref, computed, onMounted } from 'vue';
import { allSound, getAllSound, handleDelete } from '@/composables/useSounds';

const dlgCreateOrEdit = ref<InstanceType<typeof DlgSampleCreateOrEdit>>();
const searchName = ref(''); // 搜索名称
const currentPage = ref(1); // 当前页码
const pageSize = ref(10); // 每页显示条数
const total = ref(0); // 总条数

// 计算属性，用于根据搜索名称和分页信息过滤数据
const tableData = computed(() => {
  let filteredData = allSound.value;

  // 搜索过滤
  if (searchName.value) {
    filteredData = filteredData.filter((item) => item.name.includes(searchName.value));
  }

  total.value = filteredData.length; // 更新总条数

  // 分页
  const startIndex = (currentPage.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  return filteredData.slice(startIndex, endIndex);
});

// 获取所有样本数据
const fetchData = async () => {
  await getAllSound();
};

// 搜索
const handleSearch = () => {
  currentPage.value = 1; // 搜索时重置为第一页
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
});
</script>

<style lang="scss" scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  /*width: auto;*/
}

.audio-container {
  width: 100%;
  display: flex;
  justify-content: center;
}

.audio-player {
  width: 80%;
  max-width: 300px;
}
</style>

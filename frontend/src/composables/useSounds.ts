// src/composables/useSounds.ts

import { ref, reactive } from 'vue'
import { saveOrUpdate, getAll, deleteSample } from '@/api/sound'
import type { SoundItem, SoundParams } from '@/api/sound'
import { ElMessage, ElMessageBox } from 'element-plus'

// 保存样本信息
export const allSound = ref<SoundItem[]>([]) // 初始化为空数组

// 获取所有样本信息
export const getAllSound = async () => {
  try {
    const response = await getAll()
    if (response.data.success === true) {
      allSound.value = response.data.data
    } else {
      ElMessage.error('获取样本信息失败!')
      throw new Error('获取样本信息失败!')
    }
  } catch (error) {
    ElMessage.error('获取样本信息失败!')
    console.error(error)
  }
}

// 确认按钮事件处理
export const onSubmit = async (form: SoundParams) => {
  try {
    const response = await saveOrUpdate(form) // 直接传递 form 对象
    if (response.data.success === true) {
      ElMessage.success(`${msgText.value}样本成功!`)
      await getAllSound() // 重新获取样本列表
    } else {
      ElMessage.error(`${msgText.value}样本失败!`)
      throw new Error(`${msgText.value}样本失败!`)
    }
  } catch (error: any) {
    ElMessage.error(`${msgText.value}样本失败: ${error.message}`)
    console.error(error)
  } finally {
    dialogFormVisible.value = false // 始终关闭对话框
  }
}

// 表单数据
export const form = reactive<SoundParams>({
  name: '',
  gender: '',
  language: '',
  discription: '',
  audioFile: null,
})

export const msgText = ref('')
export const isCreate = ref(true)

export const dialogFormVisible = ref(false)

// 删除事件处理
export const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('此操作将永久删除该文件, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })

    const response = await deleteSample(id)
    if (response.data.success) {
      await getAllSound()
      ElMessage.success('删除样本成功!')
    } else {
      ElMessage.error('删除样本失败!')
      throw new Error('删除样本失败!')
    }
  } catch (error) {
    ElMessage.info('已取消删除')
    console.error(error)
  }
}

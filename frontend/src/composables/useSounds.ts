import { saveOrUpdate, getAll, deleteSample } from '@/api/sound'
import type { SoundItem } from '@/api/sound'
// 保存样本信息
export const allSound = ref([
  {
    id: 0,
    name: 'Chaos',
    gender: 'man',
    language: 'Chinese',
    discription: '无描述',
  },
  {
    id: 0,
    name: 'Chaos',
    gender: 'man',
    language: 'Chinese',
    discription: '无描述',
  },
  {
    id: 0,
    name: 'Chaos',
    gender: 'man',
    language: 'Chinese',
    discription: '无描述',
  },
  {
    id: 0,
    name: 'Chaos',
    gender: 'man',
    language: 'Chinese',
    discription: '无描述',
  },
  {
    id: 0,
    name: 'Chaos',
    gender: 'man',
    language: 'Chinese',
    discription: '无描述',
  },
  {
    id: 0,
    name: 'Chaos',
    gender: 'man',
    language: 'Chinese',
    discription: '无描述',
  },
  {
    id: 0,
    name: 'Chaos',
    gender: 'man',
    language: 'Chinese',
    discription: '无描述',
  },
  {
    id: 0,
    name: 'Chaos',
    gender: 'man',
    language: 'Chinese',
    discription: '无描述',
  },
  {
    id: 0,
    name: 'Chaos',
    gender: 'man',
    language: 'Chinese',
    discription: '无描述',
  },
  {
    id: 0,
    name: 'Chaos',
    gender: 'man',
    language: 'Chinese',
    discription: '无描述',
  },
  {
    id: 0,
    name: 'Chaos',
    gender: 'man',
    language: 'Chinese',
    discription: '无描述',
  },
  {
    id: 0,
    name: 'Chaos',
    gender: 'man',
    language: 'Chinese',
    discription: '无描述',
  },
  {
    id: 0,
    name: 'Chaos',
    gender: 'man',
    language: 'Chinese',
    discription: '无描述',
  },
] as SoundItem[])

// 获取所有样本信息
export const getAllSound = async () => {
  const { data } = await getAll()
  if (data.success === true) {
    allSound.value = data.data
  } else {
    ElMessage.error('获取样本信息失败!')
    throw new Error('获取样本信息失败!')
  }
}

// 确认按钮事件处理
export const onSubmit = async () => {
  const { data } = await saveOrUpdate(form).finally(() => (dialogFormVisible.value = false))
  // console.log(res)
  if (data.success === true) {
    ElMessage.success(`${msgText.value}样本成功!`)
    getAllSound()
  } else {
    ElMessage.error(`${msgText.value}样本失败!`)
    throw new Error(`${msgText.value}样本失败!`)
  }
}

export const form = reactive({
  name: '',
  gender: '',
  language: '',
  discription: '',
})

export const msgText = ref('')
export const isCreate = ref(true)

export const dialogFormVisible = ref(false)

// 删数事件处理
export const handleDelete = async (id: number) => {
  await ElMessageBox.confirm('此操作将永久删除该文件, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).catch(() => {
    ElMessage.info('已取消删除')
    return new Promise(() => {})
  })
  const { data } = await deleteSample(id)
  if (data.success) {
    getAllSound()
    ElMessage.success('删除样本成功!')
  } else {
    ElMessage.error('删除样本失败!')
    throw new Error('删除样本失败!')
  }
}

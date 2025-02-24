import type { CreateOrEditSound } from '@/api/sound'
import { update } from '@/api/sound'
export function useSounds() {
  // 表单的响应式数据
  const form = ref({} as CreateOrEditSound)
  // 表单提交事件
  const onSubmit = async () => {
    const res = await update(form.value)
    console.log(res)
  }
  return { form, onSubmit }
}

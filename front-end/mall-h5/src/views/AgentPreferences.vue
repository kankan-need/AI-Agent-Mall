<template>
  <div class="page preferences">
    <header class="top-bar">
      <button class="back" @click="router.back()">‹ 返回</button>
      <h1>购物偏好</h1>
    </header>

    <div class="card form-card">
      <div class="field">
        <label>关注类型</label>
        <div class="focus-options">
          <button
            v-for="item in focusOptions"
            :key="item.value"
            :class="{ active: form.focusType === item.value }"
            @click="form.focusType = item.value"
          >
            {{ item.label }}
          </button>
        </div>
      </div>

      <div class="field">
        <label>预算下限（元）</label>
        <input v-model="budgetMinYuan" type="number" min="0" step="0.01" placeholder="可不填" />
      </div>

      <div class="field">
        <label>预算上限（元）</label>
        <input v-model="budgetMaxYuan" type="number" min="0" step="0.01" placeholder="可不填" />
      </div>

      <div class="field">
        <label>偏好标签</label>
        <input v-model="tagsText" type="text" placeholder="多个标签用逗号分隔，如：轻便,送礼" />
      </div>

      <div class="field">
        <label>偏好摘要</label>
        <textarea v-model="form.summary" rows="4" placeholder="描述你的购物偏好" />
      </div>
    </div>

    <button class="btn-primary save-btn" :disabled="saving" @click="handleSave">
      {{ saving ? '保存中...' : '保存偏好' }}
    </button>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getPreference, updatePreference } from '@/api/agent'
import { getToken } from '@/utils/auth'

const router = useRouter()
const saving = ref(false)
const budgetMinYuan = ref('')
const budgetMaxYuan = ref('')
const tagsText = ref('')
const form = ref({
  focusType: 'BALANCED',
  summary: ''
})

const focusOptions = [
  { value: 'VALUE', label: '性价比优先' },
  { value: 'QUALITY', label: '品质优先' },
  { value: 'BALANCED', label: '均衡' }
]

function yuanToFee(value) {
  if (value === '' || value == null) return null
  const num = Number(value)
  if (Number.isNaN(num)) return null
  return Math.round(num * 100)
}

function feeToYuan(fee) {
  if (fee == null) return ''
  return (fee / 100).toFixed(2)
}

async function loadPreference() {
  if (!getToken()) {
    router.push('/login')
    return
  }
  const data = await getPreference()
  form.value.focusType = data.focusType || 'BALANCED'
  form.value.summary = data.summary || ''
  budgetMinYuan.value = feeToYuan(data.budgetMin)
  budgetMaxYuan.value = feeToYuan(data.budgetMax)
  tagsText.value = (data.tags || []).join(',')
}

async function handleSave() {
  saving.value = true
  try {
    await updatePreference({
      focusType: form.value.focusType,
      budgetMin: yuanToFee(budgetMinYuan.value),
      budgetMax: yuanToFee(budgetMaxYuan.value),
      tags: tagsText.value
        .split(/[,，]/)
        .map(item => item.trim())
        .filter(Boolean),
      summary: form.value.summary
    })
    alert('保存成功')
  } catch (err) {
    alert(err?.message || '保存失败')
  } finally {
    saving.value = false
  }
}

onMounted(loadPreference)
</script>

<style scoped>
.preferences {
  background: #f7f8fa;
  min-height: 100vh;
  padding-bottom: 24px;
}

.top-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
}

.back {
  border: none;
  background: rgba(255, 255, 255, 0.18);
  color: #fff;
  border-radius: 14px;
  padding: 6px 10px;
  font-size: 14px;
}

.top-bar h1 {
  margin: 0;
  font-size: 18px;
}

.form-card {
  margin: 12px;
  padding: 16px;
}

.field {
  margin-bottom: 16px;
}

.field label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: #646566;
}

.field input,
.field textarea {
  width: 100%;
  border: 1px solid #ebedf0;
  border-radius: 10px;
  padding: 10px 12px;
  font-size: 14px;
  box-sizing: border-box;
  background: #fff;
}

.focus-options {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.focus-options button {
  border: 1px solid #ebedf0;
  background: #fff;
  color: #646566;
  border-radius: 18px;
  padding: 8px 14px;
  font-size: 13px;
}

.focus-options button.active {
  border-color: #667eea;
  color: #667eea;
  background: rgba(102, 126, 234, 0.08);
}

.save-btn {
  display: block;
  width: calc(100% - 24px);
  margin: 0 12px;
}
</style>

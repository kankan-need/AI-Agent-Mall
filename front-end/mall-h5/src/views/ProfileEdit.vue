<template>
  <div class="page profile-edit">
    <div class="card panel">
      <h2>编辑资料</h2>
      <label>昵称</label>
      <input v-model="form.nickName" />
      <label>头像 URL</label>
      <input v-model="form.pic" placeholder="https://..." />
      <button class="btn-primary" :disabled="loading" @click="handleSave">
        {{ loading ? '保存中...' : '保存' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getUserDetail, updateUser } from '@/api/user'

const router = useRouter()
const loading = ref(false)
const form = ref({ nickName: '', pic: '' })

async function loadDetail() {
  const data = await getUserDetail()
  form.value = { nickName: data.nickName, pic: data.pic }
}

async function handleSave() {
  loading.value = true
  try {
    await updateUser(form.value)
    router.push('/my')
  } catch (e) {
    alert(e.message || '保存失败')
  } finally {
    loading.value = false
  }
}

onMounted(loadDetail)
</script>

<style scoped>
.panel {
  margin: 12px;
  padding: 20px;
}
h2 {
  margin: 0 0 16px;
}
label {
  display: block;
  margin-top: 12px;
  margin-bottom: 6px;
}
input {
  width: 100%;
  border: 1px solid #ebedf0;
  border-radius: 8px;
  padding: 10px 12px;
}
.btn-primary {
  width: 100%;
  margin-top: 20px;
}
</style>

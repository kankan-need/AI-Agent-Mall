<template>
  <div class="page register">
    <div class="card panel">
      <h2>用户注册</h2>
      <label>用户名</label>
      <input v-model="form.userName" placeholder="4-30位字母数字下划线" />
      <label>昵称</label>
      <input v-model="form.nickName" placeholder="可选，默认同用户名" />
      <label>密码</label>
      <input v-model="form.password" type="password" placeholder="登录密码" />
      <button class="btn-primary" :disabled="loading" @click="handleRegister">
        {{ loading ? '注册中...' : '注册并登录' }}
      </button>
      <button class="link-btn" @click="router.push('/login')">已有账号？去登录</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '@/api/user'
import { setToken } from '@/utils/auth'

const router = useRouter()
const loading = ref(false)
const form = ref({
  userName: '',
  nickName: '',
  password: ''
})

async function handleRegister() {
  loading.value = true
  try {
    const data = await register(form.value)
    setToken(data.accessToken)
    router.push('/my')
  } catch (e) {
    alert(e.message || '注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.panel {
  margin: 24px 12px;
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
.link-btn {
  width: 100%;
  margin-top: 12px;
  border: none;
  background: transparent;
  color: #1989fa;
}
</style>

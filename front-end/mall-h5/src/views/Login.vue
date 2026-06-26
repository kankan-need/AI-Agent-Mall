<template>
  <div class="page login">
    <div class="card panel">
      <h2>用户登录</h2>
      <p class="tip">测试账号：user1 / 123456（C端 sysType=0）</p>
      <label>用户名</label>
      <input v-model="form.principal" placeholder="user1" />
      <label>密码</label>
      <input v-model="form.credentials" type="password" placeholder="123456" />
      <button class="btn-primary" :disabled="loading" @click="handleLogin">
        {{ loading ? '登录中...' : '登录' }}
      </button>
      <p v-if="token" class="success">已登录，Token 已保存</p>
      <button v-if="token" class="logout" @click="handleLogout">退出登录</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/api/auth'
import { getToken, removeToken, setToken } from '@/utils/auth'

const router = useRouter()
const loading = ref(false)
const token = ref(getToken())
const form = ref({
  principal: 'user1',
  credentials: '123456',
  sysType: 0
})

async function handleLogin() {
  loading.value = true
  try {
    const data = await login(form.value)
    setToken(data.accessToken)
    token.value = data.accessToken
    router.push('/')
  } catch (e) {
    alert(e.message || '登录失败')
  } finally {
    loading.value = false
  }
}

function handleLogout() {
  removeToken()
  token.value = ''
}
</script>

<style scoped>
.panel {
  margin: 24px 12px;
  padding: 20px;
}
h2 {
  margin: 0 0 8px;
}
.tip {
  color: #969799;
  font-size: 13px;
}
label {
  display: block;
  margin-top: 14px;
  margin-bottom: 6px;
  font-size: 14px;
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
.success {
  color: #07c160;
  margin-top: 12px;
}
.logout {
  margin-top: 12px;
  width: 100%;
  border: 1px solid #ebedf0;
  background: #fff;
  border-radius: 20px;
  padding: 10px;
}
</style>

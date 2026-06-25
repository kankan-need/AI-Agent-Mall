<template>
  <el-card>
    <template #header>服务探活</template>
    <el-button type="primary" :loading="loading" @click="fetchPing">调用 learn-demo</el-button>
    <pre v-if="result" class="result">{{ result }}</pre>
  </el-card>
</template>

<script setup>
import { ref } from 'vue'
import request from '@/utils/request'

const loading = ref(false)
const result = ref('')

async function fetchPing() {
  loading.value = true
  try {
    const data = await request({
      url: '/learn-demo/ping',
      method: 'get'
    })
    result.value = JSON.stringify(data, null, 2)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.result {
  margin-top: 16px;
  background: #f5f7fa;
  padding: 12px;
  border-radius: 6px;
}
</style>

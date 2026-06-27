<template>
  <div class="page address-edit">
    <div class="card panel">
      <h2>{{ isEdit ? '编辑地址' : '新增地址' }}</h2>
      <label>收货人</label>
      <input v-model="form.consignee" />
      <label>手机号</label>
      <input v-model="form.mobile" />
      <label>省</label>
      <input v-model="form.province" />
      <label>市</label>
      <input v-model="form.city" />
      <label>区</label>
      <input v-model="form.area" />
      <label>详细地址</label>
      <input v-model="form.addr" />
      <label class="checkbox">
        <input v-model="isDefault" type="checkbox" /> 设为默认地址
      </label>
      <button class="btn-primary" :disabled="loading" @click="handleSave">
        {{ loading ? '保存中...' : '保存' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAddress, saveAddress, updateAddress } from '@/api/user'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const isDefault = ref(false)
const form = ref({
  addrId: null,
  consignee: '',
  mobile: '',
  province: '',
  city: '',
  area: '',
  addr: '',
  isDefault: 0
})

const isEdit = computed(() => !!route.query.addrId)

async function loadAddress() {
  if (!isEdit.value) return
  const data = await getAddress(route.query.addrId)
  form.value = { ...data }
  isDefault.value = data.isDefault === 1
}

async function handleSave() {
  loading.value = true
  try {
    const payload = {
      ...form.value,
      isDefault: isDefault.value ? 1 : 0
    }
    if (isEdit.value) {
      await updateAddress(payload)
    } else {
      await saveAddress(payload)
    }
    router.push('/address')
  } catch (e) {
    alert(e.message || '保存失败')
  } finally {
    loading.value = false
  }
}

onMounted(loadAddress)
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
  margin-top: 10px;
  margin-bottom: 6px;
}
input[type='text'], input:not([type]) {
  width: 100%;
  border: 1px solid #ebedf0;
  border-radius: 8px;
  padding: 10px 12px;
}
.checkbox {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 12px;
}
.btn-primary {
  width: 100%;
  margin-top: 20px;
}
</style>

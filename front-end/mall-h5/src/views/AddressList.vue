<template>
  <div class="page address">
    <div class="toolbar">
      <h2>收货地址</h2>
      <button class="btn-primary small" @click="goEdit()">新增</button>
    </div>

    <div v-if="list.length" class="list">
      <div v-for="item in list" :key="item.addrId" class="item card">
        <div class="top">
          <strong>{{ item.consignee }}</strong>
          <span>{{ item.mobile }}</span>
          <span v-if="item.isDefault === 1" class="tag">默认</span>
        </div>
        <div class="addr">{{ item.province }}{{ item.city }}{{ item.area }}{{ item.addr }}</div>
        <div class="actions">
          <button @click="goEdit(item.addrId)">编辑</button>
          <button @click="remove(item)" :disabled="item.isDefault === 1">删除</button>
        </div>
      </div>
    </div>
    <div v-else class="empty card">暂无地址，请新增</div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { deleteAddress, listAddress } from '@/api/user'

const router = useRouter()
const list = ref([])

async function loadList() {
  list.value = await listAddress()
}

function goEdit(addrId) {
  router.push({ path: '/address/edit', query: addrId ? { addrId } : {} })
}

async function remove(item) {
  if (!confirm('确认删除该地址？')) return
  await deleteAddress(item.addrId)
  await loadList()
}

onMounted(loadList)
</script>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
}
.small {
  padding: 6px 14px;
  font-size: 13px;
}
.list {
  padding: 0 12px 12px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.item {
  padding: 12px;
}
.top {
  display: flex;
  gap: 10px;
  align-items: center;
}
.tag {
  color: #ee0a24;
  font-size: 12px;
}
.addr {
  margin-top: 8px;
  color: #646566;
  line-height: 1.5;
}
.actions {
  margin-top: 10px;
  display: flex;
  gap: 10px;
}
.actions button {
  border: 1px solid #ebedf0;
  background: #fff;
  border-radius: 4px;
  padding: 4px 12px;
}
.empty {
  margin: 12px;
  padding: 24px;
  text-align: center;
  color: #969799;
}
</style>

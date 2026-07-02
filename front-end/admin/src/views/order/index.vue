<template>
  <el-card>
    <template #header>
      <span>订单列表</span>
    </template>

    <el-form :inline="true" :model="query" class="query-form">
      <el-form-item label="订单状态">
        <el-select v-model="query.status" clearable placeholder="全部">
          <el-option :value="1" label="待付款" />
          <el-option :value="2" label="已付款" />
          <el-option :value="5" label="已完成" />
          <el-option :value="6" label="已关闭" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list" border>
      <el-table-column prop="orderId" label="订单号" width="100" />
      <el-table-column prop="userId" label="用户ID" width="90" />
      <el-table-column prop="shopName" label="店铺" min-width="120" />
      <el-table-column label="金额" width="100">
        <template #default="{ row }">¥{{ formatPrice(row.total) }}</template>
      </el-table-column>
      <el-table-column prop="allCount" label="件数" width="70" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusTagType(row.status)">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" min-width="170">
        <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="goDetail(row.orderId)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="query.pageNum"
      v-model:page-size="query.pageSize"
      class="pager"
      layout="total, prev, pager, next"
      :total="total"
      @current-change="loadData"
    />
  </el-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { pageOrder } from '@/api/order'
import { formatPrice } from '@/utils/price'

const router = useRouter()
const list = ref([])
const total = ref(0)
const query = ref({
  pageNum: 1,
  pageSize: 10,
  status: null
})

function statusText(status) {
  const map = { 1: '待付款', 2: '已付款', 5: '已完成', 6: '已关闭' }
  return map[status] || '未知'
}

function statusTagType(status) {
  if (status === 1) return 'warning'
  if (status === 2 || status === 5) return 'success'
  return 'info'
}

function formatTime(value) {
  if (!value) return ''
  return String(value).replace('T', ' ').slice(0, 19)
}

async function loadData() {
  const data = await pageOrder(query.value)
  list.value = data.list || []
  total.value = data.total || 0
}

function goDetail(orderId) {
  router.push({ path: '/order/detail', query: { orderId } })
}

onMounted(loadData)
</script>

<style scoped>
.query-form {
  margin-bottom: 12px;
}
.pager {
  margin-top: 16px;
  justify-content: flex-end;
}
</style>

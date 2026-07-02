<template>
  <el-card v-loading="loading">
    <template #header>
      <div class="header">
        <span>订单详情</span>
        <el-button @click="router.back()">返回</el-button>
      </div>
    </template>

    <template v-if="order">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ order.orderId }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ order.userId }}</el-descriptions-item>
        <el-descriptions-item label="店铺">{{ order.shopName }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ statusText(order.status) }}</el-descriptions-item>
        <el-descriptions-item label="订单金额">¥{{ formatPrice(order.total) }}</el-descriptions-item>
        <el-descriptions-item label="商品件数">{{ order.allCount }}</el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ formatTime(order.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="支付时间">{{ formatTime(order.payTime) || '-' }}</el-descriptions-item>
        <el-descriptions-item v-if="order.status === 6" label="关闭类型">
          {{ order.closeType === 1 ? '超时关闭' : order.closeType === 4 ? '用户取消' : '-' }}
        </el-descriptions-item>
        <el-descriptions-item v-if="order.status === 6" label="取消时间">
          {{ formatTime(order.cancelTime) || '-' }}
        </el-descriptions-item>
      </el-descriptions>

      <h3 class="block-title">收货信息</h3>
      <el-descriptions v-if="order.orderAddr" :column="1" border>
        <el-descriptions-item label="收货人">
          {{ order.orderAddr.consignee }} {{ order.orderAddr.mobile }}
        </el-descriptions-item>
        <el-descriptions-item label="地址">
          {{ order.orderAddr.province }}{{ order.orderAddr.city }}{{ order.orderAddr.area }}{{ order.orderAddr.addr }}
        </el-descriptions-item>
      </el-descriptions>

      <h3 class="block-title">商品明细</h3>
      <el-table :data="order.orderItems || []" border>
        <el-table-column label="图片" width="90">
          <template #default="{ row }">
            <el-image v-if="row.pic" :src="row.pic" style="width: 48px; height: 48px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="spuName" label="商品名称" min-width="180" />
        <el-table-column label="单价" width="100">
          <template #default="{ row }">¥{{ formatPrice(row.price) }}</template>
        </el-table-column>
        <el-table-column prop="count" label="数量" width="80" />
        <el-table-column label="小计" width="100">
          <template #default="{ row }">¥{{ formatPrice(row.spuTotalAmount) }}</template>
        </el-table-column>
      </el-table>
    </template>
  </el-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getOrder } from '@/api/order'
import { formatPrice } from '@/utils/price'

const route = useRoute()
const router = useRouter()
const order = ref(null)
const loading = ref(false)

function statusText(status) {
  const map = { 1: '待付款', 2: '已付款', 5: '已完成', 6: '已关闭' }
  return map[status] || '未知'
}

function formatTime(value) {
  if (!value) return ''
  return String(value).replace('T', ' ').slice(0, 19)
}

async function loadDetail() {
  loading.value = true
  try {
    order.value = await getOrder(route.query.orderId)
  } finally {
    loading.value = false
  }
}

onMounted(loadDetail)
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.block-title {
  margin: 20px 0 12px;
  font-size: 16px;
}
</style>

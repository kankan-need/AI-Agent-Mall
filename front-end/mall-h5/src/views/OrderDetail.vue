<template>
  <div class="page detail">
    <div v-if="loading" class="empty card">加载中...</div>
    <template v-else-if="order">
      <div class="status card">
        <div class="title">{{ statusText(order.status) }}</div>
        <div class="sub">订单号：{{ order.orderId }}</div>
        <div v-if="order.createTime" class="sub">下单时间：{{ formatTime(order.createTime) }}</div>
      </div>

      <div class="section card" v-if="order.orderAddr">
        <div class="section-title">收货信息</div>
        <div>{{ order.orderAddr.consignee }} {{ order.orderAddr.mobile }}</div>
        <div class="addr">
          {{ order.orderAddr.province }}{{ order.orderAddr.city }}{{ order.orderAddr.area }}{{ order.orderAddr.addr }}
        </div>
      </div>

      <div class="section card">
        <div class="section-title">商品明细</div>
        <div v-for="item in order.orderItems || []" :key="item.orderItemId" class="goods-item">
          <img :src="item.pic" alt="" />
          <div class="info">
            <div class="name">{{ item.spuName }}</div>
            <div class="meta">
              <span class="price">¥{{ formatPrice(item.price) }}</span>
              <span>x{{ item.count }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="section card summary">
        <div><span>商品件数</span><span>{{ order.allCount }}</span></div>
        <div><span>订单金额</span><span class="money">¥{{ formatPrice(order.total) }}</span></div>
      </div>

      <div class="actions" v-if="order.status === 1">
        <button class="btn-primary" @click="goPay">去支付</button>
        <button class="btn-plain" @click="handleCancel">取消订单</button>
      </div>
    </template>
    <div v-else class="empty card">订单不存在</div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { cancelOrder, getOrderDetail } from '@/api/order'
import { formatPrice } from '@/utils/price'

const route = useRoute()
const router = useRouter()
const order = ref(null)
const loading = ref(true)

function statusText(status) {
  const map = { 1: '待付款', 2: '已付款', 5: '已完成', 6: '已关闭' }
  return map[status] || '未知状态'
}

function formatTime(value) {
  if (!value) return ''
  return String(value).replace('T', ' ').slice(0, 19)
}

async function loadDetail() {
  loading.value = true
  try {
    order.value = await getOrderDetail(route.query.orderId)
  } catch {
    order.value = null
  } finally {
    loading.value = false
  }
}

function goPay() {
  router.push({ path: '/order/pay', query: { orderId: order.value.orderId } })
}

async function handleCancel() {
  if (!confirm('确认取消该订单？')) return
  await cancelOrder(order.value.orderId)
  await loadDetail()
}

onMounted(loadDetail)
</script>

<style scoped>
.detail {
  padding: 12px;
  padding-bottom: 80px;
}
.status {
  padding: 16px;
  margin-bottom: 12px;
}
.title {
  font-size: 20px;
  font-weight: 700;
}
.sub {
  margin-top: 6px;
  color: #969799;
  font-size: 12px;
}
.section {
  padding: 12px;
  margin-bottom: 12px;
}
.section-title {
  font-weight: 600;
  margin-bottom: 10px;
}
.addr {
  margin-top: 6px;
  color: #646566;
  line-height: 1.5;
}
.goods-item {
  display: flex;
  gap: 10px;
  padding: 8px 0;
  border-bottom: 1px solid #f5f5f5;
}
.goods-item:last-child {
  border-bottom: none;
}
.goods-item img {
  width: 56px;
  height: 56px;
  object-fit: cover;
  border-radius: 6px;
}
.info { flex: 1; }
.meta {
  margin-top: 6px;
  display: flex;
  justify-content: space-between;
  color: #969799;
}
.price { color: #ee0a24; }
.summary div {
  display: flex;
  justify-content: space-between;
  padding: 6px 0;
}
.money {
  color: #ee0a24;
  font-weight: 600;
}
.actions {
  display: flex;
  gap: 12px;
}
.btn-plain {
  flex: 1;
  border: 1px solid #ebedf0;
  background: #fff;
  border-radius: 20px;
  padding: 10px 20px;
}
.actions .btn-primary {
  flex: 1;
}
.empty {
  margin: 24px 12px;
  padding: 24px;
  text-align: center;
}
</style>

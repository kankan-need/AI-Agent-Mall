<template>
  <div class="page pay">
    <div v-if="loading" class="empty card">加载中...</div>
    <template v-else-if="payInfo">
      <div class="amount card">
        <div class="label">待支付金额</div>
        <div class="money">¥{{ formatPrice(payInfo.total) }}</div>
        <div class="sub">订单号：{{ payInfo.orderId }}</div>
        <div v-if="payInfo.expireTime" class="sub">请在 {{ formatTime(payInfo.expireTime) }} 前完成支付</div>
      </div>

      <div class="section card">
        <div class="section-title">收货信息</div>
        <div v-if="payInfo.orderAddr">
          <div>{{ payInfo.orderAddr.consignee }} {{ payInfo.orderAddr.mobile }}</div>
          <div class="addr">
            {{ payInfo.orderAddr.province }}{{ payInfo.orderAddr.city }}{{ payInfo.orderAddr.area }}{{ payInfo.orderAddr.addr }}
          </div>
        </div>
      </div>

      <div class="section card">
        <div class="section-title">商品明细</div>
        <div v-for="item in payInfo.orderItems || []" :key="item.orderItemId" class="goods-item">
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

      <div class="actions">
        <button class="btn-primary" :disabled="paying" @click="handlePay">
          {{ paying ? '支付中...' : '模拟支付' }}
        </button>
        <button class="btn-cancel" :disabled="paying" @click="handleCancel">取消订单</button>
      </div>
    </template>
    <div v-else class="empty card">订单不存在或已失效</div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { cancelOrder, getPayInfo, payOrder } from '@/api/order'
import { formatPrice } from '@/utils/price'

const route = useRoute()
const router = useRouter()
const payInfo = ref(null)
const loading = ref(true)
const paying = ref(false)

function formatTime(value) {
  if (!value) return ''
  return String(value).replace('T', ' ').slice(0, 19)
}

async function loadPayInfo() {
  loading.value = true
  try {
    payInfo.value = await getPayInfo(route.query.orderId)
  } catch {
    payInfo.value = null
  } finally {
    loading.value = false
  }
}

async function handlePay() {
  if (paying.value) return
  paying.value = true
  try {
    await payOrder(route.query.orderId)
    alert('支付成功')
    router.replace({ path: '/order/detail', query: { orderId: route.query.orderId } })
  } catch (e) {
    alert(e.message || '支付失败')
  } finally {
    paying.value = false
  }
}

async function handleCancel() {
  if (!confirm('确认取消该订单？')) return
  try {
    await cancelOrder(route.query.orderId)
    alert('订单已取消')
    router.replace('/order/list')
  } catch (e) {
    alert(e.message || '取消失败')
  }
}

onMounted(loadPayInfo)
</script>

<style scoped>
.pay {
  padding: 12px;
}
.amount {
  padding: 20px 16px;
  text-align: center;
  margin-bottom: 12px;
}
.label {
  color: #969799;
}
.money {
  font-size: 28px;
  color: #ee0a24;
  font-weight: 700;
  margin: 8px 0;
}
.sub {
  color: #969799;
  font-size: 12px;
  margin-top: 4px;
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
.info {
  flex: 1;
}
.meta {
  margin-top: 6px;
  display: flex;
  justify-content: space-between;
  color: #969799;
}
.price {
  color: #ee0a24;
}
.actions {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 20px;
}
.btn-cancel {
  border: 1px solid #ebedf0;
  background: #fff;
  border-radius: 20px;
  padding: 10px 20px;
}
.empty {
  margin: 24px 12px;
  padding: 24px;
  text-align: center;
}
</style>

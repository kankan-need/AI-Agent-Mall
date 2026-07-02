<template>
  <div class="page confirm">
    <div class="section card">
      <div class="section-title">收货地址</div>
      <div v-if="addresses.length" class="addr-list">
        <label
          v-for="item in addresses"
          :key="item.addrId"
          class="addr-item"
          :class="{ active: selectedAddrId === item.addrId }"
        >
          <input v-model="selectedAddrId" type="radio" :value="item.addrId" />
          <div>
            <div class="top">
              <strong>{{ item.consignee }}</strong>
              <span>{{ item.mobile }}</span>
              <span v-if="item.isDefault === 1" class="tag">默认</span>
            </div>
            <div class="addr">{{ item.province }}{{ item.city }}{{ item.area }}{{ item.addr }}</div>
          </div>
        </label>
      </div>
      <div v-else class="empty-tip">暂无地址，请先新增</div>
      <button class="link-btn" @click="router.push('/address')">管理地址</button>
    </div>

    <div class="section card">
      <div class="section-title">商品清单</div>
      <div v-for="item in checkedItems" :key="item.cartItemId" class="goods-item">
        <img :src="item.mainImgUrl" alt="" />
        <div class="info">
          <div class="name">{{ item.spuName }}</div>
          <div class="meta">
            <span class="price">¥{{ formatPrice(item.priceFee) }}</span>
            <span>x{{ item.count }}</span>
          </div>
        </div>
      </div>
      <div v-if="!checkedItems.length" class="empty-tip">请先在购物车勾选商品</div>
    </div>

    <div class="footer">
      <div>合计：<span class="money">¥{{ formatPrice(totalMoney) }}</span></div>
      <button class="btn-primary" :disabled="submitting || !canSubmit" @click="handleSubmit">
        {{ submitting ? '提交中...' : '提交订单' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getCartInfo } from '@/api/cart'
import { submitOrder } from '@/api/order'
import { listAddress } from '@/api/user'
import { formatPrice } from '@/utils/price'

const router = useRouter()
const addresses = ref([])
const checkedItems = ref([])
const totalMoney = ref(0)
const selectedAddrId = ref(null)
const submitting = ref(false)

const canSubmit = computed(() => checkedItems.value.length > 0 && selectedAddrId.value)

async function loadData() {
  const [addrList, cart] = await Promise.all([listAddress(), getCartInfo()])
  addresses.value = addrList || []
  const items = (cart.items || []).filter(item => item.isChecked === 1)
  checkedItems.value = items
  totalMoney.value = cart.totalMoney || 0

  const defaultAddr = addresses.value.find(item => item.isDefault === 1)
  selectedAddrId.value = defaultAddr?.addrId || addresses.value[0]?.addrId || null
}

async function handleSubmit() {
  if (!canSubmit.value || submitting.value) return
  submitting.value = true
  try {
    const orderId = await submitOrder({ addrId: selectedAddrId.value })
    router.replace({ path: '/order/pay', query: { orderId } })
  } catch (e) {
    alert(e.message || '下单失败')
  } finally {
    submitting.value = false
  }
}

onMounted(loadData)
</script>

<style scoped>
.confirm {
  padding: 12px;
  padding-bottom: 80px;
}
.section {
  padding: 12px;
  margin-bottom: 12px;
}
.section-title {
  font-weight: 600;
  margin-bottom: 10px;
}
.addr-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.addr-item {
  display: flex;
  gap: 10px;
  padding: 10px;
  border: 1px solid #ebedf0;
  border-radius: 8px;
}
.addr-item.active {
  border-color: #1989fa;
  background: #f0f9ff;
}
.top {
  display: flex;
  gap: 8px;
  align-items: center;
}
.tag {
  color: #ee0a24;
  font-size: 12px;
}
.addr {
  margin-top: 6px;
  color: #646566;
  line-height: 1.5;
  font-size: 13px;
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
  width: 64px;
  height: 64px;
  object-fit: cover;
  border-radius: 6px;
}
.info {
  flex: 1;
}
.name {
  font-size: 14px;
}
.meta {
  margin-top: 8px;
  display: flex;
  justify-content: space-between;
  color: #969799;
}
.price {
  color: #ee0a24;
}
.empty-tip {
  color: #969799;
  padding: 8px 0;
}
.link-btn {
  margin-top: 10px;
  border: none;
  background: transparent;
  color: #1989fa;
}
.footer {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: #fff;
  border-top: 1px solid #ebedf0;
}
.money {
  color: #ee0a24;
  font-weight: 600;
}
.btn-primary:disabled {
  opacity: 0.5;
}
</style>

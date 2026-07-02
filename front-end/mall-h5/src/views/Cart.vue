<template>
  <div class="page cart" :class="{ 'has-footer': hasFooter }">
    <div v-if="!loggedIn" class="empty card">
      <p>请先登录后查看购物车</p>
      <button class="btn-primary" @click="router.push('/login')">去登录</button>
    </div>

    <template v-else>
      <div v-if="items.length" class="list">
        <div v-for="item in items" :key="item.cartItemId" class="item card">
          <input
            type="checkbox"
            :checked="item.isChecked === 1"
            @change="toggleCheck(item)"
          />
          <img :src="item.mainImgUrl" alt="" />
          <div class="info">
            <div class="name">{{ item.spuName }}</div>
            <div class="price">¥{{ formatPrice(item.priceFee) }}</div>
            <div class="count-row">
              <button @click="changeCount(item, -1)">-</button>
              <span>{{ item.count }}</span>
              <button @click="changeCount(item, 1)">+</button>
            </div>
          </div>
          <button class="delete" @click="removeItem(item)">删</button>
        </div>
      </div>
      <div v-else class="empty card">购物车是空的</div>

      <div class="footer" v-if="items.length">
        <div>合计：¥{{ formatPrice(totalMoney) }}</div>
        <button class="btn-primary checkout" :disabled="!hasChecked" @click="goCheckout">去结算</button>
      </div>
    </template>

    <section class="recommend">
      <div class="recommend-title">猜你喜欢</div>
      <div v-if="recommendLoading" class="recommend-empty">加载中...</div>
      <div v-else-if="recommendList.length" class="recommend-grid">
        <div
          v-for="item in recommendList"
          :key="item.spuId"
          class="recommend-item card"
          @click="goDetail(item.spuId)"
        >
          <img :src="item.mainImgUrl" alt="" />
          <div class="info">
            <div class="name">{{ item.name }}</div>
            <div class="price">¥{{ formatPrice(item.priceFee) }}</div>
          </div>
        </div>
      </div>
      <div v-else class="recommend-empty">暂无推荐商品</div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { checkCartItems, changeCartItem, deleteCartItems, getCartInfo } from '@/api/cart'
import { pageSpu } from '@/api/product'
import { getToken } from '@/utils/auth'
import { formatPrice } from '@/utils/price'

const router = useRouter()
const items = ref([])
const totalMoney = ref(0)
const recommendList = ref([])
const recommendLoading = ref(false)
const loggedIn = computed(() => !!getToken())
const hasChecked = computed(() => items.value.some(item => item.isChecked === 1))
const hasFooter = computed(() => loggedIn.value && items.value.length > 0)

function shuffle(list) {
  const arr = [...list]
  for (let i = arr.length - 1; i > 0; i -= 1) {
    const j = Math.floor(Math.random() * (i + 1));
    [arr[i], arr[j]] = [arr[j], arr[i]]
  }
  return arr
}

async function loadRecommend() {
  recommendLoading.value = true
  try {
    const data = await pageSpu({ pageNum: 1, pageSize: 50 })
    const cartSpuIds = new Set(items.value.map(item => item.spuId))
    const candidates = (data.list || []).filter(item => !cartSpuIds.has(item.spuId))
    recommendList.value = shuffle(candidates).slice(0, 4)
  } catch {
    recommendList.value = []
  } finally {
    recommendLoading.value = false
  }
}

async function loadCart() {
  if (!getToken()) {
    items.value = []
    totalMoney.value = 0
    return
  }
  const data = await getCartInfo()
  items.value = data.items || []
  totalMoney.value = data.totalMoney || 0
}

function goDetail(spuId) {
  router.push({ path: '/detail', query: { spuId } })
}

async function changeCount(item, delta) {
  await changeCartItem({
    spuId: item.spuId,
    skuId: item.skuId,
    count: delta
  })
  await loadCart()
  await loadRecommend()
}

async function removeItem(item) {
  await deleteCartItems([item.cartItemId])
  await loadCart()
  await loadRecommend()
}

async function toggleCheck(item) {
  const isChecked = item.isChecked === 1 ? 0 : 1
  await checkCartItems([{ cartItemId: item.cartItemId, isChecked }])
  await loadCart()
}

function goCheckout() {
  if (!hasChecked.value) {
    alert('请先勾选要结算的商品')
    return
  }
  router.push('/order/confirm')
}

onMounted(async () => {
  await loadCart()
  await loadRecommend()
})

watch(loggedIn, async () => {
  await loadCart()
  await loadRecommend()
})
</script>

<style scoped>
.list {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
}
.item img {
  width: 72px;
  height: 72px;
  object-fit: cover;
  border-radius: 6px;
}
.info {
  flex: 1;
}
.name {
  font-size: 14px;
}
.price {
  color: #ee0a24;
  margin-top: 4px;
}
.count-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 8px;
}
.count-row button {
  width: 28px;
  height: 28px;
  border: 1px solid #ebedf0;
  background: #fff;
  border-radius: 4px;
}
.delete {
  border: none;
  background: transparent;
  color: #969799;
}
.empty {
  margin: 24px 12px;
  padding: 24px;
  text-align: center;
}
.footer {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 56px;
  background: #fff;
  border-top: 1px solid #ebedf0;
  padding: 12px 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
}
.checkout {
  padding: 8px 18px;
  font-size: 14px;
}
.checkout:disabled {
  opacity: 0.5;
}
.cart.has-footer {
  padding-bottom: 120px;
}
.recommend {
  padding: 0 12px 12px;
}
.recommend-title {
  font-size: 16px;
  font-weight: 600;
  padding: 8px 0 12px;
}
.recommend-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}
.recommend-item img {
  width: 100%;
  height: 120px;
  object-fit: cover;
  display: block;
}
.recommend-item .info {
  padding: 8px;
}
.recommend-item .name {
  font-size: 13px;
  line-height: 1.4;
  height: 36px;
  overflow: hidden;
}
.recommend-item .price {
  color: #ee0a24;
  font-weight: 600;
  margin-top: 4px;
  font-size: 13px;
}
.recommend-empty {
  text-align: center;
  color: #969799;
  padding: 16px 0 24px;
}
</style>

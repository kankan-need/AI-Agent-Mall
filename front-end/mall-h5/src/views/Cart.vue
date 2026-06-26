<template>
  <div class="page cart">
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
      </div>
    </template>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { checkCartItems, changeCartItem, deleteCartItems, getCartInfo } from '@/api/cart'
import { getToken } from '@/utils/auth'
import { formatPrice } from '@/utils/price'

const router = useRouter()
const items = ref([])
const totalMoney = ref(0)
const loggedIn = computed(() => !!getToken())

async function loadCart() {
  if (!getToken()) return
  const data = await getCartInfo()
  items.value = data.items || []
  totalMoney.value = data.totalMoney || 0
}

async function changeCount(item, delta) {
  await changeCartItem({
    spuId: item.spuId,
    skuId: item.skuId,
    count: delta
  })
  await loadCart()
}

async function removeItem(item) {
  await deleteCartItems([item.cartItemId])
  await loadCart()
}

async function toggleCheck(item) {
  const isChecked = item.isChecked === 1 ? 0 : 1
  await checkCartItems([{ cartItemId: item.cartItemId, isChecked }])
  await loadCart()
}

onMounted(loadCart)
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
  font-weight: 600;
}
</style>

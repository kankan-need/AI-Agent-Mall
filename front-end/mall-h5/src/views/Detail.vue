<template>
  <div class="page detail" v-if="spu">
    <img class="cover" :src="spu.mainImgUrl" alt="" />
    <div class="panel card">
      <div class="price">¥{{ formatPrice(spu.priceFee) }}</div>
      <div class="name">{{ spu.name }}</div>
      <div class="point">{{ spu.sellingPoint }}</div>
      <div class="stock">库存 {{ spu.stock }}</div>
    </div>
    <div class="panel card detail-html" v-html="spu.detail"></div>

    <div class="action-bar">
      <button class="btn-primary" @click="addToCart">加入购物车</button>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getSpuDetail } from '@/api/product'
import { changeCartItem } from '@/api/cart'
import { getToken } from '@/utils/auth'
import { formatPrice } from '@/utils/price'

const route = useRoute()
const router = useRouter()
const spu = ref(null)

async function loadDetail() {
  spu.value = await getSpuDetail(route.query.spuId)
}

async function addToCart() {
  if (!getToken()) {
    router.push('/login')
    return
  }
  const sku = spu.value.skus?.[0]
  if (!sku) return
  await changeCartItem({
    spuId: spu.value.spuId,
    skuId: sku.skuId,
    count: 1
  })
  alert('已加入购物车')
}

onMounted(loadDetail)
</script>

<style scoped>
.cover {
  width: 100%;
  height: 320px;
  object-fit: cover;
  display: block;
}
.panel {
  margin: 12px;
  padding: 12px;
}
.price {
  color: #ee0a24;
  font-size: 22px;
  font-weight: 700;
}
.name {
  margin-top: 8px;
  font-size: 16px;
  font-weight: 600;
}
.point, .stock {
  margin-top: 6px;
  color: #969799;
  font-size: 13px;
}
.detail-html {
  min-height: 120px;
}
.action-bar {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 10px 16px;
  background: #fff;
  border-top: 1px solid #ebedf0;
}
.action-bar .btn-primary {
  width: 100%;
}
</style>

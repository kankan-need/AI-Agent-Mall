<template>
  <div class="page favorites">
    <div v-if="!loggedIn" class="empty card">
      <p>登录后查看我的收藏</p>
      <button class="btn-primary" @click="router.push('/login')">去登录</button>
    </div>

    <template v-else>
      <div v-if="list.length" class="goods-list">
        <div v-for="item in list" :key="item.spuId" class="goods-item card">
          <div class="main" @click="goDetail(item.spuId)">
            <img :src="item.mainImgUrl" alt="" />
            <div class="info">
              <div class="name">{{ item.name }}</div>
              <div class="price">¥{{ formatPrice(item.priceFee) }}</div>
            </div>
          </div>
          <button class="remove" @click="remove(item.spuId)">取消收藏</button>
        </div>
      </div>
      <div v-else class="empty card">
        <p>还没有收藏商品</p>
        <button class="btn-primary" @click="router.push('/')">去逛逛</button>
      </div>
    </template>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getToken } from '@/utils/auth'
import { formatPrice } from '@/utils/price'
import { getFavorites, removeFavorite } from '@/utils/favorite'

const router = useRouter()
const list = ref([])
const loggedIn = computed(() => !!getToken())

function loadList() {
  list.value = getFavorites()
}

function goDetail(spuId) {
  router.push({ path: '/detail', query: { spuId } })
}

function remove(spuId) {
  if (!confirm('确认取消收藏？')) return
  removeFavorite(spuId)
  loadList()
}

onMounted(loadList)
</script>

<style scoped>
.goods-list {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.goods-item {
  padding: 12px;
}
.main {
  display: flex;
  gap: 12px;
}
.main img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
}
.info {
  flex: 1;
}
.name {
  font-size: 14px;
  line-height: 1.5;
}
.price {
  margin-top: 8px;
  color: #ee0a24;
  font-weight: 600;
}
.remove {
  margin-top: 10px;
  width: 100%;
  border: 1px solid #ebedf0;
  background: #fff;
  border-radius: 16px;
  padding: 8px;
  color: #646566;
  font-size: 13px;
}
.empty {
  margin: 24px 12px;
  padding: 32px 24px;
  text-align: center;
}
.empty p {
  margin-bottom: 16px;
  color: #969799;
}
</style>

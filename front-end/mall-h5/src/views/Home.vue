<template>
  <div class="page home">
    <header class="banner">Learn Mall H5</header>

    <div class="search-bar card">
      <input
        v-model="keyword"
        type="search"
        placeholder="搜索商品名称"
        @keyup.enter="handleSearch"
      />
      <button v-if="keyword" class="clear" @click="clearSearch">清除</button>
      <button class="search-btn" @click="handleSearch">搜索</button>
    </div>

    <div v-if="searching" class="search-tip">搜索「{{ keyword }}」的结果</div>

    <div class="goods-list">
      <div v-for="item in list" :key="item.spuId" class="goods-item card" @click="goDetail(item.spuId)">
        <img :src="item.mainImgUrl" alt="" />
        <div class="info">
          <div class="name">{{ item.name }}</div>
          <div class="price">¥{{ formatPrice(item.priceFee) }}</div>
        </div>
      </div>
    </div>

    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="!list.length" class="empty">{{ searching ? '未找到相关商品' : '暂无商品' }}</div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { pageSpu } from '@/api/product'
import { formatPrice } from '@/utils/price'

const router = useRouter()
const keyword = ref('')
const searching = ref(false)
const list = ref([])
const loading = ref(false)

async function loadGoods() {
  loading.value = true
  try {
    const params = { pageNum: 1, pageSize: 20 }
    if (searching.value && keyword.value.trim()) {
      params.name = keyword.value.trim()
    }
    const data = await pageSpu(params)
    list.value = data.list || []
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  searching.value = !!keyword.value.trim()
  loadGoods()
}

function clearSearch() {
  keyword.value = ''
  searching.value = false
  loadGoods()
}

function goDetail(spuId) {
  router.push({ path: '/detail', query: { spuId } })
}

onMounted(loadGoods)
</script>

<style scoped>
.banner {
  background: linear-gradient(135deg, #1989fa, #4fc3f7);
  color: #fff;
  padding: 20px 16px;
  font-size: 20px;
  font-weight: 600;
}
.search-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 12px;
  padding: 8px 12px;
}
.search-bar input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 14px;
  background: transparent;
}
.clear {
  border: none;
  background: transparent;
  color: #969799;
  font-size: 13px;
}
.search-btn {
  border: none;
  background: #1989fa;
  color: #fff;
  border-radius: 14px;
  padding: 6px 14px;
  font-size: 13px;
}
.search-tip {
  padding: 0 12px 8px;
  color: #646566;
  font-size: 13px;
}
.goods-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
  padding: 0 12px 12px;
}
.goods-item img {
  width: 100%;
  height: 140px;
  object-fit: cover;
  display: block;
}
.goods-item .info {
  padding: 8px;
}
.goods-item .name {
  font-size: 14px;
  line-height: 1.4;
  height: 40px;
  overflow: hidden;
}
.goods-item .price {
  color: #ee0a24;
  font-weight: 600;
  margin-top: 6px;
}
.loading, .empty {
  text-align: center;
  color: #969799;
  padding: 24px;
}
</style>

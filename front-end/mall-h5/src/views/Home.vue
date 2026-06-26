<template>
  <div class="page home">
    <header class="banner">Learn Mall H5</header>

    <div class="categories">
      <button
        v-for="cat in categories"
        :key="cat.categoryId"
        :class="{ active: activeCategoryId === cat.categoryId }"
        @click="selectCategory(cat.categoryId)"
      >
        {{ cat.name }}
      </button>
    </div>

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
    <div v-else-if="!list.length" class="empty">暂无商品</div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { listCategories, pageSpu } from '@/api/product'
import { formatPrice } from '@/utils/price'

const router = useRouter()
const categories = ref([])
const activeCategoryId = ref(0)
const list = ref([])
const loading = ref(false)

async function loadCategories() {
  const top = await listCategories(0)
  categories.value = [{ categoryId: 0, name: '全部' }, ...top]
}

async function loadGoods() {
  loading.value = true
  try {
    const categoryId = activeCategoryId.value || undefined
    const data = await pageSpu({ pageNum: 1, pageSize: 20, categoryId })
    list.value = data.list || []
  } finally {
    loading.value = false
  }
}

function selectCategory(categoryId) {
  activeCategoryId.value = categoryId
  loadGoods()
}

function goDetail(spuId) {
  router.push({ path: '/detail', query: { spuId } })
}

onMounted(async () => {
  await loadCategories()
  await loadGoods()
})
</script>

<style scoped>
.banner {
  background: linear-gradient(135deg, #1989fa, #4fc3f7);
  color: #fff;
  padding: 20px 16px;
  font-size: 20px;
  font-weight: 600;
}
.categories {
  display: flex;
  gap: 8px;
  overflow-x: auto;
  padding: 12px;
  background: #fff;
}
.categories button {
  border: 1px solid #ebedf0;
  background: #fff;
  border-radius: 16px;
  padding: 6px 14px;
  white-space: nowrap;
}
.categories button.active {
  color: #1989fa;
  border-color: #1989fa;
}
.goods-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
  padding: 12px;
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

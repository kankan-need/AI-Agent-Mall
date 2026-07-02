<template>
  <div class="page category-page">
    <div class="layout">
      <aside class="sidebar">
        <button
          v-for="cat in topCategories"
          :key="cat.categoryId"
          :class="{ active: activeTopId === cat.categoryId }"
          @click="selectTop(cat)"
        >
          {{ cat.name }}
        </button>
      </aside>

      <main class="content">
        <div v-if="subCategories.length" class="sub-list">
          <button
            v-for="sub in subCategories"
            :key="sub.categoryId"
            :class="{ active: activeSubId === sub.categoryId }"
            @click="selectSub(sub.categoryId)"
          >
            {{ sub.name }}
          </button>
        </div>

        <div class="goods-list">
          <div
            v-for="item in list"
            :key="item.spuId"
            class="goods-item card"
            @click="goDetail(item.spuId)"
          >
            <img :src="item.mainImgUrl" alt="" />
            <div class="info">
              <div class="name">{{ item.name }}</div>
              <div class="price">¥{{ formatPrice(item.priceFee) }}</div>
            </div>
          </div>
        </div>

        <div v-if="loading" class="status">加载中...</div>
        <div v-else-if="!list.length" class="status">该分类暂无商品</div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { listShopCategories, pageSpu } from '@/api/product'
import { formatPrice } from '@/utils/price'

const router = useRouter()
const categoryTree = ref([])
const activeTopId = ref(null)
const activeSubId = ref(null)
const list = ref([])
const loading = ref(false)

const topCategories = computed(() => categoryTree.value)

const subCategories = computed(() => {
  const top = categoryTree.value.find(item => item.categoryId === activeTopId.value)
  return top?.children || []
})

function buildTree(flatList) {
  const map = {}
  const roots = []
  flatList.forEach(item => {
    map[item.categoryId] = { ...item, children: [] }
  })
  flatList.forEach(item => {
    if (!item.parentId) {
      roots.push(map[item.categoryId])
    } else if (map[item.parentId]) {
      map[item.parentId].children.push(map[item.categoryId])
    }
  })
  const sortFn = (a, b) => (a.seq || 0) - (b.seq || 0)
  roots.sort(sortFn)
  roots.forEach(root => root.children.sort(sortFn))
  return roots
}

async function loadCategories() {
  const data = await listShopCategories()
  categoryTree.value = buildTree(data || [])
  if (categoryTree.value.length) {
    selectTop(categoryTree.value[0])
  }
}

function selectTop(cat) {
  activeTopId.value = cat.categoryId
  if (cat.children?.length) {
    selectSub(cat.children[0].categoryId)
  } else {
    activeSubId.value = cat.categoryId
    loadGoods(cat.categoryId)
  }
}

function selectSub(categoryId) {
  activeSubId.value = categoryId
  loadGoods(categoryId)
}

async function loadGoods(categoryId) {
  loading.value = true
  try {
    const data = await pageSpu({ pageNum: 1, pageSize: 20, categoryId })
    list.value = data.list || []
  } finally {
    loading.value = false
  }
}

function goDetail(spuId) {
  router.push({ path: '/detail', query: { spuId } })
}

onMounted(loadCategories)
</script>

<style scoped>
.category-page {
  padding-bottom: 56px;
}
.layout {
  display: flex;
  min-height: calc(100vh - 56px);
}
.sidebar {
  width: 92px;
  background: #f7f8fa;
  border-right: 1px solid #ebedf0;
  display: flex;
  flex-direction: column;
}
.sidebar button {
  border: none;
  background: transparent;
  padding: 16px 8px;
  font-size: 13px;
  color: #646566;
  text-align: center;
}
.sidebar button.active {
  background: #fff;
  color: #1989fa;
  font-weight: 600;
  border-left: 3px solid #1989fa;
}
.content {
  flex: 1;
  padding: 12px;
  overflow-y: auto;
}
.sub-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 12px;
}
.sub-list button {
  border: 1px solid #ebedf0;
  background: #fff;
  border-radius: 16px;
  padding: 6px 12px;
  font-size: 12px;
  color: #646566;
}
.sub-list button.active {
  color: #1989fa;
  border-color: #1989fa;
  background: #f0f9ff;
}
.goods-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}
.goods-item img {
  width: 100%;
  height: 120px;
  object-fit: cover;
  display: block;
}
.goods-item .info {
  padding: 8px;
}
.goods-item .name {
  font-size: 13px;
  line-height: 1.4;
  height: 36px;
  overflow: hidden;
}
.goods-item .price {
  color: #ee0a24;
  font-weight: 600;
  margin-top: 4px;
  font-size: 13px;
}
.status {
  text-align: center;
  color: #969799;
  padding: 24px 0;
}
</style>

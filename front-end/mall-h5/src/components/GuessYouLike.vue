<template>
  <section class="guess-like">
    <div class="guess-title">猜你喜欢</div>
    <div v-if="loading" class="guess-empty">加载中...</div>
    <div v-else-if="list.length" class="guess-grid">
      <div
        v-for="item in list"
        :key="item.spuId"
        class="guess-item card"
        @click="goDetail(item.spuId)"
      >
        <img :src="item.mainImgUrl" alt="" />
        <div class="info">
          <div class="name">{{ item.name }}</div>
          <div class="price">¥{{ formatPrice(item.priceFee) }}</div>
        </div>
      </div>
    </div>
    <div v-else class="guess-empty">暂无推荐商品</div>
  </section>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { pageSpu } from '@/api/product'
import { formatPrice } from '@/utils/price'

const props = defineProps({
  excludeIds: {
    type: Array,
    default: () => []
  },
  count: {
    type: Number,
    default: 4
  }
})

const router = useRouter()
const list = ref([])
const loading = ref(false)

function shuffle(items) {
  const arr = [...items]
  for (let i = arr.length - 1; i > 0; i -= 1) {
    const j = Math.floor(Math.random() * (i + 1));
    [arr[i], arr[j]] = [arr[j], arr[i]]
  }
  return arr
}

async function loadList() {
  loading.value = true
  try {
    const data = await pageSpu({ pageNum: 1, pageSize: 50 })
    const excludeSet = new Set(props.excludeIds.map(id => Number(id)))
    const candidates = (data.list || []).filter(item => !excludeSet.has(item.spuId))
    list.value = shuffle(candidates).slice(0, props.count)
  } catch {
    list.value = []
  } finally {
    loading.value = false
  }
}

function goDetail(spuId) {
  router.push({ path: '/detail', query: { spuId } })
}

onMounted(loadList)
watch(() => props.excludeIds, loadList, { deep: true })
</script>

<style scoped>
.guess-like {
  padding: 0 12px 12px;
}
.guess-title {
  font-size: 16px;
  font-weight: 600;
  padding: 8px 0 12px;
}
.guess-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}
.guess-item img {
  width: 100%;
  height: 120px;
  object-fit: cover;
  display: block;
}
.guess-item .info {
  padding: 8px;
}
.guess-item .name {
  font-size: 13px;
  line-height: 1.4;
  height: 36px;
  overflow: hidden;
}
.guess-item .price {
  color: #ee0a24;
  font-weight: 600;
  margin-top: 4px;
  font-size: 13px;
}
.guess-empty {
  text-align: center;
  color: #969799;
  padding: 16px 0 24px;
}
</style>

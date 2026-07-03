<template>
  <div class="page home">
    <header class="banner">Learn Mall H5</header>

    <div class="search-bar card">
      <div class="search-field">
        <input
          v-model="keyword"
          type="search"
          placeholder=" "
          @keyup.enter="handleSearch"
        />
        <transition name="hint-fade" mode="out-in">
          <span v-if="!keyword" :key="searchPlaceholder" class="search-hint">{{ searchPlaceholder }}</span>
        </transition>
      </div>
      <button v-if="keyword" class="clear" @click="clearSearch">清除</button>
      <button class="search-btn" @click="handleSearch">搜索</button>
    </div>

    <BannerCarousel :banners="adBanners" />

    <div class="shortcuts card">
      <button class="shortcut" @click="goSignIn">
        <span class="icon sign">签</span>
        <span class="label">每日签到</span>
      </button>
      <button class="shortcut" @click="goCouponCenter">
        <span class="icon coupon">券</span>
        <span class="label">领券中心</span>
      </button>
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
import { onMounted, onUnmounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import BannerCarousel from '@/components/BannerCarousel.vue'
import { pageSpu } from '@/api/product'
import { getToken } from '@/utils/auth'
import { formatPrice } from '@/utils/price'

const router = useRouter()
const keyword = ref('')
const searching = ref(false)
const list = ref([])
const loading = ref(false)
const placeholderPool = ref([])
const searchPlaceholder = ref('搜索商品名称')
let placeholderTimer = null

const adBanners = [
  {
    title: '春季焕新季',
    desc: '精选好物限时优惠',
    bg: 'linear-gradient(135deg, #667eea, #764ba2)',
    link: '/category'
  },
  {
    title: '新人领券',
    desc: '最高立减 50 元',
    bg: 'linear-gradient(135deg, #f093fb, #f5576c)',
    link: '/coupon-center',
    needLogin: true
  },
  {
    imgUrl: 'https://img.yzcdn.cn/vant/apple-1.jpg',
    title: '数码家电',
    link: '/category'
  },
  {
    title: '每日签到',
    desc: '连签 7 天领大奖',
    bg: 'linear-gradient(135deg, #4facfe, #00f2fe)',
    link: '/sign-in',
    needLogin: true
  }
]

async function loadGoods() {
  loading.value = true
  try {
    const params = { pageNum: 1, pageSize: 20 }
    if (searching.value && keyword.value.trim()) {
      params.name = keyword.value.trim()
    }
    const data = await pageSpu(params)
    list.value = data.list || []
    mergePlaceholderNames(list.value.map(item => item.name))
  } finally {
    loading.value = false
  }
}

function mergePlaceholderNames(names) {
  const valid = names.filter(Boolean)
  if (!valid.length) return
  placeholderPool.value = [...new Set([...placeholderPool.value, ...valid])]
  if (searchPlaceholder.value === '搜索商品名称') {
    pickRandomPlaceholder()
  }
}

function pickRandomPlaceholder() {
  const pool = placeholderPool.value
  if (!pool.length) return
  let next = pool[Math.floor(Math.random() * pool.length)]
  if (pool.length > 1) {
    const current = searchPlaceholder.value.replace(/^搜索\s*/, '')
    while (next === current) {
      next = pool[Math.floor(Math.random() * pool.length)]
    }
  }
  searchPlaceholder.value = `搜索 ${next}`
}

async function loadPlaceholderPool() {
  try {
    const pageNum = Math.floor(Math.random() * 3) + 1
    const data = await pageSpu({ pageNum, pageSize: 30 })
    mergePlaceholderNames((data.list || []).map(item => item.name))
  } catch {
    // ignore
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

function goSignIn() {
  if (!getToken()) {
    router.push('/login')
    return
  }
  router.push('/sign-in')
}

function goCouponCenter() {
  if (!getToken()) {
    router.push('/login')
    return
  }
  router.push('/coupon-center')
}

onMounted(() => {
  loadGoods()
  loadPlaceholderPool()
  placeholderTimer = setInterval(pickRandomPlaceholder, 3000)
})

onUnmounted(() => {
  if (placeholderTimer) clearInterval(placeholderTimer)
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
.search-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 12px;
  padding: 8px 12px;
}
.search-field {
  position: relative;
  flex: 1;
  min-width: 0;
}
.search-field input {
  width: 100%;
  border: none;
  outline: none;
  font-size: 14px;
  background: transparent;
}
.search-hint {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  max-width: 100%;
  color: #c8c9cc;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  pointer-events: none;
}
.hint-fade-enter-active,
.hint-fade-leave-active {
  transition: opacity 0.35s ease;
}
.hint-fade-enter-from,
.hint-fade-leave-to {
  opacity: 0;
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
.shortcuts {
  display: flex;
  gap: 12px;
  margin: 12px;
  padding: 14px 16px;
}
.shortcut {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 10px;
  border: none;
  background: transparent;
  padding: 0;
}
.icon {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 16px;
  font-weight: 700;
}
.icon.sign {
  background: linear-gradient(135deg, #ff976a, #ee0a24);
}
.icon.coupon {
  background: linear-gradient(135deg, #7232dd, #1989fa);
}
.label {
  font-size: 14px;
  font-weight: 600;
  color: #323233;
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

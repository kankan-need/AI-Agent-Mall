<template>
  <div class="page detail" v-if="spu">
    <div class="gallery">
      <img class="cover" :src="images[activeImage]" alt="" />
      <div v-if="images.length > 1" class="gallery-dots">
        <span
          v-for="(_, index) in images"
          :key="index"
          :class="{ active: activeImage === index }"
          @click="activeImage = index"
        />
      </div>
    </div>

    <div class="panel card main-info">
      <div class="price-row">
        <span class="price">¥{{ formatPrice(spu.priceFee) }}</span>
        <span v-if="spu.marketPriceFee > spu.priceFee" class="market">
          ¥{{ formatPrice(spu.marketPriceFee) }}
        </span>
      </div>
      <div class="name">{{ spu.name }}</div>
      <div class="point">{{ spu.sellingPoint }}</div>
      <div class="meta">
        <span>销量 {{ spu.saleNum || 0 }}</span>
        <span>库存 {{ spu.stock }}</span>
      </div>
    </div>

    <div class="panel card comments" id="comments">
      <div class="section-head">
        <span>商品评价 ({{ comments.length }})</span>
        <button v-if="comments.length > 3" class="link" @click="showAllComments = !showAllComments">
          {{ showAllComments ? '收起' : '查看全部' }}
        </button>
      </div>

      <div v-if="displayComments.length" class="comment-list">
        <div v-for="item in displayComments" :key="item.id" class="comment-item">
          <div class="comment-top">
            <span class="nick">{{ item.nickName }}</span>
            <span class="score">{{ scoreText(item.score) }}</span>
          </div>
          <div class="comment-content">{{ item.content }}</div>
          <div class="comment-time">{{ item.createTime }}</div>
        </div>
      </div>
      <div v-else class="comment-empty">暂无评价，快来抢沙发吧</div>

      <div class="comment-form">
        <div class="stars">
          <button
            v-for="n in 5"
            :key="n"
            :class="{ active: commentScore >= n }"
            @click="commentScore = n"
          >
            ★
          </button>
        </div>
        <textarea v-model="commentText" rows="3" placeholder="写下你的使用感受..." />
        <button class="btn-primary small" @click="submitComment">发表评价</button>
      </div>
    </div>

    <div class="panel card detail-html">
      <div class="section-head"><span>商品详情</span></div>
      <div v-html="spu.detail || '<p>暂无详情</p>'"></div>
    </div>

    <div class="action-bar">
      <button class="icon-btn" :class="{ active: collected }" @click="handleFavorite">
        <span class="icon">{{ collected ? '♥' : '♡' }}</span>
        <span>收藏</span>
      </button>
      <button class="icon-btn" @click="showShare = true">
        <span class="icon">↗</span>
        <span>分享</span>
      </button>
      <button class="icon-btn" @click="scrollToComments">
        <span class="icon">💬</span>
        <span>评价</span>
      </button>
      <button class="btn-cart" @click="openSheet('cart')">加入购物车</button>
      <button class="btn-buy" @click="openSheet('buy')">立即购买</button>
    </div>

    <SkuActionSheet
      v-model:visible="showSkuSheet"
      :spu="spu"
      :mode="sheetMode"
      @confirm="handleSkuConfirm"
    />

    <ShareSheet
      v-model:visible="showShare"
      :share-url="shareUrl"
      :share-title="spu.name"
    />
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import SkuActionSheet from '@/components/SkuActionSheet.vue'
import ShareSheet from '@/components/ShareSheet.vue'
import { getSpuDetail } from '@/api/product'
import { changeCartItem, checkCartItems, getCartInfo } from '@/api/cart'
import { getToken } from '@/utils/auth'
import { formatPrice } from '@/utils/price'
import { isFavorite, toggleFavorite } from '@/utils/favorite'
import { addComment, getComments, scoreText } from '@/utils/comment'

const route = useRoute()
const router = useRouter()
const spu = ref(null)
const activeImage = ref(0)
const collected = ref(false)
const showSkuSheet = ref(false)
const showShare = ref(false)
const sheetMode = ref('cart')
const comments = ref([])
const showAllComments = ref(false)
const commentText = ref('')
const commentScore = ref(5)

const images = computed(() => {
  if (!spu.value) return []
  const urls = (spu.value.imgUrls || '')
    .split(',')
    .map(item => item.trim())
    .filter(Boolean)
  if (urls.length) return urls
  return spu.value.mainImgUrl ? [spu.value.mainImgUrl] : []
})

const shareUrl = computed(() => window.location.href)

const displayComments = computed(() => {
  if (showAllComments.value) return comments.value
  return comments.value.slice(0, 3)
})

function ensureLogin() {
  if (!getToken()) {
    router.push('/login')
    return false
  }
  return true
}

async function loadDetail() {
  spu.value = await getSpuDetail(route.query.spuId)
  collected.value = isFavorite(spu.value.spuId)
  comments.value = getComments(spu.value.spuId)
}

function openSheet(mode) {
  if (!ensureLogin()) return
  sheetMode.value = mode
  showSkuSheet.value = true
}

function handleFavorite() {
  if (!ensureLogin()) return
  const result = toggleFavorite(spu.value)
  collected.value = result.collected
  alert(result.collected ? '收藏成功' : '已取消收藏')
}

function scrollToComments() {
  document.getElementById('comments')?.scrollIntoView({ behavior: 'smooth' })
}

function submitComment() {
  if (!ensureLogin()) return
  const result = addComment(spu.value.spuId, commentText.value, commentScore.value)
  if (!result.success) {
    alert(result.message)
    return
  }
  commentText.value = ''
  commentScore.value = 5
  comments.value = getComments(spu.value.spuId)
  alert(result.message)
}

async function setCartCount(spuId, skuId, targetCount) {
  const cart = await getCartInfo()
  const exist = (cart.items || []).find(item => item.skuId === skuId)
  if (!exist) {
    await changeCartItem({ spuId, skuId, count: targetCount })
    return
  }
  const delta = targetCount - exist.count
  if (delta !== 0) {
    await changeCartItem({ spuId, skuId, count: delta })
  }
}

async function handleSkuConfirm({ count, mode }) {
  if (!spu.value) return
  const sku = spu.value.skus?.[0]
  if (!sku) {
    alert('商品规格异常')
    showSkuSheet.value = false
    return
  }

  try {
    await setCartCount(spu.value.spuId, sku.skuId, count)

    if (mode === 'buy') {
      const cart = await getCartInfo()
      const checks = (cart.items || []).map(item => ({
        cartItemId: item.cartItemId,
        isChecked: item.skuId === sku.skuId ? 1 : 0
      }))
      if (checks.length) {
        await checkCartItems(checks)
      }
      showSkuSheet.value = false
      router.push('/order/confirm')
      return
    }

    showSkuSheet.value = false
    alert(`已加入购物车 x${count}`)
  } catch (e) {
    alert(e.message || '操作失败')
    showSkuSheet.value = false
  }
}

onMounted(loadDetail)
</script>

<style scoped>
.detail {
  padding-bottom: 64px;
}
.gallery {
  position: relative;
}
.cover {
  width: 100%;
  height: 320px;
  object-fit: cover;
  display: block;
}
.gallery-dots {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 12px;
  display: flex;
  justify-content: center;
  gap: 6px;
}
.gallery-dots span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.5);
  cursor: pointer;
}
.gallery-dots span.active {
  background: #fff;
}
.panel {
  margin: 12px;
  padding: 12px;
}
.price-row {
  display: flex;
  align-items: baseline;
  gap: 8px;
}
.price {
  color: #ee0a24;
  font-size: 24px;
  font-weight: 700;
}
.market {
  color: #969799;
  font-size: 13px;
  text-decoration: line-through;
}
.name {
  margin-top: 10px;
  font-size: 17px;
  font-weight: 600;
  line-height: 1.5;
}
.point {
  margin-top: 8px;
  color: #646566;
  font-size: 13px;
}
.meta {
  margin-top: 10px;
  display: flex;
  gap: 16px;
  color: #969799;
  font-size: 12px;
}
.section-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  margin-bottom: 12px;
}
.link {
  border: none;
  background: transparent;
  color: #1989fa;
  font-size: 13px;
}
.comment-item {
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
}
.comment-item:last-child {
  border-bottom: none;
}
.comment-top {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
}
.nick {
  font-weight: 600;
}
.score {
  color: #ff976a;
  font-size: 12px;
}
.comment-content {
  margin-top: 8px;
  font-size: 14px;
  line-height: 1.6;
}
.comment-time {
  margin-top: 6px;
  font-size: 12px;
  color: #c8c9cc;
}
.comment-empty {
  color: #969799;
  font-size: 13px;
  padding: 8px 0 16px;
}
.comment-form {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f5f5f5;
}
.stars {
  display: flex;
  gap: 4px;
  margin-bottom: 10px;
}
.stars button {
  border: none;
  background: transparent;
  color: #dcdee0;
  font-size: 22px;
  padding: 0;
}
.stars button.active {
  color: #ff976a;
}
.comment-form textarea {
  width: 100%;
  border: 1px solid #ebedf0;
  border-radius: 8px;
  padding: 10px;
  font-size: 14px;
  resize: none;
  box-sizing: border-box;
}
.small {
  margin-top: 10px;
  width: 100%;
  padding: 10px;
}
.detail-html {
  min-height: 120px;
}
.action-bar {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 10px;
  background: #fff;
  border-top: 1px solid #ebedf0;
}
.icon-btn {
  width: 48px;
  border: none;
  background: transparent;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  font-size: 10px;
  color: #646566;
  padding: 0;
}
.icon-btn .icon {
  font-size: 18px;
  line-height: 1;
}
.icon-btn.active {
  color: #ee0a24;
}
.btn-cart, .btn-buy {
  flex: 1;
  border: none;
  border-radius: 20px;
  padding: 11px 0;
  font-size: 14px;
  font-weight: 600;
  color: #fff;
}
.btn-cart {
  background: linear-gradient(135deg, #ff976a, #ff6034);
}
.btn-buy {
  background: linear-gradient(135deg, #ff6034, #ee0a24);
}
</style>

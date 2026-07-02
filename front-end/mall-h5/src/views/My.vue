<template>
  <div class="page my">
  <div v-if="!loggedIn" class="guest-wrap">
      <div class="guest-hero">
        <div class="guest-avatar">?</div>
        <p>登录后享受更多服务</p>
        <button class="btn-primary" @click="router.push('/login')">立即登录</button>
        <button class="link-btn" @click="router.push('/register')">注册新账号</button>
      </div>
    </div>

    <template v-else>
      <div class="header-bg" />
      <div class="profile card" @click="router.push('/profile/edit')">
        <img :src="profile.pic || defaultAvatar" alt="" />
        <div class="info">
          <div class="name">{{ profile.nickName || '未设置昵称' }}</div>
          <div class="sub">点击编辑个人资料</div>
        </div>
        <span class="arrow">›</span>
      </div>

      <div class="order-card card">
        <div class="card-head" @click="goOrders('all')">
          <span class="title">我的订单</span>
          <span class="more">全部订单 ›</span>
        </div>
        <div class="order-grid">
          <button
            v-for="item in orderEntries"
            :key="item.tab"
            class="order-item"
            @click="goOrders(item.tab)"
          >
            <span class="order-icon" :style="{ background: item.bg }">{{ item.icon }}</span>
            <span class="order-label">{{ item.label }}</span>
            <span v-if="orderCounts[item.tab]" class="badge">{{ orderCounts[item.tab] }}</span>
          </button>
        </div>
      </div>

      <div class="service-card card">
        <div class="card-head">
          <span class="title">我的服务</span>
        </div>
        <div class="service-grid">
          <button
            v-for="item in serviceEntries"
            :key="item.path"
            class="service-item"
            @click="router.push(item.path)"
          >
            <span class="service-icon" :style="{ background: item.bg }">{{ item.icon }}</span>
            <span class="service-label">{{ item.label }}</span>
          </button>
        </div>
      </div>

      <button class="logout" @click="handleLogout">退出登录</button>
    </template>

    <GuessYouLike />
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import GuessYouLike from '@/components/GuessYouLike.vue'
import { listOrders } from '@/api/order'
import { getSimpleInfo } from '@/api/user'
import { getToken, removeToken } from '@/utils/auth'

const router = useRouter()
const profile = ref({})
const orderCounts = ref({})
const defaultAvatar = 'https://img.yzcdn.cn/vant/cat.jpeg'
const loggedIn = computed(() => !!getToken())

const orderEntries = [
  { tab: 'unpay', label: '待付款', icon: '付', bg: 'linear-gradient(135deg, #ff976a, #ee0a24)' },
  { tab: 'unship', label: '待发货', icon: '发', bg: 'linear-gradient(135deg, #ffd01e, #ff8917)' },
  { tab: 'receive', label: '待收货', icon: '收', bg: 'linear-gradient(135deg, #07c160, #10b981)' },
  { tab: 'refund', label: '退款/退货', icon: '退', bg: 'linear-gradient(135deg, #7232dd, #9c27b0)' }
]

const serviceEntries = [
  { path: '/favorites', label: '我的收藏', icon: '♥', bg: 'linear-gradient(135deg, #ff6034, #ee0a24)' },
  { path: '/address', label: '收货地址', icon: '址', bg: 'linear-gradient(135deg, #1989fa, #4fc3f7)' },
  { path: '/cart', label: '购物车', icon: '购', bg: 'linear-gradient(135deg, #ff976a, #ffb347)' },
  { path: '/sign-in', label: '每日签到', icon: '签', bg: 'linear-gradient(135deg, #667eea, #764ba2)' },
  { path: '/coupon-center', label: '领券中心', icon: '券', bg: 'linear-gradient(135deg, #f093fb, #f5576c)' }
]

async function loadProfile() {
  if (!loggedIn.value) return
  try {
    profile.value = await getSimpleInfo()
  } catch {
    profile.value = {}
  }
}

async function loadOrderCounts() {
  if (!loggedIn.value) return
  try {
    const data = await listOrders({ pageNum: 1, pageSize: 100 })
    const list = data.list || []
    orderCounts.value = {
      unpay: list.filter(o => o.status === 1).length,
      unship: list.filter(o => o.status === 2).length,
      receive: 0,
      refund: 0
    }
  } catch {
    orderCounts.value = {}
  }
}

function goOrders(tab) {
  router.push({ path: '/order/list', query: { tab } })
}

function handleLogout() {
  removeToken()
  router.push('/login')
}

onMounted(async () => {
  await loadProfile()
  await loadOrderCounts()
})
</script>

<style scoped>
.my {
  min-height: 100vh;
  padding-bottom: 20px;
}
.header-bg {
  height: 120px;
  background: linear-gradient(135deg, #1989fa 0%, #4fc3f7 100%);
}
.profile {
  position: relative;
  margin: -48px 12px 12px;
  padding: 20px 16px;
  display: flex;
  align-items: center;
  gap: 14px;
  box-shadow: 0 4px 16px rgba(25, 137, 250, 0.12);
}
.profile img {
  width: 68px;
  height: 68px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}
.info {
  flex: 1;
}
.name {
  font-size: 20px;
  font-weight: 700;
  color: #323233;
}
.sub {
  margin-top: 6px;
  color: #969799;
  font-size: 13px;
}
.arrow {
  color: #c8c9cc;
  font-size: 22px;
}
.order-card,
.service-card {
  margin: 0 12px 12px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}
.card-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.title {
  font-size: 16px;
  font-weight: 700;
  color: #323233;
}
.more {
  font-size: 13px;
  color: #969799;
}
.order-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 8px;
}
.order-item {
  position: relative;
  border: none;
  background: transparent;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 4px 0;
}
.order-icon {
  width: 44px;
  height: 44px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 16px;
  font-weight: 700;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}
.order-label {
  font-size: 12px;
  color: #646566;
}
.badge {
  position: absolute;
  top: 0;
  right: 8px;
  min-width: 16px;
  height: 16px;
  padding: 0 4px;
  border-radius: 8px;
  background: #ee0a24;
  color: #fff;
  font-size: 10px;
  line-height: 16px;
  text-align: center;
}
.service-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px 8px;
}
.service-item {
  border: none;
  background: transparent;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 0;
}
.service-icon {
  width: 48px;
  height: 48px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: 700;
}
.service-label {
  font-size: 12px;
  color: #646566;
}
.guest-wrap {
  padding: 12px;
}
.guest-hero {
  margin-top: 40px;
  padding: 48px 24px;
  text-align: center;
  background: #fff;
  border-radius: 12px;
}
.guest-avatar {
  width: 72px;
  height: 72px;
  margin: 0 auto 16px;
  border-radius: 50%;
  background: linear-gradient(135deg, #1989fa, #4fc3f7);
  color: #fff;
  font-size: 32px;
  line-height: 72px;
}
.guest-hero p {
  color: #646566;
  margin-bottom: 20px;
}
.link-btn {
  display: block;
  width: 100%;
  margin-top: 12px;
  border: none;
  background: transparent;
  color: #1989fa;
  font-size: 14px;
}
.logout {
  display: block;
  width: calc(100% - 24px);
  margin: 8px 12px 24px;
  border: none;
  background: #fff;
  border-radius: 24px;
  padding: 14px;
  color: #646566;
  font-size: 15px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}
</style>

<template>
  <div class="page coupon-center">
    <div class="tabs">
      <button :class="{ active: activeTab === 'claim' }" @click="activeTab = 'claim'">领券中心</button>
      <button :class="{ active: activeTab === 'mine' }" @click="activeTab = 'mine'">我的优惠券</button>
    </div>

    <div v-if="activeTab === 'claim'" class="list">
      <div v-for="item in coupons" :key="item.couponId" class="coupon card">
        <div class="left">
          <div class="amount">¥{{ formatPrice(item.amount) }}</div>
          <div class="rule">满{{ formatPrice(item.minAmount) }}元可用</div>
        </div>
        <div class="right">
          <div class="name">{{ item.name }}</div>
          <div class="expire">领取后 {{ item.validDays }} 天内有效</div>
          <button
            class="claim-btn"
            :disabled="claimedIds.has(item.couponId)"
            @click="handleClaim(item.couponId)"
          >
            {{ claimedIds.has(item.couponId) ? '已领取' : '立即领取' }}
          </button>
        </div>
      </div>
    </div>

    <div v-else class="list">
      <div v-if="myCoupons.length" class="mine-list">
        <div v-for="item in myCoupons" :key="item.userCouponId" class="coupon card mine">
          <div class="left">
            <div class="amount">¥{{ formatPrice(item.amount) }}</div>
            <div class="rule">满{{ formatPrice(item.minAmount) }}元可用</div>
          </div>
          <div class="right">
            <div class="name">{{ item.name || '优惠券' }}</div>
            <div class="expire">有效期至 {{ formatDate(item.expireTime) }}</div>
            <span class="status">{{ item.status === 'unused' ? '未使用' : '已使用' }}</span>
          </div>
        </div>
      </div>
      <div v-else class="empty">暂无优惠券，去领券中心领取吧</div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getToken } from '@/utils/auth'
import { formatPrice } from '@/utils/price'
import { fetchCoupons, claimCouponApi, fetchMyCoupons } from '@/api/coupon'

const router = useRouter()
const activeTab = ref('claim')
const coupons = ref([])
const myCoupons = ref([])
const claimedIds = ref(new Set())

function ensureLogin() {
  if (!getToken()) {
    router.replace('/login')
    return false
  }
  return true
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}-${m}-${day}`
}

async function loadCoupons() {
  try {
    coupons.value = await fetchCoupons()
  } catch {
    coupons.value = []
  }
}

async function loadMyCoupons() {
  try {
    const list = await fetchMyCoupons()
    // 补充优惠券名称和金额（user_coupon 表只存 coupon_id）
    list.forEach(uc => {
      const c = coupons.value.find(c => c.couponId === uc.couponId)
      if (c) {
        uc.name = c.name
        uc.amount = c.amount
        uc.minAmount = c.minAmount
      }
    })
    myCoupons.value = list
    claimedIds.value = new Set(list.map(item => item.couponId))
  } catch {
    myCoupons.value = []
  }
}

async function handleClaim(couponId) {
  if (!ensureLogin()) return
  try {
    await claimCouponApi(couponId)
    alert('领取成功')
    await loadMyCoupons()
  } catch (e) {
    alert(e.message || '领取失败')
  }
}

onMounted(async () => {
  if (!ensureLogin()) return
  await loadCoupons()
  await loadMyCoupons()
})
</script>

<style scoped>
.coupon-center {
  padding-bottom: 12px;
}
.tabs {
  display: flex;
  background: #fff;
  border-bottom: 1px solid #ebedf0;
}
.tabs button {
  flex: 1;
  border: none;
  background: transparent;
  padding: 12px 0;
  font-size: 14px;
}
.tabs button.active {
  color: #1989fa;
  font-weight: 600;
  border-bottom: 2px solid #1989fa;
}
.list {
  padding: 12px;
}
.coupon {
  display: flex;
  margin-bottom: 12px;
  overflow: hidden;
}
.left {
  width: 110px;
  background: linear-gradient(135deg, #ff6034, #ee0a24);
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 16px 8px;
}
.amount {
  font-size: 24px;
  font-weight: 700;
}
.rule {
  margin-top: 6px;
  font-size: 11px;
  opacity: 0.9;
}
.right {
  flex: 1;
  padding: 14px 12px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 6px;
}
.name {
  font-size: 15px;
  font-weight: 600;
}
.expire {
  font-size: 12px;
  color: #969799;
}
.claim-btn {
  align-self: flex-start;
  margin-top: 4px;
  border: none;
  background: #ee0a24;
  color: #fff;
  border-radius: 14px;
  padding: 6px 14px;
  font-size: 12px;
}
.claim-btn:disabled {
  background: #c8c9cc;
}
.status {
  display: inline-block;
  margin-top: 4px;
  font-size: 12px;
  color: #07c160;
}
.empty {
  text-align: center;
  color: #969799;
  padding: 40px 0;
}
</style>

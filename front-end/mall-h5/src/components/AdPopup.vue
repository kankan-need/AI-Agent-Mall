<template>
  <Teleport to="body">
    <transition name="ad-fade">
      <div v-if="visible" class="ad-overlay" @click.self="close">
        <div class="ad-card">
          <!-- 红包头部 -->
          <div class="ad-header">
            <div class="envelope-icon">
              <span class="envelope-body">&#x1F9E7;</span>
            </div>
            <div class="ad-title">新人专属福利</div>
            <div class="ad-subtitle">注册即享以下优惠券</div>
            <div class="header-decor left-decor" />
            <div class="header-decor right-decor" />
          </div>

          <!-- 优惠券列表 -->
          <div class="ad-body">
            <div
              v-for="(item, idx) in couponList"
              :key="idx"
              class="coupon-item"
              :class="{ gold: idx === 1 }"
            >
              <div class="c-left">
                <span class="c-symbol">&#165;</span>
                <span class="c-amount">{{ formatPrice(item.amount) }}</span>
              </div>
              <div class="c-right">
                <div class="c-name">{{ item.name }}</div>
                <div class="c-cond">满{{ formatPrice(item.minAmount) }}元可用</div>
                <div class="c-expire">领取后 {{ item.validDays }} 天有效</div>
              </div>
            </div>
          </div>

          <!-- 按钮 -->
          <div class="ad-footer">
            <button class="btn-claim" @click="goCouponCenter">立即领取</button>
            <button class="btn-close" @click="close">残忍拒绝</button>
          </div>
        </div>
      </div>
    </transition>
  </Teleport>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { fetchCoupons } from '@/api/coupon'
import { formatPrice } from '@/utils/price'

const router = useRouter()
const STORAGE_KEY = 'learn_mall_ad_seen'
const visible = ref(false)
const couponList = ref([])

async function close() {
  visible.value = false
  localStorage.setItem(STORAGE_KEY, '1')
}

function goCouponCenter() {
  close()
  router.push('/coupon-center')
}

onMounted(async () => {
  if (!localStorage.getItem(STORAGE_KEY)) {
    try {
      const list = await fetchCoupons()
      couponList.value = (list || []).slice(0, 3)
    } catch {
      // 兜底数据
      couponList.value = [
        { name: '新人专享券', amount: 1000, minAmount: 5000, validDays: 7 },
        { name: '满减优惠券', amount: 2000, minAmount: 10000, validDays: 15 },
        { name: '数码家电券', amount: 5000, minAmount: 30000, validDays: 30 }
      ]
    }
    visible.value = true
  }
})
</script>

<style scoped>
.ad-overlay {
  position: fixed;
  inset: 0;
  z-index: 9999;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
}
.ad-card {
  width: 320px;
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.18);
}

/* ===== 红包头部 ===== */
.ad-header {
  position: relative;
  background: linear-gradient(180deg, #e8380d 0%, #d4232a 60%, #b81b22 100%);
  padding: 28px 20px 24px;
  text-align: center;
  color: #fff;
  overflow: hidden;
}
.envelope-icon {
  margin-bottom: 8px;
}
.envelope-body {
  font-size: 44px;
  display: inline-block;
  animation: bounce 0.6s ease-out;
}
@keyframes bounce {
  0% { transform: scale(0.3); opacity: 0; }
  50% { transform: scale(1.15); }
  100% { transform: scale(1); opacity: 1; }
}
.ad-title {
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 2px;
}
.ad-subtitle {
  font-size: 13px;
  opacity: 0.85;
  margin-top: 6px;
}
/* 头部金色装饰圆 */
.header-decor {
  position: absolute;
  top: -18px;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: rgba(255, 215, 0, 0.3);
}
.left-decor {
  left: -18px;
}
.right-decor {
  right: -18px;
}

/* ===== 优惠券列表 ===== */
.ad-body {
  padding: 16px;
  background: #f7f8fa;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* 单张优惠券 —— 仿红包锯齿设计 */
.coupon-item {
  display: flex;
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}
/* 锯齿半圆 */
.coupon-item::before,
.coupon-item::after {
  content: '';
  position: absolute;
  left: 88px;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: #f7f8fa;
  z-index: 2;
}
.coupon-item::before {
  top: -8px;
  box-shadow: 0 -1px 2px rgba(0, 0, 0, 0.06) inset;
}
.coupon-item::after {
  bottom: -8px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.06) inset;
}

/* 左侧价格区 */
.c-left {
  width: 90px;
  background: linear-gradient(160deg, #ff6034, #ee0a24);
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 14px 6px;
  flex-shrink: 0;
}
.coupon-item.gold .c-left {
  background: linear-gradient(160deg, #e6a23c, #cf7d1f);
}
.c-symbol {
  font-size: 14px;
  font-weight: 600;
}
.c-amount {
  font-size: 28px;
  font-weight: 700;
  line-height: 1.1;
}

/* 右侧信息区 */
.c-right {
  flex: 1;
  background: #fff;
  padding: 12px 14px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 4px;
  border-left: 2px dashed #ee0a24;
}
.coupon-item.gold .c-right {
  border-left-color: #cf7d1f;
}
.c-name {
  font-size: 14px;
  font-weight: 600;
  color: #323233;
}
.c-cond {
  font-size: 12px;
  color: #969799;
}
.c-expire {
  font-size: 11px;
  color: #c8c9cc;
}

/* ===== 底部按钮 ===== */
.ad-footer {
  padding: 16px;
  display: flex;
  gap: 12px;
}
.btn-claim {
  flex: 1;
  border: none;
  background: linear-gradient(135deg, #ee0a24, #d4232a);
  color: #fff;
  border-radius: 22px;
  padding: 11px 0;
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 1px;
  box-shadow: 0 4px 10px rgba(238, 10, 36, 0.3);
}
.btn-close {
  flex: 1;
  border: 1px solid #dcdfe6;
  background: #fff;
  color: #909399;
  border-radius: 22px;
  padding: 11px 0;
  font-size: 14px;
}

/* ===== 动画 ===== */
.ad-fade-enter-active {
  transition: opacity 0.3s ease;
}
.ad-fade-enter-active .ad-card {
  transition: transform 0.35s cubic-bezier(0.34, 1.56, 0.64, 1);
}
.ad-fade-leave-active {
  transition: opacity 0.2s ease;
}
.ad-fade-enter-from {
  opacity: 0;
}
.ad-fade-enter-from .ad-card {
  transform: scale(0.7);
}
.ad-fade-leave-to {
  opacity: 0;
}
</style>

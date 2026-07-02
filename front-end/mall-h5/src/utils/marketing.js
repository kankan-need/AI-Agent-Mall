import { getToken } from './auth'

const SIGN_REWARDS = [10, 20, 30, 40, 50, 60, 100]

export const COUPON_LIST = [
  {
    id: 1,
    name: '新人专享券',
    amount: 1000,
    minAmount: 5000,
    desc: '满50元可用',
    expireDays: 7
  },
  {
    id: 2,
    name: '满减优惠券',
    amount: 2000,
    minAmount: 10000,
    desc: '满100元可用',
    expireDays: 15
  },
  {
    id: 3,
    name: '数码家电券',
    amount: 5000,
    minAmount: 30000,
    desc: '满300元可用',
    expireDays: 30
  },
  {
    id: 4,
    name: '服饰专享券',
    amount: 1500,
    minAmount: 8000,
    desc: '满80元可用',
    expireDays: 15
  }
]

function userKey(prefix) {
  const token = getToken()
  return token ? `${prefix}_${token.slice(-20)}` : `${prefix}_guest`
}

function todayStr() {
  const now = new Date()
  const y = now.getFullYear()
  const m = String(now.getMonth() + 1).padStart(2, '0')
  const d = String(now.getDate()).padStart(2, '0')
  return `${y}-${m}-${d}`
}

function yesterdayStr() {
  const date = new Date()
  date.setDate(date.getDate() - 1)
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  return `${y}-${m}-${d}`
}

function readJson(key, fallback) {
  try {
    const raw = localStorage.getItem(key)
    return raw ? JSON.parse(raw) : fallback
  } catch {
    return fallback
  }
}

function writeJson(key, value) {
  localStorage.setItem(key, JSON.stringify(value))
}

export function getSignInfo() {
  const data = readJson(userKey('learn_sign'), {
    lastSignDate: '',
    consecutiveDays: 0,
    totalPoints: 0
  })
  const today = todayStr()
  return {
    ...data,
    todaySigned: data.lastSignDate === today,
    todayReward: SIGN_REWARDS[Math.min(Math.max(data.consecutiveDays, 1), 7) - 1]
  }
}

export function signToday() {
  const key = userKey('learn_sign')
  const data = readJson(key, {
    lastSignDate: '',
    consecutiveDays: 0,
    totalPoints: 0
  })
  const today = todayStr()
  if (data.lastSignDate === today) {
    return { success: false, message: '今日已签到' }
  }

  let consecutiveDays = 1
  if (data.lastSignDate === yesterdayStr()) {
    consecutiveDays = Math.min((data.consecutiveDays || 0) + 1, 7)
  }

  const reward = SIGN_REWARDS[consecutiveDays - 1]
  const next = {
    lastSignDate: today,
    consecutiveDays,
    totalPoints: (data.totalPoints || 0) + reward
  }
  writeJson(key, next)
  return { success: true, reward, ...next }
}

export function getSignRewards() {
  return SIGN_REWARDS.map((points, index) => ({
    day: index + 1,
    points
  }))
}

export function getMyCoupons() {
  return readJson(userKey('learn_coupons'), [])
}

export function claimCoupon(couponId) {
  const coupon = COUPON_LIST.find(item => item.id === couponId)
  if (!coupon) {
    return { success: false, message: '优惠券不存在' }
  }

  const key = userKey('learn_coupons')
  const list = readJson(key, [])
  if (list.some(item => item.couponId === couponId)) {
    return { success: false, message: '已领取过该优惠券' }
  }

  const claimDate = todayStr()
  const expireDate = new Date()
  expireDate.setDate(expireDate.getDate() + coupon.expireDays)
  const expireStr = `${expireDate.getFullYear()}-${String(expireDate.getMonth() + 1).padStart(2, '0')}-${String(expireDate.getDate()).padStart(2, '0')}`

  list.unshift({
    couponId: coupon.id,
    name: coupon.name,
    amount: coupon.amount,
    minAmount: coupon.minAmount,
    desc: coupon.desc,
    claimDate,
    expireDate: expireStr,
    status: 'unused'
  })
  writeJson(key, list)
  return { success: true, message: '领取成功' }
}

export function isCouponClaimed(couponId) {
  return getMyCoupons().some(item => item.couponId === couponId)
}

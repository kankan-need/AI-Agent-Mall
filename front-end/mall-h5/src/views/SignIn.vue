<template>
  <div class="page sign-in">
    <div class="hero card">
      <div class="title">每日签到</div>
      <div class="points">累计积分 <strong>{{ signInfo.totalPoints }}</strong></div>
      <div class="sub">已连续签到 {{ signInfo.consecutiveDays }} 天</div>
    </div>

    <div class="reward-strip card">
      <div
        v-for="item in rewards"
        :key="item.day"
        class="reward-item"
        :class="{ done: signInfo.consecutiveDays >= item.day, today: !signInfo.todaySigned && signInfo.consecutiveDays + 1 === item.day }"
      >
        <div class="day">第{{ item.day }}天</div>
        <div class="value">+{{ item.points }}</div>
      </div>
    </div>

    <button class="btn-primary sign-btn" :disabled="signInfo.todaySigned || signing" @click="handleSign">
      {{ signInfo.todaySigned ? '今日已签到' : signing ? '签到中...' : `签到领 ${nextReward} 积分` }}
    </button>

    <div class="tips card">
      <p>· 每日签到可获得积分奖励</p>
      <p>· 连续签到 7 天奖励最高，断签后重新计算</p>
      <p>· 积分可用于后续营销活动（学习版演示）</p>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getToken } from '@/utils/auth'
import { getSignInfo, getSignRewards, signToday } from '@/utils/marketing'

const router = useRouter()
const signInfo = ref(getSignInfo())
const rewards = getSignRewards()
const signing = ref(false)

const nextReward = computed(() => {
  const day = signInfo.value.todaySigned
    ? signInfo.value.consecutiveDays
    : Math.min(signInfo.value.consecutiveDays + 1, 7)
  return rewards[day - 1]?.points || 10
})

function ensureLogin() {
  if (!getToken()) {
    router.replace('/login')
    return false
  }
  return true
}

function refresh() {
  signInfo.value = getSignInfo()
}

async function handleSign() {
  if (!ensureLogin() || signInfo.value.todaySigned || signing.value) return
  signing.value = true
  try {
    const result = signToday()
    if (result.success) {
      alert(`签到成功，获得 ${result.reward} 积分`)
    } else {
      alert(result.message)
    }
    refresh()
  } finally {
    signing.value = false
  }
}

onMounted(() => {
  if (!ensureLogin()) return
  refresh()
})
</script>

<style scoped>
.sign-in {
  padding: 12px;
}
.hero {
  padding: 24px 16px;
  text-align: center;
  background: linear-gradient(135deg, #ff976a, #ee0a24);
  color: #fff;
  margin-bottom: 12px;
}
.title {
  font-size: 22px;
  font-weight: 700;
}
.points {
  margin-top: 12px;
  font-size: 14px;
}
.points strong {
  font-size: 28px;
  margin-left: 6px;
}
.sub {
  margin-top: 8px;
  font-size: 13px;
  opacity: 0.9;
}
.reward-strip {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 6px;
  padding: 12px;
  margin-bottom: 16px;
}
.reward-item {
  text-align: center;
  padding: 8px 4px;
  border-radius: 8px;
  background: #f7f8fa;
  font-size: 11px;
}
.reward-item.done {
  background: #fff1f0;
  color: #ee0a24;
}
.reward-item.today {
  border: 1px solid #ee0a24;
  background: #fff;
}
.value {
  margin-top: 4px;
  font-weight: 600;
}
.sign-btn {
  display: block;
  width: 100%;
  margin-bottom: 12px;
}
.sign-btn:disabled {
  opacity: 0.6;
}
.tips {
  padding: 16px;
  color: #646566;
  font-size: 13px;
  line-height: 1.8;
}
.tips p {
  margin: 0;
}
</style>

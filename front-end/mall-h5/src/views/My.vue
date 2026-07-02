<template>
  <div class="page my">
    <div v-if="!loggedIn" class="card guest">
      <p>登录后查看个人中心</p>
      <button class="btn-primary" @click="router.push('/login')">去登录</button>
      <button class="link-btn" @click="router.push('/register')">注册新账号</button>
    </div>

    <template v-else>
      <div class="profile card" @click="router.push('/profile/edit')">
        <img :src="profile.pic || defaultAvatar" alt="" />
        <div class="info">
          <div class="name">{{ profile.nickName || '未设置昵称' }}</div>
          <div class="sub">点击编辑资料</div>
        </div>
      </div>

      <div class="menu card">
        <div class="menu-item" @click="router.push('/favorites')">
          <span>我的收藏</span>
          <span class="arrow">›</span>
        </div>
        <div class="menu-item" @click="router.push('/address')">
          <span>收货地址</span>
          <span class="arrow">›</span>
        </div>
        <div class="menu-item" @click="router.push('/order/list')">
          <span>我的订单</span>
          <span class="arrow">›</span>
        </div>
        <div class="menu-item" @click="router.push('/cart')">
          <span>我的购物车</span>
          <span class="arrow">›</span>
        </div>
      </div>

      <button class="logout" @click="handleLogout">退出登录</button>
    </template>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getSimpleInfo } from '@/api/user'
import { getToken, removeToken } from '@/utils/auth'

const router = useRouter()
const profile = ref({})
const defaultAvatar = 'https://img.yzcdn.cn/vant/cat.jpeg'
const loggedIn = computed(() => !!getToken())

async function loadProfile() {
  if (!loggedIn.value) return
  try {
    profile.value = await getSimpleInfo()
  } catch {
    profile.value = {}
  }
}

function handleLogout() {
  removeToken()
  router.push('/login')
}

onMounted(loadProfile)
</script>

<style scoped>
.guest, .profile, .menu {
  margin: 12px;
  padding: 16px;
}
.guest {
  text-align: center;
}
.link-btn {
  margin-top: 12px;
  border: none;
  background: transparent;
  color: #1989fa;
}
.profile {
  display: flex;
  align-items: center;
  gap: 12px;
}
.profile img {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  object-fit: cover;
}
.name {
  font-size: 18px;
  font-weight: 600;
}
.sub {
  color: #969799;
  font-size: 13px;
  margin-top: 4px;
}
.menu-item {
  display: flex;
  justify-content: space-between;
  padding: 14px 0;
  border-bottom: 1px solid #f5f5f5;
}
.menu-item:last-child {
  border-bottom: none;
}
.arrow {
  color: #c8c9cc;
}
.logout {
  display: block;
  width: calc(100% - 24px);
  margin: 24px 12px;
  border: 1px solid #ebedf0;
  background: #fff;
  border-radius: 20px;
  padding: 12px;
}
</style>

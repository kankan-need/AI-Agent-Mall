<template>
  <div class="app">
    <router-view />
    <nav class="bottom-bar" v-if="showTabBar">
      <router-link to="/" :class="{ active: route.path === '/' }">首页</router-link>
      <router-link to="/cart" :class="{ active: route.path === '/cart' }">
        购物车{{ cartCount > 0 ? `(${cartCount})` : '' }}
      </router-link>
      <router-link to="/login" :class="{ active: route.path === '/login' }">我的</router-link>
    </nav>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getToken } from '@/utils/auth'
import { getCartCount } from '@/api/cart'

const route = useRoute()
const cartCount = ref(0)

const showTabBar = computed(() => !['/detail'].includes(route.path))

async function refreshCartCount() {
  if (!getToken()) {
    cartCount.value = 0
    return
  }
  try {
    cartCount.value = await getCartCount()
  } catch {
    cartCount.value = 0
  }
}

watch(() => route.path, refreshCartCount)
onMounted(refreshCartCount)

defineExpose({ refreshCartCount })
</script>

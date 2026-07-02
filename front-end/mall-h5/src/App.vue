<template>
  <div class="app">
    <router-view />
    <TabBar v-if="showTabBar" :cart-count="cartCount" />
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import TabBar from '@/components/TabBar.vue'
import { getToken } from '@/utils/auth'
import { getCartCount } from '@/api/cart'

const route = useRoute()
const cartCount = ref(0)

const showTabBar = computed(() => ![
  '/detail',
  '/profile/edit',
  '/address/edit',
  '/register',
  '/order/confirm',
  '/order/pay',
  '/order/detail',
  '/sign-in',
  '/coupon-center',
  '/favorites'
].includes(route.path))

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

<template>
  <nav class="tab-bar">
    <router-link
      v-for="tab in tabs"
      :key="tab.path"
      :to="tab.path"
      class="tab-item"
      :class="{ active: isActive(tab) }"
    >
      <span class="tab-icon-wrap" :class="{ 'is-active': isActive(tab) }">
        <svg class="tab-icon" viewBox="0 0 24 24" aria-hidden="true">
          <path v-if="tab.icon === 'home'" d="M3 10.2 12 4l9 6.2V19a1 1 0 0 1-1 1h-5v-6H9v6H4a1 1 0 0 1-1-1v-8.8Z" />
          <path
            v-else-if="tab.icon === 'category'"
            d="M4.5 5.5h5a1 1 0 0 1 1 1v5a1 1 0 0 1-1 1h-5a1 1 0 0 1-1-1v-5a1 1 0 0 1 1-1Zm9 0h5a1 1 0 0 1 1 1v5a1 1 0 0 1-1 1h-5a1 1 0 0 1-1-1v-5a1 1 0 0 1 1-1ZM4.5 14.5h5a1 1 0 0 1 1 1v5a1 1 0 0 1-1 1h-5a1 1 0 0 1-1-1v-5a1 1 0 0 1 1-1Zm9 0h5a1 1 0 0 1 1 1v5a1 1 0 0 1-1 1h-5a1 1 0 0 1-1-1v-5a1 1 0 0 1 1-1Z"
          />
          <path
            v-else-if="tab.icon === 'cart'"
            d="M6.5 6h14l-1.8 9.2H8.3L6.5 6Zm0 0-1.2-2.5H3M9.5 20.2a1.2 1.2 0 1 0 0-2.4 1.2 1.2 0 0 0 0 2.4Zm8 0a1.2 1.2 0 1 0 0-2.4 1.2 1.2 0 0 0 0 2.4Z"
          />
          <path
            v-else
            d="M12 11.2a3.7 3.7 0 1 0-3.7-3.7 3.7 3.7 0 0 0 3.7 3.7Zm0 2.1c-3.72 0-6.8 1.88-6.8 4.2v.9h13.6v-.9c0-2.32-3.08-4.2-6.8-4.2Z"
          />
        </svg>
        <span v-if="tab.badge" class="tab-badge">{{ formatBadge(tab.badge) }}</span>
      </span>
      <span class="tab-label">{{ tab.label }}</span>
      <span v-if="isActive(tab)" class="tab-indicator" />
    </router-link>
  </nav>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const props = defineProps({
  cartCount: {
    type: Number,
    default: 0
  }
})

const route = useRoute()

const tabs = computed(() => [
  { path: '/', label: '首页', icon: 'home' },
  { path: '/category', label: '分类', icon: 'category' },
  { path: '/cart', label: '购物车', icon: 'cart', badge: props.cartCount },
  { path: '/my', label: '我的', icon: 'user' }
])

function isActive(tab) {
  const path = route.path
  if (tab.path === '/') return path === '/'
  if (tab.path === '/my') {
    return path.startsWith('/my')
      || path.startsWith('/profile')
      || path.startsWith('/address')
      || path.startsWith('/favorites')
      || path === '/login'
      || path === '/register'
  }
  return path === tab.path
}

function formatBadge(count) {
  if (count > 99) return '99+'
  return String(count)
}
</script>

<style scoped>
.tab-bar {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 100;
  display: flex;
  align-items: stretch;
  padding: 6px 8px calc(6px + env(safe-area-inset-bottom, 0px));
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border-top: 1px solid rgba(235, 237, 240, 0.9);
  box-shadow: 0 -6px 24px rgba(25, 137, 250, 0.08);
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 2px;
  padding: 4px 0;
  color: #969799;
  transition: color 0.2s ease, transform 0.15s ease;
  -webkit-tap-highlight-color: transparent;
}

.tab-item:active {
  transform: scale(0.96);
}

.tab-item.active {
  color: #1989fa;
}

.tab-icon-wrap {
  position: relative;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  transition: background 0.2s ease;
}

.tab-icon-wrap.is-active {
  background: linear-gradient(135deg, rgba(25, 137, 250, 0.14), rgba(79, 195, 247, 0.18));
}

.tab-icon {
  width: 22px;
  height: 22px;
  fill: none;
  stroke: currentColor;
  stroke-width: 1.8;
  stroke-linecap: round;
  stroke-linejoin: round;
  transition: transform 0.2s ease;
}

.tab-item.active .tab-icon {
  transform: translateY(-1px) scale(1.06);
}

.tab-label {
  font-size: 10px;
  line-height: 1.2;
  font-weight: 500;
  letter-spacing: 0.2px;
}

.tab-item.active .tab-label {
  font-weight: 600;
}

.tab-indicator {
  width: 18px;
  height: 3px;
  border-radius: 2px;
  background: linear-gradient(90deg, #1989fa, #4fc3f7);
  margin-top: 1px;
}

.tab-badge {
  position: absolute;
  top: -2px;
  right: -8px;
  min-width: 16px;
  height: 16px;
  padding: 0 4px;
  border-radius: 8px;
  background: linear-gradient(135deg, #ff6034, #ee0a24);
  color: #fff;
  font-size: 10px;
  font-weight: 600;
  line-height: 16px;
  text-align: center;
  border: 1.5px solid #fff;
  box-shadow: 0 2px 6px rgba(238, 10, 36, 0.35);
}
</style>

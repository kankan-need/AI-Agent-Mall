<template>
  <div class="layout">
    <aside class="sidebar">
      <div class="logo">Learn Mall</div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#001529"
        text-color="rgba(255, 255, 255, 0.82)"
        active-text-color="#ffffff"
      >
        <template v-for="route in menuRoutes" :key="route.path">
          <el-sub-menu v-if="route.children && route.children.length" :index="route.path">
            <template #title>{{ route.meta?.title || route.name }}</template>
            <el-menu-item
              v-for="child in route.children"
              :key="child.path"
              :index="resolvePath(route.path, child.path)"
            >
              {{ child.meta?.title || child.name }}
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item v-else :index="route.path">
            {{ route.meta?.title || route.name }}
          </el-menu-item>
        </template>
      </el-menu>
    </aside>
    <section class="main">
      <header class="header">
        <span>{{ userStore.name || '管理员' }}</span>
        <el-button link type="primary" @click="handleLogout">退出</el-button>
      </header>
      <main class="content">
        <router-view />
      </main>
    </section>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { usePermissionStore } from '@/stores/permission'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const permissionStore = usePermissionStore()

const menuRoutes = computed(() => permissionStore.routes.filter(item => !item.hidden && item.path !== '/login'))
const activeMenu = computed(() => route.path)

function resolvePath(parentPath, childPath) {
  if (childPath.startsWith('/')) {
    return childPath
  }
  const base = parentPath.endsWith('/') ? parentPath.slice(0, -1) : parentPath
  return `${base}/${childPath}`.replace('//', '/')
}

async function handleLogout() {
  await userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
}
.sidebar {
  width: 220px;
  background: #001529;
  color: #fff;
  flex-shrink: 0;
}
.logo {
  height: 56px;
  line-height: 56px;
  text-align: center;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 0.4px;
  color: #ffffff;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}
.sidebar :deep(.el-menu) {
  border-right: none;
  background: transparent;
  padding: 8px 0 16px;
  --el-menu-bg-color: transparent;
  --el-menu-text-color: rgba(255, 255, 255, 0.82);
  --el-menu-hover-text-color: #ffffff;
  --el-menu-active-color: #ffffff;
  --el-menu-hover-bg-color: rgba(255, 255, 255, 0.08);
  --el-menu-item-height: 48px;
}
.sidebar :deep(.el-menu-item),
.sidebar :deep(.el-sub-menu__title) {
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.2px;
  margin: 2px 8px;
  border-radius: 8px;
  height: 44px;
  line-height: 44px;
}
.sidebar :deep(.el-sub-menu__title) {
  color: rgba(255, 255, 255, 0.88) !important;
}
.sidebar :deep(.el-menu-item) {
  color: rgba(255, 255, 255, 0.82);
}
.sidebar :deep(.el-menu-item:hover),
.sidebar :deep(.el-sub-menu__title:hover) {
  color: #ffffff !important;
  background-color: rgba(255, 255, 255, 0.08) !important;
}
.sidebar :deep(.el-menu-item.is-active) {
  color: #ffffff !important;
  font-weight: 600;
  background: linear-gradient(90deg, rgba(64, 158, 255, 0.32), rgba(64, 158, 255, 0.08)) !important;
}
.sidebar :deep(.el-sub-menu .el-menu) {
  background: rgba(0, 0, 0, 0.15) !important;
  margin: 0 8px 4px;
  border-radius: 8px;
  padding: 4px 0;
}
.sidebar :deep(.el-sub-menu .el-menu-item) {
  min-width: auto;
  margin: 2px 6px;
  padding-left: 40px !important;
  font-size: 13px;
  font-weight: 400;
  color: rgba(255, 255, 255, 0.72) !important;
  height: 40px;
  line-height: 40px;
}
.sidebar :deep(.el-sub-menu .el-menu-item.is-active) {
  color: #ffffff !important;
  font-weight: 600;
  background: rgba(64, 158, 255, 0.22) !important;
}
.sidebar :deep(.el-sub-menu__icon-arrow) {
  color: rgba(255, 255, 255, 0.55);
}
.main {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.header {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 12px;
  padding: 0 20px;
  border-bottom: 1px solid #eee;
}
.content {
  padding: 20px;
}
</style>

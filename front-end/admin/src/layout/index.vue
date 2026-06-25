<template>
  <div class="layout">
    <aside class="sidebar">
      <div class="logo">Learn Mall</div>
      <el-menu :default-active="activeMenu" router>
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
}
.logo {
  height: 56px;
  line-height: 56px;
  text-align: center;
  font-weight: 600;
}
.sidebar :deep(.el-menu) {
  border-right: none;
  background: transparent;
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

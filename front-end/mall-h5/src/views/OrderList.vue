<template>
  <div class="page orders">
    <div class="tabs">
      <button
        v-for="tab in tabs"
        :key="tab.key"
        :class="{ active: activeTab === tab.key }"
        @click="changeTab(tab.key)"
      >
        {{ tab.label }}
      </button>
    </div>

    <div v-if="loading" class="empty card">加载中...</div>
    <div v-else-if="list.length" class="list">
      <div v-for="order in list" :key="order.orderId" class="item card" @click="goDetail(order)">
        <div class="head">
          <span>{{ order.shopName }}</span>
          <span :class="statusClass(order.status)">{{ displayStatus(order) }}</span>
        </div>
        <div v-for="item in (order.orderItems || []).slice(0, 2)" :key="item.orderItemId" class="goods">
          <img :src="item.pic" alt="" />
          <div class="info">
            <div class="name">{{ item.spuName }}</div>
            <div class="meta">
              <span>¥{{ formatPrice(item.price) }}</span>
              <span>x{{ item.count }}</span>
            </div>
          </div>
        </div>
        <div class="foot">
          <span>共 {{ order.allCount }} 件</span>
          <span>合计 <strong>¥{{ formatPrice(order.total) }}</strong></span>
        </div>
        <div class="actions" @click.stop>
          <button v-if="order.status === 1" class="btn-primary small" @click="goPay(order)">去支付</button>
          <button v-if="order.status === 1" class="btn-plain small" @click="cancel(order)">取消</button>
        </div>
      </div>
    </div>
    <div v-else class="empty card">{{ emptyText }}</div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { cancelOrder, listOrders } from '@/api/order'
import { formatPrice } from '@/utils/price'

const route = useRoute()
const router = useRouter()

const tabs = [
  { label: '全部', key: 'all', status: null },
  { label: '待付款', key: 'unpay', status: 1 },
  { label: '待发货', key: 'unship', status: 2 },
  { label: '待收货', key: 'receive', virtual: true },
  { label: '退款/退货', key: 'refund', virtual: true }
]

const activeTab = ref('all')
const list = ref([])
const loading = ref(false)

const currentTab = computed(() => tabs.find(tab => tab.key === activeTab.value) || tabs[0])

const emptyText = computed(() => {
  if (activeTab.value === 'receive') {
    return '暂无待收货订单（学习版暂未开通物流）'
  }
  if (activeTab.value === 'refund') {
    return '暂无退款/退货订单（学习版暂未开通售后）'
  }
  const label = currentTab.value.label
  return label === '全部' ? '暂无订单' : `暂无${label}订单`
})

function displayStatus(order) {
  if (order.status === 2 && activeTab.value === 'unship') return '待发货'
  return statusText(order.status)
}

function statusText(status) {
  const map = { 1: '待付款', 2: '待发货', 5: '已完成', 6: '已关闭' }
  return map[status] || '未知'
}

function statusClass(status) {
  if (status === 1) return 'warn'
  if (status === 2 || status === 5) return 'ok'
  return 'muted'
}

function resolveTabFromQuery() {
  const tab = route.query.tab
  if (tab && tabs.some(item => item.key === tab)) {
    activeTab.value = tab
  } else {
    activeTab.value = 'all'
  }
}

async function loadList() {
  const tab = currentTab.value
  if (tab.virtual) {
    list.value = []
    return
  }

  loading.value = true
  try {
    const params = { pageNum: 1, pageSize: 20 }
    if (tab.status != null) {
      params.status = tab.status
    }
    const data = await listOrders(params)
    list.value = data.list || []
  } finally {
    loading.value = false
  }
}

function changeTab(key) {
  activeTab.value = key
  router.replace({ path: '/order/list', query: key === 'all' ? {} : { tab: key } })
  loadList()
}

function goDetail(order) {
  router.push({ path: '/order/detail', query: { orderId: order.orderId } })
}

function goPay(order) {
  router.push({ path: '/order/pay', query: { orderId: order.orderId } })
}

async function cancel(order) {
  if (!confirm('确认取消该订单？')) return
  await cancelOrder(order.orderId)
  await loadList()
}

onMounted(() => {
  resolveTabFromQuery()
  loadList()
})

watch(() => route.query.tab, () => {
  resolveTabFromQuery()
  loadList()
})
</script>

<style scoped>
.orders {
  padding-bottom: 12px;
}
.tabs {
  display: flex;
  background: #fff;
  border-bottom: 1px solid #ebedf0;
  overflow-x: auto;
}
.tabs button {
  flex: 0 0 auto;
  min-width: 72px;
  border: none;
  background: transparent;
  padding: 12px 10px;
  font-size: 13px;
  white-space: nowrap;
}
.tabs button.active {
  color: #1989fa;
  font-weight: 600;
  border-bottom: 2px solid #1989fa;
}
.list {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.item {
  padding: 12px;
}
.head {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 13px;
}
.warn { color: #ee0a24; }
.ok { color: #07c160; }
.muted { color: #969799; }
.goods {
  display: flex;
  gap: 10px;
  margin-bottom: 8px;
}
.goods img {
  width: 56px;
  height: 56px;
  object-fit: cover;
  border-radius: 6px;
}
.info { flex: 1; }
.name { font-size: 14px; }
.meta {
  margin-top: 6px;
  display: flex;
  justify-content: space-between;
  color: #969799;
  font-size: 12px;
}
.foot {
  margin-top: 8px;
  display: flex;
  justify-content: space-between;
  font-size: 13px;
}
.foot strong {
  color: #ee0a24;
}
.actions {
  margin-top: 10px;
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}
.small {
  padding: 6px 14px;
  font-size: 13px;
}
.btn-plain {
  border: 1px solid #ebedf0;
  background: #fff;
  border-radius: 20px;
}
.empty {
  margin: 12px;
  padding: 24px;
  text-align: center;
  color: #969799;
  line-height: 1.6;
}
</style>

<template>
  <el-card>
    <template #header>
      <div class="header">
        <span>商品列表</span>
        <el-button type="primary" @click="goEdit()">新增商品</el-button>
      </div>
    </template>

    <el-form :inline="true" :model="query" class="query-form">
      <el-form-item label="商品名称">
        <el-input v-model="query.name" clearable placeholder="商品名称" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" clearable placeholder="全部">
          <el-option :value="1" label="上架" />
          <el-option :value="0" label="下架" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list" border>
      <el-table-column prop="spuId" label="ID" width="80" />
      <el-table-column label="主图" width="90">
        <template #default="{ row }">
          <el-image v-if="row.mainImgUrl" :src="row.mainImgUrl" style="width: 48px; height: 48px" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名称" min-width="180" />
      <el-table-column label="售价" width="100">
        <template #default="{ row }">¥{{ formatPrice(row.priceFee) }}</template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="80" />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="goEdit(row.spuId)">编辑</el-button>
          <el-button link type="primary" @click="toggleStatus(row)">
            {{ row.status === 1 ? '下架' : '上架' }}
          </el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="query.pageNum"
      v-model:page-size="query.pageSize"
      class="pager"
      layout="total, prev, pager, next"
      :total="total"
      @current-change="loadData"
    />
  </el-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { deleteSpu, pageSpu, updateSpuStatus } from '@/api/spu'
import { formatPrice } from '@/utils/price'

const router = useRouter()
const list = ref([])
const total = ref(0)
const query = ref({
  pageNum: 1,
  pageSize: 10,
  name: '',
  status: null
})

async function loadData() {
  const data = await pageSpu(query.value)
  list.value = data.list || []
  total.value = data.total || 0
}

function goEdit(spuId) {
  router.push({ path: '/product/spu/edit', query: spuId ? { spuId } : {} })
}

async function toggleStatus(row) {
  const status = row.status === 1 ? 0 : 1
  await updateSpuStatus(row.spuId, status)
  ElMessage.success('状态已更新')
  await loadData()
}

async function handleDelete(row) {
  await ElMessageBox.confirm(`确认删除商品「${row.name}」？`, '提示', { type: 'warning' })
  await deleteSpu(row.spuId)
  ElMessage.success('删除成功')
  await loadData()
}

onMounted(loadData)
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.query-form {
  margin-bottom: 12px;
}
.pager {
  margin-top: 16px;
  justify-content: flex-end;
}
</style>

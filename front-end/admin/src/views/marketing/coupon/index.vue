<template>
  <el-card>
    <template #header>
      <div class="header">
        <span>优惠券管理</span>
        <el-button type="primary" @click="openDialog()">新增优惠券</el-button>
      </div>
    </template>

    <el-table :data="list" border>
      <el-table-column prop="couponId" label="ID" width="80" />
      <el-table-column prop="name" label="优惠券名称" min-width="140" />
      <el-table-column label="优惠金额" width="120">
        <template #default="{ row }">¥{{ formatPrice(row.amount) }}</template>
      </el-table-column>
      <el-table-column label="最低消费" width="120">
        <template #default="{ row }">¥{{ formatPrice(row.minAmount) }}</template>
      </el-table-column>
      <el-table-column prop="validDays" label="有效天数" width="100" />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openDialog(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.couponId ? '编辑优惠券' : '新增优惠券'" width="480px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="优惠券名称">
          <el-input v-model="form.name" placeholder="如：新人专享券" />
        </el-form-item>
        <el-form-item label="优惠金额(元)">
          <el-input-number v-model="form.amountYuan" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="最低消费(元)">
          <el-input-number v-model="form.minAmountYuan" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="有效天数">
          <el-input-number v-model="form.validDays" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listCoupons, saveCoupon, updateCoupon, deleteCoupon } from '@/api/coupon'
import { formatPrice } from '@/utils/price'

const list = ref([])
const dialogVisible = ref(false)
const saving = ref(false)
const form = ref({
  couponId: null,
  name: '',
  amountYuan: 0,
  minAmountYuan: 0,
  validDays: 7,
  status: 1
})

async function loadData() {
  list.value = await listCoupons()
}

function openDialog(row) {
  if (row) {
    form.value = {
      couponId: row.couponId,
      name: row.name,
      amountYuan: (row.amount / 100).toFixed(2),
      minAmountYuan: (row.minAmount / 100).toFixed(2),
      validDays: row.validDays,
      status: row.status
    }
  } else {
    form.value = { couponId: null, name: '', amountYuan: 0, minAmountYuan: 0, validDays: 7, status: 1 }
  }
  dialogVisible.value = true
}

async function handleSave() {
  saving.value = true
  try {
    const data = {
      name: form.value.name,
      amount: Math.round(form.value.amountYuan * 100),
      minAmount: Math.round(form.value.minAmountYuan * 100),
      validDays: form.value.validDays,
      status: form.value.status
    }
    if (form.value.couponId) {
      data.couponId = form.value.couponId
      await updateCoupon(data)
    } else {
      await saveCoupon(data)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    await loadData()
  } finally {
    saving.value = false
  }
}

async function handleDelete(row) {
  await ElMessageBox.confirm(`确认删除优惠券「${row.name}」？`, '提示', { type: 'warning' })
  await deleteCoupon(row.couponId)
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
</style>
